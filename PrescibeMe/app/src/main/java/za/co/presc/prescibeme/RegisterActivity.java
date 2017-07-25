package za.co.presc.prescibeme;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;
import android.R.*;

import java.util.StringTokenizer;


public class RegisterActivity extends ActionBarActivity
{

    private Handler mHandler = new Handler();


    private Button btn;
    private TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        btn = (Button)findViewById(R.id.button2);
        txt = (TextView)findViewById(R.id.textView6);



        return true;
    }



    public void connect(View v)
    {
        EditText inputId = (EditText)findViewById(R.id.txtID);
        EditText txtFn = (EditText)findViewById(R.id.txtFName);
        EditText txtLn = (EditText)findViewById(R.id.txtLName);
        EditText txtPas = (EditText)findViewById(R.id.txtPassword);
        String fn = txtFn.getText().toString();
        String ln = txtLn.getText().toString();
        String iD = inputId.getText().toString();
        String ps = txtPas.getText().toString();
        //andy-192.168.78.1
        //my wifi "192.168.1.85"
        String[] params = new String[]{"10.254.18.211",fn,ln, iD,ps};
        new AsyncTask().execute(params);
    }

    public void sendMessage()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void register(String outFromWeb)
    {
        StringTokenizer tk = new StringTokenizer(outFromWeb);
        String first = tk.nextToken();

        if(first.toUpperCase().matches("TRUE"))
        {
            sendMessage();
        }
        //for other exceptions
        else
        {
            String next = tk.nextToken();
            if (next.matches("-"))

                txt.setText("Check your ID");
            else
                txt.setText(next);
        }
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
        public String SOAP_ACTION = "http://threepin.org/insertUser";
        public String OPERATION_NAME = "insertUser";
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


            PropertyInfo fName = new PropertyInfo();
            fName.setName("fName");
            fName.setValue(params[1]);
            fName.setType(String.class);

            PropertyInfo lName = new PropertyInfo();
            lName.setName("lName");
            lName.setValue(params[2]);
            lName.setType(String.class);

            PropertyInfo id = new PropertyInfo();
            id.setName("id");
            id.setValue(params[3]);
            id.setType(String.class);

            PropertyInfo pss = new PropertyInfo();
            pss.setName("password");
            pss.setValue(params[4]);
            pss.setType(String.class);

            request.addProperty(fName);
            fName = new PropertyInfo();
            request.addProperty(lName);
            lName = new PropertyInfo();
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
                return e.toString();
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
                    register(s);
                }
            });
        }
    }

}
