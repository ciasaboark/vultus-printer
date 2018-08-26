package io.phobotic.message;

import com.google.gson.annotations.SerializedName;
import com.sun.istack.internal.NotNull;

import java.io.Serializable;

/**
 * Created by Jonathan Nelson on 4/19/18.
 * An incoming message from the host.  This message is expected to be returned after a {@link RegisterRequestMessage}
 * has been sent to the host.  The client is not registered until a response with an appropriate originallUuid is
 * returned
 */
public class RegisterResponseMessage implements Serializable {
    @SerializedName("uuid")
    public String uuid;

    @SerializedName("original_message_uuid")
    public String originalUuid;

    @SerializedName("registered")
    public boolean isRegistered;

    @SerializedName("timeout")
    public long registerTimeout;

    @SerializedName("message")
    public String message;
}
