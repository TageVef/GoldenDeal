package goldendeal.goldendeal.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;

import goldendeal.goldendeal.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    private ImageButton taskButton;
    private ImageButton shopButton;
    private ImageButton bankButton;
    private ImageButton rulesButton;
    private Button addPointsButton;
    private Button removePointsButton;
    private ViewFlipper windowFlipper;
    private TextView pointsView;
    private TextView pointsTitleField;
    private TextView rules;
    private Button faceButton;
    private Button optionsButton;
    private Button faceBack;
    private Button optionsBack;
    private ViewFlipper mainWindowFlipper;

    private FirebaseAuth mAuth;


    private int points = 0;
    private String pointsTitle = "Points";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mAuth = FirebaseAuth.getInstance();

        faceButton = (Button) findViewById(R.id.FaceButton);
        faceBack = (Button) findViewById(R.id.FaceBack);
        optionsButton = (Button) findViewById(R.id.OptionsButton);
        optionsBack = (Button) findViewById(R.id.Optionsback);
        mainWindowFlipper = (ViewFlipper) findViewById(R.id.optionFlipper);

        taskButton = (ImageButton) findViewById(R.id.TaskButton);
        shopButton = (ImageButton) findViewById(R.id.ShopButton);
        bankButton = (ImageButton) findViewById(R.id.BankButton);
        rulesButton = (ImageButton) findViewById(R.id.RulesButton);
        addPointsButton = (Button) findViewById(R.id.addPoints);
        removePointsButton = (Button) findViewById(R.id.removePoints);
        windowFlipper = (ViewFlipper) findViewById(R.id.pageFlipper);
        pointsView = (TextView) findViewById(R.id.Points);
        pointsTitleField = (TextView) findViewById(R.id.pointTitle);
        rules = (TextView) findViewById(R.id.Rules);

        View.OnClickListener switchPage = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pointsView.setText("");
                pointsTitleField.setText("");
                ImageButton switchButton = (ImageButton) view;
                int buttonText = switchButton.getId();
                if(buttonText == taskButton.getId())
                {
                    windowFlipper.setDisplayedChild(windowFlipper.indexOfChild(findViewById(R.id.TaskView)));
                }
                else if(buttonText == shopButton.getId())
                {
                    windowFlipper.setDisplayedChild(windowFlipper.indexOfChild(findViewById(R.id.shopView)));
                }
                else if(buttonText == bankButton.getId())
                {
                    windowFlipper.setDisplayedChild(windowFlipper.indexOfChild(findViewById(R.id.bankView)));
                    pointsView.setText(Integer.toString(points));
                    pointsTitleField.setText(pointsTitle + ": ");
                }
                else if(buttonText == rulesButton.getId())
                {
                    rules.setText("1. we most follow the rules! \n\n 2. all tasks must be done by the description to get accepted");
                    windowFlipper.setDisplayedChild(windowFlipper.indexOfChild(findViewById(R.id.rulesView)));
                }
            }
        };
        View.OnClickListener addRemovePoints = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String buttonText = ((Button) view).getText().toString();

                if(buttonText == addPointsButton.getText().toString())
                {
                    points += 10;
                }
                else if(buttonText == removePointsButton.getText().toString())
                {
                    if(points >= 10)
                    {
                        points -= 10;
                    }
                    else if(points < 10 && points > 0)
                    {
                        Log.d(TAG, "cant buy something you cant afford!");
                    }
                    else
                    {
                        Log.d(TAG, "you dont even have any points to buy this!");
                    }
                }
            }
        };

        View.OnClickListener optionFlipper = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String buttonText = ((Button) view).getText().toString();

                if(buttonText == faceButton.getText().toString())
                {
                    mainWindowFlipper.setDisplayedChild(mainWindowFlipper.indexOfChild(findViewById(R.id.faceView)));
                }
                else if(buttonText == faceBack.getText().toString())
                {
                    mainWindowFlipper.setDisplayedChild(mainWindowFlipper.indexOfChild(findViewById(R.id.mainView)));
                }
                else if(buttonText == optionsButton.getText().toString())
                {
                    mainWindowFlipper.setDisplayedChild(mainWindowFlipper.indexOfChild(findViewById(R.id.optionsView)));
                }
                else if(buttonText == optionsBack.getText().toString())
                {
                    mainWindowFlipper.setDisplayedChild(mainWindowFlipper.indexOfChild(findViewById(R.id.mainView)));
                }
            }
        };

        optionsButton.setOnClickListener(optionFlipper);
        optionsBack.setOnClickListener(optionFlipper);
        faceButton.setOnClickListener(optionFlipper);
        faceBack.setOnClickListener(optionFlipper);

        taskButton.setOnClickListener(switchPage);
        shopButton.setOnClickListener(switchPage);
        bankButton.setOnClickListener(switchPage);
        rulesButton.setOnClickListener(switchPage);

        addPointsButton.setOnClickListener(addRemovePoints);
        removePointsButton.setOnClickListener(addRemovePoints);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
}
