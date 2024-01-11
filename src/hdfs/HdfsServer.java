package hdfs;

import java.io.EOFException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import config.Project;
import interfaces.KV;
import io.KVFileReaderWriter;
import io.SizedFileReaderWriter;

/** Serveur Hdfs, dont plusieurs instances seront lancés sur plusieurs machines
différentes. */
public class HdfsServer {

    /** Lancer un serveur HDFS.
     * @param args : les arguments en ligne de commande, qui doit ne contenir qu'un
     * argument.
    */
    public static void main(String[] args) {
        if (args.length != 1) {
            usage();
        } else {
            int port = Integer.parseInt(args[0]); //
            try (ServerSocket ss = new ServerSocket(port)) {
                while (true) {
                    Slave sl = new Slave(ss.accept());
                    sl.start();
                }
            } catch (IOException e) {
                System.err.println("Unable to launch Server :\n" + e.getMessage());
            } catch (NumberFormatException e) {
                usage();
            }
        }
    }


    /* Usage de la classe HdfsServer. */
    private static void usage() {
        System.out.println("Usage : java HdfsServer <PortNumber>");
    }

    /* Esclave du socket server, thread qui sera lancé plusieurs fois par la classe
     * HdfsServer.
    */
    private static class Slave extends Thread {

        /** Socket accepté par le serveur. */
        private Socket s;
        

        /* Construire  un esclave à partir d'un socket accepté par le serveur.
         * @param s : socket accepté par le serveur.
        */
        Slave(Socket s) {
            this.s = s;
        }

        @Override
        public void run() {
            String fname;
            ObjectOutputStream os;
            File[] matchingFiles;
            File path = new File(Project.PATH_HDFS);
            KV kv;
            if (!path.exists()) {
                path.mkdir();
            }

            try {
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                switch (is.readInt()) {
                    case HdfsClient.HDFS_WRITE:
                        fname = (String) is.readObject();
                        
                        KVFileReaderWriter file = new KVFileReaderWriter(path.getAbsolutePath()+File.separator+fname);
                        file.open(SizedFileReaderWriter.WRITE_MODE);

                        
                        while (true) {
                            try {
                                kv = (KV) is.readObject();
                                if (kv == null) {break;}
                                file.write(kv);
                            } catch (EOFException e) {
                                break;
                            }
                        }
                        file.close();
                        break;

                    case HdfsClient.HDFS_READ:
                        os = new ObjectOutputStream(s.getOutputStream());

                        fname = (String) is.readObject();
                        System.out.println("Looking for " + fname + " in: " + path);

                        matchingFiles = path.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.startsWith(fname);
                            }
                        });

                        System.err.println("Matching files: " + Arrays.toString(matchingFiles));
                        
                        if (matchingFiles == null || matchingFiles.length < 1) {
                            os.writeInt(-1);
                        } else { // On a un ou plusieurs fragments sur la machine
                            // Envoyer le numéro de fragment du premier fichier trouvé.
                            File f = matchingFiles[0];
                            String[] nameSplit = f.getName().split("_");
                            int fragmentNumber = Integer.parseInt(nameSplit[nameSplit.length-1]);
                            
                            // Envoyer le numéro du fragment.
                            System.out.println("Sending fragment number " + fragmentNumber);
                            os.writeInt(fragmentNumber);
                            KVFileReaderWriter reader = new KVFileReaderWriter(f.getAbsolutePath());
                            reader.open(SizedFileReaderWriter.READ_MODE);

                            // Envoyer le fragment.
                            while ((kv = reader.read()) != null) {
                                os.writeObject(kv);
                            }
                            reader.close();
                        }
                        os.close();
                        break;
                    case HdfsClient.HDFS_DELETE:
                        os = new ObjectOutputStream(s.getOutputStream());

                        fname = (String) is.readObject();
                        System.out.println("Looking for " + fname + " in: " + path);

                        matchingFiles = path.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.startsWith(fname);
                            }
                        });

                        System.err.println("Matching files: " + Arrays.toString(matchingFiles));
                        
                        if (matchingFiles == null || matchingFiles.length < 1) {
                            os.writeInt(-1);
                        } else { // On a un ou plusieurs fragments sur la machine
                            File fileToDelete = matchingFiles[0];
                            boolean deleted = fileToDelete.delete();
                            if (!deleted) {
                                os.writeInt(-2);
                            } else {
                                os.writeInt(0);
                            }
                        }
                        os.close();
                        break;
                    default:
                        break;
                }
                
                is.close();
                s.close();
            } catch (ClassNotFoundException e) {
                System.err.println("Class of a serialized object cannot be found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
