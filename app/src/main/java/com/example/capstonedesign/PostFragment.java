package com.example.capstonedesign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.mbms.FileInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

public class PostFragment extends Fragment {

    private View view;
    private EditText editTitle;
    private EditText editContents;
    private EditText editPrice;
    private Button btnPost;
    private ImageView imgGallery;
    private ImageView imgPost;
    private FirebaseDatabase database;
    private String userId;
    private String stTitle;
    private String stContents;
    private String stPrice;
    String imguri;

    private int REQUEST_IMAGE_CODE = 1001;
    private int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1002;

    private StorageReference mStorageRef;
    private Uri image;





    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_post, container, false);
        initView();




        //앱 권한 요청하기
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_PERMISSION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        //mainactivity에서 전달한 userid 받기
        Bundle bundle = getArguments();
        userId = bundle.getString("userID");

        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("post");

        mStorageRef = FirebaseStorage.getInstance().getReference();



        //글작성 완료 버튼 클릭시 이벤트
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stTitle = editTitle.getText().toString();
                stContents = editContents.getText().toString();
                stPrice = editPrice.getText().toString();



                //이미지 업로드 메서드 -> 석세스안에 db에 데이터넣는것을 구현
                final StorageReference riversRef = mStorageRef.child("posts").child(stTitle).child("post.jpg");


                riversRef.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                Calendar c = Calendar.getInstance();
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String datetime = dateformat.format(c.getTime());

                DatabaseReference myRef = database.getReference("post").child(datetime);
                Hashtable<String, String> numbers
                        = new Hashtable<String, String>();
                  numbers.put("title", stTitle);
                  numbers.put("userid", userId);
                  numbers.put("contents", stContents);
                  numbers.put("time", datetime);
                  numbers.put("price", stPrice );
                  numbers.put("imguri", uri.toString());
                  myRef.setValue(numbers);
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });



//                          여기 부분 때문에 12시간허비 코드 전부 리스너 안으로 이동하여 해결
//                          이미지 다운로드 url 메서드가 task를 반환해서 toString() 해봤자 url이 아닌 이상한 문구만 가져왔음;;
//
//                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                            @Override
//                            public void onSuccess(Uri uri) {
//                               // imguri = uri.toString();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                            }
//                      }).toString();
//
//
//
//                Toast.makeText(getActivity(), imguri,Toast.LENGTH_LONG).show();
//
//
//
//                Calendar c = Calendar.getInstance();
//                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//                String datetime = dateformat.format(c.getTime());
//
//                DatabaseReference myRef = database.getReference("post").child(stTitle);
//                Hashtable<String, String> numbers
//                        = new Hashtable<String, String>();
//                numbers.put("title", stTitle);
//                numbers.put("userid", userId);
//                numbers.put("contents", stContents);
//                numbers.put("time", datetime);
//                numbers.put("imguri", imguri);
//                myRef.setValue(numbers);

                Toast.makeText(getActivity(), "글 작성 완료하였습니다", Toast.LENGTH_LONG).show();
                //입력란 초기화
                imgGallery.setImageBitmap(null);
                editPrice.setText("");
                editTitle.setText("");
                editContents.setText("");
            }
        });

        //갤러리에서 사진선택 버튼
        imgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CODE){
            image = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), image);
                imgGallery.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //    Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    private void initView() {
        editTitle = (EditText) view.findViewById(R.id.edit_title);
        editContents = (EditText) view.findViewById(R.id.edit_contents);
        btnPost = (Button) view.findViewById(R.id.btn_post);
        imgGallery = (ImageView) view.findViewById(R.id.image_gallery);
        editPrice = (EditText) view.findViewById(R.id.edit_price);

    }
}
