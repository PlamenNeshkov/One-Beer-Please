package com.hackbulgaria.onebeerplease.smartalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.jcraft.jsch.JSchException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SubmissionActivity extends AppCompatActivity {
    private static final String TAG = "SubmissionActivity";
    private static final String NAME = "name";
    private static final String PASS = "pass";
    private static final String HOST = "host";

    private Toolbar toolbar;

    private AutoCompleteTextView user;
    private EditText pass;
    private AutoCompleteTextView host;
    private Button saveBtn;

    private Set<String> userData;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        File userDataFile = new File(context.getFilesDir(),"userData.txt");

        setContentView(R.layout.activity_submission);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(user, InputMethodManager.SHOW_IMPLICIT);

        user = (AutoCompleteTextView) findViewById(R.id.userEditText);
        pass = (EditText) findViewById(R.id.passEditText);
        host = (AutoCompleteTextView) findViewById(R.id.hostEditText);
        saveBtn = (Button) findViewById(R.id.saveButton);


        if(userDataFile.exists()){
            userData = FileManager.readFile(userDataFile);
            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,userData.toArray());
            user.setAdapter(adapter);
            user.setThreshold(1);

            host.setAdapter(adapter);
            host.setThreshold(1);
            //uncomment next row to clear the textFiled
           // userDataFile.delete();
        }


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                intent.putExtra(NAME, user.getText().toString());
                intent.putExtra(PASS, pass.getText().toString());
                intent.putExtra(HOST, host.getText().toString());

                Set<String> data  = userData != null ? userData : new HashSet<String>();

                userData.add(user.getText().toString());
                userData.add(host.getText().toString());
              //  userData = data.toArray(new String[data.size()]);
                //pass current context
                FileManager.writeToFile(context, userData);
                //Log.wtf(TAG, Arrays.toString(userData));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
