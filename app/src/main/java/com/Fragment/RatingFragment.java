package com.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.swachhagrahmobileapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.MODE_PRIVATE;

import com.Model.Rate;
import com.google.firebase.database.ValueEventListener;

public class RatingFragment extends Fragment
{
    Button submit;
    RatingBar ajmerrb,jodhpurrb,jaipurrb,kotarb,udaipurrb,bharatpurrb,bikanerrb;
    FirebaseUser firebaseUser;
    String profileid;
    DatabaseReference reff;
    Rate rate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_rating, container, false);
        ajmerrb=view.findViewById(R.id.ajmernn);
        bharatpurrb=view.findViewById(R.id.bharatpurnn);
        bikanerrb=view.findViewById(R.id.bikanernn);
        jaipurrb=view.findViewById(R.id.jaipurnn);
        jodhpurrb=view.findViewById(R.id.jodhpurnn);
        kotarb=view.findViewById(R.id.kotann);
        udaipurrb=view.findViewById(R.id.udaipurnn);
submit=view.findViewById(R.id.submit);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reff= FirebaseDatabase.getInstance().getReference().child("Rating");
        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");


        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rate=dataSnapshot.getValue(Rate.class);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v)
            {

                FirebaseDatabase.getInstance().getReference().child("Rating").child("ajmer")
                        .setValue(Long.toString((long)(5-(ajmerrb.getRating()+Long.parseLong(rate.getAjmer()))/5)));

                FirebaseDatabase.getInstance().getReference().child("Rating").child("bikaner")
                        .setValue(Long.toString((long)(5-(bikanerrb.getRating()+Long.parseLong(rate.getBikaner()))/5)));

                FirebaseDatabase.getInstance().getReference().child("Rating").child("bharatpur")
                        .setValue(Long.toString((long)(5-(bharatpurrb.getRating()+Long.parseLong(rate.getBharatpur()))/5)));

                FirebaseDatabase.getInstance().getReference().child("Rating").child("jaipur")
                        .setValue(Long.toString((long)(5-(jaipurrb.getRating()+Long.parseLong(rate.getJaipur()))/5)));

                FirebaseDatabase.getInstance().getReference().child("Rating").child("jodhpur")
                        .setValue(Long.toString((long)(5-(jodhpurrb.getRating()+Long.parseLong(rate.getJodhpur()))/5)));

                FirebaseDatabase.getInstance().getReference().child("Rating").child("kota")
                        .setValue(Long.toString((long)(5-(kotarb.getRating()+Long.parseLong(rate.getKota()))/5)));

                FirebaseDatabase.getInstance().getReference().child("Rating").child("udaipur")
                        .setValue(Long.toString((long)(5-(udaipurrb.getRating()+Long.parseLong(rate.getUdaipur()))/5)));
                Toast.makeText(getContext(),"Rating submitted to server",Toast.LENGTH_SHORT).show();
            }
        });


return view;

    }


}
