package com.example.hp.PATRON.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;

import com.example.hp.PATRON.R;
import com.example.hp.PATRON.dbconnect.DBHelper;
import com.example.hp.PATRON.feedback.feedback1;

/**
 * Created by hp on 18-09-2018.
 */

public class shortcutsetting extends Activity implements  DialogInterface.OnCancelListener {

    String api;
    int id;
    Context c;
    Activity a;
   CheckBox cbox;
    SharedPreferences.Editor editor;
    com.example.hp.PATRON.dbconnect.DBHelper db;
    public shortcutsetting(Context ctx) {
        c = ctx;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(c);
        editor = sharedPref.edit();
        db = new DBHelper(ctx);
        Dialog dialog = new Dialog(ctx);
        dialog.setTitle("Shortcut setting");

        dialog.setContentView(R.layout.shortcutsetting);
        Window w = dialog.getWindow();
        w.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        a = this;
        cbox= dialog.findViewById(R.id.feedback);

       dialog.setCanceledOnTouchOutside(false);
       dialog.closeOptionsMenu();
       dialog.setOnCancelListener(this);
        String state = sharedPref.getString("state", "");
System.out.println("this is state: "+state);
if(state.equalsIgnoreCase("true"))
{
    cbox.setChecked(true);
}
else
    {
    cbox.setChecked(false);
}
        dialog.show();
    }


    private void createShortcut(){
        Intent shortcutIntent = new Intent(c,feedback1.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "feedback");
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(c, R.drawable.feedbackicon));
        intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        intent.putExtra("duplicate", false);
        c.sendBroadcast(intent);
        System.out.println("this is shortcut");
    }
    private void removeShortcut() {

        //Deleting shortcut for MainActivity
        //on Home screen
        Intent shortcutIntent = new Intent(c,
                feedback1.class);
        shortcutIntent.setAction(Intent.ACTION_MAIN);

        Intent addIntent = new Intent();
        addIntent
                .putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "feedback");

        addIntent
                .setAction("com.android.launcher.action.UNINSTALL_SHORTCUT");
        c.sendBroadcast(addIntent);
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        if(cbox.isChecked())
        {
            createShortcut();
            editor.putString("state","true");
            editor.commit();
        }
        else
        {
            removeShortcut();
            editor.putString("state","false");
            editor.commit();
        }
    }
}