package solutis.hackathon.com.br.camguia;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements VisualRecognitionServiceResult{

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private TextView textView;
    private VisualRecognitionService visualRecognitionService = new VisualRecognitionService();
    private TextToSpeechService textToSpeechService = new TextToSpeechService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("android.intent.extra.quickCapture", true);
      //  cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, "teste");
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);

        visualRecognitionService.setVisualRecognitionServiceResult(this);
        visualRecognitionService.execute();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            MediaStore.Images.Media.insertImage(getContentResolver(), photo, "teste" , "teste desc");
            this.imageView = (ImageView)this.findViewById(R.id.imageView1);
            imageView.setImageBitmap(photo);
            createDirectoryAndSaveFile(photo, "manga");
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {

        File direct = new File(Environment.getExternalStorageDirectory() + "/DirName");

        if (!direct.exists()) {
            File wallpaperDirectory = new File("/sdcard/DirName/");
            wallpaperDirectory.mkdirs();
        }

        File file = new File(new File("/sdcard/DirName/"), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 1, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String output) {
        System.out.println(output);
        textToSpeechService.execute(output);
    }
}
