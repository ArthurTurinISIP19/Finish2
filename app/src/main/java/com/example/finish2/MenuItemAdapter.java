package com.example.finish2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<MenuItem> menuItems;

    MenuItemAdapter(Context context, List<MenuItem> menuItems) {
        this.menuItems = menuItems;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public  ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.menu_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);

        Picasso.get().load(menuItem.getIconResource()).into(holder.iconView);
        holder.nameView.setText(menuItem.getName());
        holder.priceView.setText(String.valueOf(menuItem.getPrice()));
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView iconView;
        final TextView nameView, priceView;
        ViewHolder(View view){
            super(view);
            iconView = view.findViewById(R.id.dish_icon);
            nameView = view.findViewById(R.id.name);
            priceView = view.findViewById(R.id.price);
        }
    }
}
