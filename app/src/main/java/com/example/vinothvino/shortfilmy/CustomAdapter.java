package com.example.vinothvino.shortfilmy;

/**
 * Created by vinothvino on 30/01/16.
 */
import android.app.Activity;

import android.graphics.Color;
import android.os.Bundle;
import android.content.Intent;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CustomAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] Title;
    private final String[] VideoID;
    private final String[] LikeCount;
  //  private final String LikeCounterUpdate;

    private YouTubeThumbnailLoader youTubeThumbnailLoader;
    private YouTubeThumbnailView youTubeThumbnailView;
    private TextView textView;
    private TextView textViewlike;
    private ListView listView;
  //  private ImageView imgbutton;
    private static final String TAG = "CustomAdapter";
  //  private ProgressDialog progressDialog;




    public CustomAdapter(Activity context,String[] Title,String[] VideoID,String[] LikeCount){

        super(context,R.layout.activity_main,VideoID);
        this.context = context;
        this.Title = Title;
        this.VideoID = VideoID;
        this.LikeCount = LikeCount;
      //  this.LikeCounterUpdate =




    }
    //This method is called when the list items are visible to user
    @Override
    public View getView(final int position,View convertView,ViewGroup parent){

        Log.d(TAG,"Entered into getview method");
    // Toast.makeText(context,"getView",Toast.LENGTH_SHORT).show();


        View ListItemView = convertView;

        if (convertView == null){

            LayoutInflater inflater = context.getLayoutInflater();
            ListItemView = inflater.inflate(R.layout.youtubethumbnail_loader,null,false);    //Inflating youtube thumbnail view layout
           // LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        }


      /**  imgbutton = (ImageView)ListItemView.findViewById(R.id.heartbuttonimg);
        //updating likes
        imgbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });*/




        youTubeThumbnailView = (YouTubeThumbnailView)ListItemView.findViewById(R.id.youtubethumbnailviewnew);
        youTubeThumbnailView.initialize(Config.API_KEY, new YouTubeThumbnailView.OnInitializedListener() {


            @Override
            public void onInitializationSuccess(YouTubeThumbnailView thumbnailView, YouTubeThumbnailLoader thumbnailLoader) {

                //   Toast.makeText(context,"Success initialization",Toast.LENGTH_SHORT).show();
                youTubeThumbnailLoader = thumbnailLoader;

                thumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {


                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                        // Toast.makeText(context, "Failed to load thumbnail", Toast.LENGTH_SHORT).show();

                    }
                });

                thumbnailLoader.setVideo(VideoID[position]);  //setting video id to getting thumbnail

            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {


                //   Toast.makeText(context,"Failed to initialize\nPlease check your internet connection",Toast.LENGTH_SHORT).show();

            }
        });

        textView = (TextView)ListItemView.findViewById(R.id.textViewnew);
        textView.setText(Title[position]);

        textViewlike = (TextView)ListItemView.findViewById(R.id.liketext);
        textViewlike.setText(LikeCount[position]);
      //  textViewlike.setText();

        return ListItemView;
    }

    public void noOperation(){

    }


}
