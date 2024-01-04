package com.profitz.app.promos.interfaces;



public interface HttpResponse {
    void onSuccess(String response);
    void onError(String error);
}
