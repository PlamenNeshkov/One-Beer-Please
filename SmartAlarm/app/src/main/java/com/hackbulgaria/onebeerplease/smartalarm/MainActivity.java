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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
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

        MultiAutoCompleteTextView text = (MultiAutoCompleteTextView)findViewById(R.id.cmd);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, CmdCommands.COMMANDS);

        text.setAdapter(adapter);
        text.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       //open new activity from the toolbar settings
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
        //command line button
        Button sendButton = (Button) findViewById(R.id.sendButton);
        if (sendButton != null) {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //prompt user if textField is empty
                    if (cmd.getText().toString().equals("")){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pb.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "Empty field", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        hideKeyBoard(view);
                        shell.println(cmd.getText());
                        cmd.setText("");
                        shell.flush();
                    }
                }

            });
        }
           Button arm = (Button) findViewById(R.id.armBtn);
           arm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //prompt user if textField is empty
                        shell.println("sh ./arm.sh");
                        shell.flush();

                }
           });
           Button disArm = (Button) findViewById(R.id.disarmBtn);
            disArm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //prompt user if textField is empty
                    shell.println("sh ./disarm.sh");
                    shell.flush();

                }
            });

           Button windowsDown = (Button) findViewById(R.id.windowsDownBtn);
            windowsDown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //prompt user if textField is empty
                    shell.println("sh ./windowsdown.sh");
                    shell.flush();

                }
            });
           Button windowsUp = (Button) findViewById(R.id.windowsUpBtn);
            windowsUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //prompt user if textField is empty
                    shell.println("sh ./windowsup.sh");
                    shell.flush();

                }
            });
           Button lighttsOn = (Button) findViewById(R.id.lightsOnBtn);
            lighttsOn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //prompt user if textField is empty
                    shell.println("sh ./lightson.sh");
                    shell.flush();

                }
            });
           Button lightsOff = (Button) findViewById(R.id.lightsOffBtn);
            lightsOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //prompt user if textField is empty
                    shell.println("sh ./lightsoff.sh");
                    shell.flush();

                }
            });
    }

   public void showKeyBoard(View v){
       InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
       manager.showSoftInput(cmd,InputMethodManager.SHOW_IMPLICIT);
   }
    public void hideKeyBoard(View v){
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
                //get data from submission activity
                final String host = data.getStringExtra("host");
                final String user = data.getStringExtra("name");
                final String pass = data.getStringExtra("pass");

                Log.d(TAG, "Username: " + user);
                Log.d(TAG, "Host: " + host);

                //create ssh connection
                Thread connect = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ssh = new SimpleSsh(host, user, pass);
                            shell = ssh.openShell(System.out);
                            //prompt user if login succeed
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
