package com.kimboofactory.iconvert.ui.main;

import android.content.Intent;

import com.aleengo.peach.toolbox.commons.model.Result;
import com.kimboofactory.iconvert.GetFavoriteEvent;
import com.kimboofactory.iconvert.R;
import com.kimboofactory.iconvert.domain.model.CurrencyEntity;
import com.kimboofactory.iconvert.dto.CurrencyIHM;
import com.kimboofactory.iconvert.ui.search.SearchCurrencyActivity;
import com.kimboofactory.iconvert.util.ComputeTask;
import com.kimboofactory.iconvert.util.Helper;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

/**
 * Created by CK_ALEENGO on 11/02/2019.
 * Copyright (c) 2019. All rights reserved.
 */
public class MainView implements FavoriteContract.View {

    @Getter
    private MainActivity activity;

    public MainView() {
    }

    @Override
    public void attachUi(Object activity) {
        this.activity = (MainActivity) activity;
    }

    @Override
    public CurrencyIHM getDefaultCurrency() {
        final CurrencyEntity entity =
                new CurrencyEntity(Helper.USD_CODE, Helper.USD_LIBELLE, Helper.DEFAULT_AMOUNT);

        final CurrencyIHM source  = new CurrencyIHM();
        source.setEntity(entity);
        source.setAmount(Helper.DEFAULT_AMOUNT);
        return source;
    }

    @Override
    public void showSearchActivity(int requestCode) {
        final Intent intent = new Intent(this.activity, SearchCurrencyActivity.class);
        intent.putExtra(MainActivity.REQUEST_CODE, requestCode);
        this.activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void updateFavoritesList(List<CurrencyIHM> currencies) {
        if (currencies.size() > 0) {
            final List<CurrencyIHM> newCurrencies = currencies.stream()
                    .map(dst -> {
                        final ComputeTask task = new ComputeTask(activity.getCurrencySource(), dst);
                        task.execute();
                        return task.getRealCurrency();
                    })
                    .collect(Collectors.toList());
            // update the adapter
            updateAdapterItems(newCurrencies);
        } else {
            updateAdapterItems(currencies);
        }
    }

    @Override
    public void updateSourceCurrency(CurrencyIHM newSource) {
        final String codeLibelle = activity.getResources().getString(R.string.label_code_libelle,
                newSource.getEntity().getCode(), newSource.getEntity().getLibelle());

        newSource.setAmount(Helper.DEFAULT_AMOUNT);

        activity.getTextViewCodeSrc().setText(codeLibelle);
        activity.setCurrencySource(newSource);

        // update the favorite according to the new currency source
        updateFavoritesList(getAdapterItems());
    }

    @Override
    public CurrencyIHM removeFavoriteAt(int position) {
        final CurrencyIHM itemDeleted = getAdapterItems().remove(position);
        activity.getFavoritesAdapter().notifyDataSetChanged();
        return itemDeleted;
    }

    @Override
    public List<CurrencyIHM> getAdapterItems() {
        return activity.getFavoritesAdapter().getItems();
    }

    @Override
    public void clearAdapterItems() {
        activity.getFavoritesAdapter().clear();
    }

    @Override
    public void updateAdapterItems(List<CurrencyIHM> newFavoriteItems) {
        clearAdapterItems();
        activity.getFavoritesAdapter().updateItems(newFavoriteItems);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(Result event) {
        if (event.getError() != null) {
            // show an error message
            return;
        }

        final CurrencyEntity entity = (CurrencyEntity) event.getValue();
        final CurrencyIHM ihm = new CurrencyIHM(entity);
        ihm.setAmount(Helper.DEFAULT_AMOUNT);

        final String codeLibelle = activity.getResources().getString(R.string.label_code_libelle,
                ihm.getEntity().getCode(), ihm.getEntity().getLibelle());

        activity.getTextViewCodeSrc().setText(codeLibelle);
        activity.setCurrencySource(ihm);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GetFavoriteEvent event) {

        if (event.getSource() != null && event.getCurrencies().size() > 0) {
            // update source
            final CurrencyIHM ihm = event.getSource();
            ihm.setAmount(Helper.DEFAULT_AMOUNT);

            final String codeLibelle = activity.getResources().getString(R.string.label_code_libelle,
                    ihm.getEntity().getCode(), ihm.getEntity().getLibelle());

            activity.getTextViewCodeSrc().setText(codeLibelle);
            activity.setCurrencySource(ihm);
            activity.getAmountET().setHint(Helper.DEFAULT_AMOUNT);

            // update favorite list
            final List<CurrencyIHM> newItems = event.getCurrencies()
                    .stream()
                    .map(currencyIHM -> {
                        final ComputeTask task = new ComputeTask(ihm, currencyIHM);
                        task.execute();
                        return task.getRealCurrency();
                    }).collect(Collectors.toList());
            updateAdapterItems(newItems);
        }
    }
}
