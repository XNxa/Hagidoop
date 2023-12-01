package hdfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import config.Config;
import config.Machine;
import interfaces.FileReaderWriter;
import interfaces.KV;

public class HdfsClient {

	private final static int HDFS_WRITE = 0; 
	
	private static void usage() {
		System.out.println("Usage: java HdfsClient read <file>");
		System.out.println("Usage: java HdfsClient write <txt|kv> <file>");
		System.out.println("Usage: java HdfsClient delete <file>");
	}
	
	public static void HdfsDelete(String fname) {
	}
	
	public static void HdfsWrite(int fmt, String fname) {
		List<KV> list = new LinkedList<KV>();
		try (BufferedReader br = new BufferedReader(new FileReader(fname))) {
			String line;
			int nbOfLines = 0;
			while ((line = br.readLine()) != null) {
				nbOfLines++;
				list.add(new KV(Integer.toString(nbOfLines), line));
			}
		} catch (FileNotFoundException e) {
			System.err.println("File " + fname + " not found !");
			System.exit(1);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		Config config = new Config();

		int nbOfKvs = list.size();
		int nbOfWorkers = config.getNumberOfWorkers();
		int sizeOfFragment = nbOfKvs / nbOfWorkers;
		
		int i = 0;;
		for (Machine machine : config) {
			// Ouvrir une socket
			try (Socket s = new Socket(machine.getIp(), machine.getPort())) {
				OutputStream os = s.getOutputStream();
				// Envoyer un flag HDFS_WRITE
				os.write(HDFS_WRITE);
				// Envoyer nom fichier + n° Fragment
				os.write(fname+"_"+(i++));
				// TODO: Finish that
			
			} catch (IOException e) {
				System.err.println();
			}

			// Envoyer la taille du fragement
			// Envoyer la sauce
		}

	}

	public static void HdfsRead(String fname) {
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
