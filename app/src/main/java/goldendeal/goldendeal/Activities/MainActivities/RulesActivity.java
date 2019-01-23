package goldendeal.goldendeal.Activities.MainActivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import goldendeal.goldendeal.R;

public class RulesActivity extends AppCompatActivity {

    private ImageButton taskButton;
    private ImageButton shopButton;
    private ImageButton bankButton;
    private ImageButton rulesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);

        taskButton = (ImageButton) findViewById(R.id.TaskButton);
        shopButton = (ImageButton) findViewById(R.id.ShopButton);
        bankButton = (ImageButton) findViewById(R.id.BankButton);
        rulesButton = (ImageButton) findViewById(R.id.RulesButton);

        View.OnClickListener switchPage = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                ImageButton switchButton = (ImageButton) view;
                int buttonText = switchButton.getId();
                if(buttonText == taskButton.getId())
                {
                    startActivity(new Intent(RulesActivity.this, TasksActivity.class));
                    finish();
                }
                else if(buttonText == shopButton.getId())
                {
                    startActivity(new Intent(RulesActivity.this, StoreActivity.class));
                    finish();
                }
                else if(buttonText == bankButton.getId())
                {
                    startActivity(new Intent(RulesActivity.this, BankActivity.class));
                    finish();
                }
                else if(buttonText == rulesButton.getId())
                {
                    startActivity(new Intent(RulesActivity.this, RulesActivity.class));
                    finish();
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        shopButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
    }
}
