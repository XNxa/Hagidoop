package io;

import interfaces.FileReaderWriter;
import interfaces.KV;

public interface SizedFileReaderWriter extends FileReaderWriter  {
    
    public static final String READ_MODE = "READ";
    public static final String WRITE_MODE = "WRITE";

    public long size(KV kv);
}
