package enigma;

import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Dominic Ventimiglia
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        for (int i = 0; i + 2 < cycles.length(); i++) {
            if (_alphabet.contains(cycles.charAt(i))
                    && _alphabet.contains(cycles.charAt(i + 2))
                    && " ".compareTo(cycles.substring(i + 1, i + 2)) == 0) {
                throw error("Whitespace in middle of cycle");
            }
        }
        cyclesToHashmap(cycles);
    }

    /** Creates a new alphabet by taking in a letter
     * and setting the current alphabet to have its
     * zero index be that letter.
     * @param letter character for beginning of new alphabet
     */
    void movealphabet(char letter) {
        String modalpha = _alphabet.returnsave();
        int index = _alphabet.toInt(letter);
        String back = modalpha.substring(0, index);
        String front = modalpha.substring(index);
        front += back;
        Alphabet movedalphabet = new Alphabet(front);
        _newalphabet = movedalphabet;

    }

    /** Returns the new alphabet created by the
     * rinstellung parameters.
     * @return Alphabet representing altered alphabet
     */
    Alphabet getnewalphabet() {
        return _newalphabet;
    }

    /** sets _cycles and _reversecycles to be inverse hasmhmaps using
     * a string input in the form of "(cccc) (cc) ..." where the c's
     * are characters in ALPHABET.
     * @param cycles a string that represents the permutation's cycles
     */
    private void cyclesToHashmap(String cycles) {
        char firstofcycle = Character.MIN_VALUE;
        cycles = cycles.replace(" ", "");
        for (int i = 0; i + 1 < cycles.length(); i++) {
            int first = Character.compare(cycles.charAt(i), '(');
            int second = Character.compare(cycles.charAt(i + 1), ')');
            int spec1 = Character.compare(cycles.charAt(i), ')');
            int spec2 = Character.compare(cycles.charAt(i + 1), '(');
            if (first == 0 && second == 0) {
                throw error("Invalid cycle format");
            } else {
                char key = cycles.charAt(i);
                char val = cycles.charAt(i + 1);
                if (_cycles.containsKey(key) || _cycles.containsKey(val)) {
                    throw error("multiple mapping for same character");
                }
                if (spec1 != 0 && spec2 != 0 && first != 0 && second != 0) {
                    if (!_alphabet.contains(key) || !_alphabet.contains(val)) {
                        throw error("character(s) not in alphabet");
                    }
                    if (firstofcycle == Character.MIN_VALUE) {
                        throw error("malformed cycle string");
                    }
                    _cycles.put(key, val);
                    _reversecycles.put(val, key);
                } else if (first == 0 && second != 0) {
                    if (!_alphabet.contains(val)) {
                        throw error("character(s) not in alphabet");
                    }
                    firstofcycle = val;
                } else if (first != 0 && second == 0) {
                    if (!_alphabet.contains(key)) {
                        throw error("character(s) not in alphabet");
                    }
                    if (firstofcycle == Character.MIN_VALUE) {
                        throw error("malformed cycle string");
                    }
                    _cycles.put(key, firstofcycle);
                    _reversecycles.put(firstofcycle, key);
                    firstofcycle = Character.MIN_VALUE;
                }
            }
        }
        for (int i = 0; i < _alphabet.size(); i++) {
            char letter = _alphabet.toChar(i);
            if (_cycles == null || !_cycles.containsKey(letter)) {
                _cycles.put(letter, letter);
                _reversecycles.put(letter, letter);
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        cyclesToHashmap(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        int value = wrap(p);
        char newchar = _alphabet.toChar(value);
        char permutedchar =  permute(newchar);
        return _alphabet.toInt(permutedchar);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        int value = wrap(c);
        char newchar = _alphabet.toChar(value);
        char permutedchar =  invert(newchar);
        return _alphabet.toInt(permutedchar);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_alphabet.contains(p)) {
            throw error("character not in alphabet");
        }
        return _cycles.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_alphabet.contains(c)) {
            throw error("character not in alphabet");
        }
        return _reversecycles.get(c);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (char i : _cycles.keySet()) {
            if (_cycles.get(i) == i) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /**a hashmap whose key is a character and value associate
     * is encrypted character correlated to that key. This
     * hashmap is in relation doing the forward encryption
     * process.*/
    private HashMap<Character, Character>  _cycles = new HashMap<>();

    /** A hashmap that is identical to the _cycles hashmap
     * with the keys and values inverted.
     */
    private HashMap<Character, Character> _reversecycles = new HashMap<>();

    /** The new alphabet cause by
     * ringstellung. */
    private Alphabet _newalphabet;




}
