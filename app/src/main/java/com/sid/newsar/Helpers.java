package com.sid.newsar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

public class Helpers {

    public static void openWebPage(Context context, String url) {
        Toast.makeText(context, "Opening " + url + " ...", Toast.LENGTH_SHORT).show();
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

}
