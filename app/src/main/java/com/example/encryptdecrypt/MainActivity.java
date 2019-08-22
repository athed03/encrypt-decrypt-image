package com.example.encryptdecrypt;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

//    private static final String TAG = "";
    Button encrypt, decrytp;
    ImageView gambar;
    TextView text;
    Cipher cipher;
    SecretKeySpec key;
    int ctLength;
    byte[] cipherText;

    private final static int DEFAULT_READ_WRITE_BLOCK_BUFFER_SIZE = 1024;
    private final static String ALGO_VIDEO_ENCRYPTOR = "AES/CBC/PKCS5Padding";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        encrypt = findViewById(R.id.encrypt);
        decrytp = findViewById(R.id.decrypt);
        gambar  = findViewById(R.id.gambar);
        text = findViewById(R.id.enkripsi);

//        public  boolean isWriteStoragePermissionGranted() {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Log.e("adit","Permission is granted2");
//                    return true;
                } else {

                    Log.e("Adit","Permission is revoked2");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
//                    return false;
                }
            }
            else { //permission is automatically granted on sdk<23 upon installation
                Log.v("Adit","Permission is granted2");
//                return true;
            }

    }

    public void encrypt(View view) throws NoSuchPaddingException, NoSuchAlgorithmException,
            NoSuchProviderException, InvalidKeyException, ShortBufferException,
            BadPaddingException, IllegalBlockSizeException {

        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sample);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

//        String pass = "aditya perwira joan ated";

        byte[] keyBytes = new byte[] { 0x61, 0x64, 0x69, 0x74, 0x79, 0x61, 0x20, 0x70, 0x65, 0x72,
                0x77, 0x69, 0x72, 0x61, 0x20, 0x6a, 0x6f, 0x61, 0x6e, 0x20, 0x61, 0x74, 0x65, 0x64};

//        byte[] keyBytes = pass.getBytes();

//        Log.e("adit","key"+System.Text.Encoding.Default.GetString(keyBytes));
        key = new SecretKeySpec(keyBytes, "AES");

        cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

//        Log.e("masukan ",new String(input));

        // encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, key);

        cipherText = new byte[cipher.getOutputSize(byteArray.length)];
        ctLength = cipher.update(byteArray, 0, byteArray.length, cipherText, 0);
        ctLength += cipher.doFinal(cipherText, ctLength);
//        Log.e("adit ", "enkripsi "+new String(cipherText));
//        text.setText(new String(cipherText));
        Log.e("adit ","panjang "+String.valueOf(ctLength));
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/mysdfile1.txt";

            File myFile = new File(path);
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            CipherOutputStream cipherOutputStream = new CipherOutputStream(fOut,cipher);
//            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//            myOutWriter.append(new String(cipherText));
//            myOutWriter.close();
            fOut.write(cipherText);
            cipherOutputStream.write(byteArray);
            fOut.close();
            Log.e("adit ","envi "+path);
        } catch (Exception e) {
            Log.e("ERRR", "Could not create file",e);
        }

    }

    public void decryp(View view) throws InvalidKeyException, ShortBufferException, BadPaddingException, IllegalBlockSizeException, FileNotFoundException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, key);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/mysdfile1.txt";
        File myFile = new File(path);
        FileInputStream fis = new FileInputStream(path);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        Bitmap bitmap = BitmapFactory.decodeStream(cis);
        Log.e("adit","selesai");
        gambar.setImageBitmap(bitmap);
    }
}
