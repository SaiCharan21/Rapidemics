package app.patientocity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {


    Button logout;
    ImageView imageView;
    TextView name, email, id;
    private EditText searchField;
    private ImageButton searchButton;

    private RecyclerView recyclerView;

    private FirebaseUser user;
    private DatabaseReference reference;

    private String uid;


//    GoogleSignInClient mGoogleSignIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        logout = findViewById(R.id.logoutbtnn);
//        searchField =findViewById(R.id.search_field);
//        searchButton=findViewById(R.id.imageButton2)

        imageView = findViewById(R.id.user_imageview);
        user = FirebaseAuth.getInstance().getCurrentUser();

        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Profile.this, LoginActivity.class));
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        uid = user.getUid();

        name = findViewById(R.id.name_textview);
        email = findViewById(R.id.mail);
        id = findViewById(R.id.pwd_id);


        reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ytu profile = snapshot.getValue(ytu.class);

                if (profile != null) {
                    String nam = profile.name;
                    String email_id = profile.email;
                    String pwd = profile.password;

                    email.setText(email_id);
                    id.setText(pwd);
                    name.setText(nam);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Something wrong happened", Toast.LENGTH_LONG).show();
            }
        });
    }

}