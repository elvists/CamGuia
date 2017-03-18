package solutis.hackathon.com.br.camguia;

import android.os.AsyncTask;
import android.os.Environment;

import com.ibm.watson.developer_cloud.language_translator.v2.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v2.model.Language;
import com.ibm.watson.developer_cloud.language_translator.v2.model.TranslationResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ImageClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by flavia.silva on 17/03/2017.
 */

public class LanguageTranslatorService extends AsyncTask<String, Void, String> {
    private LanguageTranslatorServiceResult languageTranslatorServiceResult;

    @Override
    protected String doInBackground(String... params) {
        String text = params[0];
        LanguageTranslator service = new LanguageTranslator();
        service.setUsernameAndPassword("7e9e904d-89b6-4b33-be8c-54ee552ccbe9","hR1PliA0hhez");

        try {
            System.out.println("Translate a text");

            TranslationResult translated = service.translate(text, Language.ENGLISH, Language.PORTUGUESE).execute();
            text = translated.getFirstTranslation();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return text;
    }


    @Override
    protected void onPostExecute(String result) {
        languageTranslatorServiceResult.processFinishTranslator(result);
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}

    public LanguageTranslatorServiceResult getLanguageTranslatorServiceResult() {
        return languageTranslatorServiceResult;
    }

    public void setLanguageTranslatorServiceResult(LanguageTranslatorServiceResult languageTranslatorServiceResult) {
        this.languageTranslatorServiceResult = languageTranslatorServiceResult;
    }
}
