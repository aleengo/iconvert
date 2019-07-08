package com.aleengo.iconvert.di.search;

import com.aleengo.iconvert.application.Constant;
import com.aleengo.iconvert.ui.search.ActivitySearchCurrency;
import com.aleengo.iconvert.ui.search.SearchCurrencyAdapter;
import com.aleengo.iconvert.ui.search.SearchTemplate;
import com.aleengo.peank.core.annotations.dagger.Activity;

import java.util.LinkedList;

import dagger.Module;
import dagger.Provides;

/**
 * Created by CK_ALEENGO on 02/03/2019.
 * Copyright (c) 2019. All rights reserved.
 */
@Module
public class SearchModule {

    private final ActivitySearchCurrency activity;

    public SearchModule(ActivitySearchCurrency activity) {
        this.activity = activity;
    }

    @Activity
    @Provides
    public ActivitySearchCurrency activity() {
        return activity;
    }

    @Activity
    @Provides
    public static SearchTemplate view(ActivitySearchCurrency activity) {
        return new SearchTemplate(activity);
    }

    @Activity
    @Provides
    public static Integer requestCode(ActivitySearchCurrency activity) {
        return activity.getIntent()
                .getIntExtra(Constant.RC_SEARCH, Constant.NO_EXTRA);
    }

    @Activity
    @Provides
    public static SearchCurrencyAdapter adapter(ActivitySearchCurrency activity, Integer requestCode) {
        return new SearchCurrencyAdapter(activity, new LinkedList<>(), requestCode);
    }
}
