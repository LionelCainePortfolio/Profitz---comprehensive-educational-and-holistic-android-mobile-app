package com.profitz.app.WysiwygUtils;

import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.profitz.app.R;import com.profitz.app.RichEditor.RichEditor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AlertDialog;

import es.dmoral.toasty.Toasty;

public class Youtube {

    public static String getVideoId(String url){
        final String[] patterns = {"https://www.youtube.com/watch\\?v=(\\S+)&list", "https://www.youtube.com/watch\\?v=(\\S+)\\??",
                "https://www.youtube.com/watch\\?time_continue=\\d+&v=(\\S+)&list", "https://www.youtube.com/watch\\?time_continue=\\d+&v=(\\S+)\\??",
                "https://youtu.be/(\\S+)\\?list", "https://youtu.be/(\\S+)\\??"};
        Pattern p;
        Matcher m;
        for(String pattern : patterns) {
            p = Pattern.compile(pattern);
            m = p.matcher(url);
            if(m.find()) {
                return m.group(1);
            }
        }
        return "error";
    }

    public static void showYoutubeDialog(LayoutInflater layoutInflater, RichEditor content, View view) {
        layoutInflater = LayoutInflater.from(view.getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_youtube, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editText = promptView.findViewById(R.id.userInputDialog);
        // setup a dialog window
        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String videoid = Youtube.getVideoId(editText.getText().toString());
                        if(videoid.equals("error")){
                           // Toast.makeText(view.getContext() ,"Wystąpił błąd podczas dodawania wideo.", Toast.LENGTH_LONG).show();
                            Toasty.error(view.getContext(), "Wystąpił błąd podczas dodawania wideo.").show();

                        }
                        else{
                            content.insertYoutubeVideo(videoid);
                        }
                        Keyboard.closeKeyboard(editText);
                    }
                })
                .setNegativeButton("Anuluj",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Keyboard.closeKeyboard(editText);
                            }
                        });

        // create an alert dialog
        AlertDialog alert = alertDialogBuilder.create();
        // back key를 눌렀을 때 dialog 닫음
        alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.cancel();
                    return true;
                }
                return false;
            }
        });
        // youtube insert 누른 후 show Keyboard 및 focus of content 지우고 in editText of dialog에 focus 줌
        Keyboard.showSoftKeyboard(alert);
        content.clearFocus();
        editText.requestFocus();
        alert.show();

    }

}
