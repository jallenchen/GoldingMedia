package com.goldingmedia.mvp.view.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.VideoView;

import com.goldingmedia.BaseActivity;
import com.goldingmedia.GDApplication;
import com.goldingmedia.R;
import com.goldingmedia.activity.WindowAdsPlayActivity;
import com.goldingmedia.contant.Contant;
import com.goldingmedia.goldingcloud.TruckMediaProtos;
import com.goldingmedia.mvp.view.adapter.BookAdapter;
import com.goldingmedia.mvp.view.adapter.ImageAdapter;
import com.goldingmedia.mvp.view.ui.BookLayout;
import com.goldingmedia.temporary.Variables;
import com.goldingmedia.utils.HandlerUtils;
import com.goldingmedia.utils.NLog;
import com.goldingmedia.utils.StreamTool;
import com.goldingmedia.mvp.view.ui.AutoPlayGallery;
import com.goldingmedia.utils.Utils;

import static com.goldingmedia.temporary.ImageProcessing.ConvertBitMap;
import static com.goldingmedia.utils.Utils.onDemandRecording;


public class ReadBookActivity extends BaseActivity implements
        View.OnTouchListener, MediaPlayer.OnCompletionListener {
    /** Called when the activity is first created. */
    private BookLayout bk;
    private TextView textView;
    private String ebookName;
    private VideoView mTopVideo;
    private AutoPlayGallery mMidImg;
    private AutoPlayGallery mBottomImg;
    private int mTopCount;
    private List<TruckMediaProtos.CTruckMediaNode> mWindowTopList;
    private  ArrayList<Bitmap> MidBitMapArray= new ArrayList<Bitmap>();
    private  ArrayList<Bitmap> BottomBitMapArray= new ArrayList<Bitmap>();
    private int mTimerCounter = 0;
    private HandlerUtils.HandlerHolder handlerHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_read_ebook);

       // Intent mIntentMsg = new Intent("com.goldingmedia.system.load.script");
      //  mIntentMsg.putExtra("scriptpath",Contant.SWITCH_HEADPHONE);
      //  sendBroadcast(mIntentMsg);

//        try {
//            GDApplication.getmInstance().getIgoldingSysService().exeCmd("headphone");
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }

        ebookName = getIntent().getStringExtra("ebookName");
    	 bk = (BookLayout) findViewById(R.id.booklayout);
        textView = (TextView) findViewById(R.id.ebook_name);
        mTopVideo = (VideoView)findViewById(R.id.videotop);
        mMidImg = (AutoPlayGallery)findViewById(R.id.imgmid);
        mBottomImg = (AutoPlayGallery)findViewById(R.id.imgbottom);

         ArrayList<String> str = StreamTool.getEbookData();
         BookAdapter ba = new BookAdapter(this);
         ba.addItem(str);
         bk.setPageAdapter(ba);
        textView.setText(ebookName);
        setListener();
        initWindowAds();
        playAds();

        ImageAdapter adapterM = new ImageAdapter(this, MidBitMapArray);
        mMidImg.setAdapter(adapterM);
        ImageAdapter adapterB = new ImageAdapter(this, BottomBitMapArray);
        mBottomImg.setAdapter(adapterB);
    }

    @Override
    protected void onResume() {
        super.onResume();
        playAds();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setListener(){
        mTopVideo.setOnTouchListener(this);
        mTopVideo.setOnCompletionListener(this);
    }

    private void  initWindowAds(){
        List<TruckMediaProtos.CTruckMediaNode> list;

        list = GDApplication.getmInstance().getTruckMedia().getcAds().getWindowOrientTrucksMap(Contant.ADS_WINDOW_ORIENT_MIDDLE);
        for(int i=0;i<list.size();i++){
            TruckMediaProtos.CTruckMediaNode truck = list.get(i);
            String path = Contant.getTruckMetaNodePath(truck.getMediaInfo().getCategoryId(),truck.getMediaInfo().getCategorySubId(), truck.getMediaInfo().getTruckMeta().getTruckFilename()+"/"+truck.getMediaInfo().getTruckMeta().getTruckFilename()+".jpg",true);
            MidBitMapArray.add(ConvertBitMap(path));
        }
        list = GDApplication.getmInstance().getTruckMedia().getcAds().getWindowOrientTrucksMap(Contant.ADS_WINDOW_ORIENT_BOTTOM);
        for(int i=0;i<list.size();i++){
            TruckMediaProtos.CTruckMediaNode truck = list.get(i);
            String path = Contant.getTruckMetaNodePath(truck.getMediaInfo().getCategoryId(),truck.getMediaInfo().getCategorySubId(), truck.getMediaInfo().getTruckMeta().getTruckFilename()+"/"+truck.getMediaInfo().getTruckMeta().getTruckFilename()+".jpg",true);
            BottomBitMapArray.add(ConvertBitMap(path));
            NLog.e("ReadBookActivity","size:"+BottomBitMapArray.size());
        }

    }

    private void playAds() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTopCount = 0;
                mWindowTopList = getWindowList(Contant.ADS_WINDOW_ORIENT_TOP);

                if( mWindowTopList.size() != 0){
                    mWinAdsPlayTime = System.currentTimeMillis();
                    TruckMediaProtos.CTruckMediaNode truck = mWindowTopList.get(mTopCount);
                    String path = Contant.getTruckMetaNodePath(truck.getMediaInfo().getCategoryId(),truck.getMediaInfo().getCategorySubId(), truck.getMediaInfo().getTruckMeta().getTruckFilename()+"/"+truck.getMediaInfo().getTruckMeta().getTruckFilename()+".mp4",true);
                    mTopVideo.stopPlayback();
                    mTopVideo.setVideoURI(Uri.parse(path));
                    mTopVideo.start();
                }
            }
        });
    }

    private List<TruckMediaProtos.CTruckMediaNode> getWindowList(int orient) {
        return GDApplication.getmInstance().getTruckMedia().getcAds().getWindowOrientTrucksMap(orient);
    }

    private Boolean mRepeat = false;
    private void startWindowAdActivity(int position, int orient) {
        if (mRepeat) return;
        mRepeat = true;
        if (getWindowList(orient) != null && getWindowList(orient).size() > 0) {

            TruckMediaProtos.CTruckMediaNode truck = GDApplication.getmInstance().getTruckMedia().getcAds().getWindowOrientTrucksMap(orient).get(position);
            Intent mIntent ;
            switch (truck.getCategorySubId()) {
                case Contant.PROPERTY_ADS_MEDIA_ID:
                    mIntent = new Intent(this, WindowAdsPlayActivity.class);
                    break;

                case Contant.PROPERTY_ADS_IMG_ID:
                    mIntent = new Intent(this, JmagazineActivity.class);
                    break;
                default:
                    return;
            }
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mIntent.putExtra("position", position);
            mIntent.putExtra("orient", orient);
            mIntent.putExtra("truck", truck);
            startActivity(mIntent);
        } else {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mRepeat = false;
            }
        }).start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if( mWindowTopList.size() != 0) minWinAdsStatistics();
        if(mTopCount < mWindowTopList.size()-1){
            mTopCount++;
        } else {
            mTopCount = 0;
        }
        if( mWindowTopList.size() != 0){
            TruckMediaProtos.CTruckMediaNode truck = mWindowTopList.get(mTopCount);
            String path = Contant.getTruckMetaNodePath(truck.getMediaInfo().getCategoryId(),truck.getMediaInfo().getCategorySubId(), truck.getMediaInfo().getTruckMeta().getTruckFilename()+"/"+truck.getMediaInfo().getTruckMeta().getTruckFilename()+".mp4",true);
            if (!new File(path).exists()) return;
            mTopVideo.stopPlayback();
            mTopVideo.setVideoURI(Uri.parse(path));
            mTopVideo.start();
        }
    }

    private long mWinAdsPlayTime = 0;
    private void minWinAdsStatistics() {
        TruckMediaProtos.CTruckMediaNode truck = mWindowTopList.get(mTopCount);
        String path = Contant.getTruckMetaNodePath(truck.getMediaInfo().getCategoryId(),truck.getMediaInfo().getCategorySubId(), truck.getMediaInfo().getTruckMeta().getTruckFilename()+"/"+truck.getMediaInfo().getTruckMeta().getTruckFilename()+".mp4",true);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(path);
        int duration = Integer.parseInt(mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        long winAdsPlayTimes = System.currentTimeMillis() - mWinAdsPlayTime;
        mWinAdsPlayTime = System.currentTimeMillis();
        if (mWinAdsPlayTime > duration - 1000) {
            Utils.onDemandRecording(truck, mWinAdsPlayTime, duration);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {

            case R.id.videotop:
                startWindowAdActivity(mTopCount, Contant.ADS_WINDOW_ORIENT_TOP);
                break;

            case R.id.imgmid:
              //  startWindowAdActivity(mMiddleCount, Contant.ADS_WINDOW_ORIENT_MIDDLE);
                break;

            case R.id.imgbottom:
              //  startWindowAdActivity(mBottomCount, Contant.ADS_WINDOW_ORIENT_BOTTOM);
                break;

        }
        return false;
    }

    public void onBack(View v){
        finish();
    }
}