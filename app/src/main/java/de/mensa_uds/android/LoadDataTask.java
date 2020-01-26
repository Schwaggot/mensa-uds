package de.mensa_uds.android;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import de.mensa_uds.android.data.MenuStatus;
import de.mensa_uds.android.data.OpeningTimesStatus;

public class LoadDataTask extends AsyncTask<String, Void, Integer> {

    private static final String CACHE_NAME = "de.mensaar.android:Cache";
    private static final String TAG = "LoadDataTask";
    //public static final String API_URL = "http://192.168.0.100:8081/mensaar/api/v1/";
    public static final String API_URL = "http://mensa-uds.de:8081/mensaar/api/v1/";

    private RequestQueue queue;
    private MainActivity context;
    private boolean isOffline;

    LoadDataTask(MainActivity context, boolean isOffline) {
        super();
        this.context = context;
        this.isOffline = isOffline;
        this.queue = Volley.newRequestQueue(context);
    }

    @Override
    protected void onPreExecute() {
        context.setProgressBarIndeterminateVisibility(true);
    }

    @Override
    protected Integer doInBackground(String... params) {
        try {
            downloadMenuStatus();
            downloadOpeningTimesStatus();
            return 1;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    protected void onPostExecute(Integer result) {
        context.setProgressBarIndeterminateVisibility(false);

        if (result == 1) {
            DataProvider.getInstance().updateData();
            context.dataLoaded();
        } else {
            throw new IllegalStateException();
        }
    }

    private void downloadOpeningTimesStatus() throws ExecutionException, InterruptedException {
        String url = API_URL + "openingtimes";
        RequestFuture<JSONObject> openingTimesStatusFuture = RequestFuture.newFuture();
        JsonObjectRequest openingTimesStatusRequest = new JsonObjectRequest(Request.Method.GET, url, null, openingTimesStatusFuture, openingTimesStatusFuture);

        queue.add(openingTimesStatusRequest);

        JSONObject response = openingTimesStatusFuture.get();
        onOpeningTimesStatusLoaded(response.toString());
    }

    private void downloadMenuStatus() throws ExecutionException, InterruptedException {
        String url = API_URL + "menu";
        RequestFuture<JSONObject> menuStatusFuture = RequestFuture.newFuture();
        JsonObjectRequest menuStatusRequest = new JsonObjectRequest(Request.Method.GET, url, null, menuStatusFuture, menuStatusFuture);

        queue.add(menuStatusRequest);

        JSONObject response = menuStatusFuture.get();
        onMenuStatusLoaded(response.toString());
    }

    private void onMenuStatusLoaded(String response) throws ExecutionException, InterruptedException {
        Gson gson = new Gson();
        MenuStatus onlineStatus = gson.fromJson(response, de.mensa_uds.android.data.MenuStatus.class);

        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        String offlineStatusStr = cache.getString("menuStatus", null);

        MenuStatus offlineStatus = new MenuStatus();

        if (offlineStatusStr == null) {
            offlineStatus.setTimestamp("-1");
            offlineStatus.setFileTimestampSB("-1");
            offlineStatus.setFileTimestampHOM("-1");
        } else {
            offlineStatus = gson.fromJson(offlineStatusStr, de.mensa_uds.android.data.MenuStatus.class);
        }

        long onlineTimestamp = Long.valueOf(onlineStatus.getTimestamp());
        long offlineTimestamp = Long.valueOf(offlineStatus.getTimestamp());

        if (onlineTimestamp > offlineTimestamp) {
            Log.i(TAG, "server data is newer, downloading from server");
            downloadMenuFromServer();
            saveMenuStatus(response);
        } else {
            Log.i(TAG, "cache data is up-to-date, loading data from cache");
            loadMenuFromCache();
        }
    }

    private void downloadMenuFromServer() throws ExecutionException, InterruptedException {

        String url = API_URL + "menu/sb";
        RequestFuture<JSONObject> sbFuture = RequestFuture.newFuture();
        JsonObjectRequest sbRequest = new JsonObjectRequest(Request.Method.GET, url, null, sbFuture, sbFuture);

        queue.add(sbRequest);

        JSONObject sbResponse = sbFuture.get();
        DataProvider.getInstance().loadMenuSBFromJSON(sbResponse.toString());
        saveSBMenu(sbResponse.toString());

        url = API_URL + "menu/hom";
        RequestFuture<JSONObject> homFuture = RequestFuture.newFuture();
        JsonObjectRequest homRequest = new JsonObjectRequest(Request.Method.GET, url, null, homFuture, homFuture);

        queue.add(homRequest);

        JSONObject homResponse = homFuture.get();
        DataProvider.getInstance().loadMenuHOMFromJSON(homResponse.toString());
        saveHOMMenu(homResponse.toString());
    }

    private void loadMenuFromCache() {
        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        String sbMenuStr = cache.getString("sbMenu", null);
        String homMenuStr = cache.getString("homMenu", null);

        if (sbMenuStr == null || homMenuStr == null) {
            Log.e(TAG, "loadMenuFromCache() -> sbMenuStr == null || homMenuStr == null");
            throw new IllegalStateException();
        } else {
            DataProvider.getInstance().loadMenuSBFromJSON(sbMenuStr);
            DataProvider.getInstance().loadMenuHOMFromJSON(homMenuStr);
        }
    }

    private void saveMenuStatus(String menuStatus) {
        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        SharedPreferences.Editor editor = cache.edit();
        editor.putString("menuStatus", menuStatus);
        editor.apply();
    }

    private void saveSBMenu(String sbMenu) {
        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        SharedPreferences.Editor editor = cache.edit();
        editor.putString("sbMenu", sbMenu);
        editor.apply();
    }

    private void saveHOMMenu(String homMenu) {
        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        SharedPreferences.Editor editor = cache.edit();
        editor.putString("homMenu", homMenu);
        editor.apply();
    }

    private void onOpeningTimesStatusLoaded(String response) throws ExecutionException, InterruptedException {
        Gson gson = new Gson();
        OpeningTimesStatus onlineStatus = gson.fromJson(response, de.mensa_uds.android.data.OpeningTimesStatus.class);

        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        String offlineStatusStr = cache.getString("openingTimesStatus", null);

        OpeningTimesStatus offlineStatus = new OpeningTimesStatus();

        if (offlineStatusStr == null) {
            offlineStatus.setTimestamp("-1");
            offlineStatus.setTimestampSB("-1");
            offlineStatus.setTimestampHOM("-1");
        } else {
            offlineStatus = gson.fromJson(offlineStatusStr, de.mensa_uds.android.data.OpeningTimesStatus.class);
        }

        long onlineTimestamp = Long.valueOf(onlineStatus.getTimestamp());
        long offlineTimestamp = Long.valueOf(offlineStatus.getTimestamp());

        if (onlineTimestamp > offlineTimestamp) {
            Log.i(TAG, "server data is newer, downloading from server");
            downloadOpeningTimesFromServer();
            saveOpeningTimesStatus(response);
        } else {
            Log.i(TAG, "cache data is up-to-date, loading data from cache");
            loadOpeningTimesFromCache();
        }
    }

    private void downloadOpeningTimesFromServer() throws ExecutionException, InterruptedException {
        String url = API_URL + "openingtimes/sb";
        RequestFuture<String> sbFuture = RequestFuture.newFuture();
        StringRequest sbRequest = new StringRequest(Request.Method.GET, url, sbFuture, sbFuture);

        queue.add(sbRequest);

        String sbResponse = sbFuture.get();
        DataProvider.getInstance().setOpeningTimesSB(sbResponse);
        saveSBOpeningTimes(sbResponse);

        url = API_URL + "openingtimes/hom";
        RequestFuture<String> homFuture = RequestFuture.newFuture();
        StringRequest homRequest = new StringRequest(Request.Method.GET, url, homFuture, homFuture);

        queue.add(homRequest);

        String homResponse = homFuture.get();
        DataProvider.getInstance().setOpeningTimesHOM(homResponse);
        saveHOMOpeningTimes(homResponse);
    }

    private void loadOpeningTimesFromCache() {
        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        String sbOpeningTimesStr = cache.getString("sbOpeningTimes", null);
        String homOpeningTimesStr = cache.getString("homOpeningTimes", null);

        if (sbOpeningTimesStr == null || homOpeningTimesStr == null) {
            Log.e(TAG, "loadOpeningTimesFromCache() -> sbOpeningTimesStr == null || homOpeningTimesStr == null");
            throw new IllegalStateException();
        } else {
            DataProvider.getInstance().setOpeningTimesSB(sbOpeningTimesStr);
            DataProvider.getInstance().setOpeningTimesHOM(homOpeningTimesStr);
        }
    }

    private void saveOpeningTimesStatus(String openingTimesStatus) {
        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        SharedPreferences.Editor editor = cache.edit();
        editor.putString("openingTimesStatus", openingTimesStatus);
        editor.apply();
    }

    private void saveSBOpeningTimes(String sbOpeningTimes) {
        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        SharedPreferences.Editor editor = cache.edit();
        editor.putString("sbOpeningTimes", sbOpeningTimes);
        editor.apply();
    }

    private void saveHOMOpeningTimes(String homOpeningTimes) {
        SharedPreferences cache = context.getSharedPreferences(CACHE_NAME, 0);
        SharedPreferences.Editor editor = cache.edit();
        editor.putString("homOpeningTimes", homOpeningTimes);
        editor.apply();
    }
}
