package de.mensa_uds.android.data;

public class MenuStatus {

    private String timestamp;
    private String fileTimestampSB;
    private String fileTimestampHOM;

    public MenuStatus() {

    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFileTimestampSB() {
        return fileTimestampSB;
    }

    public void setFileTimestampSB(String fileTimestampSB) {
        this.fileTimestampSB = fileTimestampSB;
    }

    public String getFileTimestampHOM() {
        return fileTimestampHOM;
    }

    public void setFileTimestampHOM(String fileTimestampHOM) {
        this.fileTimestampHOM = fileTimestampHOM;
    }


}
