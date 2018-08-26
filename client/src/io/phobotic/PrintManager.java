package io.phobotic;


import com.sun.istack.internal.NotNull;
import io.phobotic.generator.LabelGenerator;

import io.phobotic.label.PrintableLabel;

import javax.print.PrintException;
import javax.print.PrintService;
import java.awt.*;
import java.awt.print.*;
import java.awt.print.Printable;

/**
 * Created by Jonathan Nelson on 2/18/16.
 */
public class PrintManager {
    private static PrinterJob sPrinterJob;
    private static PrintService sPrintService;
//    private static List<Product> sPrintedProducts = new ArrayList<>();
    private static PrintManager sInstance;

    private PrintManager() {
        sPrinterJob = PrinterJob.getPrinterJob();
        sPrintService = getPrintService();
    }

    private static PrintService getPrintService() {
        PrintService printService = null;
        for (PrintService service : PrinterJob.lookupPrintServices()) {
            if (service.getName().toUpperCase().contains("DYMO LabelWriter 450".toUpperCase())) {
                printService = service;
                break;
            }
        }

        return printService;
    }

    public static PrintManager getInstance() {
        if (sInstance == null) {
            sInstance = new PrintManager();
        }

        return sInstance;
    }

    public boolean print(@NotNull LabelGenerator generator,
                         @NotNull io.phobotic.label.Printable printable,
                         @NotNull PrintableLabel label) {
        boolean printed = false;
        PageFormat pf = sPrinterJob.defaultPage();
        Paper paper = pf.getPaper();

        double width = label.getLabelWidthPx();
        double height = label.getLabelHeightPx();
        paper.setSize(width, height);
        paper.setImageableArea(1, 1, width, height);

        pf.setOrientation(label.getOrientation());
        pf.setPaper(paper);
        PrintHandler ph = new PrintHandler(generator, printable);
        sPrinterJob.setPrintable(ph, pf);
        try {
            sPrinterJob.print();
            printed = true;
        } catch (PrinterException e) {
            System.err.println("Unable to print label for " + printable + "' on " + label.getClass().getName() +
                    " using " + generator.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected exception printing label for " + printable + "' on " + label.getClass().getName() +
                    " using " + generator.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return printed;
    }

    private class PrintHandler implements java.awt.print.Printable {
        private static final String LABEL_PATH = "/tmp/vultus/";
        private LabelGenerator generator;
        private io.phobotic.label.Printable printable;

        public PrintHandler(LabelGenerator generator, io.phobotic.label.Printable printable) {
            this.generator = generator;
            this.printable = printable;
        }

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            Graphics2D g2d = (Graphics2D) graphics;
            int result = Printable.NO_SUCH_PAGE;
            if (pageIndex >= 1) {
                System.out.println("skipping page beyond the first");
            } else {
                double width = pageFormat.getPaper().getWidth();
                double height = pageFormat.getPaper().getHeight();
                double imageableHeight = pageFormat.getImageableHeight();
                double imageableWidth = pageFormat.getImageableWidth();

                System.out.println("printing label with width: " + width + " height: " + height +
                        ", orientation: " + pageFormat.getOrientation());
                System.out.println("label imageable width: " + imageableWidth + " imageable height: " + imageableHeight);
                try {
                    generator.generate(g2d, pageFormat, printable);
                    System.out.println("generator wrote to Graphics2D successful");
//                    trySaveLabelGraphics(g2d);
                } catch (PrintException e) {
                    String errMsg = "Generator threw PrintException generating label for " +
                            printable.getClass().getName();
                    System.err.println(errMsg);
                    throw new PrinterException(errMsg);
                } catch (ClassCastException e) {
                    String errMsg = "Generator " + generator.getClass().getName() + " unable to handle printable of type " +
                            printable.getClass().getName();
                    System.err.println(errMsg);
                    throw new PrinterException(errMsg);
                }
                result = Printable.PAGE_EXISTS;
            }
            return result;
        }

//        private void trySaveLabelGraphics(Graphics2D g2d) {
//            String filename = LABEL_PATH + "label-" + System.currentTimeMillis() + ".png";
//            sContext.log("trying to save label graphics to file: " + filename);
//            try {
//                int width = (int) g2d.getClipBounds().getWidth();
//                int height = (int) g2d.getClipBounds().getHeight();
//                BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//                g2d.drawImage(bi, null, 0, 0);
//                File labelFile = new File(filename);
//                ImageIO.write(bi, "PNG", labelFile);
//            } catch (IOException e) {
//                sContext.log("unable to write label graphics file: " + filename + ": " + e.getMessage());
//                sContext.log(ExceptionUtils.getStackTrace(e));
//            }
//        }
    }

}
