package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Dominic Ventimiglia
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */

    public static void main(String... args) throws IOException {
        if (args.length == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        switch (args[0]) {
        case "init":
            initcheck(args);
            break;
        case "add":
            basiccheck2(args);
            break;
        case "commit":
            commitcheck(args);
            break;
        case "rm":
            basiccheck2(args);
            break;
        case "log":
            if (basiccheck1(args)) {
                Repository.log();
            }
            break;
        case "global-log":
            if (basiccheck1(args)) {
                Repository.globallog();
            }
            break;
        case "find":
            basiccheck2(args);
            break;
        case "status":
            if (basiccheck1(args)) {
                Repository.status();
            }
            break;
        case "checkout":
            checkoutcheck(args);
            break;
        case "branch":
            basiccheck2(args);
            break;
        case "rm-branch":
            basiccheck2(args);
            break;
        case "reset":
            basiccheck2(args);
            break;
        case "merge":
            basiccheck2(args);
            break;
        default:
            System.out.println("No command with that name exists.");
        }
    }

    /** Checks the validity of an input and runs the correct
     * corresponding errors or functions.
     * @param args String array representing the inputs.
     * @throws IOException
     */
    private static void basiccheck2(String[] args) throws IOException {
        File gitletfolder = new File(".gitlet");
        int length = args.length;
        if (length != 2) {
            System.out.println("Incorrect operands.");
            return;
        }
        if (!gitletfolder.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        switch (args[0]) {
        case "add":
            Repository.add(args[1]);
            break;
        case "rm":
            Repository.remove(args[1]);
            break;
        case "find":
            Repository.find(args[1]);
            break;
        case "merge":
            Repository.merge(args[1]);
            break;
        case "reset":
            Repository.reset(args[1]);
            break;
        case "rm-branch":
            Repository.rmbranch(args[1]);
            break;
        case "branch":
            Repository.branch(args[1]);
            break;
        default:
            System.out.println("No command with that name exists.");
        }
    }

    /** Checks the validity of an input.
     * @param args inputs to the program
     * @return boolean determining if valid.
     */
    private static boolean basiccheck1(String[] args) {
        File gitletfolder = new File(".gitlet");
        int length = args.length;
        if (length != 1) {
            System.out.println("Incorrect operands.");
            return false;
        }
        if (!gitletfolder.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return false;
        }
        return true;
    }

    /** Checks the validity of an input and runs the correct
     * corresponding errors or functions.
     * @param args String array representing the inputs.
     * @throws IOException
     */
    private static void checkoutcheck(String[] args) throws IOException {
        File gitletfolder = new File(".gitlet");
        int length = args.length;
        if (length < 2 || length > 4) {
            System.out.println("Incorrect operands.");
            return;
        }
        if (length == 3 || length == 4) {
            if (length == 3 && !args[1].equals("--")) {
                System.out.println("Incorrect operands.");
                return;
            }
            if (length == 4 && !args[2].equals("--")) {
                System.out.println("Incorrect operands.");
                return;
            }
        }
        if (!gitletfolder.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Repository.checkout(args);
    }

    /** Checks the validity of an input and runs the correct
     * corresponding errors or functions.
     * @param args String array representing the inputs.
     */
    private static void commitcheck(String[] args) {
        File gitletfolder = new File(".gitlet");
        int length = args.length;
        if (length != 2 || args[1].length() < 1) {
            if (length == 1 || args[1].length() < 1) {
                System.out.println("Please enter a commit message.");
            } else {
                System.out.println("Incorrect operands.");
            }
            return;
        }
        if (!gitletfolder.exists()) {
            System.out.println("Not in an initialized Gitlet directory.");
            return;
        }
        Repository.commit(args[1]);
    }

    /** Checks the validity of an input and runs the correct
     * corresponding errors or functions.
     * @param args String array representing the inputs.
     * @throws IOException
     */
    private static void initcheck(String[] args) throws IOException {
        int length = args.length;
        if (length != 1) {
            System.out.println("Incorrect operands.");
            return;
        }
        Repository.init();
    }
}


