package com.example.shayan2013.itemmeasurev01;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


public class MainActivity extends Activity implements OnClickListener {

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    //captured picture uri
    private Uri picUri;
    //keep track of cropping intent
    final int PIC_CROP = 2;
    private Button cropBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //retrieve a reference to the UI button
        Button captureBtn = (Button)findViewById(R.id.capture_btn);
        //Double Crop for features
        cropBtn = (Button)findViewById(R.id.crop_btn);
        cropBtn.setEnabled(false);
        //handle button clicks
        captureBtn.setOnClickListener(this);
    }



   /* public void cropClicked(View v){


        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(Bitmap, "BitmapDrawable/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 210);
            cropIntent.putExtra("aspectY", 297);
            //indicate output X and Y

            cropIntent.putExtra("outputX", 210);
            cropIntent.putExtra("outputY", 297);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }*/


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.capture_btn) {

            try {
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            }catch(ActivityNotFoundException anfe){
                //display an error message
                String errorMessage = "Whoops - your device doesn't support capturing images!";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            //user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){
                //get the Uri for the captured image
                picUri = data.getData();
                //carry out the crop operation
                performCrop();
            }//user is returning from cropping the image
            else if(requestCode == PIC_CROP){
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = extras.getParcelable("data");
                //retrieve a reference to the ImageView
                ImageView picView = (ImageView)findViewById(R.id.picture);


                Drawable picPic = new BitmapDrawable(thePic);

                Drawable[] layers = new Drawable[2];
                layers[0] = picPic;
                layers[1] = getResources().getDrawable(R.drawable.uniform_3);
                LayerDrawable layerDrawable = new LayerDrawable(layers);
                picView.setImageDrawable(layerDrawable);

                cropBtn.setEnabled(true);
                //BitmapDrawable btmd = (BitmapDrawable) geSingleDrawable(layerDrawable);


            }
        }
    }


//for double crop the Image(features)

    public Drawable geSingleDrawable(LayerDrawable layerDrawable){

        int resourceBitmapHeight = 297, resourceBitmapWidth = 210;

        float widthInInches = 0.9f;

        int widthInPixels = (int)(widthInInches * getResources().getDisplayMetrics().densityDpi);
        int heightInPixels = (int)(widthInPixels * resourceBitmapHeight / resourceBitmapWidth);

        int insetLeft = 10, insetTop = 10, insetRight = 10, insetBottom = 10;

        layerDrawable.setLayerInset(1, insetLeft, insetTop, insetRight, insetBottom);

        Bitmap bitmap = Bitmap.createBitmap(widthInPixels, heightInPixels, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        layerDrawable.setBounds(0, 0, widthInPixels, heightInPixels);
        layerDrawable.draw(canvas);

        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        bitmapDrawable.setBounds(0, 0, widthInPixels, heightInPixels);

        return bitmapDrawable;
    }


    //invert Image(features)
   /* public static Bitmap invertImage(Bitmap original){

        Bitmap finalImage = Bitmap.createBitmap(original.getWidth(), original.getHeight(), original.getConfig());

        int A, R, G, B;
        int pixelColor;
        int height = original.getHeight();
        int width = original.getWidth();

        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                pixelColor = original.getPixel(x, y);
                A = 255 - Color.alpha(pixelColor);
                R = 255 - Color.red(pixelColor);
                G = 255 - Color.green(pixelColor);
                B = 255 - Color.blue(pixelColor);
                finalImage.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }
    }*/




    private void performCrop() {

        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            //Din A4(210mm * 297mm)
            //we are going to change it wenn we want to use A3 or any othe A-paper
            cropIntent.putExtra("aspectX", 210);
            cropIntent.putExtra("aspectY", 297);
            //indicate output X and Y

            cropIntent.putExtra("outputX", 210);
            cropIntent.putExtra("outputY", 297);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);

        }
        catch(ActivityNotFoundException anfe){
            //display an error message
            String errorMessage = "Whoops - your device doesn't support the crop action!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }

    }




}

//we try to fix the features to improve the application until tuesday for the presentation
//the user can zoom with the double crop (feautures 1)
//the user can change the contrast of the image to see the item better in the lines
//the next features is going to be detect the item to measuring it simply from the image
