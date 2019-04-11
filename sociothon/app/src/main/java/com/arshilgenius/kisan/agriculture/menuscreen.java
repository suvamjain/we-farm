package com.arshilgenius.kisan.agriculture;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.HamButton;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.ButtonEnum;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.text.DateFormat;
import java.util.Date;

import me.itangqi.waveloadingview.WaveLoadingView;


public class menuscreen extends ActionBarActivity {

    static int[] imageResources = new int[]{

            R.drawable.ic_predict,
            R.drawable.cloudy,
            R.drawable.smartphone,
            R.drawable.news,
            R.drawable.cart,
            R.drawable.robot
    };

    static int[] Strings = new int[] {

            R.string.prediction,
            R.string.weather,
            R.string.forum,
            R.string.news,
            R.string.buy,
            R.string.voice
    };

    static int imageResourceIndex = 0;
    static int str = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuscreen);

//        WaveLoadingView mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
//        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
//
//        mWaveLoadingView.setCenterTitleColor(Color.GRAY);
//        mWaveLoadingView.setBottomTitleSize(18);
//        mWaveLoadingView.setProgressValue(20);
//        mWaveLoadingView.setBorderWidth(0);
//        mWaveLoadingView.setAmplitudeRatio(60);
//        mWaveLoadingView.setWaveColor(Color.parseColor("#ff64c2f4"));
//        mWaveLoadingView.setTopTitleStrokeColor(Color.parseColor("#ff1ca8f4"));
//        mWaveLoadingView.setTopTitleStrokeWidth(3);
//        mWaveLoadingView.setAnimDuration(6000);
//        mWaveLoadingView.pauseAnimation();
//        mWaveLoadingView.resumeAnimation();
//        mWaveLoadingView.cancelAnimation();
//        mWaveLoadingView.startAnimation();

       bmb();
    }

    public void bmb()
    {
        BoomMenuButton bmb = (BoomMenuButton) findViewById(R.id.bmb);
        bmb.setButtonEnum(ButtonEnum.Ham);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.HAM_6);
        bmb.setButtonPlaceEnum(ButtonPlaceEnum.HAM_6);
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            HamButton.Builder builder = new HamButton.Builder().normalTextRes(getString()).listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {

                    if (index == 0) {
                        getPrediction(index);
                    }
                    if (index == 1) {
                        weather(index);
                    }
                    if (index == 2) {
                        forums(index);
                    }
                    if (index == 3) {
                        news(index);
                    }
                    if (index == 4) {
                        buy(index);
                    }
                    if (index == 5) {
                        pAssistant(index);
                    }
                }
            }).normalImageRes(getImageResource());
            bmb.addBuilder(builder);

            Animation imgAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_up);
            bmb.startAnimation(imgAnim);
            bmb.setVisibility(View.VISIBLE);
        }
    }

    public static int getString() {
        if (str >= Strings.length) str = 0;
        return Strings[str++];
    }
    public static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }
    public void pAssistant(int pos) {
        //   Toast.makeText(this, Integer.toString(pos), Toast.LENGTH_LONG).show();
        Intent in = new Intent( this, MainActivity.class);
        startActivity(in);
    }
    public void weather(int pos) {
        //   Toast.makeText(this, Integer.toString(pos), Toast.LENGTH_LONG).show();
        Intent in = new Intent( this, sunmain.class);
        startActivity(in);
    }
    public void forums(int pos) {
        //   Toast.makeText(this, Integer.toString(pos), Toast.LENGTH_LONG).show();
        Intent in = new Intent( this, forum.class);
        startActivity(in);
    }
    public void buy(int pos) {
        //   Toast.makeText(this, Integer.toString(pos), Toast.LENGTH_LONG).show();
        Intent in = new Intent( this, orders.class);
        startActivity(in);
    }
    public void news(int pos) {
        // Toast.makeText(this, Integer.toString(pos), Toast.LENGTH_LONG).show();
        Intent in = new Intent( this, NewsActivity.class);  //newsmain.class
        startActivity(in);
    }
    public void getPrediction(int pos) {
        Intent in = new Intent( this, PredictionActivity.class);
        startActivity(in);
    }

}
