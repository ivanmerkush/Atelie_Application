import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Класс, описывающий пользователя великого ателье. Описывает такие свойства как имя, дата рождения. Автоматически формируется уникальный индентификатор.
 * @author Ivan
 * @version 2.1
 */
@XmlRootElement(name = "customer")
@XmlAccessorType(XmlAccessType.FIELD)
public class Customer implements Serializable {
    @XmlAttribute(name="idCustomer")
    /** Поле индентификатор*/
    private int idCustomer;

    @XmlElement(name="name")
    /** Поле имя*/
    private String name;
    @XmlElement(name="birth")
    /** Поле дата рождения*/
    private Date birth;

    /**
     *Пустой конструктор - создание объекта без определенных значений.
     * @link Customer#Customer()
     */
    public Customer() {

    }


    /**
     * Конструктор - создание нового объекта с определенными значениями
     * @param name - имя
     * @param birth - дата Рождения
     * @link Customer#Customer()
     */
    public Customer(String name, Date birth, int idCustomer) {
        this.idCustomer = idCustomer;
        this.name = name;
        this.birth = birth;
    }
    /**
     * Конструктор - создание нового объекта по полученной строке
     * @param info - Строка содержащая нужные данные для создания объекта
     * @link Customer#Customer()
     */
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
    /**
     * Функция получения значения поля {@see Customer#idCustomer}
     * @return возвращает id пользователя
     */
    public int getIdCustomer() {
        return idCustomer;
    }
    /**
     * Функция получения значения поля {@see Customer#name}
     * @return возвращает имя пользователя
     */
    public String getName() {
        return name;
    }
    /**
     * Процедура определения производителя {@link Customer#name}
     * @param name - имя
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * Функция получения значения поля {@link Customer#birth}
     * @return возвращает дату рождения пользователя
     */
    public Date getBirth() {
        return birth;
    }

    /**
     * Процедура определения производителя {@link Customer#birth}
     * @param birth - дата рождения
     */
    public void setBirth(Date birth) {
        this.birth = birth;
    }
    /**
     * @return возвращает строковое представление пользователя
     */
    @Override
    public String toString() {
        return "Customer#" + idCustomer + "{" +
                "name='" + name + '\'' +
                ", birth=" + birth +
                '}';
    }
}
