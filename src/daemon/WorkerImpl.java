package daemon;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import interfaces.FileReaderWriter;
import interfaces.Map;
import interfaces.NetworkReaderWriter;

public class WorkerImpl implements Worker {

    @Override
    public void runMap(Map m, FileReaderWriter reader, NetworkReaderWriter writer) throws RemoteException {
        // 

    }

    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        } else {
            int port = Integer.parseInt(args[0]); //
            try {
                Registry registry = LocateRegistry.createRegistry(port);
                Worker w = new WorkerImpl();
                Naming.rebind("//" + InetAddress.getLocalHost().getHostName(), w);
                
            } catch (NumberFormatException e) {
                usage();
            } catch (RemoteException e) {
                
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
