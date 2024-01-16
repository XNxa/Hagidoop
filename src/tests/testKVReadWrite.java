package tests;

import interfaces.KV;
import io.KVFileReaderWriter;
import io.TxtFileReaderWriter;

public class testKVReadWrite {
    
    public static void main(String[] args) {
        
        TxtFileReaderWriter txtrw = new TxtFileReaderWriter(args[0]);
        KVFileReaderWriter kvrw = new KVFileReaderWriter(args[1]);

        txtrw.open(TxtFileReaderWriter.READ_MODE);
        kvrw.open(KVFileReaderWriter.WRITE_MODE);
        
        KV kk;
        while ((kk = txtrw.read()) != null) {
            kvrw.write(kk);
        }

        txtrw.close();
        kvrw.close();

        kvrw = new KVFileReaderWriter(args[1]);
        txtrw = new TxtFileReaderWriter(args[2]);

        txtrw.open(TxtFileReaderWriter.WRITE_MODE);
        kvrw.open(KVFileReaderWriter.READ_MODE);
        
        while ((kk = kvrw.read()) != null) {
            txtrw.write(kk);
        }

        txtrw.close();
        kvrw.close();
    }
}