import java.io.Reader;
import java.io.IOException;


/** Translating Reader: a stream that is a translation of an
*  existing reader.
*  @author Dominic Ventimiglia
*
*  NOTE: Until you fill in the right methods, the compiler will
*        reject this file, saying that you must declare TrReader
* 	     abstract.  Don't do that; define the right methods instead!
*/
public class TrReader extends Reader {
    /** A new TrReader that produces the stream of characters produced
     *  by STR, converting all characters that occur in FROM to the
     *  corresponding characters in TO.  That is, change occurrences of
     *  FROM.charAt(i) to TO.charAt(i), for all i, leaving other characters
     *  in STR unchanged.  FROM and TO must have the same length. */

    public Reader _string;
    public String _from;
    public String _to;

    public TrReader(Reader str, String from, String to) {
        _string = str;
        _from = from;
        _to = to;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int answer = _string.read(cbuf, off, len);
        for (int i = 0; i < cbuf.length; i++) {
            int inside = _from.indexOf(cbuf[i]);
            if (inside > -1) {
                cbuf[i] = _to.charAt(inside);
            }
        }
        return answer;
    }

    @Override
    public void close() throws IOException {
    }

}
