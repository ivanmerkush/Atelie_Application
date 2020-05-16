import javax.xml.bind.JAXBException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.util.List;

public class XmlImpl implements RemoteMethods {

    Xml xml;

    public XmlImpl(Xml xml) {
        this.xml = xml;
    }

    @Override
    public List<Customer> showCustomers() throws RemoteException, JAXBException {
        return xml.getCustomers();
    }

    @Override
    public List<String> getNamesOfCustomers() throws RemoteException, JAXBException {
        return xml.getNamesOfCustomers();
    }

    @Override
    public List<Order> showOrders(String name) throws RemoteException, JAXBException {
        return xml.getOrdersByName(name);
    }

    @Override
    public List<Order> showOrdersByTheDate(Date lowerLimit, Date upperLimit) throws RemoteException, JAXBException {
        return xml.getOrdersByDate(lowerLimit, upperLimit);
    }

    @Override
    public String deleteOrder(String clothing, int price) throws RemoteException, JAXBException {
        return xml.deleteOrder(clothing, price);
    }

    @Override
    public String addOrder(String name, String clothing, int price, Date term) throws RemoteException, JAXBException {
        return xml.addOrder(name, clothing, price, term);
    }

    @Override
    public String deleteCustomer(String nameOfPrey) throws RemoteException, JAXBException {
        return xml.deleteCustomer(nameOfPrey);
    }

    @Override
    public String addCustomer(String name, Date sqlDate) throws RemoteException, JAXBException {
        return xml.addCustomer(name, sqlDate);
    }
}
