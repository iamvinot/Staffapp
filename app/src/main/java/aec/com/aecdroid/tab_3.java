package aec.com.aecdroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.EditText;
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


/**
 * Created by Rajesh on 13-03-2016.
 */
/** Design modified Shangeeth 15-03-2016
 *
 */

public class tab_3 extends Fragment{
    private EditText editTextId;
    private Button buttonGet;
    private TextView textViewResult;
    private Button buttonGet1;

    private Button buttonGet2;

    private Button buttonGet3;

    private ProgressDialog loading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.tab_3,container,false);
        editTextId = (EditText)v.findViewById(R.id.editText_3);
        buttonGet = (Button) v.findViewById(R.id.buttonGet_3);
        textViewResult = (TextView) v.findViewById(R.id.textViewResult_3);
        buttonGet1 = (Button) v.findViewById(R.id.buttonGet_4);

        buttonGet2 = (Button) v.findViewById(R.id.buttonGet_5);

        buttonGet3 = (Button) v.findViewById(R.id.buttonGet_6);

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(1);
            }
        });
        buttonGet1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(2);
            }
        });
        buttonGet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(3);
            }
        });
        buttonGet3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(4);
            }
        });
        return v;
    }
    private void getData(int a) {
        String URL="";
        if(a==1){URL=Config.DATA_URL_CIA1;}
        else if(a==2){URL=Config.DATA_URL_CIA2; }
        else if(a==3){URL=Config.DATA_URL_MODEL; }
        else if(a==4){URL=Config.DATA_URL_UNIV; }

        loading = ProgressDialog.show(getActivity(), "Please wait...", "Fetching...", false, false);
        String url = URL+editTextId.getText().toString().trim();
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

        String username = "";
        String sub_1="";
        String sub_2="";
        String sub_3 = "";
        String sub_4="";
        String sub_5="";
        String sub_6="";
        String remarks = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject collegeData = result.getJSONObject(0);
            username = collegeData.getString(Config.KEY_USERNAME);
            sub_1 = collegeData.getString(Config.KEY_SUB_1);
            sub_2 = collegeData.getString(Config.KEY_SUB_2);
            sub_3 = collegeData.getString(Config.KEY_SUB_3);
            sub_4 = collegeData.getString(Config.KEY_SUB_4);
            sub_5 = collegeData.getString(Config.KEY_SUB_5);
            sub_6 = collegeData.getString(Config.KEY_SUB_6);
            remarks = collegeData.getString(Config.KEY_REMARKS);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        textViewResult.setText("Student Mark Details are as follows \n\n RegisterNo:\t" + username + "\n\n" + "Subject 1:\t" + sub_1 + "\n\n" + "Subject 2:\t"+sub_2+"\n\n"+
                "Subject 3:\t"+sub_3+"\n\n"+
                "Subject 4:\t"+sub_4+"\n\n"+
                "Subject 5:\t"+sub_5+"\n\n"+
                "Subject 6:\t"+sub_6+"\n\n"+
                "Remarks:\t"+ remarks);
    }

}