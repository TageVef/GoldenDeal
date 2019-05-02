package goldendeal.goldendeal.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.R;

public class OptionsActivity extends AppCompatActivity {

    private TextView languageText;
    private TextView languageChoice;
    private TextView themeText;
    private TextView themeChoice;
    private ImageView backButton;
    private Button signout;
    private ConstraintLayout optionsLayout;

    private TextView versionText;
    private TextView versionNumber;

    private User currentUser;
    private Boolean role;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        role = getIntent().getBooleanExtra("Role", false);
        SetupDatabase();
        SetupViews();

        if (role)
            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info");
        else
            mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info");

        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(User.class);
                SetupLanguage();
                SetupTheme();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SetupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        languageText = (TextView) findViewById(R.id.LanguageText);
        languageChoice = (TextView) findViewById(R.id.LanguageBox);
        themeText = (TextView) findViewById(R.id.ThemeText);
        themeChoice = (TextView) findViewById(R.id.ThemeChoice);
        backButton = (ImageView) findViewById(R.id.BackButton);
        signout = (Button) findViewById(R.id.signoutButton);
        versionText = (TextView) findViewById(R.id.VersionText);
        versionNumber = (TextView) findViewById(R.id.VersionNumber);
        optionsLayout = (ConstraintLayout) findViewById(R.id.OptionsLayout);

        versionNumber.setText("0.1.0v");

        languageChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(OptionsActivity.this, v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        currentUser.setLanguage(item.getTitle().toString());
                        SetupLanguage();
                        if (currentUser.getRole()) {
                            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
                        } else {
                            mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info").child("language");
                        }
                        mDatabaseReference.setValue(currentUser.getLanguage());
                        return false;
                    }
                });
                popup.getMenu().add("Norsk");
                popup.getMenu().add("English");
                popup.show();
            }
        });

        themeChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(OptionsActivity.this, v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        currentUser.setTheme(item.getTitle().toString());
                        SetupTheme();
                        if (currentUser.getRole()) {
                            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("theme");
                        } else {
                            mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info").child("theme");
                        }
                        mDatabaseReference.setValue(currentUser.getTheme());
                        return false;
                    }
                });
                menu.getMenu().add("Mermaids");
                menu.getMenu().add("Western");
                menu.getMenu().add("Space");
                //menu.getMenu().add("season");
                menu.getMenu().add("Standard");
                menu.show();
            }
        });

        View.OnClickListener switchPage = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signoutButton:
                        if (mAuth != null) {
                            mAuth.signOut();
                            Toast.makeText(OptionsActivity.this, "signed out!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(OptionsActivity.this, MainActivity.class));
                            finish();
                        }
                        break;
                    case R.id.BackButton:
                        finish();
                        break;
                }
            }
        };

        backButton.setOnClickListener(switchPage);
        signout.setOnClickListener(switchPage);
    }

    private void SetupLanguage() {
        languageChoice.setText(currentUser.getLanguage());
        switch(currentUser.getLanguage()){
            case "Norsk":
                signout.setText("Log ut");
                languageText.setText("språk");
                versionText.setText("Versjon: ");
                themeText.setText("Tema: ");
                break;
            case "English":
                signout.setText("Signout");
                languageText.setText("language");
                versionText.setText("Version: ");
                themeText.setText("Theme: ");
                break;
        }
    }

    private void SetupTheme() {
        themeChoice.setText(currentUser.getTheme());
        switch (currentUser.getTheme()) {
            case "Mermaids":
                backButton.setImageResource(R.drawable.mermaids_button_back);
                break;
            case "Western":
                backButton.setImageResource(R.drawable.western_button_back);
                break;
            case "Space":
                backButton.setImageResource(R.drawable.space_button_back);
                break;
            case "Season":
                break;
            case "Standard":
                backButton.setImageResource(R.drawable.pirate_button_back_english);
                break;
        }

    }
}
