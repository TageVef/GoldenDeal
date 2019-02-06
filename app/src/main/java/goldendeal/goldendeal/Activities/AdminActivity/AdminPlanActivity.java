package goldendeal.goldendeal.Activities.AdminActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import goldendeal.goldendeal.R;

public class AdminPlanActivity extends AppCompatActivity {
    private TextView currentUserMail;
    private Button editUserButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_plan);

        SetupViews();


    }

    private void SetupViews() {
        currentUserMail = (TextView) findViewById(R.id.UserEmail);
        editUserButton = (Button) findViewById(R.id.EditUserButton);

        View.OnClickListener ButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.EditUserButton:
                        startActivity(new Intent(AdminPlanActivity.this, AdminSelectUserActivity.class));
                        break;
                }
            }
        };

        editUserButton.setOnClickListener(ButtonListener);
    }
}
