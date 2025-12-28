package com.store.model;

import java.util.ArrayList;
import java.util.List;

public class Store implements Subject {
    private final List<Product> products;
    private final Cart cart;
    private final List<Observer> observers;
    private boolean isLoading;
    private String errorMessage;

    public Store() {
        this.products = new ArrayList<>();
        this.cart = new Cart();
        this.observers = new ArrayList<>();
        this.isLoading = false;
        this.errorMessage = null;
    }

    public void setProducts(List<Product> products) {
        this.products.clear();
        this.products.addAll(products);
        notifyObservers();
    }

    public List<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public Cart getCart() {
        return cart;
    }

    public void addToCart(Product product) {
        cart.addProduct(product);
        notifyObservers();
    }

    public void removeFromCart(Product product) {
        cart.removeProduct(product);
        notifyObservers();
    }

    public void updateCartQuantity(Product product, int quantity) {
        cart.updateQuantity(product, quantity);
        notifyObservers();
    }

    public void clearCart() {
        cart.clear();
        notifyObservers();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
        notifyObservers();
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}