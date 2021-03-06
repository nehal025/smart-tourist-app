package com.example.smarttourapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.smarttourapp.R;
import com.example.smarttourapp.ml.FoodModel;
import com.example.smarttourapp.ui.camera.Camera;
import com.example.smarttourapp.utils.Global;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class FoodClassifier extends AppCompatActivity {
    List<String> labels = new ArrayList<>();
    ImageView imageView;
    ImageView classify;
    ImageView retake;
    Bitmap bitmap = Global.img;
    int imageSize = 224;
    String s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifier);
        imageView = findViewById(R.id.photo);
        classify = findViewById(R.id.classify);
        retake = findViewById(R.id.retake);

        int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
        imageView.setImageBitmap(bitmap);

        classify.setOnClickListener(v -> {
            bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
            classifyImage(bitmap);

        });

        retake.setOnClickListener(v -> {
            Intent myIntent = new Intent(FoodClassifier.this, Camera.class);
            FoodClassifier.this.startActivity(myIntent);
            finish();
        });

        try {
            loadLabels();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("DefaultLocale")
    public void classifyImage(Bitmap image) {
        try {
            FoodModel model = FoodModel.newInstance(getApplicationContext());

            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            FoodModel.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();

            int maxPos = 0;
            float maxConfidence = 0;

            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            for (int i = 0; i < labels.size(); i++) {

                s += String.format("%s: %.1f%%\n", labels.get(i), confidences[i] * 100);
            }

            try {
                Log.i("String", s);
            } catch (Exception ignored) {

            }

            Intent myIntent = new Intent(FoodClassifier.this, DisplayFood.class);
            myIntent.putExtra("name", labels.get(maxPos));
//            myIntent.putExtra("location", s);
            FoodClassifier.this.startActivity(myIntent);
            model.close();
            finish();
        } catch (IOException ignored) {

        }
    }
    public void loadLabels() throws IOException {
        AssetManager manager;
        String line;
        manager = getAssets();
        InputStream is = manager.open("food_labels.txt");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br  = new BufferedReader(isr);
        try {
            while ((line = br.readLine()) != null) {
                labels.add(line) ;
            }
        }catch (IOException e1) {
            Toast.makeText(getBaseContext(), "Problem!", Toast.LENGTH_SHORT).show();
        }finally{
            br.close();
            isr.close();
            is.close();
        }
    }
}