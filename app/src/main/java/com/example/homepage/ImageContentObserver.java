package com.example.homepage;


import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class ImageContentObserver extends ContentObserver {
    private Context mContext;
    private Uri mUri;

    public ImageContentObserver(Context context, Uri uri) {
        super(null);
        mContext = context;
        mUri = uri;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        if (mUri.equals(uri)) {
            String[] projection = { MediaStore.Images.Media.DATA };
            String sortOrder = MediaStore.Images.Media.DATE_ADDED + " DESC";

            Cursor cursor = mContext.getContentResolver().query(uri, projection, null, null, sortOrder);

            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String imagePath = cursor.getString(column_index);
                cursor.close();

                // Do something with the image path, like display it in an ImageView
            }
        }
    }
}

