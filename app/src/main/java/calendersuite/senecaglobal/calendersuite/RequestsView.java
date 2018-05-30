package calendersuite.senecaglobal.calendersuite;

import android.app.DownloadManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.MainThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.xml.transform.Result;

public class RequestsView extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Request> list = new ArrayList<Request>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_view);
        mRecyclerView = findViewById(R.id.recyclerView);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),LinearLayoutManager.VERTICAL));

        DataRetreival data = new DataRetreival(getIntent().getStringExtra("usernumber"));
        data.execute();

        // specify an adapter (see also next example)
    }

    class DataRetreival extends AsyncTask {

        private String usernumber;

        private List<Request> requestList = new ArrayList<Request>();

        private Map<String, Object> requestdata = new HashMap<String, Object>();


        DataRetreival(String number) {
            this.usernumber = number;
        }

        @Override
        protected List<Request> doInBackground(Object[] objects) {

            db.collection("requests").whereEqualTo("receiverid", usernumber).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                    if (e != null) {
                        System.err.println("Listen failed: " + e);
                        return;
                    }

                    for (DocumentChange dc : documentSnapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                requestdata = dc.getDocument().getData();
                                Request req = new Request( (Date)requestdata.get("date") , requestdata.get("message").toString(), requestdata.get("senderid").toString(), requestdata.get("receiverid").toString());
                                requestList.add(req);
                                mAdapter.notifyDataSetChanged();
                                Intent intent = new Intent(RequestsView.this, ContactView.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("usernumber",getIntent().getStringExtra("usernumber"));
                                PendingIntent pendingIntent = PendingIntent.getActivity(RequestsView.this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                                Uri soundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                                Notification notify=new Notification.Builder
                                        (getApplicationContext()).setContentTitle("Calender Suite").setContentText("You received a calender request!").
                                        setContentTitle("Calender block!").setSmallIcon(R.drawable.ic_launcher_background).setSound(soundUri).setContentIntent(pendingIntent).build();

                                notify.flags |= Notification.FLAG_AUTO_CANCEL;
                                notif.notify(0, notify);
                                break;
                            case MODIFIED:
                                mAdapter.notifyDataSetChanged();
                                break;
                            case REMOVED:
                                mAdapter.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }
                    }
                }
            });
            return requestList;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            List<Request> list1 = (List<Request>) o;
            mAdapter = new MyAdapter(list1, RequestsView.this);

            mRecyclerView.setAdapter(mAdapter);


        }

    }

}