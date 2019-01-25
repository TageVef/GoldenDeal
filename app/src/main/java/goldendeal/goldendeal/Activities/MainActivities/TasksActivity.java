package goldendeal.goldendeal.Activities.MainActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import goldendeal.goldendeal.R;

public class TasksActivity extends AppCompatActivity {

    private static final String TAG = "TasksActivity";

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    private ImageButton taskButton;
    private ImageButton shopButton;
    private ImageButton bankButton;
    private ImageButton rulesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Tasks");
        mDatabaseReference.keepSynced(true);

        taskButton = (ImageButton) findViewById(R.id.TaskButton);
        shopButton = (ImageButton) findViewById(R.id.ShopButton);
        bankButton = (ImageButton) findViewById(R.id.BankButton);
        rulesButton = (ImageButton) findViewById(R.id.RulesButton);

        View.OnClickListener switchPage = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ImageButton switchButton = (ImageButton) view;
                int buttonText = switchButton.getId();
                if(buttonText == taskButton.getId())
                {
                    startActivity(new Intent(TasksActivity.this, TasksActivity.class));
                    finish();
                }
                else if(buttonText == shopButton.getId())
                {
                    startActivity(new Intent(TasksActivity.this, StoreActivity.class));
                    finish();
                }
                else if(buttonText == bankButton.getId())
                {
                    startActivity(new Intent(TasksActivity.this, BankActivity.class));
                    finish();
                }
                else if(buttonText == rulesButton.getId())
                {
                    startActivity(new Intent(TasksActivity.this, RulesActivity.class));
                    finish();
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        shopButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
    }
}
