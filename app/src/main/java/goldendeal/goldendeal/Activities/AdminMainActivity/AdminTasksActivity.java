package goldendeal.goldendeal.Activities.AdminMainActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
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

import goldendeal.goldendeal.Activities.MainActivities.BankActivity;
import goldendeal.goldendeal.Activities.MainActivities.RulesActivity;
import goldendeal.goldendeal.Activities.MainActivities.StoreActivity;
import goldendeal.goldendeal.Activities.MainActivities.TasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.R;

public class AdminTasksActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_admin_tasks);

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

/*        mDatabaseReference.child("Users").child(mUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                //Toast.makeText(AdminTasksActivity.this, currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                currentUser = null;
                Log.w(TAG, "onCancelled: Failed to read value.", databaseError.toException());
            }
        });*/


        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton switchButton = (ImageButton) view;
                int buttonText = view.getId();
                switch (buttonText) {
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
                    case R.id.optionsButton:
                        startActivity(new Intent(AdminTasksActivity.this, OptionsActivity.class));
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
