package com.example.capstonedesign;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PostlistFragment extends Fragment implements TextWatcher{

    private String userId;
    private EditText editSearch;

    private RecyclerView recyclerView;
    private PostAdapter mAdapter;
    private ArrayList<PostItemObject> postItemObjectArrayList;
    private FirebaseDatabase database;
    private LinearLayoutManager linearLayoutManager;
    private StorageReference mStorageRef;
    private View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_postlist, container, false);
        initView();

        //mainactivity에서 전달한 userid 받기
        Bundle bundle = getArguments();
        userId = bundle.getString("userID");


        recyclerView.setHasFixedSize(true);

        database = FirebaseDatabase.getInstance();

        postItemObjectArrayList = new ArrayList<>();
        mAdapter = new PostAdapter(postItemObjectArrayList);


        //커스텀 아이템클릭 리스너
        mAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                String releasedate = String.valueOf(mAdapter.filteredList.get(pos).getTime());
                String destinationUserId = String.valueOf(mAdapter.filteredList.get(pos).getUserid());

                Intent intent = new Intent(getContext(), PostdetailActivity.class);
                intent.putExtra("userID",userId);
                intent.putExtra("releasedate", releasedate);
                intent.putExtra("destinationUserId", destinationUserId);

                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    activityOptions = ActivityOptions.makeCustomAnimation(view.getContext(), R.anim.fromright,R.anim.toleft);
                    startActivity(intent,activityOptions.toBundle());
                }
            }
        });




        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);


        // 데이터 읽기
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new comment has been added, add it to the displayed list
                PostItemObject postItemObject = dataSnapshot.getValue(PostItemObject.class);


                postItemObjectArrayList.add(postItemObject);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.

                // ...
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                String commentKey = dataSnapshot.getKey();

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A comment has changed position, use the key to determine if we are
                // displaying this comment and if so move it.


                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText( getActivity(), "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        DatabaseReference ref = database.getReference("post");
        ref.addChildEventListener(childEventListener);

        mStorageRef = FirebaseStorage.getInstance().getReference();


       editSearch.addTextChangedListener(this);


        return view;
    }

    // 검색기능 오버라이드
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mAdapter.getFilter().filter(s);
    }
    @Override
    public void afterTextChanged(Editable s) {
    }



    private void initView() {
        editSearch = (EditText) view.findViewById(R.id.edit_search);
        recyclerView = view.findViewById(R.id.recyclerview2);
    }
}
