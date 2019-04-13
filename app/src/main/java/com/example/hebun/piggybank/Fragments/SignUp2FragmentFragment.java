package com.example.hebun.piggybank.Fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.hebun.piggybank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * A placeholder fragment containing a simple view.
 */
public class SignUp2FragmentFragment extends Fragment {

    TextView piggyIDs;
    String id;
    CircleImageView choosePhoto;
    final int RequestGalleryPermissionID = 1001;
    Uri uri;
    Bitmap bitmap;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    FirebaseUser current;
    StorageReference fbStorageRef;
    EditText name, email, passwords, confirmpass;
    Button signsUp;


    public SignUp2FragmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up2_fragment, container, false);

        piggyIDs = view.findViewById(R.id.BankIDs);
        name = view.findViewById(R.id.fullName);
        email = view.findViewById(R.id.mail);
        passwords = view.findViewById(R.id.password);
        confirmpass = view.findViewById(R.id.confirmPassword);
        signsUp = view.findViewById(R.id.signUpssButton);


        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fbStorageRef = FirebaseStorage.getInstance().getReference();




        Bundle IDs = getArguments();
        id = IDs.getString("CorrectID");
        piggyIDs.setText(id);

        choosePhoto = view.findViewById(R.id.imageView2);




        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(),
                                new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, RequestGalleryPermissionID);

                    }else {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent,2);
                    }




            }
        });

        signsUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAuth.createUserWithEmailAndPassword(email.getText().toString(),passwords.getText().toString())
                        .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                            final FirebaseUser userNew = mAuth.getCurrentUser();


                            UUID uuidImage = UUID.randomUUID();
                            String profilepicture = "profilepic/" + uuidImage +".png";
                            StorageReference storageReference = fbStorageRef.child(profilepicture);
                            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {

                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            String imageurl = uri.toString();
                                            String fullname = name.getText().toString();
                                            String mail = email.getText().toString();
                                            String BanksIDs = piggyIDs.getText().toString();


                                            String userUID = "user_" + userNew.getUid();

                                            myRef.child("Customers").child(userUID).child("PiggyIDs").setValue(BanksIDs);
                                            myRef.child("Customers").child(userUID).child("Fullname").setValue(fullname);
                                            myRef.child("Customers").child(userUID).child("Email").setValue(mail);
                                            myRef.child("Customers").child(userUID).child("PP").setValue(imageurl);

                                            Fragment fragment = new LoginScreenFragment();

                                            FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                                            transaction.replace(R.id.signUp2Button,fragment);
                                            transaction.commit();



                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });


                        }
                    });


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

                }


        });


        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RequestGalleryPermissionID){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null)

            uri = data.getData();

        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),uri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            choosePhoto.setImageBitmap(bitmap);
        }catch (IOException e){
            e.printStackTrace();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
