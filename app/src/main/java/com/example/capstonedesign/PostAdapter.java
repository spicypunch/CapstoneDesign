package com.example.capstonedesign;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> implements Filterable {



    //데이터 배열 선언
    private ArrayList<PostItemObject> mList;
    //필터가 된 리스트는 외부 아이템 클릭 리스너에서 접근해서 이용해야하기때문에 public처리 -> getter setter 까지하면 너무복잡해짐.
    public ArrayList<PostItemObject> filteredList;

    //아이템클릭리스너 사용을 위해서 추가
    private OnItemClickListener mListener = null ;


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView_img;
        private TextView textView_title, textView_release, texView_uerid, textView_price;


        public ViewHolder(final View itemView) {
            super(itemView);


            //아이템항목 클릭시 이벤트( 자세한 기능 내용은 postlistFragment로 옮겼음)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {

//                        String releasedate = String.valueOf(filteredList.get(pos).getTime());
//                        String destinationUserId = String.valueOf(filteredList.get(pos).getUserid());
//
//
//                        Intent intent = new Intent(v.getContext(), PostdetailActivity.class);
//                        //게시글의 realesdate, destinationUserId 를 전달
//                        intent.putExtra("releasedate", releasedate);
//                        intent.putExtra("destinationUserId", destinationUserId);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        v.getContext().startActivity(intent);
                            if(mListener !=null){
                            mListener.onItemClick(v,pos);
                        }

                    }
                }
            });


            imageView_img = (ImageView) itemView.findViewById(R.id.imageView_img1);
            textView_title = (TextView) itemView.findViewById(R.id.textView_title1);
            texView_uerid = (TextView) itemView.findViewById(R.id.textView_userid1);
            textView_release = (TextView) itemView.findViewById(R.id.textView_release1);
            textView_price = (TextView) itemView.findViewById(R.id.textView_price1);
        }


    }

    //생성자
    public PostAdapter(ArrayList<PostItemObject> list) {
        this.mList = list;
        this.filteredList = list;

    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_data, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {

        holder.textView_title.setText(String.valueOf(filteredList.get(position).getTitle()));
        holder.texView_uerid.setText(String.valueOf(filteredList.get(position).getUserid()));
        holder.textView_release.setText(String.valueOf(filteredList.get(position).getTime()));
        holder.textView_price.setText(String.valueOf(filteredList.get(position).getPrice()) + "원");

        Picasso.get().load(filteredList.get(position).getImguri()).resize(200, 200)
                .into(holder.imageView_img);

//        mStorageRef = FirebaseStorage.getInstance().getReference();
//
//        StorageReference riversRef = mStorageRef.child("posts").child(mList.get(position).getTitle());
//
//        Uri url = riversRef.getDownloadUrl().getResult();
//
//        Picasso.get().load(url)
//                .into(holder.imageView_img);
//
//        Glide.with(context)
//                .load(riversRef.getDownloadUrl())
//                .into(holder.imageView_img);
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // 검색기능
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    filteredList = mList;
                } else {
                    ArrayList<PostItemObject> filteringList = new ArrayList<>();
                    for (PostItemObject name : mList) {
                        if (name.getTitle().contains(charString.toLowerCase())) {
                            filteringList.add(name);
                        }
                    }
                    filteredList = filteringList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (ArrayList<PostItemObject>)results.values;
                notifyDataSetChanged();
            }
        };

    }



    //뷰홀더 생성할때 클릭리스너를 넣어주면 편리하지만 그래서는 intent 전달이 제한적이다.
    //그래서 외부 ( postlistfragment)에 클릭리스너를 만들기위해 기능개발 .


    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

}




