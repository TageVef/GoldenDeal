package goldendeal.goldendeal.Activities.AdminActivity.StoreActivity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
    private TextView rewardType;
    private ImageView rewardImage;
    private Button confirmButton;
    private Button backButton;
    private StoreItem newItem;

    private List<String> currencyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_reward);
        SetupDatabase();
        SetupViews();
        SetupLanguage();

        currencyList = new ArrayList<String>();

        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String currentAccess = dataSnapshot.getValue(String.class);
                mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Bank");
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot currency: dataSnapshot.getChildren()){
                            currencyList.add(currency.getKey());
                        }
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

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    private void SetupViews() {
        titleET = (EditText) findViewById(R.id.Title);
        descriptionET = (EditText) findViewById(R.id.Description);
        rewardValueET = (EditText) findViewById(R.id.RewardValue);
        rewardType = (TextView) findViewById(R.id.ValueType);
        rewardImage = (ImageView) findViewById(R.id.RewardPicture);
        confirmButton = (Button) findViewById(R.id.ConfirmButton);
        backButton = (Button) findViewById(R.id.BackButton);

        rewardType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(NewRewardActivity.this, v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        rewardType.setText(item.getTitle());
                        return false;
                    }
                });

                for(int i = 0; i < currencyList.size(); i++){
                    menu.getMenu().add(currencyList.get(i));
                }
                menu.show();
            }
        });

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
                        && !TextUtils.isEmpty(rewardValueET.getText()) && !TextUtils.isEmpty(rewardType.getText())) {
                    newItem = new StoreItem(titleET.getText().toString(), descriptionET.getText().toString(),
                            new VirtualCurrency(Long.parseLong(rewardValueET.getText().toString()), rewardType.getText().toString(), false, (long) 0), false);

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                    titleET.setHint("Titel");
                    descriptionET.setHint("Beskrivelse");
                    rewardValueET.setHint("Antall Valuta");
                    rewardType.setHint("Valuta Type");
                    confirmButton.setText("Bekreft");
                    backButton.setText("Tilbake");
                } else if(TextUtils.equals(language, "English")){
                    titleET.setHint("Title");
                    descriptionET.setHint("Description");
                    rewardValueET.setHint("Required Amount");
                    rewardType.setHint("Value Type");
                    confirmButton.setText("Confirm");
                    backButton.setText("Back");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
