

package za.co.presc.prescibeme;
import android.R.*;
import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class HomeActivity extends Activity {
    private Handler mHandler = new Handler();
    private TextView txtName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        String inputId = MainActivity.identity;
        //String[] params = new String[]{"10.254.18.211", inputId};
        // new AsyncTask().execute(params);
        ImageView bookImg = (ImageView) findViewById(R.id.imageView2);
        bookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), Appointments.class);
                //  Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
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

    /*class AsyncTask extends android.os.AsyncTask<String, Void, String>
    {
        public String SOAP_ACTION = "http://threepin.org/getname";
        public String OPERATION_NAME = "getname";
        public String WSDL_TARGET_NAMESPACE = "http://threepin.org/";
        public String SOAP_ADDRESS;
        private SoapObject request;
        private HttpTransportSE httpTransport;
        private SoapSerializationEnvelope envelope;
        Object responce = null;

        //method to fetch output from website


        @Override
        protected String doInBackground(String... params) {
            SOAP_ADDRESS = "http://"+params[0] + "/team38-master/WcfEnigmaService.asmx";

            request = new SoapObject(WSDL_TARGET_NAMESPACE,OPERATION_NAME);
            PropertyInfo id = new PropertyInfo();
            id.setName("id");
            id.setValue(params[1]);
            id.setType(String.class);


            request.addProperty(id);
            id = new PropertyInfo();


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
                  /*  if(s.matches("false"))
                        textview.setText("Please try again");
                    else if(s.matches("true"))
                        sendMessage();
                    else
                        textview.setText("Please try again");
                    txtName.setText(s);
                }
            });
        }
    }*/
}
