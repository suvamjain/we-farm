package com.arshilgenius.kisan.agriculture;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class NewsAdapter1 extends RecyclerView.Adapter<NewsAdapter1.ViewHolder> {

    private Context context;
    private List<News_List> list;
    private List<Crop> mCropList;
    private Integer mode=0;
    private static final int RECOVERY_DIALOG_REQUEST = 1;
    //these ids are the unique id for each video
//    String[] VideoID = {"zI-Pux4uaqM", "nCgQDjiotG0", "nQHjjmIVjTU"};

    public NewsAdapter1(Context context, List<News_List> list, List<Crop> cropList, int mode) {
        this.context = context;
        this.list = list;
        this.mode = mode;
        this.mCropList = cropList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.newss_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (mode == 0) {
            News_List item = list.get(position);
            holder.heading.setText(item.getHeading());
            holder.desc.setText(item.getDetails());
            holder.link.setText(item.getNews_link());
            Picasso.with(context).load(item.getImage()).into(holder.image);
        }
        else {
            final Crop item = mCropList.get(position);
            String fName = item.getName();
            holder.heading.setText(fName);
            fName = fName.toLowerCase() + ".txt";
            AssetManager files = context.getResources().getAssets();
            try {
                InputStream inputStream = files.open(fName);
                holder.desc.setText(convertStreamToString(inputStream));
                holder.readBtn.setTag(fName);
            } catch (IOException e) {
                // Log exception
                Log.e("FileNotFound:", e.toString());
                holder.desc.setText("No Description available");
                holder.readBtn.setVisibility(View.GONE);
            }
            final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
                @Override
                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {}

                @Override
                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                    holder.relativeLayoutOverYouTubeThumbnailView.setVisibility(View.VISIBLE);
                }
            };

            holder.youTubeThumbnailView.initialize(Config.DEVELOPER_KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                    youTubeThumbnailLoader.setVideo(item.getDetail());
                    youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult errorReason) {
                    if (errorReason.isUserRecoverableError()) {
                            errorReason.getErrorDialog((Activity)context, RECOVERY_DIALOG_REQUEST).show();
                    } else {
                        String errorMessage = String.format(context.getString(R.string.player_error), errorReason.toString());
                        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (mode == 0)
            return list.size();
        return mCropList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView heading;
        public TextView desc;
        public ImageView image;
        public TextView link, readBtn;
        public YouTubeThumbnailView youTubeThumbnailView;
        protected RelativeLayout relativeLayoutOverYouTubeThumbnailView;
        protected ImageView playButton;

        public ViewHolder(View itemView) {
            super(itemView);
            heading = (TextView)itemView.findViewById(R.id.topic);
            desc = (TextView)itemView.findViewById(R.id.details);
            link=(TextView)itemView.findViewById(R.id.image_link);
            readBtn = (TextView) itemView.findViewById(R.id.readbtn);
            image = (ImageView)itemView.findViewById(R.id.imageView);
            playButton=(ImageView)itemView.findViewById(R.id.btnYoutube_player);
            relativeLayoutOverYouTubeThumbnailView = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);

            if (mode == 1) {
                link.setVisibility(View.GONE);
                readBtn.setVisibility(View.VISIBLE);
                playButton.setOnClickListener(this);
                youTubeThumbnailView = (YouTubeThumbnailView) itemView.findViewById(R.id.youtube_thumbnail);
                readBtn.setOnClickListener(this);
            }
            else {
                link.setVisibility(View.VISIBLE);
                image.setVisibility(View.VISIBLE);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String Url = link.getText().toString();
                        Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(Url));
                        v.getContext().startActivity(intent);
                    }
                });
            }
        }

        @Override
        public void onClick(View view) {
            if (view == readBtn) {
                try {
                    InputStream inputStream = context.getAssets().open(view.getTag().toString());
                    String s = convertStreamToString(inputStream);
                    Log.e("READ BTN click", s);
                    AlertDialog.Builder dialog=new AlertDialog.Builder(context,android.R.style.Theme_Material_NoActionBar_Fullscreen);
                    dialog.setMessage(s);
                    dialog.setTitle(mCropList.get(getAdapterPosition()).getName());
                    dialog.setNegativeButton("Close",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });

                    AlertDialog alertDialog=dialog.create();
                    alertDialog.show();

                } catch (IOException e){
                    // Log exception
                }
            }
            else {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context,
                        Config.DEVELOPER_KEY,
                        mCropList.get(getAdapterPosition()).getDetail(),  //video id -- //getLayoutPosition was originally used
                        100,                        //after this time, video will start automatically
                        true,                      //autoplay or not
                        true);                   //lightbox mode or not; show the video in a small box
                context.startActivity(intent);
            }
        }
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    //to get two lines from file
    static String getLineFromStream(java.io.InputStream is, int linePos){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line = null;
        try {
            for (int i = 0; i <= linePos; i++) {
                line =  br.readLine();
            }
        } catch (IOException e){
            // Handle exception here (or you can throw)
        }
        return line;
    }
}
