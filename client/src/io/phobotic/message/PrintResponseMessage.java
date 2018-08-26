package io.phobotic.message;

import com.google.gson.annotations.SerializedName;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by Jonathan Nelson on 4/19/18.
 * A response message to be sent back to the host.  This message should be in response to the receipt of a
 * {@link PrintRequestMessage}.
 */
public class PrintResponseMessage extends Message {
    /**
     * The UUID field of the message this is in response to.
     */
    @SerializedName("original_message_uuid")
    private String originalUuid;

    @SerializedName("error")
    private boolean isError;

    @SerializedName("message")
    private String message;

    /**
     * Construct a new message
     * @param originalUuid the UUID of the message this is in response to
     * @param isError an indicator of whether the print job completed successfully.
     * @param message an optional message
     */
    public PrintResponseMessage(@NotNull String originalUuid, boolean isError,
                                @Nullable String message) {
        super(UUID.randomUUID().toString(), Type.PRINT_RESPONSE);

        if (originalUuid == null) {
            throw new IllegalArgumentException("Original UUID field must not be null");
        }

        this.originalUuid = originalUuid;
        this.isError = isError;
        this.message = message;
    }
}
