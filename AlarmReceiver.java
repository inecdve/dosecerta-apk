package com.dosecerta.app;
import android.app.*;
import android.content.*;
import android.os.Build;
import android.widget.Toast;
import java.util.*;
public class AlarmReceiver extends BroadcastReceiver {
@Override public void onReceive(Context c, Intent i) {
SharedPreferences p = c.getSharedPreferences("dosecerta_prefs", 0);
NotificationManager m = (NotificationManager) c.getSystemService("notification");
if ("CONFIRM".equals(i.getAction())) {
p.edit().putString("cf", p.getString("cf", "") + ";" + i.getStringExtra("med_id") + "|" + i.getStringExtra("time_str")).apply();
if (m != null) m.cancel(i.getIntExtra("nid", 0));
Toast.makeText(c, "Dose confirmada!", 0).show();
return;
}
Set<String> a = p.getStringSet("active_keys", null);
String k = i.getStringExtra("alarm_key");
if (a != null && (k == null || !a.contains(k))) return;
int n = (int)(System.currentTimeMillis() & 0xfffffff), fl = 201326592;
PendingIntent pi = PendingIntent.getActivity(c, 0, new Intent(c, MainActivity.class), fl);
Intent ci = new Intent(c, AlarmReceiver.class).setAction("CONFIRM").putExtra("med_id", i.getStringExtra("med_id")).putExtra("time_str", i.getStringExtra("time_str")).putExtra("nid", n);
Notification.Builder b = Build.VERSION.SDK_INT >= 26 ? new Notification.Builder(c, "dosecerta_alarms") : new Notification.Builder(c);
b.setSmallIcon(17301550).setContentTitle(i.getStringExtra("title")).setContentText(i.getStringExtra("body")).setAutoCancel(true).setContentIntent(pi);
b.addAction(0, "Confirmar que tomei", PendingIntent.getBroadcast(c, n, ci, fl));
if (m != null) m.notify(n, b.build());
}
}
