package goldendeal.goldendeal.Data.UserData;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import goldendeal.goldendeal.Model.StoreItem;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class StoreRecyclerAdapter extends RecyclerView.Adapter<StoreRecyclerAdapter.ViewHolder> {
    public List<StoreItem> itemList;
    public Context context;

    public StoreRecyclerAdapter(List<StoreItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reward_layout, viewGroup, false);
        return new StoreRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.ViewHolderSetup(itemList.get(i));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        StoreItem currentItem;

        public ImageView rewardImage;
        public TextView titleText;
        public TextView descriptionText;
        public TextView rewardValue;
        public TextView rewardType;
        public Button trashbutton;
        public Button completeButton;
        public Button rejectButton;

        //Firebase Variables
        private DatabaseReference mDatabaseReference;
        private FirebaseDatabase mDatabase;
        private FirebaseAuth mAuth;
        //------------------------------------------------------

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            rewardImage = (ImageView) itemView.findViewById(R.id.RewardPicture);
            titleText = (TextView) itemView.findViewById(R.id.TitleText);
            descriptionText = (TextView) itemView.findViewById(R.id.DescriptionText);
            rewardValue = (TextView) itemView.findViewById(R.id.CurrencyAmountText);
            rewardType = (TextView) itemView.findViewById(R.id.CurrencyTypeText);
            trashbutton = (Button) itemView.findViewById(R.id.TrashButton);
            completeButton = (Button) itemView.findViewById(R.id.BuyButton);
            rejectButton = (Button) itemView.findViewById(R.id.RejectButton);

            rejectButton.setVisibility(View.GONE);
            trashbutton.setVisibility(View.GONE);
            rewardImage.setVisibility(View.GONE);

            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetupDatabase();
                    mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Bank").child(currentItem.getCurrency().getTitle());
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            VirtualCurrency currentCurrency = dataSnapshot.getValue(VirtualCurrency.class);
                            if (currentCurrency.getValue() >= currentItem.getCurrency().getValue()) {
                                mDatabaseReference = mDatabaseReference.getParent().getParent().child("Store");
                                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot storeItems : dataSnapshot.getChildren()) {
                                            StoreItem currentStoreItem = storeItems.getValue(StoreItem.class);
                                            if (TextUtils.equals(currentStoreItem.getTitle(), currentItem.getTitle())) {
                                                mDatabaseReference.child(storeItems.getKey()).child("bought").setValue(Boolean.TRUE);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            } else {
                                Toast.makeText(context, "You dont have enough for that!", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });

        }

        private void ViewHolderSetup(StoreItem item) {
            SetupLanguage();
            currentItem = item;
            if (currentItem.isBought()) {
                completeButton.setVisibility(View.GONE);
            } else {
                completeButton.setVisibility(View.VISIBLE);
            }

            titleText.setText(currentItem.getTitle());
            descriptionText.setText(currentItem.getDescription());
            rewardValue.setText(Long.toString(currentItem.getCurrency().getValue()));
            rewardType.setText(currentItem.getCurrency().getTitle());

        }

        private void SetupDatabase() {
            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference();
        }

        private void SetupLanguage(){
            SetupDatabase();
            mDatabaseReference = mDatabase.getReference().child("User").child(mAuth.getUid()).child("Info").child("language");
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String language = dataSnapshot.getValue(String.class);

                    if(TextUtils.equals(language, "Norsk")){
                        completeButton.setText("Kjøp");
                    } else if(TextUtils.equals(language, "English")){
                        completeButton.setText("Buy");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
