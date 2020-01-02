package com.ippementa.ipem.presenter.payment;

import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.view.payment.PurchaseDishView;

public class PurchaseDishPresenter implements IPresenter {

    private PurchaseDishView view;

    private boolean isViewAvaliableToUse;

    public PurchaseDishPresenter(PurchaseDishView view) {
        this.view = view;
        this.isViewAvaliableToUse = true;
    }

    @Override
    public void onDestroy() {
        this.isViewAvaliableToUse = false;
    }

    @Override
    public void onResume() {
        this.isViewAvaliableToUse = true;
    }

    @Override
    public void onPause() {
        this.isViewAvaliableToUse = false;
    }
}
