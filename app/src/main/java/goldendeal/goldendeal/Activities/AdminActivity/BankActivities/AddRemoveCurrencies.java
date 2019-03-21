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

import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class AddRemoveCurrencies extends AppCompatActivity {
    private static final String TAG = "AddRemoveCurrencies";

    private Button addRemoveButton;
    private EditText numberEditText;
    private String currencyTitle;
    private String opperation;

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
        opperation = getIntent().getStringExtra("Opperation");
        SetupDatabase();
        SetupViews();
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        addRemoveButton = (Button) findViewById(R.id.AddRemoveButton);
        numberEditText = (EditText) findViewById(R.id.NumberET);

        addRemoveButton.setText(opperation);

        addRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String currentAcces = dataSnapshot.getValue(String.class);
                        mDatabaseReference = mDatabase.getReference().child("User").child(currentAcces).child("Bank").child(currencyTitle);
                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                VirtualCurrency currentCurrency = dataSnapshot.getValue(VirtualCurrency.class);
                                if (!TextUtils.isEmpty(currentCurrency.getTitle())) {
                                    if (currentCurrency.isImageEconomy()) {
                                        if (TextUtils.equals(opperation, "add")) {
                                            currentCurrency.setValue(currentCurrency.getValue() + Long.parseLong(numberEditText.getText().toString()));
                                        } else if (TextUtils.equals(opperation, "remove")) {
                                            if (currentCurrency.getValue() - Long.parseLong(numberEditText.getText().toString()) < 0) {
                                                currentCurrency.setValue((long) 0);
                                            } else {
                                                currentCurrency.setValue(currentCurrency.getValue() - Long.parseLong(numberEditText.getText().toString()));
                                            }
                                        }
                                    }else{
                                        if (TextUtils.equals(opperation, "add")) {
                                            currentCurrency.setValue(currentCurrency.getValue() + Long.parseLong(numberEditText.getText().toString()));
                                        } else if (TextUtils.equals(opperation, "remove")) {

                                            if (currentCurrency.getValue() - Long.parseLong(numberEditText.getText().toString()) < 0) {
                                                currentCurrency.setValue((long) 0);
                                            } else {
                                                currentCurrency.setValue(currentCurrency.getValue() - Long.parseLong(numberEditText.getText().toString()));
                                            }
                                        }
                                    }
                                }
                                mDatabaseReference.setValue(currentCurrency).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
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
            }
        });


    }
}
