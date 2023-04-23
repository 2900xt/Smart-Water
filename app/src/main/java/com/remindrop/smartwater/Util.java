package com.remindrop.smartwater;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Util {
    public static void swapFragments(Fragment currentFragment, Fragment newFragment)
    {
        FragmentTransaction fragmentTransaction = currentFragment.getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, newFragment);
        fragmentTransaction.addToBackStack("Home");
        fragmentTransaction.commit();
    }

    public static void writeJSON(JSONObject data, String filename, Context context) {
        try
        {
            FileOutputStream fOS = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osWriter = new OutputStreamWriter(fOS);
            BufferedWriter writer = new BufferedWriter(osWriter);

            writer.write(data.toString());

            writer.close();
            osWriter.close();
            fOS.close();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static JSONObject readJSON(String filename, Context context)
    {
        JSONObject data = null;
        try
        {
            FileInputStream fIS = context.openFileInput(filename);
            InputStreamReader isReader = new InputStreamReader(fIS);
            BufferedReader reader = new BufferedReader(isReader);

            StringBuilder jsonBuilder = new StringBuilder();
            String currentLine;
            while((currentLine = reader.readLine()) != null)
            {
                jsonBuilder.append(currentLine);
            }

            if(jsonBuilder.length() < 1)
            {
                jsonBuilder.append("{}");
            }

            data = new JSONObject(
                    new JSONTokener(jsonBuilder.toString())
            );

            reader.close();
            isReader.close();
            fIS.close();
        }
        catch (FileNotFoundException e)
        {
            try
            {
                FileOutputStream fOS = context.openFileOutput(filename, Context.MODE_PRIVATE);
                OutputStreamWriter osWriter = new OutputStreamWriter(fOS);
                BufferedWriter writer = new BufferedWriter(osWriter);

                writer.write("{}");

                writer.close();
                osWriter.close();
                fOS.close();
            }
            catch (Exception f)
            {
                f.printStackTrace();
                System.exit(-1);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(-1);
        }

        return data;
    }

    private static JSONObject JSONData;
    private static Context JSONDataContext;

    public static void JSONInit(Context context)
    {
        JSONDataContext = context;
        JSONData = readJSON("remindropData.json", JSONDataContext);
    }

    public static void JSONSave()
    {
        writeJSON(JSONData, "remindropData.json", JSONDataContext);
    }

    public static JSONObject getJSON()
    {
        return JSONData;
    }
}
