package goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys;

import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class AddNewTaskActivity extends AppCompatActivity {
    private static final String TAG = "AddNewTaskActivity";
    private Button backButton;
    private Button addTask;
    private EditText taskTitle;
    private EditText taskDescription;
    private EditText rewardValue;
    private TextView rewardType;
    private String taskID;

    private List<String> currencyList;

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
        SetupLanguage();

        currencyList = new ArrayList<String>();

        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentAccess = dataSnapshot.getValue(String.class);
                mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Bank");
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot currency: dataSnapshot.getChildren()){
                            currencyList.add(currency.getKey());
                        }
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
        addTask = (Button) findViewById(R.id.AddTaskButton);
        taskTitle = (EditText) findViewById(R.id.TitleET);
        taskDescription = (EditText) findViewById(R.id.DescriptionET);
        rewardValue = (EditText) findViewById(R.id.RewardValueET);
        rewardType = (TextView) findViewById(R.id.RewardTitleET);

        CheckingIfTaskIsSelected();

        rewardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(AddNewTaskActivity.this, v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        rewardType.setText(item.getTitle());

                        return false;
                    }
                });

                for(int i = 0; i < currencyList.size(); i++){
                    popup.getMenu().add(currencyList.get(i));
                }
                popup.show();
            }
        });


        View.OnClickListener buttonSwitch = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.BackButton:
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
        finish();
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupLanguage(){
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String language = dataSnapshot.getValue(String.class);

                if(TextUtils.equals(language, "Norsk")){
                    backButton.setText("Tilbake");
                    addTask.setText("Legg Til Oppgave");
                    taskTitle.setHint("Oppgave Titel");
                    taskDescription.setHint("Oppgave Beskrivelse");
                    rewardValue.setHint("Belønnings Mengde");
                    rewardType.setHint("Belønnings type");
                } else if(TextUtils.equals(language, "English")){
                    backButton.setText("Back");
                    addTask.setText("Add Task");
                    taskTitle.setHint("Task Title");
                    taskDescription.setHint("Task Description");
                    rewardValue.setHint("Reward");
                    rewardType.setHint("Reward Type");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
