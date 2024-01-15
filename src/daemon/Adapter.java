package daemon;

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
        return new KVNetworkReaderWriter("localhost", Project.PORT_ADAPTER);
    }

    @Override
    public KV read() {
        try {
            KV kv = kvQueue.take();
            
            if (kv == null && this.compteur >= this.n) {
                return null;
            } else if (kv == null) {
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
            while ((kv = masocket.read()) != null) {
                try {
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
