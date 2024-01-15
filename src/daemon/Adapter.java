package daemon;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private NetworkReaderWriter server;
    private int compteur;
    private final int n = (new Config()).getNumberOfWorkers();


    public Adapter () {
        kvQueue = new LinkedBlockingQueue<KV>();
        server = new KVNetworkReaderWriter(Project.PORT_ADAPTER);
        server.openServer();
        compteur = 0;
    }

    public void closeAdapter() {
        server.closeServer();
    }
    
    public NetworkReaderWriter getAdapterEntry() {
        new Slave().start();
        try {
            return new KVNetworkReaderWriter(InetAddress.getLocalHost().getHostAddress(), Project.PORT_ADAPTER);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public KV read() {
        try {
            KV kv = kvQueue.take();
            
            if (kv.v == null && kv.k == null && this.compteur == this.n - 1) {
                return null;
            } else if (kv.v == null && kv.k == null) {
                this.compteur++;
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

    private class Slave extends Thread {
        
        @Override
        public void run() {
            NetworkReaderWriter masocket = server.accept();
            KV kv;
            try {
                while ((kv = masocket.read()) != null) {
                    kvQueue.put(kv);
                    // TODO Auto-generated catch block
                }
                kvQueue.put(new KV(null, null));
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {}
            masocket.closeClient();
        }
    }
}
