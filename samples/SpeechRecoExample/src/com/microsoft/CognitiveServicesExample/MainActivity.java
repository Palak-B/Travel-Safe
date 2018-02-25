/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license.
 * //
 * Project Oxford: http://ProjectOxford.ai
 * //
 * ProjectOxford SDK GitHub:
 * https://github.com/Microsoft/ProjectOxford-ClientSDK
 * //
 * Copyright (c) Microsoft Corporation
 * All rights reserved.
 * //
 * MIT License:
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * //
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * //
 * THE SOFTWARE IS PROVIDED ""AS IS"", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.microsoft.CognitiveServicesExample;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;


import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.microsoft.bing.speech.SpeechClientStatus;
import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements ISpeechRecognitionServerEvents, SensorEventListener {
    int m_waitSeconds = 0;
    DataRecognitionClient dataClient = null;
    MicrophoneRecognitionClient micClient = null;
    FinalResponseStatus isReceivedResponse = FinalResponseStatus.NotReceived;

    SQLiteDatabase db;
    private SensorManager mSensorManager;
    private Sensor mProximity;

    int count=0;
    //RadioGroup _radioGroup;
    //Button _buttonSelectMode;
    Button _startButton;
    TextToSpeech ttobj;
    Button start, submit, end;
    TextView txt;

    String time;
    int endpressed;

    static int c=0;
    String loc;
    static double lati,longi;
    int sensorstart=0;
    int ss=0;
    Spinner sp;
    String ts;
    int ti;

    private FusedLocationProviderClient mFusedLocationClient;
    static int started = 0;



    public enum FinalResponseStatus {NotReceived, OK, Timeout}

    /**
     * Gets the primary subscription key
     */
    public String getPrimaryKey() {
        return this.getString(R.string.primaryKey);
    }

    /**
     * Gets the LUIS application identifier.
     * @return The LUIS application identifier.
     */
    private String getLuisAppId() {
        return this.getString(R.string.luisAppID);
    }

    /**
     * Gets the LUIS subscription identifier.
     * @return The LUIS subscription identifier.
     */
    private String getLuisSubscriptionID() {
        return this.getString(R.string.luisSubscriptionID);
    }

    /**
     * Gets a value indicating whether or not to use the microphone.
     * @return true if [use microphone]; otherwise, false.
     */
    private Boolean getUseMicrophone() {
        //int id = this._radioGroup.getCheckedRadioButtonId();
        //return id == R.id.micIntentRadioButton ||id == R.id.micDictationRadioButton ||id == (R.id.micRadioButton - 1);
        return true;
    }

    /**
     * Gets a value indicating whether LUIS results are desired.
     * @return true if LUIS results are to be returned otherwise, false.
     */
    /*private Boolean getWantIntent() {
        int id = this._radioGroup.getCheckedRadioButtonId();
        return id == R.id.dataShortIntentRadioButton ||
                id == R.id.micIntentRadioButton;
    }*/

    /**
     * Gets the current speech recognition mode.
     * @return The speech recognition mode.
     */
    private SpeechRecognitionMode getMode() {
        //int id = this._radioGroup.getCheckedRadioButtonId();
        //if (id == R.id.micDictationRadioButton ||id == R.id.dataLongRadioButton) {
        //return SpeechRecognitionMode.LongDictation;
        //}

        return SpeechRecognitionMode.ShortPhrase;
    }

    /**
     * Gets the default locale.
     * @return The default locale.
     */
    private String getDefaultLocale() {
        return "en-in";
    }

    /**
     * Gets the short wave file path.
     * @return The short wave file.
     */
    private String getShortWaveFile() {
        return "whatstheweatherlike.wav";
    }

    /**
     * Gets the long wave file path.
     * @return The long wave file.
     */
    private String getLongWaveFile() {
        return "batman.wav";
    }

    /**
     * Gets the Cognitive Service Authentication Uri.
     * @return The Cognitive Service Authentication Uri.  Empty if the global default is to be used.
     */
    private String getAuthenticationUri() {
        return this.getString(R.string.authenticationUri);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //this._radioGroup = (RadioGroup)findViewById(R.id.groupMode);
        //this._buttonSelectMode = (Button)findViewById(R.id.buttonSelectMode);

        start=(Button)findViewById(R.id.button2);
        submit=(Button)findViewById(R.id.button3);
        end=(Button)findViewById(R.id.button4);
        txt=(TextView)findViewById(R.id.textView2);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sp=(Spinner)findViewById(R.id.spinner);
        final String choices[]={"15sec","30sec","45sec","60sec"};
        ArrayAdapter adapter=new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1,choices);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String ts=choices[i];
                ts=ts.substring(0,2);
                ti=Integer.parseInt(ts);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ttobj=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                ttobj.setLanguage(Locale.UK);
            }
        });

        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                submit.setVisibility(View.VISIBLE);
                txt.setVisibility(View.VISIBLE);
                sp.setVisibility(View.VISIBLE);
                start.setVisibility(View.GONE);
                ttobj.speak("How long are you planning to travel for", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorstart=1;
                end.setVisibility(View.VISIBLE);
                txt.setVisibility(View.GONE);
                mSensorManager.registerListener(MainActivity.this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
                submit.setVisibility(View.GONE);
                sp.setVisibility(View.INVISIBLE);
                endpressed=0;
                ttobj.speak("Wish you a happy journey", TextToSpeech.QUEUE_FLUSH, null);
                //time=value.getText()+"";
                //String s=time.substring(time.length()-1);
                //final int i=Integer.parseInt(time)*1000;
                c=0;
                //Toast.makeText(MainActivity.this,ti+"",Toast.LENGTH_SHORT).show();
                new CountDownTimer(ti*1000,1000){
                    @Override
                    public void onTick(long l) {
                        if(endpressed!=1) {

                                //end.setVisibility(View.GONE);

                                //ttobj.speak("Emergency mode activated, what can I do for you", TextToSpeech.QUEUE_FLUSH, null);


                                //StartButton_Click();
                            }
                        }

                    @Override
                    public void onFinish() {
                        if(endpressed!=1 && ss==0) {
                            //end.setVisibility(View.GONE);
                            ttobj.speak("Your travel time has ended, have you reached your destination safely", TextToSpeech.QUEUE_FLUSH, null);
                            new CountDownTimer(3000, 1000){

                                @Override
                                public void onTick(long l) {

                                }

                                @Override
                                public void onFinish() {
                                    StartButton_Click();
                                }
                            }.start();

                        }
                    }
                }.start();
            }
        });
        end.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorstart=0;
                ttobj.speak("Hope you had a safe journey", TextToSpeech.QUEUE_FLUSH, null);
                end.setVisibility(View.GONE);
                start.setVisibility(View.VISIBLE);
                mSensorManager.unregisterListener(MainActivity.this);
                endpressed=1;
            }
        });
        db=openOrCreateDatabase("mydb",MODE_PRIVATE,null);


        if (getString(R.string.primaryKey).startsWith("Please")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.add_subscription_key_tip_title))
                    .setMessage(getString(R.string.add_subscription_key_tip))
                    .setCancelable(false)
                    .show();
        }

        // setup the buttons

        /*this._buttonSelectMode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                This.ShowMenu(This._radioGroup.getVisibility() == View.GONE);
            }
        });

        this._radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup rGroup, int checkedId) {
                This.RadioButton_Click(rGroup, checkedId);
            }
        });*/

        //this.ShowMenu(true);
    }

    /*private void ShowMenu(boolean show) {
        if (show) {
            this._radioGroup.setVisibility(View.VISIBLE);
            this._logText.setVisibility(View.GONE);
        } else {
            this._radioGroup.setVisibility(View.GONE);
            this._logText.setText("");
            this._logText.setVisibility(View.VISIBLE);
        }
    }*/

    /**
     * Handles the Click event of the _startButton control.
     */
    private void StartButton_Click() {
        //this._startButton.setEnabled(false);
        //this._radioGroup.setEnabled(false);

        this.m_waitSeconds = this.getMode() == SpeechRecognitionMode.ShortPhrase ? 20 : 200;

        //this.ShowMenu(false);

        this.LogRecognitionStart();

        if (this.getUseMicrophone()) {
            if (this.micClient == null) {
                /*if (this.getWantIntent()) {
                    this.WriteLine("--- Start microphone dictation with Intent detection ----");

                    this.micClient =
                            SpeechRecognitionServiceFactory.createMicrophoneClientWithIntent(
                                    this,
                                    this.getDefaultLocale(),
                                    this,
                                    this.getPrimaryKey(),
                                    this.getLuisAppId(),
                                    this.getLuisSubscriptionID());
                }
                else
                {*/
                this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                        this,
                        this.getMode(),
                        this.getDefaultLocale(),
                        this,
                        this.getPrimaryKey());
                //}

                this.micClient.setAuthenticationUri(this.getAuthenticationUri());
            }

            this.micClient.startMicAndRecognition();
        }
        //start.setVisibility(View.VISIBLE);
        mSensorManager.unregisterListener(MainActivity.this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                //Toast.makeText(MainActivity.this, "HelloWor", Toast.LENGTH_SHORT).show();
                if (location != null) {
                    //e1.setText(location.getLatitude()+"and"+location.getLongitude());
                    //Toast.makeText(MainActivity.this,location.getLatitude()+"and"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this,location.getLatitude()+"",Toast.LENGTH_SHORT).show();
                    lati=location.getLatitude();
                    longi=location.getLongitude();
                    loc=getCompleteAddressString(location.getLatitude(),location.getLongitude());
                    //Toast.makeText(MainActivity.this,location.getLongitude()+"",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this,loc+"",Toast.LENGTH_SHORT).show();
                    //Toast.makeText(MainActivity.this, "Hello", Toast.LENGTH_SHORT).show();
                    // Logic to handle location object
                }
            }
        });
        /*else
        {
            if (null == this.dataClient) {
                if (this.getWantIntent()) {
                    this.dataClient =
                            SpeechRecognitionServiceFactory.createDataClientWithIntent(
                                    this,
                                    this.getDefaultLocale(),
                                    this,
                                    this.getPrimaryKey(),
                                    this.getLuisAppId(),
                                    this.getLuisSubscriptionID());
                }
                else {
                    this.dataClient = SpeechRecognitionServiceFactory.createDataClient(
                            this,
                            this.getMode(),
                            this.getDefaultLocale(),
                            this,
                            this.getPrimaryKey());
                }

                this.dataClient.setAuthenticationUri(this.getAuthenticationUri());
            }

            this.SendAudioHelper((this.getMode() == SpeechRecognitionMode.ShortPhrase) ? this.getShortWaveFile() : this.getLongWaveFile());
        }*/
    }
    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("Location address", strReturnedAddress.toString());
            } else {
                Log.w("Location address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Location address", "Canont get Address!");
        }
        return strAdd+" latitude:"+LATITUDE+" longitude:"+LONGITUDE;
    }

    /**
     * Logs the recognition start.
     */
    private void LogRecognitionStart() {
        String recoSource;
        if (this.getUseMicrophone()) {
            recoSource = "microphone";
        } else if (this.getMode() == SpeechRecognitionMode.ShortPhrase) {
            recoSource = "short wav file";
        } else {
            recoSource = "long wav file";
        }

        this.WriteLine("\n--- Start speech recognition using " + recoSource + " with " + this.getMode() + " mode in " + this.getDefaultLocale() + " language ----\n\n");
    }

    private void SendAudioHelper(String filename) {
        RecognitionTask doDataReco = new RecognitionTask(this.dataClient, this.getMode(), filename);
        try {
            doDataReco.execute().get(m_waitSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            doDataReco.cancel(true);
            isReceivedResponse = FinalResponseStatus.Timeout;
        }
    }

    public void onFinalResponseReceived(final RecognitionResult response) {
        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);
        if (null != this.micClient && this.getUseMicrophone() && ((this.getMode() == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage)) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();
        }

        if (isFinalDicationMessage) {
            this._startButton.setEnabled(true);
            this.isReceivedResponse = FinalResponseStatus.OK;
        }

        if (!isFinalDicationMessage) {
            this.WriteLine("********* Final n-BEST Results *********");
            for (int i = 0; i < response.Results.length; i++) {
                this.WriteLine("[" + i + "]" + " Confidence=" + response.Results[i].Confidence +
                        " Text=\"" + response.Results[i].DisplayText + "\"");
            }
            if(response.Results.length>0) {
                Toast.makeText(this, "" + response.Results[response.Results.length - 1].DisplayText, Toast.LENGTH_SHORT).show();
                String s = response.Results[response.Results.length - 1].DisplayText;
                s = s.toLowerCase();
                if(s.contains("help"))
                {
                    ttobj.speak("Sending message to your emergency contacts", TextToSpeech.QUEUE_FLUSH, null);
                    Cursor c=db.rawQuery("select * from emergency_contacts",null);
                    String num,msg="";
                    while(c.moveToNext())
                    {
                        num=c.getString(1);
                        msg="Road Accident at "+loc;
                        //Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
                        /*Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"+num));
                        intent.putExtra("sms_body","I have met with an accident and it is an emergency! Please come to help me....IGNORE");
                        startActivity(intent);*/
                        SmsManager sms=SmsManager.getDefault();
                        sms.sendTextMessage(num,null,msg,null,null);
                        //String servernum="9643483654";
                        //String servermsg=msg+"with latitude "+lati+" and longitude "+longi;
                        //Toast.makeText(this,servermsg,Toast.LENGTH_SHORT).show();

                    }

                    String servernum="9643483654";
                    String servermsg=msg;
                    //Toast.makeText(this,servermsg,Toast.LENGTH_SHORT).show();
                    SmsManager sms1=SmsManager.getDefault();
                    //sms1.sendTextMessage(servernum,null,servermsg,null,null);
                }
                else if (s.contains("call")) {
                    Cursor c1=db.rawQuery("select * from contacts",null);
                    int flag=0;
                    while (c1.moveToNext()) {
                        if (s.contains(c1.getString(0))) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + c1.getString(1)));
                            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            flag=1;
                            startActivity(intent);
                            break;
                        }
                    }
                        if(flag==0)
                        {
                            ttobj.speak("Could not detect any contact with that name. Calling your first emergency contact", TextToSpeech.QUEUE_FLUSH, null);
                            new CountDownTimer(5000,1000){
                                @Override
                                public void onTick(long l) {

                                }

                                @Override
                                public void onFinish() {
                                    Cursor c2=db.rawQuery("select * from emergency_contacts",null);
                                    while(c2.moveToNext())
                                    {
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+c2.getString(1)));
                                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                            // TODO: Consider calling
                                            //    ActivityCompat#requestPermissions
                                            // here to request the missing permissions, and then overriding
                                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                            //                                          int[] grantResults)
                                            // to handle the case where the user grants the permission. See the documentation
                                            // for ActivityCompat#requestPermissions for more details.
                                            return;
                                        }
                                        startActivity(intent);
                                        break;
                                    }
                                }
                            }.start();

                        }

                }
                else if(s.contains("yes"))
                {
                    end.setVisibility(View.GONE);
                    start.setVisibility(View.VISIBLE);
                    sp.setVisibility(View.GONE);
                    ttobj.speak("Thanks you have a wonderful day", TextToSpeech.QUEUE_FLUSH, null);
                }
                else if(s.contains("no"))
                {
                    ttobj.speak("Thanks you may continue your journey", TextToSpeech.QUEUE_FLUSH, null);
                    mSensorManager.registerListener(MainActivity.this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
                    start.setVisibility(View.GONE);
                    end.setVisibility(View.VISIBLE);
                    ss=0;
                }
                else
                {
                    ttobj.speak("We have received no response from your side Sending message to your emergency contacts", TextToSpeech.QUEUE_FLUSH, null);
                    Cursor c=db.rawQuery("select * from emergency_contacts",null);
                    String num,msg="";
                    while(c.moveToNext())
                    {
                        num=c.getString(1);
                        msg="Road accident at "+loc;
                        //Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
                        /*Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"+num));
                        intent.putExtra("sms_body","I have met with an accident and it is an emergency! Please come to help me....IGNORE");
                        startActivity(intent);*/
                        SmsManager sms=SmsManager.getDefault();
                        sms.sendTextMessage(num,null,msg,null,null);
                        //String servernum="9643483654";
                        //String servermsg=msg+"with latitude "+lati+" and longitude "+longi;
                        //Toast.makeText(this,servermsg,Toast.LENGTH_SHORT).show();

                    }

                    String servernum="9643483654";
                    String servermsg=msg;
                    //Toast.makeText(this,servermsg,Toast.LENGTH_SHORT).show();
                    SmsManager sms1=SmsManager.getDefault();
                    //sms1.sendTextMessage(servernum,null,servermsg,null,null);
                }
                //start.setVisibility(View.VISIBLE);
                //mSensorManager.unregisterListener(MainActivity.this);
            }
            else
            {
                ttobj.speak("We have received no response from your side Sending message to your emergency contacts", TextToSpeech.QUEUE_FLUSH, null);
                Cursor c=db.rawQuery("select * from emergency_contacts",null);
                String num,msg="";
                while(c.moveToNext())
                {
                    num=c.getString(1);
                    msg="Road Accident at "+loc;
                    //Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
                        /*Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("sms:"+num));
                        intent.putExtra("sms_body","I have met with an accident and it is an emergency! Please come to help me....IGNORE");
                        startActivity(intent);*/
                    SmsManager sms=SmsManager.getDefault();
                    sms.sendTextMessage(num,null,msg,null,null);
                    //String servernum="9643483654";
                    //String servermsg=msg+"with latitude "+lati+" and longitude "+longi;
                    //Toast.makeText(this,servermsg,Toast.LENGTH_SHORT).show();

                }

                String servernum="9643483654";
                String servermsg=msg+"with latitude "+lati+" and longitude "+longi;
                //Toast.makeText(this,servermsg,Toast.LENGTH_SHORT).show();
                SmsManager sms1=SmsManager.getDefault();
                //sms1.sendTextMessage(servernum,null,servermsg,null,null);
                start.setVisibility(View.VISIBLE);
                end.setVisibility(View.INVISIBLE);
            }
            this.WriteLine();
        }
    }

    /**
     * Called when a final response is received and its intent is parsed
     */
    public void onIntentReceived(final String payload) {
        this.WriteLine("--- Intent received by onIntentReceived() ---");
        this.WriteLine(payload);
        this.WriteLine();
    }

    public void onPartialResponseReceived(final String response) {
        this.WriteLine("--- Partial result received by onPartialResponseReceived() ---");
        this.WriteLine(response);
        this.WriteLine();
    }

    public void onError(final int errorCode, final String response) {
        this._startButton.setEnabled(true);
        this.WriteLine("--- Error received by onError() ---");
        this.WriteLine("Error code: " + SpeechClientStatus.fromInt(errorCode) + " " + errorCode);
        this.WriteLine("Error text: " + response);
        this.WriteLine();
    }

    /**
     * Called when the microphone status has changed.
     //* @param recording The current recording state
     */
    public void onAudioEvent(boolean recording) {
        this.WriteLine("--- Microphone status change received by onAudioEvent() ---");
        this.WriteLine("********* Microphone status: " + recording + " *********");
        if (recording) {
            this.WriteLine("Please start speaking.");
        }

        WriteLine();
        if (!recording) {
            this.micClient.endMicAndRecognition();
        }
    }

    /**
     * Writes the line.
     */
    private void WriteLine() {
        this.WriteLine("");
    }

    /**
     * Writes the line.
     * @param text The line to write.
     */
    private void WriteLine(String text) {

    }

    /**
     * Handles the Click event of the RadioButton control.
    // * @param rGroup The radio grouping.
     //* @param checkedId The checkedId.
     */
    /*private void RadioButton_Click(RadioGroup rGroup, int checkedId) {
        // Reset everything
        if (this.micClient != null) {
            this.micClient.endMicAndRecognition();
            try {
                this.micClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.micClient = null;
        }

        if (this.dataClient != null) {
            try {
                this.dataClient.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            this.dataClient = null;
        }

        //this.ShowMenu(false);
        this._startButton.setEnabled(true);
    }*/

    /*
     * Speech recognition with data (for example from a file or audio source).  
     * The data is broken up into buffers and each buffer is sent to the Speech Recognition Service.
     * No modification is done to the buffers, so the user can apply their
     * own VAD (Voice Activation Detection) or Silence Detection
     * 
     * @param dataClient
     * @param recoMode
     * @param filename
     */
    private class RecognitionTask extends AsyncTask<Void, Void, Void> {
        DataRecognitionClient dataClient;
        SpeechRecognitionMode recoMode;
        String filename;

        RecognitionTask(DataRecognitionClient dataClient, SpeechRecognitionMode recoMode, String filename) {
            this.dataClient = dataClient;
            this.recoMode = recoMode;
            this.filename = filename;
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Note for wave files, we can just send data from the file right to the server.
                // In the case you are not an audio file in wave format, and instead you have just
                // raw data (for example audio coming over bluetooth), then before sending up any 
                // audio data, you must first send up an SpeechAudioFormat descriptor to describe 
                // the layout and format of your raw audio data via DataRecognitionClient's sendAudioFormat() method.
                // String filename = recoMode == SpeechRecognitionMode.ShortPhrase ? "whatstheweatherlike.wav" : "batman.wav";
                InputStream fileStream = getAssets().open(filename);
                int bytesRead = 0;
                byte[] buffer = new byte[1024];

                do {
                    // Get  Audio data to send into byte buffer.
                    bytesRead = fileStream.read(buffer);

                    if (bytesRead > -1) {
                        // Send of audio data to service. 
                        dataClient.sendAudio(buffer, bytesRead);
                    }
                } while (bytesRead > 0);

            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            finally {
                dataClient.endAudio();
            }
            return null;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.exit):
                finish();
                break;
            case (R.id.showcontact):
                //Toast.makeText(this, "Hello",Toast.LENGTH_SHORT).show();
                Intent i1=new Intent(MainActivity.this,EmergencyContacts.class);
                startActivity(i1);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorstart==1) {
            float distance = sensorEvent.values[0];
            count++;
            if (count == 7) {
                ss=1;
                count=0;
                ttobj.speak("We detected some disturbance what can I do for you. Say no for dismissal", TextToSpeech.QUEUE_FLUSH, null);
                new CountDownTimer(4000,1000){

                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {
                        StartButton_Click();
                    }
                }.start();
                //end.setVisibility(View.GONE);
                //start.setVisibility(View.VISIBLE);
                mSensorManager.unregisterListener(MainActivity.this);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
