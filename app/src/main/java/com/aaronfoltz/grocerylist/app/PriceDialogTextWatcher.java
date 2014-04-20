package com.aaronfoltz.grocerylist.app;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

class PriceDialogTextWatcher implements TextWatcher
{
    private final EditText etPrice;
    private final EditText etQuantity;
    private final TextView tvTotal;
    private final Item item;

    public PriceDialogTextWatcher(final View alertView, final Item item)
    {
        this.etPrice = (EditText) alertView.findViewById(R.id.etPrice);
        this.etQuantity = (EditText) alertView.findViewById(R.id.etQuantity);
        this.tvTotal = (TextView) alertView.findViewById(R.id.tvPopupTotal);
        this.item = item;
    }

    @Override
    public void beforeTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3)
    {
    }

    @Override
    public void onTextChanged(final CharSequence charSequence, final int i, final int i2, final int i3)
    {
    }

    @Override
    public void afterTextChanged(final Editable editable)
    {
        double itemPrice = getItemPrice();
        int itemQuantity = getItemQuantity();
        // Text has changed, update the displayed total on the fly
        tvTotal.setText(GeneralUtils.getFormattedValue(itemPrice * itemQuantity));

        // Update the item's properties
        item.setPrice(itemPrice);
        item.setQuantity(itemQuantity);
    }

    private double getItemPrice()
    {
        double itemPrice = 0.0;
        if (etPrice.getText() != null && etPrice.getText().length() != 0)
        {
            itemPrice = Double.parseDouble(etPrice.getText().toString());
        }
        return itemPrice;
    }

    private int getItemQuantity()
    {
        // Default quantity to 1, so they don't have to enter anything (the hint doesn't work as a default value
        int itemQuantity = 1;
        if (etQuantity.getText() != null && etQuantity.getText().length() != 0)
        {
            itemQuantity = Integer.parseInt(etQuantity.getText().toString());
        }
        return itemQuantity;
    }
}
