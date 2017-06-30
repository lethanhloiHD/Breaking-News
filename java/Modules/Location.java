package Modules;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by LeThanhLoi on 16/03/2017.
 */
public class Location {
    private String Ten;
    private double ViDo;
    private double KinhDo;
    private String ThongTin;

    public String getThongTin() {
        return ThongTin;
    }

    public void setThongTin(String thongTin) {
        ThongTin = thongTin;
    }


    public double getKinhDo() {
        return KinhDo;
    }

    public void setKinhDo(double kinhDo) {
        KinhDo = kinhDo;
    }

    public double getViDo() {
        return ViDo;
    }

    public void setViDo(double viDo) {
        ViDo = viDo;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public Location(String ten, double viDo, double kinhDo) {
        Ten = ten;
        ViDo = viDo;
        KinhDo = kinhDo;
    }
    public Location(String ten, double viDo, double kinhDo,String tt) {
        Ten = ten;
        ViDo = viDo;
        KinhDo = kinhDo;
        ThongTin=tt;
    }
    @Override
    public boolean equals(Object obj) {
        if (! (obj instanceof Location)) return false;
        Location d = (Location)obj;
        return this.getTen().equals(d.getTen());
    }

    @Override
    public String toString() {
        return getTen();
    }

    public static ArrayList<Location> getListFromJSON (Context context, int resId) {
        ArrayList<Location> list = new ArrayList<>(50);

        InputStream is = context.getResources().openRawResource(resId);
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
        String s= null;
        StringBuilder sb= new StringBuilder();
        try {
            while((  s = br.readLine())!=null) {
                sb.append(s);
                sb.append("\n");
            }
            String jsonText = sb.toString();
            JSONObject jsonRoot = new JSONObject(jsonText);

            JSONArray jsonArray = jsonRoot.getJSONArray("ToaNha");
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject js = jsonArray.getJSONObject(i);
                double vd = js.getDouble("ViDo");
                double kd = js.getDouble("KinhDo");
                String ten = js.getString("Ten");
                String tt=js.getString("ThongTin");
//                String ws=js.getString("Website");
                list.add(new Location(ten,vd,kd,tt));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return list;
    }



}
