package com.example.pc.hisenpai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnGreetings, btnLuck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGreetings = findViewById(R.id.btnGreeting);
        btnLuck = findViewById(R.id.btnPureLuck);
        btnLuck.setOnClickListener(this);
        btnGreetings.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGreeting:
                goToGreetings();
                break;

            case R.id.btnPureLuck:
                goToPureLuck();
                break;
        }
    }

    public void goToGreetings(){
        Intent intent = new Intent(this,GreetingsActivity.class);
        startActivity(intent);
    }

    public void goToPureLuck(){
        Intent intent = new Intent(this,PrizesActivity.class);
        startActivity(intent);
    }
}
