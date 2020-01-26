package de.mensa_uds.android;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import de.mensa_uds.android.anim.ZoomOutPageTransformer;
import de.mensa_uds.android.utils.DateValidater;
import de.mensa_uds.android.widgets.PageFragment;
import redfox.android.mensa.R;

public class MainActivity extends FragmentActivity {

    private static String TAG = "MainActivity";
    public static MainActivity instance;

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        Log.i(TAG, "loading SharedPreferences");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String campusPref = sharedPref.getString("pref_key_campus", "sb");
        DataProvider.getInstance().setActiveMensa(campusPref);
        Log.i(TAG, "campusPref = " + campusPref);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        tryAgain();
    }

    private String activeMensaBeforePause;

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "PAUSE");
        activeMensaBeforePause = DataProvider.getInstance().getActiveMensa();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "RESUME");

        if (activeMensaBeforePause != null && !activeMensaBeforePause.equals(DataProvider.getInstance().getActiveMensa())) {
            if (DataProvider.getInstance().isDataLoaded()) {
                startActivity(getIntent());
                finish();
            }
        }
    }

    private View.OnClickListener tryAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tryAgain();
        }
    };

    private View.OnClickListener sendErrorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sendErrorReport();
        }
    };

    private void tryAgain() {
        Log.i(TAG, "setContentView(R.layout.activity_main_loading)");
        setContentView(R.layout.activity_main_loading);

        if (isOnline()) {
            Log.i(TAG, "device is online");
            testAPI();
        } else {
            Log.i(TAG, "device is offline");

            Log.i(TAG, "setContentView(R.layout.activity_main_offline)");
            setContentView(R.layout.activity_main_offline);

            Button tryAgainButton = findViewById(R.id.offline_button_retry);
            Button sendErrorButton = findViewById(R.id.offline_button_senderror);

            tryAgainButton.setOnClickListener(tryAgainListener);
            sendErrorButton.setOnClickListener(sendErrorListener);
        }
    }

    private void displayAPIError() {
        Log.i(TAG, "displaying APIError");
        Log.i(TAG, "setContentView(R.layout.activity_main_apierror)");
        setContentView(R.layout.activity_main_apierror);

        Button tryAgainButton = findViewById(R.id.apierror_button_retry);
        Button sendErrorButton = findViewById(R.id.apierror_button_senderror);

        tryAgainButton.setOnClickListener(tryAgainListener);
        sendErrorButton.setOnClickListener(sendErrorListener);
    }

    private void sendErrorReport() {
        throw new RuntimeException("sendErrorReport()");
    }

    public void testAPI() {
        Log.i(TAG, "testing API");

        String url = LoadDataTask.API_URL + "test";
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "received response: " + response);

                        if (response.equals("test")) {
                            new LoadDataTask(MainActivity.instance, isOnline()).execute();
                        } else {
                            displayAPIError();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "error occured while testing API: " + error.getMessage());
                error.printStackTrace();
                displayAPIError();
            }
        });
        queue.add(stringRequest);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm == null)
            return false;

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_opening:
                Intent openingIntent = new Intent(MainActivity.this, OpeningTimesActivity.class);
                startActivity(openingIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void dataLoaded() {
        setContentView(R.layout.activity_main);

        actionBar = getActionBar();

        long timestamp = Long.valueOf(DataProvider.getInstance().getActiveMenu().getDays()[0].getTimestamp());
        actionBar.setTitle(DateValidater.getTimestampRepresentation(timestamp));

        mPager = findViewById(R.id.pager);
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                long timestamp = Long.valueOf(DataProvider.getInstance().getActiveMenu().getDays()[position].getTimestamp());
                actionBar.setTitle(DateValidater.getTimestampRepresentation(timestamp));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return DataProvider.getInstance().getActiveMenu().getDays().length;
        }
    }

}
