package com.store.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.store.model.Product;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ApiServiceImpl implements ApiService {
    private static final String API_URL = "https://dummyjson.com/products?limit=20";

    @Override
    public List<Product> fetchProducts() {
        List<Product> products = new ArrayList<>();

        try {
            URL url = new URL(API_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();

                JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                JsonArray productsArray = jsonObject.getAsJsonArray("products");

                for (JsonElement element : productsArray) {
                    JsonObject productJson = element.getAsJsonObject();

                    int id = productJson.get("id").getAsInt();
                    String title = productJson.get("title").getAsString();
                    String description = productJson.get("description").getAsString();
                    double price = productJson.get("price").getAsDouble();
                    String image = productJson.get("thumbnail").getAsString();
                    String category = productJson.get("category").getAsString();

                    products.add(new Product(id, title, description, price, image, category));
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching products: " + e.getMessage());
            e.printStackTrace();
        }

        return products;
    }
}