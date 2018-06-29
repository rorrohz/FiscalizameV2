package com.example.rodrigo.fiscalizamev2.m_UI;

import android.content.Context;
import android.widget.ImageView;

import com.example.rodrigo.fiscalizamev2.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Rodrigo on 27-06-2018.
 */

public class PicassoClient {

    public static void downloadImage(Context c, String imageUrl, ImageView img)
    {
        if(imageUrl!=null && imageUrl.length()>0)
        {
            Picasso.with(c).load(imageUrl).placeholder(R.drawable.placeholder).into(img);
        }
        else {
            Picasso.with(c).load(R.drawable.placeholder).into(img);
        }
    }
}
