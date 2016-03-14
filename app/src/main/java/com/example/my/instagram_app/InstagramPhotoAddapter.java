package com.example.my.instagram_app;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by my on 3/11/2016.
 */
public class InstagramPhotoAddapter extends ArrayAdapter<InstagramPhoto> {
    public InstagramPhotoAddapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);

    }
        //what our item looks like
        //use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       //Get the data item for this position
        InstagramPhoto photo = getItem(position);
        //Check if we are using a recycled view, if not we need to inflate
        if(convertView == null) {
            // create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemphoto, parent, false);

        }
        // lookup the views for populating the data(image, caption)
        TextView tvCaption = (TextView)convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
        TextView dateTime = (TextView)convertView.findViewById(R.id.dateTime);
        ImageView userImage = (ImageView)convertView.findViewById(R.id.userImage);
        TextView tvComment = (TextView)convertView.findViewById(R.id.comment);
        TextView tvUsername = (TextView)convertView.findViewById(R.id.tvusername);
        TextView tvLikes = (TextView)convertView.findViewById(R.id.tvLikes);
        TextView tvNumbercomment  = (TextView)convertView.findViewById(R.id.numberComment);
        TextView tvComment2 = (TextView)convertView.findViewById(R.id.comment2);


        //Insert the model data into each of the view items
        tvCaption.setText(photo.caption);
        // Clear out the ImageView
        ivPhoto.setBackgroundResource(0);
        // Insert the image using picaso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);
        // Datetime
        long dateUtil = Long.valueOf(photo.dateUtils)*1000;
        dateTime.setText(DateUtils.getRelativeTimeSpanString(dateUtil, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        //User Image
        Picasso.with(getContext()).load(photo.userImage).into(userImage);
        //comment1
        String userCmt =" " +  "<font color ='#80e9'>" + "<b>" + photo.userComment + "</b>" + "</font>" + " " + photo.comment;
        tvComment.setText(Html.fromHtml((userCmt)));
        //comment2
        String userCmt2 =" " +  "<font color ='#80e9'>" + "<b>" + photo.userComment2 + "</b>" + "</font>" + " " + photo.comment2;
        tvComment.setText(Html.fromHtml((userCmt2)));

        //username
        String username = "<font color ='#80e9'>" + "<b>" + photo.username+" ";
        tvUsername.setText(Html.fromHtml((username)));
        // count like
        String Like = " "+"<font color ='#80e9'>" + "<b>" + photo.LikesCount+" likes"+"</b>" + "</font>" ;
        tvLikes.setText(Html.fromHtml((Like)));
        // number comment
        String comment = " "+"<font color ='#BDBDBD'>" + "<b>" +"View all "+ photo.numberComment+ " " + "comment"+"</b>" + "</font>" ;
        tvNumbercomment.setText(Html.fromHtml((comment)));

        // return the created item a view
        return convertView;
    }
}
