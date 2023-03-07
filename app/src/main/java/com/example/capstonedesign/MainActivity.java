package com.example.capstonedesign;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    String userId;
    Bundle bundle;

    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private PostlistFragment postlistFragment;
    private Frag2 frag2;
    private PostFragment postFragment;
    private CrawlingFragment frag4;
    private MypageFragment mypageFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 로그인 Userid를 메인까지 가져온다
        userId = getIntent().getStringExtra("userID");


        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.bottom_navigation_item_home:
                        setFrag(0);
                        break;
                    case R.id.bottom_navigation_item_chat:
                        setFrag(1);
                        break;
                    case R.id.bottom_navigation_item_post:
                        setFrag(2);
                        break;
                    case R.id.bottom_navigation_item_crawling:
                        setFrag(3);
                        break;
                    case R.id.bottom_navigation_item_mypage:
                        setFrag(4);
                        break;
                }
                return true;
            }
        });
        postlistFragment = new PostlistFragment();
        frag2 = new Frag2();
        postFragment = new PostFragment();
        frag4 = new CrawlingFragment();
        mypageFragment = new MypageFragment();
        setFrag(0); // 첫 프래그먼트 화면을 무엇으로 지정해줄 것인지 선택.

    }




    // 프래그먼트 교체가 일어나는 실행문이다.
    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.main_frame, postlistFragment);
                ft.commit();
                //메인 activity의 userid 를 fragment로 전달
                bundle = new Bundle();
                bundle.putString("userID",userId);
                postlistFragment.setArguments(bundle);

                break;
            case 1:
                ft.replace(R.id.main_frame, frag2);
                ft.commit();
                bundle = new Bundle();
                bundle.putString("userID",userId);
                frag2.setArguments(bundle);
                break;
            case 2:
                ft.replace(R.id.main_frame, postFragment);
                ft.commit();
                bundle = new Bundle();
                bundle.putString("userID",userId);
                postFragment.setArguments(bundle);

                break;
            case 3:
                ft.replace(R.id.main_frame, frag4);
                ft.commit();
                break;
            case 4:
                ft.replace(R.id.main_frame, mypageFragment);
                ft.commit();
                bundle = new Bundle();
                bundle.putString("userID",userId);
                mypageFragment.setArguments(bundle);
                break;
        }
    }
}