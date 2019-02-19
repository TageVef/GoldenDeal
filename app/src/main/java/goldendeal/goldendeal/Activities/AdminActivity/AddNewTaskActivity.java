package goldendeal.goldendeal.Activities.AdminActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.util.NumberUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class AddNewTaskActivity extends AppCompatActivity {
    private static final String TAG = "AddNewTaskActivity";
    private Button backButton;
    private Button addTask;
    private EditText taskTitle;
    private EditText taskDescription;
    private EditText rewardValue;
    private EditText rewardType;
    private String taskID;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        taskID = getIntent().getStringExtra("TaskID");
        SetupDatabase();
        SetupViews();


    }

    private void SetupViews() {
        backButton = (Button) findViewById(R.id.BackButton);
        addTask = (Button) findViewById(R.id.AddTaskButton);
        taskTitle = (EditText) findViewById(R.id.TitleET);
        taskDescription = (EditText) findViewById(R.id.DescriptionET);
        rewardValue = (EditText) findViewById(R.id.RewardValueET);
        rewardType = (EditText) findViewById(R.id.RewardTitleET);

        CheckingIfTaskIsSelected();

        View.OnClickListener buttonSwitch = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.BackButton:
                        startActivity(new Intent(AddNewTaskActivity.this, EditTasksActivity.class));
                        finish();
                        break;
                    case R.id.AddTaskButton:
                        if (!TextUtils.isEmpty(taskTitle.getText()) && !TextUtils.isEmpty(taskDescription.getText())
                                && !TextUtils.isEmpty(rewardValue.getText()) && !TextUtils.isEmpty(rewardType.getText())) {

                            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String currentAccess = dataSnapshot.getValue(String.class);

                                    if (!TextUtils.isEmpty(taskID)) {
                                        mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Tasks").child(taskID);
                                        WritingTaskToDatabase();
                                        Toast.makeText(AddNewTaskActivity.this, "Task Updated", Toast.LENGTH_SHORT).show();

                                    } else {
                                        mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Tasks");
                                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                long numberOfTasks = dataSnapshot.getChildrenCount();
                                                mDatabaseReference = mDatabaseReference.child(Long.toString(numberOfTasks));
                                                WritingTaskToDatabase();
                                                Toast.makeText(AddNewTaskActivity.this, "Task Created", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        break;
                }
            }
        };

        backButton.setOnClickListener(buttonSwitch);
        addTask.setOnClickListener(buttonSwitch);

    }

    private void CheckingIfTaskIsSelected() {
        if (!TextUtils.isEmpty(taskID)) {
            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentAccess = dataSnapshot.getValue(String.class);
                    mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Tasks").child(taskID);
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Task currentTask = dataSnapshot.getValue(Task.class);
                            taskTitle.setText(currentTask.getTitle());
                            taskDescription.setText(currentTask.getDescription());
                            rewardValue.setText(Long.toString(currentTask.getRewardValue()));
                            rewardType.setText(currentTask.getRewardTitle());
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
    }

    private void WritingTaskToDatabase() {
        mDatabaseReference.child("title").setValue(taskTitle.getText().toString());
        mDatabaseReference.child("description").setValue(taskDescription.getText().toString());
        mDatabaseReference.child("rewardValue").setValue(Long.parseLong(rewardValue.getText().toString()));
        mDatabaseReference.child("rewardTitle").setValue(rewardType.getText().toString());
        startActivity(new Intent(AddNewTaskActivity.this, EditTasksActivity.class));
        finish();
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }
}
