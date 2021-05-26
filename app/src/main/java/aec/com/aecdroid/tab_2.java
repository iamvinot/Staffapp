package aec.com.aecdroid;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * Created by Rajesh on 13-03-2016.
 */
/** Design modified Shangeeth 15-03-2016
 *
 */

public class tab_2 extends Fragment  implements View.OnClickListener{
    private EditText editTextId;
    private Button buttonGet;
    public String dept;

    public String year;
    public String section;
    private TextView textViewResult;

    private ProgressDialog loading;

    final Calendar myCalendar = Calendar.getInstance();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tab_2,container,false);
        editTextId = (EditText)v.findViewById(R.id.editTextId);
        buttonGet = (Button) v.findViewById(R.id.buttonGet);
        textViewResult = (TextView) v.findViewById(R.id.textViewResult);

        Spinner spinner = (Spinner)v.findViewById(R.id.spinner_tab2);

        Spinner spinner1 = (Spinner)v.findViewById(R.id.spinner_year_tab2);

        Spinner spinner2 = (Spinner)v.findViewById(R.id.spinner_section_tab2);
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


        buttonGet.setOnClickListener(this);
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
        editTextId.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog( getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        return v;
    }


    @Override
    public void onClick(View v) {
        getData();
    }
    private void getData() {
        String id = editTextId.getText().toString().trim();
        if (id.equals("")) {
            Toast.makeText(getActivity(), "Please Select Date", Toast.LENGTH_LONG).show();
            return;
        }
        loading = ProgressDialog.show(getActivity(), "Please wait...", "Fetching...", false, false);
        String url = Config.DATA_URL_ATT+editTextId.getText().toString().trim()+"&dept="+dept.trim()+"&year="+year.trim()+"&section="+section.trim();
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response){

        String date = "";
        String absentees="";
        String dept="";
        String section="";


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            date = collegeData.getString(Config.KEY_DATE);
            dept = collegeData.getString(Config.KEY_DEPT);
            absentees = collegeData.getString(Config.KEY_ABS);
            section = collegeData.getString(Config.KEY_SEC);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        textViewResult.setText("Absentees Details Are As Follows \n\n Date:\t" + date + "\n\n" + "Dept:\t" + dept + "\t\t Section: \t" + section + "\n\n" +
                "Absentees:\t" + absentees);
    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextId.setText(sdf.format(myCalendar.getTime()));
    }
}