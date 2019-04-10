package goldendeal.goldendeal.Activities.AdminActivity.BankActivities;

import android.support.annotation.NonNull;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class AddRemoveCurrencies extends AppCompatActivity {
    private static final String TAG = "AddRemoveCurrencies";

    private Button addButton;
    private Button removeButton;
    private Button trashButton;
    private EditText numberEditText;
    private String currencyTitle;
    private String operation;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_currencies);
        currencyTitle = getIntent().getStringExtra("Currency");
        SetupDatabase();
        SetupViews();
        SetupLanguage();


    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        addButton = (Button) findViewById(R.id.AddButton);
        removeButton = (Button) findViewById(R.id.RemoveButton);
        trashButton = (Button) findViewById(R.id.TrashButton);
        numberEditText = (EditText) findViewById(R.id.NumberET);

        View.OnClickListener addRemoveTrashClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.AddButton:
                        operation = "add";
                        break;
                    case R.id.RemoveButton:
                        operation = "remove";
                        break;
                    case R.id.TrashButton:
                        operation = "trash";
                        break;
                }
                mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String currentAccess = dataSnapshot.getValue(String.class);
                        mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Bank").child(currencyTitle);

                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                VirtualCurrency activeCurrency = dataSnapshot.getValue(VirtualCurrency.class);

                                if (TextUtils.equals(operation, "add")) {
                                    if (activeCurrency.isImageEconomy()) {
                                        activeCurrency.setValue(activeCurrency.getValue() + Long.parseLong(numberEditText.getText().toString()));
                                    } else {
                                        activeCurrency.setValue(activeCurrency.getValue() + Long.parseLong(numberEditText.getText().toString()));
                                    }
                                    mDatabaseReference.setValue(activeCurrency).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                        }
                                    });
                                } else if (TextUtils.equals(operation, "remove")) {
                                    if (activeCurrency.getValue() - Long.parseLong(numberEditText.getText().toString()) < 0) {
                                        activeCurrency.setValue((long) 0);
                                    } else {
                                        activeCurrency.setValue(activeCurrency.getValue() - Long.parseLong(numberEditText.getText().toString()));
                                    }
                                    mDatabaseReference.setValue(activeCurrency).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                        }
                                    });
                                }

                                if (TextUtils.equals(operation, "trash")) {
                                    mDatabaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                        }
                                    });
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
        };

        addButton.setOnClickListener(addRemoveTrashClickListener);
        removeButton.setOnClickListener(addRemoveTrashClickListener);
        trashButton.setOnClickListener(addRemoveTrashClickListener);
    }

    private void SetupLanguage(){
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String language = dataSnapshot.getValue(String.class);

                if(TextUtils.equals(language, "Norsk")){
                    addButton.setText("Legg til");
                    removeButton.setText("Fjern");
                    trashButton.setText("Kast");
                    numberEditText.setHint("Antall");
                } else if(TextUtils.equals(language, "English")){
                    addButton.setText("Add");
                    removeButton.setText("Remove");
                    trashButton.setText("Trash");
                    numberEditText.setHint("Amount");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
