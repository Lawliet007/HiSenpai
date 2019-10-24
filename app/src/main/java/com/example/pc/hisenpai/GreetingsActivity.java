package com.example.pc.hisenpai;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class GreetingsActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnRandom;
    private LinearLayout linearLayout;
    private EditText inputNumber;
    private PopupWindow mPopupWindow;
    private List<String> greetings = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greetings);
        linearLayout = findViewById(R.id.linearLayout);
        btnRandom = findViewById(R.id.btnRandom);
        inputNumber = findViewById(R.id.inputNumber);
        btnRandom.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle(R.string.my_class_rooms);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getWords();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRandom:
                if(!inputNumber.getText().toString().isEmpty() ){
                    random(Integer.valueOf(inputNumber.getText().toString()));
                }
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void random(int number){
        if(number > greetings.size()){
            Toast.makeText(this,"Оруулсан утга нийт үгсийн тооноос их байна! Нийт үгсийн тоо = " + greetings.size(),Toast.LENGTH_SHORT).show();
        }
        else{
            Collections.shuffle(greetings);
            showMenu(number,greetings.get(number-1));
        }

    }

    public void showMenu(int chosenNumber,String greeting){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.greetings_popup ,null);
//                    float density = MenuActivity.getResources().getDisplayMetrics().density;

        mPopupWindow = new PopupWindow(customView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        TextView txtGreetings = (TextView) customView.findViewById(R.id.txtGreeting);
        TextView txtChosenNumber = (TextView) customView.findViewById(R.id.txtChosennumber);
        txtGreetings.setText("Өдрийг " + greeting + " өнгөрүүлээрэй.");
        txtChosenNumber.setText(String.valueOf(chosenNumber));

        Button btnOk = (Button) customView.findViewById(R.id.btnOk);


        btnOk.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setFocusable(true);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPopupWindow.showAtLocation(linearLayout, Gravity.CENTER,0,200);
            }
        });
    }

    public void getWords(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();

                Log.d("checkData", "Value is: " + value.toString());
                greetings = (List<String>) value.get("Words");
                Log.d("checkData", "size: " + greetings.size());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("checkData", "Failed to read value.", error.toException());
            }
        });
    }
}
