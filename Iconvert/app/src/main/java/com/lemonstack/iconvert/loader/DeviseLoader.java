package com.lemonstack.iconvert.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.util.Log;

import com.lemonstack.iconvert.dao.devise.DeviseDao;
import com.lemonstack.iconvert.dao.devise.DeviseDaoImpl;
import com.lemonstack.iconvert.model.Devise;

import java.util.List;

/**
 * Created by khranyt on 31/03/2017.
 */

public class DeviseLoader extends CursorLoader {

    private static final String TAG = DeviseLoader.class.getName();

    private Context context;

    public DeviseLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public Cursor loadInBackground() {
        final DeviseDao deviseDao = new DeviseDaoImpl(context);
        Log.d(TAG, "loadInBackground() is called");
        return deviseDao.list();
    }
}
