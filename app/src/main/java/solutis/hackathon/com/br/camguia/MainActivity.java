package solutis.hackathon.com.br.camguia;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("android.intent.extra.quickCapture", true);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, "teste");
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        //new VisualRecognitionService().execute("");
        //new TextToSpeechService().execute("");


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            MediaStore.Images.Media.insertImage(getContentResolver(), photo, "teste" , "teste desc");
            this.imageView = (ImageView)this.findViewById(R.id.imageView1);
            imageView.setImageBitmap(photo);
        }
    }
}
