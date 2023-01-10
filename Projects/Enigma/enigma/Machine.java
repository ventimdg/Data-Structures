package enigma;

import java.util.ArrayList;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Dominic Ventimiglia
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _numrotors = numRotors;
        _pawls = pawls;
        _allrotors = new ArrayList<Rotor>(allRotors);
        _plugboard = new Permutation("", alpha);
    }

    /** takes in a string input of the Ringstellung string
     * and creates an alternate alphabet for each rotor while
     * also altering their notches.
     * @param input string representing Ringstellung
     */
    void alteralphabet(String input) {
        for (int i = 0; i < input.length(); i++) {
            int savedspot = _alphabet.toInt(input.charAt(i));
            _implementedrotors.get(i + 1).changealphabet(input.charAt(i));
            Rotor rotor = _implementedrotors.get(i + 1);
            if (rotor.rotates()) {
                rotor.notchreplacer(savedspot);
            }
        }
    }


    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numrotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _implementedrotors = new ArrayList<Rotor>();
        if (rotors.length != numRotors()) {
            throw error("Malformed file input");
        }
        for (String rotor : rotors) {
            for (Rotor arotor : _allrotors) {
                if (rotor.equalsIgnoreCase(arotor.name())) {
                    _implementedrotors.add(arotor);
                    break;
                }
            }
        }
        if (_implementedrotors.size() != _numrotors) {
            throw error("Incorrect Rotors in input file");
        }
        for (int i = 0, j = 0; i < _implementedrotors.size(); i++) {
            if (_implementedrotors.get(i).rotates()) {
                j++;
                if (j > _pawls) {
                    throw error("Too many moving rotors");
                }
            }
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() < _implementedrotors.size() - 1) {
            throw error("To little settings");
        }
        if (setting.length() > _implementedrotors.size() - 1) {
            throw error("Too many settings");
        }
        for (int i = 0; i < setting.length(); i++) {
            char letter = setting.charAt(i);
            if (!_alphabet.contains(letter)) {
                throw error("Rotor Setting not in alphabet");
            }
            _implementedrotors.get(i + 1).set(letter);
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        ArrayList<Rotor> advancing =  new ArrayList<Rotor>();
        if (_implementedrotors == null
                || _implementedrotors.size() != _numrotors) {
            throw error("Input file is incorrect");
        }
        for (int i = _implementedrotors.size() - 1; i >= 0; i--) {
            if (i == _implementedrotors.size() - 1) {
                advancing.add(_implementedrotors.get(i));
            }
            if (_implementedrotors.get(i).atNotch()
                    && _implementedrotors.get(i - 1).rotates()
                    && i != _numrotors - _pawls) {
                if (!advancing.contains(_implementedrotors.get(i))) {
                    advancing.add(_implementedrotors.get(i));
                }
                advancing.add(_implementedrotors.get(i - 1));
            }
        }
        for (Rotor rotor : advancing) {
            rotor.advance();
        }
        c = _plugboard.permute(c);
        for (int i = _implementedrotors.size() - 1; i >= 0; i--) {
            c = _implementedrotors.get(i).convertForward(c);
        }
        for (int i = 1; i < _implementedrotors.size(); i++) {
            c = _implementedrotors.get(i).convertBackward(c);
        }
        return _plugboard.permute(c);
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        msg = msg.replace(" ", "");
        String answer = "";
        for (int i = 0; i < msg.length(); i++) {
            char letter = msg.charAt(i);
            int number = _alphabet.toInt(letter);
            answer += _alphabet.toChar(convert(number));
        }
        return answer;
    }

    /** Returns the rotor at the specified spot in
     * the machine with the left most rotor being
     * at position 0.
     * @param r an integer
     * @return Rotor
     */
    Rotor getimplemntedrotors(int r) {
        return _implementedrotors.get(r);
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors. */
    private int _numrotors;

    /** Number of pawls. */
    private int _pawls;

    /** A collection of all the rotors
     * that can be used in the enigma machine. */
    private ArrayList<Rotor> _allrotors;

    /** A collection of all the implemented rotors
     * for the specific machine. */
    private ArrayList<Rotor> _implementedrotors;

    /** A permutation representing the input
     * changes caused by the plugboard. */
    private Permutation _plugboard;
}
