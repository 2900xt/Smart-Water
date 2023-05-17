package com.remindrop.smartwater.ui.deviceSettings;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.remindrop.smartwater.R;

import java.util.ArrayList;
import java.util.function.Consumer;

public class BtDevicesListAdapter extends RecyclerView.Adapter<BtDevicesListAdapter.ViewHolder>
{
    private final ArrayList<String> data;
    private Consumer<String> onClicked;
    public BtDevicesListAdapter(ArrayList<String> data, Consumer<String> onClicked)
    {
        super();

        this.data = data;
        this.onClicked = onClicked;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.bt_dev_list_entry, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getButton().setText(data.get(position));
        viewHolder.getButton().setOnClickListener(v -> onClicked.accept(data.get(position)));
    }


    @Override
    public int getItemCount()
    {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final Button button;

        public ViewHolder(View view) {
            super(view);

            button = (Button) view.findViewById(R.id.bt_dev_entry_button);
        }

        public Button getButton()
        {
            return button;
        }
    }

    public void setOnClicked(Consumer<String> onClicked)
    {
        this.onClicked = onClicked;
    }
}
