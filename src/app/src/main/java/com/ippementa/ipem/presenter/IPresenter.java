package com.ippementa.ipem.presenter;

/**
 * An interface that defines functionalities that are applicable to every presenter
 */
public interface IPresenter {

    /**
     * Triggers that the view being controlled by the presenter is being destroyed
     */
    void onDestroy();

    /**
     * Triggers that the view being controlled by the presenter is resuming its lifecycle
     */
    void onResume();

    /**
     * Triggers that the view being controlled by the presented is pausing its lifecycle
     */
    void onPause();

}