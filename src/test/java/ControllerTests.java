/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Troy Baggs
 */

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.TableViewMatchers;
import ucf.assignments.ItemFormat;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ControllerTests extends ApplicationTest
{
    @Override
    public void start (Stage stage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(new File("src/main/resources/ucf/assignments/Inventory.fxml").toURI().toURL());
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Inventory Manager");
        stage.setScene(scene);
        stage.show();
    }

    @Test
    public void testAddEntry()
    {
        clickOn("#valField").write("34");
        clickOn("#snField").write("123456789a");
        clickOn("#nameField").write("Harry");
        clickOn("#addNewEntryButton");
        TableView tv = lookup("#tableView").queryTableView();
        assertEquals(1,tv.getItems().size());
    }

    @Test
    public void testRemoveEntry()
    {
        clickOn("#valField").write("34");
        clickOn("#snField").write("123456789a");
        clickOn("#nameField").write("Harry");
        clickOn("#addNewEntryButton");
        clickOn("#valField").eraseText(2).write("35.0");
        clickOn("#snField").eraseText(10).write("123456789b");
        clickOn("#nameField").eraseText(6).write("Joe");
        clickOn("#addNewEntryButton");
        clickOn("#snField").eraseText(10).write("123456789c");
        clickOn("#addNewEntryButton");
        clickOn("#removeEntryButton");
        TableView tv = lookup("#tableView").queryTableView();
        assertEquals(2,tv.getItems().size());
    }

    @Test
    public void testAddEntryValueError() throws Exception
    {
        clickOn("#addNewEntryButton");
        clickOn("OK");
    }

    @Test
    public void testAddEntrySerialError() throws Exception
    {
        clickOn("#valField").write("34");
        clickOn("#snField").write("123456789");
        clickOn("#nameField").write("Harry");
        clickOn("#addNewEntryButton");
        clickOn("OK");
    }

    @Test
    public void testAddEntryNameError() throws Exception
    {
        clickOn("#valField").write("34");
        clickOn("#snField").write("123456789a");
        clickOn("#nameField").write("");
        clickOn("#addNewEntryButton");
        clickOn("OK");
    }

    @Test
    public void testImport() throws Exception
    {
        File file = new File("src/main/resources/ucf/assignments/exporttest.json");
        String fileName = file.getAbsolutePath();
        clickOn("#userInput").type(KeyCode.S).type(KeyCode.R).type(KeyCode.C).type(KeyCode.SLASH).type(KeyCode.M).type(KeyCode.A).type(KeyCode.I).type(KeyCode.N).type(KeyCode.SLASH).type(KeyCode.R).type(KeyCode.E).type(KeyCode.S).type(KeyCode.O).type(KeyCode.U).type(KeyCode.R).type(KeyCode.C).type(KeyCode.E).type(KeyCode.S).type(KeyCode.SLASH).type(KeyCode.U).type(KeyCode.C).type(KeyCode.F).type(KeyCode.SLASH).type(KeyCode.A).type(KeyCode.S).type(KeyCode.S).type(KeyCode.I).type(KeyCode.G).type(KeyCode.N).type(KeyCode.M).type(KeyCode.E).type(KeyCode.N).type(KeyCode.T).type(KeyCode.S).type(KeyCode.SLASH).type(KeyCode.I).type(KeyCode.M).type(KeyCode.P).type(KeyCode.O).type(KeyCode.R).type(KeyCode.T).type(KeyCode.T).type(KeyCode.E).type(KeyCode.S).type(KeyCode.T);

        clickOn("#importButton");
        FxAssert.verifyThat("#accordion", (Accordion acc) ->
        {
            String name = acc.getPanes().get(6).getText();
            return name.equals("title 7");
        });
    }
}
