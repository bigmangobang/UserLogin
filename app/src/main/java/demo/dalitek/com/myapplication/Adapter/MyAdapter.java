package demo.dalitek.com.myapplication.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import demo.dalitek.com.myapplication.Data.UserData;
import demo.dalitek.com.myapplication.R;

public class MyAdapter extends ArrayAdapter {

    private int resourceId;

    @SuppressLint("ResourceType")
    public MyAdapter(@NonNull Context context,
                     int resource,
                     @NonNull List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserData product = (UserData) getItem(position);
        UserLayout userLayout = new UserLayout();
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            userLayout.nameView = view.findViewById(R.id.user);
            view.setTag(userLayout);
        } else {
            view = convertView;
            userLayout = (UserLayout) view.getTag();
        }
        userLayout.nameView.setText(product.getUserName());

        return view;
    }

    class UserLayout {
        TextView nameView;
        TextView pwdView;
        ImageView imgView;
        Button addButton;
    }
}
