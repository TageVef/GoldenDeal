package goldendeal.goldendeal.Activities.AdminActivity.BankActivities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class NewCurrencyActivity extends AppCompatActivity {
    private static final String TAG = "NewCurrencyActivity";
    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private Button addCurrencyButton;
    private Button backButton;
    private EditText currencyTitle;
    private EditText maxValue;
    private Switch imageEconomySwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_currency);
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
        addCurrencyButton = (Button) findViewById(R.id.AddCurrencyButton);
        backButton = (Button) findViewById(R.id.BackButton);
        currencyTitle = (EditText) findViewById(R.id.TitleET);
        maxValue = (EditText) findViewById(R.id.MaxValueET);
        imageEconomySwitch = (Switch) findViewById(R.id.ImageEconomySwitch);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.BackButton:
                        finish();
                        break;
                    case R.id.AddCurrencyButton:
                        if(!TextUtils.isEmpty(currencyTitle.getText())){
                            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String currencAccess = dataSnapshot.getValue(String.class);
                                    mDatabaseReference = mDatabase.getReference().child("User").child(currencAccess).child("Bank");
                                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //Log.d(TAG, "onDataChange: start");
                                            if(dataSnapshot.hasChildren()){
                                                for(DataSnapshot currencies: dataSnapshot.getChildren()){
                                                    if(TextUtils.equals(currencies.getKey(), currencyTitle.getText())){
                                                        Toast.makeText(NewCurrencyActivity.this, "Currency allready exist!", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                }
                                            }
                                            final VirtualCurrency newCurrency = new VirtualCurrency((long) 0,currencyTitle.getText().toString(), imageEconomySwitch.isChecked(), (long) 0);
                                            if(newCurrency.isImageEconomy()){
                                                newCurrency.setMaxValue(Long.parseLong(maxValue.getText().toString()));
                                            }
                                            mDatabaseReference.child(currencyTitle.getText().toString()).setValue(newCurrency).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(NewCurrencyActivity.this, "added " + currencyTitle.getText().toString() + " to bank", Toast.LENGTH_SHORT).show();
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
                        break;
                }
            }
        };

        imageEconomySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d(TAG, "onCheckedChanged: started");
                if(isChecked){
                    maxValue.setVisibility(View.VISIBLE);
                }else{
                    maxValue.setVisibility(View.GONE);
                }
            }
        });

        backButton.setOnClickListener(switchPage);
        addCurrencyButton.setOnClickListener(switchPage);
    }

    private void SetupLanguage(){
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String language = dataSnapshot.getValue(String.class);

                if(TextUtils.equals(language, "Norsk")){
                    addCurrencyButton.setText("Legg Til Valuta");
                    backButton.setText("Tilbake");
                    currencyTitle.setHint("Titel");
                    maxValue.setHint("Maks Verdi");
                    imageEconomySwitch.setText("Bilde Økonomi");
                } else if(TextUtils.equals(language, "English")){
                    addCurrencyButton.setText("Add Currency");
                    backButton.setText("Back");
                    currencyTitle.setHint("Title");
                    maxValue.setHint("Max value");
                    imageEconomySwitch.setText("Image Economy");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
