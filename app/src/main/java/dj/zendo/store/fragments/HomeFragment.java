package dj.zendo.store.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import dj.zendo.store.R;
import dj.zendo.store.activities.AboutActivity;
import dj.zendo.store.activities.ProductDetailsActivity;
import dj.zendo.store.adapters.FeaturedProductAdapter;
import dj.zendo.store.model.Product;
import dj.zendo.store.tasks.FetchFeaturedProductsTask;
import dj.zendo.store.tasks.OnProductsFetchListener;

public class HomeFragment extends Fragment implements OnProductsFetchListener {

    private Context context;
    private View view;

    public HomeFragment() {

    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FetchFeaturedProductsTask fetchFeaturedProductsTask = new FetchFeaturedProductsTask(getActivity(), this);
        fetchFeaturedProductsTask.execute();

        this.view = view;

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_about:
                Intent i = new Intent(getContext(), AboutActivity.class);
                startActivity(i);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onProductsFetch(List<Product> products) {

        final FeaturedProductAdapter adapter = new FeaturedProductAdapter(context, products);

        GridView featuredGridView = (GridView)view.findViewById(R.id.grid_home_featured);
        featuredGridView.setAdapter(adapter);

        featuredGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i = new Intent(getContext(), ProductDetailsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("id", adapter.getItem(position).getId());

                getContext().startActivity(i);

            }
        });
    }
}