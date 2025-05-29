package com.subh.recorder.Services;

import okhttp3.*;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.FileEntity;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class SupabaseStorageService {

    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImhucmVseWJ4eHV5aG5vc3RuY3RoIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDcyOTM2OTMsImV4cCI6MjA2Mjg2OTY5M30.b9HnyTIu5ToygxnybrtiEyMV3gJP4rXYu0dBFQ2NFXU";
    private static final String BUCKET_NAME = "recordertest";

    public String uploadVideo(File videoFile, String fileName) throws Exception {
        String baseUrl = "https://hnrelybxxuyhnostncth.supabase.co";
        String uploadUrl = baseUrl + "/storage/v1/object/" + BUCKET_NAME + "/" + fileName;

        OkHttpClient client = new OkHttpClient();

        RequestBody fileBody = RequestBody.create(videoFile, MediaType.parse("video/mp4"));

        Request request = new Request.Builder()
                .url(uploadUrl)
                .post(fileBody)
                .addHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY)
                .addHeader("Content-Type", "video/mp4")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("Response Code: " + response.code());
            System.out.println("Response Message: " + response.message());

            String responseBody = "";
            if (response.body() != null) {
                responseBody = response.body().string();
                System.out.println("Response Body: " + responseBody);
            }

            if (response.isSuccessful()) {
                return baseUrl + "/storage/v1/object/public/" + BUCKET_NAME + "/" + fileName;
            } else {
                throw new Exception("Upload failed [" + response.code() + "]: " + response.message() + " - " + responseBody);
            }
        }
    }
}

