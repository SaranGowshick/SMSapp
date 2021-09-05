//*The project is done by beginner,so Don't get angry at me..A kind request..
// I hope that you all familiar with google platforms!.. yes, you do.That's ok. Here I am using
// one of the online google platform and that is sheet.We all know that ,sheet is used to store so much
// data or contents of the single people or bunch of people,and to retrieve it whenever we need it.
// The sheets are very useful in schools,colleges,agencies,etc....
//Here,this project is based on the public opinion,Let's talk about that.
// The opinion is about ,"We can save the data in sheet and how to send the some message or information to the people about the saved content.".
// I hope that you can understand it,If you can't. It's okay.Here is the Explanation
// Let's consider the college,There are so many students studied at one college.Let us assume,"The name of the student","The date of birth","The phone number"and so on ....
// The college chairman need to send a message to students about some inauguration takes place in the college.
// For that he will put a notice on common point of the students where they will be seen.
// Instead of that ,the chairman thinks what about sending messages to the students about the inauguration.
// The project is based on that opinion. we can share  messages to many people at instant.*//
package com.example.sms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.sms.R.layout.activity_main;


public class MainActivity extends AppCompatActivity {
    private static final String SENT_SMS_ACTION = "SENT_SMS_ACTION";
    private static final String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
    // The Google Sheet link used for this tutorial.
    // To get this you have to do some process
    // Step1:Create a new spread sheet.
    // Step2:The spreadsheet must be "Publish To The Web" File > Publish Web
    // Step3:Copy the sheet id.
    // Step4:https://spreadsheets.google.com/feeds/list/“Enter your ID here”/od6/public/values?alt=json
    // At last you will get the link of the Google sheet
    // Go to "fetchMessage Function"
   // https://spreadsheets.google.com/feeds/list/“Enter your ID here”/od6/public/values?alt=json


    private static final String JSON_URL = "https://spreadsheets.google.com/feeds/list/1lpaAC40FDcBpQhJcd79E2ilQ_3YHuC7Ih4hNQr-a2xg/od6/public/values?alt=json";

    private static final int PERMISSION_REQUEST = 101;
    //*To get this URL you need to write script on that Google Sheet
    // For that,Go to tools and select editor
    // You need to write the following code on that script editor
    // const SHEET_URL = "Your Sheet_URL";
    //const SHEET_NAME= "Your Sheet_Name";
    //
    //
    //const doGet = () => {
    //    const sheet = SpreadsheetApp.openByUrl(SHEET_URL).getSheetByName(SHEET_NAME);
    //
    //    const [header, ...data] = sheet.getDataRange().getDisplayValues();
    //
    //    const PHONE = header.indexOf('contact');
    //    const TEXT = header.indexOf('message');
    //    const STATUS = header.indexOf('text');
    //    const output = [];
    //
    //    data.forEach((row, index) => {
    //          Logger.log(row);
    //          Logger.log(index);
    //           output.push([index + 1, row[3], row[5]]);
    //    });
    //
    //    const json = JSON.stringify(output);
    //    Logger.log(json);
    //
    //    return ContentService.createTextOutput(json).setMimeType(ContentService.MimeType.TEXT);
    //};
    //
    //const doPost = (e) => {
    //  const sheet = SpreadsheetApp.openByUrl(SHEET_URL).getSheetByName(SHEET_NAME);
    //  const data = JSON.parse(e.postData.contents)
    //  sheet.getRange(Number(data["id"])+1, 5).setValue(data["status"]);
    //  return ContentService.createTextOutput(JSON.stringify(e)).setMimeType(ContentService.MimeType.JSON);
    //};
    // Then,deploy the code as web app
    // You,will get a URL.
    // That URL is used here...*//

    private static final String UPLOAD_URL = "https://script.google.com/macros/s/AKfycbzu5ont7yKw8hdnH3IwkB5iDHThrvZroSzFS_VmYi-mhHi5xmU/exec";
    //*Here the "UserModelList"   is created for the user and their purpose.you will be understand it at end...
    List<UserModel> UserModelList;
    //*The "listview" is created to add the data's stored in the spreadsheet
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_main);
    }
    //*If we click "SEND" button the below function should be called..
    // The SMS action takes place *//
    public void SendSMS(View view) {

        try {
            Intent sentIntent = new Intent(SENT_SMS_ACTION);
            PendingIntent sentPI = PendingIntent.getBroadcast(MainActivity.this, 101, sentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Intent deliveredIntent = new Intent(DELIVERED_SMS_ACTION);
            PendingIntent deliveredPI = PendingIntent.getBroadcast(MainActivity.this, 102, deliveredIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            String myNumber = null;
            String myMsg = null;
            for (int i = 0; i < UserModelList.size(); i++) {
                if (i <= 100) {
                    UserModel message = UserModelList.get(i);
                    //Here,We get the users phone number
                    myNumber = message.getContact();
                    //Here,I am  taking only the "message".If you want to send any other data's like (id,name etc...)you can get it..
                    myMsg = message.getMessage();
                    //Here,the mobile number will be checked.
                    if (myNumber.equals("") || myMsg.equals("")) {
                        Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                        uploadStatus(message, "NOT SENT");
                    } else {
                        //If the mobile no is correct ,the below condition statement is executed
                        if (TextUtils.isDigitsOnly(myNumber)) {
                            SmsManager smsManager = SmsManager.getDefault();
                            ArrayList<String> parts = smsManager.divideMessage(myMsg);
                            smsManager.sendMultipartTextMessage(myNumber, null, parts,
                                    null, null);
                            //*From the above ,the message will be send to the users who are all in that Google Sheet.
                            // How to make sure of that,So, that I am creating the "uploadStatus" Function*//
                            uploadStatus(message, "SMS SENT");
                        } else {
                            Toast.makeText(this, "Please enter the correct number", Toast.LENGTH_SHORT).show();
                            uploadStatus(message, "NOT SENT");
                        }
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Could Not Send SMS", Toast.LENGTH_SHORT).show();

        }
    }

    private void uploadStatus(UserModel userObject, String delivery) {
        Map<String, String> data = new HashMap<>();
        data.put("id", userObject.getId());
        data.put("status", delivery);
        System.out.println(new JSONObject(data));
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, UPLOAD_URL, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("SMS SENT");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error);
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadDataFromSheets();
            } else {
                Toast.makeText(this, "You don't have required permission to send a message", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void fetchMessage(View view) {
        //*I used two buttons for this app one is "FETCH" and the other is "SEND"
        // If you click "FETCH" on that app ,this function will be called  and  below process takes place..
        // Here,I am asking some permission for the app *//
        int smsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
        int internetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int readState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        //*If the permissions are granted ,the condition statement executed *//
        if (smsPermission == PackageManager.PERMISSION_GRANTED && internetPermission == PackageManager.PERMISSION_GRANTED
                && readState == PackageManager.PERMISSION_GRANTED) {
            //*The Global declaration of "listview" is identified by the id "MessageList" created at xml*//
            listView = (ListView) findViewById(R.id.MessageList);
            //The function "loadDataFromSheets" called
            loadDataFromSheets();
        }
        //*If the permissions are not granted,else part will be executed*//
        else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.INTERNET},
                    PERMISSION_REQUEST);
        }

    }


    private void loadDataFromSheets() {
        //*I hope you understand that how to get a google sheet link..
        //Here,I am using JSON.Before explanation,you need to study about JSON.
        // You need to pass the Google Sheet URL as request shown below code..*//
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, JSON_URL, null,
                new Response.Listener<JSONObject>() {
                    //*If your Google Sheet URL is correct You will get a response and the following process need to be done.
                    // If the URL is wrong you will get errorResponse*//
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Json Response", String.valueOf(response));
                        try {
                            JSONObject feedObj = response.getJSONObject("feed");
                            JSONArray entryArray = feedObj.getJSONArray("entry");
                            //*Create an ArrayList for the user
                            UserModelList = new ArrayList<>();
                            //*we loop the sheet from rows and columns to get the data's
                            // Here,I am getting the(id, name, contact, message)from the Google sheet *//

                            for (int i = 0; i < entryArray.length(); i++) {
                                JSONObject entryObj = entryArray.getJSONObject(i);
                                String name = entryObj.getJSONObject("gsx$name").getString("$t");
                                String id = entryObj.getJSONObject("gsx$id").getString("$t");
                                String contact = entryObj.getJSONObject("gsx$contact").getString("$t");
                                String message = entryObj.getJSONObject("gsx$message").getString("$t");
                                String status = entryObj.getJSONObject("gsx$status").getString("$t");
                                //*Here, I create a UserModel class
                                UserModel temp = new UserModel(id, name, contact, message, status);
                                //*Add the obtained data's to the User
                                UserModelList.add(temp);
                            }
                            //*Here,I'm creating the ListViewAdopter class.If you studied about "Android Developing",You will be known about the ListViewAdopting class.
                            // If you don't know ,don't worry.Go and Study..
                            // Here, I am adding the user data's to the adapter *//
                            ListViewAdopter adapter = new ListViewAdopter(UserModelList, getApplicationContext());
                            //adding the adapter to listview
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                //* From the above,you will be displayed the data's of the user in the app.
                // then ,the sending part will be remaining*//
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}