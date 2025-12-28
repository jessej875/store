package com.store.controller;

import com.store.model.Product;
import com.store.model.Store;
import com.store.service.ApiService;
import javafx.application.Platform;
import java.util.List;

public class ControllerImpl implements Controller {
    private final Store store;
    private final ApiService apiService;
    private String currentView;

    public ControllerImpl(Store store, ApiService apiService) {
        this.store = store;
        this.apiService = apiService;
        this.currentView = "products";
    }

    @Override
    public void loadProducts() {
        store.setLoading(true);
        store.setErrorMessage(null);

        new Thread(() -> {
            try {
                List<Product> products = apiService.fetchProducts();
                Platform.runLater(() -> {
                    store.setProducts(products);
                    store.setLoading(false);
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    store.setErrorMessage("Failed to load products: " + e.getMessage());
                    store.setLoading(false);
                });
            }
        }).start();
    }

    @Override
    public void addToCart(Product product) {
        store.addToCart(product);
    }

    @Override
    public void removeFromCart(Product product) {
        store.removeFromCart(product);
    }

    @Override
    public void updateCartQuantity(Product product, int quantity) {
        store.updateCartQuantity(product, quantity);
    }

    @Override
    public void clearCart() {
        store.clearCart();
    }

    @Override
    public void showProductList() {
        currentView = "products";
        store.notifyObservers();
    }

    @Override
    public void showCart() {
        currentView = "cart";
        store.notifyObservers();
    }

    public String getCurrentView() {
        return currentView;
    }
}