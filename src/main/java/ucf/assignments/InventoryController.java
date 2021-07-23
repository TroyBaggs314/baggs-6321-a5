package ucf.assignments;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.converter.NumberStringConverter;

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
    private MenuItem addNewEntryButton;

    @FXML
    void addEntry(MouseEvent actionEvent)
    {
        final ObservableList<ItemFormat> data = FXCollections.observableArrayList(
                null,
                null
        );
        tColumnValue.setCellValueFactory(new PropertyValueFactory<ItemFormat,String>("Value"));
        tColumnSerial.setCellValueFactory(new PropertyValueFactory<ItemFormat,String>("SerialNumber"));
        tColumnName.setCellValueFactory(new PropertyValueFactory<ItemFormat,String>("Name"));
        tColumnValue.setCellFactory(TextFieldTableCell.forTableColumn(new NumberStringConverter()));
        tColumnSerial.setCellFactory(TextFieldTableCell.forTableColumn());
        tColumnName.setCellFactory(TextFieldTableCell.forTableColumn());
        tableView.setItems(data);
        setTableEditable();
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
        System.out.println("Editing!");
    }
}
