package com.goldingmedia.mvp.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.goldingmedia.GDApplication;
import com.goldingmedia.R;
import com.goldingmedia.contant.Contant;
import com.goldingmedia.goldingcloud.TruckMediaProtos;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jallen on 2017/7/29 0029 13:47.
 */

public class GameGirdAdapter extends BaseAdapter {
    private Context mContext;
    List<TruckMediaProtos.CTruckMediaNode> mTrucks = new ArrayList<TruckMediaProtos.CTruckMediaNode>();
    public GameGirdAdapter(Context ct){
        mContext = ct;
    }
    public void refresh(List<TruckMediaProtos.CTruckMediaNode> trucks){
        if (trucks == null){
            return;
        }
        mTrucks = trucks;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mTrucks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        TruckMediaProtos.CTruckMediaNode truckMediaNode = mTrucks.get(position);
        String imgPath = Contant.getTruckMetaNodePath(4,2,truckMediaNode.getMediaInfo().getTruckMeta().getTruckFilename(),true);
        String imgName = truckMediaNode.getMediaInfo().getTruckMeta().getTruckImage();
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_game, parent,false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageBitmap( BitmapFactory.decodeFile(imgPath+"/"+imgName));

        return convertView;
    }

       class ViewHolder{
        ImageView imageView;
    }
}
