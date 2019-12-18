package com.ippementa.ipem.presenter.dish;

import android.os.AsyncTask;

import com.ippementa.ipem.model.dish.IPEDDishRepositoryImpl;
import com.ippementa.ipem.model.dish.MenuDishesResponsePayload;
import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusModel;
import com.ippementa.ipem.util.CommunicationMediator;
import com.ippementa.ipem.util.http.RequestException;
import com.ippementa.ipem.view.dish.MenuDishesActivity;
import com.ippementa.ipem.view.dish.MenuDishesView;

import java.io.IOException;

public class MenuDishesPresenter implements IPresenter {

    private MenuDishesView view;

    private boolean isViewAvailableToInteract;

    public MenuDishesPresenter(MenuDishesView view){
        this.view = view;
        this.isViewAvailableToInteract = true;
    }

    public void requestDishes(AvailableCanteenMenusModel.Item menu){

        new RequestMenuDishesTask().execute(menu);

    }

    @Override
    public void onDestroy() {
        view = null;
        this.isViewAvailableToInteract = false;
    }

    @Override
    public void onResume() {
        isViewAvailableToInteract = true;
    }

    @Override
    public void onPause() {
        isViewAvailableToInteract = false;
    }

    private class RequestMenuDishesTask extends AsyncTask<AvailableCanteenMenusModel.Item, Integer, RequestMenuDishesTask.BackgroundResult> {

        @Override
        protected BackgroundResult doInBackground(AvailableCanteenMenusModel.Item... item) {

            IPEDDishRepositoryImpl repository = new IPEDDishRepositoryImpl();

            BackgroundResult result = new BackgroundResult();

            try {

                result.payload = repository.dishes(item[0].schoolId, item[0].canteenId, item[0].id);

            } catch (IOException e) {

                result.ioException = e;

                e.printStackTrace();
            } catch (RequestException requestFailure){

                result.requestException = requestFailure;

                requestFailure.printStackTrace();

            }

            return result;
        }

        @Override
        protected void onPostExecute(BackgroundResult result) {
            super.onPostExecute(result);

            MenuDishesPresenter presenter = MenuDishesPresenter.this;

            if(presenter.isViewAvailableToInteract){

                if(result.ioException != null){

                    if(!CommunicationMediator.hasInternetConnection((MenuDishesActivity)presenter.view)){

                        presenter.view.showNoInternetConnectionError();

                    }else{

                        presenter.view.showServerNotAvailableError();

                    }
                }else if(result.requestException != null){

                    if(result.requestException.response.statusCode == 404){

                        presenter.view.showUnavailableDishesError();

                    }else{

                        presenter.view.showUnexepectedServerFailureError();

                    }

                }else{

                    MenuDishesModel model = new MenuDishesModel();

                    for(MenuDishesResponsePayload.Item payloadItem : result.payload){

                        MenuDishesModel.Item item = new MenuDishesModel.Item();

                        item.id = payloadItem.id;

                        item.type = payloadItem.type;

                        item.description = payloadItem.description;

                        model.add(item);

                    }

                    presenter.view.showMenus(model);

                }

            }
        }

        /**
         * This class encapsulates the possible results of the doInBackground method
         * It is necessary to create this encapsulation as the method does not allow the throw of
         * checked exceptions, which may occur
         */
        private class BackgroundResult {

            public MenuDishesResponsePayload payload;

            public IOException ioException;

            public RequestException requestException;

        }
    }
}
