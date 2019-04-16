package goldendeal.goldendeal.Activities.AdminActivity.StoreActivity;

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

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.AdminActivity.BankActivities.AdminBankActivity;
import goldendeal.goldendeal.Activities.AdminActivity.PlanActivitys.AdminPlanActivity;
import goldendeal.goldendeal.Activities.AdminActivity.RulesActivity.AdminRulesActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminAddTasksActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminTasksActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.EditTasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.AdminData.AdminStoreRecyclerAdapter;
import goldendeal.goldendeal.Model.StoreItem;
import goldendeal.goldendeal.R;

public class AdminStoreActivity extends AppCompatActivity {
    private static final String TAG = "AdminStoreActivity";

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
    private Button addRewardButton;
    private TextView titleText;

    private List<StoreItem> itemList;
    private RecyclerView rewardRecycler;
    private AdminStoreRecyclerAdapter storeItemRecyclerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_store);
        SetupDatabase();
        SetupViews();
        SetupLanguage();

        itemList = new ArrayList<StoreItem>();
        rewardRecycler.hasFixedSize();
        rewardRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentAccess = dataSnapshot.getValue(String.class);

                mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Store");
                mDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        itemList.add(dataSnapshot.getValue(StoreItem.class));

                        storeItemRecyclerAdapter = new AdminStoreRecyclerAdapter(itemList, AdminStoreActivity.this);
                        rewardRecycler.setAdapter(storeItemRecyclerAdapter);
                        storeItemRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        int position = Integer.parseInt(dataSnapshot.getKey());
                        storeItemRecyclerAdapter.itemList.set(position, dataSnapshot.getValue(StoreItem.class));
                        storeItemRecyclerAdapter.notifyItemChanged(position);
                        storeItemRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        int position = Integer.parseInt(dataSnapshot.getKey());
                        storeItemRecyclerAdapter.itemList.remove(position);
                        storeItemRecyclerAdapter.notifyItemRemoved(position);
                        storeItemRecyclerAdapter.notifyItemRangeChanged(position, storeItemRecyclerAdapter.itemList.size());
                        storeItemRecyclerAdapter.notifyDataSetChanged();
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
        addRewardButton = (Button) findViewById(R.id.AddButton);
        titleText = (TextView) findViewById(R.id.TitleText);

        rewardRecycler = (RecyclerView) findViewById(R.id.RewardRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminTasksActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminBankActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminStoreActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminRulesActivity.class));
                        finish();
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(AdminStoreActivity.this, OptionsActivity.class));
                        break;
                    case R.id.AdminButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminPlanActivity.class));
                        break;
                    case R.id.AddButton:
                        startActivity(new Intent( AdminStoreActivity.this, NewRewardActivity.class));
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
        addRewardButton.setOnClickListener(switchPage);
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
                    addRewardButton.setText("Legg Til Belønning");
                    titleText.setText("Butikk");
                } else if(TextUtils.equals(language, "English")){
                    taskButton.setText("Tasks");
                    storeButton.setText("Store");
                    bankButton.setText("Bank");
                    rulesButton.setText("Rules");
                    optionsButton.setText("Options");
                    adminButton.setText("Choose Plan");
                    addRewardButton.setText("Add Reward");
                    titleText.setText("Store");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
