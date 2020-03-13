package com.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.parkingfinderprojectfinal.R;
import com.example.parkingfinderprojectfinal.order;
import com.model.Order;
import com.model.Place;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ItemHolder> {
    private List<Order> mOrders = new ArrayList<>();

    private Context context;
    private final OnOrderClickListener mListener;

    public  OrderAdapter(Context context,String[] orderNames){
        this.context=context;
        try {
            this.mListener=((OnOrderClickListener)context);
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnOrderClickListener.");
        }
        order o = new order();
        String[]  names =o.orderNames;
        String[] locs=o.orderLocs;
        String[] prices = o.orderPrices;
        String[] hrs = o.orderHrs;

        for(int i=0;i<names.length;i++){
            Order order = new Order((i+1),names[i],locs[i],prices[i]+" Rupees",hrs[i]+" hours");
            mOrders.add(order);
        }
    }

    public void setFavorite(int orderId) {
        if (mOrders.size() > 0) {
            for (int i = 0; i < mOrders.size(); i++) {
                if (mOrders.get(i).getPlaceId() == orderId) {
                    if (!mOrders.get(i).isFavorite()) {
                        mOrders.get(i).setFavorite(true);
                        break;
                    } else {
                        mOrders.get(i).setFavorite(false);
                        break;
                    }
                }
            }
        }
    }
    @NonNull
    @Override
    public OrderAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_card_order, viewGroup, false);
        OrderAdapter.ItemHolder holder = new OrderAdapter.ItemHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final Order order=mOrders.get(position);
        holder.mItem=order;
        holder.placeName.setText(order.getPlaceName());
        holder.placeLocation.setText(order.getLocation());
        holder.placeHours.setText(order.getPlaceHours());
        holder.placePrice.setText(order.getPlacePrice());
        if (holder.mItem.isFavorite()) {
            holder.icFavorite.setImageResource(R.drawable.star);
        } else {
            holder.icFavorite.setImageResource(R.drawable.star2);
        }
        holder.lnlFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener  != null)
                    mListener.onOrderFavoriteClick(order);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onOrderClickListener(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }
    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView placeName, placeLocation, placePrice, placeHours;
        public RelativeLayout lnlFavorite;
        public ImageView icFavorite;
        public final View mView;
        public Order mItem;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            placeName = itemView.findViewById(R.id.place_name);
            placeLocation = itemView.findViewById(R.id.place_location);
            placePrice = itemView.findViewById(R.id.place_price);
            placeHours = itemView.findViewById(R.id.place_hours);
            lnlFavorite = itemView.findViewById(R.id.lnl_favorite);
            icFavorite = itemView.findViewById(R.id.ic_favorite);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked Item", Toast.LENGTH_SHORT).show();
        }
    }
    public interface OnOrderClickListener {
        void onOrderClickListener(Order order);
        void onOrderFavoriteClick(Order order);
    }

}
