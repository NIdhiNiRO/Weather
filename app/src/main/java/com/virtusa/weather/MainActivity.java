package com.virtusa.weather;

import android.app.SearchManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.virtusa.weather.model.weatherModel;
import com.virtusa.weather.retrofit.RetroClient;
import com.virtusa.weather.utils.BaseActivity;
import com.virtusa.weather.utils.Constants;
import com.virtusa.weather.utils.PreferenceUtil;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by nidhiparekh on 10/11/17.
 */
public class MainActivity extends BaseActivity {

    private RecyclerView listWeather;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initControls();
        setListeners();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**we can also give solution for to very first time, grab
        latitude and longitude of location > fetch location name or use lat,long api to retrieve details***/
        if(PreferenceUtil.getLastSearchText().isEmpty()) {
            promptMessage(getString(R.string.enter_city_name));
            return;
        }
       getData(PreferenceUtil.getLastSearchText());
    }

    private void setListeners() {
    }

    private void initControls() {
        listWeather = (RecyclerView) findViewById(R.id.recyclerWeatherList);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listWeather.setLayoutManager(layoutManager);

    }

    private void getData(final String query) {
        Call<weatherModel> getLocation = RetroClient.getServiceApis().GetWeatherDetails(query+",US", Constants.APP_ID);
        getLocation.enqueue(new Callback<weatherModel>() {
            @Override
            public void onResponse(Response<weatherModel> response, Retrofit retrofit) {
                if (response.body() == null) return;
                adapter = new RecyclerAdapter();
                adapter.setData(response.body());
                listWeather.setAdapter(adapter);
                PreferenceUtil.setLastSearchText(query);
            }

            @Override
            public void onFailure(Throwable t) {
                promptMessage(getString(R.string.error_message));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final SearchView searchView = (SearchView) (menu.findItem(R.id.action_search)).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        final EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchEditText.setHint("Enter city to search");
        searchEditText.setHintTextColor(getResources().getColor(android.R.color.white));
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //run query to the server
                    if(!searchEditText.getText().toString().trim().isEmpty()){
                        getData(searchEditText.getText().toString().trim());
                    }
                    searchView.clearFocus();
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder>{
        weatherModel model;

        RecyclerAdapter() {
            model = new weatherModel();
        }


        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_row_weather, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            Glide.with(MainActivity.this).load(RetroClient.IMAGE_URL+model.getWeather().get(position).getIcon()+".png").into(holder.imgWeather);
            holder.txtLocationName.setText(model.getName() + ", " + model.getSys().getCountry());
            holder.txtWeatherMessage.setText(model.getWeather().get(position).getDescription());
            holder.txtLatnLong.setText("Geo Coordrs [" + model.getCoord().getLat() + ", " + model.getCoord().getLon() + "]");
            double minCelc = model.getMain().getTempMin() - 273.15;
            double maxCelc = model.getMain().getTempMax() - 273.15;
            holder.txtDesciption.setText("Temperature from " + minCelc + " to " + maxCelc + " celsius, wind " + model.getWind().getSpeed() + "m/s. Clouds" + model.getClouds().getAll() + "%, " + model.getMain().getPressure() + "pa.");

        }


        @Override
        public int getItemCount() {
            return model!=null?model.getWeather().size():0;
        }

        public void setData(weatherModel body) {
            model = body;
            adapter.notifyDataSetChanged();
        }

        class CustomViewHolder extends RecyclerView.ViewHolder {
            ImageView imgWeather;
            TextView txtLocationName;
            TextView txtWeatherMessage;
            TextView txtDesciption;
            TextView txtLatnLong;

            public CustomViewHolder(View itemView) {
                super(itemView);
                imgWeather = (ImageView) itemView.findViewById(R.id.imgWeather);
                txtLocationName = (TextView) itemView.findViewById(R.id.txtLocationName);
                txtWeatherMessage = (TextView) itemView.findViewById(R.id.txtWeatherMessage);
                txtDesciption = (TextView) itemView.findViewById(R.id.txtDesciption);
                txtLatnLong = (TextView) itemView.findViewById(R.id.txtLatAndLong);
            }
        }
    }

}
