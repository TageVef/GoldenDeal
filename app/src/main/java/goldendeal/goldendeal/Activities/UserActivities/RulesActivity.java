package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.UserData.RulesRecyclerAdapter;
import goldendeal.goldendeal.R;

public class RulesActivity extends AppCompatActivity {
    private static final String TAG = "RulesActivity";

    private ImageView taskButton;
    private ImageView storeButton;
    private ImageView bankButton;
    private ImageView rulesButton;
    private ImageView faceButton;
    private ImageView optionsButton;

    private List<String> ruleList;
    private RecyclerView rulesRecycler;
    private RulesRecyclerAdapter rulesRecyclerAdapter;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        SetupDatabase();
        SetupViews();

        ruleList = new ArrayList<String>();
        rulesRecycler.hasFixedSize();
        rulesRecycler.setLayoutManager(new LinearLayoutManager(this));

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Rules");
        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ruleList.add(dataSnapshot.getValue(String.class));
                rulesRecyclerAdapter = new RulesRecyclerAdapter(ruleList, RulesActivity.this);
                rulesRecycler.setAdapter(rulesRecyclerAdapter);
                rulesRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int position = Integer.getInteger(dataSnapshot.getKey());
                rulesRecyclerAdapter.rulesList.set(position, dataSnapshot.getValue(String.class));
                rulesRecyclerAdapter.notifyItemChanged(position);
                rulesRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                int position = Integer.getInteger(dataSnapshot.getKey());
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
        faceButton = (ImageView) findViewById(R.id.FaceButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);

        rulesRecycler = (RecyclerView) findViewById(R.id.RulesRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(RulesActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(RulesActivity.this, StoreActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(RulesActivity.this, BankActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(RulesActivity.this, RulesActivity.class));
                        finish();
                        break;
                    case R.id.FaceButton:
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(RulesActivity.this, OptionsActivity.class));
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
        faceButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);

    }
}
