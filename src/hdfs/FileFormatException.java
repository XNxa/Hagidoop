package hdfs;

public class FileFormatException extends Exception {
    
    public FileFormatException(String line, int lineNumber) {
        super("KV non reconnu ligne " + lineNumber + " : " + line);
    }

}
