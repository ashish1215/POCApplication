package calendersuite.senecaglobal.calendersuite;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import calendersuite.senecaglobal.calendersuite.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<Request> mDataset;
    private Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView number;
        public TextView message;
        public TextView dateTime;
        public ViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number);
            message = view.findViewById(R.id.message);
            dateTime = view.findViewById(R.id.date);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(List<Request> myDataset,Context context) {
        mDataset = myDataset;
        this.context = context;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_cell, parent, false);



        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.number.setText(mDataset.get(position).getSenderid());
        holder.message.setText(mDataset.get(position).getMessage());
        holder.dateTime.setText(mDataset.get(position).getDate().toString());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}