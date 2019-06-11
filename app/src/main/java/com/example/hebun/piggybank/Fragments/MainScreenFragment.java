package com.example.hebun.piggybank.Fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hebun.piggybank.Data;
import com.example.hebun.piggybank.R;
import com.example.hebun.piggybank.ViewHolder.TransactionsViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment {

    Button tl, dolar, euros;
    TextView moneyType, sumsMoney;
    DatabaseReference myRef,myRef2,myRef3;
    FirebaseDatabase database;
    StorageReference fbStorageRef;
    FirebaseAuth mAuth;
    FirebaseUser current;
    CircleImageView miniProfilePicture;
    ImageView signsOut;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    FirebaseRecyclerOptions<Data> recyclerOptions;
    FirebaseRecyclerAdapter<Data,TransactionsViewHolder> recyclerAdapter;
    String date;
    double sumss;

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
        recyclerView = view.findViewById(R.id.recyle);

        recyclerView.setHasFixedSize(true);

        mAuth = FirebaseAuth.getInstance();
        current = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        fbStorageRef = FirebaseStorage.getInstance().getReference();
        myRef2 = FirebaseDatabase.getInstance().getReference().child("Customers").child("user_"+ current.getUid()).child("Money Transactions");

        myRef3 = database.getReference().child("Customers").child("user_"+current.getUid());
        myRef3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myRef3.child("kasadurumu").setValue("0");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        recyclerOptions = new FirebaseRecyclerOptions.Builder<Data>().setQuery(myRef2,Data.class).build();
        recyclerAdapter = new FirebaseRecyclerAdapter<Data, TransactionsViewHolder>(recyclerOptions) {
            @Override
            protected void onBindViewHolder(final TransactionsViewHolder holder, int position, final Data model) {


                holder.dateCount.setText(model.getDateCount());
                holder.money.setText(model.getMoney());
                holder.month.setText(model.getMonth());

                holder.dateCount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Query query = myRef2.orderByChild("dateCount").equalTo(String.valueOf(holder.dateCount.getText()));
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    date = String.valueOf(ds.getKey());
                               }
                                Fragment daily = new DailyMoneyFragment();
                                Bundle bundle = new Bundle();

                                bundle.putString("DATE",date);
                                bundle.putString("MONEY",model.getMoney());
                                daily.setArguments(bundle);

                                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.homeFrame,daily).addToBackStack(null);
                                transaction.commit();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });


            }

            @NonNull
            @Override
            public TransactionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                View view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);


                return new TransactionsViewHolder(view1);
            }
        };

        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter.startListening();
        recyclerView.setAdapter(recyclerAdapter);



        myRef = database.getReference("Customers").child("user_" + current.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String ppurl = dataSnapshot.child("PP").getValue(String.class);
                    Picasso.get().load(ppurl).into(miniProfilePicture);

                  myRef.child("Money Transactions").addValueEventListener(new ValueEventListener() {
                      @Override
                      public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                          sumss=0;
                          for (DataSnapshot ds : dataSnapshot.getChildren()){

                              Map<String,String> map = (Map<String, String>) ds.getValue();
                              String moneys = map.get("money");
                              if (dataSnapshot.exists()){
                                  double mValue = Double.parseDouble(moneys);
                                  sumss += mValue;
                                  sumsMoney.setText((String.valueOf(sumss)));
                              }else{
                                  sumsMoney.setText("0.0");
                              }

                          }
                      }

                      @Override
                      public void onCancelled(@NonNull DatabaseError databaseError) {

                      }
                  });



            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        miniProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment transactionFragment = new TransactionFragment();
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
                transaction.replace(R.id.homeFrame, transactionFragment).addToBackStack(null);
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
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.enter_left_to_right,R.anim.exit_to_right);
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

                   double num1 = sumss;
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
