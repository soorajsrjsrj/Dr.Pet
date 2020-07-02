package com.example.drpet;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;

public class PetHospitalNearByFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NearbyHospitals>>, SharedPreferences.OnSharedPreferenceChangeListener  {

    private static final int EARTHQUAKE_LOADER_ID = 1;
    NearbyAdapter mAdapter;


    private static final String LOG_TAG = PetHospitalNearByFragment.class.getName();

    /** URL for earthquake data from the USGS dataset */
    private static final String USGS_REQUEST_URL =
            "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=43.7712,-79.2144&rankby=distance&type=veterinary_care&key=AIzaSyB8-VBp-EES8iDNiB-9pWCCwR0YspOuPeY";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pethospital, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        ArrayList<NearbyHospitals> nearhospital = new ArrayList<>();
//        nearhospital.add(new NearbyHospitals("MARKHAM","23KM","2.3"));
//        nearhospital.add(new NearbyHospitals("MARKHAM","23KM","2.3"));
//        nearhospital.add(new NearbyHospitals("MARKHAM","23KM","2.3"));
//        nearhospital.add(new NearbyHospitals("MARKHAM","23KM","2.3"));
//        nearhospital.add(new NearbyHospitals("MARKHAM","23KM","2.3"));


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) getView().findViewById(R.id.list);

        // Create a new {@link ArrayAdapter} of nearbbyhospitals
//
////        ArrayList<NearbyHospitals> earthquakes = QueryUtils.extractnearByhospital();
//        NearbyAdapter adapter = new NearbyAdapter(getActivity(), nearhospital);
//
//        // Set the adapter on the {@link ListView}
//        // so the list can be populated in the user interface
//        earthquakeListView.setAdapter(adapter);

        mAdapter = new NearbyAdapter(getActivity(), new ArrayList<NearbyHospitals>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);






//        new
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = getActivity().findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
//
        }


    }

    @NonNull
    @Override
    public Loader<List<NearbyHospitals>> onCreateLoader(int id, @Nullable Bundle args) {
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new NearbyLoadr(getActivity(), uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NearbyHospitals>> loader, List<NearbyHospitals> nearhsptls) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = getActivity().findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
//        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        //mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (nearhsptls != null && !nearhsptls.isEmpty()) {
//            mAdapter.addAll(data);
//
//
//   updateUi(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NearbyHospitals>> loader) {


        mAdapter.clear();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
