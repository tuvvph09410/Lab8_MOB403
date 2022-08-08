package com.example.lab8_mob403;

import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireBaseHelper {
    private FirebaseDatabase fb_instance;
    private DatabaseReference fb_db;
    private String USER_ID;

    public void createUser(String email, String password, TextView tvResult) {
        fb_instance = FirebaseDatabase.getInstance();
        fb_db = fb_instance.getReference("User");
        if (TextUtils.isEmpty(USER_ID)) {
            USER_ID = fb_db.push().getKey();
            User user= new User(email, password);
            fb_db.child(USER_ID).setValue(user);
            userChangeListener(tvResult);
        }
    }

    public void updateUser(String email, String password, TextView tvResult) {
        fb_instance = FirebaseDatabase.getInstance();
        fb_db = fb_instance.getReference("User");
        if (!TextUtils.isEmpty(email)) {
            fb_db.child(USER_ID).child("email").setValue(email);
        }
        if (!TextUtils.isEmpty(password)) {
            fb_db.child(USER_ID).child("password").setValue(password);
        }
        userChangeListener(tvResult);
    }

    public void userChangeListener(final TextView textView) {
        fb_db.child(USER_ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                textView.setText(user.getEmail() + "; " + user.getPassword());
                List<User> userList = new ArrayList<>();
                for (DataSnapshot d : snapshot.getChildren()) {
                    User user1 = snapshot.getValue(User.class);
                    userList.add(user1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                textView.setText(error.getMessage());
            }
        });
    }

}
