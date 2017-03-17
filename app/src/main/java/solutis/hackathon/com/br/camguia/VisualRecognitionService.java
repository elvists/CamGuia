package solutis.hackathon.com.br.camguia;

import android.os.AsyncTask;
import android.os.Environment;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

import java.io.File;

/**
 * Created by flavia.silva on 17/03/2017.
 */

public class VisualRecognitionService extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
        service.setApiKey("46add95753fb10d4bee7f8db18c92cafc7ca1e51");

        try {

            System.out.println("Classify an image");
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "IMG_20170311_223254073.jpg");
            ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
                    .images(file)
                    .build();
            VisualClassification result = service.classify(options).execute();
            System.out.println(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
        // TextView txt = (TextView) findViewById(R.id.output);
        // txt.setText("Executed"); // txt.setText(result);
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}
