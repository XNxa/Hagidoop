# Hagidoop

Hagidoop is a Java implementation of the Map-Reduce model, inspired by Hadoop. The project includes the Hagidoop Distributed File System (HDFS) and a service for distributed and parallel execution of Map and Reduce tasks.

## Introduction

The project was undertaken during my second year in Software Engineering School at ENSEEIHT. 
It serves as an exploration of the concepts learned in the Concurrent and Distributed Programming courses.

**Disclaimer:**
As this project is developed as part of academic coursework and is intended solely for educational purposes, it is not designed or tested for deployment in real-world applications. 

## Project Structure

The project is organized into the following directories:

- `config`: Configuration files for Hagidoop (e.g., node list).
- `data`: Contains data files for processing, and a script to generate large files.
- `doc`: Project related files.
- `scripts`: Deployment scripts for launching and cleaning up daemons on machines.
- `src/application`: Application sources, including WordCount implementations.
- `src/config`: Classes for configuring Hagidoop (e.g., reading node lists).
- `src/daemon`: Classes implementing Hagidoop components (Worker, JobLauncher).
- `src/hdfs`: Classes implementing the Hagidoop Distributed File System.
- `src/interface`: Interfaces given by the teacher for the Map-Reduce programming model and I/O operations.
- `src/io`: Classes implementing I/O operations based on the provided interfaces.

## Usage
1. **Configuration:**
    - Describe the configuration in a file in the `config` folder.
    - Following the syntax : IpAdress:HdfsPort:WorkerPort

2. **Launch HDFS:**
    - Launch the Hagidoop Distributed File System (HDFS) on different machines specified in the configuration file.
    - Run the following command on each machine:
        ```bash
        java -cp bin hdfs.HdfsServer <port>
        ```
        Or use the provided deployment script:
        ```bash
        ./launch_hdfs
        ```

3. **Write a File to Remote Machines with HDFS:**
    - Use HDFS to write a file to the distributed file system. For example:
        ```bash
        java -cp bin HdfsClient write <txt|kv> <file>
        ```

4. **Launch Workers:**
    - Launch the Worker daemons on the remote machines.
    - Run the following command on each machine:
        ```bash
        java -cp bin daemon.WorkerImpl <port>
        ```
        Or use the provided deployment script:
        ```bash
        ./launch_app
        ```

5. **Run Map-Reduce:**
    - Execute the Map-Reduce job using the provided `JobLauncher` class.
    - Run the following command:
        ```bash
        java -cp bin application.MyMapReduce
        ```

