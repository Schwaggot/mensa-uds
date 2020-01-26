package de.mensa_uds.android.data;

public class Day {

    private String timestamp;
    private Meal[] meals;

    public Day() {

    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Meal[] getMeals() {
        return meals;
    }

    public void setMeals(Meal[] meals) {
        this.meals = meals;
    }

}
