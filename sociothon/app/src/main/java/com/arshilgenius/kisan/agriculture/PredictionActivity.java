package com.arshilgenius.kisan.agriculture;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.AppCompatTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Range;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PredictionActivity extends AppCompatActivity implements TextWatcher, View.OnFocusChangeListener {

    private AppCompatEditText temp,zone,pH,nitro,phos,potass;
    private AppCompatEditText village;
    private AppCompatSpinner spinnerStartMonth,spinnerEndMonth;
    private RelativeLayout getPredictionForm;
    private Handler handler;
    private boolean tempHasEdited = false, zoneHasEdited = false, pHHasEdited = false, nitroHasEdited = false, potassHasEdited = false, phosHasEdited = false;
    private boolean shouldShowError = false;
    private FrameLayout frameLayout;
    private DatabaseReference ref;
    ViewDialog viewDialog;
    public List<Crop> crop_list;
    public HashMap<String,Integer> mnthMap;


    String[] msgs= {"Estimating Soil Type ...", "Predicting Suitable Crops ...","Loading Crop Details ..."};
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ref = FirebaseDatabase.getInstance().getReference().child("crops");
        crop_list = new ArrayList<>();
        getPredictionForm = (RelativeLayout) findViewById(R.id.get_prediction_form);
        frameLayout = (FrameLayout) findViewById(R.id.frameCropPredicted);
        temp = (AppCompatEditText) findViewById(R.id.temperature);
        zone =  (AppCompatEditText)findViewById(R.id.zone);
        village =  (AppCompatEditText) findViewById(R.id.village);
        pH =  (AppCompatEditText)findViewById(R.id.pH);
        nitro =  (AppCompatEditText)findViewById(R.id.nitro);
        potass =  (AppCompatEditText) findViewById(R.id.potass);
        phos =  (AppCompatEditText) findViewById(R.id.phosp);

        mnthMap = new HashMap<>();
        mnthMap.put("Jan",1);
        mnthMap.put("Feb",2);
        mnthMap.put("Mar",3);
        mnthMap.put("Apr",4);
        mnthMap.put("May",5);
        mnthMap.put("Jun",6);
        mnthMap.put("Jul",7);
        mnthMap.put("Aug",8);
        mnthMap.put("Sep",9);
        mnthMap.put("Oct",10);
        mnthMap.put("Nov",11);
        mnthMap.put("Dec",12);


        spinnerStartMonth = (AppCompatSpinner) findViewById(R.id.strtMnthSpinner);
        spinnerEndMonth = (AppCompatSpinner) findViewById(R.id.endMnthSpinner);

        final SpinnerAdapter spinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.months, R.layout.list_spinner);
        spinnerStartMonth.setAdapter(spinnerAdapter);
        spinnerEndMonth.setAdapter(spinnerAdapter);

        //for demo purpose set start month to current month and end to after 4 months
        spinnerStartMonth.setSelection(4);
        spinnerEndMonth.setSelection(8);

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
                zone.setText(adapter.getItem(i));
                popupWindow.dismiss();
            }
        });
        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(600);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
        popupWindow.setContentView(listZones);

        temp.addTextChangedListener(this);
        zone.addTextChangedListener(this);
        pH.addTextChangedListener(this);
        nitro.addTextChangedListener(this);
        potass.addTextChangedListener(this);
        phos.addTextChangedListener(this);

        temp.setOnFocusChangeListener(this);
        zone.setOnFocusChangeListener(this);
        pH.setOnFocusChangeListener(this);
        nitro.setOnFocusChangeListener(this);
        potass.setOnFocusChangeListener(this);
        phos.setOnFocusChangeListener(this);

        zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // initialize pop up window
                popupWindow.showAsDropDown(view, -5, 0);
            }
        });
    }

    public void predictCrops(View view) {
        final String crpTemp = temp.getText().toString().toUpperCase().trim();
        final String crppH = pH.getText().toString().trim();
        final String crpNitro = nitro.getText().toString().trim();
        final String crpPhos = phos.getText().toString().trim();
        final String crpZone = zone.getText().toString().trim();
        final String crpPotass = potass.getText().toString().trim();

        if(spinnerStartMonth.getSelectedItemPosition() < 1 || spinnerEndMonth.getSelectedItemPosition() < 1
                || spinnerStartMonth.getSelectedItemPosition() == spinnerEndMonth.getSelectedItemPosition()) {
            showSnackBar("Please select valid start and end month!");
        }
        else if (TextUtils.isEmpty(crpTemp)) {
            showSnackBar("Please enter current temperature!");
            temp.setActivated(true);
        }
        else if (TextUtils.isEmpty(crpZone) || crpZone.equals("Select Zone")) {
            showSnackBar("Please enter your zone!");
            zone.setActivated(true);
        }
        else if (TextUtils.isEmpty(crppH) || Integer.parseInt(crppH) < 1 || Integer.parseInt(crppH) > 14) {
            showSnackBar("Please enter valid pH (1-14)!");
            pH.setActivated(true);
        }
        else if (TextUtils.isEmpty(crpNitro) || Integer.parseInt(crpNitro) < 0) {
            showSnackBar("Please enter valid Nitrogen content in soil!");
            nitro.setActivated(true);
        }
        else if (TextUtils.isEmpty(crpPhos) || Integer.parseInt(crpPhos) < 0) {
            showSnackBar("Please enter valid Phosphorus content in soil!");
            phos.setActivated(true);
        }
        else if (TextUtils.isEmpty(crpPotass) || Integer.parseInt(crpPotass) < 0) {
            showSnackBar("Please enter valid Potassium content in soil!");
            potass.setActivated(true);
        }
        else {
            temp.setActivated(false);
            zone.setActivated(false);
            pH.setActivated(false);
            nitro.setActivated(false);
            phos.setActivated(false);
            potass.setActivated(false);

            handler = new Handler();
            count = 0;
            viewDialog = new ViewDialog(this);
            viewDialog.showDialog();
            viewDialog.setMessage(msgs[0]);
            checkProgress();

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        crop_list.clear();
                        for (DataSnapshot dbCrop: dataSnapshot.getChildren()) {
                            Crop crop = dbCrop.getValue(Crop.class);
                            int T = Integer.parseInt(crop.getTemp());
                            int temps = Integer.parseInt(temp.getText().toString());
                            Range<Integer> myRange = Range.create(
                                    (int)mnthMap.get(spinnerStartMonth.getSelectedItem()), (int)mnthMap.get(spinnerEndMonth.getSelectedItem()));
                            String crpMnths[] = crop.getMnth().split("_");
                            if (crop.getZone().toLowerCase().contains(zone.getText().toString().toLowerCase()) &&
                                temps < (T+3) && temps > (T-3) &&
                                (myRange.contains((int)mnthMap.get(crpMnths[0])) && myRange.contains((int)mnthMap.get(crpMnths[1])))

//                                !(mnthMap.get(spinnerStartMonth.getSelectedItem()) > mnthMap.get(crpMnths[1]) ||
//                                  mnthMap.get(spinnerEndMonth.getSelectedItem()) < mnthMap.get(crpMnths[0]))
                            ){
                                crop_list.add(crop);
                            }
                        }
                        if (!TextUtils.isEmpty(village.getText()) && crop_list.size() > 0) {
                            crop_list.remove(0);
                        }
                        Log.e("Crops List", "" + crop_list);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }
    }

    public void checkProgress() {
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                count++;
                // Some logic here to check when progress is done, then flip boolean
                if (count < 3) {
                    viewDialog.setMessage(msgs[count]);
                    checkProgress();
                }
                else {
                    viewDialog.hideDialog();
                    PredictedFragment predictedFragment = new PredictedFragment(); //where data needs to be pass
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("data", (ArrayList<? extends Parcelable>) crop_list);
                    bundle.putString("testMsg","Crops Sent");
                    predictedFragment.setArguments(bundle);

                    final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameCropPredicted, predictedFragment);
                    transaction.addToBackStack(null);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.commit();

                    frameLayout.setVisibility(View.VISIBLE);
                }
            }
        }, 4500);
    }

    public void showSnackBar(String msg){
        Snackbar snackbar = Snackbar.make(getPredictionForm,msg,Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_grey));
        snackbar.show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        if(s.hashCode() == temp.getText().hashCode()) {
            temp.setActivated(false);
            tempHasEdited = true;
        }
        if(s.hashCode() == zone.getText().hashCode()) {
            zone.setActivated(false);
            zoneHasEdited = true;
        }
        if(s.hashCode() == pH.getText().hashCode()) {
            pH.setActivated(false);
            pHHasEdited = true;
        }
        if(s.hashCode() == nitro.getText().hashCode()) {
            nitro.setActivated(false);
            nitroHasEdited = true;
        }
        if(s.hashCode() == potass.getText().hashCode()) {
            potass.setActivated(false);
            potassHasEdited = true;
        }
        if(s.hashCode() == phos.getText().hashCode()) {
            phos.setActivated(false);
            phosHasEdited = true;
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {}

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.temperature:
                shouldShowError = !hasFocus && TextUtils.isEmpty(temp.getText()) && tempHasEdited;
                if (shouldShowError)
                    temp.setActivated(true);
                break;

            case R.id.zone:
                shouldShowError = !hasFocus && TextUtils.isEmpty(zone.getText()) && zoneHasEdited;
                if (shouldShowError)
                    zone.setActivated(true);
                break;

            case R.id.pH:
                shouldShowError = !hasFocus && TextUtils.isEmpty(pH.getText()) && pHHasEdited;
                if (shouldShowError)
                    pH.setActivated(true);
                break;

            case R.id.nitro:
                shouldShowError = !hasFocus && TextUtils.isEmpty(nitro.getText()) && nitroHasEdited;
                if (shouldShowError)
                    nitro.setActivated(true);
                break;

            case R.id.potass:
                shouldShowError = !hasFocus && TextUtils.isEmpty(potass.getText()) && potassHasEdited;
                if (shouldShowError)
                    potass.setActivated(true);
                break;

            case R.id.phosp:
                shouldShowError = !hasFocus && TextUtils.isEmpty(phos.getText()) && phosHasEdited;
                if (shouldShowError)
                    phos.setActivated(true);
                break;

            default:
                break;
        }

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
        if (frameLayout.getVisibility() == View.VISIBLE)
            frameLayout.setVisibility(View.GONE);
        else {
            finish();
            super.onBackPressed();
        }
    }

    public void closeFrag(View view) {
        getFragmentManager().popBackStack();
        frameLayout.setVisibility(View.GONE);
    }
}
