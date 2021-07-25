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

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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
    public void testImportTSV() throws Exception
    {
        File file = new File("src/main/resources/ucf/assignments/importtest.txt.txt");
        String fileName = file.getAbsolutePath();
        clickOn("Import");
        clickOn("TSV");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(file.getAbsolutePath());
        clipboard.setContents(stringSelection, stringSelection);
        press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
        push(KeyCode.ENTER);
        TableView tv = lookup("#tableView").queryTableView();
        assertEquals(4,tv.getItems().size());
    }

    @Test
    public void testImportHTML() throws Exception
    {
        File file = new File("src/main/resources/ucf/assignments/importtest.html");
        String fileName = file.getAbsolutePath();
        clickOn("Import");
        clickOn("HTML");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(file.getAbsolutePath());
        clipboard.setContents(stringSelection, stringSelection);
        press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
        push(KeyCode.ENTER);
        TableView tv = lookup("#tableView").queryTableView();
        assertEquals(4,tv.getItems().size());
    }

    @Test
    public void testImportJSON() throws Exception
    {
        File file = new File("src/main/resources/ucf/assignments/exporttest.json");
        String fileName = file.getAbsolutePath();
        clickOn("Import");
        clickOn("JSON");
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(file.getAbsolutePath());
        clipboard.setContents(stringSelection, stringSelection);
        press(KeyCode.CONTROL).press(KeyCode.V).release(KeyCode.V).release(KeyCode.CONTROL);
        push(KeyCode.ENTER);
        TableView tv = lookup("#tableView").queryTableView();
        assertEquals(4,tv.getItems().size());
    }

    @Test
    public void testSorting()
    {
        clickOn("#valField").write("34");
        clickOn("#snField").write("123456789a");
        clickOn("#nameField").write("Harry");
        clickOn("#addNewEntryButton");
        clickOn("#valField").eraseText(2).write("35.0");
        clickOn("#snField").eraseText(1).write("b");
        clickOn("#nameField").eraseText(6).write("Joe");
        clickOn("#addNewEntryButton");
        clickOn("#snField").eraseText(1).write("c");
        clickOn("#addNewEntryButton");
        clickOn("Value");
        clickOn("Value");
        clickOn("Serial Number");
        clickOn("Serial Number");
        clickOn("Name");
        clickOn("Name");
        TableView tv = lookup("#tableView").queryTableView();
        TableColumn sortColumn = (TableColumn) tv.getSortOrder().get(0);
        assertEquals("DESCENDING",sortColumn.getSortType().toString());
    }

    @Test
    public void testSearch() throws Exception
    {
        TableView tv = lookup("#tableView").queryTableView();
        testAddEntry();
        clickOn("#searchInputField").write("123456789a");
        assertEquals(1,tv.getColumns().size());
    }
}
