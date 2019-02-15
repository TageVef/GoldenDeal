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
import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class AdminAddTasksActivity extends AppCompatActivity {
    private static final String TAG = "AdminAddTasksActivity";

    private Button backButton;
    private RecyclerView addTaskRecyclerView;
    private AdminTaskRecyclerAdapter adminTaskRecyclerAdapter;
    private String currentAccess;
    private List<Task> taskList;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_tasks);
        SetupDatabase();
        SetupViews();

        taskList = new ArrayList<Task>();
        addTaskRecyclerView.setHasFixedSize(true);
        addTaskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentAccess = dataSnapshot.getValue(String.class);
                mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("DailyTask");
                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Task currentTask = new Task();
                        for (DataSnapshot tasks : dataSnapshot.getChildren()) {
                            currentTask = tasks.getValue(Task.class);
                            currentTask.setId(Long.parseLong(tasks.getKey()));
                            taskList.add(currentTask);

                        }
                        adminTaskRecyclerAdapter = new AdminTaskRecyclerAdapter(AdminAddTasksActivity.this, taskList);
                        addTaskRecyclerView.setAdapter(adminTaskRecyclerAdapter);
                        adminTaskRecyclerAdapter.notifyDataSetChanged();
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
        backButton = (Button) findViewById(R.id.BackButton);
        addTaskRecyclerView = (RecyclerView) findViewById(R.id.AddTasksRecyclerView);

        View.OnClickListener buttonSwitch = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.BackButton:
                        startActivity(new Intent(AdminAddTasksActivity.this, AdminTasksActivity.class));
                        finish();
                        break;
                }
            }
        };

        backButton.setOnClickListener(buttonSwitch);
    }
}
