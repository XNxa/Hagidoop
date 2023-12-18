package hdfs;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
            String filename;
            try {
                ObjectInputStream obj_is = new ObjectInputStream(s.getInputStream());
                switch (obj_is.readInt()) {
                    case HdfsClient.HDFS_WRITE:
                        filename = (String) obj_is.readObject();
                        
                        // TODO : Changer le lieu de sauvegarde pour le 
                        // mettre dans tmp
                        KVFileReaderWriter file = new KVFileReaderWriter(filename);
                        file.open(SizedFileReaderWriter.WRITE_MODE);

                        KV kv;
                        while ((kv = (KV) obj_is.readObject()) != null) {
                            file.write(kv);
                        }

                        file.close();
                        obj_is.close();
                        
                        break;

                    case HdfsClient.HDFS_READ:
                        filename = (String) obj_is.readObject();

                        // Récupérer le fichier
                        File f = new File(Project.PATH_HDFS);
                        File[] matchingFiles = f.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.startsWith(filename);
                            }
                        });
                        if (matchingFiles.length < 1) {
                            // TODO : envoyer flag ?
                        } else if (matchingFiles.length > 1) {
                            // TODO : evoyer flag ?
                        } else {
                            // TODO : envoyer flag ?

                            // envoyer le fichier.
                        }
                    default:
                        
                        break;
                }


            } catch (ClassNotFoundException e) {
                System.err.println("Class of a serialized object cannot be found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
