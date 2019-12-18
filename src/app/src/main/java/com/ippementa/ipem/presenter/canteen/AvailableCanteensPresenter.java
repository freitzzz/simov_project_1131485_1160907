package com.ippementa.ipem.presenter.canteen;

import android.os.AsyncTask;

import com.ippementa.ipem.model.canteen.AvailableCanteensResponsePayload;
import com.ippementa.ipem.model.canteen.CanteensRepository;
import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.util.CommunicationMediator;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.util.http.RequestException;
import com.ippementa.ipem.view.canteen.AvailableCanteensActivity;
import com.ippementa.ipem.view.canteen.AvailableCanteensView;

import java.io.IOException;

public class AvailableCanteensPresenter implements IPresenter {

    private AvailableCanteensView view;

    private boolean isViewAvailableToInteract;

    public AvailableCanteensPresenter(AvailableCanteensView view){
        this.view = view;
        this.isViewAvailableToInteract = true;
    }

    public void requestCanteens(long schoolId){

        new RequestAvailableCanteensTask().execute(schoolId);

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

    private class RequestAvailableCanteensTask extends AsyncTask<Long, Integer, RequestAvailableCanteensTask.BackgroundResult> {

        /**
         * Fetches available canteens in a thread that is separated from the main thread
         * If the object returned is null then
         */
        @Override
        protected BackgroundResult doInBackground(Long... schoolId) {

            CanteensRepository repository
                    = Provider.instance().repositoryFactory().createCanteensRepository();

            BackgroundResult result = new BackgroundResult();

            try {

                result.payload = repository.availableCanteens(schoolId[0]);

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

            AvailableCanteensPresenter presenter = AvailableCanteensPresenter.this;

            if(presenter.isViewAvailableToInteract){

                if(result.ioException != null){

                    if(!CommunicationMediator.hasInternetConnection((AvailableCanteensActivity)presenter.view)){

                        presenter.view.showNoInternetConnectionError();

                    }else{

                        presenter.view.showServerNotAvailableError();

                    }
                }else if(result.requestException != null){

                    if(result.requestException.response.statusCode == 404){

                        presenter.view.showUnavailableCanteensError();

                    }else{

                        presenter.view.showUnexepectedServerFailureError();

                    }

                }else{

                    AvailableCanteensModel model = new AvailableCanteensModel();

                    for(AvailableCanteensResponsePayload.Item payloadItem : result.payload){

                        AvailableCanteensModel.Item item = new AvailableCanteensModel.Item();

                        item.id = payloadItem.id;

                        item.name = payloadItem.name;

                        model.add(item);

                    }

                    presenter.view.showCanteens(model);

                }

            }
        }

        /**
         * This class encapsulates the possible results of the doInBackground method
         * It is necessary to create this encapsulation as the method does not allow the throw of
         * checked exceptions, which may occur
         */
        private class BackgroundResult {

            public AvailableCanteensResponsePayload payload;

            public IOException ioException;

            public RequestException requestException;

        }
    }
}
