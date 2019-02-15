package goldendeal.goldendeal.Activities.AdminActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Activities.AdminActivity.MainActivity.AdminPlanActivity;
import goldendeal.goldendeal.Data.AdminData.UserSelectRecyclerAdapter;
import goldendeal.goldendeal.R;

public class AdminSelectUserActivity extends AppCompatActivity {

    private static final String TAG = "AdminSelectUserActivity";

    private Button backButton;
    private Button addUserButton;
    private RecyclerView selectUsers;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    private List<String> userIDList;
    private UserSelectRecyclerAdapter userSelectRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_select_user);
        setupDatabase();
        SetupViews();

        userIDList = new ArrayList<String>();
        selectUsers.setHasFixedSize(true);
        selectUsers.setLayoutManager(new LinearLayoutManager(this));


    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Access");

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get email addresses
                for (DataSnapshot userID : dataSnapshot.getChildren()) {
                    userIDList.add(userID.getKey());
                }

                userSelectRecyclerAdapter = new UserSelectRecyclerAdapter(AdminSelectUserActivity.this, userIDList);
                selectUsers.setAdapter(userSelectRecyclerAdapter);
                userSelectRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void setupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }

    private void SetupViews() {
        backButton = (Button) findViewById(R.id.BackButton);
        addUserButton = (Button) findViewById(R.id.AddUser);
        selectUsers = (RecyclerView) findViewById(R.id.UserSelection);

        View.OnClickListener buttonSwitch = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.BackButton:
                        startActivity(new Intent(AdminSelectUserActivity.this, AdminPlanActivity.class));
                        finish();
                        break;
                    case R.id.AddUser:
                        startActivity(new Intent(AdminSelectUserActivity.this, NewUserActivity.class));
                        break;
                }
            }
        };

        backButton.setOnClickListener(buttonSwitch);
        addUserButton.setOnClickListener(buttonSwitch);
    }
}
