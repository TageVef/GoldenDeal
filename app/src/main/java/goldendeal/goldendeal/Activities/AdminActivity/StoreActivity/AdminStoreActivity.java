package goldendeal.goldendeal.Activities.AdminActivity.StoreActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import goldendeal.goldendeal.Activities.AdminActivity.BankActivities.AdminBankActivity;
import goldendeal.goldendeal.Activities.AdminActivity.PlanActivitys.AdminPlanActivity;
import goldendeal.goldendeal.Activities.AdminActivity.RulesActivity.AdminRulesActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminAddTasksActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminTasksActivity;
import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.EditTasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.R;

public class AdminStoreActivity extends AppCompatActivity {
    private static final String TAG = "AdminStoreActivity";

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private Button taskButton;
    private Button storeButton;
    private Button bankButton;
    private Button rulesButton;
    private Button optionsButton;
    private Button adminButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_store);
        SetupDatabase();
        SetupViews();
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        taskButton = (Button) findViewById(R.id.TaskButton);
        storeButton = (Button) findViewById(R.id.StoreButton);
        bankButton = (Button) findViewById(R.id.BankButton);
        rulesButton = (Button) findViewById(R.id.RulesButton);
        optionsButton = (Button) findViewById(R.id.OptionsButton);
        adminButton = (Button) findViewById(R.id.AdminButton);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminTasksActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminBankActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminStoreActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminRulesActivity.class));
                        finish();
                        break;
                    case R.id.OptionsButton:
                        startActivity(new Intent(AdminStoreActivity.this, OptionsActivity.class));
                        break;
                    case R.id.AdminButton:
                        startActivity(new Intent(AdminStoreActivity.this, AdminPlanActivity.class));
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        //rulesButton.setOnClickListener(switchPage);
        optionsButton.setOnClickListener(switchPage);
        adminButton.setOnClickListener(switchPage);
    }
}
