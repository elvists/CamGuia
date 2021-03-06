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
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements VisualRecognitionServiceResult, LanguageTranslatorServiceResult{

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    String mCurrentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;

    private VisualRecognitionService visualRecognitionService = new VisualRecognitionService();
    private TextToSpeechService textToSpeechService = new TextToSpeechService();
    private LanguageTranslatorService languageTranslatorService = new LanguageTranslatorService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                1);


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
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "solutis.hackathon.com.br.fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
            }


            visualRecognitionService.setVisualRecognitionServiceResult(this);
            visualRecognitionService.execute();
        }
    }



    @Override
    public void processFinish(String output) {
        System.out.println(output);
        languageTranslatorService.setLanguageTranslatorServiceResult(this);
        languageTranslatorService.execute(output);
    }

    @Override
    public void processFinishTranslator(String output) {
        System.out.println(output);
        textToSpeechService.execute(output);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalCacheDir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
