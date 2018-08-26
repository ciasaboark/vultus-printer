package io.phobotic.message;

import com.google.gson.annotations.SerializedName;
import com.sun.istack.internal.NotNull;

import java.io.Serializable;

/**
 * Created by Jonathan Nelson on 4/19/18.
 * No methods are needed
 */
public class Message implements Serializable {
    @SerializedName("uuid")
    private String uuid;

    @SerializedName("type")
    private Type type;

    public Message(@NotNull String uuid, @NotNull Type type) {
        this.uuid = uuid;
        this.type = type;
    }

    public static enum Type {
        PRINT_REQUEST,
        PRINT_RESPONSE,
        REGISTER_REQUEST,
        REGISTER_RESPONSE
    }
}
