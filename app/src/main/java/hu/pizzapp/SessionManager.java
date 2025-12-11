package hu.pizzapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "PizzAppSession";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_ID = "user_id";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(AuthResponse authResponse) {
        editor.putString(KEY_AUTH_TOKEN, authResponse.getToken());
        User user = authResponse.getUser();
        if (user != null) {
            editor.putInt(KEY_USER_ID, user.getId());
            editor.putString(KEY_USER_NAME, user.getName());
            editor.putString(KEY_USER_EMAIL, user.getEmail());
        }
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(KEY_AUTH_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void logout() {
        editor.clear();
        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }
}
