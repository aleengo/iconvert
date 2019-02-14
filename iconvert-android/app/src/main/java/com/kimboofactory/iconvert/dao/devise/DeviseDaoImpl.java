package com.kimboofactory.iconvert.dao.devise;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kimboofactory.iconvert.data.IConvertContract;
import com.kimboofactory.iconvert.dao.GenericDao;
import com.kimboofactory.iconvert.persistence.model.CurrencyData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by khranyt on 31/03/2017.
 */

public class DeviseDaoImpl extends GenericDao
        implements DeviseDao {

    private static final String TAG = DeviseDaoImpl.class.getName();
    private static final int COL_ID_IDX = 0;
    private static final int COL_CODE_IDX = 1;
    private static final int COL_LIBELLE_IDX = 2;

    private Context context;

    public DeviseDaoImpl(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public CurrencyData createEntity(Cursor c) {
        return null;
                //new CurrencyData(c.getLong(COL_ID_IDX), c.getString(COL_CODE_IDX), c.getString(COL_LIBELLE_IDX));
    }

    @Override
    public Cursor list() {
        final SQLiteDatabase db = getDb();

        db.beginTransaction();
        final Cursor cursor = db.rawQuery("SELECT * FROM devise ORDER BY code", null);
        db.setTransactionSuccessful();
        db.endTransaction();

        return cursor;
    }

    @Override
    public long save(final List<CurrencyData> devises) {
        int count = 0;
        final SQLiteDatabase db = getDb();

        final String whereClause = IConvertContract.Devise.COL_CODE + "=?";

        db.beginTransaction();
        for (CurrencyData devise : devises) {
            // ContentValues is a name:value pair that makes it easy for us
            // to insert data in the database
            final ContentValues value = new ContentValues();
            value.put(IConvertContract.Devise.COL_CODE, devise.getCode());
            value.put(IConvertContract.Devise.COL_LIBELLE, devise.getLibelle());

            final String[] whereArgs = {devise.getCode()};
            // si la table T_DEVISE existe, on effectue une mise à jour
            // Si la ligne n'existe pas en base, on l'insère
            final int rowUpdated = db.update(IConvertContract.Devise.T_DEVISE, value, whereClause, whereArgs);
            if (rowUpdated == 0) {
                final long rowInserted = db.insert(IConvertContract.Devise.T_DEVISE, null, value);
                if (rowInserted != 0) {
                    count++;
                }
            }
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return count;
    }

    @Override
    public void save(JSONObject devisesJson) {
        // fill out the currencyFullName table
        final List<CurrencyData> devises = new ArrayList<>();

        if (devisesJson != null) {
            try {
                final Iterator<String> iterator = devisesJson.keys();
                while(iterator.hasNext()) {
                    final String deviseCode = iterator.next().trim();
                    final String deviseLibelle = devisesJson.getString(deviseCode).trim();

                    devises.add(null);
                }

                long inserted = save(devises);
                Log.d(TAG, inserted + " rows inserted.");

            }  catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public CurrencyData getByCode(final String code) {
        final Cursor c = super.getByCode(IConvertContract.Devise.T_DEVISE, IConvertContract.Devise.COL_CODE, code);
        return cursorToDevise(c);
    }

    private CurrencyData cursorToDevise(final Cursor c) {
        CurrencyData devise = null;
        if(c.moveToFirst()) {
            devise = null; //new CurrencyData(c.getLong(0), c.getString(1), c.getString(2));
        }
        return devise;
    }

}
