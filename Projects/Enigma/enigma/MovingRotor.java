package enigma;

import java.util.ArrayList;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Dominic Ventimiglia
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        _notches = new ArrayList<Character>();
        _notchnumbers = new ArrayList<Integer>();
        if (notches.equals("")) {
            _notches = null;
            _notchnumbers = null;
        }
        if (_notches != null) {
            for (int i = 0; i < notches.length(); i++) {
                if (_notches.contains(notches.charAt(i))) {
                    throw error("Notch list contains duplicate notches");
                } else {
                    _notches.add(notches.charAt(i));
                }
            }
            for (char word : _notches) {
                _notchnumbers.add(perm.alphabet().toInt(word));
            }
        }
    }

    @Override
    void notchreplacer(int displacement) {
        if (!_beenreplaced) {
            for (int i = 0; i < _notchnumbers.size(); i++) {
                int replaced = _notchnumbers.get(i) - displacement;
                replaced = permutation().wrap(replaced);
                _notchnumbers.set(i, replaced);
                _beenreplaced = true;
            }
        }
    }


    @Override
    boolean atNotch() {
        if (_notchnumbers == null) {
            return false;
        }
        return _notchnumbers.contains(this.setting());
    }


    @Override
    public boolean rotates() {
        return true;
    }


    /** an arraylist consisting of the characters
     *  of all notches on a moving rotor.
     */
    private ArrayList<Character> _notches;

    /** an arraylist consisting of the integer values
     *  of all notches on a moving rotor.
     */
    private ArrayList<Integer> _notchnumbers;

    /** Determines whether ringstellung has already replaced
     * the notch yet or not. */
    private boolean _beenreplaced = false;

}
