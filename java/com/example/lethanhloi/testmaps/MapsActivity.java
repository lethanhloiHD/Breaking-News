package com.example.lethanhloi.testmaps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import Modules.DirectionFinder;
import Modules.DirectionFinderListener;
import Modules.Location;
import Modules.Route;
import Modules.tinTuc;
import Modules.tinTucAdapter;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener,
        LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMyLocationButtonClickListener, TabLayout.OnTabSelectedListener {


    private GoogleMap mMap;
    private GoogleApiClient mApiClient = null;
    private final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Spinner spinner1, spinner2,spinner3,spinner4,spinnerTrangtin,spinnerKhoaVien,spinnerToaNha,spinnerTTDD;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Button btnFindPath,btnFindToaNha,btnFindKhoaVien,btn1,btn2,btnGPS,btnRadius,btnXemKV,btnXemTN,btnTTKV,btnTTDD,btnXemWebsite,btnNow;
    private EditText radius;

    private TabLayout tabLayout;
    private ViewAnimator animator;
    private LinearLayout findPath, findLo,findRadius,LocalAndFeed;
    private boolean connected = false;
    ArrayList<Location> listLocation = new ArrayList<>(50);
    ArrayList<Location> listKhoaVien = new ArrayList<>(50);
    ArrayList<Location> listStart = new ArrayList<>(50);// chua vi tri diem bat dau
    ArrayList<Location> listTrangTin = new ArrayList<>(50);

    HashMap<String , String > HashMapKhoaVien = new HashMap<String ,String>() ;
    HashMap<String , String > HashMapToaNha = new HashMap<String , String>() ;
    HashMap<String , String > HashMapTTKV = new HashMap<String ,String>() ;
    HashMap<String , String > HashMapTTTN = new HashMap<String , String>() ;
    HashMap<String , String > HashMapWebsite = new HashMap<String , String>() ;


    private Marker marker = null;
    LatLng myLocation;


    ArrayList<String> listLo = new ArrayList<>();
    Context context;
    ListView listViewLocationRadius;
    ArrayAdapter<String> adapterRadius;

    ListView listViewTinTuc;
    ListView listViewTinTucMap;
    ArrayList<tinTuc> listTinTuc;
    ArrayList<tinTuc> listTinTuc1;
    ArrayList<tinTuc> listTinTucMap;
    tinTucAdapter tinTucAdapter;
    tinTucAdapter tinTucAdapter1;
    tinTucAdapter tinTucAdapterMap;
    DatabaseReference   myRef;
    DatabaseReference   myRefMap;

    TextView txtView;
    TextView txtWeb;
    float r=10;
    boolean editTextChanged=false,circle= false;
    Circle mapCircle;
    ArrayList<tinTuc> listTT;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myRef = FirebaseDatabase.getInstance().getReference();
        myRefMap = FirebaseDatabase.getInstance().getReference();
        context=this;
        listTT= new ArrayList<tinTuc>();

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setOnTabSelectedListener(this);
        animator = (ViewAnimator) findViewById(R.id.animator);
        btnFindPath = (Button) findViewById(R.id.btnFindPath);
        btn1 = (Button) findViewById(R.id.btnPath);
        btn2 = (Button) findViewById(R.id.btnFindLocation);
        btnGPS=(Button) findViewById(R.id.myLocation) ;
        btnFindToaNha = (Button) findViewById(R.id.btnTimKiemToaNha);
        btnFindKhoaVien=(Button)findViewById(R.id.btnTimKiemKhoaVien) ;
        btnXemKV=(Button)findViewById(R.id.btnDocKV);
        btnXemTN=(Button)findViewById(R.id.btnDocTN);
        btnTTKV=(Button)findViewById(R.id.btnTTKV);
        btnTTDD=(Button)findViewById(R.id.btnTTDD);
        btnNow=(Button)findViewById(R.id.btnNow);
        btnRadius=(Button)findViewById(R.id.btnRadius);



        txtView=(TextView)findViewById(R.id.textView);
        txtWeb=(TextView)findViewById(R.id.infoTxtCredits);

        radius=(EditText)findViewById(R.id.edRadius);







        findPath = (LinearLayout) findViewById(R.id.find_path);
        findLo = (LinearLayout) findViewById(R.id.find_location);
        findRadius=(LinearLayout)findViewById(R.id.circle);
        LocalAndFeed=(LinearLayout)findViewById(R.id.LocalAndFeed);

        spinnerTrangtin=(Spinner)findViewById(R.id.spTrangtin);
        spinnerTTDD=(Spinner)findViewById(R.id.spinnerTrangTinDD);
        spinnerToaNha=(Spinner)findViewById(R.id.spinnerTN);
        spinnerKhoaVien=(Spinner)findViewById(R.id.spinnerKV);
        spinner1 = (Spinner) findViewById(R.id.sp1);
        spinner2 = (Spinner) findViewById(R.id.sp2);
        spinner3 = (Spinner) findViewById(R.id.spToaNha);
        spinner4 = (Spinner) findViewById(R.id.spKhoaVien);


        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        btnFindToaNha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationToaNha();
                if (originMarkers != null) {
                    for (Marker marker : originMarkers) {
                        marker.remove();
                    }
                }

                if (destinationMarkers != null) {
                    for (Marker marker : destinationMarkers) {
                        marker.remove();
                    }
                }

                if (polylinePaths != null) {
                    for (Polyline polyline:polylinePaths ) {
                        polyline.remove();
                    }
                }
            }

        });
        btnFindKhoaVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocationKhoaVien();
                if (originMarkers != null) {
                    for (Marker marker : originMarkers) {
                        marker.remove();
                    }
                }

                if (destinationMarkers != null) {
                    for (Marker marker : destinationMarkers) {
                        marker.remove();
                    }
                }

                if (polylinePaths != null) {
                    for (Polyline polyline:polylinePaths ) {
                        polyline.remove();
                    }
                }
            }


        });

        btnXemKV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readInfoKV();
                String t = spinnerKhoaVien.getSelectedItem().toString();
                String t1= HashMapWebsite.get(t);
                txtWeb.setText(t1);

            }
        });

        btnXemTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readInfoTN();
            }
        });
        btnTTKV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tenKV = spinnerTrangtin.getSelectedItem().toString();
                getFirebase("TinTuc",HashMapTTKV.get(tenKV));
            }
        });
        btnTTDD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tenTN = spinnerTTDD.getSelectedItem().toString();
                getFirebase("SuKien",tenTN);
            }
        });
        btnNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFeed();
            }
        });

        listLocation = Location.getListFromJSON(this,R.raw.toa_do);
        listStart = Location.getListFromJSON(this,R.raw.toa_do);
        listKhoaVien= Location.getListFromJSON(this,R.raw.khoa_vien);
        listTrangTin=Location.getListFromJSON(this,R.raw.khoa_vien);
        listViewLocationRadius=(ListView)findViewById(R.id.listLoRadius);
        listTrangTin.add(new Location("Đại Học Bách Khoa Hà Nội",21.006410, 105.843137,"toantruong"));

        addItemsOnSpinner();

        if (mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        addToHashMap();
        getFirebase("TinTuc","toantruong");
    }




    public boolean getChangeEditText(){

        radius.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                editTextChanged = true;
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        return editTextChanged;

    }

    public void getFeed(){


        listTinTuc1= new ArrayList<tinTuc>();

        for(tinTuc tt: listTinTuc) {
            String [] dates = new  String[2];
            dates = tt.time.split("->");
             if((CompareTo(dates[0]))&&(!CompareTo(dates[1]))){
                listTinTuc1.add(tt);
            }
              }
        tinTucAdapter1 = new tinTucAdapter(MapsActivity.this,R.layout.tintuc,listTinTuc1);
        listViewTinTuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MapsActivity.this,TinTucActivity.class);
                intent.putExtra("Link",listTinTuc1.get(i).link);
                startActivity(intent);
            }
        });
        tinTucAdapter1.notifyDataSetChanged();
        listViewTinTuc.setAdapter(tinTucAdapter1);

    }

// so sánh thời gian
    public Boolean CompareTo(String str2) {

        Date date2=null;
        Date now = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("HH:mm - dd/MM/yyyy");
        try {
            date2=ft.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Boolean t = date2.before(now);
        return t;
    }

    // dua du lieu vao cac hashmap
    public void addToHashMap() {
        for(int i=0;i<listTrangTin.size();i++){
            HashMapKhoaVien.put(listTrangTin.get(i).getTen(),listTrangTin.get(i).getThongTin());
        }

        for(int i=0;i<listLocation.size();i++){
            HashMapToaNha.put(listLocation.get(i).getTen(),listLocation.get(i).getThongTin());
        }

        for(int i=0;i<listTrangTin.size();i++){
            HashMapTTKV.put(listTrangTin.get(i).getTen(),listTrangTin.get(i).getThongTin());
        }


        for(int i=0;i<listLocation.size();i++){
            HashMapTTTN.put(listLocation.get(i).getTen(),listLocation.get(i).getThongTin());
        }

        String [] web = {"soict.hust.edu.vn","set.hust.edu.vn","sbft.hust.edu.vn","hust.edu.vn"};
        for(int i=0;i<listTrangTin.size();i++){
            HashMapWebsite.put(listTrangTin.get(i).getTen(),web[i]);
        }

    }
    // doc thong tin khoa vien
    public void readInfoKV() {

        String tenKV = spinnerKhoaVien.getSelectedItem().toString();
        readFile(HashMapKhoaVien.get(tenKV));

    }
    // doc thong tin các tòa nhà
    public void readInfoTN() {

        String tenTN = spinnerToaNha.getSelectedItem().toString();
        readFile(HashMapToaNha.get(tenTN));

    }
// ham doc file
    public void readFile(String filename)
    {
        try {
            InputStream in = this.getResources().openRawResource(getResources().getIdentifier( filename , "raw" , getPackageName() ));
            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)  {
                  sb.append(s).append("\n");
            }
            this.txtView.setText(sb.toString());

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
// lay du lieu trne firebase
    public void getFirebase(String str1,String str2){

        listViewTinTuc=(ListView) findViewById(R.id.list_tintuc);
        listTinTuc= new ArrayList<tinTuc>();
        tinTucAdapter = new tinTucAdapter(MapsActivity.this,R.layout.tintuc,listTinTuc);
        listViewTinTuc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MapsActivity.this,TinTucActivity.class);
                intent.putExtra("Link",listTinTuc.get(i).link);
                startActivity(intent);
            }
        });


        myRef.child(str1).child(str2).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                tinTuc tt = dataSnapshot.getValue(tinTuc.class);
                listTinTuc.add(tt);
                tinTucAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewTinTuc.setAdapter(tinTucAdapter);


    }

// them marker tren map

    public  void addMarkerOnMap(ArrayList<Location> list) {
            for (int i =0; i < list.size();i++) {
                LatLng dhbkhanoi = new LatLng(list.get(i).getViDo(), list.get(i).getKinhDo());
                Marker bkhn = mMap.addMarker(new MarkerOptions()
                        .position(dhbkhanoi)
                        .title(list.get(i).getTen())
                        .snippet("Đại học BKHN")
                );
                bkhn.showInfoWindow();
            }
    }

    @Override
    protected void onStart() {
        mApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mApiClient.disconnect();
        super.onStop();
    }
    // lay vị tri khi tim kiem khoa vien
    public void getLocationKhoaVien(){
        Location khoaVien = (Location) spinner4.getSelectedItem();
        LatLng khoaVi = new LatLng(khoaVien.getViDo(),khoaVien.getKinhDo());
        Marker kv = mMap.addMarker(new MarkerOptions()
                .position(khoaVi)
                .title(khoaVien.getTen())
                .snippet("Đại học BKHN")
        );
        kv.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(khoaVi,17));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
 // lay vi tri khi tim kiem toa nhà
    public void getLocationToaNha() {

        Location diaDiem = (Location)spinner3.getSelectedItem();
        LatLng dhbkhanoi = new LatLng(diaDiem.getViDo(), diaDiem.getKinhDo());

        Marker bkhn = mMap.addMarker(new MarkerOptions()
                .position(dhbkhanoi)
                .title(diaDiem.getTen())
                .snippet("Đại học BKHN")
                );
        bkhn.showInfoWindow();
        if(marker != null){
            marker.remove();
            marker = null;
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dhbkhanoi,17));

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
// chuyen doi giữa các view tim kiem
    public void toggleFind(View view){
        if(view == findViewById(R.id.btnPath)) {
            if(findPath.getVisibility()== View.GONE) {
                findPath.setVisibility(View.VISIBLE);
                findLo.setVisibility(View.GONE);
                findRadius.setVisibility(View.GONE);
                if(circle) { mapCircle.remove();}
            }
            else findPath.setVisibility(View.GONE);

        }
        else if(view == findViewById(R.id.btnFindLocation)) {
            if(findLo.getVisibility()== View.GONE) {
                findLo.setVisibility(View.VISIBLE);
                findPath.setVisibility(View.GONE);
                findRadius.setVisibility(View.GONE);
                if(circle) { mapCircle.remove();}
            }
            else findLo.setVisibility(View.GONE);
        }

    }
// them du lieu vào cac spinner
    public void addItemsOnSpinner() {


// Tim kiem dương di
        ArrayAdapter<Location> dataAdapterStart = new ArrayAdapter<Location>(this,
                android.R.layout.simple_spinner_item, listStart);
        // tim kiesm dia diem toa nha
        ArrayAdapter<Location> dataAdapterLocation = new ArrayAdapter<Location>(this,
                android.R.layout.simple_spinner_item, listLocation);
        // tim kiem dia diem khoa vien
        ArrayAdapter<Location> dataAdapterKhoaVien = new ArrayAdapter<Location>(this,
                android.R.layout.simple_spinner_item, listKhoaVien);
        // trang tin tuc
        ArrayAdapter<Location> dataAdapterTrangTin = new ArrayAdapter<Location>(this,
                android.R.layout.simple_spinner_item, listTrangTin);


// map
        dataAdapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(dataAdapterLocation);

        dataAdapterStart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapterStart);

        dataAdapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(dataAdapterLocation);

        dataAdapterKhoaVien.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(dataAdapterKhoaVien);
// thong tin
        dataAdapterTrangTin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerKhoaVien.setAdapter(dataAdapterTrangTin);

        dataAdapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerToaNha.setAdapter(dataAdapterLocation);
//       // tin tuc
        dataAdapterTrangTin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTrangtin.setAdapter(dataAdapterTrangTin);


        dataAdapterLocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTTDD.setAdapter(dataAdapterLocation);

    }
// gui yeu cau tim kiem
    private void sendRequest() {
            Location d1 = (Location)spinner1.getSelectedItem();
            Location d2 = (Location)spinner2.getSelectedItem();
            String origin = d1.getViDo() + "," + d1.getKinhDo();
            String destination =d2.getViDo() + "," + d2.getKinhDo();
            if (origin.isEmpty()) {
                Toast.makeText(this, "Please choose origin address!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (destination.isEmpty()) {
                Toast.makeText(this, "Please choose destination address!", Toast.LENGTH_SHORT).show();
                return;
            }
//
            try {
                new DirectionFinder(this, origin, destination).execute();

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMyLocationButtonClickListener(this);
        LatLng dhbkhanoi = new LatLng(21.006221, 105.843165);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dhbkhanoi,17));
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setLatLngBoundsForCameraTarget(new LatLngBounds(new LatLng(21.003409, 105.841468),new LatLng(21.008155, 105.847581)));
        addMarkerOnMap(listLocation);

        btnGPS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(findRadius.getVisibility()== View.GONE) {
                    findRadius.setVisibility(View.VISIBLE);
                    findPath.setVisibility(View.GONE);
                    findLo.setVisibility(View.GONE);
                }
                else findRadius.setVisibility(View.GONE);
                Marker gps = mMap.addMarker(new MarkerOptions()
                        .position(myLocation)
                        .title("Vị trí của tôi")
                );
                gps.showInfoWindow();

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation,18));



            }

        });

        btnRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(findRadius.getVisibility() == View.VISIBLE) {
                    LocalAndFeed.setVisibility(View.VISIBLE);
                }
                else {
                    LocalAndFeed.setVisibility(View.GONE);
                }
                String r1 = radius.getText().toString();
                r = Float.parseFloat(r1);

               if(getChangeEditText()){
                   mapCircle.remove();
               }

                listLo= getLo(r);

                if(drawCircle(myLocation,r)){
                    circle = true;
                }

                adapterRadius = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, listLo);
                listViewLocationRadius.setAdapter(adapterRadius);

                for (String item : listLo ) {
                    getFirebaseToMap("SuKien", item);
                }
            }
        });



    }
    //lay tin tuc do len map
    public void getFirebaseToMap(String str1,String str2){

        listViewTinTucMap=(ListView) findViewById(R.id.listViewSuKien);
        listTinTucMap= new ArrayList<tinTuc>();
        tinTucAdapterMap = new tinTucAdapter(MapsActivity.this,R.layout.tintuc,listTinTucMap);
        listViewTinTucMap.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MapsActivity.this,TinTucActivity.class);
                intent.putExtra("Link",listTinTucMap.get(i).link);
                startActivity(intent);
            }
        });


        myRefMap.child(str1).child(str2).addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                tinTuc tt = dataSnapshot.getValue(tinTuc.class);
                    String [] dates = new  String[2];
                    dates = tt.time.split("->");
                    if((CompareTo(dates[0]))&&(!CompareTo(dates[1]))){
                        listTinTucMap.add(tt);

                }
                tinTucAdapterMap.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listViewTinTucMap.setAdapter(tinTucAdapterMap);


    }


// lay cac toa nhà do len map
    private ArrayList<String> getLo (double ra) {
        ArrayList<String> list = new ArrayList<>(100);
        for(Location l : listLocation) {

            if((float) Math.sqrt(Math.pow((103793.113)*Math.abs(myLocation.longitude-l.getKinhDo()),2)
                    +Math.pow(111180 * Math.abs(myLocation.latitude-l.getViDo()),2))<= (ra)){
                list.add(l.getTen());
            }
        }

        return list;
    }
// ve vong tron ban kinh
    private boolean drawCircle(LatLng point,float r){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(point);
        circleOptions.radius(r);
        circleOptions.strokeColor(0x550000FF);
        circleOptions.fillColor(0x30ff0000);
        circleOptions.strokeWidth(2);
        mapCircle = mMap.addCircle(circleOptions);
        return true;
    }
// bat dau khi tim kiem
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }
// sau khi tim kiem xong
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 18));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
       // Toast.makeText(this, "GPS của bạn đã được xác định !", Toast.LENGTH_LONG).show();
        connected = true;
        LocationRequest locationRequest ;
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);

        // Ask for runtime permission
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mApiClient, locationRequest, this);
        }


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this, "False", Toast.LENGTH_SHORT).show();
        return;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onConnected(null);
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
       // Toast.makeText(this,"Chào mừng bạn đã đến Đại học Bách Khoa Hà Nội !", Toast.LENGTH_LONG).show();
        LocationServices.FusedLocationApi.removeLocationUpdates(mApiClient, this);
        myLocation = new LatLng(location.getLatitude(),location.getLongitude());
        Location myLo = new Location("Vị trí của tôi",myLocation.latitude,myLocation.longitude);
        listStart.add(0,myLo);

    }

    @Override
    public boolean onMyLocationButtonClick() {
        if (connected){
            onConnected(null);
        }
        return true;
    }
// chuyen doi giữa các tab
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int index = tab.getPosition();
        switch (index){
            case 0:

                if(animator.getDisplayedChild() == 1) {
                    animator.showPrevious();
                }
                else if (animator.getDisplayedChild() == 2) {
                    animator.showNext();
                }
               // Toast.makeText(this,String.valueOf(index),Toast.LENGTH_LONG).show();
                break;
            case 1:
                if(animator.getDisplayedChild() == 0) {
                    animator.showNext();
                }
                else if (animator.getDisplayedChild() == 2) {
                    animator.showPrevious();
                }

              // Toast.makeText(this,String.valueOf(index),Toast.LENGTH_LONG).show();
                break;
            case 2:
                if(animator.getDisplayedChild() == 0) {
                    animator.showPrevious();
                }
                else if (animator.getDisplayedChild() == 1) {
                    animator.showNext();
                }
               // Toast.makeText(this,String.valueOf(index),Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}

