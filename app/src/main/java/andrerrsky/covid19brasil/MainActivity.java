package andrerrsky.covid19brasil;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static MainActivity instance;

    public List<String> states;
    public List<String> statesFull;

    private ListView listView;
    private ArrayAdapter listAdapter;
    private SearchView searchView;
    private TextView textViewConfirmed;
    private TextView textViewDeaths;
    private ProgressDialog progressDialog;
    private String shareContent;
    public String countryNumbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        states = Arrays.asList(getResources().getStringArray(R.array.states));
        statesFull = Arrays.asList(getResources().getStringArray(R.array.states_full));

        listView = findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(this, R.layout.list_item, statesFull);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(isInternetAvailable()) {

                    searchView.setQuery("", false);
                    searchView.clearFocus();

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputManager != null && getCurrentFocus() != null) {
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    Intent intent = new Intent(MainActivity.this, ViewActivity.class);
                    intent.putExtra("state", listView.getItemAtPosition(position).toString());
                    startActivity(intent);

                } else {

                    showNerworkErrorDialog();

                }

            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                listAdapter.getFilter().filter(newText);
                return false;

            }

        });

        textViewConfirmed = findViewById(R.id.textViewConfirmed);
        textViewDeaths = findViewById(R.id.textViewDeaths);

        if(isInternetAvailable()) {

            get();

        } else {

            showNerworkErrorDialog();

        }

        shareContent = "";

        instance = this;

    }

    private void showNerworkErrorDialog() {

        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle("Sem conexão");
        alertDialog.setMessage("Por favor verifique sua conexão com a internet e tente novamente.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }

    private void get() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Atualizando dados...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://brasil.io/api/dataset/covid19/caso/data?is_last=True&place_type=state";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d("Network", response);

                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");

                            int confirmed = 0;
                            int deaths = 0;

                            for(int i = 0; i < jsonArray.length(); i ++) {

                                JSONObject currentJSONObject = jsonArray.getJSONObject(i);
                                confirmed += currentJSONObject.getInt("confirmed");
                                deaths += currentJSONObject.getInt("deaths");

                                shareContent = shareContent + "\n" + currentJSONObject.getString("state") + ": " + currentJSONObject.getInt("confirmed") + " conf. / "  + currentJSONObject.getInt("deaths") + " mortes";

                            }

                            textViewConfirmed.setText(confirmed + "");
                            textViewDeaths.setText(deaths + "");

                            countryNumbers = "\n\nEm todo o Brasil há " + confirmed + " casos confirmados e " + deaths + " mortes.";

                            progressDialog.dismiss();

                        } catch (Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Ocorreu um erro ao exibir as informações, por favor verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
                            finish();

                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Ocorreu um erro ao exibir as informações, por favor verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
                finish();

            }

        });

        queue.add(stringRequest);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.optionsmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                String subject = "Situação do COVID 19 no Brasil";
                String text = "Dados atualizados sobre COVID 19 no Brasil, agora são " + textViewConfirmed.getText().toString() + " casos confirmados e " + textViewDeaths.getText().toString() + " mortes.\n\nNúmeros por estados:\n" + shareContent;
                share(subject, text);
                break;
            case R.id.rate:
                rate();
                break;
            case R.id.about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.exit:
                exitApp();
                break;
        }

        return true;

    }

    public boolean isInternetAvailable() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

    }

    public void share(String subject, String text) {

        text = text + "\n\nDados do app COVID 19 Brasil by ashestore.com.br";

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(shareIntent, "Compartilhar"));

    }

    public void rate() {

        Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        try {

            startActivity(goToMarket);

        } catch (ActivityNotFoundException e) {

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));

        }

    }

    public void exitApp() {

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }

}