package com.ippementa.ipem.view.dish;

import com.ippementa.ipem.presenter.dish.MenuDishesModel;
import com.ippementa.ipem.view.error.ErrorView;

public interface MenuDishesView extends ErrorView {

    void showMenus(MenuDishesModel dishes);

    void navigateBackToAvailableCanteenMenusPage();

    void showUnavailableDishesError();

    void showUnavailableDishError();

    void showDishWasMarkedFavoriteWithSuccessToast();

    void showDishWasUnmarkedFavoriteWithSuccessToast();

    void showErrorOccurredMarkingDishAsFavoriteToast();

    void showErrorOccurredUnmarkingDishAsFavoriteToast();

    void markDishAsFavorite(MenuDishesModel.Item dish);

    void unmarkDishAsFavorite(MenuDishesModel.Item dish);

    void showMarkingDishAsFavoriteRequiresInternetConnectionToast();

    void showUnmarkingDishAsFavoriteRequiresInternetConnectionToast();

}
