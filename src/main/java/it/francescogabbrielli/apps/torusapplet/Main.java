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
        AppletViewer v = new AppletViewer(new TorusApplet()) ;
		v.start() ;
    }

}
