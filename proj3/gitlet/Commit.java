package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TreeMap;

/** Class that holds all functions for
 * creating a new commit.
 *  @author Dominic Ventimiglia
 */
public class Commit implements Serializable {

    /** The sha1 id of this commit.
     */
    private String _name;

    /**The message that was given
     * to this commit.
     */
    private String _message;

    /** The time this commit was made.
     */
    private String _time = "Wed Dec 31 16:00:00 1969 -0800";

    /** The file directory where a commits parent
     * can be found.
     */
    private File _parentname = new File(".gitlet"
            + File.separator + "head.txt");

    /** The parent commit of this commit.
     */
    private Commit _parentcommit = null;

    /** TreeMap containg a commits filenames
     * and all pointers to its stored data.
     */
    private TreeMap<String, File> _data = new TreeMap<>();

    /** Boolean representing if thus commit was
     * the result of a merge.
     */
    private boolean _merged = false;

    /** All of the merged parents of a commit.
     */
    private ArrayList<Commit> _mergeparents = new ArrayList<>();

    /** Returns that data and pointers of a commit.
     * @return TreeMap with all filenames and pointers.
     */
    public TreeMap<String, File> returndata() {
        return _data;
    }

    /** Sets the name of a commit to the given input.
     * @param s Name to be set.
     */
    public void setname(String s) {
        _name = s;
    }

    /** Returns the sha1 id that the
     * commit is named after and the name
     * of the object in the commit directory.
     * @return String representing the sha1 id
     */
    public String getname() {
        return _name;
    }

    /** Returns the time that a commit was made.
     * @return String representing commit time.
     */
    public String gettime() {
        return _time;
    }

    /** Returns a commit's message.
     * @return String that is the message.
     */
    public String getmessage() {
        return _message;
    }

    /** Returns the parentcommit of a commit.
     * @return Commit that is a parent
     */
    public Commit getparent() {
        return _parentcommit;
    }

    /** Returns whether a commit is a result
     * of a merge.
     * @return True or false.
     */
    public boolean ismerged() {
        return _merged;
    }

    /** Returns all of the merged parents of a commit.
     * @return ArrayList of all merged parents
     */
    public ArrayList<Commit> getmergedparents() {
        return  _mergeparents;
    }

    /** If a commit has been merged, sets its
     * parents so the first parent is the commit
     * in the current branch and second parent
     * is the commit from the merged branch.
     * @param args all of the parent commit ids
     */
    public void setMergeparents(String[] args) {
        for (String name : args) {
            File temp = new File(".gitlet" + File.separator
                    + "commit" + File.separator + name);
            _mergeparents.add(Utils.readObject(temp, Commit.class));
        }
        _merged = true;
    }

    /** Commit constructor for the initial commit.
     * @param message The commit message
     */
    public Commit(String message) {
        _parentcommit = null;
        _data = null;
        _message = message;
    }

    /** Commit constructor that
     * creates a new commit to save in the .gitlet
     * directory that stores pointers to all data.
     * @param message the commit message.
     * @param stage the current stage
     */
    @SuppressWarnings("unchecked")
    public Commit(String message, Stage stage) {
        _message = message;
        String name = Utils.readContentsAsString(_parentname);
        File parentdir = new File(".gitlet" + File.separator
                + "commit" + File.separator + name);
        _parentcommit = Utils.readObject(parentdir, Commit.class);
        TreeMap<String, File> parentdata = _parentcommit.returndata();
        TreeMap<String, String> addstage = stage.getaddition();
        ArrayList<String> remstage = stage.getremoval();
        File currentstar = new File(".gitlet"
                + File.separator + "currentstar.txt");
        File curfiles = new File(".gitlet" + File.separator
                + Utils.readContentsAsString(currentstar) + "files");
        ArrayList<String> allfiles
                = Utils.readObject(curfiles, ArrayList.class);
        if (parentdata != null) {
            for (String key : parentdata.keySet()) {
                if (addstage.containsKey(key)) {
                    File temp = new File(".gitlet" + File.separator
                            + "blobs" + File.separator + addstage.get(key));
                    _data.put(key, temp);
                    allfiles.add(Utils.readContentsAsString(temp) + key);
                } else {
                    _data.put(key, parentdata.get(key));
                }
            }
        }
        for (String key : addstage.keySet()) {
            if (!_data.containsKey(key)) {
                File temp = new File(".gitlet" + File.separator
                        + "blobs" + File.separator + addstage.get(key));
                _data.put(key, temp);
                allfiles.add(Utils.readContentsAsString(temp) + key);
            }
        }
        for (String key : remstage) {
            _data.remove(key);
        }
        SimpleDateFormat currtime
                = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z");
        _time = currtime.format(new Date());
        stage.clear();
        File stageholder =  Utils.join(".gitlet"
                + File.separator + "stage", "stage");
        Utils.writeObject(stageholder, stage);
        Utils.writeObject(curfiles, allfiles);
    }
}
