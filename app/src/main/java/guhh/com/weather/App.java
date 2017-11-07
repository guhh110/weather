package guhh.com.weather;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * Created by ggg on 2017/11/7.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        OkGo.getInstance().init(this);
    }
}
