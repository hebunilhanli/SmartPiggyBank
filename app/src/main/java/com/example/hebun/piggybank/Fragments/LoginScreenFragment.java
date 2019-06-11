package com.example.hebun.piggybank.Fragments;

import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hebun.piggybank.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.ghyeok.stickyswitch.widget.StickySwitch;


public class LoginScreenFragment extends Fragment {

    TextView signups,forgetPassword;
    EditText email, passoword;
    Fragment signupfragment;
    Fragment homefragment;
    Button login;
    DatabaseReference myRef, myRef2,myRef3;
    FirebaseDatabase fbDatabase;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
    String ip;
    long childCount;
    String rate, userUID;
    Calendar calendar;
    SimpleDateFormat sdf,sdf2;
    String currentDate,currentHour;
    DownloadData downloadData;
    String url;
    StickySwitch stickySwitch;






    public LoginScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_screen, container, false);

        signups = view.findViewById(R.id.signUpButton);
        login = view.findViewById(R.id.loginButton);
        email = view.findViewById(R.id.loginEmail);
        passoword = view.findViewById(R.id.signInPassword);
        stickySwitch = view.findViewById(R.id.rememberSwitch);
        forgetPassword = view.findViewById(R.id.forgetPassword);

        signupfragment = new SignUpFragment();
        homefragment = new MainScreenFragment();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        fbDatabase = FirebaseDatabase.getInstance();
        myRef = fbDatabase.getReference();
        myRef2 = fbDatabase.getReference();
        myRef3 = fbDatabase.getReference();

        Log.e("ZZZZZZ", email.getText().toString());

//----------------------------------------------------------------------------------
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf2 = new SimpleDateFormat("HH:mm");
        currentDate = sdf.format(calendar.getTime());
        currentHour = sdf2.format(calendar.getTime());


        downloadData = new DownloadData();
        url = "https://api.myip.com/";
        downloadData.execute(url);
//----------------------------------------------------------------------------------


        signups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                fragmentTransaction.replace(R.id.loginframe, signupfragment).addToBackStack(null);
                fragmentTransaction.commit();
            }


        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View view) {

            mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithEmailAndPassword(email.getText().toString(), passoword.getText().toString())
                    .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                if (task.isSuccessful()) {

                                    myRef = fbDatabase.getReference().child("Customers").child("user_"+user.getUid());
                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Long count = dataSnapshot.child("Entry Transactions").child("Login Record").getChildrenCount();
                                            myRef.child("Entry Transactions").child("Login Record").child(String.valueOf(count+1)).child("dateRecord").setValue(currentDate);
                                            myRef.child("Entry Transactions").child("Login Record").child(String.valueOf(count+1)).child("timeRecord").setValue(currentHour);
                                            myRef.child("Entry Transactions").child("Login Record").child(String.valueOf(count+1)).child("statusRecord").setValue("Success");
                                            myRef.child("Entry Transactions").child("Login Record").child(String.valueOf(count+1)).child("ipRecord").setValue(rate);
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right, R.anim.exit_to_right);
                                    fragmentTransaction.replace(R.id.loginframe, homefragment);
                                    fragmentTransaction.commit();

                                } else {
                                    Toast.makeText(requireContext(), "There is no user registered in the System. Try Again.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    myRef2 = fbDatabase.getReference().child("Customers");
                    Query query = myRef2.orderByChild("Email").equalTo(email.getText().toString());
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                userUID = String.valueOf(dsp.getKey());
                            }
                            Long count = dataSnapshot.child(userUID).child("Entry Transactions").child("Login Record").getChildrenCount();
                            myRef2.child(userUID).child("Entry Transactions").child("Login Record").child(String.valueOf(count+1)).child("dateRecord").setValue(currentDate);
                            myRef2.child(userUID).child("Entry Transactions").child("Login Record").child(String.valueOf(count+1)).child("timeRecord").setValue(currentHour);
                            myRef2.child(userUID).child("Entry Transactions").child("Login Record").child(String.valueOf(count+1)).child("statusRecord").setValue("Failed");
                            myRef2.child(userUID).child("Entry Transactions").child("Login Record").child(String.valueOf(count+1)).child("ipRecord").setValue(rate);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                }
            });


        }
    });

        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {



                if (String.valueOf(stickySwitch.getDirection()) == "RIGHT"){
                    stickySwitch.setSliderBackgroundColor(0xFF5DBD5E);
                    stickySwitch.setSwitchColor(0xFF2A632B);

                }else if (String.valueOf(stickySwitch.getDirection()) == "LEFT"){
                    stickySwitch.setSliderBackgroundColor(0x8a5dbd5e);
                    stickySwitch.setSwitchColor(0xFF2A632B);
                }

            }
        });

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment forgets = new ForgetPasswordFragment();

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                transaction.replace(R.id.loginframe, forgets).addToBackStack(null);
                transaction.commit();
                
            }
        });




        return view;
    }

    public class Test extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {

            URL whatismyip = null;
            try {
                whatismyip = new URL("http://icanhazip.com/");


                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            whatismyip.openStream()));


                    String ip = in.readLine(); //you get the IP as a String
                    Log.i("EXT IP: " , ip);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    private class DownloadData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {

            String result = "";
            URL url;
            HttpURLConnection httpURLConnection;

            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data > 0) {
                    char character = (char) data;
                    result += character;

                    data = inputStreamReader.read();
                }
                return result;

            } catch (Exception e) {

                return null;
            }

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);



            try {
                JSONObject jsonObject = new JSONObject(s);
                rate = String.valueOf(jsonObject.get("ip"));

                Log.e("RATERATE== ",rate);


            }catch (Exception e){
                e.printStackTrace();

            }

        }

    }
    }




