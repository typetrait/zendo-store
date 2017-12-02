package dj.zendo.store.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.List;

import dj.zendo.store.R;
import dj.zendo.store.activities.ProductDetailsActivity;
import dj.zendo.store.adapters.ProductAdapter;
import dj.zendo.store.model.Product;
import dj.zendo.store.tasks.FetchProductsTask;
import dj.zendo.store.tasks.OnProductsFetchListener;

public class StoreFragment extends Fragment implements OnProductsFetchListener {

    private Context context;
    private ProgressBar progressBar;
    private View view;

    public StoreFragment() {

    }

    public static StoreFragment newInstance() {
        StoreFragment fragment = new StoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_store, container, false);

        progressBar = view.findViewById(R.id.progressBar);

        FetchProductsTask fetchProductsTask = new FetchProductsTask(getActivity(), this);
        fetchProductsTask.execute();

        this.view = view;

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onProductsFetch(final List<Product> products) {

        final ProductAdapter adapter = new ProductAdapter(context, products);

        ListView productListView = view.findViewById(R.id.list_store_products);
        productListView.setAdapter(adapter);

        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent i = new Intent(getActivity(), ProductDetailsActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("id", products.get(position).getId());

                startActivity(i);
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
    }
}