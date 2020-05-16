import java.sql.Date;
import java.util.List;

public class JSONImpl implements RemoteMethods {

    JSON json;

    public JSONImpl(JSON json) {
        this.json = json;
    }

    @Override
    public List<Customer> showCustomers() {
        return json.getCustomers();
    }

    @Override
    public List<String> getNamesOfCustomers(){
        return json.getNamesOfCustomers();
    }

    @Override
    public List<Order> showOrders(String name) {
        return json.getOrdersByName(name);
    }

    @Override
    public List<Order> showOrdersByTheDate(Date lowerLimit, Date upperLimit) {
        return json.getOrdersByDate(lowerLimit, upperLimit);
    }

    @Override
    public String deleteOrder(String clothing, int price) {
        return json.deleteOrder(clothing, price);
    }

    @Override
    public String addOrder(String name, String clothing, int price, Date term) {
        return json.addOrder(name, clothing, price, term);
    }

    @Override
    public String deleteCustomer(String nameOfPrey) {
        return json.deleteCustomer(nameOfPrey);
    }

    @Override
    public String addCustomer(String name, Date sqlDate) {
        return json.addCustomer(name, sqlDate);
    }
}
