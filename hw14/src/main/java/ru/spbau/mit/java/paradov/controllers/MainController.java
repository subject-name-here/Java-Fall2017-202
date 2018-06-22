package ru.spbau.mit.java.paradov.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import ru.spbau.mit.java.paradov.ftp.Client;
import ru.spbau.mit.java.paradov.util.UtilFunctions;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Controller for main menu and its layout. This thing is a browser that connects to server with given port and browses
 * the files on server side.
 */
public class MainController {
    /** Client that asks queries from server. */
    private static Client client;
    /** Delimiter that is used on server side as file separator. */
    private static String delimiter;
    /** Root directory for file browsing. */
    private static String root;

    /** Files tree that is displayed on screen. */
    private TreeView<Item> tree;

    /** Label that shows the root or something else, such as download status. */
    public Label path;
    /** Grid that contains everything on main stage. */
    public GridPane grid;

    private static final int DEFAULT_PORT = 22229;

    /**
     * Initializes class: places TreeView on grid, sets label.
     */
    @FXML
    public void initialize() {
        path.setText(root);
        TreeItem<Item> rootItem = new MyTreeItem(new Item(root,  " true"));
        tree = new TreeView<>(rootItem);
        tree.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TreeItem<Item> item = tree.getSelectionModel().getSelectedItem();
                try {

                    if (!item.getValue().isDirectory) {
                        client.sendQuery(2, item.getValue().name, new PrintWriter(new StringWriter()));
                    }
                    path.setText("File " + item.getValue().shortName + " downloaded!");
                } catch (Exception e) {
                    path.setText("File " + item.getValue().shortName + ": failed to download!");
                }
            }
        });

        grid.add(tree, 0, 1);
    }

    /**
     * Sets some fields in class.
     * @param hostName name of host
     * @param portNumber string that contains number of port; if it's incorrect, then port is set to default
     * @param delimiter delimiter that will be used to query lists
     * @param root place for file browsing
     */
    public static void setupMain(String hostName, String portNumber, String delimiter, String root) {
        MainController.root = root;
        MainController.delimiter = delimiter;

        Integer portNum = UtilFunctions.stringToIntOrElse(portNumber, DEFAULT_PORT);
        if (portNum < 0 || portNum >= 65536) {
            portNum = DEFAULT_PORT;
        }

        client = new Client(hostName, portNum);
    }

    /**
     * Event handler for pressing Exit button. It just closes window.
     * @param actionEvent event of pressing Exit button
     */
    public void exit(ActionEvent actionEvent) {
        UtilFunctions.getStageFromButtonActionEvent(actionEvent).close();
    }


    /**
     * Class for TreeView item that contains file info, such as full name, short name and if this file is directory.
     */
    private class Item {
        /** Full name of file (based on root). */
        private String name;
        /** Last name of file. */
        private String shortName;
        /** Flag that detects if this file is a directory. */
        private boolean isDirectory;

        /**
         * Constructs item from path to file and server response.
         * @param path path to file
         * @param response response from server about this file
         */
        public Item(String path, String response) {
            shortName = response.substring(0, response.lastIndexOf(" "));
            name = path + delimiter + shortName;

            isDirectory = Boolean.valueOf(response.substring(response.lastIndexOf(" ") + 1));
        }

        /** Show of the item in file tree. */
        @Override
        public String toString() {
            return shortName;
        }
    }

    /**
     * Class that extends TreeItem with ability to upload data about file content dynamically.
     */
    private class MyTreeItem extends TreeItem<Item> {
        /** Item that associates with this tree item. */
        private Item value;
        /** Flag that detects if children items were already uploaded, so there is no need to query them. */
        private boolean childrenLoaded;

        /** Constructs TreeItem from Item: saves item and initializes flag.  */
        public MyTreeItem(Item value) {
            super(value);
            this.value = value;
            childrenLoaded = false;
        }

        /** Tells if this tree item is a leaf, which is the same as if this item associated with not directory. */
        @Override
        public boolean isLeaf() {
            return !value.isDirectory;
        }

        /**
         * Returns tree items that are children of this tree item.
         * @return list of tree items that are children of this tree item
         */
        @Override
        public ObservableList<TreeItem<Item>> getChildren() {
            if (!childrenLoaded) {
                childrenLoaded = true;
                try {
                    super.getChildren().setAll(buildChildren());
                } catch (IOException e) {
                    System.err.println(e.toString());
                    childrenLoaded = false;
                    //return FXCollections.emptyObservableList();
                }
            }
            return super.getChildren();
        }

        /**
         * Creates list of tree item children by querying them from server.
         * @return list of children of this tree item
         * @throws IOException if some error on server happened or it's impossible to read info
         */
        private ObservableList<TreeItem<Item>> buildChildren() throws IOException {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            client.sendQuery(1, value.name, pw);

            String[] response = sw.toString().split(System.lineSeparator());
            ObservableList<TreeItem<Item>> children = FXCollections.observableArrayList();

            if (response.length == 0)
                return children;

            for (String s : response) {
                if (s.isEmpty() || !s.contains(" "))
                    continue;

                TreeItem<Item> child = new MyTreeItem(new Item(value.name, s));
                children.add(child);
            }

            return children;
        }
    }
}
