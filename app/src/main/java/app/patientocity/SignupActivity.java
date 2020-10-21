package app.patientocity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {
    public static final String TAG1 = "TAG";
    public EditText email_id;
    public EditText password_id;
    FirebaseAuth mFirebaseAuth;
    TextView tvSignIn;
    Button signup;
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private static final String USERS = "users";
//    private String email,password;
    private String TAG = "RegisterActivity";
    FirebaseFirestore firebaseFirestore;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//email
//pass
        database = FirebaseDatabase.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mDatabase = database.getReference(USERS);
        mFirebaseAuth = FirebaseAuth.getInstance();
        tvSignIn=findViewById(R.id.textView);
        email_id=findViewById(R.id.email);
        password_id = findViewById(R.id.pass);
        signup = findViewById(R.id.create);

        if(mFirebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),Dashboard.class));
            finish();
        }
        //
        signup.setOnClickListener(v -> {
                final String email = email_id.getText().toString().trim();
                final String password = password_id.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    email_id.setError("Email is Required");
                    return;
                }

                if (TextUtils.isEmpty(password)){
                    password_id.setError("Password is Required");
                    return;
                }

               mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           FirebaseUser rUser =mFirebaseAuth.getCurrentUser();
                           String userId=rUser.getUid();
                           mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(userId);
                           HashMap<String,String> hashMap =new HashMap<>();
                           hashMap.put("userId",userId);
                           hashMap.put("email",email);
                           //Sai is one
                           hashMap.put("password",password);
                           mDatabase.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if(task.isSuccessful()){
                                       Intent intent= new Intent(SignupActivity.this, Dashboard.class);
                                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                       startActivity(intent);
                                   }
                                   else{
                                       Toast.makeText(SignupActivity.this, "Authentication Bad.",
                                               Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });

                       }
                       else{
                           Toast.makeText(SignupActivity.this, "Authentication Bad.",
                                   Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),Dashboard.class));                       }
                   }
               });

        });

        tvSignIn.setOnClickListener(v -> {
            Intent i = new Intent(SignupActivity.this,LoginActivity.class);
            startActivity(i);
        });
    }

}