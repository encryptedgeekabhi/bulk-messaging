package com.example.hp.PATRON.contacts.massage;

/**
 * Created by hp on 28-08-2018.
 */
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class send {
    public void onsend()
    {
        URLConnection myURLConnection=null;
        URL myURL=null;
        BufferedReader reader=null;

        String mainUrl="http://www.hellopatna.com/smsapi/sendsms?";

//Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("username="+"demo");
        sbPostData.append("&password="+"demo");
        sbPostData.append("&senderid="+"PATRON");
        sbPostData.append("&to="+"9471018608");
        sbPostData.append("&message="+"msg from steve");
        sbPostData.append("&route="+"1");
        sbPostData.append("&flash="+"0");
        sbPostData.append("&nonenglish="+"0");
//final string
        mainUrl = sbPostData.toString();

        try
        {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));

            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                Log.d("RESPONSE", ""+response);

            //finally close connection
            reader.close();


        }

        catch (IOException e)
        {
            e.printStackTrace();
        }
}
    Thread thread = new Thread(new Runnable() {

        @Override
        public void run() {
            try  {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });
}
