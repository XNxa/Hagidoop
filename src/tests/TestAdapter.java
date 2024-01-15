package tests;

import daemon.Adapter;
import interfaces.KV;
import interfaces.NetworkReaderWriter;

public class TestAdapter {
    
    public static void main(String[] args) {
        Adapter adapter = new Adapter();
        
        switch (args[0]) {
            case "s":
                while (true) {
                    System.out.println(adapter.read());
                }
            case "c":
                NetworkReaderWriter writer = adapter.getAdapterEntry();
                writer.openClient();
                writer.write(new KV("Salut", "Voici mon KKV"));
                writer.closeClient();
                break;
                
            default:
                break;
        }
    }

}
