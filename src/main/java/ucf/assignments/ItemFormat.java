package ucf.assignments;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemFormat {
    private SimpleDoubleProperty value;
    private SimpleStringProperty serialNumber;
    private SimpleStringProperty name;

    ItemFormat(Double value, String serialNumber, String name)
    {
        this.value = new SimpleDoubleProperty(value);
        this.serialNumber = new SimpleStringProperty(serialNumber);
        this.name = new SimpleStringProperty(name);
    }

    public double getValue()
    {
        return value.get();
    }

    public void setValue(double val)
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
