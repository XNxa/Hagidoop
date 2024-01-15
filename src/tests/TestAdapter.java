package tests;

import daemon.Adapter;
import interfaces.KV;
import interfaces.NetworkReaderWriter;

public class TestAdapter {
    
    public static void main(String[] args) {
        Adapter adapter = new Adapter();
        new Client(adapter, new KV("1", "envie de mourir")).start();
        new Client(adapter, new KV("2", "caca")).start();
        new Client(adapter, new KV("3", "ouaiouai")).start();
        new Client(adapter, new KV("4", "bzbzbzbz")).start();
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
