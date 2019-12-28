package com.ippementa.ipem.presenter.dish;

import android.os.AsyncTask;

import com.ippementa.ipem.R;
import com.ippementa.ipem.model.dish.Dish;
import com.ippementa.ipem.model.dish.DishRepository;
import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.presenter.menu.AvailableCanteenMenusModel;
import com.ippementa.ipem.util.CommunicationMediator;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;
import com.ippementa.ipem.view.dish.MenuDishesActivity;
import com.ippementa.ipem.view.dish.MenuDishesView;

import java.io.IOException;
import java.util.List;

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

            DishRepository repository
                    = Provider
                    .instance((MenuDishesActivity)view)
                    .repositoryFactory((MenuDishesActivity)view)
                    .createDishRepository();

            BackgroundResult result = new BackgroundResult();

            try {

                result.dishes = repository.dishes(item[0].schoolId, item[0].canteenId, item[0].id);

                if(result.dishes.isEmpty()){
                    // Need to simulate a RequestException as this was a SQL Query that found no rows

                    Client.Response response = new Client.Response();

                    response.statusCode = 404;

                    RequestException exception = new RequestException(response);

                    result.requestException = exception;
                }

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

                    for(Dish dish : result.dishes){

                        MenuDishesModel.Item item = new MenuDishesModel.Item();

                        item.id = dish.id;

                        String typeAsString;
                        MenuDishesModel.Item.Type type;

                        switch(dish.type){
                            case MEAT:
                                type = MenuDishesModel.Item.Type.MEAT;
                                typeAsString = ((MenuDishesActivity)view).getString(R.string.dish_meat_type);
                                break;
                            case FISH:
                                type = MenuDishesModel.Item.Type.FISH;
                                typeAsString = ((MenuDishesActivity)view).getString(R.string.dish_fish_type);
                                break;
                            case VEGETARIAN:
                                type = MenuDishesModel.Item.Type.VEGETARIAN;
                                typeAsString = ((MenuDishesActivity)view).getString(R.string.dish_vegetarian_type);
                                break;
                            default:
                                type = MenuDishesModel.Item.Type.DIET;
                                typeAsString = ((MenuDishesActivity)view).getString(R.string.dish_diet_type);
                                break;
                        }

                        item.type = type;

                        item.typeAsString = typeAsString;

                        item.description = dish.description;

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

            public List<Dish> dishes;

            public IOException ioException;

            public RequestException requestException;

        }
    }
}
