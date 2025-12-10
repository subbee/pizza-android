package hu.pizzapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private EditText nameInput, emailInput, passwordInput;
    private Button registerButton;

    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register, container, false);

        nameInput = view.findViewById(R.id.nameInput);
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> registerUser());

        return view;
    }

    private void registerUser() {

        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Minden mezőt ki kell tölteni!", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(name, email, password);

        PizzaApi api = ApiClient.getClient().create(PizzaApi.class);
        Call<AuthResponse> call = api.register(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Sikeres regisztráció!", Toast.LENGTH_SHORT).show();

                    // visszanavigálás loginre
                    requireActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentContainer, new LoginFragment())
                            .commit();

                } else {
                    Toast.makeText(getContext(), "Hiba: email már foglalt?", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Hiba történt: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
