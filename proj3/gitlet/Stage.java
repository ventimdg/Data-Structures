package gitlet;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeMap;


/** All of the stage data for commit to use
 * within the gitlet repository.
 *  @author Dominic Ventimiglia
 */
public class Stage implements Serializable {

    /** List representing all files that need to be added
     * to the next commit.
     */
    private TreeMap<String, String> _addition = new TreeMap<>();

    /** List representing all files that need to be removed
     * from the next commit.
     */
    private ArrayList<String> _removal = new ArrayList<>();

    /** intializes the addition and removal stage
     * to be empty.
     */
    Stage() {
        _addition = new TreeMap<>();
        _removal = new ArrayList<>();
    }

    /** Stages a file to be added to a commit if the file is
     * differnt from the file in the previous commit.
     * @param filename file to be added.
     */
    public void add(String filename) {
        File prevdir = new File(".gitlet"  + File.separator + "head.txt");
        String prevname = Utils.readContentsAsString(prevdir);
        prevdir = new File(".gitlet"  + File.separator
                + "commit"  + File.separator + prevname);
        Commit parentcommit = Utils.readObject(prevdir, Commit.class);
        File curfile = new File(filename);
        if (curfile.exists()) {
            String holder = Utils.sha1(Utils.readContentsAsString(curfile));
            File blob = new File(".gitlet" + File.separator
                    + "blobs" + File.separator + holder);
            if (parentcommit.returndata() != null
                    && parentcommit.returndata().containsKey(filename)
                    && blob.equals(parentcommit.returndata().get(filename))) {
                _addition.remove(filename);
            } else {
                _addition.remove(filename);
                _addition.put(filename, holder);
                Utils.writeContents(blob, (Object) Utils.readContents(curfile));
            }
            _removal.remove(filename);
        } else {
            System.out.println("File does not exist.");
        }
    }

    /** Deletes a file if necessary,
     * adds it to the removal stage if necessary,
     * and removes the file from the addition stage.
     * @param filename name of file to remove
     */
    public void remove(String filename) {
        File prevdir = new File(".gitlet"  + File.separator + "head.txt");
        String prevname = Utils.readContentsAsString(prevdir);
        prevdir = new File(".gitlet"  + File.separator
                + "commit"  + File.separator + prevname);
        Commit parentcommit = Utils.readObject(prevdir, Commit.class);
        if (parentcommit.returndata() == null
                && !_addition.containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            return;
        }
        if (parentcommit.returndata() != null
                && !parentcommit.returndata().containsKey(filename)
                && !_addition.containsKey(filename)) {
            System.out.println("No reason to remove the file.");
            return;
        }
        _addition.remove(filename);
        if (parentcommit.returndata() != null
                && parentcommit.returndata().containsKey(filename)) {
            _removal.add(filename);
            File removed = new File(filename);
            Utils.restrictedDelete(removed);
        }
    }

    /** Clears the addition and removal stage.
     */
    public void clear() {
        _addition = new TreeMap<>();
        _removal = new ArrayList<>();
    }

    /** Removes a file from the addition stage.
     * @param name file to be removed
     */
    public void untrack(String name) {
        _addition.remove(name);
    }

    /** Returns all of the files staged for addition.
     * @return Arraylist of all filenames
     */
    public TreeMap<String, String> getaddition() {
        return _addition;
    }

    /** Returns all of the files staged for removal.
     * @return Arraylist of all filenames
     */
    public ArrayList<String> getremoval() {
        return _removal;
    }

}

