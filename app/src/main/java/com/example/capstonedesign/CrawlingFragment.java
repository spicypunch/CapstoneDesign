package com.example.capstonedesign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.os.AsyncTask;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CrawlingFragment extends Fragment {


    private RecyclerView recyclerView;
    private ArrayList<CrawlingItemObject> list = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crawling, container, false);


        recyclerView = view.findViewById(R.id.recyclerview);
        //AsyncTask 작동시킴(파싱)
        new CrawlingFragment.Description().execute();


        return view;
    }

    private class Description extends AsyncTask<Void, Void, Void> {


        //진행바표시 삭제함 >> 살리려면 백업1차 crawl 참고

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect("https://www.aladin.co.kr/shop/common/wbest.aspx?BestType=Bestseller&BranchType=1&CID=8262").get();
                Elements mElementDataSize = doc.select("div[class=ss_book_box]"); //필요한 녀석만 꼬집어서 지정
                int mElementSize = mElementDataSize.size(); //목록이 몇개인지 알아낸다. 그만큼 루프를 돌려야 하나깐.

                for(Element elem : mElementDataSize){ //이렇게 요긴한 기능이
                    //영화목록 <li> 에서 다시 원하는 데이터를 추출해 낸다.
                    String my_title = elem.select("div[class=ss_book_list] ul li a b").text();
                    String my_link = elem.select("a[class=bo3]").attr("href");
                    String my_imgUrl = elem.select("img[class=i_cover]").attr("src");
                    //특정하기 힘들다... 저 앞에 있는집의 오른쪽으로 두번째집의 건너집이 바로 우리집이야 하는 식이다.
                    Element rElem = elem.select("div[class=ss_book_list] ul li").next().next().first();
                    String my_release = rElem.select("a").text();
                    Element dElem = elem.select("div[class=ss_book_list] ul li").next().next().next().first();
                    String my_director = "가격: " + dElem.select("span[class=ss_p2] b").text();
                    //Log.d("test", "test" + mTitle);
                    //ArrayList에 계속 추가한다.
                    list.add(new CrawlingItemObject(my_title, my_imgUrl, my_link, my_release, my_director));
                }

                //추출한 전체 <li> 출력해 보자.
                Log.d("debug :", "List " + mElementDataSize);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //ArraList를 인자로 해서 어답터와 연결한다.
            CrawlingAdapter crawlingAdapter = new CrawlingAdapter(list);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(crawlingAdapter);


        }
    }


}
