package goldendeal.goldendeal.Activities.AdminActivity.StoreActivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import goldendeal.goldendeal.Model.StoreItem;
import goldendeal.goldendeal.Model.VirtualCurrency;
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
    private StoreItem newItem;

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
    }

    private void SetupViews() {
        titleET = (EditText) findViewById(R.id.Title);
        descriptionET = (EditText) findViewById(R.id.Description);
        rewardValueET = (EditText) findViewById(R.id.RewardValue);
        rewardTypeET = (EditText) findViewById(R.id.ValueType);
        rewardImage = (ImageView) findViewById(R.id.RewardPicture);
        confirmButton = (Button) findViewById(R.id.ConfirmButton);

        rewardImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: set up picture selection.
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(titleET.getText()) && !TextUtils.isEmpty(descriptionET.getText())
                        && !TextUtils.isEmpty(rewardValueET.getText()) && !TextUtils.isEmpty(rewardTypeET.getText())) {
                    newItem = new StoreItem(titleET.getText().toString(), descriptionET.getText().toString(),
                            new VirtualCurrency(Long.parseLong(rewardValueET.getText().toString()), rewardTypeET.getText().toString(), false, (long) 0), false);

                    mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String currentAccess = dataSnapshot.getValue(String.class);
                            mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Store");
                            Toast.makeText(NewRewardActivity.this, "checking database", Toast.LENGTH_SHORT).show();
                            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //TODO: set up picture uploading
                                    mDatabaseReference.child(Long.toString(dataSnapshot.getChildrenCount())).setValue(newItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(NewRewardActivity.this, "Reward Added", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
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
                } else {
                    Toast.makeText(NewRewardActivity.this, "Not all fields are filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
