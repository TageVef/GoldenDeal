package goldendeal.goldendeal.Activities.AdminActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

import goldendeal.goldendeal.R;

public class AdminSelectUserActivity extends AppCompatActivity {
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_user);
        SetupViews();
    }

    private void SetupViews() {
        backButton = (Button) findViewById(R.id.BackButton);

        View.OnClickListener ButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.BackButton:
                        finish();
                        break;
                }
            }
        };

        backButton.setOnClickListener(ButtonListener);
    }
}
