package de.mensa_uds.android;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;

import redfox.android.mensa.R;

public class OpeningTimesActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_times);

        WebView webView = findViewById(R.id.opening_times);
        webView.loadData(DataProvider.getInstance().getActiveOpeningTimes(), "text/html", null);
        webView.setBackgroundColor(Color.TRANSPARENT);
    }
}
