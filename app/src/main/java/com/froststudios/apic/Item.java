package com.froststudios.apic;

import android.graphics.Bitmap;

class Item {
    Bitmap bitmap;
    int amount;

    Item(Bitmap bitmap, int amount) {
        this.bitmap = bitmap;
        this.amount = amount;
    }
}
