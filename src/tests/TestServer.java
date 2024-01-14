package tests;

import interfaces.KV;
import interfaces.NetworkReaderWriter;
import io.KVNetworkReaderWriter;

public class TestServer {
    
    public static void main(String[] args) {
        NetworkReaderWriter serveur = new KVNetworkReaderWriter(6969);
        serveur.openServer();
        NetworkReaderWriter client = serveur.accept();
        KV monPremierKV = client.read();
        System.out.println(monPremierKV.v);
        serveur.closeServer();
        client.closeClient();
    }
}
