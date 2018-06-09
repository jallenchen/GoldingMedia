package com.goldingmedia.mvp.view.fragment;


import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.goldingmedia.GDApplication;
import com.goldingmedia.R;
import com.goldingmedia.contant.Contant;
import com.goldingmedia.goldingcloud.TruckMediaProtos;
import com.goldingmedia.mvp.mode.CHotZone;
import com.goldingmedia.mvp.mode.EventBusCMD;
import com.goldingmedia.temporary.CardManager;
import com.goldingmedia.utils.NToast;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HotZoneFragment extends BaseFragment implements View.OnClickListener{
    private static String TAG = "HotZoneFragment";
    private List<TruckMediaProtos.CTruckMediaNode> truckMediaNodes = new ArrayList<TruckMediaProtos.CTruckMediaNode>();
    private ImageView[] mImgs = new ImageView[4];
    private FrameLayout[] mFraLy = new FrameLayout[4];
    private CHotZone hotZone = new CHotZone();

    public HotZoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_hotzone, container, false);
        mImgs[0] = (ImageView) view.findViewById(R.id.iv_re_1);
        mImgs[1] = (ImageView) view.findViewById(R.id.iv_re_2);
        mImgs[2] = (ImageView) view.findViewById(R.id.iv_re_3);
        mImgs[3] = (ImageView) view.findViewById(R.id.iv_re_4);

        mFraLy[0] = (FrameLayout) view.findViewById(R.id.fl_re_1);
        mFraLy[1] = (FrameLayout) view.findViewById(R.id.fl_re_2);
        mFraLy[2] = (FrameLayout) view.findViewById(R.id.fl_re_3);
        mFraLy[3] = (FrameLayout) view.findViewById(R.id.fl_re_4);
        initData();
        setLisenter();
        return view;
    }

    private void setLisenter(){
        for(FrameLayout fl : mFraLy){
            fl.setOnClickListener(this);
        }
    }

    private void initData(){
        truckMediaNodes = GDApplication.getmInstance().getTruckMedia().getcHotZone().getTruckMediaNodes();
        if(truckMediaNodes == null || truckMediaNodes.size()==0){
            truckMediaNodes = new ArrayList<TruckMediaProtos.CTruckMediaNode>();
            return;
        }
        getPadIndexView(truckMediaNodes);
    }


    private void getPadIndexView(List<TruckMediaProtos.CTruckMediaNode>  truckMediaNodes) {
        for(TruckMediaProtos.CTruckMediaNode truckMediaNode : truckMediaNodes){
            String truckName = truckMediaNode.getMediaInfo().getTruckMeta().getTruckFilename()+"/"+truckMediaNode.getMediaInfo().getTruckMeta().getTruckFilename()+"_l.jpg";
            String imgPath = Contant.HOTZONE_PATH + truckName;
            mImgs[truckMediaNode.getMediaInfo().getTruckMeta().getTruckIndex()-1].setImageBitmap( BitmapFactory.decodeFile(imgPath));
        }

    }

    @Override
    public void onClick(View v) {
        int index = 0;

        switch(v.getId()){
            case R.id.fl_re_1:
                index = 0;
                break;
            case R.id.fl_re_2:
                index = 1;
                break;
            case R.id.fl_re_3:
                index = 2;
                break;
            case R.id.fl_re_4:
                index = 3;
                break;
        }
        TruckMediaProtos.CTruckMediaNode truckMediaNode =  GDApplication.getmInstance().getTruckMedia().getcHotZone().getIndexNode(index+1);
        if (truckMediaNode != null) {
            if(truckMediaNodes.size()<= index){
                index = truckMediaNodes.size() -1;
            }
            CardManager.getInstance().action(index, truckMediaNode, getActivity());
        } else {
            NToast.shortToast(getActivity(), "暂未开放,敬请期待！");
        }
    }

    @Override
    public void OnEventCmd(EventBusCMD cmd) {
        super.OnEventCmd(cmd);
        switch (cmd.getCmdId()){
            case Contant.MsgID.REFLESH_UI:
                initData();
                break;
        }
    }
}
