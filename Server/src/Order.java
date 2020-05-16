import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements Serializable {
    @XmlAttribute(name="idOrder")
    private int idOrder;
    @XmlElement(name="clothing")
    private String clothing;
    @XmlElement(name="price")
    private int price;
    @XmlElement(name="term")
    private Date term;
    @XmlAttribute(name="idCustomer")
    private int idCustomer;



    public Order() {

    }

    public Order(int idOrder, String clothing, int price, Date term, int idCustomer) {
        this.idOrder = idOrder;
        this.clothing = clothing;
        this.price = price;
        this.term = term;
        this.idCustomer = idCustomer;
    }

    public Order(String clothing, int price, Date term, int id_customer) {
        this.clothing = clothing;
        this.price = price;
        this.term = term;
        this.idCustomer = id_customer;
    }

    public Order(String info) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        StringTokenizer str = new StringTokenizer(info, " ,;");
        if(str.hasMoreTokens()) {
            clothing = str.nextToken();
        }
        if(str.hasMoreTokens())
        {
            price = Integer.parseInt(str.nextToken());
        }
        if(str.hasMoreTokens())
        {
            term = sdf.parse(str.nextToken());
        }
    }

    public int getIdOrder() {
        return idOrder;
    }


    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getClothing() {
        return clothing;
    }

    public void setClothing(String clothing) {
        this.clothing = clothing;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Date getTerm() {
        return term;
    }

    public void setTerm(Date term) {
        this.term = term;
    }

    @Override
    public String toString() {
        return "Order {" +
                "clothing='" + clothing + '\'' +
                ", price=" + price +
                ", term=" + term +
                '}';
    }
}
