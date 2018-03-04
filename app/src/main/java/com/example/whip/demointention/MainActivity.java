package com.example.whip.demointention;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.ImageButton;

/**
 *
 * IntentionsGlobales montre comment utiliser les intentions pour
 * interagir avec certaines composantes du système Android, par
 * exemple la caméra ou le GPS. L'utilisation des intentions
 * pour retourner des données d'une activité appelée vers une
 * activité appelante est montrée.
 *
 * http://developer.android.com/reference/android/content/Intent.html
 *
 */

public class MainActivity extends Activity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    EditText url, streetAddress;
    ImageView imageView;
    TextView text;
    Button shareButton, cameraButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = (EditText)findViewById(R.id.url);
        ((Button)findViewById(R.id.urlSearch)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Uri searchAddress = Uri.parse(url.getText().toString());
                Intent search = new Intent(android.content.Intent.ACTION_VIEW, searchAddress);
                try
                {
                    startActivity(search);
                } catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Impossible de lancer un navigateur!", Toast.LENGTH_LONG).show();
                }
            }
        });


        streetAddress = (EditText)findViewById(R.id.streetAddress);
        ((Button)findViewById(R.id.addressSearch)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String address = streetAddress.getText().toString().replace(' ', '+');
                Uri searchAddress = Uri.parse("geo:0,0?q=" + address);
                Intent search = new Intent(android.content.Intent.ACTION_VIEW, searchAddress);
                try
                {
                    startActivity(search);
                } catch(Exception e)
                {
                    Toast.makeText(getApplicationContext(), "Impossible de lancer une carte!", Toast.LENGTH_LONG).show();
                }
            }
        });

        text = (TextView)findViewById(R.id.textView);
        shareButton = (Button)findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, text.getText()));
            }
        });

        cameraButton = (Button)findViewById(R.id.cameraButton);
        imageView = (ImageView)findViewById(R.id.imageView);
        cameraButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_IMAGE_CAPTURE)
        {
            if(resultCode == RESULT_OK)
            {
                // Dans le cas de la caméra, on sait qu'il existe une image
                // de type Bitmap dans l'extra "data"
                Bundle extras = data.getExtras();
                Bitmap img = (Bitmap)extras.get("data");
                imageView.setImageBitmap(img);
            }
        }
    }

}
