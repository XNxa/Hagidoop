package io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import interfaces.KV;

public class TxtFileReaderWriter implements SizedFileReaderWriter {

    private String fname;
    private String mode;
    private BufferedReader brfile;
    private FileWriter wfile;
    private int index;

    public TxtFileReaderWriter(String filename) {
        this.fname = filename;
    }

    public long size(KV kv) {
        return (long) (kv.v).length();
    }

    @Override
    public KV read() {
        String line;
        try {
            if ((line = brfile.readLine()) != null) {
                return new KV(Integer.toString(index++), line.toString());
            } else {
                return null;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void write(KV record) {
        try {
            wfile.append(record.v + '\n');
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void open(String mode) {
        index = 0;
        if (mode.equals(READ_MODE)) {
            this.mode = mode;
            try {
                brfile = new BufferedReader(new FileReader(fname));
            } catch (FileNotFoundException e) {
                System.err.println("File Not Found " + fname);
                e.printStackTrace();
            }
        } else if (mode.equals(WRITE_MODE)) {
            this.mode = mode;
            File f = new File(fname);
            if (!f.exists()) {
                try {
                    f.createNewFile();
                } catch (IOException e) {
                    System.err.println("Coudn't create new file : " + fname);
                    e.printStackTrace();
                }
            }

            try {
                wfile = new FileWriter(f);
            } catch (IOException e) {
                System.err.println("IO Error with file " + fname);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        switch (mode) {
            case READ_MODE:
                try {
                    brfile.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case WRITE_MODE:
                try {
                    wfile.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public long getIndex() {
        return index;
    }

    @Override
    public String getFname() {
        return fname;
    }

    @Override
    public void setFname(String fname) {
        this.fname = fname;
    }

}
