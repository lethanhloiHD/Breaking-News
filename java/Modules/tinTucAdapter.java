package Modules;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lethanhloi.testmaps.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by LeThanhLoi on 29/03/2017.
 */
public class tinTucAdapter extends ArrayAdapter<tinTuc>{


    public tinTucAdapter(Context context, int resoure, ArrayList<tinTuc> items) {
        super(context,resoure,items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(convertView == null) {
            LayoutInflater  inflater= LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.tintuc,null);
        }
        tinTuc tinTuc = getItem(position);
        if(tinTuc != null) {
            TextView   txtTitle = (TextView) view.findViewById(R.id.txtTitle);
            TextView   txtDate = (TextView) view.findViewById(R.id.txtdate);
            TextView   txtDiaDiem = (TextView) view.findViewById(R.id.txtDiaDiem);
            ImageView  img = (ImageView) view.findViewById(R.id.imageView);

            Picasso.with(getContext()).load(tinTuc.hinhAnh).into(img);
            txtDiaDiem.setText("Địa điểm: "+tinTuc.diaDiem);
            txtTitle.setText(tinTuc.title);
            txtDate.setText("Thời gian: "+tinTuc.time);
        }
        return view;
    }
}
