package goldendeal.goldendeal.Activities.AdminActivity.RulesActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import goldendeal.goldendeal.Activities.AdminActivity.BankActivities.AdminBankActivity;
import goldendeal.goldendeal.Activities.AdminActivity.PlanActivitys.AdminPlanActivity;
import goldendeal.goldendeal.Activities.AdminActivity.StoreActivity.AdminStoreActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminTasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.R;

public class NewRuleActivity extends AppCompatActivity {
    private static final String TAG = "NewRuleActivity";

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private DatabaseReference removeRule;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private EditText ruleDrescription;
    private Button backButton;
    private Button addButton;
    private Button trashButton;
    private String ruleNumber;

    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_rule);
        SetupDatabase();
        SetupViews();

        ruleNumber = getIntent().getStringExtra("RuleNumber");
        if(!TextUtils.isEmpty(ruleNumber)){
            trashButton.setVisibility(View.VISIBLE);
            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String currentAccess = dataSnapshot.getValue(String.class);
                    mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Rules").child(ruleNumber).child("text");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String ruleText = dataSnapshot.getValue(String.class);
                            ruleDrescription.setText(ruleText);
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


        SetupLanguage();
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        ruleDrescription = (EditText) findViewById(R.id.RuleDescription);
        backButton = (Button) findViewById(R.id.BackButton);
        addButton = (Button) findViewById(R.id.AddButton);
        trashButton = (Button) findViewById(R.id.TrashButton);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.BackButton:
                        finish();
                        break;
                    case R.id.TrashButton:
                        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                final String currentAccess = dataSnapshot.getValue(String.class);
                                removeRule = mDatabase.getReference().child("User").child(currentAccess).child("Rules");
                                removeRule.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        long moveCounter = dataSnapshot.getChildrenCount();
                                        for(DataSnapshot ruleItems: dataSnapshot.getChildren()){
                                            String ruleText = ruleItems.child("text").getValue(String.class);
                                            if(TextUtils.equals(ruleText, ruleDrescription.getText())){
                                                moveCounter = Long.parseLong(ruleItems.getKey());
                                            }
                                            if(Long.parseLong(ruleItems.getKey()) > moveCounter){
                                                removeRule.child(Long.toString(Long.parseLong(ruleItems.getKey()) - 1)).child("text").setValue(ruleText);
                                            }
                                            if(Long.parseLong(ruleItems.getKey()) == dataSnapshot.getChildrenCount()){
                                                removeRule.child(Long.toString(dataSnapshot.getChildrenCount())).removeValue();
                                            }
                                        }
                                        finish();
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
                        break;
                    case R.id.AddButton:
                        if (!TextUtils.isEmpty(ruleDrescription.getText())) {
                            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String currentAccess = dataSnapshot.getValue(String.class);
                                    mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Rules");
                                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(TextUtils.isEmpty(ruleNumber)){
                                                ruleNumber = Long.toString(dataSnapshot.getChildrenCount() + 1);
                                            }
                                            mDatabaseReference.child(ruleNumber).child("text").setValue(ruleDrescription.getText().toString())
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    if(TextUtils.equals(language, "Norsk")){
                                                        Toast.makeText(NewRuleActivity.this, "Regel har blitt lagt til!", Toast.LENGTH_SHORT).show();
                                                    }else if(TextUtils.equals(language, "English")){
                                                        Toast.makeText(NewRuleActivity.this, "Rule has been added", Toast.LENGTH_SHORT).show();
                                                    }
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
                            finish();
                        }
                        break;
                }
            }
        };

        backButton.setOnClickListener(switchPage);
        addButton.setOnClickListener(switchPage);
        trashButton.setOnClickListener(switchPage);
    }

    private void SetupLanguage() {
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                language = dataSnapshot.getValue(String.class);

                if (TextUtils.equals(language, "Norsk")) {
                    ruleDrescription.setHint("Regel beskrivelse");
                    backButton.setText("Tilbake");
                    addButton.setText("Legg til regel");
                    trashButton.setText("Kast");
                } else if (TextUtils.equals(language, "English")) {
                    ruleDrescription.setHint("Rules description");
                    backButton.setText("back");
                    addButton.setText("add rule");
                    trashButton.setText("Trash");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
