package com.kosmo.mukja.mypage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kosmo.mukja.R;
import com.kosmo.mukja.content.TabContent2;
import com.kosmo.mukja.review.ReviewItem;
import com.kosmo.mukja.storeinfo.Store_infoActivity;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FallowAdapter extends BaseAdapter {
    private ArrayList<FallowItem> mItems = new ArrayList<>();
    private ArrayList<String> ms_noList = new ArrayList<>();
    private ArrayList<String> ms_storeidList = new ArrayList<>();
    public FallowAdapter(){
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.fallow_item, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView fallow_storeimg =  convertView.findViewById(R.id.fallow_storeimg) ;

        TextView fallow_storename = convertView.findViewById(R.id.fallow_storename) ;
        TextView fallow_postdate = convertView.findViewById(R.id.fallow_postdate) ;
        TextView remove_fallow = convertView.findViewById(R.id.remove_fallow) ;
        LinearLayout fallow_item_layout = convertView.findViewById(R.id.fallow_item_layout);


        Log.i("MyMarker","어뎁터 아이템 길이:"+mItems.size());


        if(mItems.get(position).getImg().length()!=0){
            Picasso.get().load(mItems.get(position).getImg()).into(fallow_storeimg);
        }



        fallow_storename.setText( mItems.get(position).getStore_id());
        fallow_postdate.setText( mItems.get(position).getPostdate());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        fallow_storeimg.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            fallow_storeimg.setClipToOutline(true);
        }
        remove_fallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MyMarker"," 클릭시 발생 ms_no:"+ ms_noList.get(position));
                new FallowDeleteAsyncTask().execute("http://"+ TabContent2.ipAddr +"/mukja/Android/DeleteAndroidFallow.do",ms_noList.get(position));
                Toast.makeText(context, "삭제하였습니다!", Toast.LENGTH_SHORT).show();
                mItems.remove(position);
                notifyDataSetChanged();
            }//seton
        });
        fallow_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String store_id = ms_storeidList.get(position);
                Intent intent = new Intent(context, Store_infoActivity.class);
                intent.putExtra("store_id",store_id);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String ms_no, String ms_storename, String ms_postdate, String ms_img,String ms_storeid) {
        Log.i("MyMarker"," 어뎁터 추가");
        Log.i("MyMarker"," ms_no:"+ms_no);
        FallowItem fallowItem = new FallowItem();
        /* MyItem에 아이템을 setting한다. */
        fallowItem.setStore_id(ms_storename);
        fallowItem.setPostdate(ms_postdate);
        fallowItem.setImg(ms_img);
        mItems.add(fallowItem);
        ms_noList.add(ms_no);
        ms_storeidList.add(ms_storeid);

    }//addItem

    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class FallowDeleteAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker","요청주소:"+String.format("%s?ms_no=%s",params[0],params[1]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?ms_no=%s",params[0],params[1]));
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

            Log.i("MyMarker","찜삭제result:"+result);

        }
    }///////////////AsyncTask

}
