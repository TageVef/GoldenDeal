package goldendeal.goldendeal.Activities.AdminActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import goldendeal.goldendeal.R;

public class AddNewTaskActivity extends AppCompatActivity {
    private Button backButton;
    private Button addTask;
    private EditText taskTitle;
    private EditText taskDescription;
    private EditText rewardValue;
    private EditText rewardType;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        SetupViews();
        SetupDatabase();
    }

    private void SetupViews() {
        backButton = (Button) findViewById(R.id.BackButton);
        addTask = (Button) findViewById(R.id.AddNewTask);
        taskTitle = (EditText) findViewById(R.id.TitleET);
        taskDescription = (EditText) findViewById(R.id.DescriptionET);
        rewardValue = (EditText) findViewById(R.id.RewardValue);
        rewardType = (EditText) findViewById(R.id.RewardTitle);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int buttonID = view.getId();
                switch (buttonID) {

                }
            }
        };

    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }
}
