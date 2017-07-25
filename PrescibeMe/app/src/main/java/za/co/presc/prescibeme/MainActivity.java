

package za.co.presc.prescibeme;
import android.R.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
//import org.ksoap2.transport.HttpTransport;
import org.ksoap2.transport.HttpTransportSE;
import org.ksoap2.serialization.*;

public class MainActivity extends Activity
{
    private Handler mHandler = new Handler();
    private TextView textview;
    private Button btn;
    private EditText edit, txtPassword;
    public static String identity;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //  super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);
        edit= (EditText)findViewById(R.id.txtID);
        textview = (TextView)findViewById(R.id.textView);
        btn = (Button)findViewById(R.id.button);
        txtPassword = (EditText)findViewById(R.id.txtPass);
        identity = "0";
        //  requestWindowFeature(Window.FEATURE_NO_TITLE);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void sendMessage()
    {
        Intent intent = new Intent(this, Appointments.class);
        startActivity(intent);
    }

    public void moveToRegister(View v)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void getname(View v)
    {
        String inputId = edit.getText().toString();
        String password = txtPassword.getText().toString();
        //andy-192.168.78.1
        //my wifi "192.168.1.85"
        identity = inputId;
        String[] params = new String[]{"192.168.1.80", inputId, password};
        new AsyncTask().execute(params);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    class AsyncTask extends android.os.AsyncTask<String, Void, String>
    {
        public String SOAP_ACTION = "http://threepin.org/login";
        public String OPERATION_NAME = "login";
        public String WSDL_TARGET_NAMESPACE = "http://threepin.org/";
        public String SOAP_ADDRESS;
        private SoapObject request;
        private HttpTransportSE httpTransport;
        private SoapSerializationEnvelope envelope;
        Object responce = null;

        //method to fetch output from website


        @Override
        protected String doInBackground(String... params)
        {
            SOAP_ADDRESS = "http://"+params[0] + "/team38-master/WcfEnigmaService.asmx";

            request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
            PropertyInfo id = new PropertyInfo();
            id.setName("id");
            id.setValue(params[1]);
            id.setType(String.class);

            PropertyInfo pss = new PropertyInfo();
            pss.setName("password");
            pss.setValue(params[2]);
            pss.setType(String.class);

            request.addProperty(id);
            id = new PropertyInfo();
            request.addProperty(pss);
            pss = new PropertyInfo();

            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            httpTransport = new HttpTransportSE(SOAP_ADDRESS);
            try
            {
                httpTransport.call(SOAP_ACTION,envelope);
                responce = envelope.getResponse();
            }catch(Exception e)
            {
                return "false";
            }

            return responce.toString();
        }

        @Override
        protected void onPostExecute(final String s) {
            super.onPostExecute(s);
            mHandler.post(new Runnable() {
                @Override
                public void run()
                {
                    if(s.matches("false"))
                        textview.setText("Please try again");
                    else if(s.matches("true"))
                        sendMessage();
                    else
                        textview.setText("Please try again");

                }
            });
        }
    }
}
