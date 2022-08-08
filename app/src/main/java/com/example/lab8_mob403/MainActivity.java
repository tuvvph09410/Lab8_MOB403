package com.example.lab8_mob403;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    EditText edEmail, edPassword;
    Button btnLogin, btnRegister, btnInsertArray, btnInsertAppend, btnCreateUser, btnUpdateUser;
    TextView tvResult;
    FirebaseAuth auth;
    FireBaseHelper fireBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewById();

        init();

        clickListener();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
    }

    private void clickListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            tvResult.setText("Đăng nhập thành công");
                        }

                    }
                });

            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            tvResult.setText("Tạo tài khoản thành công");
                        }

                    }
                });

            }
        });
        btnInsertArray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] array = new String[]{"item", "item", "item"};
                Task<Void> task = FirebaseDatabase.getInstance().getReference().child("ticket").setValue(Arrays.asList(array));

            }
        });
        btnInsertAppend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference("ticket").child("item").runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                        String lastKey = "-1";
                        for (MutableData c : currentData.getChildren()) {
                            lastKey = c.getKey();
                        }
                        int nextKey = Integer.parseInt(lastKey) + 1;
                        currentData.child("" + nextKey).setValue("Giá trị mà ta muốn thêm");

                        return Transaction.success(currentData);
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                        tvResult.setText("Insert thành công");
                    }
                });
            }
        });
        btnCreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                fireBaseHelper = new FireBaseHelper();
                fireBaseHelper.createUser(email, password, tvResult);

            }
        });
        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edEmail.getText().toString();
                String password = edPassword.getText().toString();
                fireBaseHelper.updateUser(email, password, tvResult);
            }
        });
    }

    private void initViewById() {
        edEmail = findViewById(R.id.ed_email);
        edPassword = findViewById(R.id.ed_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        btnInsertArray = findViewById(R.id.btn_insert_array);
        btnInsertAppend = findViewById(R.id.btn_insert_append);
        btnCreateUser = findViewById(R.id.btn_create_user);
        btnUpdateUser = findViewById(R.id.btn_update_user);
        tvResult = findViewById(R.id.tv_result);
    }
}