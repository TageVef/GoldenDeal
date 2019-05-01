package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.UserData.CounterRecyclerAdapter;
import goldendeal.goldendeal.Data.UserData.ImageEconomyRecyclerAdapter;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class BankActivity extends AppCompatActivity {
    private static final String TAG = "BankActivity";

    private ImageView taskButton;
    private ImageView storeButton;
    private ImageView bankButton;
    private ImageView rulesButton;
    private ImageView optionsButton;
    private ImageView faceButton;
    private TextView titleText;
    private ConstraintLayout layout;

    private RecyclerView counterRecycler;
    private CounterRecyclerAdapter counterRecyclerAdapter;
    private RecyclerView imageEconomyRecycler;
    private ImageEconomyRecyclerAdapter imageEconomyRecyclerAdapter;

    private List<VirtualCurrency> counterList;
    private List<VirtualCurrency> imageEconomyList;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        SetupDatabase();
        SetupViews();
        mDatabaseReference.keepSynced(true);

        counterList = new ArrayList<VirtualCurrency>();
        counterRecycler.setHasFixedSize(true);
        counterRecycler.setLayoutManager(new LinearLayoutManager(this));

        imageEconomyList = new ArrayList<VirtualCurrency>();
        imageEconomyRecycler.setHasFixedSize(true);
        imageEconomyRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Bank");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                VirtualCurrency currentStoredCurrency = dataSnapshot.getValue(VirtualCurrency.class);
                if (currentStoredCurrency.isImageEconomy()) {
                    imageEconomyList.add(currentStoredCurrency);

                    imageEconomyRecyclerAdapter = new ImageEconomyRecyclerAdapter(BankActivity.this, imageEconomyList);
                    imageEconomyRecycler.setAdapter(imageEconomyRecyclerAdapter);
                    imageEconomyRecyclerAdapter.notifyDataSetChanged();
                } else {
                    counterList.add(currentStoredCurrency);

                    counterRecyclerAdapter = new CounterRecyclerAdapter(BankActivity.this, counterList);
                    counterRecycler.setAdapter(counterRecyclerAdapter);
                    counterRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildChanged: start");
                String currencyTitle = dataSnapshot.getKey();
                for (int i = 0; i < counterList.size(); i++){
                    if(TextUtils.equals(counterList.get(i).getTitle(), currencyTitle)){
                        counterRecyclerAdapter.counterList.set(i, dataSnapshot.getValue(VirtualCurrency.class));
                        counterRecyclerAdapter.notifyItemChanged(i);
                        counterRecyclerAdapter.notifyDataSetChanged();
                    }
                }
                for(int i = 0; i < imageEconomyList.size(); i++){
                    if(TextUtils.equals(imageEconomyList.get(i).getTitle(), currencyTitle)){
                        imageEconomyRecyclerAdapter.currencyList.set(i, dataSnapshot.getValue(VirtualCurrency.class));
                        imageEconomyRecyclerAdapter.notifyItemChanged(i);
                        imageEconomyRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved: start");
                String currencyTitle = dataSnapshot.getKey();
                for (int i = 0; i < counterList.size(); i++){
                    if(TextUtils.equals(counterList.get(i).getTitle(), currencyTitle)){
                        counterRecyclerAdapter.counterList.remove(i);
                        counterRecyclerAdapter.notifyItemRemoved(i);
                        counterRecyclerAdapter.notifyItemRangeChanged(i, counterRecyclerAdapter.counterList.size());
                        counterRecyclerAdapter.notifyDataSetChanged();
                    }
                }
                for(int i = 0; i < imageEconomyList.size(); i++){
                    if(TextUtils.equals(imageEconomyList.get(i).getTitle(), currencyTitle)){
                        imageEconomyRecyclerAdapter.currencyList.remove(i);
                        imageEconomyRecyclerAdapter.notifyItemRemoved(i);
                        imageEconomyRecyclerAdapter.notifyItemRangeChanged(i, imageEconomyRecyclerAdapter.currencyList.size());
                        imageEconomyRecyclerAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SetupLanguage();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SetupLanguage();
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        taskButton = (ImageView) findViewById(R.id.TaskButton);
        storeButton = (ImageView) findViewById(R.id.StoreButton);
        bankButton = (ImageView) findViewById(R.id.BankButton);
        rulesButton = (ImageView) findViewById(R.id.RulesButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);
        faceButton = (ImageView) findViewById(R.id.FaceButton);
        counterRecycler = (RecyclerView) findViewById(R.id.CounterRecycler);
        imageEconomyRecycler = (RecyclerView) findViewById(R.id.ImageEconomyRecycler);
        titleText = (TextView) findViewById(R.id.TitleText);
        layout = (ConstraintLayout) findViewById(R.id.BankLayout);


        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(BankActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(BankActivity.this, StoreActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(BankActivity.this, BankActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(BankActivity.this, RulesActivity.class));
                        finish();
                        break;
                    case R.id.FaceButton:
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(BankActivity.this, OptionsActivity.class));
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
        //faceButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
    }

    private void SetupLanguage(){
        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info").child("language");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String language = dataSnapshot.getValue(String.class);

                if(TextUtils.equals(language, "Norsk")){
                    titleText.setText("Butikk");
                } else if(TextUtils.equals(language, "English")){
                    titleText.setText("Store");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetupTheme(){
        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info").child("theme");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String theme = dataSnapshot.getValue(String.class);

                switch(theme){

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
