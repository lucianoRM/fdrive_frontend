package com.example.margonari.fdrive;

/**
 * Created by luciano on 10/11/15.
 */
public class NetworkCallbackClass {

    NetworkCallback callback;

    public NetworkCallbackClass(NetworkCallback callback) {
        this.callback = callback;
    }

    public void networkMethod() {
        String message = "This came from YourCallbackClass";
        callback.onUploadFileSuccess(message);
    }

    public interface NetworkCallback {
        public void onUploadFileSuccess(String message);
    }



}
