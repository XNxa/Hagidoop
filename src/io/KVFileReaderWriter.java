package io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import interfaces.KV;

public class KVFileReaderWriter implements SizedFileReaderWriter {

    private String fname;
    private long index;
    private String mode;
    private FileWriter wfile;
    private FileReader rfile;

    public KVFileReaderWriter(String filename) {
        this.fname = filename;
        this.index = 0;
    }

    public long size(KV kv) {
        return (long) (kv.toString()).length();
    }

    @Override
    public KV read() {
        try {
            StringBuilder kvStr = new StringBuilder("");
            char[] ch = new char[1];
            while (!kvStr.toString().matches("KV \\[k=.*, v=.*\\]")) {
                if (kvStr.toString().equals(KV.SEPARATOR)) {
                    kvStr = new StringBuilder("");
                }
                if (rfile.read(ch, 0, 1) == -1) {
                    return null;
                } else {
                    kvStr.append(ch[0]);
                }
            }
            this.index++;
            return extractKVfromString(kvStr.toString());
        } catch (IOException e) {
            System.err.println("Couldn't read file " + this.fname);
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void write(KV record) {
        try {
            wfile.append(record.toString()+KV.SEPARATOR);
        } catch (IOException e) {
            System.err.println("Unabled to append to file :" + fname);
            e.printStackTrace();
        }
    }

    @Override
    public void open(String mode) {
        if (mode.equals(READ_MODE)) {
            this.mode = mode;
            try {
                rfile = new FileReader(fname);
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
                    rfile.close();
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
        this.index = 0;
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

    private KV extractKVfromString(String kvStr) {
        String s = kvStr;
        s = s.replace("KV [k=", "");
        s = s.replace(", v=", ",");
        s = s.replace("]", "");
        String[] kAndV = s.split(",");
        String key = kAndV[0];
        String value = "";
        if (kAndV.length == 2) {
            value = kAndV[1];
        }
        return new KV(key, value);
    }
    
}
