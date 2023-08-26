package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;
public class ImageListAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> imageUrls;

    public ImageListAdapter(Context context, List<String> imageUrls) {
        super(context, R.layout.item_content, imageUrls);
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_content, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = view.findViewById(R.id.imageView);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String imageUrl = imageUrls.get(position);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            // Image URL is available, load the image
            Picasso.get().load(imageUrl)
                    .into(viewHolder.imageView);
        } else {
            viewHolder.imageView.setImageDrawable(null); // Clear any previous image
        }



        return view;
    }

    private static class ViewHolder {
        ImageView imageView;
    }
}
