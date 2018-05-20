package com.neo.sampathmessagelistener;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText txtPhoneNo = findViewById(R.id.phoneNo);
        txtPhoneNo.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String phoneNo = txtPhoneNo.getText().toString().trim();
                SharedPreferences sharedPref = getSharedPreferences(getString(R.string.shared_pref_file), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (phoneNo.isEmpty()) {
                    editor.remove(getString(R.string.pref_phone_no));
                    Snackbar.make(v, "Listener disabled", Snackbar.LENGTH_LONG).show();
                } else {
                    editor.putString(getString(R.string.pref_phone_no), phoneNo);
                    Snackbar.make(v, "Phone no changed to :" + txtPhoneNo.getText(), Snackbar.LENGTH_LONG).show();
                }
                editor.apply();
                return false;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.RECEIVE_SMS
                                }, 1);
                    } else {
                        Snackbar.make(view, "Permissions are already granted", Snackbar.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                    ex.printStackTrace();
                }
            }
        });
    }

}
