package com.maker.use.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import com.maker.use.R;
import com.maker.use.global.UsedMarketURL;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ShowImageActivity extends Activity {

    ImageView iv;
    String path;
    String type;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        path = intent.getStringExtra("path");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (type.equals("icon")) {
            setContentView(R.layout.activity_show_image);
            iv = (ImageView) findViewById(R.id.showimage);
            Picasso.with(this).load(UsedMarketURL.HEAD + path.replace("_", "")).into(iv, new Callback() {
                @Override
                public void onSuccess() {
                    mAttacher = new PhotoViewAttacher(iv);
                }

                @Override
                public void onError() {
                }
            });
        }
    }

}
