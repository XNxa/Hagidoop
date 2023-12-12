package hdfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import config.Config;
import config.Machine;
import interfaces.FileReaderWriter;
import interfaces.KV;
import io.KVFileReaderWriter;
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
		// TODO: Chopper la taille du fichier et faire des envoi eu serveurs au fur et a mesure
		File file = new File(fname);
		long lengthOfFile = file.length();
		
		Config config = new Config();
		int nbOfWorkers = config.getNumberOfWorkers();
		
		long stopSending = lengthOfFile / nbOfWorkers;
		
		FileReaderWriter rw = (fmt == FileReaderWriter.FMT_KV) ? new KVFileReaderWriter(fname) : new TxtFileReaderWriter(fname);
		rw.open(TxtFileReaderWriter.READ_MODE);

		long bytesRead = 0L;
		while (bytesRead < stopSending) {
			// TODO ...!
		}


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


		int nbOfKvs = list.size();
		int sizeOfFragment = nbOfKvs / nbOfWorkers;

		int i = 0;;
		for (Machine machine : config) {
			boolean last = i==config.getNumberOfWorkers()-1;

			// Ouvrir une socket
			try (Socket s = new Socket(machine.getIp(), machine.getPort())) {
				OutputStream os = s.getOutputStream();
				// Envoyer un flag HDFS_WRITE
				os.write(HDFS_WRITE);
				// Envoyer la taille du fragement
				os.write(last? sizeOfFragment + nbOfKvs%nbOfWorkers : sizeOfFragment); 
				// Envoyer nom fichier + n° Fragment
				ObjectOutputStream obj_os = new ObjectOutputStream(os);
				obj_os.writeObject(fname+"_"+(i));
				// Envoyer la sauce
				int borneSup = (last) ? nbOfKvs : (i+1)*(sizeOfFragment);
				for (int j = i * sizeOfFragment; j < borneSup ; j++) {
					obj_os.writeObject(list.get(j));
				}

				obj_os.close();
				os.close();
			} catch (IOException e) {
				System.err.println(e.getMessage());
			}
			i++;
		}

	}

	public static void HdfsRead(String fname) {
		Config config = new Config();
		int nbOfWorkers = config.getNumberOfWorkers();

		// Flag

		// Nom du fichier a demander sans le _i

		// Recoit num du fragement

		// Recoit le fragement


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
