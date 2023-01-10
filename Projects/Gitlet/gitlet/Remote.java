package gitlet;

import java.io.File;
import java.io.IOException;

public class Remote {

    public static void addremote(String remotename, String directory) throws IOException {
        String separator = java.io.File.separator;
        directory = directory.replace('/', separator.charAt(0));
        File newremote = new File(".gitlet"  + File.separator + remotename);
        if (newremote.exists()) {
            System.out.println("A remote with that name already exists.");
        } else {
            newremote.createNewFile();
            Utils.writeContents(newremote, directory);
        }
    }

    public static void rmremote(String remotename) {
        File remote = new File(".gitlet"  + File.separator + remotename);
        if (!remote.exists()) {
            System.out.println("A remote with that name does not exist.");
        } else {
            remote.delete();
        }
    }

    

}
