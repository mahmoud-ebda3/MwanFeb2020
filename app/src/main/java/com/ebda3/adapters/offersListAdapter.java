package com.ebda3.adapters;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebda3.mwan.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ebda3.Helpers.Config.imageupload;

public class offersListAdapter extends RecyclerView.Adapter<offersListAdapter.OffersListViewHolder> {

    private final Activity context;
    private final ArrayList<String> OfferName;
    private final ArrayList<String> OfferPhoto;
    private final ArrayList<String> offerItems;


    public offersListAdapter(Activity context, ArrayList<String> OfferName, ArrayList<String> OfferPhoto, ArrayList<String> offerItems) {

        this.context = context;
        this.OfferName = OfferName;
        this.OfferPhoto = OfferPhoto;
        this.offerItems = offerItems;

    }

    @NonNull
    @Override
    public OffersListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rowView = inflater.inflate(R.layout.offer_item, parent, false);
        return new OffersListViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersListViewHolder holder, int position) {
        String name = OfferName.get(position);
        String image = OfferPhoto.get(position);
        holder.dataBinding(name, image);
    }

    @Override
    public int getItemCount() {
        if (offerItems != null) {
            return offerItems.size();
        } else {
            return 0;
        }
    }

    public class OffersListViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView offer_name;

        public OffersListViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.offer_image);
            offer_name = itemView.findViewById(R.id.offer_name);
        }

        private void dataBinding(String name, String photo) {
            offer_name.setText(name);
            Picasso.get().load(imageupload + photo)
                    .resize(626, 250)
                    .centerCrop()
                    .error(R.drawable.image_not_found)
                    .into(imageView);
        }
    }
}