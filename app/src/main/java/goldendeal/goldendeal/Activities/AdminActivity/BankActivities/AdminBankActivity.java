package goldendeal.goldendeal.Activities.AdminActivity.BankActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.AdminActivity.RulesActivity.AdminRulesActivity;
import goldendeal.goldendeal.Activities.AdminActivity.StoreActivity.AdminStoreActivity;
import goldendeal.goldendeal.Activities.AdminActivity.PlanActivitys.AdminPlanActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminTasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.AdminData.CurrencyRecyclerAdapter;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class AdminBankActivity extends AppCompatActivity {
    private static final String TAG = "AdminBankActivity";

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private Button taskButton;
    private Button storeButton;
    private Button bankButton;
    private Button rulesButton;
    private Button optionsButton;
    private Button adminButton;

    private TextView currencyText;
    private TextView imageEconomyText;


    private Button newCurrencyButton;

    private RecyclerView currencyRecycler;
    private List<VirtualCurrency> currencyList;
    private CurrencyRecyclerAdapter currencyRecyclerAdapter;
    private RecyclerView imageEconomyRecycler;
    private List<VirtualCurrency> imageEconomyList;
    private CurrencyRecyclerAdapter imageEconomyRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_bank);
        SetupDatabase();
        SetupViews();
        SetupLanguage();

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
                        if(currentCurrency.isImageEconomy()){
                            imageEconomyList.add(currentCurrency);

                            imageEconomyRecyclerAdapter = new CurrencyRecyclerAdapter(AdminBankActivity.this, imageEconomyList);
                            imageEconomyRecycler.setAdapter(imageEconomyRecyclerAdapter);
                            imageEconomyRecyclerAdapter.notifyDataSetChanged();
                        }else{
                            currencyList.add(currentCurrency);

                            currencyRecyclerAdapter = new CurrencyRecyclerAdapter(AdminBankActivity.this, currencyList);
                            currencyRecycler.setAdapter(currencyRecyclerAdapter);
                            currencyRecyclerAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        String currencyTitle = dataSnapshot.getKey();
                        for (int i = 0; i < currencyList.size(); i++){
                            if(TextUtils.equals(currencyList.get(i).getTitle(), currencyTitle)){
                                currencyRecyclerAdapter.currencyList.set(i, dataSnapshot.getValue(VirtualCurrency.class));
                                currencyRecyclerAdapter.notifyItemChanged(i);
                                currencyRecyclerAdapter.notifyDataSetChanged();
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
                        String currencyTitle = dataSnapshot.getKey();
                        for (int i = 0; i < currencyList.size(); i++){
                            if(TextUtils.equals(currencyList.get(i).getTitle(), currencyTitle)){
                                currencyRecyclerAdapter.currencyList.remove(i);
                                currencyRecyclerAdapter.notifyItemRemoved(i);
                                currencyRecyclerAdapter.notifyItemRangeChanged(i, currencyRecyclerAdapter.currencyList.size());
                                currencyRecyclerAdapter.notifyDataSetChanged();
                            }
                        }
                        for(int i = 0; i < imageEconomyList.size(); i++){
                            if(TextUtils.equals(imageEconomyList.get(i).getTitle(), currencyTitle)){
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

    private void SetupDatabase() {
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
        adminButton = (Button) findViewById(R.id.AdminButton);
        newCurrencyButton = (Button) findViewById(R.id.NewCurrencyButton);

        currencyText = (TextView) findViewById(R.id.CurrencyTitle);
        imageEconomyText = (TextView) findViewById(R.id.ImageEconomyTitle);

        currencyRecycler = (RecyclerView) findViewById(R.id.CurrencyRecycler);
        imageEconomyRecycler = (RecyclerView) findViewById(R.id.ImageEconomyRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(AdminBankActivity.this, AdminTasksActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(AdminBankActivity.this, AdminBankActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(AdminBankActivity.this, AdminStoreActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(AdminBankActivity.this, AdminRulesActivity.class));
                        finish();
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(AdminBankActivity.this, OptionsActivity.class));
                        break;
                    case R.id.AdminButton:
                        startActivity(new Intent(AdminBankActivity.this, AdminPlanActivity.class));
                        break;
                    case R.id.NewCurrencyButton:
                        startActivity(new Intent(AdminBankActivity.this, NewCurrencyActivity.class));
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

    private void SetupLanguage(){
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String language = dataSnapshot.getValue(String.class);

                if(TextUtils.equals(language, "Norsk")){
                    taskButton.setText("Oppgaver");
                    storeButton.setText("Butikk");
                    bankButton.setText("Bank");
                    rulesButton.setText("Regler");
                    optionsButton.setText("Instillinger");
                    adminButton.setText("Velg Plan");
                    newCurrencyButton.setText("Ny Valuta");
                    currencyText.setText("Valuta");
                    imageEconomyText.setText("Bilde Økonomi");
                } else if(TextUtils.equals(language, "English")){
                    taskButton.setText("Tasks");
                    storeButton.setText("Store");
                    bankButton.setText("Bank");
                    rulesButton.setText("Rules");
                    optionsButton.setText("Options");
                    adminButton.setText("Choose Plan");
                    newCurrencyButton.setText("New currency");
                    currencyText.setText("Currency");
                    imageEconomyText.setText("Image Economy");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
