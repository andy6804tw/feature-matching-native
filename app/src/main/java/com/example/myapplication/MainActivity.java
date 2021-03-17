package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.example.myapplication.tools.CameraManager;
import com.example.myapplication.views.CameraPreviewView;

/**
 * main activity, responsible for loading the layout and its views
 * @author Michael Troger
 */
public class MainActivity extends Activity {
    /**
     * class name for debugging with logcat
     */
    private static final String TAG = MainActivity.class.getName();
    /**
     * the SurfaceView holding the current picture of the camera
     */
    private CameraPreviewView mPreview;
    /**
     * the camera manager is responsible for getting an instance of the camera
     * and releasing it correctly
     */
    public CameraManager mCameraManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // get a camera manager instance
        mCameraManager = new CameraManager(this);
        ImageView myCameraPreview = new ImageView(this);
        // Create the preview view
        mPreview = new CameraPreviewView(this, mCameraManager.getCamera(), myCameraPreview);
        // init the camera preview with the necessary parameters so that
        // it knows which area of the camera picture is to analyze

        // add the live picture of the camera to the prepared FrameLayout
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        preview.addView(myCameraPreview);

        Log.d(TAG, "started :)");
    }


    @Override
    protected void onPause() {
        super.onPause();
        mPreview.onPause();
        mCameraManager.onPause();

        mPreview.setVisibility(View.GONE); // this to fix freeze.
    }


    @Override
    protected void onResume() {
        super.onResume();
        // ask camera permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA}, 1);
        }else{
            mCameraManager.onResume();

            mPreview.setCamera(mCameraManager.getCamera());

            mPreview.setVisibility(View.VISIBLE); // this can fix the freeze.
        }

    }

}