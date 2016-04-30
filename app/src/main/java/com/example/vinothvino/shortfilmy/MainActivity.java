package com.example.vinothvino.shortfilmy;
/**
 * Created by vinothvino on 24/01/16.
 * AppName : ShortFilmy
 * VersionCode : 1
 * VersionName : 1.1
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Menu;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.lang.Thread;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends Activity {

    private static final String TAG = "HTTPConnection";
    private ListView listView;
    private String[] Title;
    private String[] VideoID;
    private String[] LikeCount;

    //private String likecounter;
    private ProgressDialog progressDialog;

  //  private static final int NotifyID = 0;
    //private LinearLayout linearLayout;
   // private Activity context;
    //private ArrayAdapter arrayAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

      //  imgbutton = (ImageButton)findViewById(R.id.heartbuttonimg);





        // final String JsonURL = "http://pastebin.com/raw/QfSVJ1za";

      //  final String JsonURL = "https://shortfilmy.herokuapp.com/snippets/";

      listView = (ListView) findViewById(R.id.listView);
      final String JsonURL = Config.JsonURL;

        //Executing JSON url with AsyncTask
        new AsyncHTTPTask().execute(JsonURL);


//updating likes
     /**   imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "You liked this video ", Toast.LENGTH_SHORT).show();
                imgbutton.setBackgroundColor(Color.RED);
                //    LikeCount[position] = LikeCount.toString();

                // String updateLikes = LikeCount[position]+1;
                // textViewlike.setText(updateLikes);

                //  likesCount = jsonIndexVal.optString("likes")

            }
        });*/


    }







public class AsyncHTTPTask extends AsyncTask<String, Void, Integer>{

    /**
     * User can perform any operations before getting into background tasks
     *Here,we're showing ProgressDialog until doInBackground method called
     */
    @Override
    protected void onPreExecute(){

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("ShortFilmy");
        progressDialog.setMessage("Fetching data from JSON..");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

  //Getting responses from JSON. This method is called after onPreExecute method
    protected Integer doInBackground(String...params){

        InputStream inputstream = null;
        HttpURLConnection httpURLConnection = null;
        Integer result = 0;

        try{


            URL url = new URL(params[0]);
            httpURLConnection = (HttpURLConnection)url.openConnection();

            //Request header it's optional
            httpURLConnection.setRequestProperty("Content-Type","application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");

            //Setting request method
            httpURLConnection.setRequestMethod("GET");

            int statuscode = httpURLConnection.getResponseCode();


            // 200 is HTTP OK
            if(statuscode == 200){

                inputstream = new BufferedInputStream(httpURLConnection.getInputStream());
                String convertedStringVal = convertStreamToString(inputstream);

                parseJson(convertedStringVal);
               // CustomAdapter customAdapter = new CustomAdapter(context,Title,VideoID);

                result = 1;  //Successfully parsed


            }else {

                Log.d(TAG, "customadapter completed");


               // Toast.makeText(getApplicationContext(),"Failed to fetch data...\nPlease check your Internet ", Toast.LENGTH_LONG).show();

                result = 0;   //Failed to fetch data from json

            }

        }catch (Exception e) {

            Log.d(TAG, e.getLocalizedMessage());

           }

           return result;
        }

    //Updating UI,This method is called after doInBackground method
        protected void onPostExecute(Integer result){


          //  Toast.makeText(getApplicationContext(),"onPostExecute",Toast.LENGTH_SHORT).show();
            if(result == 1){


                CustomAdapter customAdapter = new CustomAdapter(MainActivity.this,Title,VideoID,LikeCount);

                listView.setAdapter(customAdapter);



                Thread progressbarsleep = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            // Do some stuff

                            //Stop showing ProgressDialog


                            if (listView.isShown()) {
                                //if (progressDialog.isShowing()) {


                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Welcome to ShortFilmy", Toast.LENGTH_SHORT).show();

                                //  }
                            }


                        } catch (Exception e) {
                            e.getLocalizedMessage();
                        }
                    }
                });
                progressbarsleep.start();


                 //  arrayAdapter = new ArrayAdapter(MainActivity.this,R.layout.list_view,Title);
              //  listView.setAdapter(arrayAdapter);





                //Below operations performed when the user clicks one of the list items in the listview
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        int itempos = position;

                        //String itemVal = (String) listView.getItemAtPosition(itempos);


                        if (itempos >= 0) {

                            String videoIDVal = VideoID[itempos];
                            // Toast.makeText(getApplicationContext(),videoIDVal,Toast.LENGTH_LONG).show();

                            Intent i = new Intent(getApplicationContext(), YouTubeView.class);
                            i.putExtra("key", videoIDVal);
                            startActivity(i);
                            Toast.makeText(getApplicationContext(), Title[itempos], Toast.LENGTH_LONG).show();





                        }


                    }

                });

                //imgbutton = (ImageView)findViewById(R.id.heartbuttonimg);



            }else {

                if (result == 0){
                    progressDialog.dismiss();

                    //showing 404 error layout with imageview
                    setContentView(R.layout.errorlayout);

                    /// Toast.makeText(getApplicationContext(),"Failed to fetch dataa",Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(),"Failed to fetch data...\nPlease check your Internet Connection", Toast.LENGTH_SHORT).show();
                Log.e(TAG,"FAILED TO FETCH DATA");
            }
        }

  }//AsyncTask closed




    //Converting inputstreams into strings
    private String convertStreamToString(InputStream inputStream)throws IOException{

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String data = "";

        while((line = bufferedReader.readLine()) != null){
            data+= line;
        }
        if (null != inputStream){

            inputStream.close();
        }
        return data;
    }



    //Likes updating
  /**  private void likeUpdating(String likereceivedVal){

        try{

            JSONObject jsonlikeobject = new JSONObject(likereceivedVal);
            JSONArray jsonArray = jsonlikeobject
        }
    } */



    //parsing json
    public void parseJson(String receivedVal){

        try {


            JSONObject jsonData = new JSONObject(receivedVal);
            JSONArray jsonArray = jsonData.optJSONArray("youtube");

            Title = new String[jsonArray.length()];
            VideoID = new String[jsonArray.length()];
            LikeCount = new String[jsonArray.length()];




           // JSONObject jsonObj = jsonArray.getJSONObject(0);


            for (int i=0; i<jsonArray.length(); i++) {

                final JSONObject jsonIndexVal = jsonArray.optJSONObject(i);
                String title = jsonIndexVal.optString("title");
                String videoID = jsonIndexVal.optString("videoid");
                String likesCount = jsonIndexVal.optString("likes");


                Title[i] = title;
                VideoID[i] = videoID;
                LikeCount[i] = likesCount;



            }

          // sendMethod(jsonArray);


          /**  Intent lyk = new Intent(getApplicationContext(),CustomAdapter.class);
            lyk.putExtra("lykval",receivedVal);
            startActivity(lyk);*/





        }catch (JSONException e){

            e.printStackTrace();
        }

    }

   /** public void sendMethod(JSONArray receivedArray){

         ImageView imgbutton;

        JSONArray RecJSONArray = receivedArray;

        imgbutton = (ImageView)findViewById(R.id.heartbuttonimgnew);
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  Toast.makeText(getApplicationContext(),"Hey",Toast.LENGTH_SHORT).show();
            }
        });
    }*/




}


