package com.example.capstonedesign;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PostdetailActivity extends AppCompatActivity {


    private String releasedate, userId, destinationUserId;
    private FirebaseDatabase database;
    private TextView textTitle, textUserid, textContents, textRealese, textPrice;
    private ImageView imgPicture;
    private Button chat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_postdetail);
        initView();

        //postlist에서 커스텀 아이템클릭 리스너로 전달된 releasedate, destinationUserId, userid
        releasedate = getIntent().getStringExtra("releasedate");
        destinationUserId = getIntent().getStringExtra("destinationUserId");
        userId = getIntent().getStringExtra("userID");





        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userId.equals(destinationUserId)) {
                    chat.setEnabled(false);
                    Toast.makeText(PostdetailActivity.this,userId +"님의 게시글입니다.",Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(PostdetailActivity.this, ChatActivity.class);
                    intent.putExtra("destinationUserId", destinationUserId);
                    intent.putExtra("userID", userId);
                    ActivityOptions activityOptions = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        activityOptions = ActivityOptions.makeCustomAnimation(PostdetailActivity.this, R.anim.fromright,R.anim.toleft);
                        startActivity(intent,activityOptions.toBundle());
                    }
                }
            }

        });


        //realesedate를 참조하여 그 게시글 속에 있는 각각의 값을 가져옴
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("post").child(releasedate);

        ref.child("title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                Object value = tasksSnapshot.getValue(Object.class);
                textTitle.setText(value.toString());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        ref.child("price").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                Object value = tasksSnapshot.getValue(Object.class);
                textPrice.setText("가격: "+ value.toString() +"원");
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        ref.child("contents").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                Object value = tasksSnapshot.getValue(Object.class);
                textContents.setText(value.toString());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        ref.child("userid").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                Object value = tasksSnapshot.getValue(Object.class);
                textUserid.setText("작성자: "+ value.toString());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
        ref.child("time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                Object value = tasksSnapshot.getValue(Object.class);
                textRealese.setText("작성일: "+ value.toString());
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });
            //이미지 url 따서 피카소로 이미지뷰에 셋팅
        ref.child("imguri").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot tasksSnapshot) {
                Object value = tasksSnapshot.getValue(Object.class);
                Picasso.get().load(value.toString()).resize(392,392)
                        .into(imgPicture);
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) {
            }
        });

    }

    private void initView() {
        textTitle = (TextView) findViewById(R.id.textView_title3);
        textUserid = (TextView) findViewById(R.id.textView_userid3);
        textContents = (TextView) findViewById(R.id.textView_contents3);
        textRealese = (TextView) findViewById(R.id.textView_release3);
        textPrice = (TextView) findViewById(R.id.textView_price3);
        imgPicture = (ImageView) findViewById(R.id.imageView_image3);
        chat = (Button) findViewById(R.id.btn_gochat);
    }

}