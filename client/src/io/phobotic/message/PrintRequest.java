package io.phobotic.message;


import io.phobotic.label.Printable;

/**
 * Created by Jonathan Nelson on 9/11/16.
 */
public class PrintRequest implements Printable {
    private String sessionId = "";
    private String description = "";
    private String warehouse = "";
    private String location = "";
    private String productId = "";
    private Boolean isFreezer = false;
    private Boolean isChill = false;
    private Boolean isDollarGeneral = false;
    private Boolean isForkRequired = false;
    private Long timestamp;

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return location;
    }

    public PrintRequest setLocation(String location) {
        this.location = location;
        return this;
    }

//    @JsonProperty("forkRequired")
    public boolean isForkRequired() {
        return isForkRequired;
    }

//    @JsonProperty("forkRequired")
    public PrintRequest setForkRequired(boolean forkRequired) {
        isForkRequired = forkRequired;
        return this;
    }

//    @JsonProperty("chill")
    public boolean isChill() {
        return isChill;
    }

//    @JsonProperty("chill")
    public PrintRequest setChill(boolean chill) {
        isChill = chill;
        return this;
    }

//    @JsonProperty("freezer")
    public boolean isFreezer() {
        return isFreezer;
    }

//    @JsonProperty("freezer")
    public PrintRequest setFreezer(boolean freezer) {
        isFreezer = freezer;
        return this;
    }

//    @JsonProperty("dollarGeneral")
    public boolean isDollarGeneral() {
        return isDollarGeneral;
    }

//    @JsonProperty("dollarGeneral")
    public PrintRequest setDollarGeneral(boolean dg) {
        isDollarGeneral = dg;
        return this;
    }

    public String getProductId() {
        return productId;
    }

    public PrintRequest setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public PrintRequest setWarehouse(String warehouse) {
        this.warehouse = warehouse;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PrintRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public PrintRequest setSessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }
}
