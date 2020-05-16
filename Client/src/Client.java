import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Client extends Application {
    private TextArea textArea;
    private ComboBox<String> comboBox;
    private RemoteMethods remoteMethods;
    private BorderPane root;

    private MenuBar menuBar;
    private Menu showMenu;

    private MenuItem showCustomers;
    private MenuItem showOrders;
    private MenuItem showSpecialOrders;

    private Menu addMenu;

    private MenuItem addOrder;
    private MenuItem addCustomer;

    private Menu deleteMenu;

    private MenuItem deleteOrder;
    private MenuItem deleteCustomer;
    @Override
    public void start(Stage primaryStage) throws NotBoundException {
        try {
//            Registry registry = LocateRegistry.getRegistry("93.84.140.64", 1099);
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            remoteMethods = (RemoteMethods) registry.lookup("RMI");
        }
        catch (RemoteException e) {
            System.out.println(e.toString());
        }
        root = new BorderPane();
        menuBar = new MenuBar();
        showMenu = new Menu("Show");
        menuBar.getMenus().add(showMenu);
        addMenu = new Menu("Add");
        menuBar.getMenus().add(addMenu);
        deleteMenu = new Menu("Delete");
        menuBar.getMenus().add(deleteMenu);

        showCustomers = new MenuItem("Show Customers");
        showOrders = new MenuItem("Show Orders");
        showSpecialOrders = new MenuItem("Show Special Orders");
        showMenu.getItems().addAll(showCustomers, showOrders,showSpecialOrders);

        addOrder = new MenuItem("Add Order");
        addCustomer = new MenuItem("Add Customer");
        addMenu.getItems().addAll(addCustomer,addOrder);

        deleteCustomer = new MenuItem("Delete Customer");
        deleteOrder = new MenuItem("Delete Order");
        deleteMenu.getItems().addAll(deleteCustomer,deleteOrder);


        root.setTop(menuBar);
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setMinWidth(500);
        comboBox = new ComboBox<>();
        comboBox.setEditable(false);
        comboBox.setMinWidth(500);
        root.setRight(textArea);
        root.setLeft(comboBox);
        showCustomers.setOnAction(event -> {
            List<Customer> customers = remoteMethods.showCustomers();
            ObservableList<String> customersString = FXCollections.observableArrayList();
            for(Customer i : customers) {
                customersString.add(i.toString());
            }
            comboBox.setValue("All our customers");
            comboBox.setItems(customersString);
        });

        showOrders.setOnAction(event -> {
            showOrders();
        });

        showSpecialOrders.setOnAction(event -> {
            showSpecialOrders();
        });

        deleteOrder.setOnAction(event -> {
            deleteOrder();
        });

        deleteCustomer.setOnAction(event -> {
            deleteCustomer();
        });

        addCustomer.setOnAction(event -> {
            addCustomer();
        });

        addOrder.setOnAction(event -> {
            addOrder();
        });

        primaryStage.setTitle("Atelie application");
        primaryStage.setScene(new Scene(root, 1000, 1000));
        primaryStage.show();
    }

    private void showOrders() {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Order dialog");
        dialog.setHeaderText("Tuturututu");
        ComboBox<String> nameBox = new ComboBox<>();
        ObservableList<String> names = FXCollections.observableArrayList(remoteMethods.getNamesOfCustomers());
        nameBox.setValue("Customers");
        nameBox.setItems(names);
        ButtonType okButton = new ButtonType("Find orders", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameBox, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                List<Order> orders = remoteMethods.showOrders(nameBox.getValue());
                ObservableList<String> ordersString = FXCollections.observableArrayList();
                for(Order i : orders) {
                    ordersString.add(i.toString());
                }
                if(orders.size() == 0) {
                    textArea.setText("No orders from this customer");
                    comboBox.setItems(null);
                }
                else {
                    textArea.clear();
                    comboBox.setItems(ordersString);
                    comboBox.setValue("Orders of customer " + nameBox.getValue());
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void showSpecialOrders() {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Special Orders dialog");
        dialog.setHeaderText("Enter two dates");
        ButtonType okButton = new ButtonType("Find orders", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();

        TextField lowerDateField = new TextField();
        TextField upperDateField = new TextField();

        grid.add(new Label("lowerDate"), 0, 0);
        grid.add(lowerDateField, 1, 0);
        grid.add(new Label("upperDate"), 0, 1);
        grid.add(upperDateField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    java.sql.Date lowerDate = new java.sql.Date(sdf.parse(lowerDateField.getText()).getTime());
                    java.sql.Date upperDate = new java.sql.Date(sdf.parse(upperDateField.getText()).getTime());
                    List<Order> orders = remoteMethods.showOrdersByTheDate(lowerDate, upperDate);
                    ObservableList<String> ordersString = FXCollections.observableArrayList();
                    //= FXCollections.observableArrayList(sql.showOrders(nameBox.getValue()));
                    for(Order i : orders) {
                        ordersString.add(i.toString());
                    }
                    if(orders.size() == 0) {
                        textArea.setText("No orders in this period");
                        comboBox.setItems(null);
                    }
                    else {
                        textArea.clear();
                        comboBox.setItems(ordersString);
                        comboBox.setValue("Required Orders");
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void deleteOrder() {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Delete order dialog");
        dialog.setHeaderText("Enter clothing and price");
        ButtonType okButton = new ButtonType("Delete order", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();

        TextField clothingField = new TextField();
        TextField priceField = new TextField();

        grid.add(new Label("Clothing"), 0, 0);
        grid.add(clothingField, 1, 0);
        grid.add(new Label("Price"), 0, 1);
        grid.add(priceField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                String resultMessage = remoteMethods.deleteOrder(clothingField.getText(), Integer.parseInt(priceField.getText()));
                textArea.setText(resultMessage);
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void deleteCustomer() {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Delete customer dialog");
        dialog.setHeaderText("Choose your prey");
        ButtonType okButton = new ButtonType("Delete customer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();

        ComboBox<String> nameBox = new ComboBox<>();
        ObservableList<String> names = FXCollections.observableArrayList(remoteMethods.getNamesOfCustomers());
        nameBox.setValue("Customers");
        nameBox.setItems(names);

        grid.add(new Label("Name"), 0, 0);
        grid.add(nameBox, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                String resultMessage = remoteMethods.deleteCustomer(nameBox.getValue());
                textArea.setText(resultMessage);
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void addCustomer() {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Add customer");
        dialog.setHeaderText("Enter name and birth");
        ButtonType okButton = new ButtonType("Add customer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();

        TextField nameField = new TextField();
        TextField birthField = new TextField();


        grid.add(new Label("Name"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Birth"), 0, 1);
        grid.add(birthField, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                try {
                    String resultMessage = remoteMethods.addCustomer(nameField.getText(),
                            new java.sql.Date(new SimpleDateFormat("dd.MM.yyyy").parse(birthField.getText()).getTime()));
                    textArea.setText(resultMessage);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        dialog.showAndWait();
    }

    private void addOrder() {
        Dialog<Order> dialog = new Dialog<>();
        dialog.setTitle("Add order");
        dialog.setHeaderText("Enter something");
        ComboBox<String> nameBox = new ComboBox<>();
        ObservableList<String> names = FXCollections.observableArrayList(remoteMethods.getNamesOfCustomers());
        nameBox.setValue("Customers");
        nameBox.setItems(names);
        ButtonType okButton = new ButtonType("Add order", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();

        TextField clothingField = new TextField();
        TextField priceField = new TextField();
        TextField termField = new TextField();

        grid.add(new Label("Name"), 0, 0);
        grid.add(nameBox, 1, 0);
        grid.add(new Label("Clothing"), 0, 1);
        grid.add(clothingField, 1, 1);
        grid.add(new Label("Price"), 0, 2);
        grid.add(priceField, 1, 2);
        grid.add(new Label("Term"), 0, 3);
        grid.add(termField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                try {
                    String resultMessage = remoteMethods.addOrder(nameBox.getValue(), clothingField.getText(),
                            Integer.parseInt(priceField.getText()),
                            new java.sql.Date(new SimpleDateFormat("dd.MM.yyyy").parse(termField.getText()).getTime()) );
                    textArea.setText(resultMessage);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return null;
        });
        dialog.showAndWait();

    }
    public static void main(String[] args) {
        launch(args);
    }
}