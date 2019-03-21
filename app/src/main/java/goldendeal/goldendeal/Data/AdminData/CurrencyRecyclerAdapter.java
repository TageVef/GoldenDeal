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

import org.w3c.dom.Text;

import java.util.List;

import goldendeal.goldendeal.Activities.AdminActivity.BankActivities.AddRemoveCurrencies;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class CurrencyRecyclerAdapter extends RecyclerView.Adapter<CurrencyRecyclerAdapter.ViewHolder> {
    public Context context;
    public List<VirtualCurrency> currencyList;

    public CurrencyRecyclerAdapter(Context context, List<VirtualCurrency> currencyList) {
        this.context = context;
        this.currencyList = currencyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.currency_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.currentCurrency = currencyList.get(i);
        viewHolder.SettingUpViews();
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public VirtualCurrency currentCurrency;
        public TextView titleText;
        public TextView valueText;
        public TextView maxValueText;
        public TextView fillerText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = (TextView) itemView.findViewById(R.id.Title);
            valueText = (TextView) itemView.findViewById(R.id.Value);
            maxValueText = (TextView) itemView.findViewById(R.id.MaxValue);
            fillerText = (TextView) itemView.findViewById(R.id.FillerText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, AddRemoveCurrencies.class);
                    intent.putExtra("Currency", currentCurrency.getTitle());
                    context.startActivity(intent);

                }
            });
        }

        public void SettingUpViews() {
            titleText.setText(currentCurrency.getTitle());
            valueText.setText(currentCurrency.getValue().toString());
            if (currentCurrency.isImageEconomy()) {
                maxValueText.setText(currentCurrency.getMaxValue().toString());
                maxValueText.setVisibility(View.VISIBLE);
                fillerText.setVisibility(View.VISIBLE);
            }
        }
    }
}
