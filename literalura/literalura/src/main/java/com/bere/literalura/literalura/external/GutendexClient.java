package com.bere.literalura.literalura.external;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class GutendexClient {

    private static final String API_URL = "https://gutendex.com/books?search=";

    public GutendexResponse searchBookByTitle(String title) throws Exception {
        URL url = new URL(API_URL + title.replace(" ", "%20"));
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream responseStream = conn.getInputStream();
        ObjectMapper mapper = new ObjectMapper();
        GutendexResponse response = mapper.readValue(responseStream, GutendexResponse.class);

        return response;
    }
}
