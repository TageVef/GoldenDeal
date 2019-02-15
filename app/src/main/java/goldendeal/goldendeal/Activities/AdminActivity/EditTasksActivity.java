package goldendeal.goldendeal.Activities.AdminActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.AdminActivity.MainActivity.AdminTasksActivity;
import goldendeal.goldendeal.Data.AdminData.AdminTaskRecyclerAdapter;
import goldendeal.goldendeal.Data.AdminData.EditTaskRecyclerAdapter;
import goldendeal.goldendeal.Data.TaskRecyclerAdapter;
import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class EditTasksActivity extends AppCompatActivity {
    private static final String TAG = "EditTasksActivity";

    private Button backButton;
    private Button addNewTaskButton;
    private RecyclerView taskRecyclerView;
    private EditTaskRecyclerAdapter editTaskRecyclerAdapter;
    private List<Task> taskList;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tasks);
        SetupViews();
        SetupDatabase();

        taskList = new ArrayList<Task>();
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentAccess = dataSnapshot.getValue(String.class);
                Log.d(TAG, "onDataChange: accessing user " + currentAccess);
                mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Tasks");
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Task currentTask = new Task();
                        for (DataSnapshot tasks : dataSnapshot.getChildren()) {
                            currentTask = tasks.getValue(Task.class);
                            currentTask.setId(Long.parseLong(tasks.getKey()));
                            taskList.add(currentTask);

                        }
                        Log.d(TAG, "onDataChange: " + taskList.size());
                        editTaskRecyclerAdapter = new EditTaskRecyclerAdapter(EditTasksActivity.this, taskList);
                        taskRecyclerView.setAdapter(editTaskRecyclerAdapter);
                        editTaskRecyclerAdapter.notifyDataSetChanged();
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

    private void SetupViews() {
        backButton = (Button) findViewById(R.id.BackButton);
        addNewTaskButton = (Button) findViewById(R.id.AddNewTask);
        taskRecyclerView = (RecyclerView) findViewById(R.id.TaskRecyclerView);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton switchButton = (ImageButton) view;
                int buttonText = view.getId();
                switch (buttonText) {
                    case R.id.BackButton:
                        startActivity(new Intent(EditTasksActivity.this, AdminTasksActivity.class));
                        finish();
                        break;
                    case R.id.AddNewTask:
                        startActivity(new Intent(EditTasksActivity.this, AddNewTaskActivity.class));
                        finish();
                        break;
                }
            }
        };

        backButton.setOnClickListener(switchPage);
        addNewTaskButton.setOnClickListener(switchPage);
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }
}
