package com.store.view;

import com.store.controller.Controller;
import com.store.model.CartItem;
import com.store.model.Store;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CartView implements FXComponent {
    private final Controller controller;
    private final Store store;

    public CartView(Controller controller, Store store) {
        this.controller = controller;
        this.store = store;
    }

    @Override
    public Parent render() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("root");

        HBox header = createHeader();
        root.setTop(header);

        if (store.getCart().getItems().isEmpty()) {
            Label emptyLabel = new Label("Your cart is empty");
            emptyLabel.getStyleClass().add("empty-cart-label");

            Button continueButton = new Button("Continue Shopping");
            continueButton.getStyleClass().add("continue-button");
            continueButton.setOnAction(e -> controller.showProductList());

            VBox emptyBox = new VBox(20, emptyLabel, continueButton);
            emptyBox.setAlignment(Pos.CENTER);
            root.setCenter(emptyBox);
        } else {
            VBox content = new VBox();
            content.setSpacing(20);
            content.setPadding(new Insets(20));

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setFitToWidth(true);
            scrollPane.getStyleClass().add("scroll-pane");

            VBox cartItems = new VBox();
            cartItems.setSpacing(15);

            for (CartItem item : store.getCart().getItems()) {
                HBox cartItemBox = createCartItemBox(item);
                cartItems.getChildren().add(cartItemBox);
            }

            scrollPane.setContent(cartItems);
            VBox.setVgrow(scrollPane, javafx.scene.layout.Priority.ALWAYS);

            VBox totalSection = createTotalSection();

            content.getChildren().addAll(scrollPane, totalSection);
            root.setCenter(content);
        }

        return root;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.getStyleClass().add("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setSpacing(20);

        Button backButton = new Button("Back to Products");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(e -> controller.showProductList());

        Label titleLabel = new Label("Shopping Cart");
        titleLabel.getStyleClass().add("header-title");

        header.getChildren().addAll(backButton, titleLabel);
        return header;
    }

    private HBox createCartItemBox(CartItem item) {
        HBox box = new HBox();
        box.getStyleClass().add("cart-item-box");
        box.setAlignment(Pos.CENTER_LEFT);
        box.setSpacing(15);
        box.setPadding(new Insets(10));

        ImageView imageView = new ImageView();
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);

        try {
            Image image = new Image(item.getProduct().getImage(), true);
            imageView.setImage(image);
        } catch (Exception e) {
            imageView.setImage(null);
        }

        VBox detailsBox = new VBox();
        detailsBox.setSpacing(5);
        HBox.setHgrow(detailsBox, javafx.scene.layout.Priority.ALWAYS);

        Label titleLabel = new Label(item.getProduct().getTitle());
        titleLabel.getStyleClass().add("cart-item-title");

        Label priceLabel = new Label(String.format("$%.2f each", item.getProduct().getPrice()));
        priceLabel.getStyleClass().add("cart-item-price");

        detailsBox.getChildren().addAll(titleLabel, priceLabel);

        HBox quantityBox = new HBox();
        quantityBox.setSpacing(10);
        quantityBox.setAlignment(Pos.CENTER);

        Label qtyLabel = new Label("Qty:");
        qtyLabel.getStyleClass().add("qty-label");

        Spinner<Integer> quantitySpinner = new Spinner<>();
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 99, item.getQuantity());
        quantitySpinner.setValueFactory(valueFactory);
        quantitySpinner.setPrefWidth(70);
        quantitySpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            controller.updateCartQuantity(item.getProduct(), newVal);
        });

        Label totalLabel = new Label(String.format("$%.2f", item.getTotalPrice()));
        totalLabel.getStyleClass().add("cart-item-total");
        totalLabel.setMinWidth(80);
        totalLabel.setAlignment(Pos.CENTER_RIGHT);

        Button removeButton = new Button("Remove");
        removeButton.getStyleClass().add("remove-button");
        removeButton.setOnAction(e -> controller.removeFromCart(item.getProduct()));

        quantityBox.getChildren().addAll(qtyLabel, quantitySpinner);

        box.getChildren().addAll(imageView, detailsBox, quantityBox, totalLabel, removeButton);
        return box;
    }

    private VBox createTotalSection() {
        VBox totalSection = new VBox();
        totalSection.getStyleClass().add("total-section");
        totalSection.setSpacing(15);
        totalSection.setPadding(new Insets(20));
        totalSection.setAlignment(Pos.CENTER_RIGHT);

        HBox totalBox = new HBox();
        totalBox.setSpacing(20);
        totalBox.setAlignment(Pos.CENTER_RIGHT);

        Label totalLabel = new Label("Total:");
        totalLabel.getStyleClass().add("total-label");

        Label totalAmount = new Label(String.format("$%.2f", store.getCart().getTotalPrice()));
        totalAmount.getStyleClass().add("total-amount");

        totalBox.getChildren().addAll(totalLabel, totalAmount);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);

        Button clearButton = new Button("Clear Cart");
        clearButton.getStyleClass().add("clear-button");
        clearButton.setOnAction(e -> controller.clearCart());

        Button checkoutButton = new Button("Checkout");
        checkoutButton.getStyleClass().add("checkout-button");
        checkoutButton.setOnAction(e -> {
            System.out.println("Checkout clicked!");
        });

        buttonBox.getChildren().addAll(clearButton, checkoutButton);

        totalSection.getChildren().addAll(totalBox, buttonBox);
        return totalSection;
    }
}