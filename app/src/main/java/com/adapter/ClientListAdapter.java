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
import com.example.parkingfinderprojectfinal.clientList;
import com.model.ClientList;


import java.util.ArrayList;
import java.util.List;

public class ClientListAdapter extends RecyclerView.Adapter<ClientListAdapter.ItemHolder> {
    private List<ClientList> mClients = new ArrayList<>();
    private Context context;
    private final OnClientClickListener mListener;

    public ClientListAdapter(Context context,String[] Clientnames){
        this.context=context;
        try {
            this.mListener=((OnClientClickListener)context);
        }catch (ClassCastException e){
            throw new ClassCastException("Activity must implement OnClientClickListener.");
        }

        clientList o = new clientList();
        String[]  names =o.ClientNames;
        String[] no=o.ClientNos;
        String[] prices = o.ClientPrices;
        String[] hrs = o.ClientHrs;
        for(int i=0;i<names.length;i++){
            ClientList order = new ClientList((i+1),names[i],no[i],prices[i]+" Rupees",hrs[i]+" hours");
            mClients.add(order);
        }
    }
    public void setFavorite(int orderId) {
        if (mClients.size() > 0) {
            for (int i = 0; i < mClients.size(); i++) {
                if (mClients.get(i).getPlaceId() == orderId) {
                    if (!mClients.get(i).isFavorite()) {
                        mClients.get(i).setFavorite(true);
                        break;
                    } else {
                        mClients.get(i).setFavorite(false);
                        break;
                    }
                }
            }
        }
    }

    @NonNull
    @Override
    public ClientListAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_card_clients, viewGroup, false);
        ClientListAdapter.ItemHolder holder = new ClientListAdapter.ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ClientListAdapter.ItemHolder holder, int position) {
        final ClientList c = mClients.get(position);
        holder.mItem=c;
        holder.ClientName.setText(c.getClientName());
        holder.mno.setText(c.getMno());
        holder.ClientPrice.setText(c.getClientPrice());
        holder.ClientHours.setText(c.getClientHours());
        if (holder.mItem.isFavorite()) {
            holder.icFavorite.setImageResource(R.drawable.star);
        } else {
            holder.icFavorite.setImageResource(R.drawable.star2);
        }
        holder.lnlFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener  != null)
                    mListener.onClientFavoriteClick(c);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onClientClickListener(c);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mClients.size();
    }
    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView ClientName, mno, ClientPrice, ClientHours;
        public RelativeLayout lnlFavorite;
        public ImageView icFavorite;
        public final View mView;
        public ClientList mItem;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            ClientName = itemView.findViewById(R.id.client_name);
            mno = itemView.findViewById(R.id.client_mno);
            ClientPrice = itemView.findViewById(R.id.client_price);
            ClientHours = itemView.findViewById(R.id.client_hours);
            lnlFavorite = itemView.findViewById(R.id.lnl_favorite);
            icFavorite = itemView.findViewById(R.id.ic_favorite);

        }
        @Override
        public void onClick(View view) {
            Toast.makeText(context, "Clicked Item", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnClientClickListener{
        void onClientClickListener(ClientList c);
        void onClientFavoriteClick(ClientList c);
    }
}
