/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Troy Baggs
 */

package ucf.assignments;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemFormat {
    private SimpleStringProperty value;
    private SimpleStringProperty serialNumber;
    private SimpleStringProperty name;

    ItemFormat(String value, String serialNumber, String name)
    {
        this.value = new SimpleStringProperty(value);
        this.serialNumber = new SimpleStringProperty(serialNumber);
        this.name = new SimpleStringProperty(name);
    }

    public String getValue()
    {
        return value.get();
    }

    public void setValue(String val)
    {
        this.value.set(val);
    }

    public String getSerialNumber()
    {
        return serialNumber.get();
    }

    public void setSerialNumber(String val)
    {
        this.serialNumber.set(val);
    }
    public String getName()
    {
        return name.get();
    }

    public void setName(String val)
    {
        this.name.set(val);
    }
}
