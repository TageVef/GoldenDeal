package goldendeal.goldendeal.Activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import goldendeal.goldendeal.Model.User;
import goldendeal.goldendeal.R;

public class OptionsActivity extends AppCompatActivity {

    private TextView languageText;
    private TextView languageChoice;
    private Button backButton;
    private Button signout;

    private boolean userRole;
    private String language;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        SetupDatabase();
        SetupViews();

        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User currentUser = dataSnapshot.getValue(User.class);
                if(currentUser != null){
                    if(currentUser.getRole()){
                        userRole = true;
                        language = currentUser.getLanguage();
                        languageChoice.setText(language);
                        if(TextUtils.equals(language, "Norsk")){
                            signout.setText("Log ut");
                            backButton.setText("Tilbake");
                            languageText.setText("språk");
                        } else if(TextUtils.equals(language, "English")){
                            signout.setText("Signout");
                            backButton.setText("back");
                            languageText.setText("language");
                        }
                    } else {
                        userRole = false;
                    }
                } else {
                    userRole = false;
                }

                if(!userRole){
                    mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info").child("language");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            language = dataSnapshot.getValue(String.class);
                            languageChoice.setText(language);
                            if(TextUtils.equals(language, "Norsk")){
                                signout.setText("Log ut");
                                backButton.setText("Tilbake");
                                languageText.setText("språk");
                            } else if(TextUtils.equals(language, "English")){
                                signout.setText("Signout");
                                backButton.setText("back");
                                languageText.setText("language");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
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
        backButton = (Button) findViewById(R.id.BackButton);
        signout = (Button) findViewById(R.id.signoutButton);

        languageChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(OptionsActivity.this, v);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        language = item.getTitle().toString();
                        if(TextUtils.equals(item.getTitle(), "Norsk")){
                            signout.setText("Log ut");
                            backButton.setText("Tilbake");
                            languageText.setText("språk");
                        }else if(TextUtils.equals(item.getTitle(), "English")){
                            signout.setText("Signout");
                            backButton.setText("back");
                            languageText.setText("language");
                        }
                        if(userRole){
                            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
                        } else {
                            mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info").child("language");
                        }
                        mDatabaseReference.setValue(item.getTitle().toString());
                        return false;
                    }
                });
                popup.getMenu().add("Norsk");
                popup.getMenu().add("English");
                popup.show();
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
}
