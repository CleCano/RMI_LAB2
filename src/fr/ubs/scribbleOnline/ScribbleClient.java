package fr.ubs.scribbleOnline;

import java.net.MalformedURLException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import fr.ubs.scribble.FiguresCanvas;
import fr.ubs.scribble.ScribbleFrame;


public class ScribbleClient {
    String host;
    int port;
    ScribbleFrame frame;
    Registry registry;
    FiguresBox figuresBox;
    FiguresCanvas canvas;
    String url;


    public FiguresCanvas getCanvas() {
        return canvas;
    }

    public FiguresBox getFiguresBox() {
        return figuresBox;
    }

    public void setFiguresBox(FiguresBox figuresBox) {
        this.figuresBox = figuresBox;
    }

    public static void main(String[] args) throws NumberFormatException, RemoteException, InstantiationException, IllegalAccessException, MalformedURLException, ClassNotFoundException {
        testLaunch(args);

    }
    public void setCanvas(FiguresCanvas canvas) {
        this.canvas = canvas;
    }
    public ScribbleClient(String host_, int port_,String url_) throws RemoteException, InstantiationException, IllegalAccessException, MalformedURLException, ClassNotFoundException {
        this.host = host_;
        this.port = port_;
        this.url = url_;
        this.registry = LocateRegistry.getRegistry(this.host, this.port);
        
   

        try {
            this.figuresBox = (FiguresBox) registry.lookup("FiguresBox");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            this.frame = new ScribbleFrame(this);
            frame.pack();
            frame.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

    public static void testLaunch(String[] args) throws NumberFormatException, RemoteException, InstantiationException, IllegalAccessException, MalformedURLException, ClassNotFoundException {
        if (Arrays.asList(args).contains("-h") || Arrays.asList(args).contains("--help") || args.length != 2
                || !args[1].matches("[0-9]+")) {
            printUsage();
        }
        //si classpath possede triangle 
        String url ="";
        
      
        ScribbleClient client = new ScribbleClient(args[0], Integer.parseInt(args[1]),url);
        client.figuresBox.registerCallback(new CallbackImpl(client));
    }

    public static void printUsage() {
        System.out
                .println("Usage: java -cp Client.jar:triangles.jar -Djava.java.rmi.server.codebase=\"http://people.irisa.fr/Pascale.Launay/pub/triangle.jar\" fr.ubs.scribbleOnline.ScribbleClient <host> <port>");
        System.err.println("With: ");
        System.out.println("\thost: host name of the client");
        System.out.println("\tport: port number of the server");
        System.exit(-1);
    }
}
