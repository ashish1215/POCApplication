package calendersuite.senecaglobal.calendersuite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirstLoginActivity extends AppCompatActivity {


    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.namepopup);

        final EditText name = findViewById(R.id.name);

        name.setText("Ashish reddy");

        Button save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<String, Object>();

                data.put("name",name.getText().toString());
                db.collection("users").document(getIntent().getStringExtra("usernumber")).set(data);

                Intent intent = new Intent(FirstLoginActivity.this,ContactView.class);
                intent.putExtra("usernumber", getIntent().getStringExtra("usernumber"));
                startActivity(intent);

            }
        });



    }
}
