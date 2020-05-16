import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class Customer implements Serializable {
    private int idCustomer;
    private String name;
    private Date birth;


    public Customer() {

    }

    public Customer(String name, Date birth, int idCustomer) {
        this.idCustomer = idCustomer;
        this.name = name;
        this.birth = birth;
    }

    public Customer(String info) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        StringTokenizer str = new StringTokenizer(info, " ,;");
        if(str.hasMoreTokens()) {
            name = str.nextToken();
        }
        if(str.hasMoreTokens())
        {
            birth = sdf.parse(str.nextToken());
        }
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Customer#" + idCustomer + "{" +
                "name='" + name + '\'' +
                ", birth=" + birth +
                '}';
    }
}
