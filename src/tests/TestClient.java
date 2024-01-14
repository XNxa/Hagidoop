package tests;

import interfaces.KV;
import interfaces.NetworkReaderWriter;
import io.KVNetworkReaderWriter;

public class TestClient {

    public static void main(String[] args) {
        NetworkReaderWriter client = new KVNetworkReaderWriter("localhost", 6969, );
        client.openClient();
        client.write(new KV("1", "Test de communication : "));
        client.closeClient();
    }
}
