package hu.pizzapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

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

        // Példa a bejelentkezés használatára
        // loginUser("teszt@teszt.com", "password");

        // Pizzák lekérdezésének tesztelése
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
                    for (Pizza pizza : response.body()) {
                        Log.d(TAG, "Pizza: " + pizza.getNev());
                    }
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

    private void loginUser(String email, String password) {
        PizzaApi pizzaApi = ApiClient.getClient().create(PizzaApi.class);
        LoginRequest loginRequest = new LoginRequest(email, password);

        Call<AuthResponse> call = pizzaApi.login(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Sikeres bejelentkezés! Token: " + response.body().getToken());
                    // Itt lehetne menteni a tokent és tovább navigálni a felhasználót
                } else {
                    Log.e(TAG, "Sikertelen bejelentkezés: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e(TAG, "Hiba a hálózati kérés során", t);
            }
        });
    }
}
