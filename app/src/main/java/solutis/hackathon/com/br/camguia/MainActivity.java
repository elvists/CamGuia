package solutis.hackathon.com.br.camguia;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //new VisualRecognitionService().execute("");
        //new TextToSpeechService().execute("");


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");

            photo = Bitmap.createScaledBitmap(photo, photo.getWidth()/2, photo.getHeight()/2, false);

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
            File f = new File(Environment.getExternalStorageDirectory()
                    + File.separator + "Imagename.jpg");
            try {


                f.createNewFile();
                FileOutputStream fo = new FileOutputStream(f);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


            MediaStore.Images.Media.insertImage(getContentResolver(), photo, "teste", null);
            this.imageView = (ImageView)this.findViewById(R.id.imageView1);
            imageView.setImageBitmap(photo);
        }
    }
}
