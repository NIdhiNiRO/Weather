package com.virtusa.weather.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.virtusa.weather.R;

import de.greenrobot.event.EventBus;
/**
 * Created by nidhiparekh on 10/11/17.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
    }
    //register sticky on > onResume()
    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().registerSticky(this);
    }

    //unregister sticky on > onPause()
    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void promptMessage(String message){
        Toast.makeText(BaseActivity.this,message,Toast.LENGTH_LONG).show();
    }

    //Receiver of network connection thread. reflect to all the child activity
    public void onEventMainThread(InternetConnectionBroadCast.GetConnection getConnection) {
        EventBus.getDefault().removeStickyEvent(getConnection);

        if (!getConnection.isConnected) {
            promptMessage("Connection unavailable");
        }
        else{
            /**some of connection interface between code, when you are available with internet and want to do retrieve
            results and update your UI with callback **/
        }
    }

}
