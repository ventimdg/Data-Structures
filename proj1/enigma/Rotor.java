package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Dominic Ventimiglia
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _setting = 0;
        _alphabetsize = perm.alphabet().size();
    }

    /** Function that takes in a character and uses
     * that character to create a new alphabet for the rotor
     * that is moved. Also makes the program know that a ringstellung
     * config file was used.
     * @param character character to move new alphabet
     */
    void changealphabet(char character) {
        int changed = _permutation.alphabet().toInt(character);
        _permutation.movealphabet(character);
        _ringstellung = true;
    }

    /** takes in an integer representing how many
     * spots the alphabet has moved and subtracts it
     * from the all the current notch numbers so the nothces
     * represent their new place correctly.
     * @param displacement int representing how many
     * characters the alphabet was moved
     */
    void notchreplacer(int displacement) {
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return  _setting;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        _setting = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        if (_ringstellung) {
            _setting = _permutation.getnewalphabet().toInt(cposn);
        } else {
            _setting = alphabet().toInt(cposn);
        }
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int newletterindex = _permutation.wrap(p + _setting);
        newletterindex = _permutation.permute(newletterindex);
        return _permutation.wrap(newletterindex - _setting);
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int newletterindex = _permutation.wrap(e + _setting);
        newletterindex = _permutation.invert(newletterindex);
        return _permutation.wrap(newletterindex - _setting);
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {
        return false;
    }

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
        if (rotates()) {
            if (_setting + 1 == _alphabetsize) {
                _setting = 0;
            } else {
                this._setting += 1;
            }
        }
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    /** The state the rotor is in, also known
     * as the number of times it has spun, in relation
     * to its zero state.
     */
    private int _setting;

    /** The size of the alphabet to the
     * correlated permutation.
     */
    private  int _alphabetsize;

    /** Tells whether the input files had an extra
     * input for ringstelling.*/
    private boolean _ringstellung = false;



}
