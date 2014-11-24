package com.wasalny.demo.fragments;

 
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wasalny.demo.MainActivity;
import com.wasalny.demo.R;
import com.wasalny.demo.constant.FoursquareKyes;

public class ActivitywebView extends Activity{

	 private static final String TAG = "ActivityWebView";
	    public static final String CLIENT_ID =  FoursquareKyes.client_ID;
	    public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-foursquare";
	    public static final String OAUTH_CALLBACK_HOST = "callback";
	    public static final String CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;
        SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.actvity_web);
		
		prefs=getSharedPreferences(FoursquareKyes.SHARED_NAME, MODE_PRIVATE);
		
		String url =
		        "https://foursquare.com/oauth2/authenticate" + 
		            "?client_id=" + CLIENT_ID + 
		            "&response_type=token" + 
		            "&redirect_uri=simontest-android-app://xyz";

		            WebView webview = (WebView)findViewById(R.id.webview);
		    webview.getSettings().setJavaScriptEnabled(true);
		    webview.setWebViewClient(new WebViewClient() {
		        public void onPageStarted(WebView view, String url, Bitmap favicon) {
		            String fragment = "#access_token=";
		            int start = url.indexOf(fragment);
		            if (start > -1) {
		                // You can use the accessToken for api calls now.
		                String accessToken = url.substring(start + fragment.length(), url.length());

		                Log.v(TAG, "OAuth complete, token: [" + accessToken + "].");

		              //  Toast.makeText(ActivitywebView.this, "Token: " + accessToken, Toast.LENGTH_SHORT).show();
		                 prefs.edit().putString(FoursquareKyes.TOKEN_KEY, accessToken).commit();
		               
		                 startActivity(new Intent(ActivitywebView.this,MainActivity.class)
		                 .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) );  
		            }
		        }
		    });
		    webview.loadUrl(url);
		}
	 
}
