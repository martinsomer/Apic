package com.froststudios.apic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Upload extends AppCompatActivity {

    private TextView progressText;
    private ProgressBar progressBar;
    private Button retryButton;

    private int progressPerImage = 100 / Variables.itemsArray.size();
    private int imagesUploaded = 0;
    private boolean uploadSuccessful = false;
    private boolean currentlyUploading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        setTitle("Uploading Photos");

        progressText = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);
        retryButton = findViewById(R.id.retry);

        retryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                attemptUpload();
                retryButton.setVisibility(View.GONE);
            }
        });

        attemptUpload();
    }

    private void attemptUpload() {
        try {
            uploadImage();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Exception occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadImage() throws IOException {
        SSLContext sslContext;
        TrustManager[] trustManagers;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            InputStream certInputStream = getAssets().open("getapic.pem");
            BufferedInputStream bis = new BufferedInputStream(certInputStream);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                Certificate cert = certificateFactory.generateCertificate(bis);
                keyStore.setCertificateEntry("getapic.ee", cert);
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            trustManagers = trustManagerFactory.getTrustManagers();
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, null);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustManagers[0])
                .build();

        MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("firstName", Variables.firstName)
            .addFormDataPart("lastName",  Variables.lastName)
            .addFormDataPart("phone",     Variables.phone)
            .addFormDataPart("mail",      Variables.mail)
            .addFormDataPart("address",   Variables.address);

        for (Item i : Variables.itemsArray) {
            Bitmap img = i.bitmap;

            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

            requestBody.addFormDataPart("imageFile[]", String.valueOf(i.amount),
                new ProgressTracker(MediaType.parse("image/jpeg"), outStream, new ProgressTracker.ProgressListener() {

                    public void transferred(long total, long length) {
                        int progress = (int) (total / (float) length * 100);

                        if (progress == 100) {
                            // Prevent adding progress when all uploads are complete
                            if (imagesUploaded < Variables.itemsArray.size() - 1) {
                                imagesUploaded += 1;
                            } else {
                                progressBar.setProgress(100);
                                return;
                            }

                        }

                        int totalProgress = (imagesUploaded * progressPerImage) + (progress / Variables.itemsArray.size());

                        String text = "Progress: " + totalProgress + "%";

                        progressText.setText(text);
                        progressBar.setProgress(totalProgress);
                    }
                })
            );

            outStream.flush();
            outStream.close();
        }

        Request request = new Request.Builder()
                .url(Variables.serverURL)
                .addHeader("Accept", "application/json")
                .post(requestBody.build())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                handleFailure();
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                int responseCode = response.code();

                try {
                    ResponseBody responseBody = response.body();
                    String responseBodyAsString = responseBody.string();
                    JSONObject json = new JSONObject(responseBodyAsString);

                    if (responseCode == 200) {
                        String customerID = json.getString("customerID");
                        handleSuccess(customerID);
                    } else {
                        int errorCode = Integer.parseInt(json.getString("errorCode"));
                        handleError(errorCode);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void handleFailure() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                retryButton.setVisibility(View.VISIBLE);
            }
        });

        progressText.setText("Upload Failed");
        currentlyUploading = false;
    }

    private void handleError(int errorCode) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                retryButton.setVisibility(View.VISIBLE);
            }
        });

        progressText.setText("Error Occurred. Code: " + errorCode);
        currentlyUploading = false;
    }

    private void handleSuccess(String customerID) {
        Variables.customerID = customerID;
        progressText.setText("Upload Successful");
        currentlyUploading = false;
        uploadSuccessful = true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.accept) {
            nextStep();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private void nextStep() {
        if (uploadSuccessful) {
            Intent intent = new Intent(this, Payment.class);
            startActivity(intent);
        } else {
            if (currentlyUploading) {
                Toast.makeText(this, "Upload in progress.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Upload unsuccessful.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {}
}
