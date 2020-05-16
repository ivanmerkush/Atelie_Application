import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Sql {
    static private Connection conn;
    String DB_URL ="jdbc:mysql://localhost:3306/atelie?serverTimezone=UTC";
    String USER = "root";
    String PASS = "root";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    public Sql() throws SQLException {
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
//        Statement statement =conn.createStatement();
//        statement.executeUpdate("create table if not exists customer(id_customer INT NOT NULL AUTO_INCREMENT, name VARCHAR(45) NOT NULL," +
//                                   " birth DATE, PRIMARY KEY (id_customer))");
//        statement.executeUpdate("create table if not exists `order`(id_order INT NOT NULL AUTO_INCREMENT, clothing VARCHAR(45) NOT NULL," +
//                                   " price INT NOT NULL, term DATE, id_customer INT NOT NULL, PRIMARY KEY (id_order)," +
//                                   " constraint fk_order_customer foreign key (id_customer) references customer(id_customer) on delete cascade)");

    }

    public List<Customer> getCustomers()  {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(" select * from customer");
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("id_customer");
                String name = resultSet.getString("name");
                Date birth = resultSet.getDate("birth");
                Customer currentCustomer = new Customer(name, birth, id);
                customers.add(currentCustomer);
            }
            return customers;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<String> getNamesOfCustomers() {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(" select c.name from customer c");
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            List<String> namesOfCustomers = new ArrayList<>();
            while (resultSet.next()) {
                namesOfCustomers.add(resultSet.getString("name"));
            }
            return namesOfCustomers;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Order> getOrdersByName(String name){
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("select o.clothing, o.price, o.term, o.id_customer from `order` o join customer c on o.id_customer = c.id_customer where c.name = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.last();
            int sizeOfResultSet = resultSet.getRow();
            resultSet.beforeFirst();
            List<Order> orders = new ArrayList<>();
            if (sizeOfResultSet > 0) {
                while (resultSet.next()) {
                    String clothing = resultSet.getString("clothing");
                    int price = resultSet.getInt("price");
                    Date term = resultSet.getDate("term");
                    int idCustomer = resultSet.getInt("id_customer");
                    orders.add(new Order(clothing, price, term, idCustomer));
                }
            }
            return orders;
        }
        catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Order> getOrdersByTheDate(Date lowerLimit, Date upperLimit) {
        List<Order> orders = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("select o.clothing, o.price, o.term, o.id_customer from `order` o where o.term between ? and ? order by term");
            preparedStatement.setDate(1, lowerLimit);
            preparedStatement.setDate(2, upperLimit);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                String clothing = resultSet.getString("clothing");
                int price = resultSet.getInt("price");
                Date term = resultSet.getDate("term");
                int idCustomer = resultSet.getInt("id_customer");
                orders.add(new Order(clothing, price, term, idCustomer));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public String deleteOrder(String clothing, int price){
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "delete from `order` where clothing = ? and price = ?");
            preparedStatement.setString(1, clothing);
            preparedStatement.setInt(2, price);
            if (preparedStatement.executeUpdate() > 0) {
                return "Order has been deleted";
            } else {
                return "Order not found";
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }


    public String addOrder(String name, String clothing, int price, Date term) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "select id_customer from customer where name = ?");

            preparedStatement.setString(1, name);
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getResultSet();
            if (resultSet.next()) {
                int idCustomer = resultSet.getInt("id_customer");

                preparedStatement = conn.prepareStatement(
                        "insert into `order`(clothing, price, term, id_customer) " +
                                " values (?, ?, ?, ?)");

                preparedStatement.setString(1, clothing);
                preparedStatement.setInt(2, price);
                preparedStatement.setDate(3, term);
                preparedStatement.setInt(4, idCustomer);
                preparedStatement.execute();

                return "New order has been added";
            } else {
                return "There is no customer named " + name;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

    public String deleteCustomer(String nameOfPrey) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = conn.prepareStatement(
                    "delete from customer where name = ?");
            preparedStatement.setString(1, nameOfPrey);
            if (preparedStatement.executeUpdate() > 0) {
                return "Customer has been deleted";
            } else {
                return "Customer not found";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }
    public String addCustomer(String name, Date sqlDate) {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "insert into customer(name, birth) " +
                            " values (?, ? )");

            preparedStatement.setString(1, name);
            preparedStatement.setDate(2, sqlDate);
            preparedStatement.execute();
            return "New customer has been added";
        }
        catch(SQLException e) {
            e.printStackTrace();
            return "error";
        }
    }

}
