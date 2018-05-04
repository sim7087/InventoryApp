package com.newsapp071997.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.newsapp071997.inventoryapp.data.InventoryContract;

public class InventoryCursorAdaptor extends CursorAdapter {
    private final MainActivity activity;
    Button saleButton;

    public InventoryCursorAdaptor(MainActivity context, Cursor c) {
        super(context, c, 0);
        this.activity = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView quantityTextView = view.findViewById(R.id.quantity_text_view);
        TextView productNameTextView = view.findViewById(R.id.product_name_text_view);
        TextView priceTextView = view.findViewById(R.id.price_text_view);
        ImageView productImage = view.findViewById(R.id.product_image_view);
        saleButton = view.findViewById(R.id.sale);
        int nameIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_NAME);
        int quantityIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_QUANTITY);
        int imageIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRODUCT_IMAGE);
        int priceIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_PRICE);
        String name = cursor.getString(nameIndex);
        Integer price = cursor.getInt(priceIndex);
        final Integer quantity = cursor.getInt(quantityIndex);
        String image = cursor.getString(imageIndex);
        Uri uri = null;
        if (image != null) {
            uri = Uri.parse(image);
            productImage.setImageURI(uri);
        }
        quantityTextView.setText(quantity.toString());
        priceTextView.setText("Rs. " + price.toString());
        productNameTextView.setText(name);
        final long id = cursor.getLong(cursor.getColumnIndex(InventoryContract.InventoryEntry._ID));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickViewItem(id);
            }
        });
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.clickOnSaleButton(id, quantity);
            }
        });
    }
}
