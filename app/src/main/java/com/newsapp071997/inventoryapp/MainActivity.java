package com.newsapp071997.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.newsapp071997.inventoryapp.data.InventoryContract.InventoryEntry;
import com.newsapp071997.inventoryapp.data.InventoryDbHelper;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int CURSOR_LOADER_ID = 0;
    InventoryCursorAdaptor mCursorAdaptor;
    private InventoryDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton ab = findViewById(R.id.fab);

        ab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, ProductInfo.class));
            }

        });

        final ListView inventoryListView = findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);
        dbHelper = new InventoryDbHelper(this);
        Cursor cursor = dbHelper.readProduct();
        mCursorAdaptor = new InventoryCursorAdaptor(this, cursor);
        inventoryListView.setAdapter(mCursorAdaptor);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_QUANTITY,
                InventoryEntry.COLUMN_PRICE,
                InventoryEntry.COLUMN_SUPPLIER_PHONE_NUMBER,
                InventoryEntry.COLUMN_PRODUCT_IMAGE
        };
        return new CursorLoader(this,
                InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdaptor.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdaptor.swapCursor(null);
    }

    public void clickViewItem(long id) {
        Intent intent = new Intent(MainActivity.this, ProductInfo.class);
        Uri currentProductUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
        intent.setData(currentProductUri);
        intent.putExtra("productId", id);
        startActivity(intent);
    }

    public void clickOnSaleButton(long id, int quantity) {
        dbHelper.decreaseQuantityByOne(id, quantity);
        mCursorAdaptor.swapCursor(dbHelper.readProduct());
    }

}
