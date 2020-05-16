import java.sql.Date;
import java.util.List;

public class SqlImpl implements RemoteMethods {

    private Sql sql;

    public SqlImpl(Sql sql) {
        this.sql = sql;
    }

    @Override
    public List<Customer> showCustomers() {
        return sql.getCustomers();
    }

    @Override
    public List<String> getNamesOfCustomers() {
        return sql.getNamesOfCustomers();
    }

    @Override
    public List<Order> showOrders(String name) {
        return sql.getOrdersByName(name);
    }

    @Override
    public List<Order> showOrdersByTheDate(Date lowerLimit, Date upperLimit) {
        return sql.getOrdersByTheDate(lowerLimit, upperLimit);
    }

    @Override
    public String deleteOrder(String clothing, int price) {
        return sql.deleteOrder(clothing, price);
    }

    @Override
    public String addOrder(String name, String clothing, int price, Date term) {
        return sql.addOrder(name, clothing, price, term);
    }

    @Override
    public String deleteCustomer(String nameOfPrey) {
        return sql.deleteCustomer(nameOfPrey);
    }

    @Override
    public String addCustomer(String name, Date sqlDate) {
        return sql.addCustomer(name, sqlDate);
    }
}
