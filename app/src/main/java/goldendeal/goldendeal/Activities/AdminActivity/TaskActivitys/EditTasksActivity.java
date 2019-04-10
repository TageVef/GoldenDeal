package goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Data.AdminData.EditTaskRecyclerAdapter;
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
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tasks);
        SetupDatabase();
        SetupViews();
        SetupLanguage();

        taskList = new ArrayList<Task>();
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentAccess = dataSnapshot.getValue(String.class);
                mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Tasks");
                mDatabaseReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        Task currentTask = dataSnapshot.getValue(Task.class);
                        currentTask.setId(Long.parseLong(dataSnapshot.getKey()));
                        taskList.add(currentTask);

                        editTaskRecyclerAdapter = new EditTaskRecyclerAdapter(EditTasksActivity.this, taskList);
                        taskRecyclerView.setAdapter(editTaskRecyclerAdapter);
                        editTaskRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        int position = Integer.parseInt(dataSnapshot.getKey());
                        editTaskRecyclerAdapter.taskList.set(position, dataSnapshot.getValue(Task.class));
                        editTaskRecyclerAdapter.notifyItemChanged(position);
                        editTaskRecyclerAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        int position = Integer.parseInt(dataSnapshot.getKey());
                        editTaskRecyclerAdapter.taskList.remove(position);
                        editTaskRecyclerAdapter.notifyItemRemoved(position);
                        editTaskRecyclerAdapter.notifyItemRangeChanged(position, editTaskRecyclerAdapter.taskList.size());
                        editTaskRecyclerAdapter.notifyDataSetChanged();
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

    private void SetupViews() {
        backButton = (Button) findViewById(R.id.BackButton);
        addNewTaskButton = (Button) findViewById(R.id.AddNewTask);
        taskRecyclerView = (RecyclerView) findViewById(R.id.TaskRecyclerView);

        View.OnClickListener buttonSwitch = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.BackButton:
                        finish();
                        break;
                    case R.id.AddNewTask:
                        startActivity(new Intent(EditTasksActivity.this, AddNewTaskActivity.class));
                        break;
                }
            }
        };

        backButton.setOnClickListener(buttonSwitch);
        addNewTaskButton.setOnClickListener(buttonSwitch);
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
                    addNewTaskButton.setText("Legg Til Ny Oppgave");
                } else if(TextUtils.equals(language, "English")){
                    backButton.setText("Back");
                    addNewTaskButton.setText("Add new Task");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
