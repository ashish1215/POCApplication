package calendersuite.senecaglobal.calendersuite;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.google.firebase.firebase_core.*;


public class ContactView extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private Button datepicker;

    private Button timepicker;

    private EditText requestNumber;

    private int mYear, mMonth, mDay, mHour, mMinute;

    private TextView userName;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    Map<String, Object> request = new HashMap<>();

    private String receiverid;

    private String dateTime;

    private Button sendReminder;

    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_view);

        datepicker = findViewById(R.id.date);

        timepicker = findViewById(R.id.time);

        requestNumber = findViewById(R.id.requestNumber);

        sendReminder = findViewById(R.id.sendReminder);

        final TextView username = findViewById(R.id.username);

        Log.i("zoo",getIntent().getStringExtra("usernumber"));

        datepicker.setOnClickListener(this);
        timepicker.setOnClickListener(this);
        sendReminder.setOnClickListener(this);

        requestNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(hasFocus == false) {

                    db.collection("users").document("+91" + requestNumber.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            if(documentSnapshot.exists()) {
                                Map<String, Object> data = documentSnapshot.getData();
                                username.setText(data.get("name").toString());
                            }
                            else{
                                username.setTextColor(Color.RED);
                                username.setText("user number not found");
                                sendReminder.setEnabled(false);
                            }

                        }
                    });

                }

            }
        });

        }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.date){

            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear , int dayOfMonth) {

                            monthOfYear = monthOfYear+1;
                            dateTime = dayOfMonth+"-"+monthOfYear+"-"+year;

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
        else if(v.getId() == R.id.time){

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                           dateTime = dateTime+" "+hourOfDay+":"+minute;

                            try {
                                Date dateObject = new SimpleDateFormat("dd-MM-yyyy HH:mm").parse(dateTime);
                                request.put("date",dateObject);

                            } catch (ParseException e) {
                                e.printStackTrace();
                                Toast toast = Toast.makeText(getApplicationContext(),"Failed to pick date",Toast.LENGTH_LONG);

                            }


                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

        else if(v.getId() == R.id.sendReminder){

            requestNumber = findViewById(R.id.requestNumber);
            message = findViewById(R.id.message);
            request.put("receiverid", "+91"+requestNumber.getText().toString());
            request.put("senderid", getIntent().getStringExtra("usernumber"));
            request.put("message",message.getText().toString());

            db.collection("requests").add(request);

        }

    }

}
