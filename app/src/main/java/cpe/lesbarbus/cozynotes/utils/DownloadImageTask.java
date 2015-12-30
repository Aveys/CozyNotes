package cpe.lesbarbus.cozynotes.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

/**
 * Created by arthurveys on 17/12/15.
 */

/**
 * Async task for downloading images
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;

    /**
     * Construct the task
     * @param bmImage the Imageview to load
     */
    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    /**
     * Load the image for a HTTP stream
     * @param urls the url to load
     * @return the Bitmap image
     */
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mIcon11;
    }

    /**
     * Affect the loaded image to the Image View
     * @param result the loaded image
     */
    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}