package com.example.sejol_000.proyecto.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.sejol_000.proyecto.R;

/**
 * A login screen that offers login via email/password.
 */
public class ActivityLogin extends AppCompatActivity {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.txtCorreo);
        mPasswordView = (EditText) findViewById(R.id.txtContraseña);


        Button logIn = (Button) findViewById(R.id.btnLogIn);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        TextView sigIn = (TextView) findViewById(R.id.linkRegister);
        sigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),ActivityRegistrar.class);
                startActivity(i);
            }
        });

        Button visitSignInButton = (Button) findViewById(R.id.btnVisitante);
        visitSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Visitante
            }
        });

        mLoginFormView = findViewById(R.id.login_scroll);
    }

    public void attemptLogin(){
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !validarContraseña(password)) {
            mPasswordView.setError(getString(R.string.error_contraseña_invalida));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_campo_requerido));
            focusView = mEmailView;
            cancel = true;
        } else if (!validarCorreo(email)) {
            mEmailView.setError(getString(R.string.error_correo_invalido));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Intent i = new Intent(getApplicationContext(),ActivityMainAdmin.class);
            startActivity(i);
        }
    }

    private boolean validarCorreo(String email) {
        return email.contains("@");
    }

    private boolean validarContraseña(String password) {
        return password.length() > 4;
    }
}


