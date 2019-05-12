/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;


public class MainActivity extends AppCompatActivity {


  EditText username;
  EditText password;
  TextView toggleText;
  Button button;
  Boolean logInModeActive=false;


  public void redirect(){

      if (ParseUser.getCurrentUser()!=null) {
          Intent intent= new Intent(getApplicationContext(), userList.class);
          startActivity(intent);
      }

  }

  public void toggle(View view){
      if(logInModeActive)
      {
          button.setText("Sign Up");
          toggleText.setText("Log in");
          logInModeActive=false;
      }
      else{
          button.setText("Log in");
          toggleText.setText("Sign Up");
          logInModeActive=true;
      }
  }

  public void onClick(View view)
  {
      if(logInModeActive)
      {
          ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
              @Override
              public void done(ParseUser user, ParseException e) {

                  if(e==null && user!=null)
                  {
                      Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                      redirect();
                  }
                  else{
                      String msg=e.getMessage().toString();
                      if(msg.toLowerCase().contains("java"))
                      {
                          msg=msg.substring(msg.indexOf(" "));
                      }
                      Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                  }

              }
          });
      }
      else{

          ParseUser user = new ParseUser();
          user.setUsername(username.getText().toString());
          user.setPassword(password.getText().toString());
          user.signUpInBackground(new SignUpCallback() {
              @Override
              public void done(ParseException e) {

                  if(e==null)
                  {
                      Toast.makeText(MainActivity.this, "Signed in", Toast.LENGTH_SHORT).show();
                      redirect();
                  }
                  else{
                      String msg=e.getMessage().toString();
                      if(msg.toLowerCase().contains("java"))
                      {
                          msg=msg.substring(msg.indexOf(" "));
                      }
                      Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                  }


              }
          });

      }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    redirect();
    username= (EditText) findViewById(R.id.editText);
    password=(EditText) findViewById(R.id.editText2);
    toggleText=(TextView) findViewById(R.id.textView);
    button=(Button) findViewById(R.id.button);

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}