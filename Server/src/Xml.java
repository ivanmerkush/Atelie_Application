import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Xml {
    private JAXBContext customerContext;
    private JAXBContext orderContext;
    private File customerFile;
    private File orderFile;
    private int customerMaxId;
    private int orderMaxId;
    public Xml() throws JAXBException {
        customerContext = JAXBContext.newInstance(Customers.class);
        orderContext = JAXBContext.newInstance(Orders.class);
        customerFile = new File("D:/eclipse-workspace/Lab_2_JavaFX/Server/src/customers.xml");
        orderFile = new File("D:/eclipse-workspace/Lab_2_JavaFX/Server/src/orders.xml");
        customerMaxId =this.getCustomers().
                stream().
                map(Customer::getIdCustomer).
                max(Integer::compareTo).
                orElse(0);
        orderMaxId =this.getOrders().stream().map(Order::getIdOrder).max(Integer::compareTo).orElse(0);
    }



    public List<Customer> getCustomers()  {
        try {
            Unmarshaller unmarshaller = null;
            unmarshaller = customerContext.createUnmarshaller();
            Customers customers = (Customers) unmarshaller.unmarshal(customerFile);
            return customers.getCustomers();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void writeXML(EntityList list, JAXBContext context, File file){
        try {
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            FileWriter writer = new FileWriter(file);
            marshaller.marshal(list, writer);
        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        }

    }

    public List<String> getNamesOfCustomers() {
        List<Customer> allCustomers = getCustomers();
        return allCustomers.stream().map(Customer::getName).collect(Collectors.toList());
    }

    public List<Order> getOrders(){
        try {
            Unmarshaller unmarshaller = null;
            unmarshaller = orderContext.createUnmarshaller();
            Orders orders = (Orders) unmarshaller.unmarshal(orderFile);
            return orders.getOrders();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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
    public String addCustomer(String name, java.sql.Date birth)  {
        List<Customer> allCustomers = this.getCustomers();
        Optional<Customer> customer =allCustomers.stream().filter(customer1 -> customer1.getName().equals(name)).findAny();
        if(customer.isPresent()) {
            return "This customer already exists";
        }
        else {
            Customer currentCustomer = new Customer(name, new Date(birth.getTime()), ++customerMaxId);
            allCustomers.add(currentCustomer);
            Customers customers = new Customers(allCustomers);
            // сама сериализация
            writeXML(customers, customerContext, customerFile);
            return "New customer has been added";
        }
    }
    //5
    public String addOrder(String name, String clothing, int price, java.sql.Date term) {
        List<Customer> allCustomers = this.getCustomers();
        int idOfCustomer = allCustomers.stream().filter(customer1 -> customer1.getName().equals(name)).findAny().map(Customer::getIdCustomer).get();
        List<Order> allOrders = this.getOrders();
        Order currentOrder = new Order(++orderMaxId, clothing, price, new Date(term.getTime()), idOfCustomer);
        allOrders.add(currentOrder);
        Orders orders = new Orders(allOrders);
        writeXML(orders, orderContext, orderFile);
        return "New order has been added";
    }

    public String deleteCustomer(String name) {
        List<Order> allOrders = this.getOrders();
        List<Customer> allCustomers = this.getCustomers();
        Optional<Customer> currentCustomer = allCustomers.stream().filter(customer -> name.equals(customer.getName())).findAny();
        allOrders.removeIf(order -> order.getIdCustomer() == currentCustomer.get().getIdCustomer());
        allCustomers.removeIf(customer -> customer.getName().equals(name));

        Customers customers = new Customers(allCustomers);
        Orders orders = new Orders(allOrders);

        writeXML(customers, customerContext, customerFile);
        writeXML(orders, orderContext, orderFile);
        return "Customer has been deleted";
    }

    public String deleteOrder(String clothing, int price) {
        List<Order> allOrders = this.getOrders();
        allOrders.removeIf(order -> order.getClothing().equals(clothing) && order.getPrice() == price);
        Orders orders = new Orders(allOrders);
        writeXML(orders, orderContext, orderFile);
        return "Order has been deleted";
    }
}
