package andrerrsky.covid19brasil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewActivity extends AppCompatActivity {

    public static ViewActivity instance;

    private String state;
    private int id;

    public List<String> cities;

    private TextView textViewTitle;
    private TextView textViewSubTitle;
    private TextView textViewConfirmed;
    private TextView textViewDeaths;
    private ListView listView;
    private ArrayAdapter listAdapter;
    private SearchView searchView;
    private ProgressDialog progressDialog;
    public String shareContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        try {

            state = getIntent().getExtras().getString("state");

            id = findStateId();

            textViewTitle = findViewById(R.id.textViewTitle);
            textViewTitle.setText(MainActivity.instance.statesFull.get(id));

            textViewSubTitle = findViewById(R.id.textViewSubTitle);
            textViewSubTitle.setText(MainActivity.instance.states.get(id));

            textViewConfirmed = findViewById(R.id.textViewConfirmed);
            textViewDeaths = findViewById(R.id.textViewDeaths);

            setTitle(getTitle().toString().replace("Brasil", "em") + " " + textViewTitle.getText().toString() + " (" + textViewSubTitle.getText().toString() + ")");

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            cities = new ArrayList<>();
            listView = findViewById(R.id.listView);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    searchView.setQuery("", false);
                    searchView.clearFocus();

                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if(inputManager != null && getCurrentFocus() != null) {
                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }

                    Intent intent = new Intent(ViewActivity.this, InnerViewActivity.class);
                    intent.putExtra("state", listView.getItemAtPosition(position).toString() + " / " + textViewTitle.getText().toString() + " / " + textViewSubTitle.getText().toString());
                    startActivity(intent);

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

            shareContent = "";

            instance = this;

            get();

        } catch (Exception e) {

            Toast.makeText(this, "Ocorreu um erro ao exibir as informações, por favor verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();

        }

    }

    private int findStateId() {

        int stateId = -1;

        for(int i = 0; i < MainActivity.instance.statesFull.size(); i ++) {

            String currentState = MainActivity.instance.statesFull.get(i);
            if(currentState.equals(state)) {
                return i;
            }


        }

        return stateId;

    }

    private void get() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Obtendo dados...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://brasil.io/api/dataset/covid19/caso/data?is_last=True&state=" + textViewSubTitle.getText().toString();

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

                                String city = new String(currentJSONObject.getString("city").getBytes("ISO-8859-1"), "UTF-8");

                                if(city.equals("null")) {
                                    continue;
                                }

                                confirmed += currentJSONObject.getInt("confirmed");
                                deaths += currentJSONObject.getInt("deaths");

                                cities.add(city + " / " + currentJSONObject.getInt("confirmed") + " / " + currentJSONObject.getInt("deaths"));

                                if(currentJSONObject.getInt("confirmed") > 0 && currentJSONObject.getInt("deaths") > 0) {
                                    shareContent = shareContent + "\n" + city + ": " + currentJSONObject.getInt("confirmed") + " conf. / "  + currentJSONObject.getInt("deaths") + " mortes";
                                }


                            }

                            textViewConfirmed.setText(confirmed + "");
                            textViewDeaths.setText(deaths + "");

                            listAdapter = new ArrayAdapter<String>(ViewActivity.this, R.layout.list_item, cities);
                            listView.setAdapter(listAdapter);

                            progressDialog.dismiss();

                        } catch (Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(ViewActivity.this, "Ocorreu um erro ao exibir as informações, por favor verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
                            finish();

                        }

                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                Toast.makeText(ViewActivity.this, "Ocorreu um erro ao exibir as informações, por favor verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
                finish();

            }

        });

        queue.add(stringRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(inputManager != null && getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        onBackPressed();
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.optionsmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.share:
                String subject = "Situação do COVID 19 em " + textViewTitle.getText().toString() + " - " + textViewSubTitle.getText().toString();
                String text = "Dados atualizados sobre COVID 19 em " + textViewTitle.getText().toString() + " (" + textViewSubTitle.getText().toString() + "), agora são " + textViewConfirmed.getText().toString() + " casos confirmados e " + textViewDeaths.getText().toString() + " mortes.\n\nNúmeros por cidades:\n" + shareContent + MainActivity.instance.countryNumbers;
                MainActivity.instance.share(subject, text);
                break;
            case R.id.rate:
                MainActivity.instance.rate();
                break;
            case R.id.about:
                startActivity(new Intent(ViewActivity.this, AboutActivity.class));
                break;
            case R.id.exit:
                MainActivity.instance.exitApp();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;

    }

}
