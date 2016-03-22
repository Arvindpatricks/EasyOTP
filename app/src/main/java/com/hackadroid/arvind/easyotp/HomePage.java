package com.hackadroid.arvind.easyotp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonParser;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseApp;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONException;
import org.json.JSONObject;





public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public final String FIREBASE_URL= "https://easyotp.firebaseio.com";
   public  FileOutputStream fOut;
    public FileInputStream fin;

    public static TextView smsotp;
    public String otpreceived;

    String emailIDReceived,photoUrlReceived,usernameReceived;
    TextView headername,headeremail;
    RoundedImageView imageView;
    final CharSequence[] items = {"Enable", "Disable"};
    AlertDialog autoOTPDialog;
    AlertDialog.Builder builder;
    Person person = new Person();
    String subscriberId;
    Firebase ref;
    FloatingActionButton fab;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);



        if(!isNetworkAvailable()){
            Toast.makeText(getApplicationContext(),"Internet Not Available..",Toast.LENGTH_LONG).show();
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);


try {
// Strings to Show In Dialog with Radio Buttons

   fOut = openFileOutput("EasyOTP",MODE_WORLD_READABLE);
    fin = openFileInput("EasyOTP");
    // Creating and Building the Dialog
    builder = new AlertDialog.Builder(this);
    builder.setTitle("Auto Send OTP");
    builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int item) {


            switch (item) {
                case 0:

                    FileOutputStream fOut = null;
                    try {
                        fOut = openFileOutput("autootp",MODE_WORLD_READABLE);
                        fOut.write("enabled".getBytes());

                        fOut.close();
                     //   Toast.makeText(getApplicationContext(), "Presaved", Toast.LENGTH_SHORT).show();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }


                    autoOTPDialog.dismiss();
                    break;
                case 1:
                    FileOutputStream fOut2 = null;
                    try {
                        fOut = openFileOutput("autootp",MODE_WORLD_READABLE);
                        fOut.write("disabled".getBytes());

                        fOut.close();

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch(IOException e){
                        e.printStackTrace();
                    }

                    autoOTPDialog.dismiss();
                    break;


            }
        }
    });


}catch(Exception e){}

        emailIDReceived=getIntent().getExtras().getString("email");
        Firebase.setAndroidContext(this);
        usernameReceived=getIntent().getExtras().getString("name");
        System.out.println(emailIDReceived);
        photoUrlReceived=getIntent().getExtras().getString("personPhotoUrl");

       String personPhotoUrl=getIntent().getExtras().getString("personPhotoUrl");
       String coverpic=getIntent().getExtras().getString("coverpic");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getIntent().getStringExtra("otp")!= null) {
            smsotp = (TextView) findViewById(R.id.smsotp);
            smsotp.setText(getIntent().getExtras().getString("otp"));


        }
        else{
            Toast.makeText(getApplicationContext(),"OTP is NULL",Toast.LENGTH_SHORT).show();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending Data To Browser", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                 ref = new Firebase(FIREBASE_URL);

                //Getting values to store
                String name = usernameReceived;
                String emailid= emailIDReceived;
                String otp = getIntent().getExtras().getString("otp");

                String id="";

                ref.child(name).child("otp").setValue(smsotp.getText().toString());
                //ref.child(name).child("emailid").setValue(usernameReceived);
                System.out.println("Stored..");
                otpreceived=smsotp.getText().toString();


                //new SendNotification().execute();



            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    final    View header=navigationView.getHeaderView(0);
        headername = (TextView)header.findViewById(R.id.headername);
        headeremail = (TextView) header.findViewById(R.id.headeremail);
        imageView=(RoundedImageView)header.findViewById(R.id.imageView);
        Picasso.with(getApplicationContext()).load(personPhotoUrl).into(imageView);

        headername.setText(usernameReceived);
        headeremail.setText(emailIDReceived);
        Picasso.with(getApplicationContext()).load(coverpic).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d("COVER-PIC-LOADING", "LOADED");

                header.setBackground(new BitmapDrawable(getApplicationContext().getResources(), bitmap));
            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d("COVER-PIC-LOADING", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                Log.d("COVER-PIC-LOADING", "Prepare Load");
            }
        });


        String temp2="";
        try{
            FileInputStream fin1 = openFileInput("autootp");
            int c;

            while( (c = fin1.read()) != -1){
                temp2 = temp2 + Character.toString((char)c);
            }

        }
        catch(Exception e){
        }





        if(temp2.equals("enabled")) {

            if(getIntent().getStringExtra("otp")!= null){
                fab.performClick();

            }



        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
           // super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sendotp) {

                fab.performClick();
        } else if (id == R.id.logout) {

           Intent i = new Intent(HomePage.this,SignOut.class);
            startActivity(i);

        }

        else if (id==R.id.autosend){
            autoOTPDialog = builder.create();
            autoOTPDialog.show();


            String temp1="";
            try{
                FileInputStream fin1 = openFileInput("autootp");
                int c;

                while( (c = fin1.read()) != -1){
                    temp1 = temp1 + Character.toString((char)c);
                }

            }
            catch(Exception e){
            }





if(temp1.equals("enabled")) {
    autoOTPDialog.getListView().setItemChecked(0,true);

}
else{
    autoOTPDialog.getListView().setItemChecked(1,true);

}

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Toast.makeText(getApplicationContext(), "Already Connected..", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    class SendNotification extends AsyncTask<String,Void,String> {


        private  String readAll(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }


        public  JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
            InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readAll(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }


        String  encodedUrl;

        protected String doInBackground(String... urls) {
            try {
                JSONObject json = readJsonFromUrl("https://easyotp.firebaseio.com/"+usernameReceived.replaceAll(" ","%20")+".json");
                System.out.println(json.get("subscriberId").toString());
                subscriberId=json.get("subscriberId").toString();





                System.out.println("Backgournd executing..");
                OkHttpClient client = new OkHttpClient();

                RequestBody formBody = new FormBody.Builder()
                        .add("title", "Easy OTP-Testing")
                        .add("message","Your OTP is : "+otpreceived)
                        .add("url","http://www.arvinds-blog.net/")
                        .add("subscriber_id",subscriberId)

                        .build();
                Request request = new Request.Builder()
                        .url("https://pushcrew.com/api/v1/send/individual")
                        .post(formBody)
                        .addHeader("Authorization","3fb60da630c7a044e467c38c24e6f47d")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    System.out.println("Ntification response :"+response);
                    // Do something with the response.
                } catch (IOException e) {
                   System.out.println(e.toString());
                }






            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
            return "";
        }

        protected void onPostExecute() {
            System.out.println("TESTING:OTP-SENT");
        }
    }










}
