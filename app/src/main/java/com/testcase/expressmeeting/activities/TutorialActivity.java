package com.testcase.expressmeeting.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.testcase.expressmeeting.R;
import com.testcase.expressmeeting.fragments.TutorialFragment;
import com.testcase.expressmeeting.helpers.ColorPageTransformer;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends ActionBarActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private static final int NUM_PAGES = 4;
    private static final String CLIENT_ID = "8a27481f9a124bb79ef7fb3abcafd94d";
    private static final String REDIRECT_URI = "http://google.com";
    private static final String SCOPES = "profile invite miniblog";
    private static final String OAUTH_URL = "https://oauth.mig.me/oauth/";
    private static final String LOGIN_URL = OAUTH_URL+"auth?client_id="+CLIENT_ID+"&redirect_uri="+REDIRECT_URI+"&scope="+SCOPES+"&response_type=code";
    private static final String TOKEN_URL = OAUTH_URL+"token";

    private TutorialActivity context;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private Button nextButton;
    private SharedPreferences pref;


    private Intent mainIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        context = this;

        mainIntent = new Intent(getApplicationContext(), MainActivity.class);

        setContentView(R.layout.activity_tutorial);
        setupViewPager();
        setupNextButton();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void setupViewPager() {
        mPager = ((ViewPager) findViewById(R.id.tutorial_pager));
        mPagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        ColorPageTransformer pageTransformer = new ColorPageTransformer(mPager);
        CirclePageIndicator indicator = (CirclePageIndicator) findViewById(R.id.tutorial_pager_indicator);

        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, pageTransformer);
        indicator.setViewPager(mPager);
        indicator.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_button:
                if (mPager.getCurrentItem() < mPager.getChildCount()) {
                    mPager.setCurrentItem(mPager.getCurrentItem()+1, true);
                }
                break;
            case R.id.sign_in:
                final Dialog auth_dialog = new Dialog(context);
                auth_dialog.setContentView(R.layout.auth_dialog);

                WebView webView = (WebView)auth_dialog.findViewById(R.id.auth_web_view);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.loadUrl(LOGIN_URL);
                Log.e("login_url", LOGIN_URL);
                webView.setWebViewClient(new WebViewClient() {
                    boolean authComplete = false;
                    String authCode;
                    String token;

                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        if ((url.contains("?code=") && authComplete != true)) {
                            Uri uri = Uri.parse(url);
                            authCode = uri.getQueryParameter("code");
                            authComplete = true;
                            mainIntent.putExtra("code", authCode);

                            SharedPreferences.Editor edit = pref.edit();
                            edit.putString("code", authCode);
                            edit.commit();

                            auth_dialog.dismiss();

                            context.setResult(Activity.RESULT_OK, mainIntent);
                            setResult(Activity.RESULT_CANCELED, mainIntent);

                            new TokenGet().execute();

                        } else if (url.contains("error=access_denied")) {
                            Log.i("", "ACCESS_DENIED_HERE");
                            authComplete = true;
                            mainIntent.putExtra("code", authCode);
                            setResult(Activity.RESULT_CANCELED, mainIntent);
                            Toast.makeText(getApplicationContext(), "Error Occured", Toast.LENGTH_SHORT).show();
                            auth_dialog.dismiss();
                        }
                    }
                });
                auth_dialog.setCancelable(true);
                auth_dialog.setTitle("Sign in With MigMe");
                auth_dialog.show();
                break;
            default: break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {
        if (mPager.getCurrentItem() == (NUM_PAGES - 1)) {
            nextButton.setVisibility(View.INVISIBLE);
            Button signInButton = (Button) mPager.findViewById(R.id.sign_in);
            signInButton.setOnClickListener(context);
        } else {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void setupNextButton() {
        nextButton = (Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);
    }

    private class TutorialPagerAdapter extends FragmentStatePagerAdapter {
        public TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            TutorialFragment fragment = TutorialFragment.newInstance(i);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    private class TokenGet extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog progressDialog;
        String code;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Contacting MigMe ...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            code = pref.getString("code", "");

            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            JSONObject jsonObject = null;

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(TOKEN_URL);

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("code", code));
                params.add(new BasicNameValuePair("client_id", CLIENT_ID));
                params.add(new BasicNameValuePair("redirect_uri", REDIRECT_URI));
                params.add(new BasicNameValuePair("grant_type", "authorization_code"));
                httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);

                InputStream inputStream = httpResponse.getEntity().getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();

                jsonObject = new JSONObject(sb.toString());

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonObject;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            progressDialog.dismiss();

            if (jsonObject != null) {
                try {
                    String token = jsonObject.getString("access_token");

                    SharedPreferences.Editor edit = pref.edit();
                    edit.putString("token", token);
                    edit.commit();

                    mainIntent.putExtra("token", token);

                    startActivity(mainIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
