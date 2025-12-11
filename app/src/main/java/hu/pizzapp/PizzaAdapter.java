package hu.pizzapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private List<Pizza> pizzaList;

    public PizzaAdapter(List<Pizza> pizzaList) {
        this.pizzaList = pizzaList;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pizza_list_item, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzaList.get(position);
        holder.pizzaName.setText(pizza.getNev());
        holder.price.setText(pizza.getAr());
        holder.vegetarianus.setText(pizza.getVegetarianus());

        Log.d("PizzaAdapter", "onBindViewHolder image: " + pizza.getImageUrl());
        Glide.with(holder.itemView.getContext())
                .load(pizza.getImageUrl())
                .into(holder.pizzaImage);
    }

    @Override
    public int getItemCount() {
        return pizzaList == null ? 0 : pizzaList.size();
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        ImageView pizzaImage;
        TextView pizzaName;
        TextView price;
        TextView vegetarianus;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.pizza_image);
            pizzaName = itemView.findViewById(R.id.pizza_name);
            price = itemView.findViewById(R.id.price);
            vegetarianus = itemView.findViewById(R.id.vegetarianus);
        }
    }

    public void updateList(List<Pizza> newList) {
        pizzaList.clear();
        pizzaList.addAll(newList);
        notifyDataSetChanged();
    }
}
