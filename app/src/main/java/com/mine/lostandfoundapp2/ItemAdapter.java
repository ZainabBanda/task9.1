package com.mine.lostandfoundapp2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A simple RecyclerView.Adapter for our Item objects.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private final Context context;
    private List<Item>    itemList;

    public ItemAdapter(Context ctx, List<Item> items) {
        this.context  = ctx;
        this.itemList = items;
    }

    /**
     * Replace the list and refresh.
     */
    public void setItemList(List<Item> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(
        @NonNull ViewGroup parent, int viewType
    ) {
        View v = LayoutInflater.from(context)
            .inflate(R.layout.item_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
        @NonNull ViewHolder holder, int position
    ) {
        Item it = itemList.get(position);
        holder.tvName.setText(it.getName());
        holder.tvDesc.setText(it.getDescription());

        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, DetailActivity.class);
            i.putExtra("item_id", it.getId());
            // if you ever want result:
            // ((Activity)context).startActivityForResult(i, MainActivity.REQUEST_DETAIL);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvName, tvDesc;
        ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.item_name);
            tvDesc = itemView.findViewById(R.id.item_description);
        }
    }
}
