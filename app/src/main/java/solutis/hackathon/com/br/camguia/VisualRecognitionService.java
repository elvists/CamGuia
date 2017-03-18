package solutis.hackathon.com.br.camguia;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.JsonReader;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by flavia.silva on 17/03/2017.
 */

public class VisualRecognitionService extends AsyncTask<String, Void, String> {
    private VisualRecognitionServiceResult visualRecognitionServiceResult;

    @Override
    protected String doInBackground(String... params) {
        VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
        service.setApiKey("46add95753fb10d4bee7f8db18c92cafc7ca1e51");
        String text = "";

        try {

            System.out.println("Classify an image");
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "imagem-2019.jpg");
            ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
                    .images(file)
                    .build();
            VisualClassification result = service.classify(options).execute();
            System.out.println(result);
            text = findClassesMaxScore(result);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }

    private String findClassesMaxScore(VisualClassification result) {
        List<ImageClassification> imageClassifications = result.getImages();
        Double maxScore = 0.9D;
        List<VisualClassifier.VisualClass> maxOdds = new ArrayList<VisualClassifier.VisualClass>();
        maxScore = extractMaxOdds(imageClassifications, maxScore, maxOdds);

        if (maxOdds.isEmpty()) {
            extractMaxOdds(imageClassifications, maxScore, maxOdds);
        }

        String text = "";
        for (VisualClassifier.VisualClass visualClass : maxOdds) {
            Double score = Double.valueOf(String.format(Locale.US, "%.2f", visualClass.getScore()));
            text += "There are " + (score * 100D) + "% chances of being a " + visualClass.getName() + ". ";
        }

        return text;
    }

    private Double extractMaxOdds(List<ImageClassification> imageClassifications, Double validScore, List<VisualClassifier.VisualClass> maxOdds) {
        Double maxScore = 0D;
        for (ImageClassification imageClassification : imageClassifications) {
            List<VisualClassifier> visualClassifiers = imageClassification.getClassifiers();
            for (VisualClassifier visualClassifier : visualClassifiers) {
                List<VisualClassifier.VisualClass>  visualClasses = visualClassifier.getClasses();
                for (VisualClassifier.VisualClass visualClass : visualClasses) {
                    maxScore = visualClass.getScore() > maxScore ? visualClass.getScore() : maxScore;
                    if (visualClass.getScore() >= validScore) {
                        maxOdds.add(visualClass);
                    }
                }
            }
        }

        return maxScore;
    }

    @Override
    protected void onPostExecute(String result) {
        visualRecognitionServiceResult.processFinish(result);
        // TextView txt = (TextView) findViewById(R.id.output);
        // txt.setText("Executed"); // txt.setText(result);
        // might want to change "executed" for the returned string passed
        // into onPostExecute() but that is upto you
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

    public VisualRecognitionServiceResult getVisualRecognitionServiceResult() {
        return visualRecognitionServiceResult;
    }

    public void setVisualRecognitionServiceResult(VisualRecognitionServiceResult visualRecognitionServiceResult) {
        this.visualRecognitionServiceResult = visualRecognitionServiceResult;
    }
}
