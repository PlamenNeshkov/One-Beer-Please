package com.hackbulgaria.onebeerplease.smartalarm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private Intent intent;

    private TextView textView;
    private EditText cmd;
    private ProgressBar pb;

    private SimpleSsh ssh;
    private PrintStream shell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent connectionIntent = new Intent(MainActivity.this, SubmissionActivity.class);
                startActivityForResult(connectionIntent, 1);

                return true;
            }
        });


        cmd = (EditText) findViewById(R.id.cmd);
        pb = (ProgressBar) findViewById(R.id.loadingPanel);
        pb.setVisibility(View.GONE);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        if (sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideKeyBoard(view);
                    shell.println(cmd.getText());
                    shell.flush();
                }
            });
        }

    }

   public void showKeyBoard(View v){
       //textView = (TextView) findViewById(R.id.textView);
       InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       manager.showSoftInput(cmd,InputMethodManager.SHOW_IMPLICIT);
   }
    public void hideKeyBoard(View v){
        //textView = (TextView) findViewById(R.id.textView);
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(cmd.getWindowToken(),0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                pb.setVisibility(View.VISIBLE);

                final String host = data.getStringExtra("host");
                final String user = data.getStringExtra("user");
                final String pass = data.getStringExtra("pass");

                Log.d(TAG, "Username: " + user);
                Log.d(TAG, "Host: " + host);

                Thread connect = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ssh = new SimpleSsh(host, user, pass);
                            shell = ssh.openShell(System.out);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Successful connection", Toast.LENGTH_LONG).show();
                                }
                            });
                        } catch (Exception e) {
                            Log.wtf(TAG, "Ssh error!");
                            Log.wtf(TAG, e.getClass().toString());
                            Log.wtf(TAG, e.getMessage());

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), "Failed connection", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                });

                connect.start();
            }
        }
    }
}
