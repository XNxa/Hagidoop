package config;

public class Machine {
        private String ipString;
        private int portHdfs;
        private int portRmi;

        public Machine(String ipString, int portHdfs, int portRmi) {
            this.ipString = ipString;
            this.portHdfs = portHdfs;
            this.portRmi = portRmi;
        }

        public String getIp() {
            return ipString;
        } 

        public int getPortHdfs() {
            return portHdfs;
        }

        public int getPortRmi() {
            return portRmi;
        }
        
    }