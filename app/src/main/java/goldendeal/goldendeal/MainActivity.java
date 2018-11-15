package goldendeal.goldendeal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private Button taskButton;
    private Button shopButton;
    private Button bankButton;
    private Button rulesButton;
    private Button addPointsButton;
    private Button removePointsButton;
    private ViewFlipper windowFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        taskButton = (Button) findViewById(R.id.taskTaskButton);
        shopButton = (Button) findViewById(R.id.taskShopButton);
        bankButton = (Button) findViewById(R.id.taskBankButton);
        rulesButton = (Button) findViewById(R.id.taskRulesButton);
        addPointsButton = (Button) findViewById(R.id.addPoints);
        removePointsButton = (Button) findViewById(R.id.removePoints);
        windowFlipper = (ViewFlipper) findViewById(R.id.myViewFlipper);

        View.OnClickListener switchPage = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Button switchButton = (Button) view;
                String buttonText = switchButton.getText().toString();
                Log.d(TAG, "onClick: button pushed " + buttonText);

                if(buttonText == taskButton.getText().toString())
                {
                    Log.d(TAG, "Tasks");
                    windowFlipper.setDisplayedChild(windowFlipper.indexOfChild(findViewById(R.id.taskView)));
                }
                else if(buttonText == shopButton.getText().toString())
                {
                    Log.d(TAG, "Shop");
                    windowFlipper.setDisplayedChild(windowFlipper.indexOfChild(findViewById(R.id.shopView)));
                }
                else if(buttonText == bankButton.getText().toString())
                {
                    Log.d(TAG, "Bank");
                    windowFlipper.setDisplayedChild(windowFlipper.indexOfChild(findViewById(R.id.bankView)));
                }
                else if(buttonText == rulesButton.getText().toString())
                {
                    Log.d(TAG, "Rules");
                    windowFlipper.setDisplayedChild(windowFlipper.indexOfChild(findViewById(R.id.rulesView)));
                }
            }
        };

        taskButton.setOnClickListener(switchPage);
        shopButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
}
