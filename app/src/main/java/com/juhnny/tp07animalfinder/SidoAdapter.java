package com.juhnny.tp07animalfinder;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class SidoAdapter extends ArrayAdapter {

    public SidoAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.e("I'm Adapter", "notifyDataSetChanged()");
    }
}
