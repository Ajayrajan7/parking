package com.adapter;
//Adapter for food
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

import com.example.parkingfinderprojectfinal.PlacesListActivity;
import com.example.parkingfinderprojectfinal.PlacesListActivity2;
import com.example.parkingfinderprojectfinal.R;
import com.model.Place;

import java.util.ArrayList;
import java.util.List;

public class PlaceFoodAdapter extends RecyclerView.Adapter<PlaceFoodAdapter.ItemHolder> {
    private List<Place> mPlaces = new ArrayList<>();
    private Context context;
    private final OnPlaceFoodClickListener mListener;

    public PlaceFoodAdapter(Context context){
        this.context = context;

        try {
            this.mListener = ((PlaceFoodAdapter.OnPlaceFoodClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnPlaceClickListener.");
        }
        PlacesListActivity2 p2 = new PlacesListActivity2();
        String[] placeNames = p2.name;

        String[] placeDelivery = p2.locs;

        for (int i = 0; i < placeNames.length; i++){
            Place place = new Place((i + 1),placeNames[i], "Av Paulista, " + (i + 1),
                    "4." + (i + 1), placeDelivery[i]);
            mPlaces.add(place);
        }
    }
    public void setFavorite(int placeId) {
        if(mPlaces.size() > 0) {
            for (int i = 0; i < mPlaces.size(); i++) {
                if(mPlaces.get(i).getPlaceId() == placeId) {
                    if (!mPlaces.get(i).isFavorite()) {
                        mPlaces.get(i).setFavorite(true);
                        break;
                    } else {
                        mPlaces.get(i).setFavorite(false);
                        break;
                    }
                }
            }
        }
    }
    @NonNull
    @Override
    public PlaceFoodAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_card, viewGroup, false);
        PlaceFoodAdapter.ItemHolder holder = new PlaceFoodAdapter.ItemHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final PlaceFoodAdapter.ItemHolder holder, int position) {
        final Place place =  mPlaces.get(position);

        holder.mItem = place;

        holder.placeName.setText(place.getPlaceName());
        holder.placeLocation.setText(place.getLocation());
        holder.placeRating.setText(place.getRating());
        holder.placeDelivery.setText(place.getDelivery());

        if (holder.mItem.isFavorite()) {
            holder.icFavorite.setImageResource(R.drawable.star);
        } else {
            holder.icFavorite.setImageResource(R.drawable.star2);
        }

        holder.lnlFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener  != null)
                    mListener.onPlaceFoodFavoriteClick(place);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onPlaceFoodClickListener(place);
            }
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView placeName, placeLocation, placeRating, placeDelivery;
        public RelativeLayout lnlFavorite;
        public ImageView icFavorite;
        public final View mView;
        public Place mItem;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            placeName = itemView.findViewById(R.id.place_name);
            placeLocation = itemView.findViewById(R.id.place_location);
            placeRating = itemView.findViewById(R.id.place_rating);
            placeDelivery = itemView.findViewById(R.id.place_delivery);
            lnlFavorite = itemView.findViewById(R.id.lnl_favorite);
            icFavorite = itemView.findViewById(R.id.ic_favorite);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked Item", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnPlaceFoodClickListener {
        void onPlaceFoodClickListener(Place place);
        void onPlaceFoodFavoriteClick(Place place);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }


}
