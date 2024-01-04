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
            String filename;
            ObjectOutputStream os;
            File[] matchingFiles;
            File path = new File(Project.PATH_HDFS);
            if (!path.exists()) {
                path.mkdir();
            }

            try {
                ObjectInputStream is = new ObjectInputStream(s.getInputStream());
                switch (is.readInt()) {
                    case HdfsClient.HDFS_WRITE:
                        filename = (String) is.readObject();
                        
                        KVFileReaderWriter file = new KVFileReaderWriter(path.getAbsolutePath()+File.separator+filename);
                        file.open(SizedFileReaderWriter.WRITE_MODE);

                        KV kv;
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
                        is.close();
                        break;

                    case HdfsClient.HDFS_READ:
                        os = new ObjectOutputStream(s.getOutputStream());
                        // Recevoir le nom du fichier
                        filename = (String) is.readObject();

                        // Récupérer le fichier
                        matchingFiles = path.listFiles(new FilenameFilter() {
                            public boolean accept(File dir, String name) {
                                return name.startsWith(filename);
                            }
                        });
                        if (matchingFiles.length < 1) {
                            // TODO : envoyer erreur ?

                        } else if (matchingFiles.length > 1) {
                            // TODO : envoyer erreur ?

                        } else {
                            String fnameWithFragmentNumber = matchingFiles[0].getName();

                            // Envoyer le numéro de fragment
                            String[] nameSplit = fnameWithFragmentNumber.split("_");
                            if (nameSplit.length < 1) {
                                // TODO : envoyer erreur ?
                            } else {
                                int fragmentNumber = Integer.parseInt(nameSplit[nameSplit.length -1]);
                                os.writeInt(fragmentNumber);
                            }


                            // Envoyer le fragment. 
                            // TODO : on a potentiellement plus personne pour lire, il se passe quoi dans ce cas ?
                            // Envoie sous quelle forme ? des KVs ? des lignes directement ?
                                                         
                            os.close();
                            is.close();

                        }
                        break;
                    case HdfsClient.HDFS_DELETE:
                        os = new ObjectOutputStream(s.getOutputStream());

                        String fname = (String) is.readObject();
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
                            is.close();
                        }
                        break;
                    default:
                        break;
                }
                
                
                s.close();
            } catch (ClassNotFoundException e) {
                System.err.println("Class of a serialized object cannot be found.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
