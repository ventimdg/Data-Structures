
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */
    public UnionFind(int N) {
        _sets = new int[N + 1];
        _sizes = new int[N + 1];
        for (int i = 0; i <= N; i++) {
            _sets[i] = i;
            _sizes[i] = 1;
        }
        _sizes[0] = 0;
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {
        int holder = v;
        while(true) {
            int answer = _sets[holder];
            if (answer == holder) {
                _sets[v] = answer;
                return answer;
            } else {
                holder = answer;
            }
        }
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        if (!samePartition(u,v)) {
            int urep = find(u);
            int vrep = find(v);
            if (_sizes[urep] >= _sizes[vrep]) {
                _sizes[urep] += _sizes[vrep];
                _sizes[vrep] = 0;
                _sets[vrep] = urep;
                return urep;
            } else {
                _sizes[vrep] += _sizes[urep];
                _sizes[urep] = 0;
                _sets[urep] = vrep;
                return vrep;
            }
        } else {
            return find(u);
        }
    }

    int[] _sets;
    int[] _sizes;
}
