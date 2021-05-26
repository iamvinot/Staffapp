package aec.com.aecdroid;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class tab_1 extends Fragment  {
    public EditText edittext;
    public EditText absentees;
    public Button button;
    public String dept;

    public String year;
    public String section;

    private static final String base_url = "http://79cddaa2.ngrok.io/addatt.php";



    final Calendar myCalendar = Calendar.getInstance();

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v=inflater.inflate(R.layout.tab_1, container, false);
        edittext =(EditText)v.findViewById(R.id.editText);
        //post datas
         absentees =(EditText)v.findViewById(R.id.editText4);

        button=(Button)v.findViewById(R.id.button);
        Spinner spinner = (Spinner)v.findViewById(R.id.spinner);

        Spinner spinner1 = (Spinner)v.findViewById(R.id.spinner_year);

        Spinner spinner2 = (Spinner)v.findViewById(R.id.spinner_section);
// Create an ArrayAdapter using the string array and a default spinner layout
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dept=parent.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {// sometimes you need nothing here
            }
        });
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year=parent.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {// sometimes you need nothing here
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                section=parent.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {// sometimes you need nothing here
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog( getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try{

            // CALL post_data method to make post method call
            post_data();
        }
        catch(Exception ex)
        {
            Toast.makeText(getActivity(),"URL Exception",Toast.LENGTH_LONG).show();
        }


    }}
);
        return v;
}
    //post_data() called by button onClick
    private void post_data() {
        String date_post = edittext.getText().toString().trim().toLowerCase();
        String dept_post = dept.trim().toLowerCase();
        String year_post = year.trim().toLowerCase();
        String section_post = section.trim().toLowerCase();
        String absentees_post = absentees.getText().toString().trim().toLowerCase();

        post_url(date_post, dept_post, year_post, section_post, absentees_post);
    }

    // Method for posting data to online server

    private void post_url(String date, String dept, String year, String section,String absentees) {
        String urlSuffix = "?date="+date+"&dept="+dept+"&year="+year+"&section="+section+"&absentees="+absentees;
        class RegisterUser extends AsyncTask<String, Void, String> {

            ProgressDialog loading;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait",null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(String... params) {
                String s = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(base_url+s);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String result;

                    result = bufferedReader.readLine();

                    return result;
                }catch(Exception e){
                    return null;
                }
            }
        }

        RegisterUser ru = new RegisterUser();
        ru.execute(urlSuffix);
    }
    private void updateLabel() {

        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

}
