package goldendeal.goldendeal.Activities.MainActivities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import goldendeal.goldendeal.Activities.OptionsActivity;
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

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        mDatabaseReference.keepSynced(true);

        taskButton = (ImageButton) findViewById(R.id.TaskButton);
        storeButton = (ImageButton) findViewById(R.id.StoreButton);
        bankButton = (ImageButton) findViewById(R.id.BankButton);
        rulesButton = (ImageButton) findViewById(R.id.RulesButton);
        optionsButton = (Button) findViewById(R.id.optionsButton);


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
}
