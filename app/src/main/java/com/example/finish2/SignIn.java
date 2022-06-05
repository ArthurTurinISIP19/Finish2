package com.example.finish2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignIn extends AppCompatActivity {

    EditText Email;
    EditText Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Email = findViewById(R.id.userEmail);
        Password = findViewById(R.id.userPassword);

    }

    public void login(View view) {
        if (Email.getText().toString().isEmpty() || Password.getText().toString().isEmpty()) {
            сreateDialog(this, "Все поля должны быть заполнены.");
        } else {
            if (!checkEmail(Email.getText().toString())) {
                сreateDialog(this, "Почта не соответствует паттерну \"name@domenname.ru\".");
            } else {
               userLogin();
            }
        }
    }

    public void userLogin() {
        //startActivity(new Intent(this, MainScreen.class));
        String email = ((TextView)findViewById(R.id.userEmail)).getText().toString();
        String password = ((TextView)findViewById(R.id.userPassword)).getText().toString();

        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String baseUrl = "https://food.madskill.ru/auth/login";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, baseUrl, json,
                response -> {
                    startActivity(new Intent(this, MainScreen.class));
                },
                error -> сreateDialog(this, "Не удалось авторизироваться:\n" + error.getMessage()));
        requestQueue.add(request);
    }

    public void forgotPassword(View view) {
        startActivity(new Intent(SignIn.this, SignUp.class));

    }

    private boolean checkEmail(String email) {
        if (email.matches("^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,3})$")) {
            return true;
        } else {
            return false;
        }
    }

    public void сreateDialog(Activity activity, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder
                .setTitle("Error")
                .setMessage(msg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }
}
