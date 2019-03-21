package goldendeal.goldendeal.Data.AdminData;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class AdminTaskRecyclerAdapter extends RecyclerView.Adapter<AdminTaskRecyclerAdapter.ViewHolder> {
    private static final String TAG = "AdminTaskRecyclerAdapter";
    private Context context;
    public List<Task> taskList;


    public AdminTaskRecyclerAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_layout, viewGroup, false);
        return new AdminTaskRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTaskRecyclerAdapter.ViewHolder viewHolder, int i) {
        viewHolder.currentTask = taskList.get(i);
        viewHolder.title.setText(viewHolder.currentTask.getTitle());
        viewHolder.desc.setText(viewHolder.currentTask.getDescription());
        viewHolder.rewardTitle.setText(viewHolder.currentTask.getRewardTitle());
        viewHolder.reward.setText(Long.toString(viewHolder.currentTask.getRewardValue()));
        if (viewHolder.currentTask.isComplete()) {
            viewHolder.complete.setVisibility(View.VISIBLE);
            viewHolder.refresh.setVisibility(View.VISIBLE);
        } else {
            viewHolder.complete.setVisibility(View.INVISIBLE);
            viewHolder.refresh.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "recycleradapter";
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
        private DatabaseReference removeTask;
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

            refresh.setVisibility(View.INVISIBLE);
            trashButton.setVisibility(View.VISIBLE);
            trashButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetupDatabase();

                    mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String currentAccess = dataSnapshot.getValue(String.class);
                            //removing task from database
                            removeTask = mDatabase.getReference().child("User").child(currentAccess).child("DailyTasks");
                            removeTask.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot task : dataSnapshot.getChildren()) {
                                        Task sortTasks = task.getValue(Task.class);
                                        if (currentTask.getId() < sortTasks.getId()) {
                                            sortTasks.setId(sortTasks.getId() - 1);
                                            removeTask.child(Long.toString(sortTasks.getId())).setValue(sortTasks);
                                        }

                                        if (Long.parseLong(task.getKey()) == dataSnapshot.getChildrenCount() - 1) {
                                            removeTask.child(Long.toString(dataSnapshot.getChildrenCount() - 1)).removeValue();
                                        }
                                    }
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
            });
            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetupDatabase();

                    mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String currentAccess = dataSnapshot.getValue(String.class);
                            removeTask = mDatabase.getReference().child("User").child(currentAccess).child("Bank").child(currentTask.getRewardTitle()).child("value");
                            removeTask.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long oldReward = 0;
                                    if (dataSnapshot.getValue() != null) {
                                        oldReward = dataSnapshot.getValue(Long.class);
                                    }
                                    oldReward += currentTask.getRewardValue();
                                    removeTask.setValue(oldReward);
                                    removeTask = removeTask.getParent().getParent().getParent().child("History");
                                    removeTask.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Long historyCount = dataSnapshot.getChildrenCount();
                                            removeTask.child(historyCount.toString()).child("title").setValue(currentTask.getTitle());
                                            removeTask.child(historyCount.toString()).child("description").setValue(currentTask.getDescription());
                                            removeTask.child(historyCount.toString()).child("rewardValue").setValue(currentTask.getRewardValue());
                                            removeTask.child(historyCount.toString()).child("rewardTitle").setValue(currentTask.getRewardTitle());
                                            java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                                            String date = dateFormat.format(new Date(Long.valueOf(java.lang.System.currentTimeMillis())).getTime());
                                            removeTask.child(historyCount.toString()).child("completionTime").setValue(date);

                                            removeTask = removeTask.getParent().child("DailyTasks");
                                            removeTask.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot task : dataSnapshot.getChildren()) {
                                                        Task sortTasks = task.getValue(Task.class);
                                                        if (currentTask.getId() < sortTasks.getId()) {
                                                            sortTasks.setId(sortTasks.getId() - 1);
                                                            removeTask.child(Long.toString(sortTasks.getId())).setValue(sortTasks);
                                                        }

                                                        if (Long.parseLong(task.getKey()) == dataSnapshot.getChildrenCount() - 1) {
                                                            removeTask.child(Long.toString(dataSnapshot.getChildrenCount() - 1)).removeValue();
                                                        }
                                                    }
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


                                    /*removeTask.removeValue(new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                            //sorting tasks in database.
                                            removeTask = removeTask.getParent();
                                            removeTask.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.getKey() != null){
                                                        int i = 0;
                                                        for(DataSnapshot tasks: dataSnapshot.getChildren()){
                                                            Task currentTask = tasks.getValue(Task.class);
                                                            currentTask.setId(i);
                                                            removeTask.child(Integer.toString(i)).setValue(currentTask);
                                                            i++;
                                                            if(i == dataSnapshot.getChildrenCount()){
                                                                removeTask.child(Integer.toString(i)).removeValue();
                                                            }
                                                        }

                                                    }

                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });
                                        }
                                    });*/
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
            });
            refresh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetupDatabase();

                    mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String currentAccess = dataSnapshot.getValue(String.class);
                            mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("DailyTasks").child(Long.toString(currentTask.getId()));
                            mDatabaseReference.child("complete").setValue(Boolean.FALSE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
        }

        private void SetupDatabase() {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference();
        }
    }
}
