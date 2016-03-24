package com.example.jane.androidsplitcalc;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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
    }

    @Override
    public void onClick(View v) {
        if (v == addDinerButton){
            TableRow row1 = new TableRow(this); // declare variable called 'row' and we called it TableRow
                                               // and assign it to a new tableRow, with the argument in the main activity - 'this'

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

            row1.addView(et1); // Add customer's name
            row1.addView(et2); // Add he amount dollar paid
            row1.addView(ib); // Add the addItemButton

            int rowIndex = 1; // make the new Customer's name go bellow the last line of the Pre-Tax Bill section
            for (int i = 0; i < dinerList.size(); i++){
                rowIndex += dinerList.get(i).orderList.size(); // increase rowIndex by the size of the orderList
            }

            mainTable.addView(row1, rowIndex );

            TableRow row2 = new TableRow(this);
            TextView tv1 = new TextView(this);

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

            row2.addView(tv1);
            row2.addView(tv2);

            mainTable.addView(row2, rowIndex + dinerList.size() + 11); // make sure that the Split Bill section match the input from above
                                                                       // and also at the end of the list

            Diner diner = new Diner(et1, et2, ib, tv1, tv2); // create a new variable that hold the Diner object,
                                                             // called it 'diner' and set it equal to a new Diner object
                                                             // and pass these 5 arguments above into the constructor
                                                             // help keep track and organize these 5
            dinerList.add(diner); // add whatever is passed as an argument into the dinerList,
                                  // based on the diner (we created on the line above, using the 5 that we generated)
        } else {
            for (int i = 0; i < dinerList.size(); i++){
                if (v == dinerList.get(i).ibAddOrder){
                    TableRow row3 = new TableRow(this); // create a new row called row3

                    EditText emptyEditText = new EditText(this); // create an empty edit Text to make the new order allign correctly with the above
                    emptyEditText.setVisibility(View.INVISIBLE); // for hiding but keeping the place of your view

                    EditText newOrder = new EditText(this);
                    newOrder.setText("$0.00");
                    newOrder.setSelectAllOnFocus(true);
                    newOrder.setInputType(amount1of1.getInputType());
                    newOrder.setGravity(amount1of1.getGravity());
                    newOrder.setLayoutParams(amount1of1.getLayoutParams());
                    newOrder.setWidth(amount1of1.getWidth());
                    newOrder.setOnClickListener(this);
                    newOrder.setOnFocusChangeListener(this);
                    newOrder.requestFocus();

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
        }
    }

    public void updateTotal(){
        for (int i = 0; i < dinerList.size(); i++){
            dinerList.get(i).setName();
            for (int j = 0; j < dinerList.get(i).orderList.size(); j++) {
                dinerList.get(i).updateTotal(dinerList.get(i).orderList
                        .get(j), tipPercentValue);
            }
        }
    }

    public void updateSlider(){
        tipPercentValue = Double.parseDouble(tipPercent
                .getText().toString().replace("%", ""));
        if (tipPercentValue > 50) {
            tipPercentValue = 50.0;
        }
        tipSlider.setProgress((int) tipPercentValue);
        if (tipPercentValue > 0.5) {
            tipPercentValue = tipPercentValue / 100;
        }
    }

    public void calcTax() {
        tax = Double.parseDouble(taxDollar.getText()
                .toString().replace("$", "").replace(",", ""));
        taxDollar.setText("$" + String.format("%,.2f", tax));
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        updateSlider();
        updateTotal();
        calcTax();
        calcGrandTotal();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
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
