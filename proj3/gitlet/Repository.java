package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;


/** Holds all of the functions for gitlet.
 *  @author Dominic Ventimiglia
 */
public class Repository {

    /** Variable the holds the pointer to the current branch.
     */
    private static final File CURRENTSTAR = new File(".gitlet"
            + File.separator + "currentstar.txt");

    /** Variable the holds the pointer to the current
     * head commit.
     */
    private static final File HEAD
            = new File(".gitlet" + File.separator + "head.txt");

    /** Variable the holds the pointer to the current
     * stage.
     */
    private static final File STAGEHOLDER =  Utils.join(".gitlet"
            + File.separator + "stage", "stage");

    /** Variable the holds the pointer to the current
     * branch file.
     */
    private static final File CURRENTBRANCH = new File(".gitlet"
            + File.separator + "currentbranch.txt");

    /** Variable the holds the pointer to list that
     * contains all branches.
     */
    private static final File ALLBRANCHES
            = new File(".gitlet" + File.separator + "allbranches");

    /** Folder where the Gitlet repositroy should live.
     */
    private static final File GITLETFOLDER = new File(".gitlet");

    /** The length of a full sha1 id.
     */
    private static final int IDLEN = 40;

    /** Returns the full length of a sha1 id.
     * @return in representing full sha1 length
     */
    public static int getidlen() {
        return IDLEN;
    }

    /** Creates a new commit when commit is called.
     * @param message The commit message
     * @return String of the sha1 id name
     */
    public static String commit(String message) {
        Stage stage = Utils.readObject(STAGEHOLDER, Stage.class);
        if (stage.getaddition().isEmpty() && stage.getremoval().isEmpty()) {
            System.out.println("No changes added to the commit.");
            return null;
        }
        Commit commit = new Commit(message, stage);
        String name = Utils.sha1((Object) Utils.serialize(commit));
        commit.setname(name);
        File newcommit = Utils.join(".gitlet"
                + File.separator + "commit", name);
        Utils.writeObject(newcommit, commit);
        File branch = new File(Utils.readContentsAsString(CURRENTBRANCH));
        Utils.writeContents(HEAD, name);
        Utils.writeContents(branch, name);
        return name;
    }

    /** Creates an initial gitlet repositroy.
     * @throws IOException
     */
    public static void init() throws IOException {
        if (GITLETFOLDER.exists()) {
            System.out.println("A Gitlet version-control system already "
                    + "exists in the current directory.");
            return;
        } else {
            createfolders();
            initialcommit();
        }
    }

    /** Returns the commit ID, messages, date, and merge data
     * of all commits in a gitlet repository, starting from the
     * head commit.
     */
    public static void log() {
        File headcommit = new File(Utils.readContentsAsString(HEAD));
        File comitdir = new File(".gitlet" + File.separator
                + "commit" + File.separator + headcommit);
        Commit commit = Utils.readObject(comitdir, Commit.class);
        String answer = "";
        while (commit.getparent() != null) {
            answer += "===" + "\n";
            answer += "commit " + commit.getname() + "\n";
            if (commit.ismerged()) {
                ArrayList<Commit> temp = commit.getmergedparents();
                answer += "Merge: " + temp.get(0).getname().substring(0, 7)
                        + " "
                        + temp.get(1).getname().substring(0, 7) + "\n";
            }
            answer += "Date: " + commit.gettime() + "\n";
            answer += commit.getmessage() + "\n";
            answer += "\n";
            commit = commit.getparent();
        }
        answer += "===" + "\n";
        answer += "commit " + commit.getname() + "\n";
        answer += "Date: " + commit.gettime() + "\n";
        answer += commit.getmessage() + "\n";
        System.out.println(answer);
    }

    /** Creates the initial commit when a repository is initialized.
     * @throws IOException
     */
    public static void initialcommit() throws IOException {
        Commit inital = new Commit("initial commit");
        String name = Utils.sha1((Object) Utils.serialize(inital));
        inital.setname(name);
        File newcommit = Utils.join(".gitlet"
                + File.separator + "commit", name);
        Utils.writeObject(newcommit, inital);
        File master = new File(".gitlet" + File.separator + "master.txt");
        CURRENTSTAR.createNewFile();
        Utils.writeContents(CURRENTSTAR, "master");
        Utils.writeContents(HEAD, name);
        Utils.writeContents(master, name);
        Utils.writeContents(CURRENTBRANCH, ".gitlet"
                + File.separator + "master.txt");
        Utils.writeContents(CURRENTSTAR, "master");
        ArrayList<String> branches = new ArrayList<>();
        branches.add("master");
        Utils.writeObject(ALLBRANCHES, branches);
        File masterfiles = new File(".gitlet" + File.separator + "masterfiles");
        ArrayList<String> files  = new ArrayList<>();
        Utils.writeObject(masterfiles, files);



    }

    /** Creates all of the necessary intial gitlet folders to
     * set up a repository.
     * @throws IOException
     */
    public static void createfolders() throws IOException {
        GITLETFOLDER.mkdir();
        File commit = new File(".gitlet" + File.separator + "commit");
        File stagefolder = new File(".gitlet" + File.separator + "stage");
        File master = new File(".gitlet" + File.separator + "master.txt");
        File blobs = new File(".gitlet" + File.separator + "blobs");
        Stage stage = new Stage();
        blobs.mkdir();
        commit.mkdir();
        stagefolder.mkdir();
        HEAD.createNewFile();
        master.createNewFile();
        CURRENTBRANCH.createNewFile();
        Utils.writeObject(STAGEHOLDER, stage);
    }

    /** Adds a file to the staging area if
     * it is a new or modified file.
     * @param s name of the file to be added
     */
    public static void add(String s) {
        File stagedir = new File(".gitlet" + File.separator
                + "stage" + File.separator + "stage");
        Stage stage = Utils.readObject(stagedir, Stage.class);
        stage.add(s);
        Utils.writeObject(stagedir, stage);
    }

    /** Removes a file and stages it for removal,
     * whichever is necessary.
     * @param s the name of the file for removal
     */
    public static void remove(String s) {
        File stagedir = new File(".gitlet"
                + File.separator + "stage" + File.separator + "stage");
        Stage stage = Utils.readObject(stagedir, Stage.class);
        stage.remove(s);
        Utils.writeObject(stagedir, stage);
    }

    /** Terminal output for if there
     * are untracked files that will be
     * overwritten.
     */
    public static void untracked() {
        System.out.println("There is an untracked "
                + "file in the way; "
                + "delete it, or add "
                + "and commit it first.");
    }

    /** Checker that makes modifications to the CWD
     * based on dats within dat.
     * @param data current commit data
     * @param allfiles all files in CWD
     * @param branchfiles all files tracked in branch
     * @return the altered allfiles list
     * @throws IOException
     */
    public static ArrayList<String> datachecker(
            TreeMap<String, File> data, ArrayList<String> allfiles,
            ArrayList<String> branchfiles) throws IOException {
        for (String filename : data.keySet()) {
            File indir = new File(filename);
            String answer = "";
            if (indir.exists()) {
                answer = Utils.readContentsAsString(indir) + filename;
            }
            if (branchfiles.contains(answer)) {
                allfiles.remove(filename);
            }
            File replaceddata = data.get(filename);
            if (indir.exists()) {
                Utils.writeContents(indir,
                        Utils.readContentsAsString(replaceddata));
            } else {
                indir.createNewFile();
                Utils.writeContents(indir,
                        Utils.readContentsAsString(replaceddata));
            }
        }
        return allfiles;
    }

    /**
     * Method that makes sure there are no untracked files
     * in the CWD.
     * @param allfiles all files in CWD
     * @param branchfiles all files tracked in branch
     * @return whether there are untracekd files
     */
    public static boolean untrackedfile(ArrayList<String> allfiles,
                                        ArrayList<String> branchfiles) {
        if (!allfiles.isEmpty()) {
            for (String file : allfiles) {
                File incwd = new File(file);
                String answer = Utils.readContentsAsString(incwd) + file;
                if (!branchfiles.contains(answer)) {
                    Repository.untracked();
                    return true;
                }
            }
        }
        return false;
    }

    /** Method that makes sure there are no untracked files
     * in the CWD.
     * @param newcommit the newly created commit
     * @param allfiles all files in CWD
     * @param branchfiles all files tracked in branch
     * @return whether there are untracekd files
     */
    public static boolean untrackedfile2(Commit newcommit,
                                         ArrayList<String> allfiles,
                                         ArrayList<String> branchfiles) {
        for (String file : allfiles) {
            File incwd = new File(file);
            String answer = Utils.readContentsAsString(incwd) + file;
            if (newcommit.returndata() != null
                    && newcommit.returndata().containsKey(file)
                    && !branchfiles.contains(answer)) {
                Repository.untracked();
                return true;
            }
        }
        return false;
    }

    /** Prunes allfiles in the CWD to only have files that
     * we have created or are working on.
     * @param allfiles List of all files in CWD
     * @return pruned allfiles list
     */
    public static ArrayList<String> remover(ArrayList<String> allfiles) {
        allfiles.remove(".DS_Store");
        allfiles.remove(".gitignore");
        allfiles.remove("Makefile");
        allfiles.remove("proj3.iml");
        return allfiles;
    }

    /** Deletes files in the CWD that checkout
     * would get rid of.
     * @param allfiles all files in CWD
     * @param branchfiles all files tracked in branch
     */
    public static void checkoutdelete(ArrayList<String> allfiles,
                                      ArrayList<String> branchfiles) {
        if (!allfiles.isEmpty()) {
            for (String file: allfiles) {
                File indir = new File(file);
                String answer = Utils.readContentsAsString(indir) + file;
                if (branchfiles.contains(answer)) {
                    Utils.restrictedDelete(file);
                }
            }
        }
    }

    /** Depending on the nuber of arguments, replaces the files in your
     * CWD with the entirety of a branch, changing what the current
     * branch is, or just grabbing a file from the head commit or
     * particular other commit.
     * @param args determines what type of checkout you will do
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static void checkout(String[] args) throws IOException {
        if (args.length == 2) {
            ArrayList<String> branches
                    = Utils.readObject(ALLBRANCHES, ArrayList.class);
            String starred = Utils.readContentsAsString(CURRENTSTAR);
            File curfiles = new File(".gitlet"
                    + File.separator + starred + "files");
            ArrayList<String> branchfiles
                    = Utils.readObject(curfiles, ArrayList.class);
            List<String> temp = Utils.plainFilenamesIn(".");
            ArrayList<String> allfiles
                    = Repository.remover(new ArrayList<>(temp));
            File chkbranch = new File(".gitlet"
                    + File.separator + args[1] + ".txt");
            if (!branches.contains(args[1])) {
                System.out.println("No such branch exists.");
                return;
            }
            String newname = Utils.readContentsAsString(chkbranch);
            chkbranch = Utils.join(".gitlet"
                    + File.separator + "commit", newname);
            Commit newcommit = Utils.readObject(chkbranch, Commit.class);
            TreeMap<String, File> data = newcommit.returndata();
            String sumcontents = "";
            if (untrackedfile2(newcommit, allfiles, branchfiles)) {
                return;
            }
            for (String words: branchfiles) {
                sumcontents += words;
            }
            ArrayList<String> holder = new ArrayList<>(allfiles);
            for (String file: holder) {
                if (!sumcontents.contains(file)) {
                    allfiles.remove(file);
                }
            }
            if (Repository.untrackedfile(allfiles, branchfiles)) {
                return;
            }
            if (starred.equals(args[1])) {
                System.out.println("No need to checkout the current branch.");
                return;
            }
            if (data != null) {
                allfiles = Repository.datachecker(data, allfiles, branchfiles);
            }
            Repository.checkoutdelete(allfiles, branchfiles);
            Utils.writeContents(CURRENTBRANCH, ".gitlet"
                    + File.separator + args[1] + ".txt");
            Utils.writeContents(CURRENTSTAR, args[1]);
            Utils.writeContents(HEAD, newname);
            Stage stage = Utils.readObject(STAGEHOLDER, Stage.class);
            stage.clear();
            Utils.writeObject(STAGEHOLDER, stage);
        } else if (args.length == 3) {
            Repository.checkout3(args);
        } else {
            Repository.checkout4(args);
        }
    }

    /** Depending on the nuber of arguments, replaces the files in your
     * CWD with the entirety of a branch, changing what the current
     * branch is, or just grabbing a file from the head commit or
     * particular other commit.
     * @param args determines what type of checkout you will do
     * @throws IOException
     */
    public static void checkout3(String[] args) throws IOException {
        String name = Utils.readContentsAsString(HEAD);
        File commitdir = Utils.join(".gitlet"
                + File.separator + "commit", name);
        Commit commit = Utils.readObject(commitdir, Commit.class);
        TreeMap<String, File> data = commit.returndata();
        if (data == null || !data.containsKey(args[2])) {
            System.out.println("File does not exist in that commit.");
        } else {
            File newfile = data.get(args[2]);
            File replaced = new File(args[2]);
            if (replaced.exists()) {
                Utils.writeContents(replaced,
                        Utils.readContentsAsString(newfile));
            } else {
                replaced.createNewFile();
                Utils.writeContents(replaced,
                        Utils.readContentsAsString(newfile));
            }
        }
    }

    /** Depending on the nuber of arguments, replaces the files in your
     * CWD with the entirety of a branch, changing what the current
     * branch is, or just grabbing a file from the head commit or
     * particular other commit.
     * @param args determines what type of checkout you will do
     * @throws IOException
     */
    public static void checkout4(String[] args) throws IOException {
        String id = args[1];
        if (id.length() < IDLEN) {
            File comfol = new File(".gitlet" + File.separator + "commit");
            List<String> commits = Utils.plainFilenamesIn(comfol);
            if (commits != null && !commits.isEmpty()) {
                for (String name : commits) {
                    if (name.contains(id)) {
                        id = name;
                        break;
                    }
                }
            }
        }
        File comdir = Utils.join(".gitlet" + File.separator + "commit", id);
        if (!comdir.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        Commit commit = Utils.readObject(comdir, Commit.class);
        TreeMap<String, File> data = commit.returndata();
        if (data == null || !data.containsKey(args[3])) {
            System.out.println("File does not exist in that commit.");
        } else {
            File newfile = data.get(args[3]);
            File replaced = new File(args[3]);
            if (replaced.exists()) {
                Utils.writeContents(replaced,
                        Utils.readContentsAsString(newfile));
            } else {
                replaced.createNewFile();
                Utils.writeContents(replaced,
                        Utils.readContentsAsString(newfile));
            }
        }
    }

    /** Returns the commit ID, messages, date, and merge data
     * of all commits in a gitlet repository.
     */
    public static void globallog() {
        File dir = new File(".gitlet" + File.separator + "commit");
        List<String> allcommits = Utils.plainFilenamesIn(dir);
        assert allcommits != null;
        String answer = "";
        for (int i = 0; i < allcommits.size(); i++) {
            String com = allcommits.get(i);
            File comdir = Utils.join(dir, com);
            Commit commit = Utils.readObject(comdir, Commit.class);
            answer += "===" + "\n";
            if (commit.ismerged()) {
                ArrayList<Commit> temp = commit.getmergedparents();
                answer += "Merge: "
                        + temp.get(0).getname().substring(0, 7) + " "
                        + temp.get(1).getname().substring(0, 7) + "\n";
            }
            answer += "commit " + commit.getname() + "\n";
            answer += "Date: " + commit.gettime() + "\n";
            answer += commit.getmessage() + "\n";
            if (i != allcommits.size() - 1) {
                answer += "\n";
            }
        }
        System.out.println(answer);
    }

    /** Returns all commits that have a matching commit message.
     * @param message the commit message you are looking for.
     */
    public static void find(String message) {
        boolean found = false;
        File dir = new File(".gitlet" + File.separator + "commit");
        List<String> allcommits = Utils.plainFilenamesIn(dir);
        assert allcommits != null;
        for (String name : allcommits) {
            File comdir = Utils.join(dir, name);
            Commit commit = Utils.readObject(comdir, Commit.class);
            if (commit.getmessage().equals(message)) {
                System.out.println(name);
                found = true;
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }
    }

    /** Returns the current staus of your gitlet directory.
     * Checks if you have any files staged for addition or
     * removal.
     */
    @SuppressWarnings("unchecked")
    public static void status() {
        File stagedir = new File(".gitlet"
                + File.separator + "stage" + File.separator + "stage");
        Stage stage = Utils.readObject(stagedir, Stage.class);
        String answer = "=== Branches ===" + "\n";
        String starred = Utils.readContentsAsString(CURRENTSTAR);
        ArrayList<String> branches =
                Utils.readObject(ALLBRANCHES, ArrayList.class);
        Collections.sort(branches);
        branches.set(branches.indexOf(starred), "*" + starred);
        List<String> temp = Utils.plainFilenamesIn(".");
        ArrayList<String> allfiles
                = Repository.remover(new ArrayList<>(temp));
        File headcommit = new File(Utils.readContentsAsString(HEAD));
        File comitdir = new File(".gitlet" + File.separator
                + "commit" + File.separator + headcommit);
        Commit commit = Utils.readObject(comitdir, Commit.class);
        ArrayList<String> untracked = new ArrayList<>();
        if (!branches.isEmpty()) {
            for (String branch : branches) {
                answer += branch + "\n";
            }
        }
        answer += "\n" + "=== Staged Files ===" + "\n";
        ArrayList<String> added = new ArrayList<>(stage.getaddition().keySet());
        Collections.sort(added);
        ArrayList<String> removed = stage.getremoval();
        Collections.sort(removed);
        TreeMap<String, File> data = commit.returndata();
        statuschain(answer, data, removed, commit, added,
                untracked, allfiles);
    }

    /** Chain method for status so that the
     * status method is not too long.
     * @param answer String representing answer
     * @param data all data in current commit
     * @param removed removal stage
     * @param commit current commit
     * @param added addition stage
     * @param untracked files that are untracked
     * @param allfiles all files in CWD
     */
    public static void statuschain(String answer,
            TreeMap<String, File> data, ArrayList<String> removed,
            Commit commit, ArrayList<String> added,
            ArrayList<String> untracked, ArrayList<String> allfiles) {
        if (data != null) {
            for (String file : data.keySet()) {
                File incwd = new File(file);
                if (!incwd.exists()) {
                    if (!removed.contains(file) && !commit.ismerged()) {
                        untracked.add(file + " (deleted)");
                        allfiles.remove(file);
                    }
                } else {
                    String holder
                            = Utils.sha1(Utils.readContentsAsString(incwd));
                    File blob = new File(".gitlet" + File.separator
                        + "blobs" + File.separator + holder);
                    if (!added.contains(file) && !data.get(file).equals(blob)) {
                        untracked.add(file + " (modified)");
                        allfiles.remove(file);
                    } else if (data.get(file).equals(blob)) {
                        allfiles.remove(file);
                    }
                }
                Collections.sort(untracked);
            }
        }
        if (!added.isEmpty()) {
            for (String key: added) {
                allfiles.remove(key);
                answer += key + "\n";
            }
        }
        answer += "\n" + "=== Removed Files ===" + "\n";
        if (!removed.isEmpty()) {
            for (String name: removed) {
                allfiles.remove(name);
                answer += name + "\n";
            }
        }
        answer += "\n" + "=== Modifications Not Staged For Commit ===" + "\n";
        if (!untracked.isEmpty()) {
            for (String name: untracked) {
                answer += name + "\n";
            }
        }
        answer += "\n" + "=== Untracked Files ===" + "\n";
        if (!allfiles.isEmpty()) {
            Collections.sort(allfiles);
            for (String name: allfiles) {
                answer += name + "\n";
            }
        }
        System.out.println(answer);
    }

    /** Creates a new branch available to use in your gitlet
     * repository. Sets the commit it is pointing to whatever head is.
     * @param brname the name of the new branch you want to create
     */
    @SuppressWarnings("unchecked")
    public static void branch(String brname) {
        ArrayList<String> branches =
                Utils.readObject(ALLBRANCHES, ArrayList.class);
        if (branches.contains(brname)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        File newbranchfiles = new File(".gitlet"
                + File.separator + brname + "files");
        File curfiles = new File(".gitlet" + File.separator
                + Utils.readContentsAsString(CURRENTSTAR) + "files");
        ArrayList<String> files = Utils.readObject(curfiles, ArrayList.class);
        Utils.writeObject(newbranchfiles, files);
        branches.add(brname);
        Utils.writeObject(ALLBRANCHES, branches);
        String comname = Utils.readContentsAsString(HEAD);
        File newbranchfile = new File(".gitlet"
                + File.separator + brname + ".txt");
        Utils.writeContents(newbranchfile, comname);
    }

    /** Deletes all pointers to a branch but
     * does not delete its commits or data.
     * @param brname the branch name we want to delete
     */
    @SuppressWarnings("unchecked")
    public static void rmbranch(String brname) {
        ArrayList<String> branches =
                Utils.readObject(ALLBRANCHES, ArrayList.class);
        if (!branches.contains(brname)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        String starred = Utils.readContentsAsString(CURRENTSTAR);
        if (brname.equals(starred)) {
            System.out.println("Cannot remove the current branch.");
            return;
        }
        branches.remove(brname);
        Utils.writeObject(ALLBRANCHES, branches);
        File delbranch = new File(".gitlet" + File.separator + brname + ".txt");
        delbranch.delete();
        File newbranchfiles = new File(".gitlet"
                + File.separator + brname + "files");
        newbranchfiles.delete();
    }


    /** If an id is less then 40 characters long,
     * will find the full id and change the id
     * to the ful name.
     * @param id name of a commit
     * @return the full sha1 id of a commit
     */
    public static String idchanger(String id) {
        if (id.length() < IDLEN) {
            File comfol = new File(".gitlet" + File.separator + "commit");
            List<String> commits = Utils.plainFilenamesIn(comfol);
            if (commits != null && !commits.isEmpty()) {
                for (String name : commits) {
                    if (name.contains(id)) {
                        id = name;
                        break;
                    }
                }
            }
        }
        return id;
    }

    /** Essentiall checks out the branch with commit
     * ID into the current directory.
     * Also changes the branch head to be the commit with id.
     * @param id the id of the commit we want to revert all of the files to.
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static void reset(String id) throws IOException {
        id = Repository.idchanger(id);
        File comdir = Utils.join(".gitlet" + File.separator + "commit", id);
        if (!comdir.exists()) {
            System.out.println("No commit with that id exists.");
            return;
        }
        String starred = Utils.readContentsAsString(CURRENTSTAR);
        File curfiles = new File(".gitlet"
                + File.separator + starred + "files");
        ArrayList<String> branchfiles =
                Utils.readObject(curfiles, ArrayList.class);
        List<String> temp = Utils.plainFilenamesIn(".");
        ArrayList<String> allfiles
                = Repository.remover(new ArrayList<>(temp));
        File newcom = Utils.join(".gitlet" + File.separator + "commit", id);
        Commit newcommit = Utils.readObject(newcom, Commit.class);
        TreeMap<String, File> data = newcommit.returndata();
        String sumcontents = "";
        if (untrackedfile2(newcommit, allfiles, branchfiles)) {
            return;
        }
        for (String words: branchfiles) {
            sumcontents += words;
        }
        ArrayList<String> holder = new ArrayList<>(allfiles);
        for (String file: holder) {
            if (!sumcontents.contains(file)) {
                allfiles.remove(file);
            }
        }
        if (untrackedfile(allfiles, branchfiles)) {
            return;
        }
        Repository.resetdatacheck(data, allfiles);
        Repository.resetallfilesdel(allfiles, branchfiles);
        File newbranchfile = new File(".gitlet"
                + File.separator + starred + ".txt");
        Utils.writeContents(newbranchfile, id);
        Utils.writeContents(HEAD, id);
        Stage stage = Utils.readObject(STAGEHOLDER, Stage.class);
        stage.clear();
        Utils.writeObject(STAGEHOLDER, stage);
    }

    /** Deletes all files in a CWD that would be deleted
     * as a result of the reset command.
     * @param allfiles all files in CWD
     * @param branchfiles all files tracked in branch
     */
    public static void resetallfilesdel(ArrayList<String> allfiles,
                                        ArrayList<String> branchfiles) {
        if (!allfiles.isEmpty()) {
            for (String file: allfiles) {
                File indir = new File(file);
                String answer = Utils.readContentsAsString(indir) + file;
                if (branchfiles.contains(answer)) {
                    Utils.restrictedDelete(file);
                }
            }
        }
    }


    /** Overwrites data of files that would be overwritten
     * by the reset command.
     * @param data name and file pointers to commit data
     * @param allfiles all files in CWD
     * @return altered allfiles list
     * @throws IOException
     */
    public static ArrayList<String> resetdatacheck(
            TreeMap<String, File> data,
            ArrayList<String> allfiles) throws IOException {
        if (data != null) {
            for (String filename : data.keySet()) {
                File indir = new File(filename);
                allfiles.remove(filename);
                File replaceddata = data.get(filename);
                if (indir.exists()) {
                    Utils.writeContents(indir,
                            Utils.readContentsAsString(replaceddata));
                } else {
                    indir.createNewFile();
                    Utils.writeContents(indir,
                            Utils.readContentsAsString(replaceddata));
                }
            }
        }
        return allfiles;
    }
    /** Merges two branches into one another.
     * Merges the branch name into the current branch.
     * @param name branch that is going to be merged
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static void merge(String name) throws IOException {
        Stage stage = Utils.readObject(STAGEHOLDER, Stage.class);
        if (!stage.getaddition().isEmpty() || !stage.getremoval().isEmpty()) {
            System.out.println("You have uncommitted changes.");
            return;
        }
        ArrayList<String> branches =
                Utils.readObject(ALLBRANCHES, ArrayList.class);
        if (!branches.contains(name)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        String curname = Utils.readContentsAsString(CURRENTSTAR);
        if (curname.equals(name)) {
            System.out.println("Cannot merge a branch with itself.");
            return;
        }
        File curfile = new File(".gitlet" + File.separator + curname + ".txt");
        String curcom = Utils.readContentsAsString(curfile);
        File merger = new File(".gitlet" + File.separator + name + ".txt");
        String mergecomname = Utils.readContentsAsString(merger);
        File mergcomfile = new File(".gitlet" + File.separator
                + "commit" + File.separator + mergecomname);
        Commit mergecom = Utils.readObject(mergcomfile, Commit.class);
        File comitdir = new File(".gitlet"
                + File.separator + "commit"
                + File.separator + curcom);
        Commit curcommit = Utils.readObject(comitdir, Commit.class);
        String splitpoint = null;
        Commit curcommholder = curcommit;
        Commit mergecomholder = mergecom;
        ArrayList<String> allchkbrnames = new ArrayList<>();
        HashMap<String, Integer> alldistances = new HashMap<>();
        int smallest = Integer.MAX_VALUE;
        boolean encountered = false;
        int distance = 1;
        while (mergecom != null) {
            allchkbrnames.add(mergecom.getname());
            mergecom = mergecom.getparent();
        }
        String[] words = new String[] {curname, curcom, name,
            mergecomname, splitpoint};
        int[] nums = new int[] {distance, smallest};
        mergecom = mergecomholder;
        Commit[] commits = new Commit[] {mergecom, curcommit};
        Repository.mergechain1(commits, allchkbrnames,
                alldistances,  nums, curcommholder, mergecomholder,
                encountered, words);
    }

    /** Continuation of the merge method because I am not
     * allowed to have a method that is longer than 60
     * lines.
     * @param commits The merge and current commit
     * @param allchkbrnames The names of all commits in a branch
     * @param alldistances The commit names and their distances
     * @param nums all numbers
     * @param curcommholder current commit
     * @param mergecomholder commit of given branch
     * @param encountered true or false
     * @param words All String names
     * @throws IOException
     */
    public static void mergechain1(Commit[] commits,
            ArrayList<String> allchkbrnames, HashMap<String,
            Integer> alldistances, int[] nums, Commit curcommholder,
            Commit mergecomholder, boolean encountered,
            String[] words) throws IOException {
        Stage stage = Utils.readObject(STAGEHOLDER, Stage.class);
        Commit mergecom = commits[0];
        Commit curcommit = commits[1];
        int distance = nums[0];
        int smallest = nums[1];
        String curname = words[0];
        String curcom = words[1];
        String name = words[2];
        String mergecomname = words[3];
        String splitpoint = words[4];
        while (curcommit != null) {
            ArrayList<Commit> parents = curcommit.getmergedparents();
            if (Repository.check1(curcommit, allchkbrnames, parents)) {
                alldistances.put(curcommit.getname(), distance);
                alldistances.put(parents.get(1).getname(), distance + 1);
            } else {
                alldistances.put(curcommit.getname(), distance);
            }
            curcommit = curcommit.getparent();
            distance++;
        }
        while (mergecom != null) {
            ArrayList<Commit> parents = mergecom.getmergedparents();
            if (Repository.check2(mergecom, alldistances, parents)) {
                if (alldistances.get(parents.get(1).getname()) < smallest) {
                    splitpoint = parents.get(1).getname();
                    smallest = alldistances.get(parents.get(1).getname());
                }
            }
            if (Repository.check3(alldistances, mergecom, smallest)) {
                splitpoint = mergecom.getname();
                smallest = alldistances.get(mergecom.getname());
            }
            mergecom = mergecom.getparent();
        }
        String[] wordz = new String[] {curname, curcom, name,
            mergecomname, splitpoint};
        Repository.mergechain2(curcommholder, mergecomholder,
                stage, encountered, wordz);
    }


    /** Continuation of the merge method because I am not
     * allowed to have a method that is longer than 60
     * lines.
     * @param curcommholder current commit
     * @param mergecomholder commit of given branch
     * @param stage the current stage
     * @param encountered true or false
     * @param words All String names
     * @throws IOException
     */
    @SuppressWarnings("unchecked")
    public static void mergechain2(Commit curcommholder,
            Commit mergecomholder, Stage stage,
            boolean encountered, String[] words) throws IOException {
        String name = words[2];
        String splitpoint = words[4];
        assert splitpoint != null;
        File splitdir = new File(".gitlet" + File.separator
                + "commit" + File.separator + splitpoint);
        Commit splitcommit = Utils.readObject(splitdir, Commit.class);
        TreeMap<String, File> currentdata = curcommholder.returndata();
        TreeMap<String, File> mergedata = mergecomholder.returndata();
        TreeMap<String, File> splitdata = splitcommit.returndata();
        ArrayList<String> currentkey = new ArrayList<>();
        ArrayList<String> itercurrentkey = new ArrayList<>();
        ArrayList<String> mergekeys = new ArrayList<>();
        ArrayList<String> itermergekeys = new ArrayList<>();
        ArrayList<String> splitkeys = new ArrayList<>();
        if (currentdata != null) {
            currentkey = new ArrayList<String>(currentdata.keySet());
            itercurrentkey =  new ArrayList<String>(currentdata.keySet());
        }
        if (mergedata != null) {
            mergekeys =  new ArrayList<String>(mergedata.keySet());
            itermergekeys =  new ArrayList<String>(mergedata.keySet());
        }
        if (splitdata != null) {
            splitkeys =  new ArrayList<String>(splitdata.keySet());
        }
        String starred = Utils.readContentsAsString(CURRENTSTAR);
        File curfiles = new File(".gitlet"
                + File.separator + starred + "files");
        ArrayList<String> branchfiles =
                Utils.readObject(curfiles, ArrayList.class);
        List<String> temp = Utils.plainFilenamesIn(".");
        ArrayList<String> allfiles
                = Repository.remover(new ArrayList<>(temp));
        if (untrackedfile(allfiles, branchfiles)) {
            return;
        }
        if (splitpoint.equals(curcommholder.getname())) {
            Repository.checkout(new String[] {"checkout", name});
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        if (splitpoint.equals(mergecomholder.getname())) {
            System.out.println("Given branch is an ancestor "
                    + "of the current branch.");
            return;
        }
        ArrayList<String>[] holders = new ArrayList[] {currentkey,
            itercurrentkey, mergekeys, itermergekeys, splitkeys};
        Repository.mergechain3(holders, currentdata, splitdata,
                mergedata,  stage, encountered, words);
    }

    /** Continuation of the merge method because I am not
     * allowed to have a method that is longer than 60
     * lines.
     * @param holders List of Arraylists containing filenames
     * @param currentdata current commit data
     * @param splitdata splitpoint data
     * @param mergedata merged commit data
     * @param stage current stage
     * @param encountered true or false
     * @param words All String names
     * @throws IOException
     */
    public static void mergechain3(ArrayList<String>[] holders,
            TreeMap<String, File> currentdata, TreeMap<String, File> splitdata,
            TreeMap<String, File> mergedata, Stage stage,
            boolean encountered, String[] words) throws IOException {
        ArrayList<String> currentkey = holders[0];
        ArrayList<String> itercurrentkey = holders[1];
        ArrayList<String> mergekeys = holders[2];
        ArrayList<String> itermergekeys = holders[3];
        ArrayList<String> splitkeys = holders[4];
        String curname = words[0];
        String curcom = words[1];
        String name = words[2];
        String mergecomname = words[3];
        for (String file : mergekeys) {
            if (currentdata != null && splitdata != null
                    && !currentdata.containsKey(file)
                    && !splitdata.containsKey(file)) {
                Repository.checkout(
                        new String[] {"checkout", mergecomname, "--", file});
                Repository.add(file);
                currentkey.remove(file);
                itermergekeys.remove(file);
                itercurrentkey.remove(file);
                splitkeys.remove(file);
            } else if (currentdata != null && splitdata != null
                    && currentdata.containsKey(file)
                    && splitdata.containsKey(file)
                    && !mergedata.get(file).equals(splitdata.get(file))
                    && currentdata.get(file).equals(splitdata.get(file))) {
                Repository.checkout(
                        new String[] {"checkout", mergecomname, "--", file});
                Repository.add(file);
                currentkey.remove(file);
                itermergekeys.remove(file);
                itercurrentkey.remove(file);
                splitkeys.remove(file);
            } else if (currentdata != null && splitdata != null
                    && splitdata.containsKey(file)
                    && !currentdata.containsKey(file)
                    && splitdata.get(file).equals(mergedata.get(file))) {
                currentkey.remove(file);
                itermergekeys.remove(file);
                itercurrentkey.remove(file);
                splitkeys.remove(file);
            }
        }
        Repository.mergechain4(currentdata, splitdata, mergedata,
                stage, encountered, words, holders);
    }

    /** Continuation of the merge method because I am not
     * allowed to have a method that is longer than 60
     * lines.
     * @param currentdata current commit data
     * @param splitdata splitpoint data
     * @param mergedata merged commit data
     * @param stage current stage
     * @param encountered true or false
     * @param words All String names
     * @param holders List of Arraylists containing filenames
     * @throws IOException
     */
    public static void mergechain4(TreeMap<String, File> currentdata,
            TreeMap<String, File> splitdata, TreeMap<String, File> mergedata,
            Stage stage, boolean encountered, String[] words,
            ArrayList<String>[] holders) throws IOException {
        ArrayList<String> currentkey = holders[0];
        ArrayList<String> itercurrentkey = holders[1];
        ArrayList<String> itermergekeys = holders[3];
        ArrayList<String> splitkeys = holders[4];
        ArrayList<String> mergekeys =  new ArrayList<String>(itermergekeys);
        for (String file : currentkey) {
            if (splitdata != null && mergedata != null
                    && splitdata.containsKey(file)
                    && splitdata.get(file).equals(currentdata.get(file))
                    && !mergedata.containsKey(file)) {
                Utils.restrictedDelete(file);
                stage = Utils.readObject(STAGEHOLDER, Stage.class);
                stage.untrack(file);
                Utils.writeObject(STAGEHOLDER, stage);
                itermergekeys.remove(file);
                itercurrentkey.remove(file);
                mergekeys.remove(file);
                splitkeys.remove(file);
            } else if (splitdata != null && mergedata != null
                    && splitdata.containsKey(file)
                    && mergedata.containsKey(file)
                    && !splitdata.get(file).equals(currentdata.get(file))
                    && splitdata.get(file).equals(mergedata.get(file))) {
                itermergekeys.remove(file);
                itercurrentkey.remove(file);
                mergekeys.remove(file);
                splitkeys.remove(file);
            } else if (splitdata != null
                    && mergedata != null
                    && !splitdata.containsKey(file)
                    && !mergedata.containsKey(file)
                    && currentdata.containsKey(file)) {
                itermergekeys.remove(file);
                itercurrentkey.remove(file);
                mergekeys.remove(file);
                splitkeys.remove(file);
            }
        }
        currentkey =  new ArrayList<String>(itercurrentkey);
        for (String file : mergekeys) {
            if (currentdata != null && currentdata.containsKey(file)
                    && splitdata != null && splitdata.containsKey(file)
                    && !splitdata.get(file).equals(currentdata.get(file))
                    && mergedata != null
                    && !splitdata.get(file).equals(mergedata.get(file))
                    && currentdata.get(file).equals(mergedata.get(file))) {
                itermergekeys.remove(file);
                itercurrentkey.remove(file);
                currentkey.remove(file);
                splitkeys.remove(file);
            }
        }
        Repository.mergechain5(currentdata, splitdata, mergedata,
                stage, encountered, words, holders);
    }

    /** Continuation of the merge method because I am not
     * allowed to have a method that is longer than 60
     * lines.
     * @param currentdata current commit data
     * @param splitdata splitpoint data
     * @param mergedata merged commit data
     * @param stage curretn stage
     * @param encountered true or false
     * @param words All String names
     * @param holders List of Arraylists containing filenames
     * @throws IOException
     */
    public static void mergechain5(TreeMap<String, File> currentdata,
            TreeMap<String, File> splitdata,
            TreeMap<String, File> mergedata, Stage stage,
            boolean encountered, String[] words,
            ArrayList<String>[] holders) throws IOException {
        ArrayList<String> itercurrentkey = holders[1];
        ArrayList<String> itermergekeys = holders[3];
        ArrayList<String> splitkeys = holders[4];
        String curname = words[0];
        String curcom = words[1];
        String name = words[2];
        String mergecomname = words[3];
        ArrayList<String>  mergekeys =  new ArrayList<String>(itermergekeys);
        for (String file : mergekeys) {
            if (mergedata != null && mergedata.containsKey(file)
                    && splitdata != null && splitdata.containsKey(file)
                    && currentdata != null
                    && !splitdata.get(file).equals(currentdata.get(file))
                    && !splitdata.get(file).equals(mergedata.get(file))
                    && currentdata.get(file).equals(mergedata.get(file))) {
                itermergekeys.remove(file);
                itercurrentkey.remove(file);
                mergekeys.remove(file);
                splitkeys.remove(file);
            }
        }
        ArrayList<String> currentkey =  new ArrayList<String>(itercurrentkey);
        if (splitdata == null) {
            for (String file : mergekeys) {
                if (!currentkey.contains(file)) {
                    Repository.checkout(
                            new String[]{"checkout", mergecomname, "--", file});
                    Repository.add(file);
                    itermergekeys.remove(file);
                    itercurrentkey.remove(file);
                } else if (mergedata != null && currentdata != null
                        && currentdata.get(file).equals(mergedata.get(file))) {
                    itermergekeys.remove(file);
                    itercurrentkey.remove(file);
                    currentkey.remove(file);
                }
            }
            mergekeys =  new ArrayList<String>(itermergekeys);
            for (String file : currentkey) {
                if (!mergekeys.contains(file)) {
                    itercurrentkey.remove(file);
                }

            }
            currentkey =  new ArrayList<String>(itercurrentkey);
        }
        Repository.mergechain6(currentkey, mergekeys,
                currentdata, mergedata, encountered, words);
    }

    /** Continuation of the merge method because I am not
     * allowed to have a method that is longer than 60
     * lines.
     * @param currentkey Files in current commit
     * @param mergekeys File in merge branch commit
     * @param currentdata current commit date
     * @param mergedata merge branch data
     * @param encountered true or false
     * @param words All string names
     * @throws IOException
     */
    public static void mergechain6(ArrayList<String> currentkey,
            ArrayList<String> mergekeys, TreeMap<String, File> currentdata,
            TreeMap<String, File> mergedata, boolean encountered,
            String[] words) throws IOException {
        String curname = words[0];
        String curcom = words[1];
        String name = words[2];
        String mergecomname = words[3];
        for (String file : mergekeys) {
            File conflict = new File(file);
            if (!conflict.exists()) {
                conflict.createNewFile();
            }
            if (!currentkey.contains(file) && mergedata != null) {
                Utils.writeContents(conflict, "<<<<<<< HEAD" + "\n"
                        + "=======" + "\n"
                        + Utils.readContentsAsString(mergedata.get(file))
                        + ">>>>>>>" + "\n");
                Repository.add(file);
            } else if (mergedata != null && currentdata != null) {
                Utils.writeContents(conflict, "<<<<<<< HEAD" + "\n"
                        + Utils.readContentsAsString(currentdata.get(file))
                        + "=======" + "\n"
                        + Utils.readContentsAsString(mergedata.get(file))
                        + ">>>>>>>" + "\n");
                Repository.add(file);
            }
            currentkey.remove(file);
            encountered = true;
        }
        for (String file : currentkey) {
            File conflict = new File(file);
            if (!conflict.exists()) {
                conflict.createNewFile();
            }
            if (!mergekeys.contains(file) && currentdata != null) {
                Utils.writeContents(conflict, "<<<<<<< HEAD" + "\n"
                        + Utils.readContentsAsString(currentdata.get(file))
                        + "=======" + "\n" + ">>>>>>>" + "\n");
                Repository.add(file);
            } else if (mergedata != null && currentdata != null) {
                Utils.writeContents(conflict,  "<<<<<<< HEAD" + "\n"
                        + Utils.readContentsAsString(currentdata.get(file))
                        + "=======" + "\n"
                        + Utils.readContentsAsString(mergedata.get(file))
                        + ">>>>>>>" + "\n");
                Repository.add(file);
            }
            encountered = true;
        }

        String newcomname = Repository.commit("Merged " + name
                + " into " + curname + ".");
        File newcommit = Utils.join(".gitlet" + File.separator
                + "commit", newcomname);
        Commit newcom = Utils.readObject(newcommit, Commit.class);
        newcom.setMergeparents(new String[] {curcom, mergecomname});
        Utils.writeObject(newcommit, newcom);
        if (encountered) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** One of the checks to go into an if
     * case for merge.
     * @param curcommit the commmit of the current branch
     * @param allchkbrnames The names of all commits in a branch
     * @param parents The parents of a merged commit
     * @return True or False
     */
    public static boolean check1(Commit curcommit,
            ArrayList<String> allchkbrnames,
            ArrayList<Commit> parents) {
        return curcommit.ismerged()
                && allchkbrnames.contains(parents.get(1).getname());
    }

    /** One of the checks to go into an if
     * case for merge.
     * @param mergecom The commit of the merged branch
     * @param alldistances The commit names and their distances
     * @param parents The parents of a merged commit
     * @return True of false
     */
    public static boolean check2(Commit mergecom, HashMap<String,
            Integer> alldistances, ArrayList<Commit> parents) {
        return mergecom.ismerged()
                && alldistances.containsKey(parents.get(1).getname());
    }

    /** One of the checks to go into an if
     * case for merge.
     * @param alldistances The commit names and their distances
     * @param mergecom The commit of the merged branch
     * @param smallest smallest distance found
     * @return True or false
     */
    public static boolean check3(HashMap<String, Integer> alldistances,
            Commit mergecom, int smallest) {
        return alldistances.containsKey(mergecom.getname())
                && alldistances.get(mergecom.getname()) < smallest;
    }

}

