package com.example.my.instagram_app;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by my on 3/13/2016.
 */
public class InstagramCommentAddapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramCommentAddapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        InstagramPhoto comment = getItem(position);
        //Check if we are using a recycled view, if not we need to inflate
        if(convertView == null) {
            // create a new view from template
            convertView =   LayoutInflater.from(getContext()).inflate(R.layout.itemcomment,parent,false);



        }
        ImageView userImgComent = (ImageView)convertView.findViewById(R.id.userImage);
        TextView tvComment = (TextView)convertView.findViewById(R.id.tvComment);

        String userCmt =" " +  "<font color ='#80e9'>" + "<b>" + comment.username + "</b>" + "</font>" + " " + comment.comment;

        tvComment.setText(Html.fromHtml((userCmt)));
        //tvComment.setText(comment.comment);


        Picasso.with(getContext()).load(comment.userImage).into(userImgComent);

        return  convertView;
    }
}
