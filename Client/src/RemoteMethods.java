import java.rmi.Remote;
import java.sql.Date;
import java.util.List;

public interface RemoteMethods extends Remote {
    List<Customer> showCustomers();
    List<String> getNamesOfCustomers();
    List<Order> showOrders(String name);
    List<Order> showOrdersByTheDate(Date lowerLimit, Date upperLimit);
    String deleteOrder(String clothing, int price);
    String addOrder(String name, String clothing, int price, Date term);
    String deleteCustomer(String nameOfPrey);
    String addCustomer(String name, Date sqlDate);
}
