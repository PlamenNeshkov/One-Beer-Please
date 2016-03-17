package com.hackbulgaria.onebeerplease.smartalarm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

public class SubmissionActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText user;
    private EditText pass;
    private EditText host;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(user, InputMethodManager.SHOW_IMPLICIT);

        user = (EditText) findViewById(R.id.userEditText);
        pass = (EditText) findViewById(R.id.passEditText);
        host = (EditText) findViewById(R.id.hostEditText);
        saveBtn = (Button) findViewById(R.id.saveButton);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("user", user.getText());
                intent.putExtra("pass", pass.getText());
                intent.putExtra("host", host.getText());

                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

}
