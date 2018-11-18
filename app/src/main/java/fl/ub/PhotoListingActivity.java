package fl.ub;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import fl.ub.adapter.PhotoItemAdapter;
import fl.ub.model.FlickerAPIResponse;
import fl.ub.parser.ParsingUtil;

public class PhotoListingActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<FlickerAPIResponse>{
    private static final String QUERY_URL_EXTRA = "url";
    private static final int OPERATION_URL_LOADER = 1;
    private int CURRENT_PAGE_INDEX = 0;
    private int MAX_PAGE_REQ = 0;
    private String queryParam;
    private RecyclerView recyclerView;
    private PhotoItemAdapter photoItemAdapter;
    private boolean isLoading;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_listing);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queryParam = getIntent().getStringExtra(MainActivity.QUERY_PARAM);
group = findViewById(R.id.group);
        findAndInitRecyclerView();
        loadList();

    }

    private void loadList() {
        // Create a bundle called queryBundle
        if(!isLoading) {
            if(MAX_PAGE_REQ == 0 || CURRENT_PAGE_INDEX <= MAX_PAGE_REQ) {
                isLoading = true;
                group.setVisibility(View.VISIBLE);
                Bundle queryBundle = new Bundle();
                int index = CURRENT_PAGE_INDEX++;
                String url = null;
                try {
                    url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&%20format=json&nojsoncallback=1&safe_search=1&text=" + URLEncoder.encode(queryParam, "UTF-8") + "&page=" + index;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Log.d("Ankur", "Loading page " + index + " url " + url);
                queryBundle.putString(QUERY_URL_EXTRA, url);
                // Call getSupportLoaderManager and store it in a LoaderManager variable
                LoaderManager loaderManager = getSupportLoaderManager();
                // Get our Loader by calling getLoader and passing the ID we specified
                Loader<FlickerAPIResponse> loader = loaderManager.getLoader(OPERATION_URL_LOADER);
                // If the Loader was null, initialize it. Else, restart it.
                if (loader == null) {
                    loaderManager.initLoader(OPERATION_URL_LOADER, queryBundle, this);
                } else {
                    loaderManager.restartLoader(OPERATION_URL_LOADER, queryBundle, this);
                }
            }
        }
    }

    private void findAndInitRecyclerView() {
        recyclerView = findViewById(R.id.list);
        photoItemAdapter = new PhotoItemAdapter();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(photoItemAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutManager=GridLayoutManager.class.cast(recyclerView.getLayoutManager());
                int totalItemCount = layoutManager.getItemCount();
                int lastVisible = layoutManager.findLastVisibleItemPosition();

                boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
                if (totalItemCount > 0 && endHasBeenReached) {
                    //you have reached to the bottom of your recycler view
                    loadList();
                }
            }
        });
    }

    @NonNull
    @Override
    public Loader<FlickerAPIResponse> onCreateLoader(int i, @Nullable Bundle bundle) {
        final String url = bundle.getString(QUERY_URL_EXTRA);
        return new AsyncTaskLoader<FlickerAPIResponse>(this) {
            @Nullable
            @Override
            public FlickerAPIResponse loadInBackground() {
                try {
                    return ParsingUtil.loadResponse(url);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onStartLoading() {
                    forceLoad();
            }

            @Override
            protected void onStopLoading() {
                cancelLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<FlickerAPIResponse> loader, FlickerAPIResponse response) {
        if(response!=null){
            MAX_PAGE_REQ = response.getPhotos().getPages();
            photoItemAdapter.setItem(0,response.getPhotos().getPhoto());
        }
        isLoading = false;
        group.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<FlickerAPIResponse> loader) {

    }

    @Override
    public boolean onNavigateUp(){
        finish();
        return true;
    }

}
