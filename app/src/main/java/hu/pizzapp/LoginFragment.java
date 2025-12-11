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

public class LoginFragment extends Fragment {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private SessionManager sessionManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);

        sessionManager = new SessionManager(requireContext());

        emailInput = view.findViewById(R.id.emailInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> loginUser());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Amikor ez a fragment megjelenik, elrejtjük a főmenü gombjait.
        setMainActivityUiVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // A gombok visszakapcsolását már az Activity végzi az onResume-ban, itt nincs rá szükség.
        // setMainActivityUiVisibility(View.VISIBLE); // EZ A SOR OKOZTA A HIBÁT!
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

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getContext(), "Minden mező kötelező!", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        PizzaApi api = ApiClient.getClient().create(PizzaApi.class);
        Call<AuthResponse> call = api.loginUser(request);

        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sessionManager.saveSession(response.body());

                    Toast.makeText(getContext(), "Sikeres bejelentkezés!", Toast.LENGTH_SHORT).show();

                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).updateUI();
                    }

                    getParentFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Hibás email vagy jelszó!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Hálózati hiba!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
