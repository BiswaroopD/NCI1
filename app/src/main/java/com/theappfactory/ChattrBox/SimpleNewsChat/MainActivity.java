package com.theappfactory.ChattrBox.SimpleNewsChat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnPaidEventListener;
import com.google.android.gms.ads.ResponseInfo;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.FIREBASE_TABLE_COMMENTS;
import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.PREFERENCE_KEY_COMMENTID;
import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.PREFERENCE_KEY_COMMENTID_VALUE_DEFAULT;
import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.PREFERENCE_KEY_DEFAULT_COUNTRY_VALUE;
import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.PREFERENCE_KEY_SELECTED_COUNTRY;
import static com.theappfactory.ChattrBox.SimpleNewsChat.PreferenceUtils.SHARED_PREFERENCE_FILE_NAME;


//import android.widget.SearchView;
//import com.crashlytics.android.Crashlytics;
//import com.google.android.gms.ads.InterstitialAd;

public class MainActivity<mSpeechRecognizer> extends
        AppCompatActivity
        implements
//        SensorEventListener,
        ChildEventListener,
        SearchView.OnQueryTextListener {
    TextToSpeech t1;
    private SensorManager sensorManager;
//        private SensorEventListener sensorEventListener;
    private float acelVal, acelLast, shake;
    //    private static int counter = 0;
//    private SpeechRecognizer mSpeechRecognizer;
    private final SpeechRecognizer mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this); //UNCOMMENT 5DEC
    final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH); //UNCOMMENT 5DEC


//    TextToSpeech t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
//        @Override
//        public void onInit(int ttsStatus) {
//            if (ttsStatus == TextToSpeech.SUCCESS) {
//                int ttsResult = t1.setLanguage(Locale.US);
//                if (ttsResult == TextToSpeech.LANG_NOT_SUPPORTED) {
//                    Log.e("error", "This language is not supported");
//                } else {
//                    ConvertTextToSpeech();
//                }
//            } else {
//                Log.e("error", "Initialization Failed!");
//            }
//        }
//    });
//
//    private void ConvertTextToSpeech() {
//        // TODO Auto-generated method stub
////        text = et.getText().toString();
////        if(text==null||"".equals(text))
////        {
//        String myText = "Content not available";
//        Log.i("Info", "Inside ConvertTextToSpeech");
//        Toast.makeText(this, "ConvertTextToSpeech", Toast.LENGTH_SHORT).show();
//        t1.speak(myText, TextToSpeech.QUEUE_FLUSH, null, "myText");
////        }else
////            tts.speak(text+"is saved", TextToSpeech.QUEUE_FLUSH, null);
//    }

    androidx.appcompat.widget.Toolbar toolbarNews;
    EditText etComment;
    FloatingActionButton fabSendComment;
    ImageButton ibtnGotoChat;
    int intCommentId = 0;
    int intBackPressed = 1;

    // Firebase instance variables
    public static final int RC_SIGN_IN = 42;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    //Data from comment db
    private FirebaseDatabase nciFirebaseDatabase;
    private DatabaseReference nciFirebaseDatabaseReference;

    //Ads
    private AdView newsBannerAdView;
    private InterstitialAd chatInterstitialAdView;

    //Variables
    String strSelectedCountry = "";
    StateVariables stateVariables = StateVariables.getINSTANCE();

    //Data related variables
    ArticleDAO articleDAO = null;
    RecyclerView rvNewsList = null;
    List<Article> retrievedArticleListFromApi = new ArrayList<>();
    List<Article> currentCountryArticleListInDB = new ArrayList<>();
    List<Article> retrievedArticleListToAdapter = new ArrayList<>();
    List<Article> newArticleList = new ArrayList<>();

    ArticleAdapter articleAdapter = null;
    List<Comment> retrievedCommentList = new ArrayList<>();

    RecyclerView.LayoutManager newsLayoutManager;
    private static final Integer RecordAudioRequestCode = 1;

    //Progress circle
//    LinearLayout progressBarLayout;

    @Override
    protected void onStart() {
        super.onStart();
//        Toast.makeText(this, "MainActivity:onStart", Toast.LENGTH_SHORT).show();
//        Log.i("debug", "onStart: ");

        firebaseAuth = firebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser != null) {
            final DatabaseReference rootRef = firebaseDatabase.getReference();

            final DatabaseReference childRef = rootRef.child(firebaseUser.getUid());
            childRef.addChildEventListener(this);
            Log.e("TAG", "Login email:" + firebaseUser.getEmail());

//            stateVariables.setFirebaseUser(firebaseUser.getDisplayName());
//            stateVariables.setFirebaseUserId(firebaseUser.getUid());
////            stateVariables.setFirebaseUserPhotoUrl(firebaseUser.getPhotoUrl().toString());
////            stateVariables.setFirebaseUserPhotoUrl("https://randomuser.me/api/portraits/men/3.jpg");
//            //            stateVariables.setFirebaseUserPhotoUrl(String.valueOf(firebaseUser.getPhotoUrl()));
//            stateVariables.setFirebaseUserEmail(firebaseUser.getEmail());
        } else {
            Log.e("TAG", "onStart: Firebase user not created. :(");
        }


        //Load the Interstitial Ad
        chatInterstitialAdView = new InterstitialAd() {
            @NonNull
            @Override
            public String getAdUnitId() {
                return null;
            }

            @Override
            public void show(@NonNull Activity activity) {

            }

            @Override
            public void setFullScreenContentCallback(@Nullable FullScreenContentCallback fullScreenContentCallback) {

            }

            @Nullable
            @Override
            public FullScreenContentCallback getFullScreenContentCallback() {
                return null;
            }

            @Override
            public void setImmersiveMode(boolean b) {

            }

            @NonNull
            @Override
            public ResponseInfo getResponseInfo() {
                return null;
            }

            @Override
            public void setOnPaidEventListener(@Nullable OnPaidEventListener onPaidEventListener) {

            }

            @Nullable
            @Override
            public OnPaidEventListener getOnPaidEventListener() {
                return null;
            }
        };
//        chatInterstitialAdView.setAdUnitId(stateVariables.getAdMobInterstitialAdUnitId());
//        chatInterstitialAdView.loadAd(new AdRequest.Builder()
//                .build()
//        );
        stateVariables.setChatInterstitialAdView(chatInterstitialAdView);
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode != RC_SIGN_IN) {
//            return;
//        }
//
//        switch (resultCode) {
//
//            case RESULT_OK :
//                Log.e("TAG", "onActivityResult: Sign in successful!");
//                startActivity(new Intent(this, MainActivity.class));
//                break;
//
//            case RESULT_CANCELED :
//                Log.e("TAG", "onActivityResult: Sign in cancelled!");
//                break;
//        }
//    }

    @Override
    public void onBackPressed() {
//        Toast.makeText(this, "MainActivity:onBackPressed: ", Toast.LENGTH_SHORT).show();
        super.onBackPressed();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        Toast.makeText(this, "MainActivity:onKeyDown: ", Toast.LENGTH_SHORT).show();
//        if(keyCode==KeyEvent.KEYCODE_BACK && intBackPressed>0) {

        if (keyCode == KeyEvent.KEYCODE_BACK && ((LinearLayoutManager) newsLayoutManager)
                .findFirstVisibleItemPosition() > 0) {

//        Toast.makeText(this, ""+event.getRepeatCount(), Toast.LENGTH_SHORT).show();

            rvNewsList.scrollToPosition(4);
            rvNewsList.smoothScrollToPosition(0);
//            Toast.makeText(this, "Please press Back again to exit.", Toast.LENGTH_SHORT).show();
//            CoordinatorLayout placeSnackBar= findViewById(R.id.coordinatorLayoutForSnackBar);
//            Snackbar snackbarPressExitTwice = Snackbar
//            Snackbar.make(placeSnackBar, "Press again to exit...", Snackbar.LENGTH_LONG)
//                    .setAction("UNDO", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            intBackPressed = 1;
////                            Toast.makeText(MainActivity.this, "UNDO clicked", Toast.LENGTH_SHORT).show();
//                        }
//                    })
//                    .show();
//            snackbarPressExitTwice.show();
            intBackPressed--;
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
//            return true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL); UNCOMMENT 5DEC
//        Toast.makeText(this, "MainActivity.onResume()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        setTheme(R.style.AppThemeNoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toast.makeText(this, "MainActivity:onCreate", Toast.LENGTH_SHORT).show();

//****        This should be commented - for one-time use only - start
//        Log.d("Instance ID", FirebaseInstanceId.getInstance().getId());
//****        This should be commented - for one-time use only - end

        //***************** Text-to-speech implementation - start **********//
        Log.d("Debug", "Text to speech implementation test.");
        Toast.makeText(this, "TTS", Toast.LENGTH_SHORT).show();
//        TextToSpeech t1 = null;
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int ttsStatus) {
                if (ttsStatus == TextToSpeech.SUCCESS) {
                    int ttsResult = t1.setLanguage(Locale.US);
                    if (ttsResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("error", "This language is not supported");
                    } else {
                        t1.speak("Hello", TextToSpeech.QUEUE_FLUSH, null, "my Text");
//                        ConvertTextToSpeech();
                    }
                } else {
                    Log.e("error", "Initialization Failed!");
                }
            }
        });

        //***************** Text-to-speech implementation - end **********//

        //**************** detecting the double shake using **************//UNCOMMENT 5DEC
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            Log.i("INFO", "Success! There's a shake sensor!!!");
//                            Toast.makeText(this, "Success! There's a pressure sensor.", Toast.LENGTH_SHORT);
//                            t1.speak("Success! There's a shake sensor.", TextToSpeech.QUEUE_FLUSH, null, "none");
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

            acelVal = SensorManager.GRAVITY_EARTH;
            acelLast = SensorManager.GRAVITY_EARTH;
            shake = 0.00f;


        } else {
            Log.e("INFO", "Failure! No shake sensor.");
//                            Toast.makeText(this, "Failure! No pressure sensor.", Toast.LENGTH_SHORT);
//                            t1.speak("Failure! No pressure sensor.", TextToSpeech.QUEUE_FLUSH, null, "none");
        }


        //**************** detecting the double shake using **************//

        //**************** speech to text ********************************//
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            checkRecordingPermission();
        }
//        final Intent speechRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        Log.d("RecognitionListener: ", "onReadyForSpeech;))");
        mSpeechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle bundle) {
                mSpeechRecognizer.startListening(speechRecognizerIntent);
                Log.d("RecognitionListener: ", "onReadyForSpeech:)");
            }

            @Override
            public void onBeginningOfSpeech() {
//                t1.speak("Please speak what you want.", TextToSpeech.QUEUE_FLUSH, null, "speak");
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//
//                    }
//                }, 5000);

                Log.d("RecognitionListener: ", "onBeginningOfSpeech");
            }

            @Override
            public void onRmsChanged(float v) {
                Log.d("RecognitionListener: ", "onRmsChanged" + v);
            }

            @Override
            public void onBufferReceived(byte[] bytes) {
                Log.d("RecognitionListener: ", "onBufferReceived");
            }

            @Override
            public void onEndOfSpeech() {
                Log.d("RecognitionListener: ", "onEndOfSpeech");
                mSpeechRecognizer.stopListening();
            }

            @Override
            public void onError(int i) {
                Log.d("RecognitionListener: ", "onError" + i);

            }

            @Override
            public void onResults(Bundle bundle) {
                Log.d("RecognitionListener: ", "onResults");
//                new Timer().schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        Log.d("RecognitionListener: ", "Waiting inside RecognitionListener.onResults for 5s.");
////                        Toast.makeText(this, "Waiting inside RecognitionListener.onResults for 5s.").show();
//                    }
//                }, 5000);

//                final Handler handler = new Handler(Looper.getMainLooper());
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.d("RecognitionListener: ", "Waiting inside Handler in RecognitionListener.onResults for 5s.");
//                        //Do something after 100ms
//                    }
//                }, 2000);


//                String n = "";
                ArrayList<String> data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                String mSpokenText = data.get(0).trim().toLowerCase();
                String mTalkBack = " You said: " + mSpokenText;
//                isReadNews =
                Log.d("RecognitionListener", mSpokenText);
                Log.d("RecognitionListener", mTalkBack);
                Log.d("RecognitionListener", "SpeakText = " + stateVariables.getSpeakText());
                Log.d("RecognitionListener", "isReadNews = " + mSpokenText.equals("read news"));
                t1.speak(mTalkBack, TextToSpeech.QUEUE_FLUSH, null, "talkback");

                if (mSpokenText.equals("read news")) {
                    t1.speak("Reading News", TextToSpeech.QUEUE_FLUSH, null, "talkback");
                    Log.d("RecognitionListener_RN", "SpeakText = " + stateVariables.getSpeakText());
                    stateVariables.setSpeakText(1);
                    Log.d("RecognitionListener_RN2", "SpeakText = " + stateVariables.getSpeakText());
                    int temp = stateVariables.getSpeakText();
                    Log.d("RecognitionListener", mTalkBack);
//                    Toast.makeText(this, "speakText = ", Toast.LENGTH_SHORT)).show();
                }
//                int iT1 = -1;
//                int iT1 = t1.speak(mTalkBack, TextToSpeech.QUEUE_FLUSH, null, "talkback");
//                Log.d("RecognitionListener:", "" + iT1);
//                Toast t = Toast.makeText(this, "You said:" + data.get(0) +".", Toast.LENGTH_SHORT);
//                t.show();
//                .setText(data.get(0));
            }

            @Override
            public void onPartialResults(Bundle bundle) {
                Log.d("RecognitionListener: ", "onPartialResults");

            }

            @Override
            public void onEvent(int i, Bundle bundle) {
                Log.d("RecognitionListener: ", "onEvent");

            }
        });

        //**************** speech to text ********************************//UNCOMMENT 5DEC

        ProgressBar pbNewsList = findViewById(R.id.pbNewsList);
        pbNewsList.setVisibility(View.VISIBLE);

//        intBackPressed = 1;
        Intent externalIntentToStartMainActivity = getIntent();
//        String strIntentCaller = externalIntentToStartMainActivity.getStringExtra("LOGIN_FROM");
//        int intLoginUser = externalIntentToStartMainActivity.getIntExtra("STARTER_ACTIVITY_CODE",stateVariables.getFIRSTLOGIN());
//        Toast.makeText(this, "MainActivity:onCreate", Toast.LENGTH_SHORT).show();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        toolbarNews = findViewById(R.id.toolbarNews);
        setSupportActionBar(toolbarNews);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//            getSupportActionBar().menu
//            getSupportActionBar().setDisplayOptions(0);
//            getSupportActionBar().setTitle(R.string.app_name);
//            getSupportActionBar().setIcon(R.drawableic_action_name_gnc);
//        }


//        //Adding button to simulate a crash for crashlytics testing - start
//        Button crashButton = new Button(this);
//        crashButton.setText("Crash!");
//        crashButton.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Crashlytics.getInstance().crash(); // Force a crash
//            }
//        });
//        addContentView(crashButton,
//                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//
        //Adding button to simulate a crash for crashlytics testing - end


//        Log.e("TAG", "Called by: " + strIntentCaller +" : " + intLoginUser + strIntentCaller );
//        Toast.makeText(this, "Called by: " + strIntentCaller +" : " + intLoginUser, Toast.LENGTH_SHORT).show();
//        if(intLoginUser == stateVariables.getFIRSTLOGIN()) {
//            finishActivity(stateVariables.getFIRSTLOGIN());
//        }
//        else if(intLoginUser ==stateVariables.getRETURNLOGIN()) {
//            finishActivity(stateVariables.getRETURNLOGIN());
//        }
//
//        //Get Firebase  User
//        firebaseAuth = firebaseAuth.getInstance();
//        firebaseUser = firebaseAuth.getCurrentUser();
//        if (firebaseUser == null) {
//            Intent loginIntent = AuthUI.getInstance()
//                    .createSignInIntentBuilder()
//                    .setIsSmartLockEnabled(false)
//                    .setAvailableProviders(Arrays.asList(
//                            new AuthUI.IdpConfig.GoogleBuilder().build(),
////                            new AuthUI.IdpConfig.FacebookBuilder().build(),
////                            new AuthUI.IdpConfig.TwitterBuilder().build(),
////                            new AuthUI.IdpConfig.GitHubBuilder().build(),
//                            new AuthUI.IdpConfig.EmailBuilder().build(),
//                            new AuthUI.IdpConfig.PhoneBuilder().build())
//                    ).build();
//            startActivityForResult(loginIntent, RC_SIGN_IN);
//            finish();
//        } else {
//            final DatabaseReference rootRef = firebaseDatabase.getReference();
//
//            final DatabaseReference childRef = rootRef.child(firebaseUser.getUid());
//            childRef.addChildEventListener(this);
//            Log.e("TAG","Login email:" + firebaseUser.getEmail());
//
//            stateVariables.setFirebaseUser(firebaseUser.getDisplayName());
//            stateVariables.setFirebaseUserId(firebaseUser.getUid());
//            stateVariables.setFirebaseUserPhotoUrl(firebaseUser.getPhotoUrl().toString());
////            stateVariables.setFirebaseUserPhotoUrl("https://randomuser.me/api/portraits/men/3.jpg");
////            stateVariables.setFirebaseUserPhotoUrl(String.valueOf(firebaseUser.getPhotoUrl()));
//            stateVariables.setFirebaseUserEmail(firebaseUser.getEmail());
////            childRef.push().setValue();
//        }


//        Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();
//        progressBarLayout = findViewById(R.id.layout_progressBarNewsList);
//        progressBarLayout.setVisibility(View.GONE);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        //Loading Interstitial Nov23 2021
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        chatInterstitialAdView = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("TAG", loadAdError.getMessage());
                        chatInterstitialAdView = null;
//                        super.onAdFailedToLoad(loadAdError);
                    }
                });

        //Loading Interstitial Nov23 2021
        //Load the Interstitial Ad
//        chatInterstitialAdView = new InterstitialAd(this);
//        chatInterstitialAdView.setAdUnitId(stateVariables.getAdMobInterstitialAdUnitId());
//        chatInterstitialAdView.loadAd(new AdRequest.Builder()
//                .build()
//        );
        stateVariables.setChatInterstitialAdView(chatInterstitialAdView);

        //Load the banner ad
        // My AdMob app ID: ca-app-pub-9128335782223331~5964859856
//        MobileAds.initialize(this, stateVariables.getAdMobAppId());
        newsBannerAdView = findViewById(R.id.newsAdView);
//        AdRequest adRequest = new AdRequest.Builder()
//                .build();
        newsBannerAdView.loadAd(adRequest);
        newsBannerAdView.setVisibility(View.VISIBLE);

        newsBannerAdView.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
//                Toast.makeText(MainActivity.this, "onAdLoaded", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onAdLoaded: ");
                if (stateVariables.getFirstVisibleItemPosition() > 0) {
                    newsBannerAdView.setVisibility(View.VISIBLE);
                } else {
                    newsBannerAdView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError i) {
                super.onAdFailedToLoad(i);
//                Toast.makeText(MainActivity.this, "onAdFailedToLoad" + i, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onAdFailedToLoad: " + i);
//                newsBannerAdView.setVisibility(View.GONE);
            }

//            @Override
//            public void onAdLeftApplication() {
//                super.onAdLeftApplication();
//            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
            }
        });


        //Handle the logo click
//        ImageView imgvLogo = findViewById(R.id.imgvLogo);
//        imgvLogo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this, "Coming up...", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Spinner
        final Spinner spinnerCountryCode = findViewById(R.id.spinnerCountryCode);
        final ArrayAdapter<CharSequence> countryCodeAdapter;
        countryCodeAdapter = ArrayAdapter.createFromResource(
                MainActivity.this,
                R.array.country_code_array,
                R.layout.activity_main);

        //Retrieve the Shared Preferences
        //Get Country Code
        SharedPreferences retrievedSharedPreferences = getSharedPreferences(SHARED_PREFERENCE_FILE_NAME,
                MODE_PRIVATE);

        if (retrievedSharedPreferences.contains(PREFERENCE_KEY_SELECTED_COUNTRY)) {

            strSelectedCountry = retrievedSharedPreferences.getString(PREFERENCE_KEY_SELECTED_COUNTRY,
                    PREFERENCE_KEY_DEFAULT_COUNTRY_VALUE);

        } else {
//            Toast.makeText(this, "Unable to retrieve current country setting!", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onCreate:Unable to retrieve current country setting!");
            strSelectedCountry = PREFERENCE_KEY_DEFAULT_COUNTRY_VALUE;
        }


        int spinnerPosition = countryCodeAdapter.getPosition(strSelectedCountry);
        spinnerCountryCode.setSelection(spinnerPosition);

        spinnerCountryCode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("TAG", "onItemSelected: position " + position + "; id: " + id);

                strSelectedCountry = countryCodeAdapter.getItem(position).toString();
                Log.e("TAG", "onItemSelected: country: " + strSelectedCountry);
                getCountryArticlesFromApi();
                articleAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "Please Refresh for latest News!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("TAG", "onNothingSelected:");
            }
        });

        //Get the comment Id for this user
        if (retrievedSharedPreferences.contains(PREFERENCE_KEY_COMMENTID)) {

            intCommentId = retrievedSharedPreferences.getInt(PREFERENCE_KEY_COMMENTID,
                    PREFERENCE_KEY_COMMENTID_VALUE_DEFAULT);

        } else {

//            Toast.makeText(this, "Unable to retrieve current country setting!", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onCreate: Unable to retrieve current country setting! ");
            intCommentId = PREFERENCE_KEY_COMMENTID_VALUE_DEFAULT;

        }

//        //Show the no. of chat users for the focussed article & take the user to the chat activity
//        ibtnGotoChat = findViewById(R.id.ibtnGotoChat);
//        ibtnGotoChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, NCIActivity.class));
//            }
//        });
        articleAdapter = new ArticleAdapter(MainActivity.this,
                retrievedArticleListToAdapter);
        getCountryArticlesFromApi();
//        articleAdapter.notifyDataSetChanged();


///////////////////////////////////////////////////////////////
//        /*Create handle for the RetrofitInstance interface*/
//        ArticleService2 service = ArticleRepository2.getRetrofitInstance().create(
//                ArticleService2.class);
//
//        Call<Result> call;
//
//        switch (strSelectedCountry) {
//            case "ar": call = service.getAllArArticlesFromApi();
//            case "au": call = service.getAllAuArticlesFromApi();
//            case "at": call = service.getAllAtArticlesFromApi();
//            case "be": call = service.getAllBeArticlesFromApi();
//            case "br": call = service.getAllBrArticlesFromApi();
//            case "bg": call = service.getAllBgArticlesFromApi();
//            case "ca": call = service.getAllCaArticlesFromApi();
//            case "cn": call = service.getAllCnArticlesFromApi();
//            case "co": call = service.getAllCoArticlesFromApi();
//            case "cu": call = service.getAllCuArticlesFromApi();
//            case "cz": call = service.getAllCzArticlesFromApi();
//            case "eg": call = service.getAllEgArticlesFromApi();
//
//
//
//            default:call = service.getAllCnArticlesFromApi();
//
//
//        }
//
//        call.enqueue(new Callback<Result>() {
//            @Override
//            public void onResponse(Call<Result> call, Response<Result> response) {
//                long id = 0;
//                retrievedArticleList.clear();
//                retrievedArticleList.addAll(response.body().getArticles());
//                articleAdapter.notifyDataSetChanged();
//                Log.e("TAG", "onResponse: " + retrievedArticleList.size() );
//                for (Article article : retrievedArticleList){
//                    try {
//                        if (article.getAuthor() == null){
//                            article.setAuthor(" ");
//                        }
//                        id = articleDAO.insertArticle(article);
//                    } catch (Exception e ){
//                        Log.e("TAG","Error inserting in row: " + e.getMessage());
//                    }
//                    Log.e("TAG", "onResponse: " + id + " " + article.getId());
//                }
////                articleDAO.insertArticles(retrievedArticleList);
//            }
//
//            @Override
//            public void onFailure(Call<Result> call, Throwable t) {
//                Log.e("TAG", "onFailure" + t.getMessage() );
//                Toast.makeText(MainActivity.this, "Error retrieving local data...Please try later!", Toast.LENGTH_SHORT).show();
//            }
//        });

////////////////////////////////////////////////////////////////
//        getCountryArticlesFromApi();
        rvNewsList = findViewById(R.id.rvNewsList);
//        final RecyclerView.LayoutManager newsLayoutManager;
        newsLayoutManager = new LinearLayoutManager(MainActivity.this);
//        newsLayoutManager.setStackFromEnd(true);
        rvNewsList.setLayoutManager(newsLayoutManager);
        rvNewsList.setAdapter(articleAdapter);

        /**********not used****/
        NCIAndroidViewModel nciAndroidViewModel = ViewModelProviders.of(this)
                .get(NCIAndroidViewModel.class);
        /**********************/
////        TODO: 20180919, 23:53
//        articleDAO.getCountryArticlesOrderByCommentedLatest(strSelectedCountry).observe(this, new Observer<List<Article>>() {
//            @Override
//            public void onChanged(@Nullable List<Article> articleList) {
//                Log.e("TAG", "getCountryArticlesOrderByCommentedLatest().observe : onChanged: " + articleList.size());
//                currentCountryArticleListInDB.clear();
//                currentCountryArticleListInDB.addAll(articleList);
////                retrievedArticleListToAdapter.clear();
////                retrievedArticleListToAdapter.addAll(articleList);
////                articleAdapter.notifyDataSetChanged(); // TODO: commenting it to stop access to db
//            }
//        });

        int intFirstVisibleItemPosition = ((LinearLayoutManager) newsLayoutManager).findFirstVisibleItemPosition();
        stateVariables.setFirstVisibleItemPosition(intFirstVisibleItemPosition);

        Log.e("TAG", "intFirstVisibleItemPosition: " + intFirstVisibleItemPosition);
        Log.e("TAG", "stateVariables.getFirstVisibleItemPosition(): " + stateVariables.getFirstVisibleItemPosition());

        final LinearLayout layoutTopBar = findViewById(R.id.layout_top_bar);
        layoutTopBar.setVisibility(View.VISIBLE);
        toolbarNews.setVisibility(View.VISIBLE);
        ImageView imgViewTopBarLeft = findViewById(R.id.imgvTopBarLeft);
//        ivChatUserImage=null; //simulating a crash for crashlytics
        Picasso.get()
                .load(stateVariables.getFirebaseUserPhotoUrl())
                .transform(new Transformation() {
                    @Override
                    public Bitmap transform(Bitmap source) {
                        int size = Math.min(source.getWidth(), source.getHeight());

                        int x = (source.getWidth() - size) / 2;
                        int y = (source.getHeight() - size) / 2;

                        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
                        if (squaredBitmap != source) {
                            source.recycle();
                        }

                        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

                        Canvas canvas = new Canvas(bitmap);
                        Paint paint = new Paint();
                        BitmapShader shader = new BitmapShader(squaredBitmap,
                                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
                        paint.setShader(shader);
                        paint.setAntiAlias(true);

                        float r = size / 2f;
                        canvas.drawCircle(r, r, r, paint);

                        squaredBitmap.recycle();
                        return bitmap;
                    }

                    @Override
                    public String key() {
                        return "circle";
                    }
                })
                .into(imgViewTopBarLeft);

        imgViewTopBarLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "" + stateVariables.getFirebaseUserEmail(), Toast.LENGTH_SHORT).show();
            }
        });

//        int baseVersion = 0;
//        if (Build.VERSION_CODES.M > 0) {
//            baseVersion = Build.VERSION_CODES.M;
//        } else {
//            baseVersion = 23;
//        }
//        pbNewsList.setVisibility(View.GONE);


        rvNewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if ((((LinearLayoutManager) newsLayoutManager).findFirstCompletelyVisibleItemPosition()) == 0) {
                    toolbarNews.setVisibility(View.VISIBLE);
                } else {
                    toolbarNews.setVisibility(View.GONE);
                }
                layoutTopBar.setVisibility(View.VISIBLE);
                newsBannerAdView.setVisibility(View.VISIBLE);

                //Load intersttial ad at the end of the rv scrolling
                if ((((LinearLayoutManager) newsLayoutManager).findFirstVisibleItemPosition())
                        == (retrievedArticleListToAdapter.size() - 1)
                        &&
                        ((((LinearLayoutManager) newsLayoutManager).findFirstVisibleItemPosition())
                                > 18)) {
                    if (chatInterstitialAdView == null) {
                        Log.e("TAG", "onCreate: Interstitial Ad not loaded. ");
                    } else {
//                                Toast.makeText(MainActivity.this, "showing interstitial: " + intCurrentVisibleItemPosition + "; " + (retrievedArticleListToAdapter.size()-1), Toast.LENGTH_SHORT).show();
                        chatInterstitialAdView.show(MainActivity.this);
                    }
                }

                if ((((LinearLayoutManager) newsLayoutManager).findFirstVisibleItemPosition())
                        == (retrievedArticleListToAdapter.size() / 2)
                        &&
                        ((((LinearLayoutManager) newsLayoutManager).findFirstVisibleItemPosition())
                                > 4)) {
                    if (chatInterstitialAdView != null) {
                        Toast.makeText(MainActivity.this, "showing interstitial: " + "4" + "; " + (retrievedArticleListToAdapter.size() / 2), Toast.LENGTH_SHORT).show();
                        chatInterstitialAdView.show(MainActivity.this);
                    } else {
                        Log.e("TAG", "onCreate: Interstitial Ad not loaded. ");
                    }
                }

            }
        });

//        rvNewsList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
////                Toast.makeText(MainActivity.this, "addOnScrollListener.onScrollStateChanged", Toast.LENGTH_SHORT).show();
//                //                        intBackPressed = 1;
//                Integer intCurrentVisibleItemPosition = ((LinearLayoutManager) newsLayoutManager)
//                        .findFirstVisibleItemPosition();
////                stateVariables.setFirstVisibleItemPosition(intCurrentVisibleItemPosition-1);
//                Log.e("TAG", "onScrollChange: stateVariables.getFirstVisibleItemPosition(" + strSelectedCountry + ") " + stateVariables.getFirstVisibleItemPosition());
//                Log.e("TAG", "onScrollChange: intCurrentVisibleItemPosition" + ":" + strSelectedCountry + ": " + intCurrentVisibleItemPosition);
//                if ((intCurrentVisibleItemPosition - stateVariables.getFirstVisibleItemPosition()) >= 2){
//                    Log.e("TAG", "scroll down ");
//                    layoutTopBar.setVisibility(View.VISIBLE);
//                    newsBannerAdView.setVisibility(View.VISIBLE);
//                    toolbarNews.setVisibility(View.VISIBLE);
//                    stateVariables.setFirstVisibleItemPosition(stateVariables.getFirstVisibleItemPosition()+1);
//                }
//                else if ((intCurrentVisibleItemPosition - stateVariables.getFirstVisibleItemPosition()) >1) {
//                    Log.e("TAG", "scroll down ");
//                    layoutTopBar.setVisibility(View.VISIBLE);
//                    newsBannerAdView.setVisibility(View.VISIBLE);
//                    toolbarNews.setVisibility(View.VISIBLE);
//                    stateVariables.setFirstVisibleItemPosition(stateVariables.getFirstVisibleItemPosition()+1);
//                }
//                else if (intCurrentVisibleItemPosition == stateVariables.getFirstVisibleItemPosition()) {
//                    Log.e("TAG", "onScrollChange: scroll level");
//                    layoutTopBar.setVisibility(View.VISIBLE);
//                    newsBannerAdView.setVisibility(View.VISIBLE);
////                            if (intCurrentVisibleItemPosition == stateVariables.getFirstVisibleItemPosition()) {
//                    toolbarNews.setVisibility(View.VISIBLE);
////                            }
//                }
//                else if (intCurrentVisibleItemPosition < stateVariables.getFirstVisibleItemPosition()) {
//                    Log.e("TAG", "onScrollChange: scroll up");
//                    layoutTopBar.setVisibility(View.VISIBLE);
//                    newsBannerAdView.setVisibility(View.VISIBLE);
////                            if (intCurrentVisibleItemPosition < stateVariables.getFirstVisibleItemPosition()) {
//                    toolbarNews.setVisibility(View.VISIBLE);
////                            }
//                    stateVariables.setFirstVisibleItemPosition(stateVariables.getFirstVisibleItemPosition()-1);
//                }
//
//                //Load intersttial ad at the end of the rv scrolling
//                if (intCurrentVisibleItemPosition == (retrievedArticleListToAdapter.size()-1) && (intCurrentVisibleItemPosition >18)) {
//                    if (chatInterstitialAdView.isLoaded()) {
////                                Toast.makeText(MainActivity.this, "showing interstitial: " + intCurrentVisibleItemPosition + "; " + (retrievedArticleListToAdapter.size()-1), Toast.LENGTH_SHORT).show();
//                        chatInterstitialAdView.show();
//                    } else {
//                        Log.e("TAG", "onCreate: Interstitial Ad not loaded. ");
//                    }
//                }
//
//                if (intCurrentVisibleItemPosition == (retrievedArticleListToAdapter.size()/2) && (intCurrentVisibleItemPosition >18)){
//                    if (chatInterstitialAdView.isLoaded()) {
////                                Toast.makeText(MainActivity.this, "showing interstitial: " + intCurrentVisibleItemPosition + "; " + (retrievedArticleListToAdapter.size()/2), Toast.LENGTH_SHORT).show();
//                        chatInterstitialAdView.show();
//                    } else {
//                        Log.e("TAG", "onCreate: Interstitial Ad not loaded. ");
//                    }
//                }
//
//                Log.e("TAG", "stateVariables.getFirstVisibleItemPosition(): " + stateVariables.getFirstVisibleItemPosition());
//
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
////                Toast.makeText(MainActivity.this, "addOnScrollListener.onScrolled", Toast.LENGTH_SHORT).show();
//            }
//        });

//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                rvNewsList.setOnScrollChangeListener(new View.OnScrollChangeListener() {
//                    @Override
//                    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////                        intBackPressed = 1;
//                        Integer intCurrentVisibleItemPosition = ((LinearLayoutManager) newsLayoutManager)
//                                .findFirstVisibleItemPosition();
//                        Log.e("TAG", "onScrollChange: stateVariables.getFirstVisibleItemPosition(" + strSelectedCountry + ") " + stateVariables.getFirstVisibleItemPosition());
//                        Log.e("TAG", "onScrollChange: intCurrentVisibleItemPosition" + ":" + strSelectedCountry + ": " + intCurrentVisibleItemPosition);
//                        if (intCurrentVisibleItemPosition - stateVariables.getFirstVisibleItemPosition() >= 2){
//                            Log.e("TAG", "scroll down ");
//                            layoutTopBar.setVisibility(View.VISIBLE);
//                            newsBannerAdView.setVisibility(View.VISIBLE);
//                            toolbarNews.setVisibility(View.GONE);
//                            stateVariables.setFirstVisibleItemPosition(stateVariables.getFirstVisibleItemPosition()+1);
//                        }
//                        else if (intCurrentVisibleItemPosition - stateVariables.getFirstVisibleItemPosition() >1) {
//                            Log.e("TAG", "scroll down ");
//                            layoutTopBar.setVisibility(View.VISIBLE);
//                            newsBannerAdView.setVisibility(View.VISIBLE);
//                            toolbarNews.setVisibility(View.VISIBLE);
//                            stateVariables.setFirstVisibleItemPosition(stateVariables.getFirstVisibleItemPosition()+1);
//                        }
//                        else if (intCurrentVisibleItemPosition == stateVariables.getFirstVisibleItemPosition()) {
//                            Log.e("TAG", "onScrollChange: scroll level");
//                            layoutTopBar.setVisibility(View.VISIBLE);
//                            newsBannerAdView.setVisibility(View.VISIBLE);
////                            if (intCurrentVisibleItemPosition == stateVariables.getFirstVisibleItemPosition()) {
//                                toolbarNews.setVisibility(View.VISIBLE);
////                            }
//                        }
//                        else if (intCurrentVisibleItemPosition < stateVariables.getFirstVisibleItemPosition()) {
//                            Log.e("TAG", "onScrollChange: scroll up");
//                            layoutTopBar.setVisibility(View.VISIBLE);
//                            newsBannerAdView.setVisibility(View.VISIBLE);
////                            if (intCurrentVisibleItemPosition < stateVariables.getFirstVisibleItemPosition()) {
//                                toolbarNews.setVisibility(View.VISIBLE);
////                            }
//                            stateVariables.setFirstVisibleItemPosition(stateVariables.getFirstVisibleItemPosition()-1);
//                        }
//
//                        //Load intersttial ad at the end of the rv scrolling
//                        if (intCurrentVisibleItemPosition == (retrievedArticleListToAdapter.size()-1) && (intCurrentVisibleItemPosition >18)) {
//                            if (chatInterstitialAdView.isLoaded()) {
////                                Toast.makeText(MainActivity.this, "showing interstitial: " + intCurrentVisibleItemPosition + "; " + (retrievedArticleListToAdapter.size()-1), Toast.LENGTH_SHORT).show();
//                                chatInterstitialAdView.show();
//                            } else {
//                                Log.e("TAG", "onCreate: Interstitial Ad not loaded. ");
//                            }
//                        }
//
//                        if (intCurrentVisibleItemPosition == (retrievedArticleListToAdapter.size()/2) && (intCurrentVisibleItemPosition >18)){
//                            if (chatInterstitialAdView.isLoaded()) {
////                                Toast.makeText(MainActivity.this, "showing interstitial: " + intCurrentVisibleItemPosition + "; " + (retrievedArticleListToAdapter.size()/2), Toast.LENGTH_SHORT).show();
//                                chatInterstitialAdView.show();
//                            } else {
//                                Log.e("TAG", "onCreate: Interstitial Ad not loaded. ");
//                            }
//                        }
//
//                        Log.e("TAG", "stateVariables.getFirstVisibleItemPosition(): " + stateVariables.getFirstVisibleItemPosition());
//                    }
//                });
//            } else {
//                Log.e("TAG", "onCreate: version less than M!");
//                layoutTopBar.setVisibility(View.VISIBLE);
//                newsBannerAdView.setVisibility(View.VISIBLE);
//                toolbarNews.setVisibility(View.VISIBLE);
////                stateVariables.setFirstVisibleItemPosition(stateVariables.getFirstVisibleItemPosition()+1);
//                if (chatInterstitialAdView.isLoaded()) {
////                                Toast.makeText(MainActivity.this, "showing interstitial: " + intCurrentVisibleItemPosition + "; " + (retrievedArticleListToAdapter.size()-1), Toast.LENGTH_SHORT).show();
//                    chatInterstitialAdView.show();
//                } else {
//                    Log.e("TAG", "onCreate: Interstitial Ad not loaded. ");
//                }
//            }
//        } catch (Exception e) {
//            Log.e("TAG", "onCreate: " + e.getMessage());
//        }

//        //****Comment this. Shifting this to NCI Activity****//
//        //Send comment to Firebase using FAB
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase
//                .getInstance();
//
//        final DatabaseReference rootRef = firebaseDatabase
//                .getReference();
//
//        final DatabaseReference childRef = rootRef.child("comments");
//        childRef.addChildEventListener(this);
//
//        // First check there IS something written!!
//        fabSendComment = (FloatingActionButton) findViewById(R.id.fabSendComment);
//        fabSendComment.setEnabled(false);
//        fabSendComment.hide();
//        etComment = findViewById(R.id.etComment);
//        etComment.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.toString().trim().length() > 0) {
//                    fabSendComment.setEnabled(true);
//                    fabSendComment.show();
//                } else {
//                    fabSendComment.setEnabled(false);
//                    fabSendComment.hide();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        fabSendComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "FAB!!", Toast.LENGTH_SHORT).show();
//
//                EditText etComment = findViewById(R.id.etComment);
//                String strComment = etComment.getText().toString();
//                Date dateToday = new Date();
//                String strDate = dateToday.toString();
//                //TODO: need to extract this from strDate
//                Long lTime = dateToday.getTime();
//                String strTime = "12:00:00";
//                String strTimeZone = "IST";
//
//                intCommentId++;
//                String strUserId = firebaseUser.getDisplayName();
//                String strUserEmail = firebaseUser.getEmail();
//                StateVariables stateVariables = StateVariables.getINSTANCE();
//                String strArticleId = stateVariables.getStrSelectedAriticleId();
//                String strUserName = strUserId;
//                String strPhotoUrl = "https://xyz.com/pic1.jpg";
//
//                Comment myComment = new Comment(
//                        intCommentId,
//                        strUserId,
//                        strUserName,
//                        strUserEmail,
//                        strComment,
//                        strArticleId,
//                        strDate,
//                        strTime,
//                        strTimeZone,
//                        strPhotoUrl);
//
//                Task<Void> asyncTask = childRef.push().setValue(myComment);
//                etComment.setText("");
//
//                asyncTask.addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.e("TAG","Success - writing to remote database!");
//                    }
//                }).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Log.e("TAG","Completed - writing to remote database!");
//                    }
//                });
//            }
//        });

        SearchView searchViewArticles = (SearchView) findViewById(R.id.searchViewArticles);
        OnQueryTextListener onQueryTextListener = null;
        searchViewArticles.setOnQueryTextListener(this);

//        ArrayAdapter<CharSequence> countryCodeAdapter = ArrayAdapter.createFromResource(
//                this,
//                R.array.country_code_array,
//                R.layout.activity_main);

//        countryCodeAdapter.setDropDownViewResource(R.layout.activity_main);
//        spinnerCountryCode.setAdapter(countryCodeAdapter);

    }

    @Override
    protected void onStop() {
        super.onStop();

//        sensorManager.unregisterListener(sensorEventListener); UNCOMMENT 5DEC
//        mSpeechRecognizer.destroy();
        SharedPreferences mySharedPreferences = getSharedPreferences(
                SHARED_PREFERENCE_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefsEditor = mySharedPreferences.edit();

        sharedPrefsEditor.putString(PREFERENCE_KEY_SELECTED_COUNTRY, strSelectedCountry);
        sharedPrefsEditor.putInt(PREFERENCE_KEY_COMMENTID, intCommentId);
        sharedPrefsEditor.apply();

        if (sharedPrefsEditor.commit()) {
//            Toast.makeText(this, "Data saved!", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onStop: Data Saved to shared prefs file!");
        } else {
//            Toast.makeText(this, "Unable to save data", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onStop: Data not saved to shared prefs file!");
        }

        //Double checking, that, the entry has been made
        SharedPreferences mySharedPreferencesRetrieve = getSharedPreferences(
                SHARED_PREFERENCE_FILE_NAME, MODE_PRIVATE);

        if (mySharedPreferencesRetrieve.contains("COUNTRY_CODE")) {
//            Toast.makeText(this, "Current country setting stored locally!", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onStop:Current country setting stored locally! ");
        } else {
//            Toast.makeText(this, "Unable to check if data is stored correctly!", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "onStop:Unable to check if Current country setting stored locally! ");
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mSpeechRecognizer.destroy(); UNCOMMENT 5DEC
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.e("TAG", "onPrepareOptionsMenu: " + stateVariables.getFirebaseUserEmail());
//        Toast.makeText(this, "onPrepareOptionsMenu", Toast.LENGTH_SHORT).show();
//        MenuItem mnuActionUser = menu.findItem(R.id.action_loggedInAs);
        if (stateVariables.getFirebaseUserEmail() == null) {
//          stateVariables.setFirebaseUserEmail(firebaseUser.getEmail());
            Log.e("TAG", "onPrepareOptionsMenu: User email is null.");
        } else {
            Log.e("TAG", "onPrepareOptionsMenu: User email:" + stateVariables.getFirebaseUserEmail());
        }
//        mnuActionUser.setTitle("User: " + stateVariables.getFirebaseUserEmail());
        return true;
    }

    @Override
    public void openOptionsMenu() {
        super.openOptionsMenu();
        Log.e("TAG", "openOptionsMenu: ");
//        Toast.makeText(this, "openOptionsMenu", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu_main, menu);

        Log.e("TAG", "onCreateOptionsMenu: ");
//        Toast.makeText(this, "onCreateOptionsMenu", Toast.LENGTH_SHORT).show();

//        MenuItem mnuActionUser = menu.findItem(R.id.action_loggedInAs);
        if (stateVariables.getFirebaseUserEmail() == null) {
//          stateVariables.setFirebaseUserEmail(firebaseUser.getEmail());
            Log.e("TAG", "onCreateOptionsMenu: User email is null.");
        }
//        mnuActionUser.setTitle("User: " + stateVariables.getFirebaseUserEmail());


        // Get the current version
        String versionName = "";
        try {
            try {
                PackageInfo nciPackageInfo = this.getPackageManager()
                        .getPackageInfo(getPackageName(), 0);
                versionName = nciPackageInfo.versionName;
//                Toast.makeText(this, versionName, Toast.LENGTH_SHORT).show();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.e("TAG", "onCreate: " + e.getMessage());
        }
        MenuItem mnuActionVersion = menu.findItem(R.id.action_show_version);
        mnuActionVersion.setTitle("Version: " + versionName);


//        Log.e("TAG", "onCreateOptionsMenu: "+menu.getItem(2));
//        try {
//            SubMenu loginEmailId = menu.addSubMenu(firebaseUser.getEmail());
//        } catch (Exception e){
//            Log.e("TAG", "onCreateOptionsMenu: "+ e.getMessage());
//        }
        return true;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Log.e("TAG", "Child added.");
    }

    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Log.e("TAG", "Child changed.");
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Log.e("TAG", "Child removed.");
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    //    SearchView searchViewArticles = findViewById(R.id.searchViewArticles);
//
    @Override
    public boolean onQueryTextSubmit(String query) {

        Log.e("TAG", "onQueryTextSubmit: ");

        return false;
    }

    //Search functionality
    @Override
    public boolean onQueryTextChange(String newText) {

        Log.e("TAG", "onQueryTextChange: ");

        String searchedText = newText.toLowerCase();
        List<Article> newArticleList = new ArrayList<>();

        for (Article article : currentCountryArticleListInDB) {

            try {
                if (article.getDescription().toLowerCase().contains(searchedText) ||
                        article.getTitle().toLowerCase().contains(searchedText))
                //                    ||
                //                    article.source.getName().toLowerCase().contains(searchedText))
                {

                    newArticleList.add(article);
                }
            } catch (Exception e) {
                Log.e("TAG", "onQueryTextChange: " + e.getMessage());
            }
        }

        articleAdapter.updateList(newArticleList);
        if (newArticleList.size() == 0) {
            Toast.makeText(this, "No articles found. Please modify searched text.", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String strMenuTitle = item.getTitle().toString();
        int intMenuItemId = item.getOrder();
//        item.setTitle(strMenuTitle +": "+stateVariables.getFirebaseUserEmail());


        switch (intMenuItemId) {
            case 1: //"Refresh":
//                Toast.makeText(this, strMenuTitle, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onOptionsItemSelected: " + intMenuItemId + ".)" + strMenuTitle);
                getCountryArticlesFromApi();
                articleAdapter.notifyDataSetChanged();
                Toast.makeText(this, "You are up-to-date!", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onOptionsItemSelected: You are up-to-date!");
                break;
            case 2: //"Share":
                Log.e("TAG", "onOptionsItemSelected: " + intMenuItemId + ".)" + strMenuTitle);
//                Intent intent = newIntentBuilder(getString(R.string.invitation_title))
//                        .setMessage(getString(R.string.invitation_message))
//                        .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
//                        .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
//                        .setCallToActionText(getString(R.string.invitation_cta))
//                        .build();
//                startActivityForResult(intent, REQUEST_INVITE);

            case 3: //"Connect":
//                Toast.makeText(this, strMenuTitle, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onOptionsItemSelected: " + intMenuItemId + ".)" + strMenuTitle);
//                takeScreenshot();
                break;
            case 4: //"Settings":
//                Toast.makeText(this, strMenuTitle, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onOptionsItemSelected: " + intMenuItemId + ".)" + strMenuTitle);
                break;
            case 5: //"Clear":
//                Toast.makeText(this, strMenuTitle, Toast.LENGTH_SHORT).show();
                Log.e("TAG", "onOptionsItemSelected: " + intMenuItemId + ".)" + strMenuTitle);
                AlertDialog.Builder deleteAlertBuilder = new AlertDialog.Builder(this);
                deleteAlertBuilder.setMessage(R.string.alert_message_delete)
                        .setPositiveButton(R.string.alert_message_delete_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                articleDAO.deleteAllArticles();
                                articleAdapter.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Deleted articles from DB.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(R.string.alert_message_delete_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                Log.e("TAG", "onClick: Not deleting articles from DB.");
                            }
                        });
                AlertDialog deleteAlertDialog = deleteAlertBuilder.create();
                deleteAlertDialog.show();
//                articleAdapter.notifyDataSetChanged();
//                firebaseAuth.signOut();
//                Log.e("TAG", "onOptionsItemSelected: " + strMenuTitle + ": Signing out!");
                break;
            case 6: //"User":
                Log.e("TAG", "onOptionsItemSelected: " + intMenuItemId + ".)" + strMenuTitle + " : " + stateVariables.getFirebaseUserEmail());

                item.setTitle("User: " + stateVariables.getFirebaseUserEmail());
                Toast.makeText(this, "" + stateVariables.getFirebaseUserEmail(), Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
            Log.e("TAG", "takeScreenshot: " + e.getMessage());
        }
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void getCountryArticlesFromApi() {
        //Logic:
        //**1.  Get Articles from API, say: API(20)

        //**2. Get Articles from DB, say: DB(40)

        //**3. Compare API(20) with DB(40) & find the new ones in API(20), say NEW(10)

        //**4. Now check if these NEW(10) have any comments in FB, say FB(100), if the do add the comments to NEW(10) & add these to DB(40)

        //**5. Finally, get a fresh list from the DB ie, DB(50)


        nciFirebaseDatabase = FirebaseDatabase.getInstance();
        nciFirebaseDatabaseReference = nciFirebaseDatabase.getReference(FIREBASE_TABLE_COMMENTS);
        retrievedArticleListToAdapter.clear();
        NCIRoomDatabase nciRoomDatabase = NCIRoomDatabase.getDatabase(getApplicationContext());
        articleDAO = nciRoomDatabase.getArticleDAO();

        //**1.  Get Articles from API, say: API(20)

//        Toast.makeText(this, "getCountryArticlesFromApi with country: " + strSelectedCountry, Toast.LENGTH_SHORT).show();
///////////////////////////////////////////////////////////////
        /*Create handle for the RetrofitInstance interface*/
//        progressBarLayout.setVisibility(View.VISIBLE);
//        Log.e("TAG", "getCountryArticlesFromApi: Setting progressBarLayout visibility to VISIBLE" );

        //get the comment count from Firebase
//        nciFirebaseDatabaseReference.child("indexhtml")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        int size = (int) dataSnapshot.getChildrenCount();
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//
//
        Log.e("TAG", "getCountryArticlesFromApi:Country: " + strSelectedCountry);

        ArticleService2 service = ArticleRepository2.getRetrofitInstance().create(ArticleService2.class);

        Call<Result> call = null;

        switch (strSelectedCountry) {
//            https://newsapi.org/sources
            case "AR":
                call = service.getAllArArticlesFromApi();
                break;
            case "AU":
                call = service.getAllAuArticlesFromApi();
                break;
            case "AT":
                call = service.getAllAtArticlesFromApi();
                break;
            case "BE":
                call = service.getAllBeArticlesFromApi();
                break;
            case "BR":
                call = service.getAllBrArticlesFromApi();
                break;
            case "BG":
                call = service.getAllBgArticlesFromApi();
                break;
            case "CA":
                call = service.getAllCaArticlesFromApi();
                break;
            case "CN":
                call = service.getAllCnArticlesFromApi();
                break;
            case "CO":
                call = service.getAllCoArticlesFromApi();
                break;
            case "CU":
                call = service.getAllCuArticlesFromApi();
                break;
            case "CZ":
                call = service.getAllCzArticlesFromApi();
                break;
            case "EG":
                call = service.getAllEgArticlesFromApi();
                break;
            case "FR":
                call = service.getAllFrArticlesFromApi();
                break;
            case "DE":
                call = service.getAllDeArticlesFromApi();
                break;
            case "GR":
                call = service.getAllGrArticlesFromApi();
                break;
            case "HK":
                call = service.getAllHkArticlesFromApi();
                break;
            case "HU":
                call = service.getAllHuArticlesFromApi();
                break;
            case "IN":
                call = service.getAllInArticlesFromApi();
                break;
            case "ID":
                call = service.getAllIdArticlesFromApi();
                break;
            case "IE":
                call = service.getAllIeArticlesFromApi();
                break;
            case "IL":
                call = service.getAllIlArticlesFromApi();
                break;
            case "IT":
                call = service.getAllItArticlesFromApi();
                break;
            case "LV":
                call = service.getAllLvArticlesFromApi();
                break;
            case "LT":
                call = service.getAllLtArticlesFromApi();
                break;
            case "MY":
                call = service.getAllMyArticlesFromApi();
                break;
            case "MX":
                call = service.getAllMxArticlesFromApi();
                break;
            case "MA":
                call = service.getAllMaArticlesFromApi();
                break;
            case "NL":
                call = service.getAllNlArticlesFromApi();
                break;
            case "NZ":
                call = service.getAllNzArticlesFromApi();
                break;
            case "NG":
                call = service.getAllNgArticlesFromApi();
                break;
            case "NO":
                call = service.getAllNoArticlesFromApi();
                break;
            case "PH":
                call = service.getAllPhArticlesFromApi();
                break;
            case "PL":
                call = service.getAllPlArticlesFromApi();
                break;
            case "PT":
                call = service.getAllPtArticlesFromApi();
                break;
            case "RO":
                call = service.getAllRoArticlesFromApi();
                break;
            case "RU":
                call = service.getAllRuArticlesFromApi();
                break;
            case "SA":
                call = service.getAllSaArticlesFromApi();
                break;
            case "RS":
                call = service.getAllRsArticlesFromApi();
                break;
            case "SG":
                call = service.getAllSgArticlesFromApi();
                break;
            case "SK":
                call = service.getAllSkArticlesFromApi();
                break;
            case "SI":
                call = service.getAllSiArticlesFromApi();
                break;
            case "ZA":
                call = service.getAllZaArticlesFromApi();
                break;
            case "KR":
                call = service.getAllKrArticlesFromApi();
                break;
            case "SE":
                call = service.getAllSeArticlesFromApi();
                break;
            case "CH":
                call = service.getAllChArticlesFromApi();
                break;
            case "TW":
                call = service.getAllTwArticlesFromApi();
                break;
            case "TH":
                call = service.getAllThArticlesFromApi();
                break;
            case "TR":
                call = service.getAllTrArticlesFromApi();
                break;
            case "AE":
                call = service.getAllAeArticlesFromApi();
                break;
            case "UA":
                call = service.getAllUaArticlesFromApi();
                break;
            case "GB":
                call = service.getAllGbArticlesFromApi();
                break;
            case "US":
                call = service.getAllUsArticlesFromApi();
                break;
            case "VE":
                call = service.getAllVeArticlesFromApi();
                break;
//            default:
//                call = service.getAllUsArticlesFromApi();
//                break;
        }

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
//                long id = 0;
//                retrievedArticleListFromApi.clear(); //TODO 20180819-22:37
                retrievedArticleListFromApi.clear();
                retrievedArticleListFromApi.addAll(response.body().getArticles());
                Log.e("TAG", "onResponse: Articles received from api: " + retrievedArticleListFromApi.size());

                //**2. Get Articles from DB, say: DB(40)
                articleDAO.getCountryArticlesOrderByCommentedLatest(strSelectedCountry).observe(MainActivity.this, new Observer<List<Article>>() {
                    @Override
                    public void onChanged(@Nullable List<Article> articleList) {
                        Log.e("TAG", "getCountryArticlesOrderByCommentedLatest().observe : onChanged: " + articleList.size());
                        if (articleList.size() > 0) {
                            currentCountryArticleListInDB.clear();
                            currentCountryArticleListInDB.addAll(articleList);
//                            retrievedArticleListToAdapter.clear();
//                            retrievedArticleListToAdapter.addAll(articleList); //TODO : 20180819:10:51
                            articleAdapter.updateList(articleList);
                            articleAdapter.notifyDataSetChanged();
                        } else {
//                    Toast.makeText(MainActivity.this, retrievedArticleListFromApi.size(), Toast.LENGTH_SHORT).show();

                        }
//                articleAdapter.notifyDataSetChanged(); // TODO: BD commenting it to stop access to db
                    }
                });

                //**3. Compare API(20) with DB(40) & find the new ones in API(20), say NEW(10)
                boolean isPresent = false;
//            Log.e("TAG", "onResponse: " + retrievedArticleListFromApi.size() );
                int newArticleCount = 0;
                int duplicateCount = 0;
//                List<Article> newArticleList = new ArrayList<>();
                for (final Article article : retrievedArticleListFromApi) {
                    isPresent = false;
                    for (final Article article1 : currentCountryArticleListInDB) {
                        if (article.getTitle().equals(article1.getTitle())) {
//                            Toast.makeText(MainActivity.this, article.getTitle() + " : EXISTS", Toast.LENGTH_SHORT).show();
                            isPresent = true;
                            duplicateCount++;
//                            try {
//                                if (stateVariables.getStrSelectedAriticleId().equals(article1.getId())) {
//                                    Toast.makeText(MainActivity.this, "baba"+article1.getTitle(), Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (Exception e) {
//                                Log.e("TAG", "onResponse: " + e.getMessage());
//                            }
                        }
                    }
                    if (!isPresent) {
                        newArticleCount++;
                        try {
                            if (article.getAuthor() == null) {
                                article.setAuthor(" ");
                            }
                            //set the queried country
                            article.setCountryCode(strSelectedCountry);

                            //**4. Now check if these NEW(10) have any comments in FB, say FB(100),
                            // if they do add the comments to NEW(10). Add these NEW(10) to DB(40)

                            //get the comment count
                            nciFirebaseDatabaseReference.child(article.getId())
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            int size = (int) dataSnapshot.getChildrenCount();
//                                    Toast.makeText(MainActivity.this, "article: " + article.getId() + ", no. of comments: " + size, Toast.LENGTH_SHORT).show();
                                            Log.e("TAG", "onDataChangeAA: " + article.getId() + ": " + article.getCommentCount());
                                            article.setCommentCount(size);
                                            Log.e("TAG", "onDataChangeAA: " + article.getId() + ": " + article.getCommentCount());
                                            //                                            article.setCommentCount(size);
                                            newArticleList.add(article);

                                            //**4.(cont) add to DB
                                            try {
                                                long id = articleDAO.insertArticle(article);
                                                Log.e("TAG", "onResponse: " + id + " " + article.getId());
                                            } catch (Exception e) {
                                                Log.e("TAG", "onDataChange: Error: " + e.getMessage());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
//                            int checkCC = article.getCommentCount();
//                            article.setCommentCount(0);
                            Log.e("TAG", "onResponse: current Article CommentCount = " + article.getCommentCount());


//                    retrievedArticleListToAdapter.add(article);
//                    articleAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            Log.e("TAG", "Error inserting in row: " + e.getMessage());
                        }

                        if (newArticleList.size() > 0) {
                            articleAdapter.updateList(newArticleList);
                        }
                    }
                    //                        Toast.makeText(MainActivity.this, article.getTitle() + ": " + article.getCommentCount(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Log.e("TAG", "onFailure" + t.getMessage());
//                Toast.makeText(MainActivity.this, "Error retrieving local data...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

//                retrievedArticleListToAdapter.clear();
//                retrievedArticleListToAdapter.addAll(response.body().getArticles()); //TODO 1908
//                articleAdapter.notifyDataSetChanged(); //TODO 1908: commenting this off to not access the db

        stateVariables.setStrSelectedAriticleId("");

        //**5. Finally, get a fresh list from the DB ie, DB(50)
        articleDAO.getCountryArticlesOrderByCommentedLatest(strSelectedCountry).observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articleList) {
                int position = 0;
                Log.e("TAG", "getCountryArticlesOrderByCommentedLatest().observe : onChanged: " + articleList.size());
                if (articleList.size() > 0) {
                    currentCountryArticleListInDB.clear();
                    currentCountryArticleListInDB.addAll(articleList);
                    retrievedArticleListToAdapter.clear();
                    retrievedArticleListToAdapter.addAll(articleList); //TODO : 20180819:10:51
                    for (int i = 0; i < articleList.size(); i++) {
                        if (articleList.get(i).getId().equals(stateVariables.getStrSelectedAriticleId())) {
                            position = i;
//                            Toast.makeText(MainActivity.this, stateVariables.getStrSelectedAriticleId() + ": " + position, Toast.LENGTH_SHORT).show();
                        }
                    }
//                    stateVariables.setStrSelectedAriticleId("");
                    articleAdapter.notifyDataSetChanged();
//                    rvNewsList.scrollToPosition(position);
                } else {
//                    Toast.makeText(MainActivity.this, retrievedArticleListFromApi.size(), Toast.LENGTH_SHORT).show();

                }
//                articleAdapter.notifyDataSetChanged(); // TODO: BD commenting it to stop access to db
            }
        });
    }
//        int newTotalInDB = newArticleCount + currentCountryArticleListInDB.size();
//                Toast.makeText(MainActivity.this, newArticleCount + " new articles added! " + newTotalInDB + " in DB!", Toast.LENGTH_SHORT).show();
//                articleDAO.insertArticles(retrievedArticleList);
//                            nciFirebaseDatabaseReference.child(article.getId())
//                                    .addChildEventListener(new ChildEventListener() {
//                                        @Override
//                                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                                            int size = (int) dataSnapshot.getChildrenCount();
//                                            article.setCommentCount(size);
//
//                                        }
//
//                                        @Override
//                                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                                            int size = (int) dataSnapshot.getChildrenCount();
//                                            article.setCommentCount(size);
//                                        }
//
//                                        @Override
//                                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                                            int size = (int) dataSnapshot.getChildrenCount();
//                                            article.setCommentCount(size);
//
//                                        }
//
//                                        @Override
//                                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                                            int size = (int) dataSnapshot.getChildrenCount();
//                                            article.setCommentCount(size);
//
//                                        }
//
//                                        @Override
//                                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                        }
//                                    });

//                            nciFirebaseDatabaseReference.chi
    //                        getNCIFirebaseCommentCount(article.getId());


//        progressBarLayout.setVisibility(View.GONE);
//        Log.e("TAG", "getCountryArticlesFromApi: Setting progressBarLayout visibility to GONE" );
////////////////////////////////////////////////////////////////

    //    public int getNCIFirebaseCommentCount(final String articleId) {
//        String path = "nci-test/comments/" + articleId;
////        Toast.makeText(this, "getNCIFirebaseCommentCount: " + articleId, Toast.LENGTH_SHORT).show();
//        //Variables
//        FirebaseDatabase nciFirebaseDatabase;
//        DatabaseReference nciFirebaseDatabaseReference;
////        int commentCount;
//
//        //get the comment count
//        nciFirebaseDatabase = FirebaseDatabase.getInstance();
////        nciFirebaseDatabaseReference = nciFirebaseDatabase.getReference(FIREBASE_TABLE_COMMENTS);
//        nciFirebaseDatabaseReference = nciFirebaseDatabase.getReference("nci-test/comments/" + articleId);
//
////        Toast.makeText(this, nciFirebaseDatabaseReference.getKey(), Toast.LENGTH_SHORT).show();
//
//        nciFirebaseDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Log.e("TAG", "MainActivity():onDataChange: HAHAHA "
//                        + dataSnapshot.getKey()
//                        + " : "
//                        + dataSnapshot.getChildrenCount());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                Log.e("TAG", "onCancelled: " + databaseError.getMessage());
//            }
//        });
//
////        nciFirebaseDatabaseReference.addChildEventListener(new ChildEventListener() {
////            @Override
////            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////                String check = dataSnapshot.getKey() + ": " + dataSnapshot.getChildrenCount();
////                 Log.e(dataSnapshot.getKey(),dataSnapshot.getChildrenCount() + "");
////                Toast.makeText(MainActivity.this, check, Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////                Log.e("TAG", "onChildChanged: ");
////            }
////
////            @Override
////            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
////
////                Log.e("TAG", "onChildRemoved: ");
////            }
////
////            @Override
////            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
////
////                Log.e("TAG", "onChildMoved: ");
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError databaseError) {
////
////                Log.e("TAG", "onCancelled: ");
////            }
////        });
//
////        nciFirebaseDatabaseReference
////                .addChildEventListener(new ChildEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                        int count = (int) dataSnapshot.getChildrenCount();
////                        Toast.makeText(MainActivity.this, articleId + ": " + count, Toast.LENGTH_SHORT).show();
////                    }
////
////                    @Override
////                    public void onCancelled(@NonNull DatabaseError databaseError) {
////                    }
////                });
//        return 1;
//        //
//    }
    //*********************double shake detection***************// UNCOMMENT 5DEC
    final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x * x) + (y * y) + (z * z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;
//            Toast t = Toast.makeText(getApplicationContext(), "Shaken " + shake + " times!", Toast.LENGTH_LONG);
//            t.show();
//            Log.i("Info", "Shaken " + shake + " times!");

//            if (shake > 12) {
//                counter++;
//            }

            if (shake >= 6) {
//                counter = 0;
//                Toast tt = Toast.makeText(getApplicationContext(), "Dont Shake phone", Toast.LENGTH_LONG);
//                tt.show();
//                t1.speak("Stop shaking!!", TextToSpeech.QUEUE_FLUSH, null, "stop");
                Log.d("Info", "Shaken " + shake + " times! Stop shaking!");

                //********** Vibrating the phone on shaking *******//
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
// Vibrate for 500 milliseconds
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        v.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_DOUBLE_CLICK));
                        Log.d("Vibration:", "VibrationEffect.EFFECT_DOUBLE_CLICK");
                    } else {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        Log.d("Vibration:", "VibrationEffect.DEFAULT_AMPLITUDE, 500ms");
                    }
                } else {
                    //deprecated in API 26
                    v.vibrate(500);
                    Log.d("Vibration:", "500ms (API 26)");
                }
                //********** Vibrating the phone on shaking *******//

                mSpeechRecognizer.startListening(speechRecognizerIntent);
                Log.i("Info", "Started listening");
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    private void checkRecordingPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, RecordAudioRequestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == RecordAudioRequestCode && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }
    //*********************double shake detection***************// UNCOMMENT 5DEC

}
