package hu.pizzapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PizzaApi {

    @POST("register")
    Call<AuthResponse> register(@Body RegisterRequest registerRequest);

    @POST("login")
    Call<AuthResponse> login(@Body LoginRequest loginRequest);

    @GET("pizzak")
    Call<List<Pizza>> getPizzak();
}
