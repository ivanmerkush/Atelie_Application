import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

public class JSON {
    private Gson gson;
    private File customersFile;
    private File ordersFile;
    private Type customers;
    private Type orders;
    private int customerMaxId;
    private int orderMaxId;
    public JSON() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        customersFile = new File("D:/eclipse-workspace/Lab_2_JavaFX/Server/src/customers.json");
        ordersFile =new File("D:/eclipse-workspace/Lab_2_JavaFX/Server/src/orders.json");
        customers = new TypeToken<List<Customer>>(){}.getType();
        orders = new TypeToken<List<Order>>(){}.getType();
        customerMaxId =this.getCustomers().
                            stream().
                            map(Customer::getIdCustomer).
                            max(Integer::compareTo).
                            orElse(0);
        orderMaxId =this.getOrders().stream().map(Order::getIdOrder).max(Integer::compareTo).orElse(0);
    }

    public String readJSON(File file)  {
        StringBuilder str = new StringBuilder("");
        try (Scanner scanner = new Scanner(new FileReader(file))) {
            while (scanner.hasNextLine()) {
                str.append(scanner.nextLine());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return str.toString();
    }

    public void writeJSON(String jsonList, File file) throws IOException {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(jsonList);
        }
    }
    //1
    public List<Customer> getCustomers() {
        return gson.fromJson(readJSON(customersFile), customers);
    }

    public List<String> getNamesOfCustomers() {
        List<Customer> allCustomers = gson.fromJson(readJSON(customersFile), customers);
        return allCustomers.stream().map(Customer::getName).collect(Collectors.toList());
    }

    public List<Order> getOrders() {
        return gson.fromJson(readJSON(ordersFile), orders);
    }
    //2
    public List<Order> getOrdersByName(String name) {
        Optional<Customer> customer = getCustomers().stream().filter(customer1 -> customer1.getName().equals(name)).findFirst();
        return getOrders().stream().filter(order -> order.getIdCustomer() == customer.get().getIdCustomer()).collect(Collectors.toList());
    }
    //3
    public List<Order> getOrdersByDate(java.sql.Date lowerLimit, java.sql.Date upperLimit) {
        Date lowLimit = new Date(lowerLimit.getTime());
        Date upLimit = new Date(upperLimit.getTime());
        return getOrders().stream().filter(order -> order.getTerm().before(upLimit) && order.getTerm().after(lowLimit)).collect(Collectors.toList());
    }
    //4
    public String addCustomer(String name, java.sql.Date birth) {
        List<Customer> allCustomers = this.getCustomers();
        Optional<Customer> customer =allCustomers.stream().filter(customer1 -> customer1.getName().equals(name)).findAny();
        if(customer.isPresent()) {
            return "This customer already exists";
        }
        else {
            try {
                Customer currentCustomer = new Customer(name, new Date(birth.getTime()), ++customerMaxId);
                allCustomers.add(currentCustomer);
                String jsonCustomers = gson.toJson(allCustomers);
                writeJSON(jsonCustomers, customersFile);
                return "New customer has been added";
            } catch (IOException e) {
                e.printStackTrace();
                return "Error";
            }
        }
    }
    //5
    public String addOrder(String name, String clothing, int price, java.sql.Date term) {
        List<Customer> allCustomers = this.getCustomers();
        int idOfCustomer = allCustomers.stream().filter(customer1 -> customer1.getName().equals(name)).findAny().map(Customer::getIdCustomer).get();
        try {
            List<Order> allOrders = this.getOrders();
            Order currentOrder = new Order(++orderMaxId, clothing, price, new Date(term.getTime()), idOfCustomer);
            allOrders.add(currentOrder);
            String jsonOrder = gson.toJson(allOrders);
            writeJSON(jsonOrder, ordersFile);
            return "New order has been added";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error";
        }

    }

    public String deleteCustomer(String name) {
        List<Order> allOrders = this.getOrders();
        List<Customer> allCustomers = this.getCustomers();
        Optional<Customer> currentCustomer = allCustomers.stream().filter(customer -> name.equals(customer.getName())).findAny();
        allOrders.removeIf(order -> order.getIdCustomer() == currentCustomer.get().getIdCustomer());
        allCustomers.removeIf(customer -> customer.getName().equals(name));
        String jsonOrders = gson.toJson(allOrders);
        String jsonCustomers = gson.toJson(allCustomers);
        try {
            writeJSON(jsonCustomers, customersFile);
            writeJSON(jsonOrders, ordersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Customer has been deleted";
    }

    public String deleteOrder(String clothing, int price) {
        List<Order> allOrders = this.getOrders();
        allOrders.removeIf(order -> order.getClothing().equals(clothing) && order.getPrice() == price);
        String jsonOrders = gson.toJson(allOrders);
        try {
            writeJSON(jsonOrders, ordersFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Order has been deleted";
    }

}
