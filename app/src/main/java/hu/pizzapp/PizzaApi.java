package hu.pizzapp;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PizzaApi {

    @POST("register")
    Call<AuthResponse> registerUser(@Body RegisterRequest request);

    @POST("login")
    Call<AuthResponse> loginUser(@Body LoginRequest request);

    @GET("pizzak")
    Call<List<Pizza>> getPizzak();

    @POST("rendelesek")
    Call<Object> createOrder(@Header("Authorization") String token, @Body OrderRequest orderRequest);
}
