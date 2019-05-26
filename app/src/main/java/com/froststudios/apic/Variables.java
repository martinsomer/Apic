package com.froststudios.apic;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;

class Variables {
    private Variables(){}

    static final float price = 1.0f;
    static final String shopID = "24f5da45-7ecf-4011-bf43-4d2eb7d1e7a1";
    static final String serverURL = "https://getapic.ee/upload/upload.php";
    static final String returnURL = "https://getapic.ee/payment/return.html";
    static final String cancelURL = "https://getapic.ee/payment/cancel.html";

    static Uri originalImageUri;
    static Bitmap finalImageBitmap;

    static ArrayList<Item> itemsArray = new ArrayList<>();

    static String firstName;
    static String lastName;
    static String phone;
    static String mail;
    static String address;

    static String customerID;
}
