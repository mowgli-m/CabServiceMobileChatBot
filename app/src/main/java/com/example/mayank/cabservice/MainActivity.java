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
import android.os.Environment;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Location;
import android.graphics.Color;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.*;
/*

import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Entity;
import com.textrazor.annotations.Sentence;
*//*
import com.textrazor.AnalysisException;
import com.textrazor.NetworkException;
import com.textrazor.TextRazor;
import com.textrazor.annotations.AnalyzedText;
import com.textrazor.annotations.Sentence;
import com.textrazor.annotations.Word;*/
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

/*
import com.textrazor.annotations.Word;
*/

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

//import com.textrazor.TextRazor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URI;
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

public class MainActivity extends AppCompatActivity implements AmazonThread.AmazonInterface, StanfordThread.MaxentTaggerAsync ,DriverAndVehicle.DVResult, CreateTrip.TripResult ,FareCalculator.FareResult, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Network.TranferResult, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {



    public ChatArrayAdapter chatArrayAdapter;
    private EditText chatText;
    private ImageButton buttonSend;
    private boolean side = false;
    public String[] st = {"weather", "book ride", "Yes", "Book", "Fast Bid"};
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
    public String bidLatitudeDrop = "";
    public String bidLongitudeDrop = "";
    public String bidLatitudePickUp = "";
    public String bidLongitudePickUp = "";
    public String receivedBidRate = "";
    public String driverId = "";
    public String vehicleId = "";
    public String vehiclePlateNum = "";
    public String driverName = "";
    public String driverMob = "";
    public String rajorTaggedString = "";
    public String[] byBot = new String[500];
    public String[] byUser = new String[500];


    public int tryCount = 0;
    public int botArrayLength = 0;
    public int userArrayLength = 0;

    public double latitude;
    public double longitude;

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
    public boolean isBidReceivedFlag = false;
    public boolean tryOnceMoreFlag = false;
    public boolean moreTryFlag = true;
    public boolean isCardFlag = false;
    public boolean yesFlag = false;


    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    ImageButton speakButton;
    MaxentTagger tagger = null;

    AmazonS3Client s3;
    TransferUtility transferUtility1;

    public File file2;
    public ProgressBar mprogress;


    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonSend = (ImageButton) findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.msgview);
        mprogress = (ProgressBar)findViewById(R.id.progress_bar);


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

        new AmazonThread(this, getApplicationContext()).execute();

        if (checkPlayServices()) {
            buildGoogleApiClient();
        }





        displayLocation();

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.righht);
        listView.setAdapter(chatArrayAdapter);

        sendBotMessage("Hi User, I am Vihik Bot.");
        sendBotMessage("In order to chat with drivers you \n will need to create a trip. \n I will help with trip creation.");
        sendBotMessage("Please choose one of the below options");
        sendBotMessage("You will be able to chat with drivers if you select Book option.");
        showButton("BOOK",3);
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
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

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
                sendJson(dropLocation,"NO", "NO");
                mprogress.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        mprogress.setVisibility(View.GONE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                yesFlag = true;
                tempPickUpLocation = currentLocation;
                if(tempPickUpLocation.contains("Hyderabad")) {
                    pickUpLocation = tempPickUpLocation;
                    sendBotMessage("Trip Id is being generated. Thanks for your patience.");
                    bookFlow(dropLocation,pickUpLocation);

                    flag++;
                }
                else
                {
                    flag = 1;
                    sendBotMessage("The Vihik cab service is limited to Hyderabad as of now. We request you to enter pick up location in Hyderabad itself");
                }

            }
            else {
                LinearLayout ll = (LinearLayout)findViewById(R.id.sec_layout);
                ll.removeAllViews();
                tempPickUpLocation = extractLocation(message, flag);
                tempPickUpArray = tempPickUpLocation.split("\\s");

                if(tempPickUpArray.length < 20)
                {
                    pickUpLocation = tempPickUpLocation;
                    sendJson(pickUpLocation,"NO","NO");

                    mprogress.setVisibility(View.VISIBLE);

                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            mprogress.setVisibility(View.GONE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            Log.e("PLF",pickUpLocationFlag + "");
                            if(pickUpLocationFlag && userPlacePickUpFlag) {
                                sendBotMessage("Trip Id is being generated. Thanks for your patience.");
                                bookFlow(dropLocation,pickUpLocation);


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

    void bookFlow(final String dropLocation1, final String pickUpLocation1)
    {
        Log.e("here","boook flow");
        runFareCalculator(dropLocation1, pickUpLocation1, "6");
        mprogress.setVisibility(View.VISIBLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                public void run() {
                    mprogress.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                    Log.e("here","before is bid received");
                    if (isBidReceivedFlag) {
                        Log.e("here","after is bid received + received bidrate is" + receivedBidRate);
                        sendJson(tempDropLocation, tempPickUpLocation, receivedBidRate);
                        mprogress.setVisibility(View.VISIBLE);
                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Log.e("here","after sendJson");
                        Handler handler1 = new Handler();
                        handler1.postDelayed(new Runnable() {
                            public void run() {
                                mprogress.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                Log.e("here","before user id flag");
                                if (UserIdFlag) {
                                    Log.e("here","after user id flag");
                                    String message = "";
                                    message += "Drop Location: " + dropLocation1 + "\n" + "PickUp Location: " + pickUpLocation1 + "\n" + "Id is: " + UserId + "\n";

                                    isCardFlag = true;
                                    sendBotMessage(message);
                                    isCardFlag = false;
                                    Log.e("here","before start trip creation");
                                    startTripCreation();
                                    Log.e("here","after trip creation");
                                    mprogress.setVisibility(View.VISIBLE);

                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    final Handler handler2 = new Handler();
                                    handler2.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            mprogress.setVisibility(View.GONE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                            if (moreTryFlag) {
                                                Log.e("try count and TOMF", tryOnceMoreFlag + " " + tryCount + "");
                                                if (tryCount < 8 && tryOnceMoreFlag) {
                                                    startTripCreation();

                                                    mprogress.setVisibility(View.VISIBLE);

                                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                    handler2.postDelayed(this, 15000);
                                                    mprogress.setVisibility(View.GONE);
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                    Log.e("inside ", "again");
                                                    Log.e("try count", tryCount + " " + tryOnceMoreFlag + "");
                                                }
                                                if (tryCount == 8 && tryOnceMoreFlag) {
                                                    Log.e("inside", "middle");
                                                    runFailService();
                                                    mprogress.setVisibility(View.VISIBLE);

                                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                    Handler handler3 = new Handler();
                                                    handler3.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            mprogress.setVisibility(View.GONE);
                                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                            sendBotMessage("We are unable to fetch the driver right now. Please try after sometime");
                                                        }
                                                    }, 10000);


                                                }
                                                if (tryCount < 8 && tryOnceMoreFlag == false) {
                                                    Log.e("about", "to start  driver and vehicle");
                                                    startDriverAndVehicleService();

                                                    mprogress.setVisibility(View.GONE);
                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                                                    Handler handler3 = new Handler();
                                                    handler3.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            mprogress.setVisibility(View.GONE);
                                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                            String message1 = null;
                                                            sendBotMessage("your trip is accepted by below driver.Please reach out to driver if needed.Contact support@vihik.com incase of any issues");
                                                            message1 += "Driver Name: " + driverName + "\n" + "Driver Mobile Number: " + driverMob + "\n" + "Vehicle Plate Number: " + vehiclePlateNum;
                                                            isCardFlag = true;
                                                            sendBotMessage(message1);
                                                            isCardFlag = false;

                                                            createFile();
                                                            downLoadFile();
                                                                                    /*createFile();
                                                                                    downLoadFile();*/
                                                        }
                                                    }, 10000);
                                                }
                                            } else
                                                sendBotMessage("Unable to process further. Please try after some time");
                                        }
                                    }, 15000);

                                } else {
                                    sendBotMessage("Some technical issue. Please try after sometime");
                                    UserIdFlag = false;
                                    fastUserPlaceDropFlag = false;
                                    fastUserPlacePickFlag = false;
                                    localAllVerifiedFlag = false;

                                }
                            }
                        }, 6000);
                    } else {
                        sendBotMessage("Some technical issue. Please try after sometime");
                        UserIdFlag = false;
                        fastUserPlaceDropFlag = false;
                        fastUserPlacePickFlag = false;
                        localAllVerifiedFlag = false;

                    }

                }
            }, 5000);

    }

    String extractLocation(String mes, int flag)
    {
        String completeLocation = "";
        boolean f = true;
        postagger(mes);
        mprogress.setVisibility(View.VISIBLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {

            @Override
            public void run() {
                mprogress.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        },5000);
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

    String postagger(String mes) {
        String posTagged = null;

        posTagged = tagger.tagString(mes);
        return posTagged;
        //rajorTaggedString = posTagged;
    ///new TextRajor(this).execute(mes);

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.e("this is nice","yo");
            }
        },5000);
*/
        //return "Bid/VB a/A cab/NN from/IN Almond/NNP Bakery/NNP Hyderabad/NNP to/TO CharMinar/NNP at/IN 10/CC";
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
    void sendJson(final String dLocation, final String picLocation, String bid)
    {
        String bidorBook = "";
        if(fastBidBookFlag)
            bidorBook = "Bid";
        else
            bidorBook = "Book";
        String[] net = {dLocation,picLocation, bid, bidorBook};
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
                if(id == 4) {
                    fastBookBid(st[id]);
                    fastBidBookFlag = true;
                }
                if(id == 3)
                    sendBotMessage("Please provide your drop location");
            }
        });

    }

    void fastBookBid(String book_or_bid)
    {
        BidorBook = book_or_bid;
        sendBotMessage("You have chosen " + book_or_bid + " option.\n You may follow the below template.");
        if(book_or_bid.contains("Fast Book"))
            sendBotMessage("Example: to Char Minar from Almond Bakery.");
        else
            sendBotMessage("Example: Bid a cab to Char Minar Hyderabad International Airport @ Rs. 10/km.");
        //sendBotMessage("If you want your current location as pick up then you may directly write your drop location");

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
        //String posTaggedString = rajorTaggedString;
        Log.e("the postagged string",posTaggedString);
        String[] posArray = posTaggedString.split("\\s");
        String[] innerposArray;

        String tempMessage = "";
        boolean toFlag = false;
        boolean fromFlag = false;

        tempFastMessage = message;
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
            sendJson(tempDropLocation, "NO","NO");
            sendJson(tempPickUpLocation, "NO","NO");
            sendBotMessage("We are verifying your drop and pick up location. Thanks  for your patience");

            mprogress.setVisibility(View.VISIBLE);

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                public void run() {
                    mprogress.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                    Log.e("f",fastUserPlaceDropFlag + " " + fastUserPlacePickFlag);
                    if(fastUserPlaceDropFlag && fastUserPlacePickFlag) {
                        localAllVerifiedFlag = true;


                        Log.e("temp fast",tempFastMessage + localAllVerifiedFlag + fastBidBookFlag + localBidFlag);

                        mprogress.setVisibility(View.VISIBLE);

                        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {

                                mprogress.setVisibility(View.GONE);
                                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                Log.e("lav",localAllVerifiedFlag + "");
                                if(localAllVerifiedFlag)
                                {
                                    if(localBidFlag)
                                    {
                                        tempFastMessage = tempFastMessage.replace(tempDropLocation,"");
                                        tempFastMessage = tempFastMessage.replace(tempPickUpLocation,"");
                                        bidRate = tempFastMessage.replaceAll("[^0-9]","");
                                        Log.e("the fare","its here");
                                        runFareCalculator(tempDropLocation, tempPickUpLocation, bidRate);
                                        Log.e("The fare  rate is", receivedBidRate);

                                    }
                                    mprogress.setVisibility(View.VISIBLE);

                                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    Handler handle = new Handler();
                                    handle.postDelayed(new Runnable() {
                                        public void run() {
                                            mprogress.setVisibility(View.GONE);
                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                            if(isBidReceivedFlag) {
                                                sendJson(tempDropLocation, tempPickUpLocation, receivedBidRate);
                                                mprogress.setVisibility(View.VISIBLE);
                                                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                Handler handler1 = new Handler();
                                                handler1.postDelayed(new Runnable() {
                                                    public void run() {
                                                        mprogress.setVisibility(View.GONE);
                                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                        if (UserIdFlag) {
                                                            String message = "";
                                                            message += "Drop Location: " + tempDropLocation + "\n" + "PickUp Location: " + tempPickUpLocation + "\n" + "Id is: " + UserId + "\n";
                                                            //sendBotMessage("The drop location is: " + tempDropLocation + ".");
                                                            //sendBotMessage("The pick up location is: " + tempPickUpLocation + ".");
                                                            //sendBotMessage("The Id is: " + UserId + ".");
                                                            if (localBidFlag) {
                                                                //sendBotMessage("The bid rate as given by you is: " + bidRate + ".");
                                                                message += "Bid Rate: " + bidRate + "\n" + "Thanks for booking through us.";
                                                            }
                                                            //sendBotMessage("Thanks for booking through us.");
                                                            //sendBotMessage("Press 1 to make a new booking");
                                                            isCardFlag = true;
                                                            sendBotMessage(message);
                                                            isCardFlag = false;
                                                            startTripCreation();
                                                            mprogress.setVisibility(View.VISIBLE);

                                                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                            final Handler handler2 = new Handler();
                                                            handler2.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    mprogress.setVisibility(View.GONE);
                                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                                    if(moreTryFlag) {
                                                                        Log.e("try count and TOMF", tryOnceMoreFlag + " " + tryCount + "");
                                                                        if (tryCount < 8 && tryOnceMoreFlag) {
                                                                            startTripCreation();

                                                                            mprogress.setVisibility(View.VISIBLE);

                                                                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                                            handler2.postDelayed(this, 15000);
                                                                            mprogress.setVisibility(View.GONE);
                                                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                                            Log.e("inside ", "again");
                                                                            Log.e("try count", tryCount + " " + tryOnceMoreFlag + "");
                                                                        }
                                                                        if (tryCount == 8 && tryOnceMoreFlag) {
                                                                            Log.e("inside", "middle");
                                                                            runFailService();
                                                                            mprogress.setVisibility(View.VISIBLE);

                                                                            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                                                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                                            Handler handler3 = new Handler();
                                                                            handler3.postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    mprogress.setVisibility(View.GONE);
                                                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                                                    sendBotMessage("We are unable to fetch the driver right now. Please try after sometime");
                                                                                }
                                                                            }, 10000);


                                                                        }
                                                                        if (tryCount < 8 && tryOnceMoreFlag == false) {
                                                                            Log.e("about", "to start  driver and vehicle");
                                                                            startDriverAndVehicleService();

                                                                            mprogress.setVisibility(View.GONE);
                                                                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);


                                                                            Handler handler3 = new Handler();
                                                                            handler3.postDelayed(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    mprogress.setVisibility(View.GONE);
                                                                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                                                                    String message1 = "";
                                                                                    sendBotMessage("your trip is accepted by below driver.Please reach out to driver if needed.Contact support@vihik.com incase of any issues");
                                                                                    message1 += "Driver Name: " + driverName + "\n" + "Driver Mobile Number: " + driverMob + "\n" + "Vehicle Plate Number: " + vehiclePlateNum;
                                                                                    isCardFlag = true;
                                                                                    sendBotMessage(message1);
                                                                                    isCardFlag = false;

                                                                                    createFile();
                                                                                    downLoadFile();
                                                                                    /*createFile();
                                                                                    downLoadFile();*/
                                                                                }
                                                                            }, 10000);
                                                                        }
                                                                    }
                                                                    else
                                                                        sendBotMessage("Unable to process further. Please try after some time");
                                                                }
                                                            }, 15000);

                                                        } else {
                                                            sendBotMessage("Some technical issue. Please try after sometime");
                                                            UserIdFlag = false;
                                                            fastUserPlaceDropFlag = false;
                                                            fastUserPlacePickFlag = false;
                                                            localAllVerifiedFlag = false;

                                                        }
                                                    }
                                                }, 6000);
                                            }
                                            else {
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
            sendJson(tempDropLocation, "NO", "NO");
            tempCurrentLocation = displayLocation();
            sendBotMessage("We are verifying your drop and  current location. Thanks for your patience");
            mprogress.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

            Handler handle = new Handler();
            handle.postDelayed(new Runnable() {
                public void run() {
                    mprogress.setVisibility(View.GONE);
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
        /*tempFastMessage = message;
        Log.e("temp fast",tempFastMessage + localAllVerifiedFlag + fastBidBookFlag + localBidFlag);
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
                        Log.e("the fare","its here");
                        runFareCalculator(tempDropLocation, tempPickUpLocation, bidRate);
                        Log.e("The bid  rate is", bidRate);

                    }
                    Handler handle = new Handler();
                    handle.postDelayed(new Runnable() {
                        public void run() {
                            sendJson(tempDropLocation, tempPickUpLocation);
                            Handler handler1 = new Handler();
                            handler1.postDelayed(new Runnable() {
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
                            }, 6000);

                        }
                    }, 5000);


                }
            }
        }, 5000);
*/

    }

    void createFile()
    {
        file2 = new File(Environment.getExternalStorageDirectory() + File.separator + "file.txt");
        try {
            file2.createNewFile();
            if(file2.exists()) {
                Log.e("created","file2");
            }
        } catch (IOException e) {
            Log.e("io t2",e + "");
            e.printStackTrace();
        }

    }
    void sendToCard(String message)
    {
        LinearLayout ll = (LinearLayout) findViewById(R.id.sec_layout);
        CardView cardView = new CardView(getApplicationContext());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardView.setLayoutParams(params);
        cardView.setContentPadding(15,15,15,15);
        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
        cardView.setMaxCardElevation(15);
        cardView.setCardElevation(9);
        TextView tv = new TextView(getApplicationContext());
        tv.setLayoutParams(params);
        tv.setText(message);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
        tv.setTextColor(Color.BLUE);
        cardView.addView(tv);
        ll.addView(cardView);

    }


    void runFailService(){
        new FailService().execute(UserId);
    }
    void startDriverAndVehicleService(){

        Log.e("inside","driver n vehicle");
        String[] s = {driverId, vehicleId};
        new DriverAndVehicle(this).execute(s);

    }

    void startTripCreation()
    {
        Log.e("inside","trip creation");
        new CreateTrip(this).execute(UserId);
    }

    void runFareCalculator(String tD, String tP, String bidR){
        Log.e("it is in","runfarecalc");
        if(yesFlag) {
            bidLatitudePickUp = latitude + "";
            bidLongitudePickUp = longitude + "";
        }
        String[] bidData = {bidLatitudeDrop, bidLongitudeDrop, bidLatitudePickUp, bidLongitudePickUp, bidR};
        Log.e("runFareFunc",bidLatitudeDrop + " " + bidLongitudeDrop + " " + bidLatitudePickUp + " " + bidLongitudePickUp);
        new FareCalculator(this).execute(bidData);
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
        Log.e("userArrayLenght", userArrayLength + "");
        byUser[userArrayLength] = message;
        userArrayLength += 1;
        chatArrayAdapter.add(new ChatMessage(side, message, false));
        chatText.setText("");
        return true;
    }

    private boolean sendBotMessage(String mes) {
        Log.e("botArrayLen",botArrayLength + "");
        byBot[botArrayLength] = "BOT:: " + mes;
        botArrayLength += 1;
        if(isCardFlag == true)
            chatArrayAdapter.add(new ChatMessage(!side, mes, true));
        else{
            chatArrayAdapter.add(new ChatMessage(!side, mes, false));
        }
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

    public void statusSearch(String status,boolean placeFlag, String latitude, String longitude)
    {
        Log.e("SS", placeFlag + "");
        if(!fastBidBookFlag) {
            if (status.equals("OK") && flag == 0) {
                bidLatitudeDrop = latitude;
                bidLongitudeDrop = longitude;
                dropLocationFlag = true;
                if (placeFlag == true)
                    userPlaceDropFlag = true;
            }
            if (status.equals("OK") && flag == 1) {
                bidLatitudePickUp = latitude;
                bidLongitudePickUp= longitude;
                pickUpLocationFlag = true;
                if (placeFlag == true)
                    userPlacePickUpFlag = true;
            }
        }
        else
        {
            if(status.equals("OK") && fastDropFlag == false){
                bidLatitudeDrop = latitude;
                bidLongitudeDrop = longitude;
                fastUserPlaceDropFlag = true;
                fastDropFlag = true;
                Log.e("the first","nice");
                Log.e("D lat long", latitude + " " + longitude);
            }
            if(status.equals("OK") && fastDropFlag == true){
                bidLatitudePickUp = latitude;
                bidLongitudePickUp= longitude;
                fastUserPlacePickFlag = true;
                Log.e("the sec","nice");
                Log.e("P lat long", latitude + " " + longitude);
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

    @Override
    public void showBidRate(String output) {
        if(!output.equals("Some technical issue. Please try after sometime"))
        {
            receivedBidRate = output;
            isBidReceivedFlag = true;
        }
        else
        {
            isBidReceivedFlag = false;

        }

    }

    @Override
    public void showDriverId(String output) {

        Log.e("output",output);
        if(output.equals("Wait for sometime"))
        {

            tryOnceMoreFlag = true;
            tryCount++;
        }
        else if(output.equals("Some technical issue. Please try after sometime"))
        {
            moreTryFlag =  false;
            resetFunction();
        }
        else{
            tryOnceMoreFlag = false;
            driverId = output;
        }

    }

    @Override
    public void showVehicleId(String output) {
        vehicleId = output;
    }

    void resetFunction()
    {
        sendBotMessage("Some technical issue. Please try after sometime");
        tryOnceMoreFlag = true;
        tryCount = 0;
    }

    @Override
    public void driverMobNo(String output) {
        driverMob = output;
    }

    @Override
    public void driverFullName(String output) {
        driverName = output;
    }

    @Override
    public void vehiclePlateNo(String output) {
        vehiclePlateNum = output;
    }

    public void upLoadFile(){

        Log.e("upload to file","here");

        TransferObserver observer = transferUtility1.upload(
                "vihikbot",     //* The bucket to upload to *//*
                "user_chat",    //* The key for the uploaded object *//*
                file2      //* The file where the data to upload exists *//*
        );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("on state changed","State changed to : "+state.toString());
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                //  transferPercentage = (float)bytesCurrent/(float)bytesTotal;
                if(bytesCurrent == bytesTotal)
                {
                    //    transferComplete = true;
                    Log.e("uploaded", "successfully");
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("here","Upload Unsuccessful due to `"+ex.toString());
            }
        });

        Log.e("uploaded", "the file");


        //new UploadAmazon(transferUtility).execute(file);
    }
    public void downLoadFile(){

        TransferObserver observer = null;
        try {
            observer = transferUtility1.download(
                    "vihikbot",     //* The bucket to upload to *//*
                    "user_chat",    //* The key for the uploaded object *//*
                    file2   //* The file where the data to upload exists *//*
            );
        } catch (Exception e) {
            Log.e("exception",e + "");
            e.printStackTrace();
        }
        Log.e("downloaded", "the file");
        observer.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("on state download","change");
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.e("the percent download", percentage + "");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("some error", "while download");
            }

        });
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String ret = "";
                StringBuilder text = new StringBuilder();

                try {
                    BufferedReader br = new BufferedReader(new FileReader(file2));
                    String line;

                    while ((line = br.readLine()) != null) {
                        text.append(line);
                        text.append('\n');
                    }
                    Log.e("the file",text + "");

                    br.close();
                }
                catch (IOException e) {
                    Log.e("the exception", e + "");
                }
                ret = text + "" + "\n";
                ret += "NEW USER";
                for(int i = 0; i< Math.max(botArrayLength, userArrayLength); i++)
                {
                    Log.e("this","inside loop");
                    if(i < botArrayLength)
                        ret += byBot[i];
                    if(i < userArrayLength)
                        ret += byUser[i];
                    ret += "\n";
                }

                writeChatToFile(ret);
                upLoadFile();
                Handler handler1 =  new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(file2.exists()) {
                            Log.e("file exists","file exists");
                            file2.delete();
                        }
                    }
                },20000);

            }
        },20000);

    }
    void writeChatToFile(String chat)
    {
        try {

            FileWriter writer = new FileWriter(file2);
            writer.append(chat);
            writer.flush();
            writer.close();
            Log.e("saved", "saved");
        } catch (Exception e) {
            Log.e("could not","writechat");
            e.printStackTrace();
        }

    }
/*
    @Override
    public void taggedString(String output)
    {
       // rajorTaggedString = output;
        Log.e("please","w");
    }
*/

    @Override
    public void amazonMethods(TransferUtility transferUtility, AmazonS3Client amazonS3) {
        transferUtility1 = transferUtility;
        s3 = amazonS3;
    }

}

class Network extends AsyncTask<String[],Void,String[]>
{
    public interface TranferResult {
        void process(String output);
        void statusSearch(String status, boolean placeFlag, String latitude, String longitude);
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
        String totalBid = params[0][2];
        String bidorBook = params[0][3];
        String bidredius = "3";
        String latitude = "";
        String longitude = "";

        if(bidorBook.equals("Book")) {
            bidredius = "0";
            totalBid = "0";
        }
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
                HttpPost post = new HttpPost("http://35.154.105.140:8080/testapi/webapi/trips/trip/book2");
                Log.e("after", "post");

                json.put("t_bidamount", totalBid);
                json.put("t_bidredius", bidredius);
                json.put("t_bookdatetime", currentDateTimeString);
                json.put("t_picdatetime", "");
                json.put("t_dropplace", dropLocation);
                json.put("t_mobileno", "9676190692");
                json.put("t_dropdatetime", "");
                json.put("t_picplace", pickUpLocation);
                json.put("t_status", "New");
                json.put("t_totalfare", "200");
                json.put("t_type", bidorBook);
                json.put("td_driverid", "1127");
                json.put("tu_userid", "773");
                json.put("tv_vehicalid", "1128");
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
                        if(jsonObject1.getString("formatted_address").contains("Hyderabad")) {
                            placeFlag = true;
                            Double longitudeD = (jsonObject1.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));
                            Double latitudeD = (jsonObject1.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                            longitude = longitudeD.toString();
                            latitude = latitudeD.toString();
                            Log.e("lat",latitude);
                            Log.e("long", longitude);
                        }
                    }
                    tf.statusSearch(status, placeFlag, latitude, longitude);

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
       //
        try {
            tagger = new MaxentTagger(
                    "taggers/left3words-wsj-0-18.tagger");
        }
        catch(Exception e)
        {
            Log.e("some exception","some");
        }
        mt.maxentTagger(tagger);
        return null;
    }
}



class FareCalculator extends AsyncTask<String[], Void, String>
{


    public interface FareResult {
        void showBidRate(String output);
    }
    public  FareResult fareResult = null;


    public FareCalculator(FareResult fareResult)
    {
       this.fareResult = fareResult;
    }
    @Override
    protected String doInBackground(String[]... strings) {

        String latDrop = strings[0][0];
        String longDrop = strings[0][1];
        String latPick = strings[0][2];
        String longPick = strings[0][3];
        String bidRate = strings[0][4];

        Looper.prepare(); //For Preparing Message Pool for the child Thread
        HttpClient client = new DefaultHttpClient();
        HttpConnectionParams.setConnectionTimeout(client.getParams(), 20000); //Timeout Limit
        HttpResponse response;
        JSONObject json = new JSONObject();

        try {

            Log.e("in fare", "try");
            HttpPost post = new HttpPost("http://35.154.105.140:8080/testapi/webapi/fares/fare");
            Log.e("after fare", "post");

            json.put("f_piclat", latPick);
            json.put("f_piclog", longPick);
            json.put("f_droplat", latDrop);
            json.put("f_droplog", longDrop);
            json.put("f_price", bidRate);

            StringEntity se = new StringEntity(json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            Log.e("after fare", "entity");
            response = client.execute(post);
            Log.e("code fare", response.getStatusLine().getStatusCode() + "");
            Log.e("response fare", response + "");

                    /*Checking response */
            if (response != null) {
                Log.e("in fare", "res");
                String result = EntityUtils.toString(response.getEntity()); //Get the data in the entity
                Log.e("the result fare", result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("success").equals("true")) {
                    String data = jsonObject.getString("data");
                   fareResult.showBidRate(data);
                }
                else
                    fareResult.showBidRate("Some technical issue. Please try after sometime");


            }

        } catch (Exception e) {
            Log.e("ex fare", e + "");
            e.printStackTrace();
        }

            return null;
    }
}

class CreateTrip extends AsyncTask<String, Void, String>
{


    public interface TripResult {
        void showDriverId(String output);
        void showVehicleId(String output);
    }
    public  TripResult tripResult = null;


    public CreateTrip(TripResult tripResult)
    {
       this.tripResult = tripResult;
    }
    @Override
    protected String doInBackground(String... strings) {

        String tripId = strings[0];
        Log.e("the user id is",tripId);

        if (Looper.myLooper() == null)
        {
            Looper.prepare();
        }
        HttpClient client = new DefaultHttpClient();
       // HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
        HttpResponse response;
        //JSONObject json = new JSONObject();
         HttpGet request = new HttpGet();
        try {

            request.setURI(URI.create("http://35.154.105.140:8080/testapi/webapi/trips/trip2/" + tripId));
            /*Log.e("in trip", "try");
            HttpPost post = new HttpPost("http://35.166.44.35:8080/testapi/webapi/trips/trip2/" + tripId);
            Log.e("after fare", "post");

            json.put("f_piclat", latPick);
            json.put("f_piclog", longPick);
            json.put("f_droplat", latDrop);
            json.put("f_droplog", longDrop);
            json.put("f_price", bidRate);

            StringEntity se = new StringEntity(json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            Log.e("after fare", "entity");*/
            response = client.execute(request);
            Log.e("code fare", response.getStatusLine().getStatusCode() + "");
            Log.e("response fare", response + "");

                    /*Checking response */
            if (response != null) {
                Log.e("in fare", "res");
                String result = EntityUtils.toString(response.getEntity()); //Get the data in the entity
                Log.e("the result fare", result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("success").equals("true")) {
                    String status = jsonObject.getJSONObject("data").getString("t_status");
                    Log.e("status",status);
                    // code changed  here for testing. later change new to Accepted.
                    if(status.equals("Accepted")) {
                        String driverId = jsonObject.getJSONObject("data").getString("td_driverid");
                        String vehicleId = jsonObject.getJSONObject("data").getString("tv_vehicalid");
                        tripResult.showDriverId(driverId);
                        tripResult.showVehicleId(vehicleId);
                    }
                    else{
                        tripResult.showDriverId("Wait for sometime");
                    }

                }
                else
                    tripResult.showDriverId("Some technical issue. Please try after sometime");


            }

        } catch (Exception e) {
            Log.e("ex fare", e + "");
            e.printStackTrace();
        }

            return null;
    }
}


class DriverAndVehicle extends AsyncTask<String[], Void, String>
{


    public interface DVResult {
        void driverMobNo(String output);
        void driverFullName(String output);
        void vehiclePlateNo(String output);
    }
    public  DVResult dvResult = null;


    public DriverAndVehicle(DVResult dvResult)
    {
        this.dvResult = dvResult;
    }
    @Override
    protected String doInBackground(String[]... strings) {

        String driverId = strings[0][0];
        String vehicleId = strings[0][1];
        String driverMob = "";
        String driverName = "";
        if (Looper.myLooper() == null)
        {
            Looper.prepare();
        }
        HttpClient client = new DefaultHttpClient();
        HttpClient client1 = new DefaultHttpClient();
        HttpResponse response;
        HttpResponse response1;
        HttpGet request = new HttpGet();
        HttpGet request1 = new HttpGet();
        try {

            request.setURI(URI.create("http://35.154.105.140:8080/testapi/webapi/drivers/driver2/" + driverId));
            response = client.execute(request);
            Log.e("code driver", response.getStatusLine().getStatusCode() + "");
            Log.e("response fare", response + "");


            if (response != null) {
                Log.e("in fare", "res");
                String result = EntityUtils.toString(response.getEntity()); //Get the data in the entity
                Log.e("the result fare", result);
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("success").equals("true")) {
                    driverMob = jsonObject.getJSONObject("data").getString("d_mobno");
                    driverName = jsonObject.getJSONObject("data").getString("d_fullName");
                } else {
                    dvResult.driverFullName("Some technical issue. Please try after sometime");
                    dvResult.driverMobNo("Some technical issue. Please try after sometime");
                }


            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        request1.setURI(URI.create("http://35.154.105.140:8080/testapi/webapi/vehicals/vehical2/" + vehicleId));
        try {
            response1 = client1.execute(request1);

            if (response1 != null) {
                Log.e("in fare", "res");
                String result1 = null; //Get the data in the entity
                try {
                    result1 = EntityUtils.toString(response1.getEntity());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("the result fare", result1);
                JSONObject jsonObject1 = new JSONObject(result1);
                if (jsonObject1.getString("success").equals("true")) {
                    String vehicleNum = jsonObject1.getJSONObject("data").getString("v_licenPlatNo");
                    dvResult.driverMobNo(driverMob);
                    dvResult.driverFullName(driverName);
                    dvResult.vehiclePlateNo(vehicleNum);

                } else {
                    dvResult.vehiclePlateNo("Some technical issue. Please try after sometime");

                }


            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}


class FailService extends AsyncTask<String, Void, String>
{

    @Override
    protected String doInBackground(String... strings) {

        String UserId = strings[0];
        if (Looper.myLooper() == null)
        {
            Looper.prepare();
        }
        HttpGet request = new HttpGet();
        JSONObject json = new JSONObject();
        HttpClient client = new DefaultHttpClient();
        
        HttpResponse response;
        
       
        
        try {


            Log.e("in fare", "try");
            HttpPost post = new HttpPost("http://35.154.105.140:8080/testapi/webapi/trips/trip/book/status2/" + UserId);
            Log.e("after fare", "post");

            json.put("t_status", "failed");
           

            StringEntity se = new StringEntity(json.toString());
            se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            post.setEntity(se);
            Log.e("after fare", "entity");
            response = client.execute(post);
            Log.e("code fare", response.getStatusLine().getStatusCode() + "");
            Log.e("response fare", response + "");


            request.setURI(URI.create("http://35.154.105.140:8080/testapi/webapi/trips/trip/book/status2/" + UserId));
            response = client.execute(request);
            Log.e("code driver", response.getStatusLine().getStatusCode() + "");
            Log.e("response fare", response + "");


            if (response != null) {
               Log.e("updated","suc deleted trip");


            }
        } catch (JSONException e1) {
            e1.printStackTrace();
        } catch (ClientProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return null;
    }
}
/*
class TextRajor extends AsyncTask<String, Void, Void>
{




    public interface TextRajorInterface {
       void taggedString(String output);
    }
    public TextRajorInterface textRajorInterface = null;

    public TextRajor(TextRajorInterface textRajorInterface) {
        this.textRajorInterface = textRajorInterface;
    }
    @Override
    protected Void doInBackground(String... params) {
        String toParse = params[0];
        String finalWord = null;
        TextRazor client = new TextRazor("7f542aa2a6a28b70ec49b57d2991277d938985c9a12ab69d7b1a160b");

        client.addExtractor("words");
        client.addExtractor("entities");

        AnalyzedText response = null;
        try {
            response = client.analyze(toParse);
        } catch (NetworkException e) {
            e.printStackTrace();
        } catch (AnalysisException e) {
            e.printStackTrace();
        }

        for (Sentence sentence : response.getResponse().getSentences()) {
            for (Word word : sentence.getWords()) {
                finalWord += word + "/" + word.getPartOfSpeech();
                Log.e("pos", word.getPartOfSpeech());
            }
        }
        Log.e("fw",finalWord);
        textRajorInterface.taggedString(finalWord);

        return null;
    }
}*/

class AmazonThread extends AsyncTask<Void, Void, Void>
{
    public interface AmazonInterface {

        void amazonMethods(TransferUtility transferUtility, AmazonS3Client amazonS3);

    }
    AmazonInterface amazonInterface = null;
    Context context;
    public AmazonThread(AmazonInterface amazonInterface, Context context)
    {
        this.amazonInterface = amazonInterface;
        this.context = context;
    }




    @Override
    protected Void doInBackground(Void... voids) {


        AmazonS3Client s3;
        TransferUtility transferUtility;
        Log.e("creating connection","doing");
        try {
            CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                    context,
                    "us-west-2:c174953c-21c7-400e-95b6-b9c5737a3657",
                    Regions.US_WEST_2
            );

            Log.e("done with connection","done");
            s3 = new AmazonS3Client(credentialsProvider);
            s3.setRegion(Region.getRegion(Regions.US_WEST_2));
            transferUtility = new TransferUtility(s3, context);
            amazonInterface.amazonMethods(transferUtility, s3);

        }
        catch(Exception e)
        {
            Log.e("upload exception",e + "");
        }
        return null;


    }
}

class UploadAmazon extends AsyncTask<File, Void, Void>
{
    TransferUtility transferUtility = null;
    public UploadAmazon(TransferUtility transferUtility)
    {
        this.transferUtility = transferUtility;
    }

    @Override
    protected Void doInBackground(File... files) {
        File uploadFile = files[0];
        Log.e("before uploading","this");
        TransferObserver observer = transferUtility.upload(
                "vihikbot",     //* The bucket to upload to *//*
                "user_chat",    //* The key for the uploaded object *//*
                uploadFile       //* The file where the data to upload exists *//*
        );
        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
            Log.e("on state changed","State changed to : "+state.toString());
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                //  transferPercentage = (float)bytesCurrent/(float)bytesTotal;
                if(bytesCurrent == bytesTotal)
                {
                    //    transferComplete = true;
                    Log.e("uploaded", "successfully");
                }
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("here","Upload Unsuccessful due to `"+ex.toString());
            }
        });

        Log.e("uploaded", "the file");

        return null;
    }
}


class DownLoadAmazon extends AsyncTask<File, Void, Void>
{
    Context context;
    TransferUtility transferUtility = null;
    public DownLoadAmazon(Context context,TransferUtility transferUtility)
    {
        this.transferUtility = transferUtility;
        this.context = context;
    }

    @Override
    protected Void doInBackground(File... files) {
        File downloadedFile = files[0];

        TransferObserver observer = transferUtility.upload(
                "vihikbot",     //* The bucket to upload to *//*
                "user_chat",    //* The key for the uploaded object *//*
                downloadedFile        //* The file where the data to upload exists *//*
        );
        Log.e("downloaded", "the file");

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("text2.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                Log.e("the read string",ret);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return null;
    }

}
