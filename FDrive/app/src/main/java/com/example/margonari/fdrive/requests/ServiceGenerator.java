package com.example.margonari.fdrive.requests;

import com.squareup.okhttp.OkHttpClient;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by luciano on 16/10/15.
 */
public class ServiceGenerator {

    // No need to instantiate this class.
    private ServiceGenerator() {
    }

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {


        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(baseUrl)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();

        return adapter.create(serviceClass);
    }
}
