package com.aaronfoltz.grocerylist.app;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

class PriceDialogTextWatcher implements TextWatcher
{
    private final EditText etPrice;
    private final TextView tvTotal;
    private final EditText etQuantity;
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
        if (etPrice.getText() != null)
        {
            double itemTotal = getItemTotal(etPrice, etQuantity);
            // Text has changed, update the total on the fly
            tvTotal.setText(GeneralUtils.getFormattedValue(itemTotal));
            item.setTotal(itemTotal);
        }
    }

    private double getItemTotal(final EditText etPrice, final EditText etQuantity)
    {
        // Default quantity to 1, so they don't have to enter anything (the hint doesn't work as a default value
        int itemQuantity = 1;
        if (etQuantity.getText() != null && etQuantity.getText().length() != 0)
        {
            itemQuantity = Integer.parseInt(etQuantity.getText().toString());
        }

        double itemPrice = 0.0;
        if (etPrice.getText() != null && etPrice.getText().length() != 0)
        {
            itemPrice = Double.parseDouble(etPrice.getText().toString());
        }

        return itemPrice * itemQuantity;
    }
}
