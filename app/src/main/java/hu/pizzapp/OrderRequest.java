package hu.pizzapp;

import android.service.notification.StatusBarNotification;

public class OrderRequest {
    private int pizza_id;
    private int user_id;
    private int darab;
    private String cim;

    public OrderRequest(int pizza_id, int user_id, int darab, String cim) {
        this.pizza_id = pizza_id;
        this.user_id = user_id;
        this.darab = darab;
        this.cim = cim;
    }
}
