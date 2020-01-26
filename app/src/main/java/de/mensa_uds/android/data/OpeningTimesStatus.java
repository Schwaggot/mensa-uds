package de.mensa_uds.android.data;

public class OpeningTimesStatus {

    private String timestamp;
    private String timestampSB;
    private String timestampHOM;

    public OpeningTimesStatus() {

    }

    public String getTimestampSB() {
        return timestampSB;
    }

    public void setTimestampSB(String timestampSB) {
        this.timestampSB = timestampSB;
    }

    public String getTimestampHOM() {
        return timestampHOM;
    }

    public void setTimestampHOM(String timestampHOM) {
        this.timestampHOM = timestampHOM;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

}
