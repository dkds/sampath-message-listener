package com.neo.sampathmessagelistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.shared_pref_file), Context.MODE_PRIVATE);
        if (sharedPref.contains(context.getString(R.string.pref_phone_no))) {
            SmsMessage[] smsMessage = Telephony.Sms.Intents.getMessagesFromIntent(intent);
            String sender = smsMessage[0].getOriginatingAddress();
            String phoneNo = sharedPref.getString(context.getString(R.string.pref_phone_no), null);
            if (sender.equals(phoneNo)) {
                List<Transaction> transactions = extractTransactions(smsMessage[0].getMessageBody());
                StringJoiner builder = new StringJoiner("\n", "Received text from Sampath:", "");
                for (Transaction transaction : transactions) {
                    builder.add(transaction.toString());
                }
                Toast.makeText(context, builder.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private List<Transaction> extractTransactions(String messageBody) {
        String[] lines = messageBody.split("\\r?\\n");
        List<Transaction> transactions = new LinkedList<>();
        for (String line : lines) {
            String[] split = line.split(" ");
            Transaction transaction = new Transaction();
            transaction.account = split[2];
            transaction.type = split[3].startsWith("debit") ? Transaction.Type.DEBIT : Transaction.Type.CREDIT;
            transaction.amount = split[5];
            transactions.add(transaction);
        }
        return transactions;
    }

    private static class Transaction {
        String account;
        String amount;
        Type type;

        @Override
        public String toString() {
            return "[ACC:" + account + ", TYPE:" + type + ", AMOUNT:" + amount + "]";
        }

        enum Type {
            DEBIT, CREDIT
        }
    }
}
