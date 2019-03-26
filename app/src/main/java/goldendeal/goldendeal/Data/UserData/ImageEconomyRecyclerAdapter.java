package goldendeal.goldendeal.Data.UserData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class ImageEconomyRecyclerAdapter extends RecyclerView.Adapter<ImageEconomyRecyclerAdapter.ViewHolder> {


    private Context context;
    public List<VirtualCurrency> currencyList;

    public ImageEconomyRecyclerAdapter(Context context, List<VirtualCurrency> currencyList) {
        this.context = context;
        this.currencyList = currencyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_economy_parent_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.activeCurrency = currencyList.get(i);
        viewHolder.currencyTitle.setText(viewHolder.activeCurrency.getTitle());

        viewHolder.initialiseRecycler();
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RecyclerView currencyRecycler;
        public ImageEconomyChildAdapter imageEconomyChildAdapter;
        public TextView currencyTitle;
        public VirtualCurrency activeCurrency;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currencyRecycler = (RecyclerView) itemView.findViewById(R.id.CurrencyRecycler);
            currencyTitle = (TextView) itemView.findViewById(R.id.CurrencyTitle);
        }

        public void initialiseRecycler() {

            Long currencyAmount = activeCurrency.getMaxValue();
            Long obtainedCurrency = activeCurrency.getValue();
            List<VirtualCurrency> currencyRowList = new ArrayList<VirtualCurrency>();


            while (currencyAmount > 0) {

                if (currencyAmount > 10) {
                    if (obtainedCurrency > 10) {
                        currencyRowList.add(new VirtualCurrency((long) 10, (long) 10));
                        currencyAmount -= 10;
                        obtainedCurrency -= 10;
                    } else {
                        currencyRowList.add(new VirtualCurrency(obtainedCurrency, (long) 10));
                        currencyAmount -= 10;
                        obtainedCurrency -= obtainedCurrency;
                    }
                } else if (currencyAmount > 0) {
                    currencyRowList.add(new VirtualCurrency(obtainedCurrency, currencyAmount));
                    currencyAmount -= currencyAmount;
                    obtainedCurrency -= obtainedCurrency;
                }
            }
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(currencyRecycler.getContext(), LinearLayoutManager.VERTICAL, false);
            imageEconomyChildAdapter = new ImageEconomyChildAdapter(context, currencyRowList);
            currencyRecycler.setLayoutManager(layoutManager);
            currencyRecycler.setAdapter(imageEconomyChildAdapter);
            imageEconomyChildAdapter.notifyDataSetChanged();

        }
    }
}
