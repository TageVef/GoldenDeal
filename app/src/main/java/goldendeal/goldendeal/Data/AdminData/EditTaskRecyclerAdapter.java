package goldendeal.goldendeal.Data.AdminData;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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

import goldendeal.goldendeal.Activities.AdminActivity.TaskActivitys.AddNewTaskActivity;
import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class EditTaskRecyclerAdapter extends RecyclerView.Adapter<EditTaskRecyclerAdapter.ViewHolder> {
    private Context context;
    public List<Task> taskList;

    public EditTaskRecyclerAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_layout, viewGroup, false);
        return new EditTaskRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
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
                            String currentAccess = dataSnapshot.getValue(String.class);
                            //sorting tasks in database.
                            mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Tasks");
                            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot task : dataSnapshot.getChildren()) {
                                        Task sortTasks = task.getValue(Task.class);
                                        sortTasks.setId(Long.parseLong(task.getKey()));
                                        if (currentTask.getId() < sortTasks.getId()) {
                                            sortTasks.setId(sortTasks.getId() - 1);
                                            mDatabaseReference.child(Long.toString(sortTasks.getId())).child("title").setValue(sortTasks.getTitle());
                                            mDatabaseReference.child(Long.toString(sortTasks.getId())).child("description").setValue(sortTasks.getDescription());
                                            mDatabaseReference.child(Long.toString(sortTasks.getId())).child("rewardTitle").setValue(sortTasks.getRewardTitle());
                                            mDatabaseReference.child(Long.toString(sortTasks.getId())).child("rewardValue").setValue(sortTasks.getRewardValue());
                                        }

                                        if (Long.parseLong(task.getKey()) == dataSnapshot.getChildrenCount() - 1) {
                                            mDatabaseReference.child(Long.toString(dataSnapshot.getChildrenCount() - 1)).removeValue();
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            /*mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot tasks : dataSnapshot.getChildren()) {
                                        Task sortTasks = tasks.getValue(Task.class);
                                        if (currentTask.getId() < sortTasks.getId()) {
                                            sortTasks.setId(sortTasks.getId() - 1);
                                            mDatabaseReference.child(Long.toString(sortTasks.getId())).setValue(sortTasks);
                                        }
                                        if (Long.parseLong(tasks.getKey()) == dataSnapshot.getChildrenCount() - 1) {
                                            mDatabaseReference.child(Long.toString(dataSnapshot.getChildrenCount() - 1)).removeValue();
                                        }
                                    }

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });*/
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });
            complete.setVisibility(View.INVISIBLE);

            View.OnClickListener viewClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddNewTaskActivity.class);
                    intent.putExtra("TaskID", Long.toString(currentTask.getId()));
                    context.startActivity(intent);
                }
            };
            itemView.setOnClickListener(viewClick);

            SetupDatabase();
            SetupLanguage();
        }

        private void SetupDatabase() {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference();
        }

        private void SetupLanguage(){
            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String language = dataSnapshot.getValue(String.class);

                    if(TextUtils.equals(language, "Norsk")){
                        complete.setText("Godkjent");
                        refresh.setText("Avslag");
                        trashButton.setText("Kast");
                    } else if(TextUtils.equals(language, "English")){
                        complete.setText("Accept");
                        refresh.setText("Reject");
                        trashButton.setText("Trash");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
