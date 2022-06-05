package com.example.finish2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }


    public void userRegister(View view)
    {
        String email = ((TextView)findViewById(R.id.userEmail)).getText().toString();
        String password = ((TextView)findViewById(R.id.userPassword)).getText().toString();
        String fullName = ((TextView)findViewById(R.id.userFullName)).getText().toString();
        String login = fullName.split(" ")[0];

        if (email.isEmpty() || password.isEmpty() || fullName.isEmpty())
        {
            Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_LONG).show();
            return;
        }

        if(!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(.[A-Za-z]{2,})$"))
        {
            Toast.makeText(this, "Некорректный адрес электронной почты", Toast.LENGTH_LONG).show();
            return;
        }

        String baseUrl = "https://food.madskill.ru/auth/register";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, baseUrl,
                response -> {
                    Toast.makeText(SignUp.this, response.toString(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);
                },
                error -> Toast.makeText(SignUp.this, "Регистрация не удалась", Toast.LENGTH_LONG).show()
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("email", email);
                params.put("password", password);
                params.put("login", login);
                return params;
            }
        };

        requestQueue.add(request);
    }

    public void toBoardingScreen(View view) {
        Intent intent = new Intent(SignUp.this, SignIn.class);
        startActivity(intent);
    }
}