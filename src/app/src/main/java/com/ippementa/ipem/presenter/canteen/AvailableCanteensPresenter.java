package com.ippementa.ipem.presenter.canteen;

import android.os.AsyncTask;

import com.ippementa.ipem.model.canteen.Canteen;
import com.ippementa.ipem.model.canteen.CanteensRepository;
import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.util.CommunicationMediator;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.util.http.Client;
import com.ippementa.ipem.util.http.RequestException;
import com.ippementa.ipem.view.canteen.AvailableCanteensActivity;
import com.ippementa.ipem.view.canteen.AvailableCanteensView;

import java.io.IOException;
import java.util.List;

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

    public void requestCanteenToDisplayOnMap(long schoolId, long canteenId) {

        RequestCanteenTask task = new RequestCanteenTask();

        RequestCanteenTask.TaskRequest request = task.new TaskRequest();

        request.schoolId = schoolId;

        request.canteenId = canteenId;

        task.execute(request);

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
                    = Provider
                    .instance((AvailableCanteensActivity)view)
                    .repositoryFactory((AvailableCanteensActivity)view)
                    .createCanteensRepository();

            BackgroundResult result = new BackgroundResult();

            try {

                result.canteens = repository.canteens(schoolId[0]);

                if(result.canteens.isEmpty()){
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

                    for(Canteen canteen : result.canteens){

                        AvailableCanteensModel.Item item = new AvailableCanteensModel.Item();

                        item.id = canteen.id;

                        item.name = canteen.name;

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

            public List<Canteen> canteens;

            public IOException ioException;

            public RequestException requestException;

        }
    }

    private class RequestCanteenTask extends AsyncTask<RequestCanteenTask.TaskRequest, Integer, RequestCanteenTask.BackgroundResult> {

        /**
         * Fetches available canteens in a thread that is separated from the main thread
         * If the object returned is null then
         */
        @Override
        protected BackgroundResult doInBackground(RequestCanteenTask.TaskRequest... requests) {

            CanteensRepository repository
                    = Provider
                    .instance((AvailableCanteensActivity)view)
                    .repositoryFactory((AvailableCanteensActivity)view)
                    .createCanteensRepository();

            BackgroundResult result = new BackgroundResult();

            try {

                TaskRequest request = requests[0];

                System.out.println(request.schoolId);

                System.out.println(request.canteenId);

                result.canteen = repository.canteen(request.schoolId, request.canteenId);

                if(result.canteen == null){
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

                System.out.println(result.requestException.response.statusCode);

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

                        presenter.view.showUnavailableCanteenError();

                    }else{

                        presenter.view.showUnexepectedServerFailureError();

                    }

                }else{

                    CanteenWithMapLocationModel model = new CanteenWithMapLocationModel();

                    model.id = result.canteen.id;

                    model.name = result.canteen.name;

                    model.latitude = result.canteen.location.latitude;

                    model.longitude = result.canteen.location.longitude;

                    presenter.view.navigateToCanteenOnMapLocation(model);

                }

            }
        }

        private class TaskRequest {

            public long schoolId;

            public long canteenId;
        }

        /**
         * This class encapsulates the possible results of the doInBackground method
         * It is necessary to create this encapsulation as the method does not allow the throw of
         * checked exceptions, which may occur
         */
        private class BackgroundResult {

            public Canteen canteen;

            public IOException ioException;

            public RequestException requestException;

        }
    }
}
