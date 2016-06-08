package com.example.sejol.secsys.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sejol.secsys.Clases.Usuario;
import com.example.sejol.secsys.R;
import com.example.sejol.secsys.Utilidades.NFC_Controller;
import com.example.sejol.secsys.Utilidades.SQLite_Controller;
import com.firebase.client.Firebase;

import java.text.ParseException;

public class LoginActivity extends AppCompatActivity
{
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_login);
        cargarCredenciales();
        this.mEmailView = ((AutoCompleteTextView)findViewById(R.id.lngInCorreo));
        this.mPasswordView = ((EditText)findViewById(R.id.lngInContraseña));

        // Boton de iniciar sesión
        ((Button)findViewById(R.id.lngInBTNLogIn)).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                try
                {
                    attemptLogin();
                    return;
                }
                catch (ParseException localParseException)
                {
                    localParseException.printStackTrace();
                }
            }
        });
        // Boton para registrar
        ((TextView)findViewById(R.id.lngInLinkRegister)).setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View paramAnonymousView)
            {
                Intent localIntent =
                        new Intent(LoginActivity.this.getApplicationContext(), RegistroActivity.class);
                LoginActivity.this.startActivity(localIntent);
            }
        });

        this.toolbar = ((Toolbar)findViewById(R.id.toolbarL));
        this.toolbar.setTitle("Regencias Agropecuarias");
    }

    public void attemptLogin() throws ParseException
    {
        this.mEmailView.setError(null);
        this.mPasswordView.setError(null);
        String Correo = this.mEmailView.getText().toString();
        String Constraseña = this.mPasswordView.getText().toString();

        boolean focus = false;
        Object localObject = null;
        if (TextUtils.isEmpty(Constraseña))
        {
            this.mPasswordView.setError("Ingresar contraseña");
            localObject = this.mPasswordView;
            focus = true;
        }
        if (!validarContraseña(Constraseña))
        {
            this.mPasswordView.setError("Contraseña invalida");
            localObject = this.mPasswordView;
            focus = true;
        }
        if (TextUtils.isEmpty(Correo))
        {
            this.mEmailView.setError("Ingresar correo");
            localObject = this.mEmailView;
            focus = true;
        }
        if (!validarCorreo(Correo))
        {
            this.mEmailView.setError("Correo Invalido");
            localObject = this.mEmailView;
            focus = true;
        }
        while (focus)
        {
            ((View)localObject).requestFocus();
            return;
        }
        loadUser(Correo, Constraseña);
    }

    private void loadUser(String email, String password)
    {
        if(!email.equals("") && !password.equals(""))
        {
            SQLite_Controller db = new SQLite_Controller(this);

            Usuario usuario = db.getUsuario(email,password);

            if(usuario == null)
            {
                Toast.makeText(getApplicationContext(), "Usuario o contraseña inválidos", Toast.LENGTH_SHORT).show();
            }
            else
            {
                guardarCredenciales(email, password);
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("usuario",usuario);
                startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Favor completar todos los campos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validarContraseña(String paramString)
    {
        return paramString.length() > 4;
    }

    private boolean validarCorreo(String paramString)
    {
        return paramString.contains("@");
    }

    public void cargarCredenciales()
    {
        SharedPreferences localSharedPreferences = getSharedPreferences("Login", 0);
        String str1 = localSharedPreferences.getString("Unm", null);
        String str2 = localSharedPreferences.getString("Psw", null);
        try
        {
            loadUser(str1, str2);
            return;
        }
        catch (Exception e)
        {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void guardarCredenciales(String paramString1, String paramString2)
    {
        SharedPreferences.Editor localEditor = getSharedPreferences("Login", 0).edit();
        localEditor.putString("Unm", paramString1);
        localEditor.putString("Psw", paramString2);
        localEditor.commit();
    }
}