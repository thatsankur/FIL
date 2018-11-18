package fl.ub;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String QUERY_PARAM = "QUERY_PARAM";
    private EditText queryEditText;
    private Button searchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        queryEditText = findViewById(R.id.queryEditText);
        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String queryString = queryEditText.getText().toString();
                if(!TextUtils.isEmpty(queryString)){
                    launchGalleryPage(queryString);
                }else{
                    Toast.makeText(view.getContext(),"Query Can't be Empty",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void launchGalleryPage(String queryString) {
        Intent photoListingIntent = new Intent(this,PhotoListingActivity.class);
        photoListingIntent.putExtra(QUERY_PARAM,queryString);
        startActivity(photoListingIntent);
    }
    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }
}
