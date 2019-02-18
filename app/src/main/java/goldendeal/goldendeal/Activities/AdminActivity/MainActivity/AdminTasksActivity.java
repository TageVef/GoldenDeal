package goldendeal.goldendeal.Activities.AdminActivity.MainActivity;

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

import goldendeal.goldendeal.Activities.AdminActivity.AddNewTaskActivity;
import goldendeal.goldendeal.Activities.AdminActivity.AdminAddTasksActivity;
import goldendeal.goldendeal.Activities.AdminActivity.EditTasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.AdminData.AdminTaskRecyclerAdapter;
import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.R;

public class AdminTasksActivity extends AppCompatActivity {
    private static final String TAG = "AdminTasksActivity";

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private Button taskButton;
    private Button storeButton;
    private Button bankButton;
    private Button rulesButton;
    private Button optionsButton;
    private Button adminButton;
    private Button addTaskButton;
    private Button editTasksButton;
    private RecyclerView taskRecyclerView;
    private AdminTaskRecyclerAdapter taskRecyclerAdapter;

    private String currentAccess;
    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_tasks);
        SetupDatabase();
        SetupViews();


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
                        taskRecyclerAdapter = new AdminTaskRecyclerAdapter(AdminTasksActivity.this, taskList);
                        taskRecyclerView.setAdapter(taskRecyclerAdapter);
                        taskRecyclerAdapter.notifyDataSetChanged();
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
        mUser = mAuth.getCurrentUser();

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
        addTaskButton = (Button) findViewById(R.id.AddTasksButton);
        editTasksButton = (Button) findViewById(R.id.EditTasksButton);
        taskRecyclerView = (RecyclerView) findViewById(R.id.TaskRecycler);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(AdminTasksActivity.this, AdminTasksActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(AdminTasksActivity.this, AdminStoreActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(AdminTasksActivity.this, AdminBankActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(AdminTasksActivity.this, AdminRulesActivity.class));
                        finish();
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(AdminTasksActivity.this, OptionsActivity.class));
                        finish();
                        break;
                    case R.id.AdminButton:
                        startActivity(new Intent(AdminTasksActivity.this, AdminPlanActivity.class));
                        finish();
                        break;
                    case R.id.AddTasksButton:
                        startActivity(new Intent(AdminTasksActivity.this, AdminAddTasksActivity.class));
                        finish();
                        break;
                    case R.id.EditTasksButton:
                        startActivity(new Intent(AdminTasksActivity.this, EditTasksActivity.class));
                        finish();
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        //storeButton.setOnClickListener(switchPage);
        //bankButton.setOnClickListener(switchPage);
        //rulesButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
        adminButton.setOnClickListener(switchPage);
        addTaskButton.setOnClickListener(switchPage);
        editTasksButton.setOnClickListener(switchPage);
    }
}
