package goldendeal.goldendeal.Activities.AdminActivity.PlanActivitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import goldendeal.goldendeal.R;

public class AdminPlanActivity extends AppCompatActivity {
    private static final String TAG = "AdminPlanActivity";

    private TextView currentUserMail;
    private Button editUserButton;
    private Button backButton;
    private TextView userText;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_plan);
        SetupViews();
        SetupDatabase();
        SetupLanguage();

        FindingUsersEmailAddress();

    }

    private void SetupViews() {
        currentUserMail = (TextView) findViewById(R.id.UserEmail);
        editUserButton = (Button) findViewById(R.id.EditUserButton);
        backButton = (Button) findViewById(R.id.BackButton);
        userText = (TextView) findViewById(R.id.User);

        View.OnClickListener buttonSwitch = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.EditUserButton:
                        startActivity(new Intent(AdminPlanActivity.this, AdminSelectUserActivity.class));
                        break;
                    case R.id.BackButton:
                        finish();
                        break;
                }
            }
        };

        editUserButton.setOnClickListener(buttonSwitch);
        backButton.setOnClickListener(buttonSwitch);
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void FindingUsersEmailAddress() {
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userID = dataSnapshot.getValue(String.class);


                mDatabaseReference = mDatabase.getReference().child("User").child(userID).child("Info").child("email");
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentUserMail.setText(dataSnapshot.getValue(String.class));
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

    private void SetupLanguage(){
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String language = dataSnapshot.getValue(String.class);

                if(TextUtils.equals(language, "Norsk")){
                    backButton.setText("Tilbake");
                    editUserButton.setText("Velg Bruker");
                    userText.setText("Aktive Bruker: ");
                } else if(TextUtils.equals(language, "English")){
                    backButton.setText("Back");
                    editUserButton.setText("Choose User");
                    userText.setText("Active User: ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
