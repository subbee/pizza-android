package hu.pizzapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {

    private SessionManager sessionManager;

    private TextView pizzaNameText, pizzaPriceText, loginWarningText;
    private TextInputEditText quantityInput, addressInput;
    private Button submitOrderButton;
    private View orderFormGroup;

    private int pizzaId;
    private String pizzaName;
    private String pizzaPrice;

    public static OrderFragment newInstance(int pizzaId, String pizzaName, String pizzaPrice) {
        OrderFragment fragment = new OrderFragment();
        Bundle args = new Bundle();
        args.putInt("PIZZA_ID", pizzaId);
        args.putString("PIZZA_NAME", pizzaName);
        args.putString("PIZZA_PRICE", pizzaPrice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pizzaId = getArguments().getInt("PIZZA_ID");
            pizzaName = getArguments().getString("PIZZA_NAME");
            pizzaPrice = getArguments().getString("PIZZA_PRICE");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        sessionManager = new SessionManager(requireContext());

        loginWarningText = view.findViewById(R.id.login_warning_text);
        orderFormGroup = view.findViewById(R.id.order_form_group);
        pizzaNameText = view.findViewById(R.id.pizza_name_order);
        pizzaPriceText = view.findViewById(R.id.pizza_price_order);
        quantityInput = view.findViewById(R.id.quantity_input);
        addressInput = view.findViewById(R.id.address_input);
        submitOrderButton = view.findViewById(R.id.submit_order_button);

        if (sessionManager.isLoggedIn()) {
            loginWarningText.setVisibility(View.GONE);
            orderFormGroup.setVisibility(View.VISIBLE);
            pizzaNameText.setText(pizzaName);
            pizzaPriceText.setText(pizzaPrice);

            submitOrderButton.setOnClickListener(v -> submitOrder());

        } else {
            loginWarningText.setVisibility(View.VISIBLE);
            orderFormGroup.setVisibility(View.GONE);
        }

        return view;
    }

    private void submitOrder() {
        String quantityStr = quantityInput.getText().toString();
        String address = addressInput.getText().toString().trim();
        int userId = sessionManager.getUserId(); // Lekérdezzük a felhasználó ID-ját

        if (quantityStr.isEmpty() || address.isEmpty()) {
            Toast.makeText(getContext(), "Minden mező kitöltése kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (userId == -1) { // Biztonsági ellenőrzés
            Toast.makeText(getContext(), "Hiba: Felhasználói azonosító nem található!", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);

        // Itt adjuk át a lekérdezett userId-t a konstruktornak
        OrderRequest orderRequest = new OrderRequest(pizzaId, userId, quantity, address);
        String token = "Bearer " + sessionManager.getToken();

        PizzaApi api = ApiClient.getClient().create(PizzaApi.class);
        Call<Object> call = api.createOrder(token, orderRequest);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Rendelés sikeresen leadva!", Toast.LENGTH_LONG).show();
                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Hiba a rendelés során: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Toast.makeText(getContext(), "Hálózati hiba!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
