package com.github.ttdyce.model.api;

public abstract class ResponseCallback {
    public abstract void onReponse(String response);
    public abstract void onError(String error);
}
