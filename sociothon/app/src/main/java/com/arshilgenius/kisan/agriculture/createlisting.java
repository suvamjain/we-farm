package com.arshilgenius.kisan.agriculture;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class createlisting extends ActionBarActivity {
    String selected = "";
    EditText ed;
    EditText ed2;
    EditText ed3;
    ImageView i1,i2,i3,i4,i5,i6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createlisting);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ed = (EditText) findViewById(R.id.fname);
        ed2 = (EditText) findViewById(R.id.lname);
        ed3 = (EditText) findViewById(R.id.cname);
        i1 = (ImageView) findViewById(R.id.imageView1);
        i2 = (ImageView) findViewById(R.id.imageView2);
        i3 = (ImageView) findViewById(R.id.imageView3);
        i4 = (ImageView) findViewById(R.id.imageView4);
        i5 = (ImageView) findViewById(R.id.imageView5);
        i6 = (ImageView) findViewById(R.id.imageView6);

        final PopupWindow popupWindow = new PopupWindow(this);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.zonesList));
        // the drop down list is a list view
        ListView listZones = new ListView(this);
        // set our adapter and pass our pop up window contents
        listZones.setAdapter(adapter);
        // set the item click listener
        listZones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ed3.setText(adapter.getItem(i));
                popupWindow.dismiss();
            }
        });
        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(600);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
        popupWindow.setContentView(listZones);

        ed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               popupWindow.showAsDropDown(view, -5, 0);
            }
        });
    }

    public void c(View view) {
        String amount = ed.getText().toString();
        String kg = ed2.getText().toString();
        String loc = ed3.getText().toString();

        if (TextUtils.isEmpty(ed.getText())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                    .setContentText("You Did Not Enter The Amount").show();
        }
        else if (TextUtils.isEmpty(ed2.getText())) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                    .setContentText("You Did Not Enter The Quantity").show();
        }
        else if (TextUtils.isEmpty(ed3.getText()) || ed3.equals("Select Zone")) {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                    .setContentText("You Did Not Enter The Location").show();
        }
        else if(selected.equals(""))
        {
            new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("Oops...")
                    .setContentText("You Did Not Selected Any Crop").show();
        }
        else
        {
//           String  url="https://adaa-45b17.firebaseio.com/ORDERS";
//            Firebase ref = new Firebase(url);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("ORDERS");
            ordering order = new ordering();
            order.setAmount(amount);
            order.setKgs(kg);
            order.setCrop(selected);
            order.setLat("9.9312");
            order.setLongg("76.2673");
            order.setLoc(loc);
            ref.push().setValue(order);
            new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Done...")
                    .setContentText("Crop order placed successfully...").show();
            finish();
        }
    }

    public void im1(View view) {
        clearFields();
        view.setBackgroundResource(R.drawable.btn_bg);
        selected = "rice";
    }

    public void im2(View view) {
        clearFields();
        view.setBackgroundResource(R.drawable.btn_bg);
        selected = "corn";
    }

    public void im3(View view) {
        clearFields();
        view.setBackgroundResource(R.drawable.btn_bg);
        selected = "wheat";
    }

    public void im4(View view) {
        clearFields();
        view.setBackgroundResource(R.drawable.btn_bg);
        selected = "rye";
    }

    public void im5(View view) {
        clearFields();
        view.setBackgroundResource(R.drawable.btn_bg);
        selected = "oat";
    }

    public void im6(View view) {
        clearFields();
        view.setBackgroundResource(R.drawable.btn_bg);
        selected = "flour";
    }

    public void clearFields() {
        i1.setBackgroundResource(0);
        i2.setBackgroundResource(0);
        i3.setBackgroundResource(0);
        i4.setBackgroundResource(0);
        i5.setBackgroundResource(0);
        i6.setBackgroundResource(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
