package com.store.view;

import com.store.controller.Controller;
import com.store.model.Product;
import com.store.model.Store;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductListView implements FXComponent {
    private final Controller controller;
    private final Store store;

    public ProductListView(Controller controller, Store store) {
        this.controller = controller;
        this.store = store;
    }

    @Override
    public Parent render() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        HBox header = createHeader();
        root.setTop(header);

        if (store.isLoading()) {
            Label loadingLabel = new Label("Loading products...");
            loadingLabel.getStyleClass().add("loading-label");
            VBox loadingBox = new VBox(loadingLabel);
            loadingBox.setAlignment(Pos.CENTER);
            root.setCenter(loadingBox);
        } else if (store.getErrorMessage() != null) {
            Label errorLabel = new Label(store.getErrorMessage());
            errorLabel.getStyleClass().add("error-label");
            Button retryButton = new Button("Retry");
            retryButton.getStyleClass().add("retry-button");
            retryButton.setOnAction(e -> controller.loadProducts());
            VBox errorBox = new VBox(15, errorLabel, retryButton);
            errorBox.setAlignment(Pos.CENTER);
            root.setCenter(errorBox);
        } else {
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("scroll-pane");

            FlowPane productGrid = new FlowPane();
            productGrid.getStyleClass().add("product-grid");
            productGrid.setHgap(20);
            productGrid.setVgap(20);
            productGrid.setPadding(new Insets(20));

            for (Product product : store.getProducts()) {
                VBox productCard = createProductCard(product);
                productGrid.getChildren().add(productCard);
            }

            scrollPane.setContent(productGrid);
            root.setCenter(scrollPane);
        }

        return root;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setSpacing(20);

        Label titleLabel = new Label("Simple Store");
        titleLabel.getStyleClass().add("header-title");

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        Label cartCountLabel = new Label("Cart (" + store.getCart().getTotalItems() + ")");
        cartCountLabel.getStyleClass().add("cart-count");

        Button viewCartButton = new Button("View Cart");
        viewCartButton.getStyleClass().add("view-cart-button");
        viewCartButton.setOnAction(e -> controller.showCart());

        header.getChildren().addAll(titleLabel, spacer, cartCountLabel, viewCartButton);
        return header;
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox();
        card.getStyleClass().add("product-card");
        card.setAlignment(Pos.TOP_CENTER);
        card.setSpacing(10);
        card.setPrefWidth(220);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        try {
            Image image = new Image(product.getImage(), true);
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setImage(null);
        }

        Label titleLabel = new Label(product.getTitle());
        titleLabel.getStyleClass().add("product-title");
        titleLabel.setWrapText(true);
        titleLabel.setMaxWidth(200);

        Label categoryLabel = new Label(product.getCategory());
        categoryLabel.getStyleClass().add("product-category");

        Label priceLabel = new Label(String.format("$%.2f", product.getPrice()));
        priceLabel.getStyleClass().add("product-price");

        Button addButton = new Button("Add to Cart");
        addButton.getStyleClass().add("add-button");
        addButton.setOnAction(e -> controller.addToCart(product));

        card.getChildren().addAll(imageView, titleLabel, categoryLabel, priceLabel, addButton);
        return card;
    }
}