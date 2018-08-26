package io.phobotic.message;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jonathan Nelson on 4/19/18.
 */
public class RegisterRequestMessage extends Message {
    @SerializedName("printer")
    private Printer printer;

    public RegisterRequestMessage(String uuid, Printer printer) {
        super(uuid, Type.REGISTER_REQUEST);
        this.printer = printer;
    }
}
