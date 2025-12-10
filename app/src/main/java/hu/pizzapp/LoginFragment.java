package hu.pizzapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private Button loginButton;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // XML elemek bekötése
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        Button registerButton = view.findViewById(R.id.registerButton);

        // Bejelentkezés gomb
        loginButton.setOnClickListener(v -> loginUser());

        // Regisztráció gomb → RegisterFragment
        registerButton.setOnClickListener(v -> {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new RegisterFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    private void loginUser() {

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Minden mezőt tölts ki!", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, password);

        PizzaApi api = ApiClient.getClient().create(PizzaApi.class);
        Call<AuthResponse> call = api.login(loginRequest);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {

                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Hibás bejelentkezési adatok!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Hiba történt: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
