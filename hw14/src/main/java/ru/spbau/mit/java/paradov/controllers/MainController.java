package ru.spbau.mit.java.paradov.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.spbau.mit.java.paradov.ftp.FtpGuiClient;
import ru.spbau.mit.java.paradov.util.UtilFunctions;

import java.io.IOException;

/**
 * Controller for main menu and its layout.
 */
public class MainController {
    private static FtpGuiClient client;
    private static String delimiter;
    private static String root;
    private static Stage stage;
    private TreeView<Item> tree;

    public Label path;
    public GridPane grid;

    @FXML
    public void initialize() {
        path.setText(root);
        TreeItem<Item> rootItem = new MyTreeItem(new Item("", root + " true"));
        tree = new TreeView<>(rootItem);
        tree.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    try {
                        TreeItem<Item> item = tree.getSelectionModel().getSelectedItem();
                        if (!item.getValue().isDirectory) {
                            //System.err.println(item.getValue().shortName);
                            client.sendQuery(2, item.getValue().name, null);
                        }
                    } catch (Exception e) {
                        // do nothing
                    }
                }
            }
        });

        grid.add(tree, 0, 1);
    }

    public static void setupMain(String hostName, String portNumber, String delimiter, String root, Stage stage) {
        MainController.root = root;
        MainController.delimiter = delimiter;
        MainController.stage = stage;

        Integer portNum = UtilFunctions.stringToIntOrElse(portNumber, FtpGuiClient.DEFAULT_PORT);
        if (portNum < 0 || portNum >= 65536) {
            portNum = FtpGuiClient.DEFAULT_PORT;
        }

        client = new FtpGuiClient(hostName, portNum);
    }

    /**
     * Event handler for pressing Exit button. It just closes window.
     * @param actionEvent event of pressing Exit button
     */
    public void exit(ActionEvent actionEvent) {
        UtilFunctions.getStageFromButtonActionEvent(actionEvent).close();
    }



    private class Item {
        private String name;
        private String shortName;

        private boolean isDirectory;

        public Item(String path, String response) {
            shortName = response.substring(0, response.lastIndexOf(" "));
            if (path.equals("")) {
                name = shortName;
            } else if (path.equals("/")) {
                name = "/" + shortName;
            } else {
                name = path + delimiter + shortName;
            }

            isDirectory = Boolean.valueOf(response.substring(response.lastIndexOf(" ") + 1));
        }

        @Override
        public String toString() {
            //return shortName + " (" + isDirectory + ")";
            return shortName;
        }
    }

    private class MyTreeItem extends TreeItem<Item> {
        private Item value;
        private boolean childrenLoaded;

        public MyTreeItem(Item value) {
            super(value);
            this.value = value;
            childrenLoaded = false;
        }

        @Override
        public boolean isLeaf() {
            return !value.isDirectory;
        }

        @Override
        public ObservableList<TreeItem<Item>> getChildren() {
            if (!childrenLoaded) {
                childrenLoaded = true;
                try {
                    super.getChildren().setAll(buildChildren(this));
                } catch (IOException e) {
                    System.err.println(e.toString());
                    //return FXCollections.emptyObservableList();
                }
            }
            return super.getChildren();
        }

        private ObservableList<TreeItem<Item>> buildChildren(MyTreeItem treeItem) throws IOException {
            String[] response = client.sendQuery(1, value.name, null);
            ObservableList<TreeItem<Item>> children = FXCollections.observableArrayList();

            if (response.length == 0)
                return children;

            for (String s : response) {
                if (s.equals(""))
                    continue;

                TreeItem<Item> child = new MyTreeItem(new Item(value.name, s));
                children.add(child);
            }

            return children;
        }
    }
}
