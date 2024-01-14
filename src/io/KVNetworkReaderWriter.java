package io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import interfaces.KV;
import interfaces.NetworkReaderWriter;

public class KVNetworkReaderWriter implements NetworkReaderWriter {

    private String fname;

    public KVNetworkReaderWriter(String fname) {
        this.fname = fname;
    }

    @Override
    public KV read() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public void write(KV record) {
        System.out.println(record);
    }

    @Override
    public void openServer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'openServer'");
    }

    @Override
    public void openClient() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'openClient'");
    }

    @Override
    public NetworkReaderWriter accept() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'accept'");
    }

    @Override
    public void closeServer() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closeServer'");
    }

    @Override
    public void closeClient() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'closeClient'");
    }
    
}
