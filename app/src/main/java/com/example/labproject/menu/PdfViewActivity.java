package com.example.labproject.menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.denzcoskun.imageslider.animations.Toss;
import com.example.labproject.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PdfViewActivity extends AppCompatActivity {

    private static String url;
    private PDFView pdfView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_view);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("College_Management");

        url = getIntent().getStringExtra("pdfUrl");

        pdfView = findViewById(R.id.pdfView);

        new PdfDownloadTask().execute(url);
    }

    private class PdfDownloadTask extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {
                String pdfUrl = strings[0];
                if (pdfUrl != null && !pdfUrl.isEmpty()) {
                    URL url = new URL(pdfUrl);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    } else {
                        // Handle the case where the response code is not HTTP_OK
                    }
                } else {
                    // Handle the case where the URL is null or empty
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            if (inputStream != null) {
                pdfView.fromStream(inputStream).load();
            } else {
                // Handle the case where the input stream is null (e.g., invalid URL)
                // You can show an error message or take appropriate action.
            }
        }
    }

}