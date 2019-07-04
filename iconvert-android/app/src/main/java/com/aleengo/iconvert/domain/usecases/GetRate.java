package com.aleengo.iconvert.domain.usecases;

import com.aleengo.peach.toolbox.commons.model.RawJSON;
import com.aleengo.peach.toolbox.commons.model.Response;
import com.aleengo.peach.toolbox.commons.model.Result;
import com.aleengo.peach.toolbox.commons.strategy.RawJSONDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.aleengo.iconvert.domain.Repository;
import com.aleengo.iconvert.domain.UseCase;
import com.aleengo.iconvert.domain.common.QueryValue;

import java.io.StringReader;

import javax.inject.Inject;

/**
 * Created by CK_ALEENGO on 11/02/2019.
 * Copyright (c) 2019. All rights reserved.
 */
public class GetRate extends UseCase<QueryValue> {

    @Inject
    public GetRate(Repository repository) {
        super(repository);
    }

    private void onDataLoaded(Response response) {

        if (response.getError() != null) {
            getUseCaseCallback().onResult(new Result<String>(null, response.getError()));
            return;
        }

        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(RawJSON.class, new RawJSONDeserializer())
                .create();

        final String json = (String) response.getValue();
        final RawJSON rawData = gson.fromJson(new JsonReader(new StringReader(json)),
                RawJSON.class);

        /*final List<CurrencyIHM> list = rawData.getItems().entrySet()
                .stream()
                .map(entrySet -> new CurrencyIHM(entrySet.getKey(), entrySet.getAmount(), false))
                .collect(Collectors.toList());*/

        getUseCaseCallback().onResult(new Result(null, null));
    }

    @Override
    protected void executeUseCase() {
        final String query = (String) getQueryValue().getValue();
        getRepository().search(query, this::onDataLoaded);
    }
}
