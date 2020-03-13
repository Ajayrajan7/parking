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

import com.example.parkingfinderprojectfinal.PlacesListActivity;
import com.example.parkingfinderprojectfinal.R;
import com.model.Place;

import java.util.ArrayList;
import java.util.List;

//Adapter class for workshop list
public class PlaceWSAdapter  extends RecyclerView.Adapter<PlaceWSAdapter.ItemHolder>{
    private List<Place> mPlaces = new ArrayList<>();
    private Context context;
    private final OnPlaceWSClickListener mListener;

    public PlaceWSAdapter(Context context){
        this.context = context;
        try {
            this.mListener = ((OnPlaceWSClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnPlaceClickListener.");
        }
        PlacesListActivity p = new PlacesListActivity();
        String[] placeNames = p.name;

        String[] placeDelivery =p.locs;

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
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_card, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
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
                    mListener.onPlaceWSFavoriteClick(place);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onPlaceWSClickListener(place);
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

    public interface OnPlaceWSClickListener {
        void onPlaceWSClickListener(Place place);
        void onPlaceWSFavoriteClick(Place place);
    }

    @Override
    public int getItemCount() {
        return mPlaces.size();
    }


}
