package goldendeal.goldendeal.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AdminTasksActivity;
import goldendeal.goldendeal.Activities.UserActivities.TasksActivity;
import goldendeal.goldendeal.R;

public class CreateAccountActivity extends AppCompatActivity {
    private static final String TAG = "CreateAccountActivity";
    private EditText email;
    private EditText phoneNum;
    private EditText password;
    private Switch permission;
    private Button createAccount;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        email = (EditText) findViewById(R.id.emailEt);
        phoneNum = (EditText) findViewById(R.id.phoneNumEt);
        password = (EditText) findViewById(R.id.passwordEt);
        permission = (Switch) findViewById(R.id.Role);
        createAccount = (Button) findViewById(R.id.CreateAccButton);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewAccount();
            }
        });
    }
    
    private void createNewAccount() {

        final String em = email.getText().toString();
        final String phnum = phoneNum.getText().toString();
        final String pw = password.getText().toString();
        final Boolean role = permission.isChecked();

        if(!TextUtils.isEmpty(em) && !TextUtils.isEmpty(phnum) && !TextUtils.isEmpty(pw)){
            mProgressDialog.setMessage("Creating Account....");
            mProgressDialog.show();

            mAuth.createUserWithEmailAndPassword(em, pw).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if(authResult != null){

                        if(role){
                            String UserID = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = mDatabaseReference.child("Admin").child(UserID).child("Info");

                            currentUserDb.child("email").setValue(em);
                            currentUserDb.child("phoneNum").setValue(phnum);
                            currentUserDb.child("role").setValue(role);
                            currentUserDb.child("theme").setValue("none");
                            currentUserDb.child("password").setValue(pw);
                            currentUserDb.child("CurrentAccess").setValue("none");

                            mProgressDialog.dismiss();
                            //sending user to main activity
                            Intent intent = new Intent(CreateAccountActivity.this, AdminTasksActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else{
                            String UserID = mAuth.getCurrentUser().getUid();
                            DatabaseReference currentUserDb = mDatabaseReference.child("User").child(UserID).child("Info");

                            currentUserDb.child("email").setValue(em);
                            currentUserDb.child("phoneNum").setValue(phnum);
                            currentUserDb.child("role").setValue(role);
                            currentUserDb.child("theme").setValue("none");
                            currentUserDb.child("password").setValue(pw);

                            mProgressDialog.dismiss();
                            //sending user to main activity
                            Intent intent = new Intent(CreateAccountActivity.this, TasksActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }


                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressDialog.dismiss();

                    Toast.makeText(CreateAccountActivity.this, "Couldn't create account: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
