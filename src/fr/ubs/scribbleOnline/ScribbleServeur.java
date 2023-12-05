package fr.ubs.scribbleOnline;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;




public class ScribbleServeur extends RemoteServer{
    private int port;
    private FiguresBox figuresBox;
    private Remote stub;
    private Registry registry;
    public static void main(String[] args) {
        testlaunch(args);

    }
    public static void testlaunch(String[] args){
        if (args.length != 1 || args[0].equals("-h") || args[0].equals("--help") || !args[0].matches("[0-9]+")) {
            printUsage();   
        }
    }
    public ScribbleServeur(int port) throws RemoteException{
        this.port = port;
        this.figuresBox = new FiguresBoxImpl(); 
        this.stub = UnicastRemoteObject.exportObject(figuresBox, this.port);
        this.registry = LocateRegistry.createRegistry(this.port);
        this.registry.rebind("FiguresBox", this.stub);
        System.out.println("Serveur is ready on port "+this.port);
    }
    public static void printUsage(){
        System.out.println("Usage: java -jar Serveur.jar <port> ");
        System.err.println("With: ");
        System.out.println("\tport: port number to listen on");
        System.exit(-1);
    }
}
