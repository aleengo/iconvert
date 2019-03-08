package com.kimboofactory.iconvert.ui.search.presentation;

import com.aleengo.peach.toolbox.commons.model.Response;
import com.aleengo.peach.toolbox.commons.model.Result;
import com.kimboofactory.iconvert.common.AbstractPresenter;
import com.kimboofactory.iconvert.domain.Repository;
import com.kimboofactory.iconvert.domain.model.CurrencyEntity;
import com.kimboofactory.iconvert.dto.CurrencyIHM;
import com.kimboofactory.iconvert.persistence.repository.CurrencyRepository;

import org.greenrobot.eventbus.EventBus;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import lombok.Getter;

/**
 * Created by CK_ALEENGO on 13/02/2019.
 * Copyright (c) 2019. All rights reserved.
 */
public class SearchPresenter extends AbstractPresenter<MvpSearchView>
        implements SearchContract.Presenter {

    private Repository repository;
    @Getter
    private List<CurrencyIHM> selectedItems = new LinkedList<>();

    @Inject
    public SearchPresenter(CurrencyRepository repository) {
        this.repository = repository;
    }

    public void start() {
        loadCurrencies();
    }

    @Override
    public void itemSelectedCheckbox(final CurrencyIHM item) {

        if (item.getCheckboxChecked()) {
            selectedItems.add(item);
        } else {
            selectedItems.remove(item);
        }

        final boolean show = selectedItems.size() > 0;
        getMvpView().toggleSnackbar(show, selectedItems);
    }

    @Override
    public void itemSelectedRadioButton(CurrencyIHM item) {
        getMvpView().showCurrency(item);
    }

    @Override
    public void filter(String query) {
        getMvpView().filter(query);
    }

    @Override
    public void loadCurrencies() {
       repository.getCurrencies((Response response) -> {

           final Result<List<CurrencyEntity>> result = new Result<>(null, null);

           if (response.getError() != null) {
               result.setError(response.getError());
               EventBus.getDefault().post(result);
               return;
           }
           final List<CurrencyEntity> data = (List<CurrencyEntity>) response.getValue();
           result.setValue(data);
           EventBus.getDefault().post(result);
       });
    }

    // free resources
    public void clear() {
        detach();
        selectedItems = null;
    }
}
