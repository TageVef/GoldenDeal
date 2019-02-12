package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.TaskRecyclerAdapter;
import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.R;

public class TasksActivity extends AppCompatActivity {

    private static final String TAG = "TasksActivity";

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private ImageButton taskButton;
    private ImageButton storeButton;
    private ImageButton bankButton;
    private ImageButton rulesButton;
    private Button optionsButton;
    private RecyclerView recyclerView;

    private User currentUser;
    private TaskRecyclerAdapter taskRecyclerAdapter;
    private List<Task> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        setupViews();
        setupDatabase();
        mDatabaseReference.keepSynced(true);

        GettingCurrentUser();

        mDatabaseReference = mDatabase.getReference();

        taskList = new ArrayList<Task>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("DailyTask");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Task currentTask = new Task();
                for(DataSnapshot task : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: key " + task.getKey());
                    Log.d(TAG, "onDataChange: data " + task.getValue());
                    /*for(DataSnapshot data : dataSnapshot.getChildren()){
                        switch(data.getKey()){
                            case "title":
                                currentTask.setTitle(data.getValue(String.class));
                                Log.d(TAG, "onChildAdded: 1 " + data.getValue());
                                break;
                            case "description":
                                currentTask.setDescription(data.getValue(String.class));
                                Log.d(TAG, "onChildAdded: 2 " + data.getValue());
                                break;
                            case "rewardValue":
                                currentTask.setRewardValue(data.getValue(long.class).toString());
                                Log.d(TAG, "onChildAdded: 3 " + data.getValue());
                                break;
                            case "rewardTitle":
                                currentTask.setRewardTitle(data.getValue(String.class));
                                Log.d(TAG, "onChildAdded: 4 " + data.getValue());
                                break;
                        }
                    }*/
                    currentTask = task.getValue(Task.class);
                    taskList.add(currentTask);
                }


                taskRecyclerAdapter = new TaskRecyclerAdapter(TasksActivity.this, taskList);
                recyclerView.setAdapter(taskRecyclerAdapter);
                taskRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void setupViews() {
        taskButton = (ImageButton) findViewById(R.id.TaskButton);
        storeButton = (ImageButton) findViewById(R.id.StoreButton);
        bankButton = (ImageButton) findViewById(R.id.BankButton);
        rulesButton = (ImageButton) findViewById(R.id.RulesButton);
        optionsButton = (Button) findViewById(R.id.optionsButton);
        recyclerView = (RecyclerView) findViewById(R.id.TaskRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton switchButton = (ImageButton) view;
                int buttonText = view.getId();
                switch (buttonText) {
                    case R.id.TaskButton:
                        startActivity(new Intent(TasksActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(TasksActivity.this, StoreActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(TasksActivity.this, BankActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(TasksActivity.this, RulesActivity.class));
                        finish();
                        break;
                    case R.id.optionsButton:
                        startActivity(new Intent(TasksActivity.this, OptionsActivity.class));
                        finish();
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
    }

    private void GettingCurrentUser() {
        mDatabaseReference = mDatabase.getReference().child("User").child(mUser.getUid()).child("Info");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                if (currentUser != null) {
                    currentUser.setUserID(mUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "failed to retrieve user in TasksActivity");
            }
        });


    }
}
