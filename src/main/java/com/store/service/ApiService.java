package com.store.service;

import com.store.model.Product;
import java.util.List;

public interface ApiService {
    List<Product> fetchProducts();
}