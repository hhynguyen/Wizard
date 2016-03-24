package com.example.jane.Wizard;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Diner {

    public EditText etName;
    public EditText etFirstOrder;
    public ImageButton ibAddOrder;
    public TextView tvName;
    public TextView tvSplitBill;
    public ArrayList<EditText> orderList;

    public double total;

    public Diner(EditText et1, EditText et2, ImageButton ib, TextView tv1, TextView tv2){

        // declare objects from main activity class
        total = 0.0;
        orderList = new ArrayList<EditText>(); // an array list to keep track of each Diner's orders
        etName = et1;
        etFirstOrder = et2;
        ibAddOrder = ib;
        tvName = tv1;
        tvSplitBill = tv2;

        orderList.add(etFirstOrder);
    }

    //set name, set text on all name related fields
    public void setName(){
        tvName.setText(etName.getText().toString()); // get the the customer's name on the Pre-Tax Bill section
                                                     // and set a string version of that the in Split Bill section
    }

    //add new item for a diner
    public void newOrder(EditText newOrder){
        orderList.add(newOrder); // add new item to order list
    }

    public void updateTotal(EditText toBeUpdated, double tip) {
        total = 0.0; // reset the total to zero and re-calculate the total every time we run
        toBeUpdated.setText("$" + String.format("%,.02f", editTextToDouble(toBeUpdated)));
        for (int i = 0; i < orderList.size(); i++) {
            total += editTextToDouble(orderList.get(i)); // add the new item cost to the total
                                                         // first we need to take the text from above, convert it to string
                                                         // then lastly into double
        }
        tvSplitBill.setText("$" + String.format("%,.2f", total * (1 + tip))); // make a $ sign in the front of the total and make the total 2-decimal number
    }

    public void updateTotal(double tip) {
        updateTotal(etFirstOrder, tip);
    }

    public double editTextToDouble(EditText et){
        double db = 0.0;
        db = Double.parseDouble(et.getText().toString().replace("$", "").replace(",", ""));
        return db;
    }

    public void setTotalText(double tip, double tax) {
        tvSplitBill.setText("$" + String.format("%,.2f", total * (1 + tip) + total * tax));
    }

}
