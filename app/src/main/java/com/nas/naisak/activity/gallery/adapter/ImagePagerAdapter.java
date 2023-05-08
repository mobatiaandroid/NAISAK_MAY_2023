package com.nas.naisak.activity.gallery.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.nas.naisak.R;
import com.nas.naisak.activity.gallery.model.GetPhotoResponseModel;
import com.nas.naisak.constants.CustomProgressBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by RIJO K JOSE on 29/3/16.
 */
public class ImagePagerAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<GetPhotoResponseModel.Data> mImagesArrayListBg;
    ArrayList<String> mImagesArrayListUrlBg;
    private LayoutInflater mInflaters;
    String orientation;
    RelativeLayout relativeLayout;
    int imageHeight;
    int imageWidth;
    int pos;
    Bitmap myImage;
    ImageView imageView;
    ImageView downloadimageView;
    ImageView share;
    CustomProgressBar pDialog;
    Bitmap bitmapShareImg;

    public ImagePagerAdapter(Context context, ArrayList<GetPhotoResponseModel.Data> mImagesArrayList, String orientation)
    {
        this.mImagesArrayListBg=new ArrayList<GetPhotoResponseModel.Data>();
        this.mContext=context;
        this.mImagesArrayListBg=mImagesArrayList;
        this.orientation=orientation;
    }
    public ImagePagerAdapter(ArrayList<String> mImagesArrayListUrlBg, Context context)
    {
        this.mImagesArrayListUrlBg=new ArrayList<String>();
        this.mContext=context;
        this.mImagesArrayListUrlBg=mImagesArrayListUrlBg;
    }
    @Override
    public int getCount() {
        return mImagesArrayListBg.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        pos=position;
        View pageview = null;
        pDialog = new CustomProgressBar(mContext,
                R.drawable.spinner);
        mInflaters = LayoutInflater.from(mContext);
        pageview = mInflaters.inflate(R.layout.layout_photo_imagepager_adapter, null);
         imageView = pageview.findViewById(R.id.adImg);
        downloadimageView = pageview.findViewById(R.id.download);
        share = pageview.findViewById(R.id.share);
        ImageView back= pageview.findViewById(R.id.back);
        relativeLayout= pageview.findViewById(R.id.relativeLayout);
        if(!mImagesArrayListBg.get(position).getImage().equals("")){
            if(orientation.equals("portrait")) {
                imageView.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                Glide.with(mContext).load(mImagesArrayListBg.get(position).getImage()).fitCenter()
                        .into(imageView);
//                loadImage(mContext, (CommonMethods.replace(mImagesArrayListBg.get(position).getImage()), imageView);
//                new  DownloadImg().execute();

            } else {
                imageView.setVisibility(View.VISIBLE);

                // relativeLayout.setVisibility(View.GONE);
                back.setVisibility(View.GONE);
                Glide.with(mContext).load(mImagesArrayListBg.get(position).getImage()).fitCenter()
                        .into(imageView);
//                loadImage(mContext, AppUtils.replace(mImagesArrayListBg.get(position).getPhotoUrl()), imageView);

/*
                Picasso.with(mContext).load(AppUtils.replace(mImagesArrayListBg.get(position).getPhotoUrl()))
                        .into(imageView, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }
                        });*/
            }
        }





        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)mContext).finish();
            }
        });
        downloadimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file =  new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "NAS_PHOTO_" + System.currentTimeMillis() + ".png");
                file.getParentFile().mkdirs();
                DownloadImage mDowloadImage=new DownloadImage(mImagesArrayListBg.get(position).getImage().replaceAll(" ","%20"),file);
                mDowloadImage.execute();
            }
        });


        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// Get access to the URI for the bitmap
                GetImage imgtask=new  GetImage(position);
                imgtask.execute();
            }
        });



        container.

                addView(pageview, 0);

        return pageview;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }



   /* public class DownloadImg extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            final CustomProgressBar pDialog = new CustomProgressBar(mContext,
                    R.drawable.spinner);
        pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            myImage = getBitmapFromURL(AppUtils.replace(mImagesArrayListBg.get(pos).getPhotoUrl()));

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pDialog.dismiss();
            Drawable dr = new BitmapDrawable(myImage);
            if(orientation.equals("portrait")) {
                relativeLayout.setBackgroundDrawable(dr);
            }

//            imageView.setBackground(dr);
//            imageView.setVisibility(View.VISIBLE);

        }
    }*/
    private Target mTarget;

    void loadImage(Context context, String url, final ImageView img) {

        pDialog.show();

        mTarget = new Target() {
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                //Do something

                img.setImageBitmap(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

        Picasso.with(context)
                .load(url)
                .into(mTarget);
        pDialog.dismiss();
    }
    public  void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();

        } catch(FileNotFoundException e) {

            return; // swallow a 404
        } catch (IOException e) {

            return; // swallow a 404
        }
    }
    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception

            e.printStackTrace();
            return null;
        }
    }

    public class GetImage extends AsyncTask<Void, String, Void> {
       int  positions;
        GetImage(int posit)
        {
            positions=posit;
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            bitmapShareImg=getBitmapFromURL((mImagesArrayListBg.get(positions).getImage()).replaceAll(" ","%20"));
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            shareImage(bitmapShareImg);

        }

    }
    public class DownloadImage extends AsyncTask<Void, String, Void> {
        File mFile;
        String URL;
        DownloadImage(String mURL ,File mFil)
        {
            mFile=mFil;
            URL=mURL;
        }
        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            downloadFile(URL,mFile);

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            Toast.makeText(mContext, "Image saved successfully.", Toast.LENGTH_SHORT).show();

        }

    }

//    public class DownloadImage extends AsyncTask<Void, String, Void> {
//        File mFile;
//        String URL;
//        DownloadImage(String mURL ,File mFil)
//        {
//            mFile=mFil;
//            URL=mURL;
//        }
//        @Override
//        protected Void doInBackground(Void... params) {
//            // TODO Auto-generated method stub
//            downloadFile(URL,mFile);
//
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            Toast.makeText(mContext, "Image saved successfully.", Toast.LENGTH_SHORT).show();
//
//        }
//
//    }

    private void shareImage(Bitmap mBitmap) {

        String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                mBitmap, "Image I want to share", null);


        if(path!=null) {
            Uri uri = Uri.parse(path);
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_TEXT, mContext.getResources()
                    .getString(R.string.app_name));
            shareIntent.setType("image/*");
            mContext.startActivity(Intent.createChooser(shareIntent, mContext.getApplicationContext()
                    .getResources().getString(R.string.app_name)));
        }
        else
        {
            Toast.makeText(mContext,"Path is empty",Toast.LENGTH_SHORT).show();
        }
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
//    public void getLocalBitmapUri(ImageView imageView) {
//        // Extract Bitmap from ImageView drawable
//        Drawable drawable = imageView.getDrawable();
//        Bitmap bmp = null;
//        if (drawable instanceof BitmapDrawable){
//            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//        } else {
////            return null;
//        }
//        // Store image to default external storage directory
//        Uri bmpUri = null;
//        try {
//            File file =  new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DOWNLOADS), "NAS_PHOTO_" + System.currentTimeMillis() + ".png");
//            file.getParentFile().mkdirs();
//            FileOutputStream out = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
//            out.close();
//            bmpUri = Uri.fromFile(file);
//            Toast.makeText(mContext, "Image saved successfully.", Toast.LENGTH_SHORT).show();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    // DownloadImage AsyncTask
//    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            // Create a progressdialog
////               mProgressDialog = new ProgressDialog(mContext);
////            // Set progressdialog title
////            mProgressDialog.setTitle("Download Photo");
////            // Set progressdialog message
////            mProgressDialog.setMessage("Loading...");
////            mProgressDialog.setIndeterminate(false);
////            // Show progressdialog
////            mProgressDialog.show();
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... URL) {
//
//            String imageURL = URL[0];
//
//            Bitmap bitmap = null;
//            try {
//                // Download Image from URL
//                InputStream input = new java.net.URL(imageURL).openStream();
//                // Decode Bitmap
//                bitmap = BitmapFactory.decodeStream(input);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap result) {
//            // Set the bitmap into ImageView
//            // Close progressdialog
////            mProgressDialog.dismiss();
//        }
//    }
    public Bitmap BITMAP_RESIZER(Bitmap bitmap,int newWidth,int newHeight) {
        Bitmap scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);

        float ratioX = newWidth / (float) bitmap.getWidth();
        float ratioY = newHeight / (float) bitmap.getHeight();
        float middleX = newWidth / 2.0f;
        float middleY = newHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;

    }
}
