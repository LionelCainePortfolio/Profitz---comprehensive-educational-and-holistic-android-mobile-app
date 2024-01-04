package com.profitz.app.promos.async;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;


import com.profitz.app.R;import com.profitz.app.promos.interfaces.DownloadAttachmentListener;
import com.profitz.app.util.Utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadImageTask extends AsyncTask<Void, Void, Void> {
    private final String imagePath;
    private final String fileName;
    private final Context context;
    private final DownloadAttachmentListener downloadAttachmentListener;

    public DownloadImageTask(Context context, String imagePath, String fileName, DownloadAttachmentListener downloadAttachmentListener) {
        this.context = context;
        this.imagePath = imagePath;
        this.fileName = fileName;
        this.downloadAttachmentListener = downloadAttachmentListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            File directoryPath = new File(root.getAbsolutePath() + "/" + context.getString(R.string.app_name));
            if (!directoryPath.exists()) {
                directoryPath.mkdir();
            }
            File cachePath = new File(directoryPath + "/" + fileName);
            cachePath.createNewFile();
            byte[] buffer = new byte[1024];
            int bufferLength;
            String urlPath = Utility.BASE_URL + "/" + imagePath;
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutput = new FileOutputStream(cachePath);
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.write(buffer);
            fileOutput.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (downloadAttachmentListener != null) {
            downloadAttachmentListener.onDownloaded();
        }
    }
}
