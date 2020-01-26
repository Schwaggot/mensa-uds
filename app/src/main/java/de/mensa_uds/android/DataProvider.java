package de.mensa_uds.android;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import de.mensa_uds.android.data.Day;
import de.mensa_uds.android.data.Menu;
import de.mensa_uds.android.utils.DateValidater;

public class DataProvider {

    private static DataProvider instance = null;
    private Gson gson;

    private Menu sbMenu;
    private Menu homMenu;
    private String sbOpeningTimes;
    private String homOpeningTimes;

    private String activeMensa;
    private Menu activeMenu;
    private String activeOpeningTimes;

    public Menu getActiveMenu() {
        return activeMenu;
    }

    public String getActiveOpeningTimes() {
        return activeOpeningTimes;
    }

    private Menu getSBMenu() {
        return sbMenu;
    }

    private Menu getHomMenu() {
        return homMenu;
    }

    private String getSbOpeningTimes() {
        return sbOpeningTimes;
    }

    private String getHomOpeningTimes() {
        return homOpeningTimes;
    }

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    private DataProvider() {
        gson = new Gson();
    }

    public static DataProvider getInstance() {
        if (instance == null) {
            instance = new DataProvider();
        }
        return instance;
    }

    public void loadMenuSBFromJSON(String jsonString) {
        try {
            lock.writeLock().lock();
            sbMenu = gson.fromJson(jsonString, Menu.class);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void loadMenuHOMFromJSON(String jsonString) {
        try {
            lock.writeLock().lock();
            homMenu = gson.fromJson(jsonString, Menu.class);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setOpeningTimesSB(String times) {
        try {
            lock.writeLock().lock();
            sbOpeningTimes = times;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setOpeningTimesHOM(String times) {
        try {
            lock.writeLock().lock();
            homOpeningTimes = times;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setActiveMensa(String mensa) {
        this.activeMensa = mensa;

        switch (activeMensa) {
            case "sb":
                activeMenu = getSBMenu();
                activeOpeningTimes = getSbOpeningTimes();
                break;
            case "hom":
                activeMenu = getHomMenu();
                activeOpeningTimes = getHomOpeningTimes();
                break;
            default:
                throw new IllegalArgumentException("activeMensa is: " + mensa);
        }

        if (activeMenu != null) {
            Day[] allDays = activeMenu.getDays();
            ArrayList<Day> validDays = new ArrayList<>();
            for (Day d : allDays) {
                if (DateValidater.isTimeStampValid(Long.valueOf(d.getTimestamp()))) {
                    validDays.add(d);
                }
            }

            Day[] daysArr = new Day[validDays.size()];
            validDays.toArray(daysArr);
            activeMenu.setDays(daysArr);
        }
    }

    public String getActiveMensa() {
        return activeMensa;
    }

    public void updateData() {
        setActiveMensa(getActiveMensa());
    }

    public boolean isDataLoaded() {
        try {
            lock.readLock().lock();
            return sbMenu != null && homMenu != null && sbOpeningTimes != null && homOpeningTimes != null;
        } finally {
            lock.readLock().unlock();
        }
    }
}
