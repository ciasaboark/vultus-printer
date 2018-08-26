package io.phobotic;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sun.istack.internal.NotNull;
import io.phobotic.generator.LabelGenerator;
import io.phobotic.generator.product.BasicProductLabelGenerator;
import io.phobotic.label.DymoSmallHorizontalLabel;
import io.phobotic.label.PrintableLabel;
import io.phobotic.led.LedManager;
import io.phobotic.message.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.UUID;

public class Main {
    private static final String DEFAULT_SERVER_ADDRESS = "localhost";
    private static String serverAddress = DEFAULT_SERVER_ADDRESS;

    private static final String DEFAULT_PRINTER_NAME = "A Vultus Printer";
    private static String printerName = DEFAULT_PRINTER_NAME;

    private static final int DEFAULT_PORT = 8090;
    private static int port = DEFAULT_PORT;

    private static final long SOCKET_OPEN_FAIL_PAUSE_MS = 5000;
    private static boolean isRunning = true;
    private static SocketChannel client;
    private static StringBuilder buffer = new StringBuilder();
    private static Deque<Message> outgoingMessages = new ArrayDeque<>();
    private static LedManager ledManager;

    public static void main(String[] args) throws Exception {

        if (args.length > 0) {
            printerName = args[0];
            if (printerName.length() == 0) {
                throw new IllegalArgumentException("Printer name must have a length > 0");
            }
        } else {
            System.out.println("No printer name provided, using default: " + DEFAULT_PRINTER_NAME);
        }

        if (args.length > 1) {
            serverAddress = args[1];
            if (serverAddress.length() == 0) {
                throw new IllegalArgumentException("Server name must have a length > 0");
            }
        } else {
            System.out.println("No server address provided.  Will attempt to connect to: " + DEFAULT_SERVER_ADDRESS);
        }


        if (args.length > 2) {
            try {
                port = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Unable to parse port number: " + e.getMessage());
            }
        } else {
            System.out.println("No port number provided.  Will attempt to connect using port: " + DEFAULT_PORT);
        }



        init();
        mainLoop();
    }

    private static void mainLoop() {
        while (isRunning) {
            try {
                registerIfNeeded();
                readBuffer();
                processBuffer();
                sendMessages();

                Thread.sleep(5000);
            } catch (IOException e) {
                registerIfNeeded();
            } catch (InterruptedException e) {
                isRunning = false;
            }
        }
    }

    private static void checkConnection() {
        if (!client.isConnected()) {

        }
    }

    private static void sendMessages() throws IOException, InterruptedException {
        if (client.isConnected()) {
            Iterator<Message> it = outgoingMessages.iterator();
            while (it.hasNext()) {
                Message msg = it.next();
                it.remove();
                Gson gson = new Gson();
                String json = gson.toJson(msg);

                String messageString = json + "\n";
                byte[] messageBytes = messageString.getBytes();
                ByteBuffer buffer = ByteBuffer.wrap(messageBytes);

                System.out.println(String.format("Sending [%s]: '%s'", msg.getClass().getSimpleName(), json));
                client.write(buffer);
                buffer.clear();
            }
        }
    }

    /**
     * Check if the buffer data has a newline.  If so try to process each line
     */
    private static void processBuffer() {
        String str = buffer.toString();
        if (str.contains("\n")) {
            System.out.println("Found at least one full line in buffer");

            //only process the first full line.  Any additional lines will be picked up in
            //+ subsequent passes
            String[] parts = str.split("\n", 2);
            String msg = parts[0];
            System.out.println("Read line: " + msg);

            //replace the buffer with the remainder
            String tail = parts[1];
            if (tail == null) tail = "";
            buffer = new StringBuilder(tail);
            processMessageString(msg);
        }
    }

    private static void processMessageString(String messageString) {
        try {
            //read the message string as a generic JSON object first.
            JsonElement root = new JsonParser().parse(messageString);
            String typeString = root.getAsJsonObject().get("type").getAsString();
            Message.Type type = Message.Type.valueOf(typeString);
            Gson gson = new Gson();
            switch (type) {
                case PRINT_REQUEST:
                    PrintRequestMessage printRequest = gson.fromJson(messageString, PrintRequestMessage.class);
                    processPrintRequest(printRequest);
                    break;
                case REGISTER_RESPONSE:
                    RegisterResponseMessage registerResponseMessage = gson.fromJson(messageString, RegisterResponseMessage.class);
                    processRegisterResponse(registerResponseMessage);
                    break;
                default:
                    System.err.printf("Unknown message type: %s, skipping this message\n", typeString);
            }
        } catch (Exception e) {
            System.err.printf("Unknown Message format: %s\n", messageString);
        }
    }

    private static void printStartupMessage() {
        System.out.println("Printing startup message");
        LabelGenerator labelGenerator = new BasicProductLabelGenerator();
        PrintableLabel printableLabel = new DymoSmallHorizontalLabel();
        PrintRequest printRequest = new PrintRequest()
                .setChill(true)
                .setDescription("Connected to server")
                .setForkRequired(true)
                .setProductId(String.format("%s:%d", serverAddress, port))
                .setWarehouse("99");
//        PrintManager.getInstance().print(labelGenerator, printRequest, printableLabel);
    }

    private static RegisterRequestMessage getRegisterMessage() {
        String messageUuid = UUID.randomUUID().toString();
        RegisterRequestMessage message = new RegisterRequestMessage(messageUuid, new Printer(UUID.randomUUID().toString(),
                printerName));
        return message;
    }

    private static void processRegisterResponse(@NotNull RegisterResponseMessage message) {
        //received a registration response from the server
        //todo
    }

    private static void processPrintRequest(@NotNull PrintRequestMessage message) {
        PrintResponseMessage responseMessage = null;
        try {
            PrintRequest printRequest = message.printRequest;
            System.out.printf("Received request to print label for product '%s'\n",
                    printRequest.getProductId());
            LabelGenerator labelGenerator = new BasicProductLabelGenerator();
            PrintableLabel printableLabel = new DymoSmallHorizontalLabel();
            PrintManager.getInstance().print(labelGenerator, printRequest, printableLabel);
            responseMessage = new PrintResponseMessage(message.uuid, false,
                    "Label printed successfully");
        } catch (Exception e) {
            //need to be careful of malformatted messages
            String productID = null;
            if (message != null && message.printRequest != null && message.printRequest.getProductId() != null) {
                productID = message.printRequest.getProductId();
            }

            String msgString = String.format("Exception: [%s], unable to print label for product '%s': %s",
                    e.getClass().getSimpleName(),
                    productID,
                    e.getMessage());

            responseMessage = new PrintResponseMessage(message.uuid, true, msgString);
        }

        if (responseMessage != null) {
            outgoingMessages.addFirst(responseMessage);
        }
    }

    private static void readBuffer() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        int numRead = -1;
        numRead = client.read(buf);

        if (numRead == -1) {
            System.out.println("Connection closed by server");
            client.close();
        } else if (numRead > 0){
            byte[] data = new byte[numRead];
            System.arraycopy(buf.array(), 0, data, 0, numRead);
            String str = new String(data);
            buffer.append(str);
            System.out.println(String.format("appending buffer -> %s", str));
        }
    }

//    private static void readBufferNew() throws IOException {
//        byte[] resultBuff = new byte[0];
//        byte[] buff = new byte[1024];
//        int k = -1;
//        while((k = sock.getInputStream().read(buff, 0, buff.length)) > -1) {
//            byte[] tbuff = new byte[resultBuff.length + k]; // temp buffer size = bytes already read + bytes last read
//            System.arraycopy(resultBuff, 0, tbuff, 0, resultBuff.length); // copy previous bytes
//            System.arraycopy(buff, 0, tbuff, resultBuff.length, k);  // copy current lot
//            resultBuff = tbuff; // call the temp buffer as your result buff
//        }
//        System.out.println(resultBuff.length + " bytes read.");
//        return resultBuff;
//    }

    /**
     * Perform any first-run initialization
     */
    private static void init() {
        initLeds();
        connect();
        register();
        printStartupMessage();
    }

    private static void initLeds() {
        ledManager = LedManager.getInstance();
    }



    private static void registerIfNeeded() {
        if (client == null || !client.isConnected()) {
            connect();
            register();
        }
    }

    private static void register() {
        System.out.println("Sendng registration message");
        RegisterRequestMessage registerMessage = getRegisterMessage();
        outgoingMessages.addFirst(registerMessage);
    }

    /**
     * Open a socket to the host.  Controll will only be returned to the caller once a connection is
     * established
     *
     */
    private static void connect() {
        int tryCount = 0;
        while (client == null || !client.isConnected()) {
            try {
                System.out.println(String.format("Connecting to server (try %d)", tryCount));
                tryConnectSocket();
            } catch (IOException e) {
//                e.printStackTrace();
                System.out.println("Unable to open socket to server: " + e.getMessage());
                blockingPause(SOCKET_OPEN_FAIL_PAUSE_MS);
            }

            tryCount++;
        }

    }

    private static void blockingPause(long pauseMiliseconds) {
        long pauseSlice = pauseMiliseconds / 5;
        //pause for a bit
        System.out.print("Waiting ");
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(pauseSlice);
                System.out.print(".");
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
        System.out.println();
    }

    private static void tryConnectSocket() throws IOException {
        System.out.println(String.format("Trying to open connection to %s:%d", serverAddress, port));
        InetSocketAddress hostAddress = new InetSocketAddress(serverAddress, port);
        client = SocketChannel.open(hostAddress);
        client.configureBlocking(false);
        System.out.println("Socket opened");
    }
}

