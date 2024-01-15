package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.DataFormatException;


public class Config implements Iterable<Machine> {

    private static final String CONFIG_FOLDER = "config";
    private static final String CONFIG_FILE = "config1";
    private static final String PATH = CONFIG_FOLDER + File.separator + CONFIG_FILE;

    private List<Machine> machines;

    public Config() {
        machines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
            String line;
            String[] tab;
            int i = 0;
            while ((line = br.readLine()) != null) {
                i++;
                tab = line.split(":");
                if (tab.length != 3) {
                    throw new DataFormatException("Bad format line " + i + " : " + line);
                }
                machines.add(new Machine(tab[0], Integer.parseInt(tab[1]), Integer.parseInt(tab[2])));
            }
        } catch (NumberFormatException e) {
            System.err.println("Config file with wrong format");
        } catch (DataFormatException e) {
            System.err.println(e.getMessage());
        } catch (FileNotFoundException e) {
            System.err.println("Unable to find config file :" + PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getNumberOfWorkers() {
        return machines.size();
    }

    @Override
    public Iterator<Machine> iterator() {
        return machines.iterator();
    }

}
