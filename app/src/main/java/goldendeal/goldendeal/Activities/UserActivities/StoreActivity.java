package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Set;

import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.Data.UserData.CounterRecyclerAdapter;
import goldendeal.goldendeal.Data.UserData.ImageEconomyRecyclerAdapter;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class StoreActivity extends AppCompatActivity {
    private static final String TAG = "StoreActivity";

    private ImageView taskButton;
    private ImageView storeButton;
    private ImageView bankButton;
    private ImageView rulesButton;
    private ImageView optionsButton;
    private ImageView faceButton;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        SetupDatabase();
        SetupViews();

    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        taskButton = (ImageView) findViewById(R.id.TaskButton);
        storeButton = (ImageView) findViewById(R.id.StoreButton);
        bankButton = (ImageView) findViewById(R.id.BankButton);
        rulesButton = (ImageView) findViewById(R.id.RulesButton);
        optionsButton = (ImageView) findViewById(R.id.OptionsButton);
        faceButton = (ImageView) findViewById(R.id.FaceButton);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(StoreActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(StoreActivity.this, StoreActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(StoreActivity.this, BankActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(StoreActivity.this, RulesActivity.class));
                        finish();
                        break;
                    case R.id.FaceButton:
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(StoreActivity.this, OptionsActivity.class));
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        //rulesButton.setOnClickListener(switchPage);
        //faceButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
    }
}
