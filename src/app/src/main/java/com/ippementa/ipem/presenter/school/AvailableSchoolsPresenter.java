package com.ippementa.ipem.presenter.school;

import android.os.AsyncTask;

import com.ippementa.ipem.model.school.AvailableSchoolsResponsePayload;
import com.ippementa.ipem.model.school.SchoolsRepository;
import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.util.CommunicationMediator;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.util.http.RequestException;
import com.ippementa.ipem.view.school.AvailableSchoolsActivity;
import com.ippementa.ipem.view.school.AvailableSchoolsView;

import java.io.IOException;

public class AvailableSchoolsPresenter implements IPresenter {

    private AvailableSchoolsView view;

    private boolean isViewAvailableToInteract;

    public AvailableSchoolsPresenter(AvailableSchoolsView view){
        this.view = view;
        this.isViewAvailableToInteract = true;
    }

    public void requestSchools(){

        new RequestAvailableSchoolsTask().execute();

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

    private class RequestAvailableSchoolsTask extends AsyncTask<Void, Integer, RequestAvailableSchoolsTask.BackgroundResult> {

        /**
         * Fetches available schools in a thread that is separated from the main thread
         * If the object returned is null then
         */
        @Override
        protected BackgroundResult doInBackground(Void... voids) {

            SchoolsRepository repository
                    = Provider.instance().repositoryFactory().createSchoolsRepository();

            BackgroundResult result = new BackgroundResult();

            try {

                result.payload = repository.availableSchools();

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

            AvailableSchoolsPresenter presenter = AvailableSchoolsPresenter.this;

            if(presenter.isViewAvailableToInteract){

                if(result.ioException != null){

                    if(!CommunicationMediator.hasInternetConnection((AvailableSchoolsActivity)presenter.view)){

                        presenter.view.showNoInternetConnectionError();

                    }else{

                        presenter.view.showServerNotAvailableError();

                    }
                }else if(result.requestException != null){

                    if(result.requestException.response.statusCode == 404){

                        presenter.view.showUnavailableSchoolsError();

                    }else{

                        presenter.view.showUnexepectedServerFailureError();

                    }

                }else{

                    AvailableSchoolsModel model = new AvailableSchoolsModel();

                    for(AvailableSchoolsResponsePayload.Item payloadItem : result.payload){

                        AvailableSchoolsModel.Item item = new AvailableSchoolsModel.Item();

                        item.id = payloadItem.id;

                        item.acronym = payloadItem.acronym;

                        item.name = payloadItem.name;

                        model.add(item);

                    }

                    presenter.view.showSchools(model);

                }

            }
        }

        /**
         * This class encapsulates the possible results of the doInBackground method
         * It is necessary to create this encapsulation as the method does not allow the throw of
         * checked exceptions, which may occur
         */
        private class BackgroundResult {

            public AvailableSchoolsResponsePayload payload;

            public IOException ioException;

            public RequestException requestException;

        }
    }
}
