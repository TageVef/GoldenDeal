package goldendeal.goldendeal.Activities.AdminActivity.BankActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import goldendeal.goldendeal.Activities.AdminActivity.RulesActivity.AdminRulesActivity;
import goldendeal.goldendeal.Activities.AdminActivity.StoreActivity.AdminStoreActivity;
import goldendeal.goldendeal.Activities.AdminActivity.PlanActivitys.AdminPlanActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminTasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.AdminData.CurrencyRecyclerAdapter;
import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class AdminBankActivity extends AppCompatActivity {
    private static final String TAG = "AdminBankActivity";
    private User currentUser;
    private List<VirtualCurrency> currencyList;
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
    private Button adminButton;
    private Button newCurrencyButton;

    private TextView currencyText;
    private TextView imageEconomyText;

    private RecyclerView currencyRecycler;
    private CurrencyRecyclerAdapter currencyRecyclerAdapter;
    private RecyclerView imageEconomyRecycler;
    private CurrencyRecyclerAdapter imageEconomyRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bank);
        SetupDatabase();
        SetupViews();

        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                SetupLanguage();
                SetupTheme();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        currencyList = new ArrayList<VirtualCurrency>();
        currencyRecycler.hasFixedSize();
        currencyRecycler.setLayoutManager(new LinearLayoutManager(this));
        imageEconomyList = new ArrayList<VirtualCurrency>();
        imageEconomyRecycler.hasFixedSize();
        imageEconomyRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentAccess = dataSnapshot.getValue(String.class);
                mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Bank");
                mDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        VirtualCurrency currentCurrency = dataSnapshot.getValue(VirtualCurrency.class);
                        if (currentCurrency.isImageEconomy()) {
                            imageEconomyList.add(currentCurrency);

                            imageEconomyRecyclerAdapter = new CurrencyRecyclerAdapter(AdminBankActivity.this, imageEconomyList);
                            imageEconomyRecycler.setAdapter(imageEconomyRecyclerAdapter);
                            imageEconomyRecyclerAdapter.notifyDataSetChanged();
                        } else {
                            currencyList.add(currentCurrency);

                            currencyRecyclerAdapter = new CurrencyRecyclerAdapter(AdminBankActivity.this, currencyList);
                            currencyRecycler.setAdapter(currencyRecyclerAdapter);
                            currencyRecyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String currencyTitle = dataSnapshot.getKey();
                        for (int i = 0; i < currencyList.size(); i++) {
                            if (TextUtils.equals(currencyList.get(i).getTitle(), currencyTitle)) {
                                currencyRecyclerAdapter.currencyList.set(i, dataSnapshot.getValue(VirtualCurrency.class));
                                currencyRecyclerAdapter.notifyItemChanged(i);
                                currencyRecyclerAdapter.notifyDataSetChanged();
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
                        for (int i = 0; i < currencyList.size(); i++) {
                            if (TextUtils.equals(currencyList.get(i).getTitle(), currencyTitle)) {
                                currencyRecyclerAdapter.currencyList.remove(i);
                                currencyRecyclerAdapter.notifyItemRemoved(i);
                                currencyRecyclerAdapter.notifyItemRangeChanged(i, currencyRecyclerAdapter.currencyList.size());
                                currencyRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                        for (int i = 0; i < imageEconomyList.size(); i++) {
                            if (TextUtils.equals(imageEconomyList.get(i).getTitle(), currencyTitle)) {
                                imageEconomyRecyclerAdapter.currencyList.remove(i);
                                imageEconomyRecyclerAdapter.notifyItemRemoved(i);
                                imageEconomyRecyclerAdapter.notifyItemRangeChanged(i, currencyRecyclerAdapter.currencyList.size());
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
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                SetupLanguage();
                SetupTheme();
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
        background = (ConstraintLayout) findViewById(R.id.AdminRulesLayout);
        taskButton = (ImageView) findViewById(R.id.TaskButton);
        storeButton = (ImageView) findViewById(R.id.StoreButton);
        bankButton = (ImageView) findViewById(R.id.BankButton);
        rulesButton = (ImageView) findViewById(R.id.RulesButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);
        adminButton = (Button) findViewById(R.id.AdminButton);
        newCurrencyButton = (Button) findViewById(R.id.NewCurrencyButton);

        currencyText = (TextView) findViewById(R.id.CurrencyTitle);
        imageEconomyText = (TextView) findViewById(R.id.ImageEconomyTitle);

        currencyRecycler = (RecyclerView) findViewById(R.id.CurrencyRecycler);
        imageEconomyRecycler = (RecyclerView) findViewById(R.id.ImageEconomyRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(AdminBankActivity.this, AdminBankActivity.class);
                switch (view.getId()) {
                    case R.id.TaskButton:
                        newIntent = new Intent(AdminBankActivity.this, AdminTasksActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.StoreButton:
                        newIntent = new Intent(AdminBankActivity.this, AdminStoreActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.RulesButton:
                        newIntent = new Intent(AdminBankActivity.this, AdminRulesActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.OptionsButton:
                        newIntent = new Intent(AdminBankActivity.this, OptionsActivity.class);
                        newIntent.putExtra("Role", true);
                        startActivity(newIntent);
                        break;
                    case R.id.AdminButton:
                        newIntent = new Intent(AdminBankActivity.this, AdminPlanActivity.class);
                        startActivity(newIntent);
                        break;
                    case R.id.NewCurrencyButton:
                        newIntent = new Intent(AdminBankActivity.this, NewCurrencyActivity.class);
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
        adminButton.setOnClickListener(switchPage);
        newCurrencyButton.setOnClickListener(switchPage);
    }

    private void SetupLanguage() {

        switch (currentUser.getLanguage()) {
            case "Norsk":
                adminButton.setText("Velg Plan");
                newCurrencyButton.setText("Ny Valuta");
                currencyText.setText("Valuta");
                imageEconomyText.setText("Bilde Økonomi");
                break;
            case "English":
                adminButton.setText("Choose Plan");
                newCurrencyButton.setText("New currency");
                currencyText.setText("Currency");
                imageEconomyText.setText("Image Economy");
                break;
        }
    }

    private void SetupTheme() {
        switch (currentUser.getTheme()) {
            case "Mermaids":
                background.setBackgroundResource(R.drawable.mermaids_background_bank_english);
                optionsButton.setImageResource(R.drawable.mermaids_button_options);
                taskButton.setImageResource(R.drawable.mermaids_button_task_english);
                bankButton.setImageResource(R.drawable.mermaids_button_bank_english);
                storeButton.setImageResource(R.drawable.mermaids_button_store_english);
                rulesButton.setImageResource(R.drawable.mermaids_button_rules_english);
                break;
            case "Western":
                background.setBackgroundResource(R.drawable.western_background_bank_english);
                optionsButton.setImageResource(R.drawable.western_button_options);
                taskButton.setImageResource(R.drawable.western_button_task_english);
                bankButton.setImageResource(R.drawable.western_button_bank_english);
                storeButton.setImageResource(R.drawable.western_button_store_english);
                rulesButton.setImageResource(R.drawable.western_button_rules_english);
                break;
            case "Space":
                background.setBackgroundResource(R.drawable.space_background_bank_english);
                optionsButton.setImageResource(R.drawable.space_button_options);
                taskButton.setImageResource(R.drawable.space_button_task_english);
                bankButton.setImageResource(R.drawable.space_button_bank_english);
                storeButton.setImageResource(R.drawable.space_button_store_english);
                rulesButton.setImageResource(R.drawable.space_button_rules_english);
                break;
            case "Spring":
                background.setBackgroundResource(R.drawable.spring_background_bank);
                optionsButton.setImageResource(R.drawable.spring_button_options);
                taskButton.setImageResource(R.drawable.spring_button_task);
                bankButton.setImageResource(R.drawable.spring_button_bank);
                storeButton.setImageResource(R.drawable.spring_button_store);
                rulesButton.setImageResource(R.drawable.spring_button_rules);
                break;
            case "Summer":
                background.setBackgroundResource(R.drawable.summer_background_bank);
                optionsButton.setImageResource(R.drawable.summer_button_options);
                taskButton.setImageResource(R.drawable.summer_button_task);
                bankButton.setImageResource(R.drawable.summer_button_bank);
                storeButton.setImageResource(R.drawable.summer_button_store);
                rulesButton.setImageResource(R.drawable.summer_button_rules);
                break;
            case "Fall":
                background.setBackgroundResource(R.drawable.fall_background_bank);
                optionsButton.setImageResource(R.drawable.fall_button_options);
                taskButton.setImageResource(R.drawable.fall_button_task);
                bankButton.setImageResource(R.drawable.fall_button_bank);
                storeButton.setImageResource(R.drawable.fall_button_store);
                rulesButton.setImageResource(R.drawable.fall_button_rules);
                break;
            case "Winter":
                background.setBackgroundResource(R.drawable.winter_background_bank);
                optionsButton.setImageResource(R.drawable.winter_button_options);
                taskButton.setImageResource(R.drawable.winter_button_task);
                bankButton.setImageResource(R.drawable.winter_button_bank);
                storeButton.setImageResource(R.drawable.winter_button_store);
                rulesButton.setImageResource(R.drawable.winter_button_rules);
                break;
            case "Standard":
                background.setBackgroundResource(R.drawable.pirate_background_bank_english);
                optionsButton.setImageResource(R.drawable.pirate_button_options);
                taskButton.setImageResource(R.drawable.pirate_button_task_english);
                bankButton.setImageResource(R.drawable.pirate_button_bank_english);
                storeButton.setImageResource(R.drawable.pirate_button_store_english);
                rulesButton.setImageResource(R.drawable.pirate_button_rules_english);
                break;
        }
    }
}
