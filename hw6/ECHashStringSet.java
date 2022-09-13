import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** A set of String values.
 *  @author Dominic Ventimiglia
 */
class ECHashStringSet implements StringSet {

    public ECHashStringSet () {
        buckets = (LinkedList<String>[]) new LinkedList[_numbuckets];
        for (int i = 0; i < _numbuckets; i++) {
            buckets[i] = new LinkedList<>();
        }
    }

    private LinkedList<String>[] buckets;

    private int _numbuckets = 1;

    private int _numitems = 0;

    private int bucketfinder(String s){
        return (s.hashCode() & 0x7fffffff) % _numbuckets;
        }


    private void resize() {
        List<String> prevset = asList();
        _numitems = 1;
        _numbuckets = _numbuckets * 2;
        buckets = (LinkedList<String>[]) new LinkedList[_numbuckets];
        for (int i = 0; i < _numbuckets; i++) {
            buckets[i] = new LinkedList<>();
        }
        for (String str : prevset) {
            put(str);
        }
    }

    @Override
    public void put(String s) {
        _numitems += 1;
        if (_numitems/_numbuckets > 5) {
            resize();
        }
        int bktnum = bucketfinder(s);
        if (!buckets[bktnum].contains(s)) {
            buckets[bktnum].add(s);
        }
    }

    @Override
    public boolean contains(String s) {
        int bktnum = bucketfinder(s);
        return buckets[bktnum].contains(s);
    }

    @Override
    public List<String> asList() {
        ArrayList<String> answer = new ArrayList<>();
        for (int i = 0; i < _numbuckets; i++) {
            for (int j = 0; j < buckets[i].size(); j++) {
                answer.add(buckets[i].get(j));
            }
        }
        return answer;
    }


}
