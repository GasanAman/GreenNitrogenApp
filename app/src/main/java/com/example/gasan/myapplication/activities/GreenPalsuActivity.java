package com.example.gasan.myapplication.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.gasan.myapplication.R;
import com.example.gasan.myapplication.ServiceHandler;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GreenPalsuActivity extends AppCompatActivity {

    Spinner spinner;
    ArrayAdapter<CharSequence> arrayAdapter;
    ProgressDialog pDialog;
    EditText kolomComment, inputnama, inputalamat, inputnohp, inputemail, inputnopol, inputMrkKend, judulText;
    Button b1;

    private String URL_NEW_CATEGORY = "http://10.0.3.2/input_comment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_palsu);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        kolomComment = (EditText) findViewById(R.id.kolomComment);
        inputnama = (EditText) findViewById(R.id.inputnama);
        inputalamat = (EditText) findViewById(R.id.inputalamat);
        inputnohp = (EditText) findViewById(R.id.inputnohp);
        inputemail = (EditText) findViewById(R.id.inputemail);
        inputnopol = (EditText) findViewById(R.id.inputnopol);

        spinner = (Spinner) findViewById(R.id.spinner);

        inputMrkKend = (EditText) findViewById(R.id.inputMrkKend);
        judulText = (EditText) findViewById(R.id.judulText);

        arrayAdapter = ArrayAdapter.createFromResource(this, R.array.jenis_kendaraan, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        ImageButton back = (ImageButton) findViewById(R.id.imageButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(GreenPalsuActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        b1 = (Button) findViewById(R.id.buttonSubmit);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveData().execute();
            }
        });

    }

    public void onClick(View v){

        new SaveData().execute();
    }

    private class SaveData extends AsyncTask<String, String, String> {

        boolean isNewCategoryCreated = false;

        String kolomComentParam     = kolomComment.getText().toString();
        String inputNamaParam       = inputnama.getText().toString();
        String inputAlamatParam     = inputalamat.getText().toString();
        String inputNoHpParam       = inputnohp.getText().toString();
        String inputEmailParam      = inputemail.getText().toString();
        String inputNoPolParam      = inputnopol.getText().toString();
        String inputJenisKend       = spinner.getSelectedItem().toString();
        String inputMrkKendParam    = inputMrkKend.getText().toString();
        String judulTextParam       = judulText.getText().toString();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GreenPalsuActivity.this);
            pDialog.setMessage("Menyimpan Data.");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... arg) {


            // Preparing post params
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("komentar", kolomComentParam));
            params.add(new BasicNameValuePair("nama", inputNamaParam));
            params.add(new BasicNameValuePair("alamat", inputAlamatParam));
            params.add(new BasicNameValuePair("no_handphone", inputNoHpParam));
            params.add(new BasicNameValuePair("email", inputEmailParam));
            params.add(new BasicNameValuePair("no_polisi", inputNoPolParam));
            params.add(new BasicNameValuePair("jenis_kendaraan", inputJenisKend));
            params.add(new BasicNameValuePair("merek", inputMrkKendParam));
            params.add(new BasicNameValuePair("judul", judulTextParam));

            ServiceHandler serviceClient = new ServiceHandler();

            String json = serviceClient.makeServiceCall(URL_NEW_CATEGORY,
                    ServiceHandler.POST, params);

            Log.d("Create Response: ", "> " + json);

            if (json != null) {
                try {
                    JSONObject jsonObj = new JSONObject(json);
                    boolean error = jsonObj.getBoolean("error");
                    // checking for error node in json
                    if (!error) {
                        // new category created successfully
                        isNewCategoryCreated = true;
                    } else {
                        Log.e("Create Category Error: ", "> " + jsonObj.getString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

        }
    }
}
