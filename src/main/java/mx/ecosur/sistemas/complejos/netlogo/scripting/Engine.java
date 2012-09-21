package mx.ecosur.sistemas.complejos.netlogo.scripting;

import org.apache.commons.cli.*;
import org.nlogo.headless.HeadlessWorkspace;

import java.io.*;

/**
 * The Engine class uses the Apache CLI to interpret command line
 * arguments. Users should call the NetLogo scripting engine by
 * means of the following arguments:
 *
 * java -jar scripting-extension.jar -file script.nlogo
 *
 * Or, to invoke a headless console:
 *
 * java -jar scripting-extension.jar -console
 *
 * Requirements:
 *
 * NetLogo.jar must be in the same directory as this jarfile.
 *
 * Note: Passing in no arguments to the Engine, will result in the engine
 * attempting to read NetLogo commands in from standard input (STDIO).
 *
 * @author "Andrew Waterman" awaterma@ecosur.mx
 */
public class Engine {

    private static Options options;

    static {
        options = new Options();
        options.addOption("file",true,"file with NetLogo commands to be run");
        options.addOption("console",false,"accept commands from the console");
    }

    private File input;

    private BufferedReader reader;

    private HeadlessWorkspace workspace;

    public Engine() {
        input = null;
        workspace = HeadlessWorkspace.newInstance();
    }

    public Engine(File f) throws FileNotFoundException {
        this();
        input = f;
        reader = new BufferedReader(new FileReader(input));
    }

    public boolean ready() {
        boolean ret = true;
        if (reader != null) {
            try {
                ret = reader.ready();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ret;
    }

    public String nextLine() {
        if (reader != null) {
            try { return reader.readLine(); } catch (Exception e) { e.printStackTrace(); }
        }
        return null;
    }

    public String readLine() {
        String ret = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            ret = reader.readLine();
        } catch (Exception e) { System.out.println(e.getLocalizedMessage()); }
        return ret;
    }

    public void command (String command) {
        if (command.startsWith("open")) {
            String file = command.substring(5);
            workspace.open(file);
        } else if (command.equals("exit")) {
            System.exit(0);
        } else {
            workspace.command(command);
        }
    }

    public static void main(String[] args) throws ParseException, IOException {
        Engine engine;

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse (options, args);

        if (cmd.hasOption("file")) {
            engine = new Engine(new File(cmd.getOptionValue("file")));
            String line = engine.nextLine();
            while (line != null) {
                try { engine.command(line); } catch (Exception e) { System.out.println(e.getLocalizedMessage()); }
                line = engine.nextLine();
            }

        } else if (cmd.hasOption("console")) {
            engine = new Engine();
            while (engine.ready()) {
                System.out.print("observer> ");
                String command = engine.readLine();
                try {
                    engine.command(command);
                } catch (Exception e) { System.out.println(e.getLocalizedMessage()); }
            }
        } else {
            engine = new Engine();
            for (String s : args) {
                try { engine.command(s); } catch (Exception e) { System.out.println(e.getLocalizedMessage()); }
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        workspace.dispose();
    }
}
