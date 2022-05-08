package com.example.fask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fask.Adapters.CheckoutAdapter;
import com.example.fask.Models.Address;
import com.example.fask.Models.DifferentOrderForCheckout;
import com.example.fask.Models.Order;
import com.example.fask.Models.ProductListItem;
import com.example.fask.databinding.ActivityCheckoutBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Checkout extends AppCompatActivity {
    private ActivityCheckoutBinding binding;
    private ProgressDialog pd;
    private CheckoutAdapter adapter;
    private ArrayList<Order> productList;
    private LinearLayoutManager llm;
    private int cartTotal = 0;
    private String completeAddress;
    private final static String TAG_MAIN = "DebuggingMain";

    int GOOGLE_PAY_REQUEST_CODE = 123;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    // payment code
    String amount;
    String name = "Mansoori Khalid";
    String upiId = "mansoorikhalid033@okaxis";
    String transactionNote = "Test payment";
    String status;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCheckoutBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        Objects.requireNonNull(getSupportActionBar()).hide();
        setTitle("Checkout");

        pd = new ProgressDialog(this);
        pd.setMessage("Wait a minute");
        productList = new ArrayList<>();
        llm = new LinearLayoutManager(this);

        getAddress();
        getProduct();

        binding.change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // placing the order on pressing place order button.
        binding.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uri = getUpiPaymentUri(name, upiId, transactionNote, amount);
                payWithGpay();
            }
        });

    }

    private void getProduct() {
        FirebaseDatabase.getInstance().getReference().
                child("Orders").
                child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                productList.clear();
                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Order cartItem = snapshot1.getValue(Order.class);
                    cartTotal += Integer.parseInt(Objects.requireNonNull(cartItem.getProductPrice()));
                    productList.add(cartItem);
                }
                // Setting total prices.
                binding.subTotalNumber.setText(String.valueOf("₹ " + cartTotal));
                binding.totalNumber.setText(String.valueOf("₹ " + cartTotal));
                amount = String.valueOf(cartTotal);
                // Setting up recyclerView
                adapter = new CheckoutAdapter(Checkout.this, productList);
                binding.allItemRecyclerView.setAdapter(adapter);
                binding.allItemRecyclerView.setLayoutManager(llm);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAddress() {
        pd.show();
        FirebaseDatabase.getInstance().getReference().child("Address").child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot sp: snapshot.getChildren()){
                    Address address = sp.getValue(Address.class);
                    binding.nameAddressCheckout.setText(address.getName());
                    binding.cityNameAddress.setText(address.getCity());
                    binding.phoneNumberAddressCheckout.setText(address.getPhoneNo());
                    binding.addressCheckout.setText(address.getAddress());
                    completeAddress = address.getName() + "  " + address.getPhoneNo() + "  " + address.getAddress() + "  " + address.getName();
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void completeOrder(){
        String id = String.valueOf(System.currentTimeMillis());
        DifferentOrderForCheckout order = new DifferentOrderForCheckout(completeAddress, productList, String.valueOf(cartTotal), "placed", FirebaseAuth.getInstance().getUid(), id);
        FirebaseDatabase.getInstance().getReference()
                .child("UserOrders")
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child(id)
                .setValue(order).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.w(TAG_MAIN, "Order added to database.");
                FirebaseDatabase.getInstance().getReference()
                        .child("Orders")
                        .child(FirebaseAuth.getInstance().getUid())
                        .removeValue().
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.w(TAG_MAIN, "Cart emptied.");
                                Toast.makeText(Checkout.this, "Order Placed.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Checkout.this, CompleteOrder.class);
                                startActivity(intent);
                            }
                        });
            }
        });
    }
    //  <- payment code starts ->
    private void payWithGpay() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
        startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        if(appIsInstalled(Checkout.this, GOOGLE_PAY_PACKAGE_NAME)){

        } else {
            Toast.makeText(Checkout.this, "Please Install GPay.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ( data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == requestCode) && status.equals("success")){
            completeOrder();
            Toast.makeText(Checkout.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Checkout.this, "Transaction unsuccessful", Toast.LENGTH_SHORT).show();

        }
    }

    private static boolean appIsInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException exception) {
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }
    // Payment code ends.

}