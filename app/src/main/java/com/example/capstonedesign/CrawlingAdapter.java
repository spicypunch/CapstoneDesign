package com.example.capstonedesign;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CrawlingAdapter extends RecyclerView.Adapter<CrawlingAdapter.ViewHolder> {

    //데이터 배열 선언
    private ArrayList<CrawlingItemObject> mList;

    public  class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_img;
        private TextView textView_title, textView_release, texView_director;

        public ViewHolder(final View itemView) {
            super(itemView);


            //////////// 클릭 이벤트 기능 작업 -> 뷰홀더가 생성될때 position 값을 활용하여 해당 번호의 item에서 detail_link  속성을 가져온다..
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {

                        String url =  String.valueOf(mList.get(pos).getDetail_link());
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        v.getContext().startActivity(intent);

                    }
                }
            });

            imageView_img = (ImageView) itemView.findViewById(R.id.imageView_img);
            textView_title = (TextView) itemView.findViewById(R.id.textView_title);
            textView_release = (TextView) itemView.findViewById(R.id.textView_release);
            texView_director = (TextView) itemView.findViewById(R.id.textView_director);
        }
    }

    //생성자
    public CrawlingAdapter(ArrayList<CrawlingItemObject> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public CrawlingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.crawling_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrawlingAdapter.ViewHolder holder, int position) {

        holder.textView_title.setText(String.valueOf(mList.get(position).getTitle()));
        holder.textView_release.setText(String.valueOf(mList.get(position).getRelease()));
        holder.texView_director.setText(String.valueOf(mList.get(position).getDirector()));
        //다 해줬는데도 GlideApp 에러가 나면 rebuild project를 해주자.
        Picasso.get().load(mList.get(position).getImg_url())
                .into(holder.imageView_img);

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}