package com.andvilouette.vilouette.activity.ui.home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.andvilouette.vilouette.Adapter.MoviesAdapter;
import com.andvilouette.vilouette.Connectivity.RestClient;
import com.andvilouette.vilouette.DataObject.Products;
import com.andvilouette.vilouette.DataObject.Products_cat;
import com.andvilouette.vilouette.DataObject.User;
import com.andvilouette.vilouette.Model.Movie;
import com.andvilouette.vilouette.R;
import com.andvilouette.vilouette.activity.Homenav_act;
import com.andvilouette.vilouette.activity.Login_act;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;


public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<Movie> listdata = new ArrayList<>();
    private RecyclerView recyclerView;
    private MoviesAdapter mAdapter;
    ProgressDialog dialog;
    private List<Products> feedList = new ArrayList<>();
    private List<Products> feedList1 = new ArrayList<>();
    Products newFeed1;
    CategoryListing catadapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);


        // mAdapter = new MoviesAdapter(listdata);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //  recyclerView.setAdapter(mAdapter);

        //  prepareMovieData();
        new GetDashboardApi("28").execute();
        return root;
    }


    private void prepareMovieData() {
        Movie movie = new Movie("BUY", R.drawable.list1);
        listdata.add(movie);

        movie = new Movie("BUY", R.drawable.list1);
        listdata.add(movie);

      /*  movie = new Movie("BUY", R.drawable.list1);
        listdata.add(movie);
        movie = new Movie("BUY", R.drawable.list1);
        listdata.add(movie);
        movie = new Movie("BUY", R.drawable.list1);
        listdata.add(movie);
        movie = new Movie("BUY", R.drawable.list1);
        listdata.add(movie);*/


        mAdapter.notifyDataSetChanged();
    }


    public class GetDashboardApi extends AsyncTask<Void, Void, Boolean> {
        String responseString;
        Boolean success = false;

        GetDashboardApi(String id) {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.show();
            dialog.setCancelable(false);
            // pbLoading.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... v) {

            try {

                RequestParams params = new RequestParams();
                params.put("customerID", "28");


                String str_url = getActivity().getString(R.string.url_live);
                String normalUrl = str_url + "account/Api/dashboardInfo";

                android.util.Log.i("DASHBOARD", normalUrl);


                JSONObject jObject = new JSONObject();
                jObject.accumulate("customerID", "28");

                StringEntity entity = new StringEntity(jObject.toString());
                Log.i("test", jObject.toString());

                RestClient.post(getActivity(), normalUrl, entity, new JsonHttpResponseHandler() {
                    // RestClient.post(normalUrl,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        success = false;
                        responseString = response;
                        android.util.Log.d("response", responseString.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        success = false;
                        //                       responseString = errorResponse.toString();
                        android.util.Log.d("response", errorResponse.toString());
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        android.util.Log.d("response", errorResponse.toString());
                        success = false;
                        responseString = errorResponse.toString();
                        android.util.Log.d("response", responseString);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        super.onSuccess(statusCode, headers, responseString);
                        android.util.Log.i("responseString", responseString);
                        success = true;
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        android.util.Log.i("JSONObject response", response.toString());
                        try {

                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            JSONArray jsonArray = jsonObject.getJSONArray("suggestion_list");

                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String Cat_images = object.getString("imagelist");
                                    JSONArray jsn = new JSONArray(Cat_images);
                                    Log.d("CAT_IMAGES", Cat_images);
                                    for (int j = 0; j < jsn.length(); j++) {
                                        JSONObject object1 = jsn.getJSONObject(j);
                                        Log.d("JSON_IMAGES", String.valueOf(object1));

                                        newFeed1 = new Gson().fromJson(object1.toString(), Products.class);
                                        feedList1.add(newFeed1);
                                        Log.d("IMAGES_DATA1",feedList1+newFeed1.getImage_name());

                                    }


                                    Products newFeed = new Gson().fromJson(object.toString(), Products.class);

                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //do stuff like remove view etc
                                            feedList.add(newFeed);
                                            catadapter = new CategoryListing(feedList, getActivity());
                                            catadapter.notifyDataSetChanged();
                                            recyclerView.setHasFixedSize(true);
                                            recyclerView.setAdapter(catadapter);
                                            Log.d("SHOWDATA", feedList + newFeed.getProduct_name());

                                        }
                                    });
                                }
                            } else {


                                //  Toast.makeText(RideHistory_act.this, "No Detail Found", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        android.util.Log.i("JSONArray response", response.toString());
                        success = true;
                    }
                });

                return success;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            dialog.cancel();
            //pbLoading.setVisibility(View.GONE);

        }

    }

    public class CategoryListing extends RecyclerView.Adapter<CategoryListing.PostViewHolder> {

        private List<Products> list;
        Context context;
        String jobid;
        User user;
        UserAllergyIconAdapter adapter;

        public CategoryListing(List<Products> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public void onBindViewHolder(final CategoryListing.PostViewHolder postViewHolder, final int position) {

            final Products item = list.get(position);
            postViewHolder.tvProductId.setText(item.getProduct_id());
            postViewHolder.tvProductName.setText(item.getProduct_name());

            /*List<Products> aList = new ArrayList<>();
            for(String s : item.user.allergens){
                Products a = new Products();
                a.seti(s);
                aList.add(a);
            }*/
            Log.d("IMAGES_DATA",feedList1+newFeed1.getImage_name());
            adapter = new UserAllergyIconAdapter(feedList1,context);
            adapter.notifyDataSetChanged();
            postViewHolder.rvAllergyIcon.setAdapter(adapter);


        }

        @Override
        public CategoryListing.PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_list, viewGroup, false);


            return new CategoryListing.PostViewHolder(itemView);
        }


        public class PostViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvProductName, tvProductId;
            protected RecyclerView rvAllergyIcon,rvMealImages;


            public PostViewHolder(View v) {
                super(v);
                tvProductName = (TextView) v.findViewById(R.id.product_title);
                tvProductId = v.findViewById(R.id.product_id);
                rvAllergyIcon = (RecyclerView) v.findViewById(R.id.recycler_productView);
                rvAllergyIcon.setLayoutManager(new GridLayoutManager(context,2));


            }
        }

    }


    public class UserAllergyIconAdapter extends RecyclerView.Adapter<UserAllergyIconAdapter.PostViewHolder> {

        private List<Products> allergyList;
        private Context context;
        private final int limit = 2;


        public UserAllergyIconAdapter(List<Products> allergyList, Context context) {
            this.allergyList = allergyList;
            this.context = context;
        }

        @Override
        public int getItemCount() {
            if(allergyList.size() > limit){
                return limit;
            }
            else
            {
                return allergyList.size();
            }
           // return allergyList.size();
        }

        @Override
        public void onBindViewHolder(final PostViewHolder postViewHolder, final int position) {

            final Products item = allergyList.get(position);
            postViewHolder.tvAllergy.setText(item.getImage_name());

           Glide.with(context).load(item.getImage_url()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE).skipMemoryCache(true).placeholder(R.drawable.vilouette_icon).dontAnimate().timeout(60000)).into(postViewHolder.product_img);


        }

        @Override
        public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_cat, viewGroup, false);


            return new PostViewHolder(itemView);
        }


        public class PostViewHolder extends RecyclerView.ViewHolder {

            protected TextView tvAllergy;
            protected  ImageView product_img;

            public PostViewHolder(View v) {
                super(v);
                tvAllergy = (TextView) v.findViewById(R.id.tvAllergy);
                product_img =  v.findViewById(R.id.product_image);

            }
        }
    }

}