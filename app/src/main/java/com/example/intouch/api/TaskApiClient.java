package com.example.intouch.api;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import java.io.IOException;

import okhttp3.*;

public class TaskApiClient {

    private static final String BASE_URL = "http://10.0.2.2:8080/api/tasks";
    private OkHttpClient client = new OkHttpClient();

    public interface TaskApiCallback {
        void onTaskApiResponse(String response);
    }


    public void getAllTasks(TaskApiCallback callback) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    Request request = new Request.Builder()
                            .url(BASE_URL)
                            .build();

                    try (Response response = client.newCall(request).execute()) {
                        return response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String response) {
                if (callback != null) {
                    callback.onTaskApiResponse(response);
                }
            }
        }.execute();
    }

    // Implement similar methods for adding, updating, and deleting tasks
}
