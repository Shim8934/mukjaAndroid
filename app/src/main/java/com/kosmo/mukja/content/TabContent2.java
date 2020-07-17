package com.kosmo.mukja.content;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.mapsub.AddrList;

import com.kosmo.mukja.FilterActivity;
import com.kosmo.mukja.R;
import com.kosmo.mukja.fcm.EroomlistActivity;
import com.kosmo.mukja.mapsub.RealTimeTableInfo_Activity;
import com.kosmo.mukja.storeinfo.Store_infoActivity;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent2 extends Fragment   implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private static final int RESULT_OK = 2000 ;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private Button filter,searcher,store_info,reatime_table_info,eroom_list,btn_x;
    private String bukdonglat,namsualat,bukdonglng,namsualng;
    private Context context;
    private List markerList = new ArrayList();
    private BottomSheetBehavior sheetBehavior;
    private FrameLayout bottom_sheet;
    private ImageButton btn_searchaddr;
    private EditText edit_addr;
    private TextView store_name, store_addr,store_intro,store_time;

    private String[] check_avoid= {"FS","EG","MK","BD","PK","CW","PE","SF","DP","FL","SB"};
    private	String[] check_prefer= {"CS","JS","HS","BS","YS"};
    private String query;
    private Bundle avoid_codes = new Bundle();
    private Bundle prefer_codes = new Bundle();
    private String store_id;
    public static final String ipAddr="115.91.88.230:9998";
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    //2]onCreateView()오버 라이딩
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabmenu2_layout,null,false);

        FragmentManager fm = getChildFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        if(prefer_codes.size()==0) {
            Log.i("MyMarker","선호 번들 크기 0");
            for (String item : check_prefer) {
                prefer_codes.putString(item, item);
            }
        }
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        Log.i("MyMarker",prefer_codes.get("CS")+"/"+prefer_codes.get("JS")+"/"+prefer_codes.get("HS")+"/"+prefer_codes.get("BS")+"/"+prefer_codes.get("YS"));


        filter = view.findViewById(R.id.filter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FilterActivity.class);
                Log.i("MyMarker","가기전 기피코드"+avoid_codes.size());
                Log.i("MyMarker","가기전 선호코드"+prefer_codes.size());
                intent.putExtra("avoid_codes",avoid_codes);
                intent.putExtra("prefer_codes",prefer_codes);
                startActivityForResult(intent,3000);
            }
        });//filter


        searcher=view.findViewById(R.id.btn_searcher);
        searcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searcher.setVisibility(View.GONE);
                query="";
                Log.i("MyMarker","bukdonglat :"+bukdonglat +", bukdonglng  :"+bukdonglng +", namsualat  :"+namsualat+", namsualng  :"+namsualng);
                for (String mapkey : avoid_codes.keySet()){
                    Log.i("MyMarker","쿼리에 더할때 key:"+mapkey+" val:"+avoid_codes.get(mapkey));
                    query +=mapkey+'='+avoid_codes.get(mapkey).toString()+"&";
                }
                for (String mapkey : prefer_codes.keySet()){
                    query +=mapkey+'='+prefer_codes.get(mapkey).toString()+"&";
                }
                Log.i("MyMarker",query);
                new SearchMarkerAsyncTask().execute("http://"+ipAddr+"/mukja/getMarker.pbs",bukdonglat,bukdonglng,namsualat,namsualng,query);
            }
        });//searcher

        bottom_sheet = view.findViewById(R.id.standardBottomSheet);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        store_info= view.findViewById(R.id.store_info);
        reatime_table_info = view.findViewById(R.id.reatime_table_info);
        eroom_list = view.findViewById(R.id.eroom_list);
        store_name = view.findViewById(R.id.store_name);
        store_addr = view.findViewById(R.id.store_addr);
        store_intro = view.findViewById(R.id.store_intro);
        store_time = view.findViewById(R.id.store_time);

        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        btn_x=view.findViewById(R.id.btn_x);
        btn_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        edit_addr= view.findViewById(R.id.edit_addr);
        btn_searchaddr = view.findViewById(R.id.btn_searchaddr);
        btn_searchaddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_addr.getText().toString().trim().equals("")){
                    Toast.makeText(context, "검색어를 입력하세요!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent addrIntent = new Intent(context, AddrList.class);
                addrIntent.putExtra("search_text",edit_addr.getText().toString().trim());

                startActivityForResult(addrIntent,3000);
            }
        });

        reatime_table_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent realtim_intent = new Intent(context, RealTimeTableInfo_Activity.class);
                realtim_intent.putExtra("store_id",store_id);
                startActivityForResult(realtim_intent,3000);
            }
        });

        eroom_list=view.findViewById(R.id.eroom_list);
        eroom_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent eroom = new Intent(context,EroomlistActivity.class);
                eroom.putExtra("store_id",store_id);
                startActivityForResult(eroom,3000);
            }
        });


        store_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent store_info_intent = new Intent(context, Store_infoActivity.class);

                store_info_intent.putExtra("store_id",store_id);
                Log.i("MyMarker","스토어 인포가기전 store_id:"+store_id);
                startActivityForResult(store_info_intent,3000);
            }
        });
        return view;
    }//oncerate


    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class SearchMarkerAsyncTask extends AsyncTask<String,Void,String> {

        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setIcon(android.R.drawable.btn_star_big_on);
            builder.setTitle("로딩");
            builder.setMessage("음식점을 검색 중입니다.");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();
        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();



            try {
                Log.i("MyMarker","쿼리보내기전 :"+avoid_codes.get("BD")+"/"+avoid_codes.get("CW")+"/"+avoid_codes.get("DP")+"/"+avoid_codes.get("EG")+"/"+avoid_codes.get("FL")
                        +"/"+avoid_codes.get("MK")+"/"+avoid_codes.get("PE")+"/"+avoid_codes.get("PK")+"/"+avoid_codes.get("SB")+"/"+avoid_codes.get("SF"));

                URL url = new URL(String.format("%s?bukdonglat=%s&bukdonglng=%s&namsualat=%s&namsualng=%s&%s",params[0],params[1],params[2],params[3],params[4],params[5]));
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                //서버에 요청 및 응답코드 받기
                int responseCode=conn.getResponseCode();
                if(responseCode ==HttpURLConnection.HTTP_OK){
                    //연결된 커넥션에서 서버에서 보낸 데이타 읽기
                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    String line;
                    while((line=br.readLine())!=null){
                        buf.append(line);
                    }
                    br.close();
                }
            }
            catch(Exception e){e.printStackTrace();}

            SystemClock.sleep(2000);
            return buf.toString();
        }///////////doInBackground

        @Override
        protected void onPostExecute(String result) {
            if(markerList.size()!=0){
                removeMarker(markerList);
            }

            JsonParser jsonParser = new JsonParser();
            JsonArray markers = (JsonArray) jsonParser.parse(result);
            Log.i("MyMarker",markers.toString());
            for(int i=0;i<markers.size();i++){
                JsonObject markerInfo=(JsonObject)markers.get(i);
                Log.i("MyMarker","markerInfo"+markerInfo.toString());

                Marker marker = new Marker();
                marker.setPosition(new LatLng(markerInfo.get("store_lat").getAsDouble(),markerInfo.get("store_lng").getAsDouble()));
                marker.setMap(naverMap);
                marker.setIcon(OverlayImage.fromResource(R.drawable.marker));
                marker.setCaptionText(markerInfo.get("store_name").getAsString());
                marker.setWidth(120);
                marker.setHeight(120);
                marker.setOnClickListener(new Overlay.OnClickListener() {
                    @Override
                    public boolean onClick(@NonNull Overlay overlay) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        store_id=markerInfo.get("store_id").toString();
                        store_name.setText(markerInfo.get("store_name").toString().replaceAll("\"",""));
                        store_addr.setText(markerInfo.get("store_addr").toString().replaceAll("\"",""));
                        store_intro.setText(
                                markerInfo.get("store_intro").toString()
                                        .replaceAll("\"","")
                                        .replaceAll("<p>","")
                                        .replaceAll("</p>","")
                                        .replaceAll("r","")
                                        .replaceAll("n","")
                                        .replace("\\","")
                                        .replace("  ","")
                        );
                        store_time.setText(
                                markerInfo.get("store_time").toString()
                                        .replaceAll("\"","")
                                        .replaceAll("r","")
                                        .replaceAll("n","")
                                        .replace("\\","")
                                        .replace("  ","")
                        );
                        return false;
                    }
                });
                markerList.add(marker);
            }
            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask





    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        uiSettings.setCompassEnabled(false);

        this.naverMap = naverMap;
        LatLngBounds latLngBounds = naverMap.getContentBounds();
        naverMap.addOnCameraIdleListener(new NaverMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                LatLngBounds latLngBounds = naverMap.getContentBounds();
                bukdonglat = String.valueOf(latLngBounds.getNorthEast().toLatLng().latitude);
                bukdonglng = String.valueOf(latLngBounds.getNorthEast().toLatLng().longitude);
                namsualat = String.valueOf(latLngBounds.getSouthWest().toLatLng().latitude);
                namsualng = String.valueOf(latLngBounds.getSouthWest().toLatLng().longitude);
                Log.i("MyMarker",bukdonglat+"/"+bukdonglng+"/"+namsualat+"/"+namsualng);
                System.out.println(bukdonglat+"/"+bukdonglng+"/"+namsualat+"/"+namsualng);
                searcher.setVisibility(View.VISIBLE);

            }
        });



        bukdonglat = String.valueOf(latLngBounds.getNorthEast().toLatLng().latitude);
        bukdonglng = String.valueOf(latLngBounds.getNorthEast().toLatLng().longitude);
        namsualat = String.valueOf(latLngBounds.getSouthWest().toLatLng().latitude);
        namsualng = String.valueOf(latLngBounds.getSouthWest().toLatLng().longitude);


        naverMap.setLocationSource(locationSource);



    }

    private void removeMarker(List markerList){

        for(Object marker:markerList){
            ((Marker)marker).setMap(null);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MyMarker","resultCode:"+resultCode);
        if(resultCode == -1){
            this.avoid_codes = data.getBundleExtra("avoid_codes");
            Log.i("MyMarker","resultCode"+resultCode);
            this.prefer_codes = data.getBundleExtra("prefer_codes");
            Log.i("MyMarker",prefer_codes.get("CS")+"/"+prefer_codes.get("JS")+"/"+prefer_codes.get("HS")+"/"+prefer_codes.get("BS")+"/"+prefer_codes.get("YS"));
            Log.i("MyMarker",avoid_codes.get("BD")+"/"+avoid_codes.get("CW")+"/"+avoid_codes.get("DP")+"/"+avoid_codes.get("EG")+"/"+avoid_codes.get("FL")
                    +"/"+avoid_codes.get("MK")+"/"+avoid_codes.get("PE")+"/"+avoid_codes.get("PK")+"/"+avoid_codes.get("SB")+"/"+avoid_codes.get("SF"));

        }else if(resultCode == 1){
            Log.i("MyMarker","resultCode:"+resultCode);
            Log.i("MyMarker","data:"+data);
            if(data==null){
                return;
            }
            Double lat= data.getDoubleExtra("lat", 10.1234567);
            Double lng= data.getDoubleExtra("lng", 100.1234567);
            Log.i("MyMarker","lat:"+lat+" lng:"+lng);

            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(lat, lng))
                    .animate(CameraAnimation.Easing);
            naverMap.moveCamera(cameraUpdate);
        }
    }

}
