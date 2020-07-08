package com.kosmo.mukja.review;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kosmo.mukja.R;
import com.kosmo.mukja.content.TabContent2;
import com.kosmo.mukja.review.ReviewItem;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ReviewAdapter extends BaseAdapter {
    private ArrayList<ReviewItem> mItems = new ArrayList<>();
    private ArrayList<String> rv_noList = new ArrayList<>();
    private ArrayList<String> id_List = new ArrayList<>();
    private String myid;

    public ReviewAdapter(String myid){
        this.myid = myid;
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
            convertView = inflater.inflate(R.layout.review_item, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView reviewProfile =  convertView.findViewById(R.id.img_reviewProfile) ;
        TextView reviewId = convertView.findViewById(R.id.tv_reviewId) ;
        ImageView reviewIMG =  convertView.findViewById(R.id.img_reviewIMG) ;
        TextView reviewContent = convertView.findViewById(R.id.tv_reviewContent) ;
        TextView reviewdelete = convertView.findViewById(R.id.reviewdelete);

        Log.i("MyMarker","어뎁터 아이템 길이:"+mItems.size());
/*
        Glide.with(context).load(mItems.get(position).getSnsProfile()).into(snsProfile);
        Glide.with(context).load(mItems.get(position).getSnsContent()).into(snsContent);*/

        if(mItems.get(position).getReviewProfile().length()!=0){
            Picasso.get().load(mItems.get(position).getReviewProfile()).into(reviewProfile);
        }

        if(mItems.get(position).getReviewIMG().length()!=0){
            Picasso.get().load(mItems.get(position).getReviewIMG()).into(reviewIMG);
        }

        reviewId.setText( mItems.get(position).getReviewID());
        reviewContent.setText( mItems.get(position).getReviewContent());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        reviewProfile.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            reviewProfile.setClipToOutline(true);
        }
        reviewdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MyMarker"," myid:"+ myid);
                Log.i("MyMarker"," 작성자 id:"+ id_List.get(position));
                if(id_List.get(position).equals(myid)){
                    Log.i("MyMarker"," 클릭시 발생 rv_no:"+ rv_noList.get(position));
                    //new ReviewDeleteAsyncTask().execute("http://"+ TabContent2.ipAddr +":8080/mukja/Android/DeleteReview.do",rv_noList.get(position));
                    Toast.makeText(context, "삭제하였습니다!", Toast.LENGTH_SHORT).show();
                    mItems.remove(position);
                    notifyDataSetChanged();
                }//if
                else{
                    Toast.makeText(context, "본인의 글만 삭제 가능합니다.", Toast.LENGTH_SHORT).show();
                }
            }//seton
        });

        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String reviewProfile, String reviewId, String reviewIMG, String reviewContent,String rv_no,String id) {
        Log.i("MyMarker"," 어뎁터 추가");
        Log.i("MyMarker"," rv_no:"+rv_no);
        ReviewItem reviewItem = new ReviewItem();
        /* MyItem에 아이템을 setting한다. */
        reviewItem.setReviewProfile(reviewProfile);
        reviewItem.setReviewID(reviewId);
        reviewItem.setReviewIMG(reviewIMG);
        reviewItem.setReviewContent(reviewContent);
        id_List.add(id);
        mItems.add(reviewItem);
        rv_noList.add(rv_no);

    }//addItem

    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class ReviewDeleteAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker","요청주소:"+String.format("%s?rv_no=%s",params[0],params[1]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?rv_no=%s",params[0],params[1]));
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

            Log.i("MyMarker","추천result:"+result);

        }
    }///////////////AsyncTask

}
