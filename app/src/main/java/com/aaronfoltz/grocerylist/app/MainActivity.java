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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity implements AdapterView.OnItemClickListener
{
    private EditText mTaskInput;
    private ListView mListView;
    private TaskAdapter mAdapter;
    private TextView tvSubTotal, tvTax1, tvTax2, tvTotal;
    private double subTotal = 0.0;
    private double tax1 = 0.0;
    private double tax2 = 0.0;
    private double total = 0.0;
    private static final NumberFormat formatter = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(this, "esOZW5wp4y1J8tWnRUXspkGGmLeRFqcuopyM5Qwf", "KZ7Bg47x7cpq6GIDDUbfKn1k8Bz2UA6BT7JaMK1b");
        ParseAnalytics.trackAppOpened(getIntent());
        ParseObject.registerSubclass(Task.class);

        // Get item references
        mTaskInput = (EditText) findViewById(R.id.task_input);
        mListView = (ListView) findViewById(R.id.task_list);
        tvSubTotal = (TextView) findViewById(R.id.tvSubtotal);
        tvTax1 = (TextView) findViewById(R.id.tvTax1);
        tvTax2 = (TextView) findViewById(R.id.tvTax2);
        tvTotal = (TextView) findViewById(R.id.tvTotal);

        // Default values
        tvSubTotal.setText(getFormattedValue(subTotal));
        tvTax1.setText(getFormattedValue(tax1));
        tvTax2.setText(getFormattedValue(tax2));
        tvTotal.setText(getFormattedValue(total));

        // Task List View Setup
        mAdapter = new TaskAdapter(this, new ArrayList<Task>());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        updateData();
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createTask(final View v) {
        if (mTaskInput.getText() != null && mTaskInput.getText().length() > 0){
            Task t = new Task();
            t.setDescription(mTaskInput.getText().toString());
            t.setCompleted(false);
            t.saveEventually();
            mTaskInput.setText("");
            mAdapter.insert(t, 0);
        }
    }

    public void updateData(){
        ParseQuery<Task> query = ParseQuery.getQuery(Task.class);
        query.findInBackground(new FindCallback<Task>() {

            @Override
            public void done(final List<Task> tasks, final ParseException e) {
                if(tasks != null){
                    mAdapter.clear();
                    mAdapter.addAll(tasks);
                }
            }
        });
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        Task task = mAdapter.getItem(i);
        TextView taskDescription = (TextView) view.findViewById(R.id.task_description);

        // Show the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title")
               .setCancelable(true)
               .setPositiveButton("Add", new DialogInterface.OnClickListener()
               {

                   @Override
                   public void onClick(final DialogInterface dialog, final int id)
                   {
                   }
               });

        builder.setView(getLayoutInflater().inflate(R.layout.popup, null));

        final AlertDialog alert = builder.create();
        alert.show();

        task.setCompleted(!task.isCompleted());

        if(task.isCompleted()){
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            taskDescription.setPaintFlags(taskDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }

        task.saveEventually();
    }

    private String getFormattedValue(final double value)
    {
        return formatter.format(value);
    }
}
