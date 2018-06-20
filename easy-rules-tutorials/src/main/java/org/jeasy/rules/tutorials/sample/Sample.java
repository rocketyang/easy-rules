package org.jeasy.rules.tutorials.sample;

/**
 * Created by yonching on 6/20/18.
 */
public class Sample {
    private Boolean isExist;
    private Boolean isAvailable;
    private Boolean inventory;
    private Boolean isRepetition;
    private String sampleId;

    public Sample() {
        this.isExist = false;
        this.isAvailable = false;
        this.inventory = false;
        this.inventory = true;
        this.sampleId = "";
    }

    public String getSampleId() {
        return sampleId;
    }

    public void setSampleId(String sampleId) {
        this.sampleId = sampleId;
    }



    public Boolean getExist() {
        return isExist;
    }

    public void setExist(Boolean exist) {
        isExist = exist;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }

    public void setAvailable(Boolean available) {
        isAvailable = available;
    }

    public Boolean getInventory() {
        return inventory;
    }

    public void setInventory(Boolean inventory) {
        this.inventory = inventory;
    }

    public Boolean getRepetition() {
        return isRepetition;
    }

    public void setRepetition(Boolean repetition) {
        isRepetition = repetition;
    }
}
