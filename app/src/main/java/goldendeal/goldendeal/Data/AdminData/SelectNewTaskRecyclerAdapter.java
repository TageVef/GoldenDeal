package goldendeal.goldendeal.Data.AdminData;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import goldendeal.goldendeal.Activities.AdminActivity.MainActivity.AdminTasksActivity;
import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class SelectNewTaskRecyclerAdapter extends RecyclerView.Adapter<SelectNewTaskRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Task> taskList;

    public SelectNewTaskRecyclerAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public SelectNewTaskRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_layout, viewGroup, false);
        return new SelectNewTaskRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectNewTaskRecyclerAdapter.ViewHolder viewHolder, int i) {
        viewHolder.currentTask = taskList.get(i);
        viewHolder.title.setText(viewHolder.currentTask.getTitle());
        viewHolder.desc.setText(viewHolder.currentTask.getDescription());
        viewHolder.rewardTitle.setText(viewHolder.currentTask.getRewardTitle());
        viewHolder.reward.setText(Long.toString(viewHolder.currentTask.getRewardValue()));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Task currentTask;
        public TextView title;
        public TextView desc;
        public TextView reward;
        public TextView rewardTitle;
        public Button complete;
        public Button refresh;
        public Button trashButton;

        //Firebase Variables
        private DatabaseReference mDatabaseReference;
        private FirebaseDatabase mDatabase;
        private FirebaseAuth mAuth;
        //------------------------------------------------------

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            title = (TextView) itemView.findViewById(R.id.taskTitle);
            desc = (TextView) itemView.findViewById(R.id.taskDecription);
            reward = (TextView) itemView.findViewById(R.id.reward);
            rewardTitle = (TextView) itemView.findViewById(R.id.rewardTitle);
            complete = (Button) itemView.findViewById(R.id.complete);
            refresh = (Button) itemView.findViewById(R.id.RefreshButton);
            trashButton = (Button) itemView.findViewById(R.id.TrashButton);

            complete.setVisibility(View.INVISIBLE);
            refresh.setVisibility(View.INVISIBLE);
            trashButton.setVisibility(View.INVISIBLE);

            View.OnClickListener selectTask = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetupDatabase();
                    mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String currentAccess = dataSnapshot.getValue(String.class);
                            mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("DailyTasks");
                            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long countTask = dataSnapshot.getChildrenCount();
                                    mDatabaseReference = mDatabaseReference.child(Long.toString(countTask));
                                    mDatabaseReference.setValue(currentTask);
                                    Intent intent = new Intent(context, AdminTasksActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            };

            title.setOnClickListener(selectTask);
            desc.setOnClickListener(selectTask);
            reward.setOnClickListener(selectTask);
            rewardTitle.setOnClickListener(selectTask);
        }

        private void SetupDatabase() {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference();
        }
    }
}
