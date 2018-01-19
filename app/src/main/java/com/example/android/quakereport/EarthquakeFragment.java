package com.example.android.quakereport;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by ntumba on 17-12-28.
 */

public class EarthquakeFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {

    private LoaderManager manager;
    private ProgressBar progressBar;

    private static final String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    RecyclerView recyclerView;
    List<EarthQuake> list;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.earthquake_fragment, container, false);

        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);

        recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new Adapter(new LinkedList<EarthQuake>()));

        list = new LinkedList<>();
        list.add(new EarthQuake("2.3", "dkjfkdjfkjd", "23;232"));
        recyclerView.setAdapter(new Adapter(list));
        manager = getLoaderManager();
        startAsychTask();

        return view;
    }



    public void startAsychTask(){
        manager.initLoader(1 , null , this);
    }



    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {


        private List<EarthQuake> list;

        public Adapter(List<EarthQuake> list) {
            this.list = list;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(EarthquakeFragment.this.getActivity());
            View view = inflater.inflate(R.layout.quake_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<EarthQuake> list) {
            this.list = list;
        }


        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private EarthQuake earthQuake;
            private TextView mag, city, date;

            public ViewHolder(View view) {
                super(view);
                view.setOnClickListener(this);
                mag = (TextView) view.findViewById(R.id.magnitude);
                city = (TextView) view.findViewById(R.id.location);
                date = (TextView) view.findViewById(R.id.date);
            }


            public void bind(EarthQuake earthQuake) {
                this.earthQuake = earthQuake;
                mag.setText(earthQuake.getMagnitude());
                city.setText(earthQuake.getCity());
                date.setText(earthQuake.getDate());

                GradientDrawable color = (GradientDrawable) mag.getBackground();
                int colorMag = getMagnitudeColor(Double.parseDouble(earthQuake.getMagnitude()));
                color.setColor(colorMag);
            }


            private int getMagnitudeColor(double magnitude) {
                int magnitudeColorResourceId;
                int magnitudeFloor = (int) Math.floor(magnitude);
                switch (magnitudeFloor) {
                    case 0:
                    case 1:
                        magnitudeColorResourceId = R.color.magnitude1;
                        break;
                    case 2:
                        magnitudeColorResourceId = R.color.magnitude2;
                        break;
                    case 3:
                        magnitudeColorResourceId = R.color.magnitude3;
                        break;
                    case 4:
                        magnitudeColorResourceId = R.color.magnitude4;
                        break;
                    case 5:
                        magnitudeColorResourceId = R.color.magnitude5;
                        break;
                    case 6:
                        magnitudeColorResourceId = R.color.magnitude6;
                        break;
                    case 7:
                        magnitudeColorResourceId = R.color.magnitude7;
                        break;
                    case 8:
                        magnitudeColorResourceId = R.color.magnitude8;
                        break;
                    case 9:
                        magnitudeColorResourceId = R.color.magnitude9;
                        break;
                    default:
                        magnitudeColorResourceId = R.color.magnitude10plus;
                        break;
                }
                return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
            }


            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.quake_item:
                        launchBrowser();
                        break;
                }
            }


            private void launchBrowser() {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri uri = Uri.parse(earthQuake.getWebAddress());
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }





    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.quake_menu , menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings :
                Intent intent = SettingsActivity.getIntent(getActivity());
                startActivity(intent);
                return true;

            default :
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String magnitude = sharedPrefs.getString(
                getString(R.string.setting_min_magnitude_key) ,
                getString(R.string.setting_min_magnitude_default));


        Uri baseuri = Uri.parse(URL);
        Uri.Builder builder = baseuri.buildUpon();

        builder.appendQueryParameter("format" , "geojson");
        builder.appendQueryParameter("limit" , "10");
        builder.appendQueryParameter("minmag" , magnitude);
        builder.appendQueryParameter("orderby" , "time");

        return new Back2(getActivity() , builder.toString());
    }


    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
        this.list.clear();
        this.list.addAll(data);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {

    }




    public static EarthquakeFragment instance() {
        return new EarthquakeFragment();
    }


    private class Back extends AsyncTask<String, Void, List<EarthQuake>> {


        @Override
        protected List<EarthQuake> doInBackground(String... strings) {
            List<EarthQuake> list = null;
            try {
                list = NetWorkUtils.getEarthQuakeList(strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return list;
        }


        @Override
        protected void onPostExecute(List<EarthQuake> earthQuakes) {
            super.onPostExecute(earthQuakes);

            EarthquakeFragment.this.list.clear();
            EarthquakeFragment.this.list.addAll(earthQuakes);
            recyclerView.getAdapter().notifyDataSetChanged();

        }
    }



}
