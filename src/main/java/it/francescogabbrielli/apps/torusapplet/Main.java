package it.francescogabbrielli.apps.torusapplet;

/**
 *
 * @author Francesco Gabbrielli
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //GUI run
        if (args.length == 0) {
            new AppletViewer(new TorusApplet()).start();
        }

        //Command Line run
        for (String arg : args) {

        }

    }

}
