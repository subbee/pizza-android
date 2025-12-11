package hu.pizzapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private EditText nameInput, emailInput, passwordInput;
    private Button registerButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        nameInput = view.findViewById(R.id.nameInput);
        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        registerButton = view.findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> registerUser());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // A fő Activity UI elemeinek elrejtése, amíg ez a fragment látható
        setMainActivityUiVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // A fő Activity UI elemeinek újra láthatóvá tétele, amikor ez a fragment eltűnik
        setMainActivityUiVisibility(View.VISIBLE);
    }

    private void setMainActivityUiVisibility(int visibility) {
        View header = requireActivity().findViewById(R.id.header_image);
        View pizzasBtn = requireActivity().findViewById(R.id.pizzas_button);
        View loginBtn = requireActivity().findViewById(R.id.login_button);
        View registerBtn = requireActivity().findViewById(R.id.register_button);

        if (header != null) header.setVisibility(visibility);
        if (pizzasBtn != null) pizzasBtn.setVisibility(visibility);
        if (loginBtn != null) loginBtn.setVisibility(visibility);
        if (registerBtn != null) registerBtn.setVisibility(visibility);
    }

    private void registerUser() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Minden mező kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(name, email, password);

        PizzaApi api = ApiClient.getClient().create(PizzaApi.class);
        Call<AuthResponse> call = api.registerUser(request); // Helyes metódusnév: 'register'

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Sikeres regisztráció! Kérlek, jelentkezz be.", Toast.LENGTH_LONG).show();

                    // HELYES MEGOLDÁS: Csak csukjuk be ezt a fragmentet
                    getParentFragmentManager().popBackStack();

                } else {
                    Toast.makeText(getContext(), "Hiba: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Hálózati hiba!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
