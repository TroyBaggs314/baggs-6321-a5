/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Troy Baggs
 */

package ucf.assignments;

import com.sun.javafx.binding.StringFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
    private ArrayList<ItemFormat> invList = new ArrayList<ItemFormat>();

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
            System.out.println(iF.getSerialNumber());
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

    private boolean verifySerialNumber(String str)
    {
        for(int i = 0; i < 10; i++)
        {
            if((Character.isLetterOrDigit(str.charAt(i)) == false))
            {
                return false;
            }
        }
        return true;
    }

    private String verifySerialNumber(String str, int j)
    {
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

            /*TableColumn sortcolumn = null;
            TableColumn.SortType st = null;
            if(tableView.getSortOrder().size() > 0)
            {
                sortcolumn = (TableColumn) tableView.getSortOrder().get(0);
                st = sortcolumn.getSortType();
                System.out.println(sortcolumn.toString() + "\t" + st.toString());
            }*/
            addEntry();
        }
    }

    void addEntry()
    {
        final ObservableList<ItemFormat> data = FXCollections.observableArrayList();
        SortedList sortedList = new SortedList(data);

        DecimalFormat df = new DecimalFormat("#.00");
        for(int i = 0; i < getArrayList().size(); i++)
        {
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
    void importChoice()
    {

    }
    @FXML
    void exportChoice()
    {

    }
}
