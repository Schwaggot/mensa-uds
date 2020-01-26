package redfox.android.mensa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import de.mensa_uds.android.MainActivity;

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i("StartActivity", "StartActivity.onCreate(...)");

        Intent i = new Intent(StartActivity.this, MainActivity.class);
        Log.i("StartActivity", "starting MainActivity");
        startActivity(i);
        Log.i("StartActivity", "MainActivity started, calling finish()");
        finish();
    }

}
