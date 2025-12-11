package hu.pizzapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PizzaListFragment extends Fragment {

    private RecyclerView pizzaRecyclerView;
    private PizzaAdapter pizzaAdapter;
    private SearchView searchView;

    private List<Pizza> displayedPizzaList = new ArrayList<>();
    private List<Pizza> fullPizzaList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pizza_list, container, false);

        pizzaRecyclerView = view.findViewById(R.id.pizza_recycler_view);
        searchView = view.findViewById(R.id.search_view);

        setupRecyclerView();
        setupSearchView();

        getPizzas();

        return view;
    }

    private void setupRecyclerView() {
        pizzaAdapter = new PizzaAdapter(displayedPizzaList);
        pizzaRecyclerView.setAdapter(pizzaAdapter);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // A szűrés már gépelés közben megtörténik, itt nincs teendő
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPizzas(newText);
                return true;
            }
        });
    }

    private void getPizzas() {
        PizzaApi pizzaApi = ApiClient.getClient().create(PizzaApi.class);
        Call<List<Pizza>> call = pizzaApi.getPizzak();

        call.enqueue(new Callback<List<Pizza>>() {
            @Override
            public void onResponse(Call<List<Pizza>> call, Response<List<Pizza>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullPizzaList.clear();
                    fullPizzaList.addAll(response.body());

                    // Kezdetben a teljes listát mutatjuk
                    filterPizzas("");
                } else {
                    Toast.makeText(getContext(), "Pizza lista lekérdezése sikertelen!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Pizza>> call, Throwable t) {
                Toast.makeText(getContext(), "Hálózati hiba!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterPizzas(String text) {
        List<Pizza> filteredList = new ArrayList<>();

        // A teljes listából szűrünk
        for (Pizza pizza : fullPizzaList) {
            // Kis- és nagybetű érzéketlen keresés
            if (pizza.getNev().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(pizza);
            }
        }

        // Frissítjük a megjelenített listát és értesítjük az adaptert
        displayedPizzaList.clear();
        displayedPizzaList.addAll(filteredList);
        pizzaAdapter.notifyDataSetChanged();
    }
}
