package goldendeal.goldendeal.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class AdminTaskRecyclerAdapter extends RecyclerView.Adapter<AdminTaskRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Task> taskList;

    public AdminTaskRecyclerAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public AdminTaskRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.task_layout, viewGroup, false);
        return new AdminTaskRecyclerAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminTaskRecyclerAdapter.ViewHolder viewHolder, int i) {
        Task task = taskList.get(i);
        viewHolder.title.setText(task.getTitle());
        viewHolder.desc.setText(task.getDescription());
        viewHolder.rewardTitle.setText(task.getRewardTitle());
        viewHolder.reward.setText(Long.toString(task.getRewardValue()));
        viewHolder.trashButton.setVisibility(View.VISIBLE);
        viewHolder.trashButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (task.isComplete()) {
            viewHolder.complete.setVisibility(View.VISIBLE);
            viewHolder.complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            viewHolder.complete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
