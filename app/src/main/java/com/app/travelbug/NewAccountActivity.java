package com.app.travelbug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.travelbug.data.DatabaseHelper;
import com.app.travelbug.data.model.LoggedInUser;

public class NewAccountActivity extends AppCompatActivity {

    private NewAccountActivity activity = this;
    private EditText firstName;
    private EditText lastName;
    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private LoggedInUser check = null;


    DatabaseHelper db;

    /**
     * Purpose:
     * Sets up the view when the new account activity is started
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        firstName = findViewById(R.id.firstNameEditText);
        lastName = findViewById(R.id.lastNameEditText);
        username = findViewById(R.id.passwordEditText);
        email = findViewById(R.id.emailEditText);
        password = findViewById(R.id.passwordEditText);
        confirmPassword = findViewById(R.id.passwordConfirmEditText);

        db = new DatabaseHelper(getApplicationContext());

        Button signupButton = findViewById(R.id.signupButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                String firstNameText = firstName.getText().toString();
                String lastNameText = lastName.getText().toString();
                String usernameText = username.getText().toString();
                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();
                String confirmPasswordText = confirmPassword.getText().toString();

                try {
                    check = db.getUserByEmail(emailText);
                    if (check == null){
                        Toast.makeText(getApplicationContext(), "DDDDDDDDDDDDDDDDDDd", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {e.printStackTrace();}

                //Toast.makeText(getApplicationContext(), Boolean.toString(confirmPasswordText==passwordText), Toast.LENGTH_LONG).show();


                if (!passwordText.equals(confirmPasswordText)){
                    Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }




                try {
                    //@TODO need to change this to save to the DB
                    LoggedInUser user = new LoggedInUser(emailText, firstNameText, lastNameText, passwordText);
                    db.createUser(user);

                    //LoggedInUser user = new LoggedInUser(1, usernameText);

                    //controller.saveUser(user);
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra("username", user.getDisplayName());
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}