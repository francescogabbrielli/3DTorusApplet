package it.francescogabbrielli.apps.torusapplet;

import Jama.Matrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

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
        try {

            Properties properties = new Properties();
            try {
                properties.load(Main.class.getResourceAsStream("/app.properties"));
            } catch(IOException e) {
                e.printStackTrace();
            }

            SimulationThread st = new SimulationThread();
            SimulationSystem system = st.getSystem();

            int skip = 0;
            long time = 1000;
            double radius = system.getRadius();
            boolean translate = false;
            File file = new File("coords.txt");
            for (int i = 0; i < args.length; i++) {
                if (skip > 0) {
                    skip--;
                    continue;
                }
                switch (args[i]) {
                    case "-f":
                        file = new File(args[i + 1]);
                        skip = 1; break;
                    case "-n":
                        system.setAtomsNumber(Integer.parseInt(args[i + 1]));
                        skip = 1; break;
                    case "-d":
                        system.setDensity(Double.parseDouble(args[i + 1]));
                        skip = 1; break;
                    case "-t":
                        system.setApplTemp(Double.parseDouble(args[i + 1]));
                        skip = 1; break;
                    case "--time":
                        time = Long.parseLong(args[i + 1]);
                        skip = 1; break;
                    case "--translate":
                        translate = true;
                        break;
                    case "--transform":
                        system.setTransform(Matrix.constructWithCopy(new double[][] {
                                new double[] {Double.parseDouble(args[i + 1]), Double.parseDouble(args[i + 2]), Double.parseDouble(args[i + 3])},
                                new double[] {Double.parseDouble(args[i + 4]), Double.parseDouble(args[i + 5]), Double.parseDouble(args[i + 6])},
                                new double[] {Double.parseDouble(args[i + 7]), Double.parseDouble(args[i + 8]), Double.parseDouble(args[i + 9])}
                        }));
                        skip = 9; break;
                    case "-h":
                    case "--help":
                        System.out.println("\n" + properties.getProperty("title") + " v" +properties.getProperty("version"));
                        System.out.println("\nParameters:");
                        System.out.printf("%25s\tfilename (default is coords.txt)%n", "-f <name>");
                        System.out.printf("%25s\tatoms number (default is 20)%n", "-n <nr>");
                        System.out.printf("%25s\tapplied temperature (default is 150)%n", "-t <temperature>");
                        System.out.printf("%25s\tdensity (default is calculated on atoms number)%n", "-d <density>");
                        System.out.printf("%25s\tsimulation time in milliseconds (default is 1000ms)%n", "--time <ms>");
                        System.out.printf("%25s\ttransform (default is 5.3 0 0 0 5.3 0 0 0 5.3)%n", "--transform <matrix>");
                        System.out.printf("%25s\ttranslate to origin%n", "--translate");
                        System.out.printf("%25s\tthis help message%n", "-h, --help");
                        System.out.println();
                        System.exit(-1);
                    default:
                        System.out.printf("WARNING! Argument not recognized: %s%n", args[i]);
                }
            }

            st.start();
            Thread.sleep(time);
            st.setRunning(false);

            if (system.isExploded()) {
                System.out.println("System exploded!");
            } else {
                System.out.printf("Exporting to %s - radius=%f; translate=%s", file, radius, translate);
                system.export(file, translate, radius);
                //runVCS(file, file.getParentFile());
            }

        } catch(Exception e) {
            System.err.println("Export error: "+e.getMessage());
        }

    }

    public static String getFilenameWithoutExtension(File f) {
        String n = f.getName();
        int dot = n.lastIndexOf(".");
        if(dot>0)
            n = n.substring(0, dot);
        return n;
    }

    public static void runVCS(File f, File homeDir) {

        try {
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec(String.format("vcs < %s", f.getName()), null, homeDir);
            p.waitFor();
            logErrors(p);

            System.out.println("VCS OK");

            String n = getFilenameWithoutExtension(f);
            p = runtime.exec(String.format("torus_munge < %s_unmunged.fe > %s.fe", n, n), null, homeDir);
            p.waitFor();
            logErrors(p);

            System.out.println("TORUS_MUNGE OK");

            /*ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "evolver", n+".fe");
            pb.directory(homeDir);
            p = pb.start();
            DataInputStream in = new DataInputStream(p.getInputStream());
            String line;

            while((line=in.readLine())!=null) {
                System.out.println(line);
            }
            new DataOutputStream(p.getOutputStream()).writeUTF("\n");
            System.out.println("EVOLVER OK");*/
        } catch(Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static void logErrors(Process p) throws IOException {
        try (BufferedReader r  = new BufferedReader(new InputStreamReader(p.getErrorStream()))) {
            String line;
            while((line = r.readLine())!=null)
                System.err.println(line);
        }
    }

}
