package io.phobotic.message;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jonathan Nelson on 4/19/18.
 * A message from the host requesting that a label be generated.
 */
public class PrintRequestMessage implements Serializable {
    /**
     * A unique identifier for this specific message
     */
    @SerializedName("uuid")
    public String uuid;

    /**
     * A {@link PrintRequest} holding all required information for label generation
     */
    @SerializedName("print_request")
    public PrintRequest printRequest;
}
