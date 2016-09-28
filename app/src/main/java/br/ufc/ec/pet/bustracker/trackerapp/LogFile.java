package br.ufc.ec.pet.bustracker.trackerapp;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by santana on 24/07/16.
 */
public class LogFile {
    private static String file = "log";
    public static String TAG = "Bus";

    public static void writeln(Context context, String log, boolean defaultLog){
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_APPEND));
            outputStreamWriter.write(log+System.getProperty("line.separator"));
            if(defaultLog)
                Log.d(TAG, log);
        }catch (IOException e){Log.d("LogFile Error: ", e.toString());}
        finally{
            try {
                outputStreamWriter.close();
            }catch (IOException e){Log.d("LogFile", "Error on close log file");}
        }
    }
    public static void writeln(Context context, String log){
        writeln(context, log, false);
    }
    public static String read(Context context){
        String ret = "";
        try {

            InputStream inputStream = context.openFileInput(file);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.insert(0, receiveString);
                    stringBuilder.insert(receiveString.length(),  "\n");
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("LogFile", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("LogFile", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public static void clear(Context context){
        OutputStreamWriter outputStreamWriter = null;
        try {
            outputStreamWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE));
            outputStreamWriter.write("");
        }catch (IOException e){Log.d("LogFile Error: ", e.toString());}
        finally{
            try {
                outputStreamWriter.close();
            }catch (IOException e){Log.d("LogFile", "Error on close log file");}
        }
    }
}
