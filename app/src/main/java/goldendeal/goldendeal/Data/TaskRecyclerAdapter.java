package goldendeal.goldendeal.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import goldendeal.goldendeal.Model.Task;
import goldendeal.goldendeal.R;

public class TaskRecyclerAdapter extends RecyclerView.Adapter<TaskRecyclerAdapter.ViewHolder> {

    private Context context;
    private List<Task> taskList;

    public TaskRecyclerAdapter(Context context, List<Task> taskList) {
        this.context = context;
        this.taskList = taskList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.task_layout, viewGroup,false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Task task = taskList.get(i);

        viewHolder.title.setText(task.getTitle());
        viewHolder.desc.setText(task.getDescription());
        viewHolder.rewardTitle.setText(task.getRewardTitle());
        viewHolder.reward.setText(task.getRewardValue());

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

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;
            title = (TextView) itemView.findViewById(R.id.taskTitle);
            desc = (TextView) itemView.findViewById(R.id.taskDecription);
            reward = (TextView) itemView.findViewById(R.id.reward);
            rewardTitle = (TextView) itemView.findViewById(R.id.rewardTitle);
            complete = (Button) itemView.findViewById(R.id.complete);




        }
    }
}
