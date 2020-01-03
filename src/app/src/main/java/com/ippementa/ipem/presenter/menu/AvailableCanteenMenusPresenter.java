package com.ippementa.ipem.presenter.menu;

import android.os.AsyncTask;

import com.ippementa.ipem.R;
import com.ippementa.ipem.model.menu.Menu;
import com.ippementa.ipem.model.menu.MenusRepository;
import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.presenter.canteen.AvailableCanteensModel;
import com.ippementa.ipem.util.CommunicationMediator;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;
import com.ippementa.ipem.view.menu.AvailableCanteenMenusActivity;
import com.ippementa.ipem.view.menu.AvailableCanteenMenusView;

import java.io.IOException;
import java.util.List;

public class AvailableCanteenMenusPresenter implements IPresenter {

    private AvailableCanteenMenusView view;

    private boolean isViewAvailableToInteract;

    public AvailableCanteenMenusPresenter(AvailableCanteenMenusView view){
        this.view = view;
        this.isViewAvailableToInteract = true;
    }

    public void requestMenus(AvailableCanteensModel.Item canteen){

        new RequestAvailableCanteenMenusTask().execute(canteen);

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

    private class RequestAvailableCanteenMenusTask extends AsyncTask<AvailableCanteensModel.Item, Integer, RequestAvailableCanteenMenusTask.BackgroundResult> {

        /**
         * Fetches available canteens in a thread that is separated from the main thread
         * If the object returned is null then
         */
        @Override
        protected BackgroundResult doInBackground(AvailableCanteensModel.Item... item) {

            MenusRepository repository
                    = Provider
                    .instance((AvailableCanteenMenusActivity)view)
                    .repositoryFactory((AvailableCanteenMenusActivity)view)
                    .createMenusRepository();

            BackgroundResult result = new BackgroundResult();

            try {

                result.menus = repository.menus(item[0].schoolId, item[0].id);

                if(result.menus.isEmpty()){
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

            AvailableCanteenMenusPresenter presenter = AvailableCanteenMenusPresenter.this;

            if(presenter.isViewAvailableToInteract){

                if(result.ioException != null){

                    if(!CommunicationMediator.hasInternetConnection((AvailableCanteenMenusActivity)presenter.view)){

                        presenter.view.showNoInternetConnectionError();

                    }else{

                        presenter.view.showServerNotAvailableError();

                    }
                }else if(result.requestException != null){

                    if(result.requestException.response.statusCode == 404){

                        presenter.view.showUnavailableMenusError();

                    }else{

                        presenter.view.showUnexepectedServerFailureError();

                    }

                }else{

                    AvailableCanteenMenusModel model = new AvailableCanteenMenusModel();

                    for(Menu menu : result.menus){

                        AvailableCanteenMenusModel.Item item = new AvailableCanteenMenusModel.Item();

                        item.id = menu.id;

                        String typeAsString;

                        AvailableCanteenMenusModel.Item.Type type;

                        switch (menu.type){
                            case LUNCH:
                                type = AvailableCanteenMenusModel.Item.Type.LUNCH;
                                typeAsString = ((AvailableCanteenMenusActivity)view).getString(R.string.menu_lunch_type);
                                break;
                            default:
                                type = AvailableCanteenMenusModel.Item.Type.DINNER;
                                typeAsString = ((AvailableCanteenMenusActivity)view).getString(R.string.menu_dinner_type);
                                break;
                        }

                        item.type = type;

                        item.typeAsString = typeAsString;

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

            public List<Menu> menus;

            public IOException ioException;

            public RequestException requestException;

        }
    }
}
