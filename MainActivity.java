package com.example.jane.Wizard;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener, SeekBar.OnSeekBarChangeListener {

    private double tipPercentValue;
    private EditText tipPercent;
    private SeekBar tipSlider;

    private double grandTotal;
    private double totalTip;
    private TextView textGTotal;
    private TextView tipDollar;

    private double tax;
    private EditText taxDollar;
    private double taxPercentValue;

    private TableLayout mainTable;
    private ImageButton addDinerButton;
    private EditText firstCustomer;
    private EditText amount1of1;
    private ImageButton addButton1;
    private TextView textSplit1;
    private TextView textSplit1Dollar;
    private ArrayList<Diner> dinerList; // create an array list for <Diner> to organise the objects inside

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tipPercentValue = 0.15;
        grandTotal = 0.0;
        totalTip = 0.0;
        tax = 0.0;
        taxPercentValue = 0.0;

        //get ids
        mainTable = (TableLayout)findViewById(R.id.mainTable);
        addDinerButton = (ImageButton)findViewById(R.id.addDinerButton);
        firstCustomer = (EditText) findViewById(R.id.firstCustomer);
        amount1of1 = (EditText) findViewById(R.id.amount1of1);
        textSplit1 = (TextView) findViewById(R.id.textSplit1);
        textSplit1Dollar = (TextView) findViewById(R.id.textSplit1Dollar);
        addButton1 = (ImageButton) findViewById(R.id.addButton1);
        tipPercent = (EditText) findViewById(R.id.tipPercent);
        tipSlider = (SeekBar) findViewById(R.id.tipSlider);
        textGTotal = (TextView) findViewById(R.id.textGTotal);
        tipDollar = (TextView) findViewById(R.id.tipDollar);
        taxDollar = (EditText) findViewById(R.id.taxDollar);
        Button resetButton = (Button)findViewById(R.id.resetButton);

        dinerList = new ArrayList<Diner>();
        Diner diner = new Diner(firstCustomer, amount1of1, addButton1, textSplit1, textSplit1Dollar); // first customer
        dinerList.add(diner);

        // set up listener
        firstCustomer.setOnFocusChangeListener(this);
        taxDollar.setOnFocusChangeListener(this);
        amount1of1.setOnFocusChangeListener(this);
        addDinerButton.setOnClickListener(this); // make main activity listen to addDinerButton
        addButton1.setOnClickListener(this);
        tipSlider.setOnSeekBarChangeListener(this);
        taxDollar.setOnFocusChangeListener(this);
        resetButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == addDinerButton){
            //create table structure for Pre-tax Bill section, called row1
            TableRow row1 = new TableRow(this);

            //create diner components
            EditText et1 = new EditText(this);
            et1.setText("Customer");
            et1.setSelectAllOnFocus(true);
            et1.setInputType(firstCustomer.getInputType());
            et1.setGravity(firstCustomer.getGravity());
            et1.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            et1.setLayoutParams(firstCustomer.getLayoutParams());
            et1.setWidth(firstCustomer.getWidth());
            et1.setOnFocusChangeListener(this);
            et1.requestFocus();

            EditText et2 = new EditText(this);
            et2.setText("$0.00");
            et2.setSelectAllOnFocus(true);
            et2.setGravity(amount1of1.getGravity());
            et2.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            et2.setInputType(amount1of1.getInputType());
            et2.setLayoutParams(amount1of1.getLayoutParams());
            et2.setWidth(amount1of1.getWidth());
            et2.setOnFocusChangeListener(this);
            et2.requestFocus();

            ImageButton ib = new ImageButton(this);
            ib.setImageResource(R.drawable.additem);
            ib.setOnClickListener(this);

            //compose table
            row1.addView(et1); // Add customer's name
            row1.addView(et2); // Add he amount dollar paid
            row1.addView(ib); // Add the addItemButton

            int rowIndex = 1; // make the new Customer's name go bellow the last line of the Pre-Tax Bill section
            for (int i = 0; i < dinerList.size(); i++){
                rowIndex += dinerList.get(i).orderList.size(); // increase rowIndex by the size of the orderList
            }

            mainTable.addView(row1, rowIndex );

            //create table structure for Split Bill section, called row2
            TableRow row2 = new TableRow(this);
            TextView tv1 = new TextView(this);

            // create diner components corresponding to customer name and their orders
            tv1.setText(et1.getText().toString());
            tv1.setGravity(textSplit1.getGravity());
            tv1.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            tv1.setTextSize(17);
            tv1.setOnFocusChangeListener(this);

            TextView tv2 = new TextView(this);
            tv2.setText(et2.getText().toString());
            tv2.setGravity(textSplit1Dollar.getGravity());
            tv2.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            tv2.setTextSize(17);
            tv2.setOnFocusChangeListener(this);

            //compose table
            row2.addView(tv1);
            row2.addView(tv2);

            mainTable.addView(row2, rowIndex + dinerList.size() + 11); // make sure that the Split Bill section match the input from above
                                                                       // and also at the end of the list

            // Create first diner
            Diner diner = new Diner(et1, et2, ib, tv1, tv2); // create a new variable that hold the Diner object,
                                                             // help keep track and organize these 5 objects
            dinerList.add(diner); // add whatever is passed as an argument into the dinerList,
                                  // based on the diner (we created on the line above, using the 5 that we generated)
        } else {
            for (int i = 0; i < dinerList.size(); i++){
                if (v == dinerList.get(i).ibAddOrder){
                    //create table structure for adding new item, called row3
                    TableRow row3 = new TableRow(this);

                    EditText emptyEditText = new EditText(this); // create an empty edit Text to make the new order allign correctly with the above
                    emptyEditText.setVisibility(View.INVISIBLE); // for hiding but keeping the place of your view

                    EditText newOrder = new EditText(this);
                    newOrder.setText("$0.00");
                    newOrder.setSelectAllOnFocus(true);
                    newOrder.setInputType(amount1of1.getInputType());
                    newOrder.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
                    newOrder.setGravity(amount1of1.getGravity());
                    newOrder.setLayoutParams(amount1of1.getLayoutParams());
                    newOrder.setWidth(amount1of1.getWidth());
                    newOrder.setOnClickListener(this);
                    newOrder.setOnFocusChangeListener(this);
                    newOrder.requestFocus();

                    //compose table
                    row3.addView(emptyEditText);
                    row3.addView(newOrder);

                    int rowIndex2 = 1;
                    for (int j = 0; j <= i; j++){
                        rowIndex2 += dinerList.get(j).orderList.size();
                    }

                    mainTable.addView(row3, rowIndex2); // add to main table
                    dinerList.get(i).newOrder(newOrder);
                }
            }
        } if (v.getId() == R.id.resetButton) {
            // reset the app
            Intent intent = getIntent();
            overridePendingTransition(0, 0);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);
            startActivity(intent);
        }
    }

    public void updateTotal(){
        for (int i = 0; i < dinerList.size(); i++){
            dinerList.get(i).setName();
            for (int j = 0; j < dinerList.get(i).orderList.size(); j++) {
                dinerList.get(i).updateTotal(dinerList.get(i).orderList.get(j), tipPercentValue);
            }
        }
    }

    public void updateSlider(){
        tipPercentValue = Double.parseDouble(tipPercent.getText().toString().replace("%", ""));
        if (tipPercentValue > 50) {
            tipPercentValue = 50.0; // even when the percent entered by the user is > 50, it'll be automatically set to 50% only
        }
        tipSlider.setProgress((int) tipPercentValue);
        if (tipPercentValue > 0.5) {
            tipPercentValue = tipPercentValue / 100; // make sure that the tip won't go beyond 50%
        }
    }

    public void calcTax() {
        // calculate tax
        tax = Double.parseDouble(taxDollar.getText().toString().replace("$", "").replace(",", ""));
        taxDollar.setText("$" + String.format("%,.2f", tax)); // set the format to 2 decimal places with % sign
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // make sure that every time a diner or an item is added or the slider is changed
        // the app will run through everything to update the data and re-calculate the grand total, tip total, and the total in Split Bill section
        updateSlider();
        updateTotal();
        calcTax();
        calcGrandTotal();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // set and calculate the tip percentage
        if (seekBar == tipSlider) {
            tipPercentValue = progress;
            tipPercent.setText(String.format("%.0f", tipPercentValue) + "%");
            tipPercentValue = tipPercentValue / 100;
            for (int i = 0; i < dinerList.size(); i++) {
                dinerList.get(i).updateTotal(tipPercentValue);
            }
            calcGrandTotal();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // not needed
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        // not needed
    }

    public void calcGrandTotal() {
        // calculate the grand total and tax
        grandTotal = 0.0;
        for (int i = 0; i < dinerList.size(); i++) {
            grandTotal += dinerList.get(i).total;
        }
        taxPercentValue = tax / grandTotal;
        for (int i = 0; i < dinerList.size(); i++) {
            dinerList.get(i).setTotalText(tipPercentValue, taxPercentValue);
        }
        totalTip = grandTotal * tipPercentValue;
        tipDollar.setText("$" + String.format("%,.2f", totalTip));
        textGTotal.setText("$" + String.format("%,.2f", grandTotal + totalTip + tax));
    }
}
