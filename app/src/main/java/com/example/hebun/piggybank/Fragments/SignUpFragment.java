package com.example.hebun.piggybank.Fragments;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.example.hebun.piggybank.R;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;


public class SignUpFragment extends Fragment {

    SurfaceView surfaceView;
    TextView qrviews;
    BarcodeDetector detector;
    CameraSource camsource;
    final int RequestCameraPermissionID = 1001;
    Button qrbuttons;
    String newqrviews;
    Fragment signUp2;
    FirebaseDatabase fbDatabase;
    DatabaseReference myRef;
    FirebaseUser fbUser;
    FirebaseAuth mAuth;
    String id;
    ArrayList<String> piggybankids;






    public SignUpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        signUp2 = new SignUp2FragmentFragment();
        surfaceView = view.findViewById(R.id.cameraPreview);
        qrviews = view.findViewById(R.id.qrview);
        qrbuttons = view.findViewById(R.id.qrbutton);

        fbDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
        myRef = fbDatabase.getReference();


        piggybankids = new ArrayList<String>();




        detector = new BarcodeDetector.Builder(requireActivity()).setBarcodeFormats(Barcode.QR_CODE).build();

        camsource = new CameraSource.Builder(requireContext(),detector).setRequestedPreviewSize(640,480).build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(),
                            new String[]{Manifest.permission.CAMERA}, RequestCameraPermissionID);
                    return;
                }
                try {
                    camsource.start(surfaceView.getHolder());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }


            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                camsource.stop();

            }
        });

        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodess = detections.getDetectedItems();

                if (qrcodess.size() != 0){
                    qrviews.post(new Runnable() {
                        @Override
                        public void run() {
                            Vibrator vibrator = (Vibrator)requireContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(1000);
                            qrviews.setText(qrcodess.valueAt(0).displayValue);
                            newqrviews = qrviews.toString();




                        }
                    });
                }


            }
        });




        qrbuttons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("LALALALALA= " + id);

                myRef = fbDatabase.getReference().child("PiggBankIDs");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {

                            piggybankids.add(String.valueOf(ds.getValue()));

                        }
                        id = qrviews.getText().toString();


                        if (piggybankids.contains(id.toString())){

                            Bundle bundle = new Bundle();

                            bundle.putString("CorrectID",id);

                            signUp2.setArguments(bundle);


                            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                            fragmentTransaction.replace(R.id.signUpFrame,signUp2).addToBackStack(null);
                            fragmentTransaction.commit();

                        }else {
                            Toast.makeText(requireContext(),"Your QR code does not match the IDs in the system. Please read the correct code.",Toast.LENGTH_SHORT).show();
                        }


                        }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }
        });






        return view;
    }


}
