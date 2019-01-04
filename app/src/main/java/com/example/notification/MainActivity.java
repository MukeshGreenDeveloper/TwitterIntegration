package com.example.notification;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.AccountService;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.default_twitter_login_button)
    TwitterLoginButton twitterLoginButton;
    @BindView(R.id.layout_tweetData)
    LinearLayout layout_tweetData;
    @BindView(R.id.name)
    TextView text_name;
    @BindView(R.id.userName)
    TextView text_user_name;
    @BindView(R.id.id)
    TextView text_id;
    @BindView(R.id.location)
    TextView text_location;
    @BindView(R.id.emailid)
    TextView text_email;
    @BindView(R.id.profielic)
    ImageView profileImage;
    @BindView(R.id.banner_profielic)
    ImageView bannerImage;
    final static String ScreenName = "GiveCentral";
    final static String LOG_TAG = "gc";
    static String TwitterTokenURL = "https://api.twitter.com/oauth2/2258696856-bNQ3v3kycKXiKfQwwoE4JKmb9OYiMLcZdSrcO9I";
    final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";
//    private vodi twitterEmail;

    //     static String TwitterTokenURL = "https://api.twitter.com/oauth2/";//2258696856-bNQ3v3kycKXiKfQwwoE4JKmb9OYiMLcZdSrcO9I";

    APIInterface apiInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        twitterLoginButton = (TwitterLoginButton) findViewById(R.id.default_twitter_login_button);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                Log.d("kesy", "Got the result");
                final List<Long> tweetIds =
                        Arrays.asList(result.data.getUserId(), 20L);
//                TwitterTokenURL=TwitterTokenURL+result.data.getAuthToken();
                ((TextView) findViewById(R.id.name)).setText(result.data.getUserName());
                ((TextView) findViewById(R.id.id)).setText(result.data.getUserId() + "");
              /*  Log.d("kesy", "name:" + result.data.getUserName());
                Log.d("kesy", "userID:" + result.data.getUserId());
                Log.d("kesy", "AuthToken:" + result.data.getAuthToken());
                Log.d("kesy", "ID:" + result.data.getId());*/
                getTwitterEmail(result.data);
                getUserDetails(result.data);
//                downloadTweets();
        /*        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient(result.data);
                final ArrayList<Tweet> tweets = new ArrayList<>();
                TwitterCore.getInstance().getApiClient(result.data).getStatusesService()
                        .userTimeline(null,
                                "screenname",
                                10, //the number of tweets we want to fetch,
                null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        new Callback<List<Tweet>>() {
                            @Override
                            public void success(Result<List<Tweet>> result) {
                                for (Tweet t : result.data) {
                                    tweets.add(t);
                                    android.util.Log.d("twittercommunity", "tweet is " + t.text);
                                }
                            }

                            @Override
                            public void failure(TwitterException exception) {
                                android.util.Log.d("twittercommunity", "exception " + exception);
                            }
                        });*//* TweetUtils.loadTweets(tweetIds, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        for (Tweet tweet : result.data) {
                            Log.v("tweet", tweet.toString());
                            layout_tweetData.addView(new CompactTweetView(MainActivity.this, tweet));
                        }
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });*/
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Log.d("kesy", "Faild --" + exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    /*// download twitter timeline after first checking to see if there is a network connection
    public void downloadTweets() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadTwitterTask().execute(ScreenName);
        } else {
            Log.v(LOG_TAG, "No network connection available.");
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void getTwitterEmail(final TwitterSession session) {
Long userID;String id,userName;
        userID=session.getUserId();
        id=session.getUserName()+"";
        userName=session.getUserName();
        try {
            // URL encode the consumer key and secret
            String urlApiKey = URLEncoder.encode(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY), "UTF-8");
            String urlApiSecret = URLEncoder.encode(getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET), "UTF-8");
            // Concatenate the encoded consumer key, a colon character, and the
            // encoded consumer secret
            String combined = /*urlApiKey + ":" + urlApiSecret*//*session.getAuthToken().token+":"+session.getAuthToken().secret*/session.getAuthToken().token;

            // Base64 encode the string
            String base64Encoded = Base64.encodeToString(combined.getBytes(), Base64.NO_WRAP);
            HashMap<String,String> headerMap=new HashMap<>();
            headerMap.put("Authorization", "Bearer " + combined );
            headerMap.put("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//            httpPost.setEntity(new StringEntity("grant_type=client_credentials"));
            /**
             GET List Resources
             **/
//        Call<User> call = apiInterface.show("Bearer "+session.getAuthToken().token,userID,id);
//            Call<User> call = apiInterface.list("Bearer <" + session.getAuthToken().token + ">", userID, userName, false);
            Call<User> call = apiInterface.list(headerMap, userID, userName, false);
            call.enqueue(new retrofit2.Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Log.d("kesy", "Got Message");
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Log.d("kesy", "Failed Message");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getUserDetails(TwitterSession twitterSession) {
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        Call<List<com.twitter.sdk.android.core.models.Tweet>> callTweets= twitterApiClient.getStatusesService().mentionsTimeline(10,twitterSession.getId(),10L,false,true,false);
        callTweets.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                Log.d("kess","adsfsdf");
            }

            @Override
            public void failure(TwitterException exception) {

            }
        });
        AccountService accountService = twitterApiClient.getAccountService();
        Call<com.twitter.sdk.android.core.models.User> call = accountService.verifyCredentials(true, true, true);
        call.enqueue(new Callback<com.twitter.sdk.android.core.models.User>() {
            @Override
            public void success(Result<com.twitter.sdk.android.core.models.User> result) {
                //here we go User details
                Log.e("kesy","ABCdGot Message");
                if(result!=null&&result.data!=null){
                    if(!TextUtils.isEmpty(result.data.name))
                    text_name.setText(result.data.name);
                    if(!TextUtils.isEmpty(result.data.screenName ))
                    text_user_name.setText(result.data.screenName );
                    if(result.data.id>0)
                    text_id.setText(result.data.id+"" );
                    if(!TextUtils.isEmpty(result.data.email))
                    text_email.setText(result.data.email);
                    if(!TextUtils.isEmpty(result.data.location))
                    text_location.setText(result.data.location);
                    if(!TextUtils.isEmpty(result.data.profileImageUrl))
                        Picasso.with(MainActivity.this).load(result.data.profileImageUrl)
                        .into(profileImage);
                    if(!TextUtils.isEmpty(result.data.profileBannerUrl))
                        Picasso.with(MainActivity.this).load(result.data.profileBannerUrl)
                        .into(bannerImage);
                }
            }

            @Override
            public void failure(TwitterException exception) {
            }
        });
    }
    /*class MyTwitterApiClient extends TwitterApiClient {
        static final String BASE_URL = "https://api.twitter.com/";
        public MyTwitterApiClient(TwitterSession session) {
            super(session);
        }
        public CustomService getCustomService() {
            return getService(CustomService.class);
        }


    }

    interface CustomService {
        @GET("/1.1/users/show.json")
        Call<User> show(@Query("user_id") long id, Callback<User> cb);
    }
*/


}