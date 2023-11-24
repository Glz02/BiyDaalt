package com.example.biydaalt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button type1Btn = findViewById(R.id.type1Btn);


        type1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Эхлэх Button Clicked", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, List.class));
                finish();
            }
        });

    }
}
