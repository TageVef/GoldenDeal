package goldendeal.goldendeal.Activities.UserActivities;

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
import android.widget.ImageView;

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
import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class BankActivity extends AppCompatActivity {
    private static final String TAG = "BankActivity";

    private User currentUser;
    private List<VirtualCurrency> counterList;
    private List<VirtualCurrency> imageEconomyList;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private ConstraintLayout background;
    private ImageView taskButton;
    private ImageView storeButton;
    private ImageView bankButton;
    private ImageView rulesButton;
    private ImageView optionsButton;

    private RecyclerView counterRecycler;
    private CounterRecyclerAdapter counterRecyclerAdapter;
    private RecyclerView imageEconomyRecycler;
    private ImageEconomyRecyclerAdapter imageEconomyRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        SetupDatabase();
        SetupViews();
        ;

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                SetupTheme();
                SetupDatabase();
                mDatabaseReference.keepSynced(true);

                counterList = new ArrayList<VirtualCurrency>();
                counterRecycler.setHasFixedSize(true);
                counterRecycler.setLayoutManager(new LinearLayoutManager(BankActivity.this));

                imageEconomyList = new ArrayList<VirtualCurrency>();
                imageEconomyRecycler.setHasFixedSize(true);
                imageEconomyRecycler.setLayoutManager(new LinearLayoutManager(BankActivity.this));

                mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Bank");
                mDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        VirtualCurrency currentStoredCurrency = dataSnapshot.getValue(VirtualCurrency.class);
                        if (currentStoredCurrency.isImageEconomy()) {
                            imageEconomyList.add(currentStoredCurrency);

                            imageEconomyRecyclerAdapter = new ImageEconomyRecyclerAdapter(BankActivity.this, imageEconomyList, currentUser.getTheme());
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
                        String currencyTitle = dataSnapshot.getKey();
                        for (int i = 0; i < counterList.size(); i++) {
                            if (TextUtils.equals(counterList.get(i).getTitle(), currencyTitle)) {
                                counterRecyclerAdapter.counterList.set(i, dataSnapshot.getValue(VirtualCurrency.class));
                                counterRecyclerAdapter.notifyItemChanged(i);
                                counterRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                        for (int i = 0; i < imageEconomyList.size(); i++) {
                            if (TextUtils.equals(imageEconomyList.get(i).getTitle(), currencyTitle)) {
                                imageEconomyRecyclerAdapter.currencyList.set(i, dataSnapshot.getValue(VirtualCurrency.class));
                                imageEconomyRecyclerAdapter.notifyItemChanged(i);
                                imageEconomyRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        String currencyTitle = dataSnapshot.getKey();
                        for (int i = 0; i < counterList.size(); i++) {
                            if (TextUtils.equals(counterList.get(i).getTitle(), currencyTitle)) {
                                counterRecyclerAdapter.counterList.remove(i);
                                counterRecyclerAdapter.notifyItemRemoved(i);
                                counterRecyclerAdapter.notifyItemRangeChanged(i, counterRecyclerAdapter.counterList.size());
                                counterRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                        for (int i = 0; i < imageEconomyList.size(); i++) {
                            if (TextUtils.equals(imageEconomyList.get(i).getTitle(), currencyTitle)) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SetupDatabase();
        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                SetupTheme();
                imageEconomyRecyclerAdapter = new ImageEconomyRecyclerAdapter(BankActivity.this, imageEconomyList, currentUser.getTheme());
                imageEconomyRecycler.setAdapter(imageEconomyRecyclerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        background = (ConstraintLayout) findViewById(R.id.BankLayout);
        taskButton = (ImageView) findViewById(R.id.TaskButton);
        storeButton = (ImageView) findViewById(R.id.StoreButton);
        bankButton = (ImageView) findViewById(R.id.BankButton);
        rulesButton = (ImageView) findViewById(R.id.RulesButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);
        counterRecycler = (RecyclerView) findViewById(R.id.CounterRecycler);
        imageEconomyRecycler = (RecyclerView) findViewById(R.id.ImageEconomyRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(BankActivity.this, BankActivity.class);
                switch (view.getId()) {
                    case R.id.TaskButton:
                        newIntent = new Intent(BankActivity.this, TasksActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.StoreButton:
                        newIntent = new Intent(BankActivity.this, StoreActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.RulesButton:
                        newIntent = new Intent(BankActivity.this, RulesActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.OptionsButton:
                        newIntent = new Intent(BankActivity.this, OptionsActivity.class);
                        newIntent.putExtra("Role", false);
                        startActivity(newIntent);
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
    }

    private void SetupTheme() {
        switch (currentUser.getTheme()) {
            case "Mermaids":
                background.setBackgroundResource(R.drawable.mermaids_background_bank_english);
                taskButton.setImageResource(R.drawable.mermaids_button_task_english);
                bankButton.setImageResource(R.drawable.mermaids_button_bank_english);
                storeButton.setImageResource(R.drawable.mermaids_button_store_english);
                rulesButton.setImageResource(R.drawable.mermaids_button_rules_english);
                optionsButton.setImageResource(R.drawable.mermaids_button_options);
                break;
            case "Western":
                background.setBackgroundResource(R.drawable.western_background_bank_english);
                taskButton.setImageResource(R.drawable.western_button_task_english);
                bankButton.setImageResource(R.drawable.western_button_bank_english);
                storeButton.setImageResource(R.drawable.western_button_store_english);
                rulesButton.setImageResource(R.drawable.western_button_rules_english);
                optionsButton.setImageResource(R.drawable.western_button_options);
                break;
            case "Space":
                background.setBackgroundResource(R.drawable.space_background_bank_english);
                taskButton.setImageResource(R.drawable.space_button_task_english);
                bankButton.setImageResource(R.drawable.space_button_bank_english);
                storeButton.setImageResource(R.drawable.space_button_store_english);
                rulesButton.setImageResource(R.drawable.space_button_rules_english);
                optionsButton.setImageResource(R.drawable.space_button_options);
                break;
            case "Spring":
                background.setBackgroundResource(R.drawable.spring_background_bank);
                taskButton.setImageResource(R.drawable.spring_button_task);
                bankButton.setImageResource(R.drawable.spring_button_bank);
                storeButton.setImageResource(R.drawable.spring_button_store);
                rulesButton.setImageResource(R.drawable.spring_button_rules);
                optionsButton.setImageResource(R.drawable.spring_button_options);
                break;
            case "Summer":
                background.setBackgroundResource(R.drawable.summer_background_bank);
                taskButton.setImageResource(R.drawable.summer_button_task);
                bankButton.setImageResource(R.drawable.summer_button_bank);
                storeButton.setImageResource(R.drawable.summer_button_store);
                rulesButton.setImageResource(R.drawable.summer_button_rules);
                optionsButton.setImageResource(R.drawable.summer_button_options);
                break;
            case "Fall":
                background.setBackgroundResource(R.drawable.fall_background_bank);
                taskButton.setImageResource(R.drawable.fall_button_task);
                bankButton.setImageResource(R.drawable.fall_button_bank);
                storeButton.setImageResource(R.drawable.fall_button_store);
                rulesButton.setImageResource(R.drawable.fall_button_rules);
                optionsButton.setImageResource(R.drawable.fall_button_options);
                break;
            case "Winter":
                background.setBackgroundResource(R.drawable.winter_background_bank);
                taskButton.setImageResource(R.drawable.winter_button_task);
                bankButton.setImageResource(R.drawable.winter_button_bank);
                storeButton.setImageResource(R.drawable.winter_button_store);
                rulesButton.setImageResource(R.drawable.winter_button_rules);
                optionsButton.setImageResource(R.drawable.winter_button_options);
                break;
            case "Standard":
                background.setBackgroundResource(R.drawable.pirate_background_bank_english);
                taskButton.setImageResource(R.drawable.pirate_button_task_english);
                bankButton.setImageResource(R.drawable.pirate_button_bank_english);
                storeButton.setImageResource(R.drawable.pirate_button_store_english);
                rulesButton.setImageResource(R.drawable.pirate_button_rules_english);
                optionsButton.setImageResource(R.drawable.pirate_button_options);
                break;
        }
    }
}
