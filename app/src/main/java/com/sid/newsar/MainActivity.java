package com.sid.newsar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.arButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getPackageManager().getLaunchIntentForPackage("com.Sid.NewsAug"));
            }
        });

        findViewById(R.id.qrButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(MainActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                integrator.setPrompt("Scan a QR code");
                // integrator.setBeepEnabled(false);
                // integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });

        findViewById(R.id.historyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HistoryActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            String url = result.getContents();
            if(url == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (url.startsWith("http://") || url.startsWith("https://")) {
                    Helpers.openWebPage(getApplicationContext(), url);
                    encache(url);
                } else Toast.makeText(this, url + " isn't a valid URL!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Help")
                        .setMessage("This is a sample help text. :D")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.about:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("About")
                        .setMessage("This is a sample about text. :D")
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //
                            }
                        })
                        .create()
                        .show();
                break;
        }
        return true;
    }

    private void encache(String url) {
        try {
            SharedPreferences preferences = getSharedPreferences("news_ar", Context.MODE_PRIVATE);
//            preferences.edit().putString("history", new JSONArray(preferences.getString("history", "[]")).put(new JSONArray("[" + url + "," + System.currentTimeMillis() + "]")).toString()).apply();
            preferences.edit().putString("history", new JSONArray(preferences.getString("history", "[]")).put(url).toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}