import hdfs.KVFileReaderWriter;
import interfaces.KV;

public class testKVReadWrite {
    
    public static void main(String[] args) {
        
        
        
        KVFileReaderWriter rw = new KVFileReaderWriter("salut.txt");

        KV kk;

        rw.open(KVFileReaderWriter.READ_MODE);
        while ((kk = rw.read()) != null) {
            System.out.println(kk);
        }
        rw.close();
    }

}
