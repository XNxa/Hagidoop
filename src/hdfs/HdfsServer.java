package hdfs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HdfsServer implements Runnable{

    private Socket socket;
    public HdfsServer(Socket s) {
        this.socket = s;
    }
    
    public static void main(String[] args) {
        int port = 0; //TODO : gérer les arguments 
        
        try (ServerSocket ss = new ServerSocket(port)) {
            while (true) {
                Socket s = ss.accept();
                new Thread(new HdfsServer(s)).start();
            }
        } catch (IOException e) {
            System.err.println("Unable to launch Server :\n" + e.getMessage());
        }
    }

    @Override
    public void run() {
        // TODO : todo
    }
}
