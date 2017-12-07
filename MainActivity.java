package com.example.android.justjava;

/**
 * IMPORTANT: Add your package below. Package name can be found in the project's AndroidManifest.xml file.
 * This is the package name our example uses:
 * <p>
 * package com.example.android.justjava;
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int priceOfCoffee = 5;
    public static final int priceOfWhippedCream = 1;
    public static final int priceOfChocolate = 2;

    int quantity = 2;
    boolean whippedCreamStatus = false;
    boolean chocolateStatus = false;
    String userText = "";
    int orderPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is pressed
     */
    public void increment(View view) {
        if (quantity == 100) {
            Log.i("Increment: ", "You cannot have more than 100 coffees: " + quantity);
            Toast.makeText(this, "You cannot have more than 100 coffees", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is pressed
     */
    public void decrement(View view) {
        if (quantity == 1) {
            Log.i("Decrement: ", "You cannot have less than 1 coffee: " + quantity);
            Toast.makeText(this, "You cannot have less than 1 coffee", Toast.LENGTH_SHORT).show();
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the Summary button is clicked.
     */
    public void submitOrder(View view) {

        displayMessage(createOrderSummary());

    }

    /**
     * This method is called when E-mail button is clicked
     */
    public void EmailOrder(View view) {
        String EmailSubject = "[Coffee Shop] Order Summary for " + userText;
        String EmailBody = createOrderSummary();
        Log.i("EmailOrder: ", "EmailOrder() has been called");

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/html");
        intent.setData(Uri.parse("mailto: zciprian@yahoo.com"));
        intent.putExtra(Intent.EXTRA_EMAIL, "emailaddress@emailaddress.com");
        intent.putExtra(Intent.EXTRA_SUBJECT, EmailSubject);
        intent.putExtra(Intent.EXTRA_TEXT, EmailBody);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
            Log.i("EmailOrder: ", "After startactivity(intent) ");
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    /**
     * This method displays the order message on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

    /**
     * This method calculates price of the order.
     */
    private int calculatePrice(boolean whippedCreamStatus, boolean chocolateStatus) {
        int basePrice = priceOfCoffee;
        if (whippedCreamStatus) {
            basePrice = basePrice + priceOfWhippedCream;
        }
        if (chocolateStatus) {
            basePrice = basePrice + priceOfChocolate;
        }
        return basePrice * quantity;
    }

    /**
     * This method creates the orderSummary, taking all the input from the screen and transforming into a String.
     */
    private String createOrderSummary() {

        CheckBox myCheckBoxWhippedCream = (CheckBox) findViewById(R.id.checkbox_whipped_cream);
        whippedCreamStatus = myCheckBoxWhippedCream.isChecked();

        CheckBox myCheckBoxChocolate = (CheckBox) findViewById(R.id.checkbox_chocolate);
        chocolateStatus = myCheckBoxChocolate.isChecked();

        orderPrice = calculatePrice(whippedCreamStatus, chocolateStatus);

        EditText myEditText = (EditText) findViewById(R.id.nameField);
        userText = myEditText.getText().toString();

        String FalseTrueWhippedCream = "No";
        String FalseTrueChocolate = "No";

        if (whippedCreamStatus) {
            FalseTrueWhippedCream = "Yes";
        }

        if (chocolateStatus) {
            FalseTrueChocolate = "Yes";
        }
        String OrderSummaryMessage = "Name: " + userText;
        OrderSummaryMessage += "\n" + getString(R.string.add_whipped_cream) + " " + FalseTrueWhippedCream;
        OrderSummaryMessage += "\n" + getString(R.string.add_chocolate) + " " + FalseTrueChocolate;
        OrderSummaryMessage += "\n" + getString(R.string.quantity) + " " + quantity;
        OrderSummaryMessage += "\n" + getString(R.string.total) + " " + "$" + orderPrice;
        OrderSummaryMessage += "\n" + getString(R.string.thank_you);
        return OrderSummaryMessage;
    }

    /**
     * This method hides soft keyboard whenever is clicked outside the Edit Box  (Thanks to stackoverflow)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            Log.d("Activity", "Touch event " + event.getRawX() + "," + event.getRawY() + " " + x + "," + y + " rect " + w.getLeft() + "," + w.getTop() + "," + w.getRight() + "," + w.getBottom() + " coords " + scrcoords[0] + "," + scrcoords[1]);
            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom())) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
}