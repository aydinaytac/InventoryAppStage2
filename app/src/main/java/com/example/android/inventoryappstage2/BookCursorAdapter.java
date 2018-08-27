package com.example.android.inventoryappstage2;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryappstage2.data.BookContract;

import static android.provider.Settings.System.getString;

/**
 * {@link BookCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of pet data as its data source. This adapter knows
 * how to create list items for each row of pet data in the {@link Cursor}.
 */
public class BookCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link BookCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView nameTextView = (TextView) view.findViewById(R.id.list_book_name);
        final TextView quantityTextView = (TextView) view.findViewById(R.id.list_book_quantity);
        TextView priceTextView = (TextView) view.findViewById(R.id.list_book_price);
        Button saleButton = (Button) view.findViewById(R.id.list_sale_button);

        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        final int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
        final Uri currentBookUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI,
                cursor.getInt(cursor.getColumnIndexOrThrow(BookContract.BookEntry._ID)));

        // Populate fields with extracted properties
        nameTextView.setText(name);
        quantityTextView.setText(String.valueOf(quantity));
        priceTextView.setText(String.valueOf(price));

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (quantity > 0) {
                    Integer saleQuantity = quantity - 1;
                    ContentValues values = new ContentValues();
                    values.put(BookContract.BookEntry.COLUMN_BOOK_QUANTITY, saleQuantity);
                    context.getContentResolver().update(currentBookUri, values, null, null);
                    quantityTextView.setText(saleQuantity.toString());
                }
            }
        });
    }
}