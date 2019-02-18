package goldendeal.goldendeal.Activities.UserActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import goldendeal.goldendeal.Activities.AdminActivity.AdminAddTasksActivity;
import goldendeal.goldendeal.Activities.AdminActivity.EditTasksActivity;
import goldendeal.goldendeal.Activities.AdminActivity.MainActivity.AdminBankActivity;
import goldendeal.goldendeal.Activities.AdminActivity.MainActivity.AdminPlanActivity;
import goldendeal.goldendeal.Activities.AdminActivity.MainActivity.AdminRulesActivity;
import goldendeal.goldendeal.Activities.AdminActivity.MainActivity.AdminStoreActivity;
import goldendeal.goldendeal.Activities.AdminActivity.MainActivity.AdminTasksActivity;
import goldendeal.goldendeal.Activities.OptionsActivity;
import goldendeal.goldendeal.R;

public class BankActivity extends AppCompatActivity {

    private Button taskButton;
    private Button storeButton;
    private Button bankButton;
    private Button rulesButton;
    private TextView pointsView;
    private TextView pointsTitleField;

    private int points = 0;
    private String pointsTitle = "Points";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        SetupViews();

        pointsView = (TextView) findViewById(R.id.Points);
        pointsTitleField = (TextView) findViewById(R.id.pointTitle);

        //pointsView.setText(getText(points));
        //pointsTitleField.setText(pointsTitle);


    }

    private void SetupViews() {
        taskButton = (Button) findViewById(R.id.TaskButton);
        storeButton = (Button) findViewById(R.id.StoreButton);
        bankButton = (Button) findViewById(R.id.BankButton);
        rulesButton = (Button) findViewById(R.id.RulesButton);

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.TaskButton:
                        startActivity(new Intent(BankActivity.this, TasksActivity.class));
                        finish();
                        break;
                    case R.id.StoreButton:
                        startActivity(new Intent(BankActivity.this, StoreActivity.class));
                        finish();
                        break;
                    case R.id.BankButton:
                        startActivity(new Intent(BankActivity.this, BankActivity.class));
                        finish();
                        break;
                    case R.id.RulesButton:
                        startActivity(new Intent(BankActivity.this, RulesActivity.class));
                        finish();
                        break;
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        storeButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
    }
}
