package config;

public class Machine {
        private String ipString;
        private int port;

        public Machine(String ipString, int port) {
            this.ipString = ipString;
            this.port = port;
        }

        public String getIp() {
            return ipString;
        } 

        public int getPort() {
            return port;
        }
        
    }