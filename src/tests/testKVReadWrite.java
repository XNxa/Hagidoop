package tests;

import interfaces.KV;
import io.KVFileReaderWriter;
import io.TxtFileReaderWriter;

public class testKVReadWrite {
    
    public static void main(String[] args) {
        
        TxtFileReaderWriter txtrw = new TxtFileReaderWriter("bonjour.txt");
        KVFileReaderWriter kvrw = new KVFileReaderWriter("salut.txt");

        txtrw.open(TxtFileReaderWriter.READ_MODE);
        kvrw.open(KVFileReaderWriter.WRITE_MODE);
        
        KV kk;
        while ((kk = txtrw.read()) != null) {
            kvrw.write(kk);
        }

        txtrw.close();
        kvrw.close();

        txtrw = new TxtFileReaderWriter("bonjour2.txt");
        txtrw.open(TxtFileReaderWriter.WRITE_MODE);
        kvrw.open(KVFileReaderWriter.READ_MODE);
        
        while ((kk = kvrw.read()) != null) {
            txtrw.write(kk);
        }

        txtrw.close();
        kvrw.close();

    }

}
