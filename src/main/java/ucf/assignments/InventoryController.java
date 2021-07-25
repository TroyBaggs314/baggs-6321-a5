/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Troy Baggs
 */

package ucf.assignments;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.javafx.binding.StringFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class InventoryController {

    @FXML
    private TableView tableView;
    @FXML
    private TableColumn tColumnValue;
    @FXML
    private TableColumn tColumnSerial;
    @FXML
    private TableColumn tColumnName;
    @FXML
    private TextField valField;
    @FXML
    private TextField snField;
    @FXML
    private TextField nameField;
    @FXML
    private Button addNewEntryButton;
    @FXML
    private Button removeEntryButton;

    @FXML
    private TextField searchInputField;
    public ArrayList<ItemFormat> invList = new ArrayList<ItemFormat>();

    public void addArrayList(ItemFormat iF)
    {
        invList.add(iF);
    }

    public void setArrayList(ArrayList<ItemFormat> arr)
    {
        for(int i = 0; i < arr.size(); i++)
        {
            invList.set(i,arr.get(i));
        }
    }

    public ArrayList<ItemFormat> getArrayList()
    {
        return invList;
    }

    @FXML
    void addEntry(MouseEvent actionEvent)
    {
        /*addEntry: create an entirely new observablearraylist for the table viewer to base off
        create an entirely new one to remove any chances of duplicate cells being created or misplaced
        verify that cell being added follows the guidelines and if they don't throw an error and return
        if verified implement listeners for edits in each column
        at the end make the table editable by calling tableEditable()
        */
        final ObservableList<ItemFormat> data = FXCollections.observableArrayList();
        DecimalFormat df = new DecimalFormat("#.00");
        if(verifySerialNumber(snField.getText(),0).equals("Duplicate."))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Duplicate serial number found, try again.",ButtonType.OK);
            alert.showAndWait();
            return;
        }
        else if(verifySerialNumber(snField.getText(),0).equals("Bad format"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong format entered, try again.",ButtonType.OK);
            alert.showAndWait();
            return;
        }
        else if(valField.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid value entered, try again.",ButtonType.OK);
            alert.showAndWait();
            return;
        }
        else if(nameField.getText().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid name entered, try again.",ButtonType.OK);
            alert.showAndWait();
            return;
        }
        Button btn = (Button)actionEvent.getSource();
        if(btn.getId().equals("addNewEntryButton"))
        {
            addArrayList(new ItemFormat(df.format(Double.parseDouble(valField.getText())), snField.getText(), nameField.getText()));
        }
        for(int i = 0; i < getArrayList().size(); i++)
        {
            data.add(getArrayList().get(i));
        }
        SortedList sortedList = new SortedList(data);
        for(int i = 0; i < sortedList.size(); i++)
        {
            ItemFormat iF = (ItemFormat)sortedList.get(i);
        }
        tColumnValue.setCellValueFactory(new PropertyValueFactory<ItemFormat,String>("Value"));
        tColumnSerial.setCellValueFactory(new PropertyValueFactory<ItemFormat,String>("SerialNumber"));
        tColumnName.setCellValueFactory(new PropertyValueFactory<ItemFormat,String>("Name"));
        tColumnValue.setCellFactory(TextFieldTableCell.forTableColumn());
        tColumnSerial.setCellFactory(TextFieldTableCell.forTableColumn());
        tColumnName.setCellFactory(TextFieldTableCell.forTableColumn());

        tColumnValue.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemFormat, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ItemFormat, String> event)
                {
                    ItemFormat iF = event.getRowValue();
                    Double db = Double.parseDouble(event.getNewValue());
                    DecimalFormat df = new DecimalFormat("#.00");
                    iF.setValue(df.format(db));
                    tableView.refresh();
                }
            }
        );
        tColumnSerial.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemFormat, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ItemFormat, String> event)
                {
                    ItemFormat iF = event.getRowValue();
                    String str = event.getNewValue();
                    if(!event.getNewValue().equals(event.getOldValue()))
                    {
                        while (!verifySerialNumber(str, 0).equals(str)) {
                            TextInputDialog alert = new TextInputDialog("XXXXXXXXXX");
                            alert.setTitle("Invalid Serial Number");
                            alert.setHeaderText("Invalid serial number entered");
                            alert.setContentText("Please enter a valid serial number");
                            Optional<String> result = alert.showAndWait();
                            if (result.isPresent()) {
                                str = result.get();
                            }
                        }
                    }
                    iF.setSerialNumber(str);
                    tableView.refresh();
                }
            }
        );
        tColumnName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemFormat, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<ItemFormat, String> event)
                {
                    ItemFormat iF = event.getRowValue();
                    iF.setName(event.getNewValue());
                    tableView.refresh();
                }
            }
        );
        tableView.setItems(sortedList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        setTableEditable();
    }

    private String verifySerialNumber(String str, int j)
    {
        // measly check for first serial length is 10
        // second it is unique and doesn't match any of the current ones
        // and third that it contains only letters and digits
        if(str.length() != 10)
        {
            return "Bad format";
        }
        else if(checkUniqueSerial(str) == false)
        {
            return "Duplicate.";
        }
        else
        {
            for (int i = 0; i < 10; i++)
            {
                if ((Character.isLetterOrDigit(str.charAt(i)) == false))
                {
                    return "Bad format";
                }
            }
        }
        return str;
    }

    private boolean checkUniqueSerial(String str)
    {
        for(int i = 0; i < getArrayList().size(); i++)
        {
            if(getArrayList().get(i).getSerialNumber().equals(str))
            {
                return false;
            }
            //System.out.println(getArrayList().get(i).getSerialNumber() + "!=" + str);
        }
        return true;
    }

    private void editFocusedCell() {
        final TablePosition< ItemFormat, ? > focusedCell = tableView.getFocusModel().getFocusedCell();
        tableView.edit(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    void setTableEditable()
    {
        tableView.setEditable(true);
        tableView.getSelectionModel().cellSelectionEnabledProperty().set(true);

    }

    @FXML
    void editEntry()
    {
        editFocusedCell();
    }

    @FXML
    void removeEntry(MouseEvent actionEvent)
    {
        //if user clicks remove, verify that something is focused
        //then loop through array for matching serial and delete
            //must do it from serial of array and not vice versa because when tables sort,
            // its only the visible list and the index is unchanged
            // and will select the wrong one
        if(tableView.getSelectionModel().getFocusedIndex() != -1)
        {
            for(int i = 0; i < getArrayList().size(); i++)
            {
                ItemFormat iF = (ItemFormat) tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex());
                if(getArrayList().get(i).getSerialNumber().equals(iF.getSerialNumber()))
                {
                    getArrayList().remove(i);
                    break;
                }
            }
            addEntry();
        }
    }

    void clearEntries()
    {
        while(getArrayList().size() > 0)
        {
            getArrayList().remove(0);
        }
        addEntry();
    }

    void addEntry()
    {
        final ObservableList<ItemFormat> data = FXCollections.observableArrayList();
        System.out.println(getArrayList().size());
        for(int i = 0; i < getArrayList().size(); i++)
        {
            System.out.println(getArrayList().get(i));
            data.add(getArrayList().get(i));
        }
        tColumnValue.setCellValueFactory(new PropertyValueFactory<ItemFormat,String>("Value"));
        tColumnSerial.setCellValueFactory(new PropertyValueFactory<ItemFormat,String>("SerialNumber"));
        tColumnName.setCellValueFactory(new PropertyValueFactory<ItemFormat,String>("Name"));
        tColumnValue.setCellFactory(TextFieldTableCell.forTableColumn());
        tColumnSerial.setCellFactory(TextFieldTableCell.forTableColumn());
        tColumnName.setCellFactory(TextFieldTableCell.forTableColumn());

        tColumnValue.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemFormat, String>>() {
                                         @Override
                                         public void handle(TableColumn.CellEditEvent<ItemFormat, String> event)
                                         {
                                             ItemFormat iF = event.getRowValue();
                                             Double db = Double.parseDouble(event.getNewValue());
                                             DecimalFormat df = new DecimalFormat("#.00");
                                             iF.setValue(df.format(db));
                                             tableView.refresh();
                                         }
                                     }
        );
        tColumnSerial.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemFormat, String>>() {
                                          @Override
                                          public void handle(TableColumn.CellEditEvent<ItemFormat, String> event)
                                          {
                                              ItemFormat iF = event.getRowValue();
                                              String str = event.getNewValue();
                                              while(!verifySerialNumber(str, 0).equals(str))
                                              {
                                                  TextInputDialog alert = new TextInputDialog("XXXXXXXXXX");
                                                  alert.setTitle("Invalid Serial Number");
                                                  alert.setHeaderText("Invalid serial number entered");
                                                  alert.setContentText("Please enter a valid serial number");
                                                  Optional<String> result = alert.showAndWait();
                                                  if(result.isPresent())
                                                  {
                                                      str = result.get();
                                                  }
                                              }
                                              iF.setSerialNumber(str);
                                              tableView.refresh();
                                          }
                                      }
        );
        tColumnName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ItemFormat, String>>() {
                                        @Override
                                        public void handle(TableColumn.CellEditEvent<ItemFormat, String> event)
                                        {
                                            ItemFormat iF = event.getRowValue();
                                            iF.setName(event.getNewValue());
                                            tableView.refresh();
                                        }
                                    }
        );
        tableView.setItems(data);
        setTableEditable();
    }

    @FXML
    void searchEntries()
    {
        if(!searchInputField.getText().isEmpty())
        {
            for(int i = 0; i < getArrayList().size(); i++)
            {
                if(searchInputField.getText().length() == 10)
                {
                    ItemFormat iF = (ItemFormat) tableView.getItems().get(i);
                    if (searchInputField.getText().equals(iF.getSerialNumber()))
                    {
                        focusTableCell(i);
                        break;
                    }
                    searchName(i);
                }
                else
                {
                    searchName(i);
                }
            }
        }
    }
    private void searchName(int i)
    {
        ItemFormat iF = (ItemFormat) tableView.getItems().get(i);
        if (searchInputField.getText().equals(iF.getName()))
        {
            focusTableCell(i);
            return;
        }
    }
    private void focusTableCell(int i)
    {
        tableView.getSelectionModel().clearSelection();
        tableView.requestFocus();
        tableView.getSelectionModel().select(i);
        tableView.getFocusModel().focus(1);
    }
    @FXML
    void importChoice(MouseEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Import confirmation");
        alert.setHeaderText("Import a file.");
        alert.setContentText("Choose file format.");

        ButtonType btn1 = new ButtonType("TSV");
        ButtonType btn2 = new ButtonType("HTML");
        ButtonType btn3 = new ButtonType("JSON");
        ButtonType btn4 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btn1,btn2,btn3,btn4);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == btn1)
        {
            imports(0,actionEvent);
        }
        else if(result.get() == btn2)
        {
            imports(1,actionEvent);
        }
        else if(result.get() == btn3)
        {
            imports(2,actionEvent);
        }
    }
    @FXML
    void exportChoice(MouseEvent actionEvent)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Export confirmation");
        alert.setHeaderText("Export to a file.");
        alert.setContentText("Choose output format.");

        ButtonType btn1 = new ButtonType("TSV");
        ButtonType btn2 = new ButtonType("HTML");
        ButtonType btn3 = new ButtonType("JSON");
        ButtonType btn4 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btn1,btn2,btn3,btn4);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == btn1)
        {
            export(0,actionEvent);
        }
        else if(result.get() == btn2)
        {
            export(1,actionEvent);
        }
        else if(result.get() == btn3)
        {
            export(2,actionEvent);
        }
    }

    private void imports(int i, MouseEvent actionEvent)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        if(i == 0)
        {
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV","*.txt"));
        }
        else if(i == 1)
        {
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("HTML","*.html"));
        }
        else if(i == 2)
        {
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON","*.json"));
        }
        final Node source = (Node) actionEvent.getSource();
        Scene scene = source.getScene();
        File file = fileChooser.showOpenDialog(scene.getWindow());
        clearEntries();
        getArrayList().clear();
        if(i == 0)
        {
            loadTSV(file);
        }
        else if(i == 1)
        {
            loadHTML(file);
        }
        else if(i == 2)
        {
            loadJSON(file);
        }
    }

    private void export(int i, MouseEvent actionEvent)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        if(i == 0)
        {
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV","*.txt"));
        }
        else if(i == 1)
        {
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("HTML","*.html"));
        }
        else if(i == 2)
        {
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON","*.json"));
        }
        final Node source = (Node) actionEvent.getSource();
        Scene scene = source.getScene();
        File file = fileChooser.showOpenDialog(scene.getWindow());
        if(i == 0)
        {
            saveTSV(file);
        }
        else if(i == 1)
        {
            saveHTML(file);
        }
        else if(i == 2)
        {
            saveJSON(file);
        }

    }

    private void saveTSV(File file)
    {
        try {
            BufferedWriter wr = Files.newBufferedWriter(file.toPath());
            for(int i = 0; i < getArrayList().size(); i++)
            {
                System.out.println(getArrayList().get(i).getValue() +"\t" + getArrayList().get(i).getSerialNumber()  +"\t" + getArrayList().get(i).getName() + "\n");
                wr.write(getArrayList().get(i).getValue() +"\t" + getArrayList().get(i).getSerialNumber()  +"\t" + getArrayList().get(i).getName() + "\n");
            }
            wr.close();
        }
        catch (Exception e)
        {
            System.out.println("Failed export");
        }
    }

    private void saveHTML(File file)
    {
        try
        {
            Writer writer = new FileWriter(file);
            writer.write("<html>\n<body>\n<table style=\"width:100%\">\n<tr>\n<th>Value</th>\n<th>Serial Number</th>\n<th>Name</th>\n</tr>");
            for(int i = 0; i < getArrayList().size();i++)
            {
                writer.write("<tr>\n<td>" + getArrayList().get(i).getValue() + "</td>\n" + "<td>" + getArrayList().get(i).getSerialNumber() + "</td>\n" +"<td>" + getArrayList().get(i).getName() + "</td>\n</tr>");
            }
            writer.write("</table>\n</body>\n</html>");
            writer.flush();
            writer.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    private void saveJSON(File file)
    {
        try
        {
            Writer writer = new FileWriter(file);
            Gson gson = new GsonBuilder().create();
            gson.toJson(getArrayList(), writer);
            writer.flush();
            writer.close();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    private void loadTSV(File file)
    {
        try
        {
            Scanner sc = new Scanner(file);
            do
            {
                ItemFormat iF = new ItemFormat(sc.next(), sc.next(), sc.next());
                getArrayList().add(iF);
            }while(sc.hasNext());
            addEntry();
        }
        catch (Exception e)
        {

        }
    }

    private void loadHTML(File file){

        try
        {
            Document doc = Jsoup.parse(file, "UTF-8");
            Elements ele = doc.select("td");
            int j = 0;
            System.out.println(ele.size());
            for(int i = 0; i < (ele.size()/3); i++)
            {
                System.out.println(ele.get(j).text() + "\t" + ele.get(j + 1).text() + "\t" + ele.get(j + 2).text());
                ItemFormat iF = new ItemFormat(ele.get(j).text(),ele.get(j + 1).text(),ele.get(j + 2).text());
                getArrayList().add(iF);
                j+= 3;
                System.out.println("Done");
            }
            System.out.println("Calling addEntry");
            addEntry();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    private void loadJSON(File file)
    {
        Gson gson = new Gson();
        System.out.println("Trying");
        try
        {
            ItemFormat[] itemArray = gson.fromJson(new FileReader(file),ItemFormat[].class);
            System.out.println("length: " + itemArray.length);
            for(int i = 0; i < itemArray.length; i++)
            {
                getArrayList().add(itemArray[i]);
            }
            addEntry();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}
