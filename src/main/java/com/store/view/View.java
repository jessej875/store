package com.store.view;

import com.store.controller.Controller;
import com.store.controller.ControllerImpl;
import com.store.model.Observer;
import com.store.model.Store;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class View implements FXComponent, Observer {
    private final Controller controller;
    private final Store store;
    private final Stage stage;

    public View(Controller controller, Store store, Stage stage) {
        this.controller = controller;
        this.store = store;
        this.stage = stage;
    }

    @Override
    public Parent render() {
        String currentView = ((ControllerImpl) controller).getCurrentView();

        if ("cart".equals(currentView)) {
            CartView cartView = new CartView(controller, store);
            return cartView.render();
        } else {
            ProductListView productListView = new ProductListView(controller, store);
            return productListView.render();
        }
    }

    @Override
    public void update() {
        Scene scene = new Scene(render(), 1000, 700);
        scene.getStylesheets().add(getClass().getResource("/store.css").toExternalForm());
        stage.setScene(scene);
    }
}