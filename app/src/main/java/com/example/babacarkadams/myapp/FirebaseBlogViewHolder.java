package com.example.babacarkadams.myapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by layely on 21/11/2017.
 */

public class FirebaseBlogViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private static final int MAX_WIDTH = 200;
    private static final int MAX_HEIGHT = 200;

    View mView;
    Context mContext;

    public FirebaseBlogViewHolder(View itemView) {
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void bindBlog(Blog blog) {
        ImageView blogImageView = (ImageView) mView.findViewById(R.id.post_image);
        TextView titleTextView = (TextView) mView.findViewById(R.id.post_title);
        TextView descriptionTextView = (TextView) mView.findViewById(R.id.post_text);

        Picasso.with(mContext)
                .load(blog.getImage())
                .resize(MAX_WIDTH, MAX_HEIGHT)
                .centerCrop()
                .into(blogImageView);

        titleTextView.setText(blog.getTitle());
        descriptionTextView.setText(blog.getDesc());
    }

    @Override
    public void onClick(View v) {

    }
}
