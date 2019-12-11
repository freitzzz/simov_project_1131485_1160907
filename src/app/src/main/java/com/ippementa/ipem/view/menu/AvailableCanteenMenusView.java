package com.ippementa.ipem.view.menu;

import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusModel;
import com.ippementa.ipem.view.error.ErrorView;

public interface AvailableCanteenMenusView extends ErrorView {

    void showMenus(AvailableCanteenMenusModel menus);

    void navigateToMenuDishesPage(AvailableCanteenMenusModel.Item menu);

    void navigateBackToAvailableCanteensPage();

    void showUnavailableMenusError();

}
