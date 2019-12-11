package com.ippementa.ipem.view.dish;

import com.ippementa.ipem.presenter.dish.MenuDishesModel;
import com.ippementa.ipem.view.error.ErrorView;

public interface MenuDishesView extends ErrorView {

    void showMenus(MenuDishesModel dishes);

    void navigateBackToAvailableCanteenMenusPage();

    void showUnavailableDishesError();

}
