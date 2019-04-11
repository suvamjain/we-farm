package com.arshilgenius.kisan.agriculture;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;


public class orders extends ActionBarActivity {
    ArrayList<Card> cards = new ArrayList<Card>();
    ArrayList<String> amountt = new ArrayList<String>();
    ArrayList<String> cropp = new ArrayList<String>();
    ArrayList<String> latt = new ArrayList<String>();
    ArrayList<String> longgg = new ArrayList<String>();
    ArrayList<String> kg = new ArrayList<String>();
    ArrayList<String> location = new ArrayList<String>();

    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        Firebase.setAndroidContext(this);
        ref = FirebaseDatabase.getInstance().getReference().child("ORDERS");
        ordering order = new ordering();

//        order.setAmount("50");
//        order.setKgs("2");
//        order.setCrop("rice");
//        order.setLat("9.9312");
//        order.setLongg("76.2673");
//        order.setLoc("Kerala");
//        ref.push().setValue(order);

        read();
    }
    public void read()
    {
//        final Firebase ref = new Firebase("https://adaa-45b17.firebaseio.com/ORDERS");
//        final Firebase ref1 = new Firebase("https://adaa-45b17.firebaseio.com/CurrentAmbulance"); //PreviousLocation
//
//        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.e("Firebase Data:", dataSnapshot.getChildrenCount() + "");
//                for(DataSnapshot d: dataSnapshot.getChildren()) {
//                    Log.e("Firebase Children:", "" + d.toString());
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
        //Value event listener for realtime data update


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.e("Firebase Data:", dataSnapshot.getChildrenCount() + " - " + dataSnapshot);
                cards.clear();
                location.clear();
                amountt.clear();
                cropp.clear();
                latt.clear();
                longgg.clear();
                kg.clear();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    ordering forums = userSnapshot.getValue(ordering.class);
                    String amount = forums.getAmount();
                    String crop = forums.getCrop();
                    String lat = forums.getLat();
                    String longg = forums.getLongg();
                    String kgs= forums.getKgs();
                    String l= forums.getLoc();
                    location.add(l);
                    amountt.add(amount);
                    cropp.add(crop);
                    latt.add(lat);
                    longgg.add(longg);
                    kg.add(kgs);
                }
                cardpop();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }

        });
    }

    public void cardpop()
    {
        for(int i=0;i<amountt.size();i++)
        {
            Card card = new Card(getApplicationContext());
            final String amount= amountt.get(i);
            final String crop=cropp.get(i);
            final String lat= latt.get(i);
            final String longg= longgg.get(i);
            final String kgss= kg.get(i);
            String locc= location.get(i);

            String s= "Crop Name: "+ crop+"\nAmount: "+amount+"\nKiloGrams Available: "+kgss+"\nLocation: "+locc;
            // blah(s);
            //Create a CardHeader
            CardHeader header = new CardHeader(getApplicationContext());
            header.setTitle(crop.toUpperCase());
            card.setTitle(s);
            //Add Header to card
           // card.setBackgroundResourceId(R.color.colorAccent);

           CardThumbnail thumb = new CardThumbnail(getApplicationContext());

            //Set ID resource
            if(crop.equals("corn"))
                thumb.setDrawableResource(R.drawable.corn);
            else if(crop.equals("rice"))
                thumb.setDrawableResource(R.drawable.rice);
            else if(crop.equals("rye"))
                thumb.setDrawableResource(R.drawable.rye);
            else if(crop.equals("oat"))
                thumb.setDrawableResource(R.drawable.oat);
            else if(crop.equals("flour"))
                thumb.setDrawableResource(R.drawable.flour);
            else
                thumb.setDrawableResource(R.drawable.wheat);
//
       // Add thumbnail to a card
       //
       //
       card.addCardThumbnail(thumb);
            //header.setOtherButtonVisible(true);
            //header.setOtherButtonDrawable(R.drawable.pencil);
            //Add a callback
            card.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {

                    Intent in = new Intent(orders.this, MapsActivity.class);
                    in.putExtra("lat",lat);
                    in.putExtra("long",longg);
                    startActivity(in);
                }
            });
            card.addCardHeader(header);
            cards.add(card);
        }
        CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getApplicationContext(),cards);

        CardListView listView = (CardListView) findViewById(R.id.myList);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
    }

    public void addNewOrder(View view) {
        Intent in = new Intent(orders.this, createlisting.class);
        startActivity(in);
    }
}
