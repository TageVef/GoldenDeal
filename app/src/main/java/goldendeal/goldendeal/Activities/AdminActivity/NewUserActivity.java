package goldendeal.goldendeal.Activities.AdminActivity;

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
    private FirebaseUser mUser;
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
        mUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();
    }

    private void setupViews(){
        addUserButton = (Button) findViewById(R.id.AddUser);
        backButton = (Button) findViewById(R.id.BackButton);
        emailView = (EditText) findViewById(R.id.EmailView);

        View.OnClickListener ButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.BackButton:
                        finish();
                        break;
                    case R.id.AddUser:
                        if(!TextUtils.isEmpty(emailView.getText())){
                            mDatabaseReference = mDatabase.getReference().child("User");
                            mDatabaseReference.orderByChild("email").equalTo(emailView.getText().toString());
                            mDatabaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Toast.makeText(NewUserActivity.this, dataSnapshot.toString(), Toast.LENGTH_SHORT).show();
                                    Log.d(TAG, "onDataChange: " + dataSnapshot.toString());
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

        addUserButton.setOnClickListener(ButtonListener);
        backButton.setOnClickListener(ButtonListener);
    }
}
