package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    String activeUser="";
    EditText text;
    ArrayList<String> messages = new ArrayList<String>();
    ArrayAdapter arrayAdapter;
    ListView listView;

    public void onSend(View view){
        ParseObject message = new ParseObject("Messages");
        final String messageContent= text.getText().toString();
        message.put("sender", ParseUser.getCurrentUser().getUsername().toString());
        message.put("recipient", activeUser);
        message.put("message",messageContent);
        text.setText("");
        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {

                if(e==null){

                    messages.add(messageContent);
                    arrayAdapter.notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        text=(EditText) findViewById(R.id.message);
        Intent intent= getIntent();
        activeUser= intent.getStringExtra("username");
        setTitle(activeUser);
        arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, messages);
        listView= (ListView) findViewById(R.id.listView);
        listView.setAdapter(arrayAdapter);
        ParseQuery<ParseObject> query1= ParseQuery.getQuery("Messages");
        query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername().toString());
        query1.whereEqualTo("recipient", activeUser);
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Messages");
        query2.whereEqualTo("sender", activeUser);
        query2.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername().toString());
        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(query1);
        queries.add(query2);
        ParseQuery<ParseObject> query = ParseQuery.or(queries);
        query.orderByAscending("createdAt");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if(e==null)
                {
                    if(objects.size()>0)
                    {
                        messages.clear();
                        for(ParseObject msg: objects){
                            String msgContent=msg.getString("message");
                            if(!msg.get("sender").toString().equals(ParseUser.getCurrentUser().getUsername().toString()))
                            {
                                msgContent=">> "+msgContent;
                            }
                            messages.add(msgContent);
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

            }
        });


        //Toast.makeText(this, activeUser, Toast.LENGTH_SHORT).show();
    }
}
