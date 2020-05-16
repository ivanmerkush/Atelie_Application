import javax.xml.bind.annotation.*;
import java.util.List;

@XmlType(name="customers")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Customers extends EntityList{
    @XmlElement(name = "customer", type = Customer.class)
    private List<Customer> customers = null;

    public Customers() {
    }

    public Customers(List<Customer> customers) {
        this.customers = customers;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(List<Customer> customers) {
        this.customers = customers;
    }
}
