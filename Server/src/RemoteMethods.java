import javax.xml.bind.JAXBException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

public interface RemoteMethods extends Remote {
    List<Customer> showCustomers() throws RemoteException, JAXBException;
    List<String> getNamesOfCustomers() throws RemoteException, JAXBException;
    List<Order> showOrders(String name) throws RemoteException, JAXBException;
    List<Order> showOrdersByTheDate(Date lowerLimit, Date upperLimit) throws RemoteException, JAXBException;
    String deleteOrder(String clothing, int price) throws RemoteException, JAXBException;
    String addOrder(String name, String clothing, int price, Date term) throws RemoteException, JAXBException;
    String deleteCustomer(String nameOfPrey) throws RemoteException, JAXBException;
    String addCustomer(String name, Date sqlDate) throws RemoteException, JAXBException;
}
