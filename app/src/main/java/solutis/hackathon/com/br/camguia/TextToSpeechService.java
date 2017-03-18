package solutis.hackathon.com.br.camguia;

import android.os.AsyncTask;
import android.os.Environment;

import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by flavia.silva on 17/03/2017.
 */

public class TextToSpeechService extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        TextToSpeech service = new TextToSpeech();
        service.setUsernameAndPassword("f4696abd-97b7-4018-918d-bd44bbb13e37", "VCREA3ElTGms");

        try {
            String text = params[0];
            InputStream stream = service.synthesize(text, Voice.EN_ALLISON,
                    AudioFormat.WAV).execute();
            InputStream in = WaveUtils.reWriteWaveHeader(stream);

            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "CamGuia.wav");

            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
            stream.close();
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
