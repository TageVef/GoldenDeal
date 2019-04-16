package goldendeal.goldendeal.Data.AdminData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import goldendeal.goldendeal.R;

public class AdminRulesRecyclerAdapter extends RecyclerView.Adapter<AdminRulesRecyclerAdapter.ViewHolder> {
    public List<String> rulesList;
    private Context context;

    public AdminRulesRecyclerAdapter(List<String> rulesList, Context context) {
        this.rulesList = rulesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rule_layout, viewGroup, false);
        return new AdminRulesRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.ViewHolderSetup(rulesList.get(i), i+1);
    }

    @Override
    public int getItemCount() {
        return rulesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public String ruleText;
        public long ruleNumber;

        private TextView ruleNumberText;
        private TextView ruleDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ruleDescription = (TextView) itemView.findViewById(R.id.RuleDescription);
            ruleNumberText = (TextView) itemView.findViewById(R.id.NumberText);
        }

        public void ViewHolderSetup(String ruleDescription, long number) {
            ruleText = ruleDescription;
            ruleNumber = number;
            ruleNumberText.setText(Long.toString(ruleNumber) + ":");
            this.ruleDescription.setText(ruleText);

        }
    }
}
