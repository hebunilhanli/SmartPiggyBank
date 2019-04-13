package com.example.hebun.piggybank.Fragments;

import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hebun.piggybank.R;
import com.example.hebun.piggybank.RecyclerViewAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment {

    Button tl, dolar, euros;
    TextView moneyType, sumsMoney;
    DatabaseReference myRef;
    FirebaseDatabase database;
    StorageReference fbStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser current;
    String fbTotal;
    CircleImageView miniProfilePicture;
    ImageView signsOut;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;


    public MainScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        tl = view.findViewById(R.id.turkishLira);
        dolar = view.findViewById(R.id.dollar);
        euros = view.findViewById(R.id.euro);
        moneyType = view.findViewById(R.id.type);
        sumsMoney = view.findViewById(R.id.sumMoney);
        miniProfilePicture = view.findViewById(R.id.miniPP);
        signsOut = view.findViewById(R.id.signOut);




        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fbStorageRef = FirebaseStorage.getInstance().getReference();


        myRef = database.getReference("Customers").child("user_" + current.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    fbTotal = String.valueOf(dataSnapshot.child("Total").getValue());
                    sumsMoney.setText(fbTotal);

                    String ppurl = dataSnapshot.child("PP").getValue(String.class);
                    Picasso.get().load(ppurl).into(miniProfilePicture);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        miniProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment transactionFragment = new TransactionFragment();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                transaction.replace(R.id.homeFrame, transactionFragment);
                transaction.commit();

            }
        });




        euros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                euros.setBackgroundResource(R.drawable.eurochoosed);
                tl.setBackgroundResource(R.drawable.tlnot);
                dolar.setBackgroundResource(R.drawable.dollarnot);
                moneyType.setText("€");

                DownloadData downloadData = new DownloadData();

                try {

                    String url = "https://api-currency-converter.herokuapp.com/api/convert?from=EUR&to=TRY";

                    downloadData.execute(url);


                }catch (Exception e){
                    e.printStackTrace();

                }

            }
        });
        tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                euros.setBackgroundResource(R.drawable.euronot);
                tl.setBackgroundResource(R.drawable.tlchoosed);
                dolar.setBackgroundResource(R.drawable.dollarnot);
                moneyType.setText("₺");

                DownloadData downloadData = new DownloadData();

                try {

                    String url = "https://api-currency-converter.herokuapp.com/api/convert?from=TRY&to=TRY";

                    downloadData.execute(url);

                }catch (Exception e){
                    e.printStackTrace();

                }

            }
        });
        dolar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                euros.setBackgroundResource(R.drawable.euronot);
                tl.setBackgroundResource(R.drawable.tlnot);
                dolar.setBackgroundResource(R.drawable.dollarchoosed);
                moneyType.setText("$");

                DownloadData downloadData = new DownloadData();

                try {

                    String url = "https://api-currency-converter.herokuapp.com/api/convert?from=USD&to=TRY";

                    downloadData.execute(url);


                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        signsOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment loginFragment = new LoginScreenFragment();
                mAuth.signOut();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                transaction.replace(R.id.homeFrame, loginFragment);
                transaction.commit();
            }
        });


        return view;

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
                String rate = String.valueOf(jsonObject.getDouble("rate"));

                   double num1 = Double.parseDouble(fbTotal);
                   double num2 = Double.parseDouble(rate);
                   double sum = num1 / num2;

                   String ha = String.valueOf(sum);

                   sumsMoney.setText(ha);

                   
            }catch (Exception e){
                e.printStackTrace();

            }

        }




    }
}
