package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Intent;
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
import goldendeal.goldendeal.Data.UserData.TaskRecyclerAdapter;
import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class TasksActivity extends AppCompatActivity {

    private static final String TAG = "TasksActivity";

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private ImageView taskButton;
    private ImageView storeButton;
    private ImageView bankButton;
    private ImageView rulesButton;
    private ImageView optionsButton;
    private ImageView faceButton;
    private RecyclerView recyclerView;

    private TaskRecyclerAdapter taskRecyclerAdapter;
    private List<Task> taskList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        SetupViews();
        SetupDatabase();
        mDatabaseReference.keepSynced(true);

        mDatabaseReference = mDatabase.getReference();

        taskList = new ArrayList<Task>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("DailyTasks");

        mDatabaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Task currentTask = dataSnapshot.getValue(Task.class);
                currentTask.setId(Long.parseLong(dataSnapshot.getKey()));
                taskList.add(currentTask);

                taskRecyclerAdapter = new TaskRecyclerAdapter(TasksActivity.this, taskList);
                recyclerView.setAdapter(taskRecyclerAdapter);
                taskRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                int position = Integer.parseInt(dataSnapshot.getKey());
                taskRecyclerAdapter.taskList.set(position, dataSnapshot.getValue(Task.class));
                taskRecyclerAdapter.notifyItemChanged(position);
                taskRecyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                //Log.d(TAG, "onChildRemoved datasnapshot: " + dataSnapshot.getValue().toString());
                int position = Integer.parseInt(dataSnapshot.getKey());
                if(taskList.get(position) != null){
                    //Log.d(TAG, "onChildRemoved tasklist: " + taskList.get(position).printTask());
                    taskList.remove(position);
                    taskRecyclerAdapter.notifyItemRemoved(position);
                    taskRecyclerAdapter.notifyItemRangeChanged(position, taskList.size());
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildMoved: triggered");
                Intent intent = getIntent();
                startActivity(intent);
                finish();
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
        bankButton = (ImageView) findViewById(R.id.BankButton);
        storeButton = (ImageView) findViewById(R.id.StoreButton);
        rulesButton = (ImageView) findViewById(R.id.RulesButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);
        faceButton = (ImageView) findViewById(R.id.FaceButton);
        recyclerView = (RecyclerView) findViewById(R.id.TaskRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(TasksActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(TasksActivity.this, BankActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(TasksActivity.this, StoreActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(TasksActivity.this, RulesActivity.class));
                        finish();
                        break;
                    case R.id.FaceButton:
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(TasksActivity.this, OptionsActivity.class));
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
}
