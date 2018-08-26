package io.phobotic.message;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jonathan Nelson on 4/19/18.
 */
public class Printer implements Serializable {
    @SerializedName("uuid")
    private String uuid;

    @SerializedName("name")
    private String name;

    public Printer(String uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }
}
