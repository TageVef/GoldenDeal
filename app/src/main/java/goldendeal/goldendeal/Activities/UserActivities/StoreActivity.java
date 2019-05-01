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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Set;

import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.UserData.CounterRecyclerAdapter;
import goldendeal.goldendeal.Data.UserData.ImageEconomyRecyclerAdapter;
import goldendeal.goldendeal.Data.UserData.StoreRecyclerAdapter;
import goldendeal.goldendeal.Model.StoreItem;
import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class StoreActivity extends AppCompatActivity {
    private static final String TAG = "StoreActivity";

    private User currentUser;
    private List<StoreItem> itemList;

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

    private RecyclerView itemRecycler;
    private StoreRecyclerAdapter storeRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        SetupDatabase();
        SetupViews();

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                SetupTheme();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        SetupDatabase();
        itemList = new ArrayList<StoreItem>();
        itemRecycler.hasFixedSize();
        itemRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Store");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                itemList.add(dataSnapshot.getValue(StoreItem.class));
                storeRecyclerAdapter = new StoreRecyclerAdapter(itemList, StoreActivity.this);
                itemRecycler.setAdapter(storeRecyclerAdapter);
                storeRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int position = Integer.parseInt(dataSnapshot.getKey());
                storeRecyclerAdapter.itemList.set(position, dataSnapshot.getValue(StoreItem.class));
                storeRecyclerAdapter.notifyItemChanged(position);
                storeRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int position = Integer.parseInt(dataSnapshot.getKey());
                storeRecyclerAdapter.itemList.remove(position);
                storeRecyclerAdapter.notifyItemRemoved(position);
                storeRecyclerAdapter.notifyItemRangeChanged(position, storeRecyclerAdapter.itemList.size());
                storeRecyclerAdapter.notifyDataSetChanged();
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
    protected void onRestart() {
        super.onRestart();
        SetupDatabase();
        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
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
        background = (ConstraintLayout) findViewById(R.id.StoreLayout);
        taskButton = (ImageView) findViewById(R.id.TaskButton);
        storeButton = (ImageView) findViewById(R.id.StoreButton);
        bankButton = (ImageView) findViewById(R.id.BankButton);
        rulesButton = (ImageView) findViewById(R.id.RulesButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);
        itemRecycler = (RecyclerView) findViewById(R.id.StoreRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(StoreActivity.this, StoreActivity.class);
                switch (view.getId()) {
                    case R.id.TaskButton:
                        newIntent = new Intent(StoreActivity.this, TasksActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.BankButton:
                        newIntent = new Intent(StoreActivity.this, BankActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.RulesButton:
                        newIntent = new Intent(StoreActivity.this, RulesActivity.class);
                        startActivity(newIntent);
                        finish();
                        break;
                    case R.id.OptionsButton:
                        newIntent = new Intent(StoreActivity.this, OptionsActivity.class);
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
                background.setBackgroundResource(R.drawable.mermaids_background_store_english);
                taskButton.setImageResource(R.drawable.mermaids_button_task_english);
                bankButton.setImageResource(R.drawable.mermaids_button_bank_english);
                storeButton.setImageResource(R.drawable.mermaids_button_store_english);
                rulesButton.setImageResource(R.drawable.mermaids_button_rules_english);
                optionsButton.setImageResource(R.drawable.mermaids_button_options);
                break;
            case "Western":
                background.setBackgroundResource(R.drawable.western_background_store_english);
                taskButton.setImageResource(R.drawable.western_button_task_english);
                bankButton.setImageResource(R.drawable.western_button_bank_english);
                storeButton.setImageResource(R.drawable.western_button_store_english);
                rulesButton.setImageResource(R.drawable.western_button_rules_english);
                optionsButton.setImageResource(R.drawable.western_button_options);
                break;
            case "Space":
                background.setBackgroundResource(R.drawable.space_background_store_english);
                taskButton.setImageResource(R.drawable.pirate_button_task_english);
                bankButton.setImageResource(R.drawable.pirate_button_bank_english);
                storeButton.setImageResource(R.drawable.pirate_button_store_english);
                rulesButton.setImageResource(R.drawable.pirate_button_rules_english);
                optionsButton.setImageResource(R.drawable.pirate_button_options);
                break;
            case "Season":
                background.setBackgroundResource(R.drawable.season_background_night_english);
                taskButton.setImageResource(R.drawable.pirate_button_task_english);
                bankButton.setImageResource(R.drawable.pirate_button_bank_english);
                storeButton.setImageResource(R.drawable.pirate_button_store_english);
                rulesButton.setImageResource(R.drawable.pirate_button_rules_english);
                optionsButton.setImageResource(R.drawable.pirate_button_options);
                break;
            case "Standard":
                background.setBackgroundResource(R.drawable.pirate_background_store_english);
                taskButton.setImageResource(R.drawable.pirate_button_task_english);
                bankButton.setImageResource(R.drawable.pirate_button_bank_english);
                storeButton.setImageResource(R.drawable.pirate_button_store_english);
                rulesButton.setImageResource(R.drawable.pirate_button_rules_english);
                optionsButton.setImageResource(R.drawable.pirate_button_options);
                break;
        }
    }
}
