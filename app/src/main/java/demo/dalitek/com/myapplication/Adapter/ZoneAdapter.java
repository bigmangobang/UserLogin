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
import android.widget.TextView;

import java.util.List;

import demo.dalitek.com.myapplication.Util.ZoneData;
import demo.dalitek.com.myapplication.R;

public class ZoneAdapter extends ArrayAdapter {
    private Context context;
    private int resourceId;
    @SuppressLint("ResourceType")
    public ZoneAdapter(@NonNull Context context,
                     int resource,
                     @NonNull List objects) {
        super(context, resource, objects);
        resourceId = resource;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ZoneData zoneData = (ZoneData) getItem(position);
        ZoneLayout zoneLayout = new ZoneLayout();
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            zoneLayout.nameView = view.findViewById(R.id.user_zone_name);
            zoneLayout.userimg = view.findViewById(R.id.user_zone_image);
            zoneLayout.word = view.findViewById(R.id.user_zone_word);
            view.setTag(zoneLayout);
        } else {
            view = convertView;
            zoneLayout = (ZoneLayout) view.getTag();
        }
        zoneLayout.nameView.setText(zoneData.getUserName());
        zoneLayout.userimg.setText(zoneData.getImg());
        zoneLayout.word.setText(zoneData.getZoneWord());
        return view;
    }

    class ZoneLayout {
        TextView nameView;
        TextView userimg;
        TextView word;
        Button addButton;
    }
}
