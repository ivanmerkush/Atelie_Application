import javax.xml.bind.JAXBException;
import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class Server {


    public static void main(String[] args) throws RemoteException, AlreadyBoundException, SQLException, JAXBException {
//        SqlImpl service = new SqlImpl(new Sql());
//        System.setProperty("java.rmi.server.hostname", "192.168.100.2");
//        JSONImpl service = new JSONImpl(new JSON());
        XmlImpl service = new XmlImpl(new Xml());
        Remote stub = UnicastRemoteObject.exportObject(service, 0);
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("RMI", stub);
    }

}
