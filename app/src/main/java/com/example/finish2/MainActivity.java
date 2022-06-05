package com.example.finish2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        MyAdapter myAdapter = new MyAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myAdapter);

        ScrollingPagerIndicator indicator = findViewById(R.id.indicator);
        indicator.attachToPager(viewPager);
    }

    public void SignInClick(View view) {
        Intent intent = new Intent(MainActivity.this, SignIn.class);
        startActivity(intent);
    }

    public void SignUpClick(View view) {
        Intent intent = new Intent(MainActivity.this, SignUp.class);
        startActivity(intent);
    }
}