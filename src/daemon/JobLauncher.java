package daemon;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import config.Config;
import config.Machine;
import interfaces.MapReduce;
import interfaces.NetworkReaderWriter;
import io.KVFileReaderWriter;
import io.KVNetworkReaderWriter;
import io.SizedFileReaderWriter;
import io.TxtFileReaderWriter;

public class JobLauncher {

	public static void startJob (MapReduce mr, int format, String fname) {

		// Create writer of the results
		KVFileReaderWriter resultsWriter = new KVFileReaderWriter("results_" + fname);
		resultsWriter.open(SizedFileReaderWriter.WRITE_MODE);

		// lancer les map en appelant runmap sur les workers
		Config config = new Config();
		int i = 0;
		for (Machine m : config) {
			String URL = "//" + m.getIp() + ":" + m.getPortRmi() + "/HagidoopWorker";
			try {
				// Get the stub
				Worker w = (Worker) Naming.lookup(URL);

				// Open the reader
				KVFileReaderWriter reader = new KVFileReaderWriter(fname + "_" + i++);
				reader.open(SizedFileReaderWriter.READ_MODE);

				KVNetworkReaderWriter writer = new KVNetworkReaderWriter(, i)

				// Run the remote method
				w.runMap(mr, reader, writer);

				// Close the writer and the reader
				reader.close();
				writer.closeClient();
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	// Close the writer of the results
	resultsWriter.close();
	}

}
