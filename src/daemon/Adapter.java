package daemon;

import java.net.SocketException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import config.Config;
import config.Project;
import interfaces.KV;
import interfaces.NetworkReaderWriter;
import interfaces.Reader;
import io.KVNetworkReaderWriter;

public class Adapter implements Reader {

    private BlockingQueue<KV> kvQueue;
    private int compteur;
    private final int n = (new Config()).getNumberOfWorkers();
    private NetworkReaderWriter[] servers;
    private int[] ports;
    private int nextPortIndex = 0;


    public Adapter () {
        kvQueue = new LinkedBlockingQueue<KV>();
        servers = new KVNetworkReaderWriter[n];
        ports = new int[n];
        for (int i = 0 ; i < n ; i++) {
            ports[i] = Project.PORT_ADAPTER + i;
            servers[i] = new KVNetworkReaderWriter(ports[i]);
            servers[i].openServer();
        } 
        compteur = 0;
    }

    public void closeAdapter() {
        for (NetworkReaderWriter server : servers) {
            server.closeServer();
        }
    }
    
    public NetworkReaderWriter getAdapterEntry() {
        new AdapterSlave(nextPortIndex).start();
        return new KVNetworkReaderWriter("localhost", (nextPortIndex++) + Project.PORT_ADAPTER);
    }

    @Override
    public KV read() {
        try {
            KV kv = kvQueue.take();
            
            if (kv == null && this.compteur == this.n) {
                return null;
            } else if (kv == null) {
                compteur++;
                return read();
            } else {
                return kv;
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    private class AdapterSlave extends Thread {
        
        private int portIndex;

        public AdapterSlave(int i) {
            portIndex = i;
        }

        @Override
        public void run() {
            KVNetworkReaderWriter masocket = (KVNetworkReaderWriter) servers[portIndex].accept();
            KV kv;
            if (masocket.getSocket().isClosed()) {
                System.out.println("socket closed..... POURQUOI ???");
            } else {
                System.err.println("Socket ouverte au niveau d'adapter");
            }
            while ((kv = masocket.read()) != null) {
                try {
                    System.out.println("Put in the queue the kv of key : " + kv.k );
                    kvQueue.put(kv);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            masocket.closeClient();
        }
    }


}
