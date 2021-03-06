package goldendeal.goldendeal.Data.UserData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    private Context context;
    public List<Task> taskList;

    public TaskRecyclerAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_layout, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.currentTask = taskList.get(i);

        viewHolder.title.setText(viewHolder.currentTask.getTitle());
        viewHolder.desc.setText(viewHolder.currentTask.getDescription());
        viewHolder.rewardTitle.setText(viewHolder.currentTask.getRewardTitle());
        viewHolder.reward.setText(Long.toString(viewHolder.currentTask.getRewardValue()));

        showTextCheck(viewHolder);
    }

    private void showTextCheck(@NonNull ViewHolder viewHolder) {
        if (viewHolder.currentTask.isShowText()) {
            viewHolder.desc.setVisibility(View.VISIBLE);
            viewHolder.reward.setVisibility(View.VISIBLE);
            viewHolder.rewardTitle.setVisibility(View.VISIBLE);
            if (viewHolder.currentTask.isComplete()) {
                viewHolder.complete.setVisibility(View.GONE);
            } else {
                viewHolder.complete.setVisibility(View.VISIBLE);
            }

        } else {
            viewHolder.desc.setVisibility(View.GONE);
            viewHolder.reward.setVisibility(View.GONE);
            viewHolder.rewardTitle.setVisibility(View.GONE);
            viewHolder.complete.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Task currentTask;
        public ConstraintLayout taskLayout;
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
            taskLayout = (ConstraintLayout) itemView.findViewById(R.id.TaskLayout);
            title = (TextView) itemView.findViewById(R.id.taskTitle);
            desc = (TextView) itemView.findViewById(R.id.taskDecription);
            reward = (TextView) itemView.findViewById(R.id.reward);
            rewardTitle = (TextView) itemView.findViewById(R.id.rewardTitle);
            complete = (Button) itemView.findViewById(R.id.complete);
            refresh = (Button) itemView.findViewById(R.id.RefreshButton);
            trashButton = (Button) itemView.findViewById(R.id.TrashButton);

            trashButton.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);

            title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetupDatabase();

                    currentTask.setShowText(!currentTask.isShowText());
                    mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("DailyTasks").child(Long.toString(currentTask.getId()));
                    mDatabaseReference.child("showText").setValue(currentTask.isShowText());
                }
            });

            complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTask != null) {
                        SetupDatabase();
                        mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("DailyTasks").child(Long.toString(currentTask.getId()));
                        mDatabaseReference.child("complete").setValue(Boolean.TRUE);
                    }


                }
            });

            SetupDatabase();
            SetupLanguage();
        }

        private void SetupDatabase() {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference();
        }

        private void SetupLanguage(){
            mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info").child("language");
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String language = dataSnapshot.getValue(String.class);

                    if(TextUtils.equals(language, "Norsk")){
                        complete.setText("Fullfør");
                    } else if(TextUtils.equals(language, "English")){
                        complete.setText("Complete");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
