package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Dominic Ventimiglia
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        while (_input.hasNextLine()) {
            String line = _input.nextLine();
            if (line.equals("")) {
                if (_input.hasNextLine()) {
                    line = _input.nextLine();
                    if (!line.equals("")) {
                        _output.println();
                    } else {
                        _output.println();
                        while (_input.hasNextLine() && line.equals("")) {
                            line = _input.nextLine();
                        }
                    }
                }
            }
            if (!line.equals("") && line.charAt(0) == '*') {
                setUp(machine, line);
            } else {
                printMessageLine(machine.convert(line));
            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            String alphabet = _config.nextLine().trim();
            int numrotors = _config.nextInt();
            int pawls = _config.nextInt();
            _alphabet = new Alphabet(alphabet);
            ArrayList<Rotor> allrotors = new ArrayList<Rotor>();
            if (!_config.hasNext()) {
                throw error("No rotors in config file");
            }
            while (_config.hasNext()) {
                Rotor rotor = readRotor();
                allrotors.add(rotor);
            }
            return new Machine(_alphabet, numrotors, pawls, allrotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String rotname = _config.next();
            String notch = _config.next();
            String cycles = "";
            while (_config.hasNext("\\s*\\(.+\\)")) {
                cycles += _config.next("\\s*\\(.+\\)");
            }
            Permutation rotperm = new Permutation(cycles, _alphabet);
            if (notch.charAt(0) == 'M') {
                return new MovingRotor(rotname, rotperm, notch.substring(1));
            } else if (notch.charAt(0) == 'N') {
                return new FixedRotor(rotname, rotperm);
            } else {
                return new Reflector(rotname, rotperm);
            }
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] newsettings = settings.trim().split("\\s+");
        String[] rotors = new String[M.numRotors()];
        for (int i = 1; i <= M.numRotors(); i++) {
            if (Arrays.asList(rotors).contains(newsettings[i])) {
                throw error("Duplicate rotors");
            } else {
                rotors[i - 1] = newsettings[i];
            }
        }
        M.insertRotors(rotors);
        M.setRotors(newsettings[M.numRotors() + 1]);
        String plugcycles = "";
        for (int i = M.numRotors() + 2; i < newsettings.length; i++) {
            char letter = newsettings[i].charAt(0);
            if (Character.compare(letter, '(') != 0) {
                M.alteralphabet(newsettings[i]);
                M.setRotors(newsettings[M.numRotors() + 1]);
            } else {
                plugcycles += newsettings[i];
            }
        }
        M.setPlugboard(new Permutation(plugcycles, _alphabet));
        if (!M.getimplemntedrotors(0).reflecting()) {
            throw error("First Rotor should be a reflector");
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        while (msg.length() > 5) {
            _output.append(msg.substring(0, 5) + " ");
            msg = msg.substring(5);
        }
        _output.append(msg);
        _output.println();
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;

}
