package com.ippementa.ipem.view.canteen;

import com.ippementa.ipem.presenter.canteen.AvailableCanteensModel;
import com.ippementa.ipem.presenter.canteen.CanteenWithMapLocationModel;
import com.ippementa.ipem.view.error.ErrorView;

public interface AvailableCanteensView extends ErrorView {

    void showCanteens(AvailableCanteensModel canteens);

    void navigateToCanteenMenusPage(AvailableCanteensModel.Item canteen);

    void navigateToCanteenOnMapLocation(CanteenWithMapLocationModel canteen);

    void navigateBackToAvailableSchoolsPage();

    void showUnavailableCanteenError();

    void showUnavailableCanteensError();

}
