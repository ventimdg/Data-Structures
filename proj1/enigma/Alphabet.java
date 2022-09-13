package enigma;

import java.util.ArrayList;
import static enigma.EnigmaException.*;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Dominic Ventimiglia
 */
class Alphabet {
    /** An arraylist containing each individual chracter
     * in an alphabet.
     */
    private ArrayList<Character> _alphabet;

    /** A new alphabet containing CHARS. The K-th character has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _saved = chars;
        _alphabet = new ArrayList<Character>(chars.length());
        for (int i = 0; i < chars.length(); i++) {
            if (_alphabet.contains(chars.charAt(i))) {
                throw error("Alphabet contains duplicate characters");
            } else {
                _alphabet.add(chars.charAt(i));
            }
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _alphabet.size();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _alphabet.contains(ch);
    }

    /** returns the intial string input into the constructor. */
    String returnsave() {
        return _saved;
    }


    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        return _alphabet.get(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        return _alphabet.indexOf(ch);
    }

    /** The string of characters inputted into the constructor. */
    private String _saved;

}
