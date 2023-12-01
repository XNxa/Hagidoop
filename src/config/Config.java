package config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Config implements Iterable<String> {

    private static final String CONFIG_FOLDER = "config";
    private static final String CONFIG_FILE = "config1";
    private static final String PATH = CONFIG_FOLDER + File.separator + CONFIG_FILE;

    private List<String> machines;

    public Config() {
        machines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                machines.add(line);
            }
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
    public Iterator<String> iterator() {
        return machines.iterator();
    }
}
