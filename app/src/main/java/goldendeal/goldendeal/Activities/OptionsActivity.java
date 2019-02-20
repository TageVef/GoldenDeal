package goldendeal.goldendeal.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import goldendeal.goldendeal.R;

public class OptionsActivity extends AppCompatActivity {

    private Button backButton;
    private Button signout;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        backButton = (Button) findViewById(R.id.BackButton);
        signout = (Button) findViewById(R.id.signoutButton);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.signoutButton:
                        if(mAuth != null && mUser != null){
                            mAuth.signOut();
                            Toast.makeText(OptionsActivity.this, "signed out!", Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(OptionsActivity.this, MainActivity.class));
                            finish();
                        }
                        break;
                    case R.id.BackButton:
                        finish();
                        break;
                }
            }
        };

        backButton.setOnClickListener(switchPage);
        signout.setOnClickListener(switchPage);
    }
}
