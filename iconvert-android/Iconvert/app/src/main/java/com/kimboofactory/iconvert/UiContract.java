package com.kimboofactory.iconvert;

import android.support.annotation.LayoutRes;
import android.view.View;

/**
 * 25/01/2019.
 */
public interface UiContract {

    // ################ ABSTRACT METHODS ########################
    String getClassName();

    @LayoutRes int getLayoutResId();
    /**
     * Called when the Ui is created. This method is
     * used to setup your views attributes, connect views between them
     * and attach listeners to views that need it.
     */
    void initialize(View view);
    // ###########################################################
}
