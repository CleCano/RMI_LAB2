package fr.ubs.scribbleOnline;

import java.util.Arrays;

import javax.swing.SwingUtilities;

import fr.ubs.scribble.ScribbleFrame;

public class ScribbleClient {
    String host;
    int port;
    ScribbleFrame frame;

    public static void main(String[] args) {
        testLaunch(args);

    }

    public ScribbleClient(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            ScribbleFrame frame = new ScribbleFrame();
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.frame.getCa
    }

    public static void testLaunch(String[] args) {
        if (Arrays.asList(args).contains("-h") || Arrays.asList(args).contains("--help") || args.length != 2
                || !args[1].matches("[0-9]+")) {
            printUsage();
        }

        if (!System.getProperty("java.class.path").contains("triangles.jar")) {
            System.out.println("You must run this program with triangles.jar in the classpath");
            printUsage();
        }
    }

    public static void printUsage() {
        System.out
                .println("Usage: java -cp Client.jar:triangles.jar fr.ubs.scribbleOnline.ScribbleClient <host> <port>");
        System.err.println("With: ");
        System.out.println("\thost: host name of the client");
        System.out.println("\tport: port number of the server");
        System.exit(-1);
    }
}
