package goldendeal.goldendeal.Activities.AdminActivity.StoreActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import goldendeal.goldendeal.R;

public class NewRewardActivity extends AppCompatActivity {
    private static final String TAG = "NewRewardActivity";

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private EditText titleET;
    private EditText descriptionET;
    private EditText rewardValueET;
    private EditText rewardTypeET;
    private ImageView rewardImage;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reward);
        SetupDatabase();
        SetupViews();
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        titleET = (EditText) findViewById(R.id.Title);
        descriptionET = (EditText) findViewById(R.id.Description);
        rewardValueET = (EditText) findViewById(R.id.RewardValue);
        rewardTypeET = (EditText) findViewById(R.id.ValueType);
        rewardImage = (ImageView) findViewById(R.id.RewardPicture);
        confirmButton = (Button) findViewById(R.id.ConfirmButton);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
