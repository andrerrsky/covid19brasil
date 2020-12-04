package andrerrsky.covid19brasil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void contact(View v) {
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getText(R.string.dev_email) + ""});
        i.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(R.string.contact_subject));
        try {
            startActivity(Intent.createChooser(i, getResources().getText(R.string.contact)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AboutActivity.this, getResources().getText(R.string.email_client_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
