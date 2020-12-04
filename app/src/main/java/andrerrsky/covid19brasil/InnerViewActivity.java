package andrerrsky.covid19brasil;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InnerViewActivity extends AppCompatActivity {

    private String state;

    private TextView textViewTitle;
    private TextView textViewSubTitle;
    private TextView textViewConfirmed;
    private TextView textViewDeaths;
    private String[] separated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner_view);

        try {

            state = getIntent().getExtras().getString("state");

            separated = state.split("/");

            textViewTitle = findViewById(R.id.textViewTitle);
            textViewTitle.setText(separated[0].trim());

            textViewSubTitle = findViewById(R.id.textViewSubTitle);
            textViewSubTitle.setText(separated[3].trim() + " - " + separated[4].trim());

            textViewConfirmed = findViewById(R.id.textViewConfirmed);
            textViewConfirmed.setText(separated[1].trim());

            textViewDeaths = findViewById(R.id.textViewDeaths);
            textViewDeaths.setText(separated[2].trim());

            setTitle(getTitle().toString().replace("Brasil", "em") + " " + textViewTitle.getText().toString() + " - " + separated[4].trim());

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        } catch (Exception e) {

            Toast.makeText(this, "Ocorreu um erro ao exibir as informações, por favor verifique sua conexão e tente novamente.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            finish();

        }

    }

    @Override
    public boolean onSupportNavigateUp() {

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
                String subject = "Situação do COVID 19 em " + textViewTitle.getText().toString() + " - " + separated[4].trim();
                String text = "Dados atualizados sobre COVID 19 em " + textViewTitle.getText().toString() + " - " + separated[4].trim() + ", agora são " + textViewConfirmed.getText().toString() + " casos confirmados e " + textViewDeaths.getText().toString() + " mortes.\n\nNúmeros em outras cidades de " + separated[4].trim() + ":\n" + ViewActivity.instance.shareContent + MainActivity.instance.countryNumbers;
                MainActivity.instance.share(subject, text);
                break;
            case R.id.rate:
                MainActivity.instance.rate();
                break;
            case R.id.about:
                startActivity(new Intent(InnerViewActivity.this, AboutActivity.class));
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
