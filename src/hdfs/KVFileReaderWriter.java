package hdfs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import interfaces.FileReaderWriter;
import interfaces.KV;

public class KVFileReaderWriter implements FileReaderWriter {

    private String fname;
    private long index;
    private KV[] kvs;
    private FileWriter infile;

    public KVFileReaderWriter(String filename) throws FileFormatException {
        
        // TODO: Change that because array to big
        /* fname = filename;
        try (BufferedReader file = new BufferedReader(new FileReader(fname))) {
            String line;
            int lineNumber = 0;
            LinkedList<KV> kv = new LinkedList<KV>();
            while ((line = file.readLine()) != null) {
                lineNumber++;
                String[] kvFromThisLine = line.split(KV.SEPARATOR);
                for (String kvStr : kvFromThisLine) {
                    if (!kvStr.matches("KV [k=.*, v=.*]")) {
                        throw new FileFormatException(kvStr, lineNumber);
                    } else {
                        // Extract key and value
                        kvStr.replace("KV [k=", "");
                        kvStr.replace(", v=", "");
                        kvStr.replace("]", "");
                        String[] kAndV = kvStr.split(",");
                        String key = kAndV[0];
                        String value = kAndV[1];
                        // Add the KV to the array
                        kv.add(new KV(key, value));
                    }
                }
            }
            // Add kv to kvs
            kvs = kv.toArray(new KV[0]);
        } catch (FileNotFoundException e) {
            kvs = new KV[0];
        } catch (IOException e) {
            System.err.println("An error occured while reading file : " + filename);
            e.printStackTrace();
        } */
    }

    @Override
    public KV read() {
        return kvs[0];
    }

    @Override
    public void write(KV record) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'write'");
    }

    @Override
    public void open(String mode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'open'");
    }

    @Override
    public void close() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'close'");
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

    private KV extractKVfromString(String kvStr, int lineNumber) throws FileFormatException {
        if (!kvStr.matches("KV [k=.*, v=.*]")) {
            throw new FileFormatException(kvStr, lineNumber);
        } else {
            // Extract key and value
            kvStr.replace("KV [k=", "");
            kvStr.replace(", v=", "");
            kvStr.replace("]", "");
            String[] kAndV = kvStr.split(",");
            String key = kAndV[0];
            String value = kAndV[1];
            // Add the KV to the array
            return new KV(key, value);
        }
    }
    
}
