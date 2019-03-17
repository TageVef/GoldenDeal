package goldendeal.goldendeal.Activities.AdminActivity.BankActivities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
                    //maxValue.setHeight(80);
                }else{
                    maxValue.setVisibility(View.INVISIBLE);
                    //maxValue.setHeight(1);
                }
            }
        });
    }
}
