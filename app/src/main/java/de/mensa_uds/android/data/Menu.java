package de.mensa_uds.android.data;

public class Menu {

    private String timestamp;
    private Day[] days;

    public Menu() {

    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Day[] getDays() {
        return days;
    }

    public void setDays(Day[] days) {
        this.days = days;
    }

}
