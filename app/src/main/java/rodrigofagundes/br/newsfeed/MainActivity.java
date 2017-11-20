package rodrigofagundes.br.newsfeed;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String LOG_TAG = MainActivity.class.getName();
    private ListView articleListView;
    private ArticleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the {@link ListView} in the layout
        articleListView = (ListView) findViewById(R.id.list);

        adapter = new ArticleAdapter(this, new ArrayList<Article>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        articleListView.setAdapter(adapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // Achar o terremoto atual que foi clicado
                    Article currentArticle = adapter.getItem(position);

                    // Converte o URL String em um objeto URI (para passar no construtor de Intent)
                    Uri articleUri = Uri.parse(currentArticle.getLink());

                    // Cria um novo intent para visualizar a URI do article
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                    // Envia o intent para lançar uma nova activity
                    startActivity(websiteIntent);
            }
        });

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        LoaderManager loaderManager = getLoaderManager();
        if (isConnected) {
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            ((ProgressBar)findViewById(R.id.loading)).setVisibility(View.GONE);
            TextView msgView = (TextView)findViewById(R.id.empty);
            msgView.setText("No internet connection");
            articleListView.setEmptyView(msgView);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String numberOfDays = sharedPrefs.getString(
        getString(R.string.settings_from_last_days_key),
        getString(R.string.settings_from_last_days_default));

        Calendar fromDate = Calendar.getInstance();
        fromDate.add(Calendar.DAY_OF_MONTH, -Integer.valueOf(numberOfDays));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String strFromDate = formatter.format(fromDate.getTime());

        Uri baseUri = Uri.parse(QueryUtils.getUrl());
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("from-date", strFromDate);
        uriBuilder.appendQueryParameter("api-key", "test");

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        ((ProgressBar)findViewById(R.id.loading)).setVisibility(View.GONE);
        adapter.clear();
        if (articles != null && !articles.isEmpty()) {
            adapter.addAll(articles);
        } else {
            TextView msgView = (TextView)findViewById(R.id.empty);
            msgView.setText("No articles found");
            articleListView.setEmptyView(msgView);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
