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
import android.widget.Toast;

import java.util.List;

import goldendeal.goldendeal.Activities.AdminActivity.AddNewTaskActivity;
import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class EditTaskRecyclerAdapter extends RecyclerView.Adapter<EditTaskRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Task> taskList;

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
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        viewHolder.task = taskList.get(i);
        viewHolder.title.setText(viewHolder.task.getTitle());
        viewHolder.desc.setText(viewHolder.task.getDescription());
        viewHolder.rewardTitle.setText(viewHolder.task.getRewardTitle());
        viewHolder.reward.setText(Long.toString(viewHolder.task.getRewardValue()));
        viewHolder.trashButton.setVisibility(View.VISIBLE);

        viewHolder.trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        viewHolder.complete.setVisibility(View.INVISIBLE);

        View.OnClickListener viewClick = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNewTaskActivity.class);
                intent.putExtra("TaskID", Long.toString(viewHolder.task.getId()));
                context.startActivity(intent);
            }
        };

        viewHolder.title.setOnClickListener(viewClick);
        viewHolder.desc.setOnClickListener(viewClick);
        viewHolder.reward.setOnClickListener(viewClick);
        viewHolder.rewardTitle.setOnClickListener(viewClick);
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Task task;
        public TextView title;
        public TextView desc;
        public TextView reward;
        public TextView rewardTitle;
        public Button complete;
        public Button trashButton;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
            title = (TextView) itemView.findViewById(R.id.taskTitle);
            desc = (TextView) itemView.findViewById(R.id.taskDecription);
            reward = (TextView) itemView.findViewById(R.id.reward);
            rewardTitle = (TextView) itemView.findViewById(R.id.rewardTitle);
            complete = (Button) itemView.findViewById(R.id.complete);
            trashButton = (Button) itemView.findViewById(R.id.TrashButton);
        }
    }
}
