package hu.pizzapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

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

        sessionManager = new SessionManager(this);

        findViewById(R.id.pizzas_button).setOnClickListener(v -> loadFragment(new PizzaListFragment(), false));
        findViewById(R.id.login_button).setOnClickListener(v -> loadFragment(new LoginFragment(), true));
        findViewById(R.id.register_button).setOnClickListener(v -> loadFragment(new RegisterFragment(), true));
        findViewById(R.id.logout_button).setOnClickListener(v -> {
            sessionManager.logout();
            updateUI();
        });

        if (savedInstanceState == null) {
            loadFragment(new PizzaListFragment(), false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI() {
        // Először is, állítsuk vissza a közös elemek láthatóságát
        findViewById(R.id.header_image).setVisibility(View.VISIBLE);
        findViewById(R.id.pizzas_button).setVisibility(View.VISIBLE);

        // UI elemek megkeresése
        Button loginButton = findViewById(R.id.login_button);
        Button registerButton = findViewById(R.id.register_button);
        TextView welcomeText = findViewById(R.id.welcome_text);
        Button logoutButton = findViewById(R.id.logout_button);

        // Majd az állapotnak megfelelően állítsuk be a többit
        if (sessionManager.isLoggedIn()) {
            // Bejelentkezett állapot
            loginButton.setVisibility(View.GONE);
            registerButton.setVisibility(View.GONE);

            welcomeText.setVisibility(View.VISIBLE);
            welcomeText.setText("Szia, " + sessionManager.getUserName() + "!");
            logoutButton.setVisibility(View.VISIBLE);
        } else {
            // Kijelentkezett állapot
            loginButton.setVisibility(View.VISIBLE);
            registerButton.setVisibility(View.VISIBLE);

            welcomeText.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
        }
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}
