package goldendeal.goldendeal.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import goldendeal.goldendeal.Model.Reward;
import goldendeal.goldendeal.R;

public class CounterRecyclerAdapter extends RecyclerView.Adapter<CounterRecyclerAdapter.ViewHolder> {
    private Context context;
    public List<Reward> counterList;

    public CounterRecyclerAdapter(Context context, List<Reward> counterList) {
        this.context = context;
        this.counterList = counterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.counter_layout, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.currentReward = counterList.get(i);
        viewHolder.value.setText(viewHolder.currentReward.printValue());
        viewHolder.title.setText(viewHolder.currentReward.getTitle() + ": ");
    }

    @Override
    public int getItemCount() {return counterList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Reward currentReward;
        public TextView value;
        public TextView title;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);

            context = ctx;
            value = (TextView) itemView.findViewById(R.id.Value);
            title = (TextView) itemView.findViewById(R.id.Title);
        }
    }
}
