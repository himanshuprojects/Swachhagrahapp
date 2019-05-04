package com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.Fragment.ProfileFragment;
import com.Model.User;
import com.bumptech.glide.Glide;
import com.example.swachhagrahmobileapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.swachhagrahmobileapp.MainActivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
// todo 4.2 created useradapter class

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ImageViewHolder>
{

//todo 4.4 typecast it to ImageViewHolder(which we made at 4.3)
    //todo 4.5 generate 3 imp functions- oncreate, onbind, getcount..

    private Context mContext;
    private List<User> mUsers;
    private boolean isFragment;

    private FirebaseUser firebaseUser;
    //todo 4.6 create a useradapter constuctor to adapt values
    public UserAdapter(Context context, List<User> users, boolean isFragment){
        mContext = context;
        mUsers = users;
        this.isFragment = isFragment;
    }

    // todo 4.7 USER_ITEM.XML ..before oncreate, its neccessary to create a layout for views under viewholder..

    @NonNull
    @Override
    public UserAdapter.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        //todo 4.8 get view object for each views of user_item.xml to be place to imageviewholder..
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ImageViewHolder(view);
        //from here the control goes to ImageViewHolder constructor to get view refrence by their ids..then comesback here a complete viewholder object is returned from here...

    }

    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.ImageViewHolder holder, final int position)
    {
        //todo 4.9 lets get our datasource- firebase for a user by position(index)
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final User user = mUsers.get(position);
        //make follow button visible
        holder.btn_follow.setVisibility(View.VISIBLE);
        //if current user already following then change button from follow to following...
        isFollowing(user.getId(), holder.btn_follow);
        //make username and cityname to visible for given user's viewholder
        holder.username.setText(user.getUsername());
        holder.fullname.setText(user.getCityname());
        //make profilepic visible
        Glide.with(mContext).load(user.getImageurl()).into(holder.image_profile);
        //make follow button not visible for same user.. same user are same person righhttt!!!
        if (user.getId().equals(firebaseUser.getUid())){
            holder.btn_follow.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFragment) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getId());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new ProfileFragment()).commit();
                }
                else {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra("publisherid", user.getId());
                    mContext.startActivity(intent);
                }
            }
        });

        holder.btn_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.btn_follow.getText().toString().equals("follow")) {
                    //setting viewholder userid on following list of current user
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).setValue(true);
                    //setting current user id got by firebaseuser.getuid() on followers list of shown viewholder user we clicked..
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).setValue(true);

                    //addNotification(user.getId());
                } else {// else oppostite to above opertaion ..for removing(unfollow)
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid())
                            .child("following").child(user.getId()).removeValue();
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId())
                            .child("followers").child(firebaseUser.getUid()).removeValue();
                }
            }

        });
    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    //todo 4.3 created view holder class which holds the views
    public class ImageViewHolder extends RecyclerView.ViewHolder
    {

        public TextView username;
        public TextView fullname;
        public CircleImageView image_profile;
        public Button btn_follow;

        public ImageViewHolder(View itemView)
        {
            super(itemView);

            username = itemView.findViewById(R.id.username);
            fullname = itemView.findViewById(R.id.cityname);
            image_profile = itemView.findViewById(R.id.image_profile);
            btn_follow = itemView.findViewById(R.id.btn_follow);
        }
    }

    private void isFollowing(final String userid, final Button button)
    {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(userid).exists()){
                    button.setText("following");
                } else{
                    button.setText("follow");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}