package tests;

import interfaces.KV;
import interfaces.NetworkReaderWriter;
import io.KVNetworkReaderWriter;

/* Classe client pour tester le comportement de KVNetworkReaderWriter. */
public class TestClient {

    public static void main(String[] args) {
        NetworkReaderWriter client = new KVNetworkReaderWriter("localhost", 6969);
        client.openClient();
        client.write(new KV("1", "hello !"));
        client.closeClient();
    }
}
