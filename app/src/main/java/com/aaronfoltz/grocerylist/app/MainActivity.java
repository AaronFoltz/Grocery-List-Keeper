package com.aaronfoltz.grocerylist.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener
{
    public static final String APP_ID = "esOZW5wp4y1J8tWnRUXspkGGmLeRFqcuopyM5Qwf";
    public static final String CLIENT_KEY = "KZ7Bg47x7cpq6GIDDUbfKn1k8Bz2UA6BT7JaMK1b";
    public static final double DEFAULT_DOUBLE_VAL = 0.0;

    private EditText mItemInput;
    private ListView mItemListView;
    private ItemAdapter mItemAdapter;
    private TextView tvSubTotal, tvTax1, tvTax2, tvTotal;
    private double subTotal = DEFAULT_DOUBLE_VAL;
    private double tax1 = DEFAULT_DOUBLE_VAL;
    private double tax2 = DEFAULT_DOUBLE_VAL;
    private double total = DEFAULT_DOUBLE_VAL;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, APP_ID, CLIENT_KEY);
        ParseAnalytics.trackAppOpened(getIntent());
        ParseObject.registerSubclass(Item.class);

        // Get item references
        mItemInput = (EditText) findViewById(R.id.item_input);
        mItemListView = (ListView) findViewById(R.id.item_list);
        tvSubTotal = (TextView) findViewById(R.id.tvSubtotal);
        tvTax1 = (TextView) findViewById(R.id.tvTax1);
        tvTax2 = (TextView) findViewById(R.id.tvTax2);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        // Default values
        tvSubTotal.setText(GeneralUtils.getFormattedValue(subTotal));
        tvTax1.setText(GeneralUtils.getFormattedValue(tax1));
        tvTax2.setText(GeneralUtils.getFormattedValue(tax2));
        tvTotal.setText(GeneralUtils.getFormattedValue(total));

        // Item List View Setup
        mItemAdapter = new ItemAdapter(this, new ArrayList<Item>());
        mItemListView.setAdapter(mItemAdapter);
        mItemListView.setOnItemClickListener(this);

        updateData();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createItemEntry(final View v)
    {
        if (mItemInput.getText() != null && mItemInput.getText().length() > 0)
        {
            Item t = new Item();
            t.setItemName(mItemInput.getText().toString());
            t.setRetrieved(false);
            t.saveEventually();
            mItemInput.setText("");
            mItemAdapter.insert(t, 0);
        }
    }

    public void updateData()
    {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.findInBackground(new FindCallback<Item>()
        {

            @Override
            public void done(final List<Item> items, final ParseException e)
            {
                if (items != null)
                {
                    mItemAdapter.clear();
                    mItemAdapter.addAll(items);
                }
            }
        });
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View rowView, final int i, final long l)
    {
        final Item item = mItemAdapter.getItem(i);

        // If already completed, just mark it as not completed
        if (item.isRetrieved())
        {
            handleItemState(rowView, item);
            handleSummaryState(item);
        }
        else
        {
            // Not already completed, show the dialog
            // Show the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            // Setup the view
            View alertView = getLayoutInflater().inflate(R.layout.popup, null);
            builder.setView(alertView);

            // Get references
            final EditText etPrice = (EditText) alertView.findViewById(R.id.etPrice);
            final EditText etQuantity = (EditText) alertView.findViewById(R.id.etQuantity);
            final TextView tvTotal = (TextView) alertView.findViewById(R.id.tvPopupTotal);

            // Setup the dialog builder
            builder.setTitle("Enter Price")
                   .setCancelable(true)
                   .setPositiveButton("Add", new DialogInterface.OnClickListener()
                   {
                       @Override
                       public void onClick(final DialogInterface dialog, final int id)
                       {
                           handleItemState(rowView, item);
                           handleSummaryState(item);
                       }
                   })
                   .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                   {

                       @Override
                       public void onClick(final DialogInterface dialog, final int id)
                       {

                           dialog.cancel();
                       }
                   });

            // Popup EditText events
            etPrice.addTextChangedListener(new PriceDialogTextWatcher(alertView, item));

            // Popup EditText events
            etQuantity.addTextChangedListener(new PriceDialogTextWatcher(alertView, item));

            // Popup total
            tvTotal.setText(GeneralUtils.getFormattedValue(DEFAULT_DOUBLE_VAL));

            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Handle updates to the summary info:  total, subtotal, etc.
     *
     * @param item the item object
     */
    private void handleSummaryState(final Item item)
    {
        double itemTotal = item.getPrice() * item.getQuantity();
        total = total + itemTotal;
        subTotal = subTotal + itemTotal;

        // Update the displayed summary values
        tvSubTotal.setText(GeneralUtils.getFormattedValue(subTotal));
        tvTotal.setText(GeneralUtils.getFormattedValue(total));
    }

    /**
     * Handle updates to the item state (i.e., the row item).
     * @param view the row_item's view
     * @param item the item object
     */
    private void handleItemState(final View view, final Item item)
    {
        TextView itemDescription = (TextView) view.findViewById(R.id.item_description);

        item.setRetrieved(!item.isRetrieved());

        if (item.isRetrieved())
        {
            itemDescription.setPaintFlags(itemDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            itemDescription.setPaintFlags(
                    itemDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        item.saveEventually();
    }
}
