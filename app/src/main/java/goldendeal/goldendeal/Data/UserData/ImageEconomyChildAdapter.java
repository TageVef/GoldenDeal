package goldendeal.goldendeal.Data.UserData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class ImageEconomyChildAdapter extends RecyclerView.Adapter<ImageEconomyChildAdapter.ViewHolder> {
    private String theme;
    private Context context;
    private List<VirtualCurrency> CurrencyRowList;

    public ImageEconomyChildAdapter(Context context, List<VirtualCurrency> currencyRowList, String theme) {
        this.theme = theme;
        this.context = context;
        CurrencyRowList = currencyRowList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_economy_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.CurrencyRow = CurrencyRowList.get(i);


        for (int j = 0; j < viewHolder.CurrencyRow.getMaxValue(); j++) {
            viewHolder.imageList.get(j).setVisibility(View.VISIBLE);
            switch(theme){
                case "Mermaids":
                    viewHolder.imageList.get(j).setImageResource(R.drawable.mermaids_prize_black);
                    break;
                case "Western":
                    viewHolder.imageList.get(j).setImageResource(R.drawable.western_prize_black);
                    break;
                case "Space":
                    viewHolder.imageList.get(j).setImageResource(R.drawable.space_prize_black);
                    break;
                case "Spring":
                    viewHolder.imageList.get(j).setImageResource(R.drawable.spring_prize_black);
                    break;
                case "Summer":
                    viewHolder.imageList.get(j).setImageResource(R.drawable.summer_prize_black);
                    break;
                case "Fall":
                    viewHolder.imageList.get(j).setImageResource(R.drawable.fall_prize_black);
                    break;
                case "Winter":
                    viewHolder.imageList.get(j).setImageResource(R.drawable.winter_prize_black);
                    break;
                case "Standard":
                    viewHolder.imageList.get(j).setImageResource(R.drawable.pirate_prize_black);
                    break;
            }
        }

        if (viewHolder.CurrencyRow.getValue() > 0) {
            for (int j = 0; j < viewHolder.CurrencyRow.getValue(); j++) {
                switch(theme){
                    case "Mermaids":
                        viewHolder.imageList.get(j).setImageResource(R.drawable.mermaids_prize);
                        break;
                    case "Western":
                        viewHolder.imageList.get(j).setImageResource(R.drawable.western_prize);
                        break;
                    case "Space":
                        viewHolder.imageList.get(j).setImageResource(R.drawable.space_prize);
                        break;
                    case "Spring":
                        viewHolder.imageList.get(j).setImageResource(R.drawable.spring_prize);
                        break;
                    case "Summer":
                        viewHolder.imageList.get(j).setImageResource(R.drawable.summer_prize);
                        break;
                    case "Fall":
                        viewHolder.imageList.get(j).setImageResource(R.drawable.fall_prize);
                        break;
                    case "Winter":
                        viewHolder.imageList.get(j).setImageResource(R.drawable.winter_prize);
                        break;
                    case "Standard":
                        viewHolder.imageList.get(j).setImageResource(R.drawable.pirate_prize);
                        break;
                }
            }
        }


    }

    @Override
    public int getItemCount() {
        return CurrencyRowList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public List<ImageView> imageList;

        public VirtualCurrency CurrencyRow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageList = new ArrayList<ImageView>();

            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy1));
            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy2));
            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy3));
            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy4));
            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy5));
            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy6));
            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy7));
            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy8));
            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy9));
            imageList.add((ImageView) itemView.findViewById(R.id.ImageEconomy10));

            for (ImageView view : imageList) {
                view.setVisibility(view.INVISIBLE);
            }


        }
    }
}
