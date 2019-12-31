package com.ebda3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ebda3.Model.OptionObject;
import com.ebda3.mwan.R;

import java.util.List;

public class OptionsItemAdapter extends RecyclerView.Adapter<OptionsItemAdapter.OptionsItemViewHolder> {

    private Context context;
    private List<OptionObject> objectList;
    private RecyclerViewItemOnClickListener listener;


    public OptionsItemAdapter(Context context, List<OptionObject> objectList, RecyclerViewItemOnClickListener listener) {
        this.context = context;
        this.objectList = objectList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OptionsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View rootView = inflater.inflate(R.layout.options_list_item, parent, false);
        return new OptionsItemViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull OptionsItemViewHolder holder, int position) {
        OptionObject object = objectList.get(position);
        holder.dataBinding(object);
    }

    @Override
    public int getItemCount() {
        if (objectList != null) {
            return objectList.size();
        } else {
            return 0;
        }
    }


    public class OptionsItemViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        LinearLayout container;

        public OptionsItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.option_name);
            icon = itemView.findViewById(R.id.options_icon);
            container = itemView.findViewById(R.id.container_view);
        }

        private void dataBinding(OptionObject object) {
            name.setText(object.getName());
            icon.setImageResource(object.getImage());
            container.setOnClickListener(click -> {
                listener.onItemClick(object);
            });
        }
    }

    public interface RecyclerViewItemOnClickListener {
        void onItemClick(OptionObject object);
    }
}
