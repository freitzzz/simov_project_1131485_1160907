package com.ippementa.ipem.presenter.settings;

import android.content.Context;
import android.os.AsyncTask;

import com.ippementa.ipem.model.RepositoryFactory;
import com.ippementa.ipem.model.canteen.Canteen;
import com.ippementa.ipem.model.canteen.RoomCanteensRepositoryImpl;
import com.ippementa.ipem.model.dish.Dish;
import com.ippementa.ipem.model.dish.RoomDishRepositoryImpl;
import com.ippementa.ipem.model.menu.Menu;
import com.ippementa.ipem.model.menu.RoomMenusRepositoryImpl;
import com.ippementa.ipem.model.school.RoomSchoolsRepositoryImpl;
import com.ippementa.ipem.model.school.School;
import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.view.settings.SettingsActivity;
import com.ippementa.ipem.view.settings.SettingsView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.room.RoomDatabase;

public class SettingsPresenter implements IPresenter {

    private SettingsView view;

    private boolean isViewAvailableToInteract;

    public SettingsPresenter(SettingsView view){

        this.view = view;

        this.isViewAvailableToInteract = true;

    }

    public void startOfflineDataDownload(){

        this.view.showOfflineDataDownloadStartSnackbar();

        this.view.disableOfflineModeSwitchInteraction();

        new DownloadOfflineDataAsyncTask().execute();

    }

    public void setNotUseOfflineData() {

        Provider.instance((SettingsActivity)view).settings().activateOnlineMode((SettingsActivity)view);

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

    private class DownloadOfflineDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                // view.getContext() instead of cast...

                Context ctx = (SettingsActivity)view;

                // repository wont change so we can declare it to a variable

                RepositoryFactory repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

                System.out.println(repositoryFactory);

                List<School> schools
                        = repositoryFactory
                        .createSchoolsRepository()
                        .availableSchools();

                List<Canteen> canteens = new ArrayList<>();

                List<Menu> menus = new ArrayList<>();

                List<Dish> dishes = new ArrayList<>();

                for(School school : schools){
                    try {
                        List<Canteen> _canteens
                                = repositoryFactory
                                .createCanteensRepository()
                                .canteens(school.id);

                        canteens.addAll(_canteens);

                    }catch (Exception e){

                        e.printStackTrace();

                    }

                }

                for(Canteen canteen : canteens){

                    System.out.println(canteen.schoolId);
                    try {
                        List<Menu> _menus
                                = repositoryFactory
                                .createMenusRepository()
                                .menus(canteen.schoolId, canteen.id);
                        menus.addAll(_menus);
                    }catch (Exception e){

                        e.printStackTrace();

                    }


                }

                for(Menu menu : menus){

                    long schoolId = 0;

                    for(Canteen canteen : canteens){

                        if(menu.canteenId == canteen.id){
                            schoolId = canteen.schoolId;
                            break;
                        }

                    }

                    try{
                        List<Dish> _dishes
                                = repositoryFactory
                                .createDishRepository()
                                .dishes(schoolId, menu.canteenId, menu.id);
                        dishes.addAll(_dishes);
                    }catch (Exception e){

                        e.printStackTrace();

                    }


                }

                Provider.instance(ctx).settings().activateOfflineMode(ctx);

                ((RoomDatabase)Provider.instance(ctx).repositoryFactory(ctx)).clearAllTables();

                // as offline mode was activated we need a new instance of repository factory

                repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

                ((RoomSchoolsRepositoryImpl)repositoryFactory.createSchoolsRepository()).insertAll(schools.toArray(new School[]{}));

                ((RoomCanteensRepositoryImpl)repositoryFactory.createCanteensRepository()).insertAll(canteens.toArray(new Canteen[]{}));

                ((RoomMenusRepositoryImpl)repositoryFactory.createMenusRepository()).insertAll(menus.toArray(new Menu[]{}));

                ((RoomDishRepositoryImpl)repositoryFactory.createDishRepository()).insertAll(dishes.toArray(new Dish[]{}));

            }catch(IOException e){
                e.printStackTrace();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if(result){

                view.showOfflineDataDownloadFinishSnackbar();

            }else{

                view.showUnexepectedServerFailureError();

            }

            view.enableOfflineModeSwitchInteraction();

        }
    }
}
