package daemon;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import config.Config;
import config.Machine;
import interfaces.MapReduce;
import io.KVFileReaderWriter;

public class JobLauncher {

	public static void startJob (MapReduce mr, int format, String fname) {
		// lancer les map en appelant runmap sur les workers
		Config config = new Config();
		for (Machine m : config) {
			String URL = "//" + m.getIp() + ":" + m.getPortRmi() + "/HagidoopWorker";
			try {
				Worker w = (Worker) Naming.lookup(URL);
				KVFileReaderWriter reader = new KVFileReaderWriter(fname);
				w.runMap(mr, reader, null);
			} catch (MalformedURLException | RemoteException | NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// lancer le reduce sur cette machine
		
	}
}
