package com.app.travelbug;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.travelbug.data.DatabaseHelper;
import com.app.travelbug.data.Result;
import com.app.travelbug.data.model.LoggedInUser;


public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper db = new DatabaseHelper(this);
    private LoginActivity activity = this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.loginButton);
        final Button createAccountButton = findViewById(R.id.createAccountButton);




        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                //what
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NewAccountActivity.class);
                startActivity(intent);
            }
        });
    }


    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        if (isUserNameValid(username)) {
            if(isPasswordValid(password)){
                LoggedInUser user = db.getUserByEmail(username);
                if(user == null){
                    Toast.makeText(activity, "Username doesn't exist", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(activity, MapsActivity.class);
                    startActivity(intent);
                }

            } else {
                Toast.makeText(activity, "Invalid Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "Invalid Username", Toast.LENGTH_SHORT).show();
        }

    }

    public void loginDataChanged(String username, String password) {

    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}