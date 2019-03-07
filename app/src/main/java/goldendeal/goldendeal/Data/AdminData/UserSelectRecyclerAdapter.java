package goldendeal.goldendeal.Data.AdminData;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import goldendeal.goldendeal.Activities.AdminActivity.PlanActivitys.AdminPlanActivity;
import goldendeal.goldendeal.R;

public class UserSelectRecyclerAdapter extends RecyclerView.Adapter<UserSelectRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<String> userList;

    //Firebase Variables
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    //------------------------------------------------------

    public UserSelectRecyclerAdapter(Context context, List<String> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.select_user_layout, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final String userID = userList.get(i);
        setupDatabase();

        mDatabaseReference = mDatabase.getReference().child("User").child(userID).child("Info").child("email");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewHolder.userButton.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button userButton;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;
            userButton = (Button) itemView.findViewById(R.id.UserButton);

            userButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Button buttonClicked = (Button) v;
                    final String mail = buttonClicked.getText().toString();

                    mDatabaseReference = mDatabase.getReference().child("User");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Looking through users to find correct user
                            for (DataSnapshot data : dataSnapshot.getChildren()) {

                                // Checking if the currenct user has the correct mail assigned to it
                                if (data.child("Info").child("email").getValue(String.class).equalsIgnoreCase(mail)) {
                                    mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info");
                                    mDatabaseReference.child("CurrentAccess").setValue(data.getKey());
                                    Intent intent = new Intent(context, AdminPlanActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });

        }
    }

    private void setupDatabase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
    }
}
