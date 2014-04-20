package com.aaronfoltz.grocerylist.app;

import java.text.NumberFormat;

public class GeneralUtils
{
    private static final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    protected static String getFormattedValue(final double value)
    {
        return formatter.format(value);
    }
}
