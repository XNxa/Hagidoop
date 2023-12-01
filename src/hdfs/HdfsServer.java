package hdfs;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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
            // TODO unimplemented method stub
        }
    }

}
