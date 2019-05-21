package serviceapplication.htlgkr.at.geburtstagsmanager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int RQ_Contacts = 2;
    private final String TAG = MainActivity.class.getSimpleName();
    private ListView listView;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (checkSelfPermission(Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS},RQ_Contacts);
        } else {
            getMessages();
        }

        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
    }

    public void onRequestPermissionsResult( int requestCode,
                                            String[] permissions,
                                            int[] grantResults ) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);
        if (requestCode==RQ_Contacts) {
            if (grantResults.length>0 && grantResults[0]!= PackageManager.PERMISSION_GRANTED) {
                //user does not allow
            } else {
                getMessages();
            }
        }
    }

    private void getMessages(){
        List<String> list = new LinkedList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        //(address, body, date_sent
        //content://sms/inbox
        String[] mProjection =
                {
                        ContactsContract.CommonDataKinds.Event.START_DATE
                };
        String where = ContactsContract.CommonDataKinds.Event.START_DATE + " != null";

        Cursor cursor = getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        if(null == cursor){
            Log.e(TAG, "Cursor is null");
        }else if(cursor.getCount() < 1){
            Log.i(TAG, "Nothing selected");
        }else{
            while(cursor.moveToNext()){
                String msg = cursor.getString(0);
                list.add(msg);

            }
            adapter.clear();
            adapter.addAll(list);
        }
    }
}
