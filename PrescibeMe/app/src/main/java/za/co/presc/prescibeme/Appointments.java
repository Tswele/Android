package za.co.presc.prescibeme;


import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class Appointments extends Activity implements NavigationDrawerFragment.NavigationDrawerCallbacks
{
    private Handler mHandler = new Handler();
    private Button btn,btnB;
    private TextView txtV,txtDt,txtTm,txtD,txtT;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #//restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

        btn = (Button)findViewById(R.id.btnChange);
        btnB = (Button)findViewById(R.id.btnBookAppointment);
        txtV = (TextView) findViewById(R.id.textView2);
        txtD = (TextView) findViewById(R.id.in_date);
        txtT = (TextView) findViewById(R.id.in_time);
        txtTm = (TextView) findViewById(R.id.btn_time);
        txtDt = (TextView) findViewById(R.id.btn_date);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(v.getContext(), Booking.class);
                //  Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
            }
        });



        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {



            }
        });

        getAppointments();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    public void getAppointments()
    {

        String[] params = new String[]{"192.168.1.80"};
        new AsyncTask().execute(params);
    }

    private void hideAll()
    {
        btn.setVisibility(View.GONE);
        txtV.setVisibility(View.GONE);
        txtD.setVisibility(View.GONE);
        txtDt.setVisibility(View.GONE);
        txtT.setVisibility(View.GONE);
        txtTm.setVisibility(View.GONE);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    class AsyncTask extends android.os.AsyncTask<String, Void, String>
    {
        public String SOAP_ACTION = "http://threepin.org/isAppointment";
        public String OPERATION_NAME = "isAppointment";
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
            /*PropertyInfo id = new PropertyInfo();
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
            pss = new PropertyInfo();*/

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

                    if(s.matches("true"))
                        hideAll();
                    else
                        txtV.setText(s);


                }
            });
        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_navigation, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((Appointments) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
