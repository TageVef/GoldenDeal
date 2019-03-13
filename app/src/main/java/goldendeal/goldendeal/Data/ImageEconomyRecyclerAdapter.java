package goldendeal.goldendeal.Data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Currency;
import java.util.List;

import goldendeal.goldendeal.R;

public class ImageEconomyRecyclerAdapter extends RecyclerView.Adapter<ImageEconomyRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Currency> currencyList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_economy_layout, viewGroup, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
        }
    }
}
