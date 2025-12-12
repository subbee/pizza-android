package hu.pizzapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    public interface OnPizzaOrderListener {
        void onPizzaOrder(Pizza pizza);
    }

    private final List<Pizza> pizzaList;
    private final OnPizzaOrderListener orderListener;

    public PizzaAdapter(List<Pizza> pizzaList, OnPizzaOrderListener orderListener) {
        this.pizzaList = pizzaList;
        this.orderListener = orderListener;
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

        Glide.with(holder.itemView.getContext())
                .load(pizza.getImageUrl())
                .into(holder.pizzaImage);

        holder.orderButton.setOnClickListener(v -> {
            if (orderListener != null) {
                orderListener.onPizzaOrder(pizza);
            }
        });
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
        Button orderButton;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaImage = itemView.findViewById(R.id.pizza_image);
            pizzaName = itemView.findViewById(R.id.pizza_name);
            price = itemView.findViewById(R.id.price);
            vegetarianus = itemView.findViewById(R.id.vegetarianus);
            orderButton = itemView.findViewById(R.id.order_btn);
        }
    }
}
