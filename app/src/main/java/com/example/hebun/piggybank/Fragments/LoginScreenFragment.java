package com.example.hebun.piggybank.Fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

import io.ghyeok.stickyswitch.widget.StickySwitch;


public class LoginScreenFragment extends Fragment {

    TextView signups;
    EditText email, passoword;
    Fragment signupfragment;
    Fragment homefragment;
    Button login;
    DatabaseReference myRef;
    FirebaseDatabase fbDatabase;
    private FirebaseAuth mAuth;
    FirebaseUser mUser;
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

        signupfragment = new SignUpFragment();
        homefragment = new MainScreenFragment();

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        fbDatabase = FirebaseDatabase.getInstance();
        myRef = fbDatabase.getReference();


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

                                    myRef = fbDatabase.getReference("Customers").child("user_" + user.getUid());
                                    myRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Calendar calendar = Calendar.getInstance();
                                            String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());


                                            myRef.child("Entry Transactions").child("Last Entry").child("Date").setValue(currentDate);


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




        return view;
    }



    }

