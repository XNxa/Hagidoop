package daemon;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import interfaces.FileReaderWriter;
import interfaces.Map;
import interfaces.NetworkReaderWriter;

public class WorkerImpl extends UnicastRemoteObject implements Worker {

    protected WorkerImpl() throws RemoteException {
        super();
        //TODO Auto-generated constructor stub
    }

    @Override
    public void runMap(Map m, FileReaderWriter reader, NetworkReaderWriter writer) throws RemoteException {
        System.out.println("Starting map on this node");
        m.map(reader, writer);
        System.out.println("Map finished");
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        } else {
            int port = Integer.parseInt(args[0]); //
            try {
                LocateRegistry.createRegistry(port);
                Worker w = new WorkerImpl();
                Naming.rebind("//" + InetAddress.getLocalHost().getHostName() +
                ":" + port + "/HagidoopWorker", w);
                
            } catch (NumberFormatException e) {
                usage();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    private static void usage() {
        System.out.println("Usage : java WorkerImpl <port>");
    }
}
