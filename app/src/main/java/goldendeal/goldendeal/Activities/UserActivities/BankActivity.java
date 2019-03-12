package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.CounterRecyclerAdapter;
import goldendeal.goldendeal.Model.Reward;
import goldendeal.goldendeal.R;

public class BankActivity extends AppCompatActivity {

    private Button taskButton;
    private Button storeButton;
    private Button bankButton;
    private Button rulesButton;
    private Button optionsButton;
    private RecyclerView counterRecycler;
    private CounterRecyclerAdapter counterRecyclerAdapter;
    private RecyclerView imageEconomyRecycler;

    private List<Reward> counterList;
    private List<Reward> imageEconomyList;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        setupDatabase();
        SetupViews();
        mDatabaseReference.keepSynced(true);

        counterList = new ArrayList<Reward>();
        counterRecycler.setHasFixedSize(true);
        counterRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Bank");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Reward currentStoredReward = dataSnapshot.getValue(Reward.class);
                if(!currentStoredReward.isImageEconomy()){
                    counterList.add(currentStoredReward);

                    counterRecyclerAdapter = new CounterRecyclerAdapter(BankActivity.this, counterList);
                    counterRecycler.setAdapter(counterRecyclerAdapter);
                    counterRecyclerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        taskButton = (Button) findViewById(R.id.TaskButton);
        storeButton = (Button) findViewById(R.id.StoreButton);
        bankButton = (Button) findViewById(R.id.BankButton);
        rulesButton = (Button) findViewById(R.id.RulesButton);
        optionsButton = (Button) findViewById(R.id.OptionsButton);
        counterRecycler = (RecyclerView) findViewById(R.id.CounterRecycler);
        imageEconomyRecycler = (RecyclerView) findViewById(R.id.ImageEconomyRecycler);

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
                    case R.id.OptionsButton:
                        startActivity(new Intent(BankActivity.this, OptionsActivity.class));
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        //storeButton.setOnClickListener(switchPage);
        //rulesButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
    }
}
