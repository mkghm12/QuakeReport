package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QuakeAdapter extends ArrayAdapter<Earthquake> {
    private static final String LOCATION_SEPARATOR = " of ";
    public QuakeAdapter(@NonNull Context context, List<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Earthquake item=getItem(position);
        View listItemView = convertView;
        if (convertView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.quake_list_item,parent,false);
        }
        TextView magView =(TextView)listItemView.findViewById(R.id.magnitude);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        if (item != null) {
            magView.setText(decimalFormat.format(item.getMagnitude()));
        }
        GradientDrawable magnitudeCircle = (GradientDrawable) magView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = 0;
        if (item != null) {
            magnitudeColor = getMagnitudeColor(item.getMagnitude());
        }

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        TextView locView1 =(TextView)listItemView.findViewById(R.id.location_offset);
        TextView locView2 =(TextView)listItemView.findViewById(R.id.primary_location);
        String locationOffset ;
        String location = null;
        if (item != null) {
            location = item.getLocation();
        }
        String primaryLocation ;
        if (location.contains(LOCATION_SEPARATOR)){
            String[] loc = location.split(LOCATION_SEPARATOR);
            locationOffset = loc[0]+ LOCATION_SEPARATOR;
            primaryLocation = loc[1];
        }else {
            locationOffset = getContext().getString(R.string.near_the);
            primaryLocation = location;
        }
        locView1.setText(locationOffset);
        locView2.setText(primaryLocation);
        TextView DateView =(TextView)listItemView.findViewById(R.id.date);
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        Date dateObject = new Date(item.getTime());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        String dateToDisplay = dateFormat.format(dateObject);
        DateView.setText(dateToDisplay);
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm:ss a");
        String timeToDisplay = timeFormat.format(dateObject);
        timeView.setText(timeToDisplay);
        return listItemView;
    }

    public int getMagnitudeColor(double magnitude){
        int magFloor =(int)Math.floor(magnitude);
        int colorId;
        switch (magFloor){
            case 0:
            case 1:
                colorId=R.color.magnitude1;
                break;
            case 2:
                colorId=R.color.magnitude2;
                break;
            case 3:
                colorId=R.color.magnitude3;
                break;
            case 4:
                colorId=R.color.magnitude4;
                break;
            case 5:
                colorId=R.color.magnitude5;
                break;
            case 6:
                colorId=R.color.magnitude6;
                break;
            case 7:
                colorId=R.color.magnitude7;
                break;
            case 8:
                colorId=R.color.magnitude8;
                break;
            case 9:
                colorId=R.color.magnitude9;
                break;
            default:
                colorId=R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(),colorId);
    }
}
