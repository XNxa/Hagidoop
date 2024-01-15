package io;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import interfaces.KV;
import interfaces.NetworkReaderWriter;

public class KVNetworkReaderWriter implements NetworkReaderWriter {

    private String ip;
    private int serverPort;
    private ServerSocket ss;
    private Socket cs;

    private enum Opentype {
        SERVER, 
        CLIENT
    }

    private Opentype opentype;
    

    public KVNetworkReaderWriter(int serverPort) {
        this.serverPort = serverPort;
        opentype = Opentype.SERVER; 
    }

    public KVNetworkReaderWriter(Socket s) {
        this.cs = s;
        opentype = Opentype.CLIENT;
    }

    public KVNetworkReaderWriter(String ip, int port) {
        this.ip = ip;
        this.serverPort = port;
        opentype = Opentype.CLIENT;
    }

    @Override
    public KV read() {
        if (opentype == Opentype.CLIENT) {
            try (ObjectInputStream is = new ObjectInputStream(cs.getInputStream())) {
                KV kv =  (KV) is.readObject();
                return kv;
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        } else {
            throw new UnsupportedOperationException("You can't use the method read on a server.");
        }
    }

    @Override
    public void write(KV record) {
        System.out.println(record);
        if (opentype == Opentype.CLIENT) {
            try (ObjectOutputStream os = new ObjectOutputStream(cs.getOutputStream())) {
                os.writeObject(record);
            } catch (Exception e) {
                // TODO: handle exception
            }
        } else {
            throw new UnsupportedOperationException("You can't use the method write on a server.");
        }
    }

    @Override
    public void openServer() {
        if (opentype == Opentype.SERVER) { 
            try {
                ss = new ServerSocket(serverPort);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("You can't use the method openServer on a client.");
        }
    }

    @Override
    public void openClient() {
        if (opentype == Opentype.CLIENT) {
            try {
                cs = new Socket(this.ip, this.serverPort);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("You can't use the method openClient on a server.");
        }
    }

    @Override
    public NetworkReaderWriter accept() {
        if (opentype == Opentype.SERVER) { 
            try {
                return new KVNetworkReaderWriter(ss.accept());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            throw new UnsupportedOperationException("You can't use the method accept on a client.");
        }
    }

    @Override
    public void closeServer() {
        if (opentype == Opentype.SERVER) {
            try {
                ss.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else { 
            throw new UnsupportedOperationException("You can't use the method closeServer on a client.");
        }
    }

    @Override
    public void closeClient() {
        if (opentype == Opentype.CLIENT) {
            try {
                cs.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("You can't use the method closeClient on a client.");
        }
    }
    
}
