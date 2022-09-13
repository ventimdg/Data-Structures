# Gitlet Design Document

**Name**: Dominic Ventimiglia 

# Classes and Data Structures

##Main

Takes in terminal commands and directs them to the correct functions.

**Fields**

I do not know yet

##Init

Creates the repository if one is not already created

**Fields**

None, one function that creates the .gitlet repository

##Commit

Creates individual commits for the project class to take in.

**Fields**

LinkedList<Something> node: A linkedlist node for one commit

##Project

Holds all data and the commit tree for a project/group of files, as well as stores all of the stage data.

**Fields**

LinkedList<Commits> committree: The entire commit tree for a set of files 

Hashmap<String, file> addition: List of the names of the files staged for addition

List<String> removal: List of the names of the files staged for removal

List<String> branches: List of all the branches.

# Algorithms

##Main
Main(String[] args): Will take in the terminal commands and direct which class to create or functions to use 

##Init

Init(): The repository constructor. Will create a new .gitlet folder if one does not exist or will error if one already exists

##Commit

Commit(Project current): Will take in a project and access everything that is staged. Will create a new commit based on the staged files.

##Project

add(String name): takes in a file name and adds it to the stage. This function serializes a specific file and maps its name to its serialized version. Replaces the older version if a file is already on the stage and does nothing if the file is a duplicate.

rm(String name): Will remove a file from the stage if it exists in the stage list.

log(): Will print out a string representation of the commit tree.

globallog(): Will access all of the different projects and create a string representation of all of their commit trees.

find(String message): Will take in a string name and search the commit tree for a certain message. Will return all of the commmit nodes that have a matching message. Does this by taking the label of the commit node. Ideally, the label will be formatted so you have access to the entirety of the commit data and can just return the label of the commit with the mathching commit message.

status(): Will create a string representation of branches, addition, removal instance variables. Will possibly check the working directory for any modified files that have not been added to the stage.

Checkout(String[] args): Puts the head pointer at a certain commit. Does this make navigating through a linked list and setting head to a node once it finds the correct node. Can also replace a certain file in the CWD with a checked out file. 

Branch(String name): Will add a new branch to the instance variable the holds all the branches.

rmbranch(String name): Will search the branch list for a string name and remove it if it exists.

reset(String id): Replaces all files in the CWD with the files in the commit that matches the ID. Does this by overwriting all of the files in the CWD with the files found in the commit and making copies of files in the commit that are not currently present in the CWD

merge(): I have no idea what I am going to do for this. I do not fully understand this method yet. I need to do more studying of this method to understand it so I can make a plan for creating it.

# Persistence
1. .gitlet folder will contain a bunch of folders representing different projects, all of the different projects will have their unique commits.
2. Every time add is called, the file that is being added will be serialized and added to a staging folder for safe keeping.
3. Every time commit is called, create a new commit and serialize the new commit, adding the commit to the folder containing all commits for a certain porject/file. Each of the commit files will be labeled by their commit ID. This command will make a copy of all of the serialized files in the previous commit. It will then replace the copied files with files that have the same file name that are in the stage. Groups all of the new files into a new commit.
4. Every time commit is called, overwrite an exisiting file that contains the committree, adding the new commit ID to the file that represents the commit tree.
5. Every time checkout is called, find the correct file(s), deserialize it, and add it to the CWD.