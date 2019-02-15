package goldendeal.goldendeal.Activities.AdminActivity;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

import goldendeal.goldendeal.Activities.AdminActivity.MainActivity.AdminPlanActivity;
import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.R;

public class NewUserActivity extends AppCompatActivity {
    private static final String TAG = "NewUserActivity";

    private Button addUserButton;
    private Button backButton;
    private EditText emailView;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        setupDatabase();
        setupViews();
    }

    private void setupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void setupViews() {
        addUserButton = (Button) findViewById(R.id.AddUser);
        backButton = (Button) findViewById(R.id.BackButton);
        emailView = (EditText) findViewById(R.id.EmailView);

        View.OnClickListener buttonSwitch = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.BackButton:
                        startActivity(new Intent(NewUserActivity.this, AdminSelectUserActivity.class));
                        finish();
                        break;
                    case R.id.AddUser:
                        if (!TextUtils.isEmpty(emailView.getText())) {
                            mDatabaseReference = mDatabase.getReference().child("User");
                            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    // Looking through users to find correct user
                                    int counter = 0;
                                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                                        // Checking if the currenct user has the correct mail assigned to it
                                        if (data.child("Info").child("email").getValue(String.class).equalsIgnoreCase(emailView.getText().toString())) {
                                            // Setting up database reference for the admin's Access table
                                            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Access");
                                            // Adds the data to the table through using the userID of the found user, and saving their email address
                                            mDatabaseReference.child(data.getKey()).child("email").setValue(emailView.getText().toString());
                                            Toast.makeText(NewUserActivity.this, "User's ID has been added to Access", Toast.LENGTH_LONG).show();
                                            finish();
                                            break;
                                        } else {
                                            counter++;
                                        }
                                    }
                                    if (counter == dataSnapshot.getChildrenCount()) {
                                        Toast.makeText(NewUserActivity.this, "Could not find user ID", Toast.LENGTH_SHORT).show();
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

        addUserButton.setOnClickListener(buttonSwitch);
        backButton.setOnClickListener(buttonSwitch);
    }
}
