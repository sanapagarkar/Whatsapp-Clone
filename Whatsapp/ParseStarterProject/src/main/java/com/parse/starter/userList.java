package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class userList extends AppCompatActivity {

    ArrayList<String> users= new ArrayList<>();
    ArrayAdapter arrayAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        setTitle("Chats");

        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, users);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("username", users.get(i));
                startActivity(intent);

            }
        });
        listView.setAdapter(arrayAdapter);
        ParseQuery<ParseUser> query= ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername().toString());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if(e==null){
                    if(objects.size()>0)
                    {
                        users.clear();
                        for(ParseUser user : objects){
                            users.add(user.getUsername().toString());
                        }
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

            }
        });


    }
}
