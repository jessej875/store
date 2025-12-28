package com.store.controller;

import com.store.model.Product;

public interface Controller {
    void loadProducts();
    void addToCart(Product product);
    void removeFromCart(Product product);
    void updateCartQuantity(Product product, int quantity);
    void clearCart();
    void showProductList();
    void showCart();
}