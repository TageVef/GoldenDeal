package goldendeal.goldendeal.Data.AdminData;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
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

import goldendeal.goldendeal.Activities.AdminActivity.StoreActivity.NewRewardActivity;
import goldendeal.goldendeal.Model.StoreItem;
import goldendeal.goldendeal.Model.VirtualCurrency;
import goldendeal.goldendeal.R;

public class AdminStoreRecyclerAdapter extends RecyclerView.Adapter<AdminStoreRecyclerAdapter.ViewHolder> {
    public List<StoreItem> itemList;
    public Context context;

    public AdminStoreRecyclerAdapter(List<StoreItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reward_layout , viewGroup, false);
        return new AdminStoreRecyclerAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.ViewHolderSetUp(itemList.get(i));
    }

    @Override
    public int getItemCount() {return itemList.size();}

    public class ViewHolder extends RecyclerView.ViewHolder {
        public StoreItem currentItem;

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
        private DatabaseReference removeStoreItem;
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

            completeButton.setText("Complete");
            completeButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            rewardImage.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NewRewardActivity.class);
                    intent.putExtra("StoreItemID", Long.toString(currentItem.getId()));
                    context.startActivity(intent);
                }
            });

            trashbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: set up removal of item
                    SetupDatabase();
                    mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final String currentAccess = dataSnapshot.getValue(String.class);
                            removeStoreItem = mDatabase.getReference().child("User").child(currentAccess).child("Store");
                            removeStoreItem.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    long moveCounter = dataSnapshot.getChildrenCount();
                                    for(DataSnapshot storeItems: dataSnapshot.getChildren()){
                                        StoreItem currentStoreItem = storeItems.getValue(StoreItem.class);
                                        if(TextUtils.equals(currentStoreItem.getTitle(), currentItem.getTitle())){
                                            moveCounter = Long.parseLong(storeItems.getKey());
                                        }
                                        if(Long.parseLong(storeItems.getKey()) > moveCounter){
                                            removeStoreItem.child(Long.toString(Long.parseLong(storeItems.getKey()) - 1)).setValue(currentStoreItem);
                                        }
                                        if(Long.parseLong(storeItems.getKey()) == (dataSnapshot.getChildrenCount() - 1)){
                                            removeStoreItem.child(storeItems.getKey()).removeValue();
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

            completeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: set up subtraction of currency
                    SetupDatabase();
                    mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String currentAccess = dataSnapshot.getValue(String.class);
                            mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Bank").child(currentItem.getCurrency().getTitle());
                            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    VirtualCurrency currentCurrency = dataSnapshot.getValue(VirtualCurrency.class);
                                    if(currentCurrency.getValue() > currentItem.getCurrency().getValue()){
                                        currentCurrency.setValue(currentCurrency.getValue() - currentItem.getCurrency().getValue());
                                        mDatabaseReference.setValue(currentCurrency);
                                        mDatabaseReference = mDatabaseReference.getParent().getParent().child("Store");
                                        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for(DataSnapshot storeItems: dataSnapshot.getChildren()){
                                                    StoreItem currentStoreItem = storeItems.getValue(StoreItem.class);
                                                    if(TextUtils.equals(currentStoreItem.getTitle(), currentItem.getTitle())){
                                                        mDatabaseReference.child(storeItems.getKey()).child("bought").setValue(Boolean.FALSE);
                                                        return;
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                    } else {
                                        Toast.makeText(context, "This item can not be bought", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetupDatabase();
                    mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("CurrentAccess");
                    mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String currentAccess = dataSnapshot.getValue(String.class);
                            mDatabaseReference = mDatabase.getReference().child("User").child(currentAccess).child("Store");
                            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for(DataSnapshot storeItems: dataSnapshot.getChildren()){
                                        StoreItem currentStoreItem = storeItems.getValue(StoreItem.class);
                                        if(TextUtils.equals(currentStoreItem.getTitle(), currentItem.getTitle())){
                                            mDatabaseReference.child(storeItems.getKey()).child("bought").setValue(Boolean.FALSE);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            });
            SetupDatabase();
            SetupLanguage();
        }

        public void ViewHolderSetUp(StoreItem newItem){
            currentItem = newItem;
            if(newItem.isBought()){
                completeButton.setVisibility(View.VISIBLE);
                rejectButton.setVisibility(View.VISIBLE);
            } else {
                completeButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
            }
            //TODO: set up picture downloading and showing
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
            mDatabaseReference = mDatabase.getReference().child("Admin").child(mAuth.getUid()).child("Info").child("language");
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String language = dataSnapshot.getValue(String.class);

                    if(TextUtils.equals(language, "Norsk")){
                        trashbutton.setText("Kast");
                        completeButton.setText("Fullfør");
                        rejectButton.setText("Avslå");
                    } else if(TextUtils.equals(language, "English")){
                        trashbutton.setText("Trash");
                        completeButton.setText("Complete");
                        rejectButton.setText("Reject");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
