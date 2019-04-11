package com.arshilgenius.kisan.agriculture;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;


public class forum extends ActionBarActivity {
    ChatView chatView;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Firebase.setAndroidContext(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ref = FirebaseDatabase.getInstance().getReference().child("FORUMS");
        chatView = (ChatView) findViewById(R.id.chat_view);
        read();

        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
//                Firebase ref = new Firebase("https://adaa-45b17.firebaseio.com/FORUMS/");
                foru person = new foru();
                person.setPost(chatMessage.getMessage());
                person.setTime(System.currentTimeMillis());
                person.setStackId(123);
//                person.setStackId();
                ref.push().setValue(person);
                return true;
            }
        });
    }
    public void read() {

//        final Firebase ref = new Firebase("https://adaa-45b17.firebaseio.com/FORUMS/");
        //Value event listener for realtime data update

        ref.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    foru user = userSnapshot.getValue(foru.class);
                    Log.e("Firebase Children:", "" + dataSnapshot.toString());
                    String post = user.getPost();
                    chatView.addMessage(new ChatMessage(post,
                            user.getTime()==0? System.currentTimeMillis() : user.getTime(),
                            user.getStackId() == 123? ChatMessage.Type.SENT : ChatMessage.Type.RECEIVED));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });

//        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot usersSnapshot) {
//
//                for (DataSnapshot userSnapshot : usersSnapshot.getChildren()) {
//                    foru user = userSnapshot.getValue(foru.class);
//                    Log.e("Firebase Children:", "" + usersSnapshot.toString());
//                    String post = user.getPost();
//                    chatView.addMessage(new ChatMessage(post, user.getTime()==0?System.currentTimeMillis():user.getTime(), ChatMessage.Type.RECEIVED));
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });

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
