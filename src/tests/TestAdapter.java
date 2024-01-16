package tests;

import daemon.Adapter;
import interfaces.KV;
import interfaces.NetworkReaderWriter;

/* Classe pour tester le comportement de l'adapter. */
public class TestAdapter {
    
    public static void main(String[] args) {
        Adapter adapter = new Adapter();
        new Client(adapter, new KV("1", "Mon premier kv")).start();
        new Client(adapter, new KV("2", "Mon deuxieme kv")).start();
        new Client(adapter, new KV("3", "Mon troisème kv")).start();
        new Client(adapter, new KV("4", "Mon quatrième kv")).start();
        System.out.println("!!!! READ : " + adapter.read());
        System.out.println("!!!! READ : " + adapter.read());
        System.out.println("!!!! READ : " + adapter.read());
        System.out.println("!!!! READ : " + adapter.read());
        System.out.println("!!!! READ : " + adapter.read());
        System.out.println("!!!! READ : " + adapter.read());
        System.out.println("!!!! READ : " + adapter.read());
        System.out.println("!!!! READ : " + adapter.read());
        System.out.println("!!!! READ : " + adapter.read());
    }

    private static class Client extends Thread {
        Adapter adapter;
        KV kv;
        public Client(Adapter a, KV kv) {
            this.adapter = a;
            this.kv = kv;
        }
        @Override
        public void run() {
            NetworkReaderWriter writer = adapter.getAdapterEntry();
            writer.openClient();
            writer.write(kv);
            writer.write(kv);
            writer.write(null);
            writer.closeClient();
        }    
    }  

}
