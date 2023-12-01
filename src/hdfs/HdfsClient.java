package hdfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import interfaces.FileReaderWriter;
import interfaces.KV;

public class HdfsClient {
	
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
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		int nbOfKvs = list.size();
		int nbOfWorkers = 4; // TODO : Ajouter un get du nombre de workers
		int sizeOfFragment = nbOfKvs / nbOfWorkers;
		
		for (int i = 0; i < nbOfWorkers; i++) {
			// TODO :
			// Ouvrir une socket
			// Envoyer un flag HDFS_WRITE
			// Envoyer nom fichier + n° Fragment
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
