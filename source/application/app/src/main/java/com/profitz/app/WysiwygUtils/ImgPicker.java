package com.profitz.app.WysiwygUtils;

import android.os.Environment;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.esafirm.imagepicker.features.ImagePicker;


public class ImgPicker {

    private static ImagePicker imagePicker;

    private static ImagePicker getImagePicker(View view) {
        imagePicker = ImagePicker.create((AppCompatActivity) view.getContext());

        return imagePicker.limit(5) // max images can be selected (99 by default)
                .toolbarFolderTitle("Galeria")
                .toolbarDoneButtonText("Zatwierdź")
                .showCamera(false) // show camera or not (true by default)
                .folderMode(true)
                .includeVideo(false)
                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath()); // can be full path
    }

    public static void start(View view) {
        getImagePicker(view).start(); // start image picker activity with request code
    }

}
