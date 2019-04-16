package goldendeal.goldendeal.Activities.AdminActivity.RulesActivity;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.AdminActivity.BankActivities.AdminBankActivity;
import goldendeal.goldendeal.Activities.AdminActivity.PlanActivitys.AdminPlanActivity;
import goldendeal.goldendeal.Activities.AdminActivity.StoreActivity.AdminStoreActivity;
import goldendeal.goldendeal.Activities.AdminActivity.StoreActivity.NewRewardActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminTasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.AdminData.AdminRulesRecyclerAdapter;
import goldendeal.goldendeal.R;

public class AdminRulesActivity extends AppCompatActivity {
    private static final String TAG = "AdminRulesActivity";

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
    private Button addRulesButton;

    private List<String> ruleList;
    private RecyclerView rulesRecycler;
    private AdminRulesRecyclerAdapter rulesRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rules);

        SetupDatabase();
        SetupViews();

        ruleList = new ArrayList<String>();
        rulesRecycler.hasFixedSize();
        rulesRecycler.setLayoutManager(new LinearLayoutManager(this));


        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentAccess = dataSnapshot.getValue(String.class);
                mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Rules");
                mDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        ruleList.add(dataSnapshot.getValue(String.class));

                        rulesRecyclerAdapter = new AdminRulesRecyclerAdapter(ruleList, AdminRulesActivity.this);
                        rulesRecycler.setAdapter(rulesRecyclerAdapter);
                        rulesRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        int position = Integer.parseInt(dataSnapshot.getKey());
                        rulesRecyclerAdapter.rulesList.set(position, dataSnapshot.getValue(String.class));
                        rulesRecyclerAdapter.notifyItemChanged(position);
                        rulesRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        int position = Integer.parseInt(dataSnapshot.getKey());
                        rulesRecyclerAdapter.rulesList.remove(position);
                        rulesRecyclerAdapter.notifyItemRemoved(position);
                        rulesRecyclerAdapter.notifyItemRangeChanged(position, rulesRecyclerAdapter.rulesList.size());
                        rulesRecyclerAdapter.notifyDataSetChanged();
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

        SetupLanguage();
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
        addRulesButton = (Button) findViewById(R.id.AddButton);

        rulesRecycler = (RecyclerView) findViewById(R.id.RulesRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(AdminRulesActivity.this, AdminTasksActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(AdminRulesActivity.this, AdminBankActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(AdminRulesActivity.this, AdminStoreActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(AdminRulesActivity.this, AdminRulesActivity.class));
                        finish();
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(AdminRulesActivity.this, OptionsActivity.class));
                        break;
                    case R.id.AdminButton:
                        startActivity(new Intent(AdminRulesActivity.this, AdminPlanActivity.class));
                        break;
                    case R.id.AddButton:
                        startActivity(new Intent(AdminRulesActivity.this, NewRuleActivity.class));
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
        addRulesButton.setOnClickListener(switchPage);
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
                    addRulesButton.setText("Legg Til Regel");
                } else if(TextUtils.equals(language, "English")){
                    taskButton.setText("Tasks");
                    storeButton.setText("Store");
                    bankButton.setText("Bank");
                    rulesButton.setText("Rules");
                    optionsButton.setText("Options");
                    adminButton.setText("Choose Plan");
                    addRulesButton.setText("Add Rule");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
