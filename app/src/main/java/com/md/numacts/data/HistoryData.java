package com.md.numacts.data;

/**
 * Created by Danish Ansari on 12-Oct-17.
 */

public class HistoryData

{
    String number, fact, type;

    public HistoryData(String number, String fact)
    {
        this.number = number;
        this.fact = fact;
    }

    public String getNumber()
    {
        return number;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public String getFact()
    {
        return fact;
    }

    public void setFact(String fact)
    {
        this.fact = fact;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
