package com.example.mayank.cabservice;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.Address;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.location.Location;
import android.graphics.Color;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.*;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import java.util.logging.LogRecord;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
/*
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
*/

public class MainActivity extends AppCompatActivity implements StanfordThread.MaxentTaggerAsync,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Network.TranferResult, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {



    private ChatArrayAdapter chatArrayAdapter;
    private EditText chatText;
    private ImageButton buttonSend;
    private boolean side = false;
    public String[] st = {"weather", "book ride", "Yes", "Fast Book", "Fast Bid"};
    private CardArrayAdapter cardArrayAdapter;
    private ListView listView;
    public int flag;
    public String receivedMessage = "";
    public String dropLocation;
    public String pickUpLocation;
    public String currentLocation;
    public String UserId = null;
    public String BidorBook = null;
    public String tempCurrentLocation = "";
    public String tempDropLocation = "";
    public String tempPickUpLocation = "";
    public String tempFastMessage = "";
    public String bidRate = "";

    public boolean dropLocationFlag = false;
    public boolean pickUpLocationFlag = false;
    public boolean UserIdFlag = false;
    public boolean userPlaceDropFlag = false;
    public boolean userPlacePickUpFlag = false;
    public boolean fastBidBookFlag = false;
    public boolean fastUserPlaceDropFlag = false;
    public boolean fastUserPlacePickFlag = false;
    public boolean fastDropFlag = false;
    public boolean localAllVerifiedFlag = false;
    public boolean localBidFlag = false;


    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    ImageButton speakButton;

    MaxentTagger tagger = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend = (ImageButton) findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.msgview);


        /*try {
            tagger = new MaxentTagger(
                    "taggers/left3words-wsj-0-18.tagger");
        } catch (IOException e) {
            Log.e("taggger", e + "");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e("clas", "not found");
            e.printStackTrace();
        }*/
        new StanfordThread(this).execute();
        Log.e("done with tagging", "yo");


        if (checkPlayServices()) {
            buildGoogleApiClient();
        }

        displayLocation();

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.righht);
        listView.setAdapter(chatArrayAdapter);

        sendBotMessage("I am Vihik Bot.");
        sendBotMessage("In order to chat with drivers you \n will need to create a trip. \n I will help with trip creation.");
        sendBotMessage("Please provide your drop location or choose the below options");
        showButton("FAST BOOK",3);
        showButton("FAST BID", 4);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage(chatText.getText().toString());
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Log.e("above", "chat");
                receivedMessage = chatText.getText().toString();
                sendChatMessage(receivedMessage);
                Log.e("below", "chat");
                Log.e("the", receivedMessage);
                if(fastBidBookFlag == true)
                    decodeMessageFast(receivedMessage);
                else
                    decodeMessage(receivedMessage);
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        speakButton = (ImageButton)findViewById(R.id.speech);
        speakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptSpeechInput();
            }
        });

    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sendBotMessage(result.get(0));
                }
                break;
            }

        }
    }


    String displayLocation() {

        String currentAddress = "";
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return "Please enable google play services";
        }
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            Log.e("the location", latitude + " " + longitude + " " );
            currentAddress = convertToAddress(latitude, longitude);

        } else {
            Log.e("the location", "not possible");
            }
        return currentAddress;
    }

    String convertToAddress(double latitude, double longitude){
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.e("the add",address + city + state + country + postalCode + knownName);
            return address + " " + city + " " + state + " " + country + " " + postalCode + " " + knownName;// Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "some issue with finding the current location";
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    void decodeMessage(String message)
    {

        if(flag == 0) {
            String tempDroplocation = extractLocation(message,flag);
            String[] tempDropArray = tempDroplocation.split("\\s");
            sendBotMessage("The drop location is being verified. This may take couple of seconds");
            if(tempDropArray.length < 7) {
                dropLocation = tempDroplocation;
                sendJson(dropLocation,"NO");

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        Log.e("DLF",dropLocationFlag + "");
                        if(dropLocationFlag && userPlaceDropFlag) {
                            sendBotMessage("Detecting your current location.");
                            currentLocation = displayLocation();
                            sendBotMessage("Drop location verified. \n We detected \n" + currentLocation +  "\n as pickup location. \n Do you want to use this as pickup location ? \n Press yes or write any other location");
                            showButton("YES",2);
                            flag++;
                        }
                        else if (dropLocationFlag == true && userPlaceDropFlag == false){
                            flag = 0;
                            sendBotMessage("The Vihik cab service is limited to Hyderabad as of now. We request you to enter drop location in Hyderabad itself");
                        }
                        else
                        {   flag = 0;
                            sendBotMessage("We were unable to verify your drop location. Please follow the template or you maybe on slow internet connection");
                        }
                    }
                }, 6000);

            }
            else{
                flag = 0;
                sendBotMessage(tempDroplocation);
            }
        }
        else if(flag == 1){
            String tempPickUpLocation;
            String[] tempPickUpArray;
            if(message.equals("Yes") || message.equals("yes") || message.equals("YES")) {
                tempPickUpLocation = currentLocation;
                if(tempPickUpLocation.contains("Hyderabad")) {
                    pickUpLocation = tempPickUpLocation;
                    sendBotMessage("Trip Id is being generated. Thanks for your patience.");
                    sendJson(dropLocation, pickUpLocation);

                    sendBotMessage("The drop location is: " + dropLocation + ".");
                    sendBotMessage("The pick up location is: " + pickUpLocation + ".");
                    sendBotMessage("The Id is: " + UserId + ".");
                    sendBotMessage("Thanks for booking through us.");
                    sendBotMessage("Press 1 to make a new booking");
                    flag++;
                }
                else
                {
                    flag = 1;
                    sendBotMessage("The Vihik cab service is limited to Hyderabad as of now. We request you to enter pick up location in Hyderabad itself");
                }

            }
            else {
                tempPickUpLocation = extractLocation(message, flag);
                tempPickUpArray = tempPickUpLocation.split("\\s");

                if(tempPickUpArray.length < 7)
                {
                    pickUpLocation = tempPickUpLocation;
                    sendJson(pickUpLocation,"NO");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Log.e("PLF",pickUpLocationFlag + "");
                            if(pickUpLocationFlag && userPlacePickUpFlag) {
                                sendBotMessage("Trip Id is being generated. Thanks for your patience.");
                                sendJson(dropLocation, pickUpLocation);

                                Handler handle = new Handler();
                                handle.postDelayed(new Runnable() {
                                    public void run() {
                                        if(UserIdFlag == true) {
                                            sendBotMessage("The drop location is: " + dropLocation + ".");
                                            sendBotMessage("The pick up location is: " + pickUpLocation + ".");
                                            sendBotMessage("The Id is: " + UserId + ".");
                                            sendBotMessage("Thanks for booking through us.");
                                            sendBotMessage("Press 1 to make a new booking");
                                            flag++;
                                        }
                                        else
                                        {
                                            flag = 0;
                                            sendBotMessage("Some technical issue. Please try again after sometime");
                                        }
                                    }
                                }, 6000);


                            }
                            else if (pickUpLocationFlag == true && userPlacePickUpFlag == false){
                                flag = 1;
                                sendBotMessage("The Vihik cab service is limited to Hyderabad as of now. We request you to enter pick up location in Hyderabad itself");
                            }
                            else {
                                flag = 1;
                                sendBotMessage("We were unable to get the pick up location. Please follow the template");
                            }
                        }
                    }, 6000);

                }
                else{
                    flag = 1;
                    sendBotMessage(tempPickUpLocation);
                }
            }


        }
        if(message.equals("1"))
        {
            sendBotMessage("Enter the Drop Location");
            flag = 0;
        }

        if(message.contains("add button"))
        {
            for(int i=0;i<2 ; i++)
            showButton(st[i],i);
        }
        if(message.contains("date"))
            showDate();
        if(message.contains("time"))
            showTime();
        /*if(message.contains("card"))
            showCard();
*/

    }

    String extractLocation(String mes, int flag)
    {
        String completeLocation = "";
        boolean f = true;
        String posTaggedMes = postagger(mes);
        String[] posArray;
        String[] innerArray;
        Log.e("tagged", posTaggedMes);
        Log.e("res", !mes.contains(" ") + "");
        if(!mes.contains(" "))
        {
            innerArray = posTaggedMes.split("/");
            if(innerArray[1].equals("NN") || innerArray[1].equals("NNP") || innerArray[1].equals("NNS"))
                return innerArray[0];
            else
                return "This is not the proper destination location. Please enter correct destination.";
        }
        else {
                posArray = posTaggedMes.trim().split("\\s+");
                for (int i = 0; i < posArray.length; i++) {
                    innerArray = posArray[i].split("/");
                    Log.e("here", innerArray[0] + innerArray[1] + flag + "");
                    if (innerArray[1].equals("TO") && innerArray[0].equals("to") && flag == 0 )
                    {
                        innerArray = posArray[i+1].split("/");
                        if(innerArray[1].equals("NN") || innerArray[1].equals("NNS") || innerArray[1].equals("NNP") || innerArray[1].equals("CD")) {
                            completeLocation +=innerArray[0] + " ";
                            int j = 2;
                            while(f && (i+j)<posArray.length)
                              {
                                innerArray = posArray[i+j].split("/");
                                if(innerArray[1].equals("NN") || innerArray[1].equals("NNS") || innerArray[1].equals("NNP") || innerArray[1].equals("CD")) {
                                    completeLocation += innerArray[0] + " ";
                                    j++;
                                    f = true;
                                }
                                else
                                    f = false;
                            }
                            Log.e("CL",completeLocation);
                            return completeLocation;
                        }
                        else
                            return "This is not the proper destination location. Please enter correct destination.";
                    }
                    if (innerArray[1].equals("IN") && innerArray[0].equals("from") && flag == 1)
                    {
                        innerArray = posArray[i+1].split("/");
                        if(innerArray[1].equals("NN") || innerArray[1].equals("NNS") || innerArray[1].equals("NNP") || innerArray[1].equals("CD")){
                            completeLocation +=innerArray[0] + " ";
                            int j = 2;
                            while(f && (i+j)<posArray.length)
                            {
                                innerArray = posArray[i+j].split("/");
                                if(innerArray[1].equals("NN") || innerArray[1].equals("NNS") || innerArray[1].equals("NNP") || innerArray[1].equals("CD")) {
                                    completeLocation += innerArray[0] + " ";
                                    j++;
                                    f = true;
                                }
                                else
                                    f = false;
                            }
                            Log.e("CL",completeLocation);
                            return completeLocation;
                        }
                        else
                            return "This is not the proper pick up location. Please enter correct pick up location.";
                    }
                    if (innerArray[1].equals("TO") && innerArray[0].equals("to") && flag != 0)
                        return "We already have your drop location. Please enter your pick up location.";

                    if (innerArray[1].equals("IN") && innerArray[0].equals("from") && flag == 0)
                        return "The destination location is required first. Please enter the pick up location when asked.";
            }
        }

        return null;
    }

    String postagger(String mes)
    {
        String posTagged = null;

        posTagged = tagger.tagString(mes);
        return posTagged;
    }

    /*void openNLP()
    {
        String paragraph = "Hi. How are you? This is Mike.";

        // always start with a model, a model is learned from training data
        InputStream is = null;
        try {
            is = new FileInputStream("en-sent.bin");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SentenceModel model = null;
        try {
            model = new SentenceModel(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SentenceDetectorME sdetector = new SentenceDetectorME(model);
/
        String sentences[] = sdetector.sentDetect(paragraph);

        Log.e("one",sentences[0]);
        Log.e("two",sentences[1]);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
    void sendJson(final String dLocation, final String picLocation)
    {
        String[] net = {dLocation,picLocation};
        new Network(this).execute(net);

    }

    /*void showCard()
    {
        listView = (ListView) findViewById(R.id.card_listView);

        listView.addHeaderView(new View(this));
        listView.addFooterView(new View(this));

        cardArrayAdapter = new CardArrayAdapter(getApplicationContext(), R.layout.list_item_card);

        for (int i = 0; i < 3; i++) {
            Card card = new Card("Card " + (i+1) + " Line 1", "Card " + (i+1) + " Line 2");
            cardArrayAdapter.add(card);
        }
        listView.setAdapter(cardArrayAdapter);

    }
*/


    void showButton(String user, int id)
    {
        final Button myButton = new Button(this);
        myButton.setText(user);
        myButton.setBackgroundResource(R.drawable.button_shape);
        myButton.setId(id);
        myButton.setTextColor(Color.parseColor("#0099CC"));

        LinearLayout ll = (LinearLayout) findViewById(R.id.sec_layout);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ll.addView(myButton, lp);

        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                toastMessage();
                LinearLayout ll = (LinearLayout) findViewById(R.id.sec_layout);
                ll.removeAllViews();
                sendChatMessage(st[id]);
                if(id == 2)
                    decodeMessage(st[id]);
                if(id == 3 || id == 4) {
                    fastBookBid(st[id]);
                    fastBidBookFlag = true;
                }
            }
        });

    }

    void fastBookBid(String book_or_bid)
    {
        BidorBook = book_or_bid;
        sendBotMessage("You have chosen " + book_or_bid + " option.\n You may follow the below template.");
        if(book_or_bid.contains("Fast Book"))
            sendBotMessage("to Char Minar from Almond Bakery.");
        else
            sendBotMessage("to Char Minar from Almond Bakery @ Rs. 10/km.");
        sendBotMessage("If you want your current location as pick up then you may directly write your drop location");

    }

    void decodeMessageFast(String message){

        if(message.contains("Bid")||message.contains("BID")||message.contains("bid"))
            localBidFlag = true;
        if(message.contains("Bid") || message.contains("Book") || message.contains("BID") || message.contains("BOOK") || message.contains("bid") || message.contains("book"))
        {
            message = message.replace("Bid","");
            message = message.replace("Book","");
            message = message.replace("BID","");
            message = message.replace("BOOK","");
            message = message.replace("bid","");
            message = message.replace("book","");
        }
        String posTaggedString = postagger(message);
        Log.e("the postagged string",posTaggedString);
        String[] posArray = posTaggedString.split("\\s");
        String[] innerposArray;

        String tempMessage = "";
        boolean toFlag = false;
        boolean fromFlag = false;


        if((message.contains("to")&& message.contains("from"))||(message.contains("TO")&& message.contains("FROM"))||(message.contains("to")&& message.contains("from"))||(message.contains("To")&& message.contains("from"))||(message.contains("to")&& message.contains("From"))) {
            Log.e("inside","if");
            for (int i = 0; i < posArray.length; i++) {
                innerposArray = posArray[i].split("/");
                if (innerposArray[1].equals("TO") && (innerposArray[0].equals("to") || innerposArray[0].equals("To") || innerposArray[0].equals("TO"))) {
                    int j = 1;
                    boolean f = true;
                    while (f && (i + j) < posArray.length) {
                        innerposArray = posArray[i + j].split("/");
                        if (innerposArray[1].equals("NN") || innerposArray[1].equals("NNS") || innerposArray[1].equals("NNP") || innerposArray[1].equals("CD")) {
                            tempDropLocation += innerposArray[0] + " ";
                            j++;
                            f = true;
                        } else
                            f = false;
                    }
                    toFlag = true;
                }
                if (innerposArray[1].equals("IN") && innerposArray[0].equals("from")) {
                    int j = 1;
                    boolean f = true;
                    while (f && (i + j) < posArray.length) {
                        innerposArray = posArray[i + j].split("/");
                        if (innerposArray[1].equals("NN") || innerposArray[1].equals("NNS") || innerposArray[1].equals("NNP") || innerposArray[1].equals("CD")) {
                            tempPickUpLocation += innerposArray[0] + " ";
                            j++;
                            f = true;
                        } else
                            f = false;
                    }
                    fromFlag = true;
                }


            }
        }
        else if(message.contains("to")||message.contains("To")|| message.contains("TO")) {
            for (int i = 0; i < posArray.length; i++) {
                innerposArray = posArray[i].split("/");
                if (innerposArray[1].equals("TO") && (innerposArray[0].equals("to") || innerposArray[0].equals("To") || innerposArray[0].equals("TO"))) {
                    int j = 1;
                    boolean f = true;
                    while (f && (i + j) < posArray.length) {
                        innerposArray = posArray[i + j].split("/");
                        if (innerposArray[1].equals("NN") || innerposArray[1].equals("NNS") || innerposArray[1].equals("NNP") || innerposArray[1].equals("CD")) {
                            tempDropLocation += innerposArray[0] + " ";
                            j++;
                            f = true;
                        } else
                            f = false;
                    }
                    toFlag = true;
                }

            }
        }
        else
        {
            sendBotMessage("We could not successfully get your locations.\n Please follow the template for fast results.");
        }
        if(toFlag && fromFlag)
        {
            Log.e("d",tempDropLocation + " " + tempPickUpLocation);
            Log.e("inside","to and from flag");
            sendJson(tempDropLocation, "NO");
            sendJson(tempPickUpLocation, "NO");
            sendBotMessage("We are verifying your drop and pick up location. Thanks  for your patience");
            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                public void run() {
                    Log.e("f",fastUserPlaceDropFlag + " " + fastUserPlacePickFlag);
                    if(fastUserPlaceDropFlag && fastUserPlacePickFlag)
                        localAllVerifiedFlag = true;
                    else if((fastUserPlaceDropFlag && !fastUserPlacePickFlag) || (!fastUserPlaceDropFlag && fastUserPlacePickFlag))
                    {
                        sendBotMessage("The Vihik bot service is as of now limited to Hyderabad. Please use the locations within Hyderabad only.");
                        fastUserPlaceDropFlag = false;
                        fastUserPlacePickFlag = false;
                        localAllVerifiedFlag = false;
                    }

                }
            }, 5000);
        }
        else if(toFlag)
        {
            sendJson(tempDropLocation, "NO");
            tempCurrentLocation = displayLocation();
            sendBotMessage("We are verifying your drop and  current location. Thanks for your patience");
            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                public void run() {
                 if(tempCurrentLocation.contains("Hyderabad") && fastUserPlaceDropFlag)
                 {
                     localAllVerifiedFlag = true;
                     tempPickUpLocation = tempCurrentLocation;
                 }
                    else{
                     sendBotMessage("The Vihik bot service is as of now limited to Hyderabad. Please use the locations within Hyderabad only.");
                     fastUserPlaceDropFlag = false;
                     fastUserPlacePickFlag = false;
                     localAllVerifiedFlag = false;
                 }
                }
            }, 4000);


        }
        tempFastMessage = message;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Log.e("lav",localAllVerifiedFlag + "");
                if(localAllVerifiedFlag)
                {
                    if(localBidFlag)
                    {
                        tempFastMessage = tempFastMessage.replace(tempDropLocation,"");
                        tempFastMessage = tempFastMessage.replace(tempPickUpLocation,"");
                        bidRate = tempFastMessage.replaceAll("[^0-9]","");
                        Log.e("The bid  rate is", bidRate);

                    }

                    sendJson(tempDropLocation, tempPickUpLocation);
                    Handler handle = new Handler();
                    handle.postDelayed(new Runnable() {
                        public void run() {
                            if(UserIdFlag){
                                sendBotMessage("The drop location is: " + tempDropLocation + ".");
                                sendBotMessage("The pick up location is: " + tempPickUpLocation + ".");
                                sendBotMessage("The Id is: " + UserId + ".");
                                if(localBidFlag)
                                    sendBotMessage("The bid rate as given by you is: " + bidRate + ".");
                                sendBotMessage("Thanks for booking through us.");
                                sendBotMessage("Press 1 to make a new booking");
                            }
                            else
                            {
                                sendBotMessage("Some technical issue. Please try after sometime");
                                UserIdFlag = false;
                                fastUserPlaceDropFlag = false;
                                fastUserPlacePickFlag = false;
                                localAllVerifiedFlag = false;

                            }
                        }
                    }, 5000);
                }
            }
        }, 5000);


    }


    void showTime()
    {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                false
        );
        tpd.show(getFragmentManager(), "Timepickerdialog");

    }

    void showDate(){
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                MainActivity.this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }



    void toastMessage()
    {
        Toast.makeText(getApplicationContext(),"nice",Toast.LENGTH_SHORT).show();
    }

    private boolean sendChatMessage(String message) {
        chatArrayAdapter.add(new ChatMessage(side, message));
        chatText.setText("");
        return true;
    }

    private boolean sendBotMessage(String mes) {
        chatArrayAdapter.add(new ChatMessage(!side, mes));
        return true;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {

    }

    @Override
    public void process(String output) {
        if(!output.equals("Some technical issue. Please try after sometime")) {
            UserIdFlag = true;
            UserId = output;
        }
        else
            UserIdFlag = false;
    }

    public void statusSearch(String status,boolean placeFlag )
    {
        Log.e("SS", placeFlag + "");
        if(!fastBidBookFlag) {
            if (status.equals("OK") && flag == 0) {
                dropLocationFlag = true;
                if (placeFlag == true)
                    userPlaceDropFlag = true;
            }
            if (status.equals("OK") && flag == 1) {
                pickUpLocationFlag = true;
                if (placeFlag == true)
                    userPlacePickUpFlag = true;
            }
        }
        else
        {
            if(status.equals("OK") && fastDropFlag == false){
                fastUserPlaceDropFlag = true;
                fastDropFlag = true;
                Log.e("the first","nice");
            }
            if(status.equals("OK") && fastDropFlag == true){
                fastUserPlacePickFlag = true;
                Log.e("the sec","nice");
            }
        }


    }

    public void maxentTagger(MaxentTagger tagger)
    {
        this.tagger = tagger;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

            mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("connection failed", "connection failed");
    }

    @Override
    protected void onStart(){
    super.onStart();
    if (mGoogleApiClient != null) {
        mGoogleApiClient.connect();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

}

class Network extends AsyncTask<String[],Void,String[]>
{
    public interface TranferResult {
        void process(String output);
        void statusSearch(String status, boolean placeFlag);
    }
    public  TranferResult tf = null;


    public Network(TranferResult tf)
    {
        this.tf = tf;
    }
    @Override
    protected String[] doInBackground(String[]... params) {

        String dropLocation = params[0][0];
        String pickUpLocation = params[0][1];
        if (!pickUpLocation.equals("NO")) {

            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            currentDateTimeString = format.format(Date.parse(currentDateTimeString));
            Log.e("d", currentDateTimeString);
            Log.e("in", "thread");
            Looper.prepare(); //For Preparing Message Pool for the child Thread
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
            HttpResponse response;
            JSONObject json = new JSONObject();

            try {
                Log.e("pic", dropLocation);
                Log.e("pic", pickUpLocation);
                Log.e("in", "try");
                HttpPost post = new HttpPost("http://35.166.44.35:8080/testapi/webapi/trips/trip/book2");
                Log.e("after", "post");

                json.put("t_bidamount", "0");
                json.put("t_bidredius", "0");
                json.put("t_bookdatetime", currentDateTimeString);
                json.put("t_dropdatetime", "0000-00-00 00:00:00");
                //json.put("t_dropplace", dropLocation);
                json.put("t_dropplace", "Vijaywada");
                json.put("t_mobileno", "9515119304");
                json.put("t_picdatetime", "");
                //json.put("t_picplace", pickUpLocation);
                json.put("t_picplace", "Hyderabad");
                json.put("t_status", "New");
                json.put("t_totalfare", "0");
                json.put("t_type", "Chat");
                json.put("td_driverid", "836");
                json.put("tu_userid", "764");
                json.put("tv_vehicalid", "837");
                json.put("t_rating", "3");
                json.put("t_feedback", "good");

                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                Log.e("after", "entity");
                response = client.execute(post);
                Log.e("code", response.getStatusLine().getStatusCode() + "");
                Log.e("response", response + "");

                    /*Checking response */
                if (response != null) {
                    Log.e("in", "res");
                    String result = EntityUtils.toString(response.getEntity()); //Get the data in the entity
                    Log.e("the result", result);
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("success").equals("true")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Log.e("data ", data.getString("t_id"));
                        tf.process(data.getString("t_id"));
                    }
                    else
                        tf.process("Some technical issue. Please try after sometime");


                }

            } catch (Exception e) {
                Log.e("ex", e + "");
                e.printStackTrace();

            }
        } else {
            dropLocation = dropLocation.replaceAll("\\s", "+");
            String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query=" + dropLocation + "&key=AIzaSyBb_ExzKZLFAwBDfIRy1ODLV-oLmCxeL_8";
            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 30000); //Timeout Limit
            HttpResponse response;
            HttpGet httpGet = new HttpGet(URL);
            try {
                response = client.execute(httpGet);
                if (response != null) {
                    Log.e("in", "res");
                    String result = EntityUtils.toString(response.getEntity()); //Get the data in the entity
                    Log.e("the result", result);
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    boolean placeFlag = false;
                    Log.e("status", status);

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for(int i = 0;i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        Log.e("the name", jsonObject1.getString("formatted_address"));
                        if(jsonObject1.getString("formatted_address").contains("Hyderabad"))
                            placeFlag = true;
                    }
                    tf.statusSearch(status, placeFlag);

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

class StanfordThread extends AsyncTask<Void, Void, Void>
{

    public interface MaxentTaggerAsync {
        void maxentTagger(edu.stanford.nlp.tagger.maxent.MaxentTagger tagger);
    }
    public MaxentTaggerAsync mt = null;


    public StanfordThread(MaxentTaggerAsync mt)
    {
        this.mt = mt;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        MaxentTagger tagger = null;
        try {
            tagger = new MaxentTagger(
                    "taggers/left3words-wsj-0-18.tagger");
        } catch (IOException e) {
            Log.e("taggger", e + "");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            Log.e("clas", "not found");
            e.printStackTrace();
        }
        mt.maxentTagger(tagger);
        return null;
    }
}