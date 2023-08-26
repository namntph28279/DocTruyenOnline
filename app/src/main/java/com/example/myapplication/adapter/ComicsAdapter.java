package com.example.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.example.myapplication.MainActivity2;
import com.example.myapplication.R;
import com.example.myapplication.model.Comic;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicsAdapter extends BaseAdapter {
    private Context context;
    private List<Comic> list;

    public ComicsAdapter(Context context, List<Comic> list) {
        this.context = context;
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_comics, null);
            viewHolder = new ViewHolder();
            viewHolder.mImgAvatarComics = view.findViewById(R.id.img_avatarComics);
            viewHolder.layout_comics = view.findViewById(R.id.layout_comics);
            viewHolder.mTvNameComics = view.findViewById(R.id.tv_nameComics);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Comic comic = list.get(i);
        viewHolder.mTvNameComics.setText(comic.getName());
        Picasso.get().load(comic.getAvatar()).into(viewHolder.mImgAvatarComics);

        viewHolder.layout_comics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity2.class);
                intent.putExtra("comics_id", comic.get_id());
                context.startActivity(intent);
            }
        });

        return view;
    }


    private static class ViewHolder {
        CardView layout_comics;
        ImageView mImgAvatarComics;
        TextView mTvNameComics;
    }
}
