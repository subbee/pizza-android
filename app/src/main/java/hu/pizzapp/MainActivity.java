package hu.pizzapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private RecyclerView pizzaRecyclerView;
    private PizzaAdapter pizzaAdapter;
    private List<Pizza> pizzaList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pizzaRecyclerView = findViewById(R.id.pizza_recycler_view);
        pizzaAdapter = new PizzaAdapter(pizzaList);
        pizzaRecyclerView.setAdapter(pizzaAdapter);

        getPizzas();
    }

    private void getPizzas() {
        PizzaApi pizzaApi = ApiClient.getClient().create(PizzaApi.class);
        Call<List<Pizza>> call = pizzaApi.getPizzak();

        call.enqueue(new Callback<List<Pizza>>() {
            @Override
            public void onResponse(Call<List<Pizza>> call, Response<List<Pizza>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Sikeres pizza lekérdezés!");
                    pizzaList.clear();
                    pizzaList.addAll(response.body());
                    pizzaAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Sikertelen pizza lekérdezés: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Pizza>> call, Throwable t) {
                Log.e(TAG, "Hiba a hálózati kérés során", t);
            }
        });
    }
}
