package inc.seven.smsfilter;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    ArrayList<SmsModel> smsModelArrayList = new ArrayList<>();
    RecyclerViewAdapter adapter;
    RecyclerView recyclerView;

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private static final String SMS_MESSAGE_KEY = "smsMessage";
    private static final String SMS_SENDER_KEY = "smsSender";
    private SMSReceiver smsReceiver;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SMS_MESSAGE_KEY)) {

            String smsMessage = intent.getStringExtra(SMS_MESSAGE_KEY);
            String smsSender = intent.getStringExtra(SMS_SENDER_KEY);

            showAlertDialog(smsMessage,smsSender);
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECEIVE_SMS},
                    SMS_PERMISSION_REQUEST_CODE);
        } else {
            registerSmsReceiver();
        }


        smsReceiver = new SMSReceiver();
        IntentFilter filter = new IntentFilter("inc.seven.MY_ACTION");
        registerReceiver(smsReceiver, filter);


        recyclerView = findViewById(R.id.recyclerContact);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerViewAdapter(this, smsModelArrayList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, register the SmsReceiver
                registerSmsReceiver();
            } else {
                Toast.makeText(this, "SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void registerSmsReceiver() {
        smsReceiver = new SMSReceiver();
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }


//    private void accessSmsMessages() {
//        ArrayList<String> smsList = smsReceiver.getSmsList();
//
//        Log.d("TAG", "accessSmsMessages11: "+smsList);
//    }

    private void showAlertDialog(String message,String smsSender) {
         alertDialog = new AlertDialog.Builder(this)
                .setTitle(smsSender)
                .setMessage(message)
                 .setCancelable(false)
//                .setPositiveButton("OK", null)

                 /////////////
                 .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                      /*
                         try {
                             Uri uri = Uri.parse("content://sms/inbox");

                             String selection = "_id = ?";
                             String[] selectionArgs = {String.valueOf(message)};

                             ContentResolver contentResolver =getContentResolver();
                             int rowsDeleted = contentResolver.delete(uri, selection, selectionArgs);

                             if (rowsDeleted > 0) {
                                 // SMS message deleted successfully
                                 Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                             } else {
                                 // Failed to delete the SMS message
                                 Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                             }
                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                         */

                         smsModelArrayList.add(new SmsModel(smsSender,message));
                         adapter.notifyItemInserted(smsModelArrayList.size()-1);
                         recyclerView.scrollToPosition(smsModelArrayList.size()-1);

//                         alertDialog.dismiss();

                     }
                 })

                .create();
        alertDialog.show();
    }

}