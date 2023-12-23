package hdfs;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import config.Config;
import config.Machine;
import interfaces.FileReaderWriter;
import interfaces.KV;
import io.KVFileReaderWriter;
import io.SizedFileReaderWriter;
import io.TxtFileReaderWriter;

public class HdfsClient {

	public final static int HDFS_WRITE = 0;
	public final static int HDFS_READ = 1;
	public final static int HDFS_DELETE = 2;
	
	private static void usage() {
		System.out.println("Usage: java HdfsClient read <file>");
		System.out.println("Usage: java HdfsClient write <txt|kv> <file>");
		System.out.println("Usage: java HdfsClient delete <file>");
	}
	
	public static void HdfsDelete(String fname) {
	}
	
	public static void HdfsWrite(int fmt, String fname) {

		// Recuperer le nombre de machines
		Config config = new Config();

		int nbOfServer = config.getNumberOfWorkers();

		// Recuperer la taille du fichier
		File file = new File(fname);
		long sizeOfFile = file.length();

		// Diviser la taille par le nombre de machine
		long threshold = sizeOfFile / nbOfServer;

		// Ouvrir le fichier
		SizedFileReaderWriter rw = (fmt == FileReaderWriter.FMT_KV) ? 
			new KVFileReaderWriter(fname) : new TxtFileReaderWriter(fname);

		rw.open(SizedFileReaderWriter.READ_MODE);
		// Pour chaque machine
		int i = 0;
		for (Machine m : config) {
			// Se connecter
			try (Socket serverSocket = new Socket(m.getIp(), m.getPort())) {
				ObjectOutputStream os = 
					new ObjectOutputStream(serverSocket.getOutputStream());

				os.writeInt(HDFS_WRITE);
				os.writeObject(fname + "_" + i);

				long bytesSent = 0L;
				KV kv = null;
				// Tant qu'on ne depasse pas le seuil on envoi des kv
				while (bytesSent < threshold || i == nbOfServer - 1 && kv != null ) {
					kv = rw.read();
					bytesSent += rw.size(kv);
					os.writeObject(kv);
				}

				os.writeObject(null);
				os.close();

			} catch (Exception e) {
				// TODO: Valentin ... ???
			}
			i++;
		}
	}

	public static void HdfsRead(String fname) {
		// Recuperer la congiguration
		Config config = new Config();

		// Recevoir les fragments dans l'ordre.

		for (int i = 0 ; i < config.getNumberOfWorkers() ; i++ ) {
			for (Machine m : config) {
			
				try (Socket serverSocket = new Socket(m.getIp(), m.getPort())) {
					ObjectOutputStream os = 
						new ObjectOutputStream(serverSocket.getOutputStream());

					ObjectInputStream is = 
						new ObjectInputStream(serverSocket.getInputStream());

						
					// Envoyer le nom du fichier
					os.writeObject(fname);
						
					// Recevoir le numéro de fragment
					int fragment_number = is.readInt();

					if (fragment_number == i) {

						// Recevoir le fragment
						while (true) {
							// TODO : recevoir le fichier, sous quellle forme ? des kv ? des lignes directement ?
							break;
						}
						os.close();
						is.close();
						break; // On peut sortir de la boucle de recherche du fragment courant, et passer au fragment suivant.
					}

					os.close();
					is.close();;

				} catch (UnknownHostException e) {
					System.err.println(e.getMessage());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		// java HdfsClient <read|write> <txt|kv> <file>
		// appel des méthodes précédentes depuis la ligne de commande
		switch (args.length) {
			case 2:
				if (args[0].equals("read")) {
					HdfsRead(args[1]);
				} else if (args[0].equals("delete")) {
					HdfsDelete(args[1]);
				} else {
					usage();
				}
				break;
			case 3:
				if (args[0].equals("write")) {
					if (args[1].equals("txt")) {
						HdfsWrite(FileReaderWriter.FMT_TXT, args[2]);
					} else if (args[1].equals("kv")) {
						HdfsWrite(FileReaderWriter.FMT_KV, args[2]);
					} else {
						usage();
					}
				} else {
					usage();
				}
				break;
			default:
				usage();
				break;
		}
	}
}
