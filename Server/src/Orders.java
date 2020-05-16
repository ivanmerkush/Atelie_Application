import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlType(name = "orders")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Orders extends EntityList{
    @XmlElement(name = "order", type = Order.class)
    private List<Order> orders = new ArrayList<>();

    public Orders() {
    }

    public Orders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
