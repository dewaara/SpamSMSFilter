package inc.seven.smsfilter;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.text.nlclassifier.NLClassifier;

public class SMSReceiver extends BroadcastReceiver {

    private final String MODEL_PATH = "model_spam.tflite";
    private NLClassifier classifier;


    ArrayList<String> smsList = new ArrayList<>();


    Context context;


    public SMSReceiver(Context context) {
        this.context = context;
    }

    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        try {
            classifier = NLClassifier.createFromFile(context.getApplicationContext(), MODEL_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Object[] smsObjects = (Object[]) intent.getExtras().get("pdus");
        Log.d(TAG, "onReceive: "+intent.getAction());
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

            if (smsMessages != null && smsMessages.length > 0) {

                for (SmsMessage smsMessage : smsMessages) {
                    String sender = smsMessage.getDisplayOriginatingAddress();
                    String message = smsMessage.getDisplayMessageBody();

                    Log.d("TAG1", "Received SMS:");
                    Log.d("TAG2", "Sender: " + sender);
                    Log.d("TAG3", "Message: " + message);


                    List<Category> apiResults = classifier.classify(String.valueOf(message));
                    float score = apiResults.get(1).getScore();
                    Log.d(TAG, "runClassification: " + score);
                    if (score > 0.6f) {

                        Intent mainActivityIntent = new Intent(context, MainActivity.class);
                        mainActivityIntent.putExtra("smsMessage", message);
                        mainActivityIntent.putExtra("smsSender",sender);
                        mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(mainActivityIntent);

                        Toast.makeText(context.getApplicationContext(), "spam: " + score, Toast.LENGTH_SHORT).show();

                    } else {
                        Log.d(TAG, "onReceive:48 "+"not spam");
                        Toast.makeText(context.getApplicationContext(), "ham: " + score, Toast.LENGTH_SHORT).show();
                    }


                }
            }
        }

    }


//
//    public void showNotification(Context context) {
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), Utils.CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("sender sms")
//                .setContentText("sms")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setStyle(new NotificationCompat.BigTextStyle()
//                        .bigText("hgdbm hbxbhk dgksfdh kxchjvk sdgs xvxcv sdgsg" +
//                                "psp hgdbm hbxbhk dgksfdh kxchjvk sdgs xvxcv sdgsg psp" +
//                                "hgdbm hbxbhk dgksfdh kxchjvk sdgs xvxcv sdgsg psp"));
//        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context.getApplicationContext());
//        if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        notificationManagerCompat.notify(Utils.NOTI_ID, builder.build());
//    }



    public ArrayList<String> getSmsList() {
        Log.d("TAG5", "getSmsList: "+smsList);
        return smsList;

    }
}