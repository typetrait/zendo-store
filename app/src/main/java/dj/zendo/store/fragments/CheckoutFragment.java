package dj.zendo.store.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import dj.zendo.store.R;
import dj.zendo.store.adapters.ProductAdapter;
import dj.zendo.store.model.Cart;
import dj.zendo.store.tasks.OnSendPurchaseEmailListener;
import dj.zendo.store.tasks.SendPurchaseEmailTask;
import dj.zendo.store.util.CurrencyFormatter;

public class CheckoutFragment extends Fragment implements OnSendPurchaseEmailListener {

    private Context context;
    private ListView productListView;

    public CheckoutFragment() {

    }

    public static CheckoutFragment newInstance() {
        CheckoutFragment fragment = new CheckoutFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        ProductAdapter adapter = new ProductAdapter(getContext(), Cart.items);

        productListView = view.findViewById(R.id.list_checkout_items);
        productListView.setAdapter(adapter);

        registerForContextMenu(productListView);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_checkout, menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_finalize:

                if (Cart.items.size() < 1) {
                    Toast.makeText(getContext(), R.string.checkout_items, Toast.LENGTH_LONG).show();
                    return true;
                }

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                dialogBuilder.setMessage(getString(R.string.checkout_proceed) + "\n" + getString(R.string.checkout_total, CurrencyFormatter.format(Cart.getTotal())))
                        .setCancelable(false)
                        .setPositiveButton(R.string.checkout_proceed_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                new SendPurchaseEmailTask(context, CheckoutFragment.this).execute(Cart.items);
                            }
                        })
                        .setNegativeButton(R.string.checkout_proceed_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog dialog = dialogBuilder.create();
                dialog.show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.list_checkout_items) {
            menu.add(R.string.checkout_remove);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

        ProductAdapter adapter = (ProductAdapter)productListView.getAdapter();

        switch (item.getItemId()) {
            case 0:
                Cart.items.remove(menuInfo.position);
                adapter.notifyDataSetChanged();
                break;
        }

        return super.onContextItemSelected(item);
    }

    @Override
    public void onSendPurchaseEmail(Boolean success) {

        if (success) {

            ProductAdapter adapter = (ProductAdapter)productListView.getAdapter();

            Cart.items.clear();
            adapter.notifyDataSetChanged();

            Toast.makeText(context, R.string.checkout_success, Toast.LENGTH_LONG).show();

        } else {

            Toast.makeText(context, R.string.checkout_fail, Toast.LENGTH_LONG).show();

        }
    }
}