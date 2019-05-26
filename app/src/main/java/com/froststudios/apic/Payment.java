package com.froststudios.apic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class Payment extends AppCompatActivity {

    private WebView webView;
    private boolean paymentFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        setTitle("Payment");

        webView = findViewById(R.id.paymentView);
        startPayment();
    }

    private void startPayment() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.requestFocus(View.FOCUS_DOWN);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            @Override
            public void onLoadResource(WebView view, String url) {}

            @Override
            public void onPageFinished(WebView view, String url) {
                //super.onPageFinished(view, url);

                if (
                    url.equals(Variables.returnURL) ||
                    url.equals(Variables.cancelURL)
                ) {
                    // Payment completed (successful or cancelled)
                    paymentFinished = true;
                    webView.clearHistory();
                    clearVariables();
                }
            }
        });

        webView.loadUrl(
            "https://payment-test.maksekeskus.ee/pay/1/link.html" +
            "?shop=" + Variables.shopID +
            "&amount=" + getTotalPrice() +
            "&reference=" + Variables.customerID
        );
    }

    private void clearVariables() {
        Variables.originalImageUri = null;
        Variables.finalImageBitmap = null;
        Variables.itemsArray.clear();
        Variables.firstName = null;
        Variables.lastName = null;
        Variables.phone = null;
        Variables.mail = null;
        Variables.address = null;
        Variables.customerID = null;
    }

    private float getTotalPrice() {
        float sum = 0;
        for (Item i : Variables.itemsArray) {
            sum += i.amount * Variables.price;
        }
        return sum;
    }

    private void nextStep() {
        if (paymentFinished) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Payment in progress.", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.accept) {
            nextStep();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_payment_toolbar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {}
}
