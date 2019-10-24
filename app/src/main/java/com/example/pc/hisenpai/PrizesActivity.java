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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PrizesActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnRandom;
    private LinearLayout linearLayout;
    private EditText inputNumber;
    private TextView txtSize;
    private PopupWindow mPopupWindow;
    private List<Prize> prizesOriginal = new ArrayList<>();
    private List<Prize> prizesFiller = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prizes);
        linearLayout = findViewById(R.id.linearLayout);
        btnRandom = findViewById(R.id.btnRandom);
        inputNumber = findViewById(R.id.inputNumber);
        txtSize = findViewById(R.id.txtSize);
        btnRandom.setOnClickListener(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        getSupportActionBar().setTitle(R.string.my_class_rooms);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getPrizes();
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
        if(number > prizesFiller.size()){
            Toast.makeText(this,"Оруулсан утга нийт үгсийн тооноос их байна! Нийт үгсийн тоо = " + prizesFiller.size(),Toast.LENGTH_SHORT).show();
        }
        else{
            Collections.shuffle(prizesFiller);
            showMenu(number,prizesFiller.get(number-1));
        }

    }

    public void showMenu(int chosenNumber,Prize prize){
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.prizes_popup ,null);
//                    float density = MenuActivity.getResources().getDisplayMetrics().density;

        mPopupWindow = new PopupWindow(customView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        LinearLayout linearLayoutPopup = customView.findViewById(R.id.linearLayoutPopup);
        TextView txtPrize = (TextView) customView.findViewById(R.id.txtPrize);
        TextView txtChosenNumber = (TextView) customView.findViewById(R.id.txtChosennumber);
        if(prize.getTier() == 0){
            linearLayoutPopup.setBackgroundResource(R.drawable.prize_tier0);
            txtChosenNumber.setTextColor(getResources().getColor(R.color.newIndigo));
            txtPrize.setTextColor(getResources().getColor(R.color.newIndigo));
            txtPrize.setText("Азын тэнгэр таныг ивээх болтугай");
        }

        else if(prize.getTier() == 1){
            linearLayoutPopup.setBackgroundResource(R.drawable.prize_tier1);
            txtChosenNumber.setTextColor(getResources().getColor(R.color.newOrange));
            txtPrize.setTextColor(getResources().getColor(R.color.newOrange));
            txtPrize.setText("Та\n" + prize.getName() + "\nхожлоо");
        }

        else if(prize.getTier() == 2){
            linearLayoutPopup.setBackgroundResource(R.drawable.prize_tier2);
            txtChosenNumber.setTextColor(getResources().getColor(R.color.newBlue));
            txtPrize.setTextColor(getResources().getColor(R.color.newBlue));
            txtPrize.setText("Та\n" + prize.getName() + "\nхожлоо");
        }

        else if(prize.getTier() == 3){
            linearLayoutPopup.setBackgroundResource(R.drawable.prize_tier3);
            txtChosenNumber.setTextColor(getResources().getColor(R.color.newYellow));
            txtPrize.setTextColor(getResources().getColor(R.color.newYellow));
            txtPrize.setText("Та\n" + prize.getName() + "\nхожлоо");
        }

        else if(prize.getTier() == 4){
            linearLayoutPopup.setBackgroundResource(R.drawable.prize_tier4);
            txtChosenNumber.setTextColor(getResources().getColor(R.color.newPurple));
            txtPrize.setTextColor(getResources().getColor(R.color.newPurple));
            txtPrize.setText("Та\n" + prize.getName() + "\nхожлоо");
        }
        else if(prize.getTier() == 5){
            linearLayoutPopup.setBackgroundResource(R.drawable.prize_tier5);
            txtChosenNumber.setTextColor(getResources().getColor(R.color.newPink));
            txtPrize.setTextColor(getResources().getColor(R.color.newPink));
            txtPrize.setText("Та\n" + prize.getName() + "\nхожлоо");
        }

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

    public void getPrizes(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, Object> value = (Map<String, Object>) dataSnapshot.getValue();

                Log.d("checkData", "Value is: " + value.get("Prizes").toString());
                JSONObject data = new JSONObject(value);
                try {
                    JSONObject prizes = data.getJSONObject("Prizes");
                    JSONArray tier1 = prizes.getJSONArray("1");
                    JSONArray tier2 = prizes.getJSONArray("2");
                    JSONArray tier3 = prizes.getJSONArray("3");
                    JSONArray tier4 = prizes.getJSONArray("4");
                    JSONArray tier5 = prizes.getJSONArray("5");
                    for(int i = 0; i < tier1.length(); i++){
                        prizesOriginal.add(new Prize(1,tier1.get(i).toString()));
                    }

                    for(int i = 0; i < tier2.length(); i++){
                        prizesOriginal.add(new Prize(2,tier2.get(i).toString()));
                    }

                    for(int i = 0; i < tier3.length(); i++){
                        prizesOriginal.add(new Prize(3,tier3.get(i).toString()));
                    }

                    for(int i = 0; i < tier4.length(); i++){
                        prizesOriginal.add(new Prize(4,tier4.get(i).toString()));
                    }

                    for(int i = 0; i < tier5.length(); i++){
                        prizesOriginal.add(new Prize(5,tier5.get(i).toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.d("checkDataaaa", data.getJSONObject("Prizes").toString());
                    Log.d("checkDataaaa", "size = " + prizesOriginal.size());
                    fillPrizes();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("checkData", "Failed to read value.", error.toException());
            }
        });
    }

    public void fillPrizes(){
        Prize nothing = new Prize(0,"nothing");
        for(int i = 0; i < prizesOriginal.size(); i++){
            if(prizesOriginal.get(i).getTier() == 1){
                for(int j = 0; j < 5; j++){
                    prizesFiller.add(prizesOriginal.get(i));
                    prizesFiller.add(nothing);
                }
            }
            else if(prizesOriginal.get(i).getTier() == 2){
                for(int j = 0; j < 4; j++){
                    prizesFiller.add(prizesOriginal.get(i));
                    prizesFiller.add(nothing);
                }
            }
            else if(prizesOriginal.get(i).getTier() == 3){
                for(int j = 0; j < 3; j++){
                    prizesFiller.add(prizesOriginal.get(i));
                    prizesFiller.add(nothing);
                }
            }
            else if(prizesOriginal.get(i).getTier() == 4){
                for(int j = 0; j < 2; j++){
                    prizesFiller.add(prizesOriginal.get(i));
                    prizesFiller.add(nothing);
                }
            }
            else if(prizesOriginal.get(i).getTier() == 5){
                prizesFiller.add(prizesOriginal.get(i));
                prizesFiller.add(nothing);
            }
        }
        txtSize.setText(String.valueOf(prizesFiller.size()));
        Log.d("checkSize", "size = " + prizesFiller.size());

    }
}