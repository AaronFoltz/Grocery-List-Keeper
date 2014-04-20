package com.aaronfoltz.grocerylist.app;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Item")
public class Item extends ParseObject
{
    public Item()
    {

    }

    public boolean isRetrieved()
    {
        return getBoolean("retrieved");
    }

    public void setRetrieved(final boolean retrieved)
    {
        put("retrieved", retrieved);
    }

    public String getItemName()
    {
        return getString("itemName");
    }

    public void setItemName(final String itemName)
    {
        put("itemName", itemName);
    }

    public Double getPrice()
    {
        return getDouble("price");
    }

    public void setPrice(final Double price)
    {
        put("price", price);
    }

    public Integer getQuantity()
    {
        return getInt("quantity");
    }

    public void setQuantity(final Integer quantity)
    {
        put("quantity", quantity);
    }
}
