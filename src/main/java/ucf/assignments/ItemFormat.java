/*
 *  UCF COP3330 Summer 2021 Assignment 5 Solution
 *  Copyright 2021 Troy Baggs
 */

package ucf.assignments;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemFormat implements Comparable<ItemFormat> {
    private String value;
    private String serialNumber;
    private String name;

    ItemFormat(String value, String serialNumber, String name)
    {
        this.value = new String(value);
        this.serialNumber = new String(serialNumber);
        this.name = new String(name);
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String val)
    {
        this.value = (val);
    }

    public String getSerialNumber()
    {
        return serialNumber;
    }

    public void setSerialNumber(String val)
    {
        this.serialNumber=(val);
    }
    public String getName()
    {
        return name;
    }

    public void setName(String val)
    {
        this.name=(val);
    }

    @Override
    public int compareTo(ItemFormat compareIF) {
        String compareSN=((ItemFormat)compareIF).getSerialNumber();
        /* For Ascending order*/
        return (this.serialNumber.compareTo(compareSN));

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }

    @Override
    public String toString()
    {
        return "value : " + getValue() + ",\nserial_number=" + getSerialNumber() + ", name =" + getName() +"]";
    }
}
