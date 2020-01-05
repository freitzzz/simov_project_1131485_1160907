package com.ippementa.ipem.presenter.settings;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ippementa.ipem.model.RepositoryFactory;
import com.ippementa.ipem.model.canteen.Canteen;
import com.ippementa.ipem.model.canteen.RoomCanteensRepositoryImpl;
import com.ippementa.ipem.model.canteen.UserNearbyCanteensGeofencingBroadcastReceiver;
import com.ippementa.ipem.model.dish.Dish;
import com.ippementa.ipem.model.dish.RoomDishRepositoryImpl;
import com.ippementa.ipem.model.menu.Menu;
import com.ippementa.ipem.model.menu.RoomMenusRepositoryImpl;
import com.ippementa.ipem.model.pushnotifications.PushNotificationsRepository;
import com.ippementa.ipem.model.school.RoomSchoolsRepositoryImpl;
import com.ippementa.ipem.model.school.School;
import com.ippementa.ipem.presenter.IPresenter;
import com.ippementa.ipem.util.CommunicationMediator;
import com.ippementa.ipem.util.Provider;
import com.ippementa.ipem.util.http.RequestException;
import com.ippementa.ipem.view.settings.SettingsActivity;
import com.ippementa.ipem.view.settings.SettingsView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class SettingsPresenter implements IPresenter, Parcelable {

    private SettingsView view;

    private boolean isViewAvailableToInteract;

    public SettingsPresenter(SettingsView view) {

        this.view = view;

        this.isViewAvailableToInteract = true;

    }

    protected SettingsPresenter(Parcel in) {
        isViewAvailableToInteract = in.readByte() != 0;
    }

    public static final Creator<SettingsPresenter> CREATOR = new Creator<SettingsPresenter>() {
        @Override
        public SettingsPresenter createFromParcel(Parcel in) {
            return new SettingsPresenter(in);
        }

        @Override
        public SettingsPresenter[] newArray(int size) {
            return new SettingsPresenter[size];
        }
    };

    public void startOfflineDataDownload() {

        this.view.showOfflineDataDownloadStartSnackbar();

        this.view.disableOfflineModeSwitchInteraction();

        new DownloadOfflineDataAsyncTask().execute();

    }

    public void registerFavoriteDishesPushNotificationsReceive() {

        new RegisterFavoriteDishesPushNotificationsReceiveAsyncTask().execute();

    }

    public void unregisterFavoriteDishesPushNotificationsReceive() {

        new UnregisterFavoriteDishesPushNotificationsReceiveAsyncTask().execute();

    }

    public void registerNearbyCanteensPushNotificationsReceive() {

        view.showRegisteringNearbyCanteensPushNotificationsReceiveStartToast();

        view.disableNearbyCanteensPushNotificationsSwitchInteraction();

        new RegisterNearbyCanteensPushNotificationsReceiveAsyncTask().execute();

    }

    public void unregisterNearbyCanteensPushNotificationsReceive() {

        new UnregisterNearbyCanteensPushNotificationsReceiveAsyncTask().execute();

    }

    public void setNotUseOfflineData() {

        Provider.instance((SettingsActivity) view).settings().activateOnlineMode((SettingsActivity) view);

    }

    public void changeToDarkMode() {

        boolean changedWithSuccess = Provider.instance((SettingsActivity) view).settings().activateDarkMode((SettingsActivity) view);

        if (changedWithSuccess) {
            this.view.showDarkModeEnabledSnackbar();
        } else {
            this.view.deactivateDarkModeSwitch();
            this.view.showErrorEnablingDarkModeSnackBar();
        }

    }

    public void changeToLightMode() {

        boolean changedWithSuccess = Provider.instance((SettingsActivity) view).settings().activateLightMode((SettingsActivity) view);

        if (changedWithSuccess) {
            this.view.showDarkModeDisabledSnackbar();
        } else {
            this.view.activateDarkModeSwitch();
            this.view.showErrorDisablingDarkModeSnackBar();
        }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isViewAvailableToInteract ? 1 : 0));
    }

    private class DownloadOfflineDataAsyncTask extends AsyncTask<Void, Void, BackgroundResult> {
        @Override
        protected BackgroundResult doInBackground(Void... voids) {

            // view.getContext() instead of cast...

            Context ctx = (SettingsActivity) view;

            // repository wont change so we can declare it to a variable

            RepositoryFactory repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

            BackgroundResult result = new BackgroundResult();

            try {

                List<School> schools
                        = repositoryFactory
                        .createSchoolsRepository()
                        .availableSchools();

                List<Canteen> canteens = new ArrayList<>();

                List<Menu> menus = new ArrayList<>();

                List<Dish> dishes = new ArrayList<>();

                for (School school : schools) {
                    try {
                        List<Canteen> _canteens
                                = repositoryFactory
                                .createCanteensRepository()
                                .canteens(school.id);

                        canteens.addAll(_canteens);

                    } catch (IOException ioException) {

                        ioException.printStackTrace();

                        result.ioException = ioException;

                        return result;

                    } catch (RequestException requestException) {

                        requestException.printStackTrace();

                        if (requestException.response.statusCode != 404) {
                            result.requestException = requestException;

                            return result;
                        }

                    }

                }

                for (Canteen canteen : canteens) {

                    try {
                        Canteen _canteen
                                = repositoryFactory
                                .createCanteensRepository()
                                .canteen(canteen.schoolId, canteen.id);

                        canteen.location = _canteen.location;

                    } catch (IOException ioException) {

                        ioException.printStackTrace();

                        result.ioException = ioException;

                        return result;

                    } catch (RequestException requestException) {

                        requestException.printStackTrace();

                        if (requestException.response.statusCode != 404) {
                            result.requestException = requestException;

                            return result;
                        }

                    }

                    System.out.println(canteen.schoolId);
                    try {
                        List<Menu> _menus
                                = repositoryFactory
                                .createMenusRepository()
                                .menus(canteen.schoolId, canteen.id);
                        menus.addAll(_menus);
                    } catch (IOException ioException) {

                        ioException.printStackTrace();

                        result.ioException = ioException;

                        return result;

                    } catch (RequestException requestException) {

                        requestException.printStackTrace();

                        if (requestException.response.statusCode != 404) {
                            result.requestException = requestException;

                            return result;
                        }

                    }


                }

                for (Menu menu : menus) {

                    long schoolId = 0;

                    for (Canteen canteen : canteens) {

                        if (menu.canteenId == canteen.id) {
                            schoolId = canteen.schoolId;
                            break;
                        }

                    }

                    try {
                        List<Dish> _dishes
                                = repositoryFactory
                                .createDishRepository()
                                .dishes(schoolId, menu.canteenId, menu.id);
                        dishes.addAll(_dishes);
                    } catch (IOException ioException) {

                        ioException.printStackTrace();

                        result.ioException = ioException;

                        return result;

                    } catch (RequestException requestException) {

                        requestException.printStackTrace();

                        if (requestException.response.statusCode != 404) {
                            result.requestException = requestException;

                            return result;
                        }

                    }


                }

                Provider.instance(ctx).settings().activateOfflineMode(ctx);

                // as offline mode was activated we need a new instance of repository factory

                repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

                RoomSchoolsRepositoryImpl schoolsRepository = (RoomSchoolsRepositoryImpl) repositoryFactory.createSchoolsRepository();

                RoomCanteensRepositoryImpl canteensRepository = (RoomCanteensRepositoryImpl) repositoryFactory.createCanteensRepository();

                RoomMenusRepositoryImpl menusRepository = (RoomMenusRepositoryImpl) repositoryFactory.createMenusRepository();

                RoomDishRepositoryImpl dishRepository = (RoomDishRepositoryImpl) repositoryFactory.createDishRepository();

                // first clear old data

                schoolsRepository.clearTable();

                canteensRepository.clearTable();

                menusRepository.clearTable();

                List<Dish> allStoredDishes = dishRepository.dishes();

                List<Dish> dishesToDelete = new ArrayList<>();

                List<Dish> dishesToUpdate = new ArrayList<>();

                List<Dish> dishesToInsert = new ArrayList<>();

                for (Dish dish : allStoredDishes) {

                    if (!dishes.contains(dish)) {
                        dishesToDelete.add(dish);
                    } else {
                        dishesToUpdate.add(dish);
                    }

                }

                for (Dish dish : dishes) {

                    if (!allStoredDishes.contains(dish)) {
                        dishesToInsert.add(dish);
                    }

                }

                if (!dishesToDelete.isEmpty()) {

                    dishRepository.deleteAll(dishesToDelete.toArray(new Dish[]{}));

                }

                // then insert new data

                schoolsRepository.insertAll(schools.toArray(new School[]{}));

                canteensRepository.insertAll(canteens.toArray(new Canteen[]{}));

                menusRepository.insertAll(menus.toArray(new Menu[]{}));

                dishRepository.insertAll(dishesToInsert.toArray(new Dish[]{}));

                dishRepository.updateAll(dishesToUpdate.toArray(new Dish[]{}));

            } catch (IOException ioException) {

                ioException.printStackTrace();

                result.ioException = ioException;

                return result;

            } catch (RequestException requestException) {

                requestException.printStackTrace();

                if (requestException.response.statusCode != 404) {
                    result.requestException = requestException;

                    return result;
                }

            }

            return result;

        }

        @Override
        protected void onPostExecute(BackgroundResult result) {

            super.onPostExecute(result);

            SettingsPresenter presenter = SettingsPresenter.this;

            if (presenter.isViewAvailableToInteract) {

                if (result.ioException != null) {

                    if (!CommunicationMediator.hasInternetConnection((SettingsActivity) presenter.view)) {

                        presenter.view.showNoInternetConnectionError();

                    } else {

                        presenter.view.showServerNotAvailableError();

                    }

                    presenter.view.deactivateOfflineModeSwitch();

                } else if (result.requestException != null) {

                    presenter.view.showUnexepectedServerFailureError();

                    presenter.view.deactivateOfflineModeSwitch();

                } else {

                    view.showOfflineDataDownloadFinishSnackbar();

                }

            }

            view.enableOfflineModeSwitchInteraction();

        }
    }

    private class RegisterFavoriteDishesPushNotificationsReceiveAsyncTask extends AsyncTask<Void, Void, RegisterFavoriteDishesPushNotificationsReceiveAsyncTask.BackgroundResult> {
        @Override
        protected BackgroundResult doInBackground(Void... voids) {

            // view.getContext() instead of cast...

            Context ctx = (SettingsActivity) view;

            // repository wont change so we can declare it to a variable

            RepositoryFactory.RoomRepositoryFactoryImpl repositoryFactory = Provider.instance(ctx).roomRepositoryFactory();

            BackgroundResult result = new BackgroundResult();

            Provider.instance(ctx).settings().allowReceiveOfFavoriteDishPushNotifications(ctx);

            try {


                List<Dish> dishes
                        = repositoryFactory
                        .createDishRepository()
                        .dishes();

                if (!dishes.isEmpty()) {

                    ((SettingsActivity) view).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            view.showRegisteringFavoriteDishesPushNotificationsReceiveStartToast();

                            view.disableFavoriteDishPushNotificationsSwitchInteraction();

                        }
                    });

                    PushNotificationsRepository pushNotificationsRepository
                            = Provider
                            .instance(ctx)
                            .pushNotificationsRepository();

                    String deviceFcmRegistrationToken
                            = Provider
                            .instance(ctx)
                            .settings()
                            .fcmRegistrationToken();

                    for (Dish dish : dishes) {

                        if (dish.isFavorite) {

                            String dishDescription = dish.description;

                            String dishType = dish.type.name().toLowerCase();

                            pushNotificationsRepository.enablePushNotificationsForFavoriteDish(
                                    deviceFcmRegistrationToken,
                                    dishDescription,
                                    dishType
                            );

                        }

                    }

                } else {

                    result.noDishesToRegister = true;

                }

            } catch (IOException ioException) {

                ioException.printStackTrace();

                result.ioException = ioException;

                return result;

            } catch (RequestException requestException) {

                requestException.printStackTrace();

                result.requestException = requestException;

            }

            return result;

        }

        @Override
        protected void onPostExecute(BackgroundResult result) {

            super.onPostExecute(result);

            SettingsPresenter presenter = SettingsPresenter.this;

            if (presenter.isViewAvailableToInteract) {

                if (!result.noDishesToRegister) {

                    if (result.ioException != null) {

                        if (!CommunicationMediator.hasInternetConnection((SettingsActivity) presenter.view)) {

                            presenter.view.showNoInternetConnectionError();

                        } else {

                            presenter.view.showServerNotAvailableError();

                        }

                        presenter.view.deactivateFavoriteDishPushNotificationsSwitch();

                    } else if (result.requestException != null) {

                        presenter.view.showUnexepectedServerFailureError();

                        presenter.view.deactivateFavoriteDishPushNotificationsSwitch();

                    } else {

                        view.showRegisteringFavoriteDishesPushNotificationsReceiveFinishToast();

                    }

                }

                view.enableFavoriteDishPushNotificationsSwitchInteraction();

            }

        }

        private class BackgroundResult {

            public boolean noDishesToRegister;

            public IOException ioException;

            public RequestException requestException;

        }
    }

    private class UnregisterFavoriteDishesPushNotificationsReceiveAsyncTask extends AsyncTask<Void, Void, UnregisterFavoriteDishesPushNotificationsReceiveAsyncTask.BackgroundResult> {
        @Override
        protected BackgroundResult doInBackground(Void... voids) {

            // view.getContext() instead of cast...

            Context ctx = (SettingsActivity) view;

            // repository wont change so we can declare it to a variable

            RepositoryFactory.RoomRepositoryFactoryImpl repositoryFactory = Provider.instance(ctx).roomRepositoryFactory();

            BackgroundResult result = new BackgroundResult();

            Provider.instance(ctx).settings().disallowReceiveOfFavoriteDishPushNotifications(ctx);

            try {

                List<Dish> dishes
                        = repositoryFactory
                        .createDishRepository()
                        .dishes();

                if (!dishes.isEmpty()) {

                    ((SettingsActivity) view).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            view.showUnregisteringFavoriteDishesPushNotificationsReceiveStartToast();

                            view.disableFavoriteDishPushNotificationsSwitchInteraction();

                        }
                    });

                    PushNotificationsRepository pushNotificationsRepository
                            = Provider
                            .instance(ctx)
                            .pushNotificationsRepository();

                    String deviceFcmRegistrationToken
                            = Provider
                            .instance(ctx)
                            .settings()
                            .fcmRegistrationToken();

                    for (Dish dish : dishes) {

                        if (dish.isFavorite) {

                            String dishDescription = dish.description;

                            String dishType = dish.type.name().toLowerCase();

                            pushNotificationsRepository.disablePushNotificationsForFavoriteDish(
                                    deviceFcmRegistrationToken,
                                    dishDescription,
                                    dishType
                            );

                        }

                    }

                } else {

                    result.noDishesToRegister = true;

                }

            } catch (IOException ioException) {

                ioException.printStackTrace();

                result.ioException = ioException;

                return result;

            } catch (RequestException requestException) {

                requestException.printStackTrace();

                result.requestException = requestException;

            }

            return result;

        }

        @Override
        protected void onPostExecute(BackgroundResult result) {

            super.onPostExecute(result);

            SettingsPresenter presenter = SettingsPresenter.this;

            if (presenter.isViewAvailableToInteract) {

                if (!result.noDishesToRegister) {

                    if (result.ioException != null) {

                        if (!CommunicationMediator.hasInternetConnection((SettingsActivity) presenter.view)) {

                            presenter.view.showNoInternetConnectionError();

                        } else {

                            presenter.view.showServerNotAvailableError();

                        }

                        presenter.view.activateFavoriteDishPushNotificationsSwitch();

                    } else if (result.requestException != null) {

                        presenter.view.showUnexepectedServerFailureError();

                        presenter.view.activateFavoriteDishPushNotificationsSwitch();

                    } else {

                        view.showUnregisteringFavoriteDishesPushNotificationsReceiveFinishToast();

                    }

                }

                view.enableFavoriteDishPushNotificationsSwitchInteraction();

            }

        }

        private class BackgroundResult {

            public boolean noDishesToRegister;

            public IOException ioException;

            public RequestException requestException;

        }
    }

    private class RegisterNearbyCanteensPushNotificationsReceiveAsyncTask extends AsyncTask<Void, Void, RegisterNearbyCanteensPushNotificationsReceiveAsyncTask.BackgroundResult> {
        @Override
        protected BackgroundResult doInBackground(Void... voids) {

            // view.getContext() instead of cast...

            final Context ctx = (SettingsActivity) view;

            // repository wont change so we can declare it to a variable

            RepositoryFactory repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

            BackgroundResult result = new BackgroundResult();

            try {

                List<School> schools
                        = repositoryFactory
                        .createSchoolsRepository()
                        .availableSchools();

                if (schools.isEmpty()) {

                    // if schools list is empty then offline mode is activated

                    Provider.instance(ctx).settings().activateOnlineMode(ctx);

                    repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

                    schools = repositoryFactory.createSchoolsRepository().availableSchools();

                    Provider.instance(ctx).settings().activateOfflineMode(ctx);

                    repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

                }

                List<Canteen> allCanteens = new ArrayList<>();

                for (School school : schools) {

                    List<Canteen> schoolCanteens
                            = repositoryFactory
                            .createCanteensRepository()
                            .canteens(school.id);

                    if (schoolCanteens.isEmpty()) {

                        // if school canteens is empty then offline mode is activated

                        Provider.instance(ctx).settings().activateOnlineMode(ctx);

                        repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

                        schoolCanteens = repositoryFactory.createCanteensRepository().canteens(school.id);

                        Provider.instance(ctx).settings().activateOfflineMode(ctx);

                        repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

                    }

                    allCanteens.addAll(schoolCanteens);

                }

                for (Canteen canteen : allCanteens) {

                    if (canteen.location == null) {

                        Canteen canteenDetails
                                = repositoryFactory
                                .createCanteensRepository()
                                .canteen(canteen.schoolId, canteen.id);

                        if (canteenDetails == null) {

                            // if canteen details is null then offline mode is activated

                            Provider.instance(ctx).settings().activateOnlineMode(ctx);

                            repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

                            canteenDetails = repositoryFactory.createCanteensRepository().canteen(canteen.schoolId, canteen.id);

                            Provider.instance(ctx).settings().activateOfflineMode(ctx);

                            repositoryFactory = Provider.instance(ctx).repositoryFactory(ctx);

                        }

                        canteen.location = canteenDetails.location;

                    }

                    System.out.println(canteen.name);

                }

                List<Geofence> geofences = new ArrayList<>();

                for (Canteen canteen : allCanteens) {

                    // Geofence id will be a join of the canteen name + its id and school id
                    // (e.g. cantinadoh-1-1)
                    // By doing so, we create a unique identifier for the geofence and will be able
                    // to identify the canteen and school identifiers on the broadcast receiver

                    String geofenceId
                            = new StringBuilder()
                            .append(canteen.name.toLowerCase().replaceAll("\\s+", ""))
                            .append("-")
                            .append(canteen.id)
                            .append("-")
                            .append(canteen.schoolId)
                            .toString();

                    Geofence geofence
                            = new Geofence
                            .Builder()
                            .setRequestId(geofenceId)
                            .setCircularRegion(
                                    canteen.location.latitude,
                                    canteen.location.longitude,
                                    75
                            )
                            .setLoiteringDelay(5)
                            .setExpirationDuration(60000)
                            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL)
                            .build();

                    geofences.add(geofence);

                }

                GeofencingRequest geofencingRequest
                        = new GeofencingRequest
                        .Builder()
                        .addGeofences(geofences)
                        .build();

                PendingIntent geofencingPendingIntent;

                Intent intent = new Intent(ctx, UserNearbyCanteensGeofencingBroadcastReceiver.class);

                geofencingPendingIntent
                        = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                GeofencingClient geofencingClient = LocationServices.getGeofencingClient(ctx);

                if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    OnSuccessListener<Void> onAddGeofencesSuccessListener = new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            if(isViewAvailableToInteract) {

                                view.showRegisteringNearbyCanteensPushNotificationsReceiveFinishToast();

                                view.enableNearbyCanteensPushNotificationsSwitchInteraction();

                            }

                            Provider.instance(ctx).settings().allowReceiveOfNearbyCanteensPushNotifications(ctx);
                        }
                    };

                    OnFailureListener onAddGeofencesFailureListener = new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            e.printStackTrace();

                            if(isViewAvailableToInteract) {

                                view.showRegisteringNearbyCanteensPushNotificationsReceiveFailedToast();

                                view.deactivateNearbyCanteensDishPushNotificationsSwitch();

                                view.enableNearbyCanteensPushNotificationsSwitchInteraction();

                            }

                        }
                    };

                    geofencingClient.addGeofences(geofencingRequest, geofencingPendingIntent)
                            .addOnSuccessListener(onAddGeofencesSuccessListener)
                            .addOnFailureListener(onAddGeofencesFailureListener);

                } else {
                    result.hasNoFineLocationAccessPermission = true;
                }

            }catch (IOException ioException){

                ioException.printStackTrace();

                result.ioException = ioException;

                return result;

            }catch (RequestException requestException){

                requestException.printStackTrace();

                result.requestException = requestException;

            }

            return result;

        }

        @Override
        protected void onPostExecute(BackgroundResult result) {

            super.onPostExecute(result);

            SettingsPresenter presenter = SettingsPresenter.this;

            if(presenter.isViewAvailableToInteract){

                if(!result.hasNoFineLocationAccessPermission) {

                    if (result.ioException != null) {

                        if (!CommunicationMediator.hasInternetConnection((SettingsActivity) presenter.view)) {

                            presenter.view.showNoInternetConnectionError();

                        } else {

                            presenter.view.showServerNotAvailableError();

                        }

                        presenter.view.deactivateNearbyCanteensDishPushNotificationsSwitch();

                        view.enableNearbyCanteensPushNotificationsSwitchInteraction();

                    } else if (result.requestException != null) {

                        presenter.view.showUnexepectedServerFailureError();

                        presenter.view.deactivateNearbyCanteensDishPushNotificationsSwitch();

                        view.enableNearbyCanteensPushNotificationsSwitchInteraction();

                    }

                } else {

                    view.showApplicationRequireFineLocationAccessPermissionToast();

                    view.enableNearbyCanteensPushNotificationsSwitchInteraction();

                }

            }

        }

        private class BackgroundResult {

            public boolean hasNoFineLocationAccessPermission;

            public IOException ioException;

            public RequestException requestException;

        }
    }

    private class UnregisterNearbyCanteensPushNotificationsReceiveAsyncTask extends AsyncTask<Void, Void, UnregisterNearbyCanteensPushNotificationsReceiveAsyncTask.BackgroundResult> {
        @Override
        protected BackgroundResult doInBackground(Void... voids) {

            // view.getContext() instead of cast...

            final Context ctx = (SettingsActivity) view;

            BackgroundResult result = new BackgroundResult();

            try {

                PendingIntent geofencingPendingIntent;

                Intent intent = new Intent(ctx, UserNearbyCanteensGeofencingBroadcastReceiver.class);

                geofencingPendingIntent
                        = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                GeofencingClient geofencingClient = LocationServices.getGeofencingClient(ctx);

                OnSuccessListener<Void> onAddGeofencesSuccessListener = new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if(isViewAvailableToInteract) {

                            view.showUnregisteringNearbyCanteensPushNotificationsReceiveFinishToast();

                            view.enableNearbyCanteensPushNotificationsSwitchInteraction();

                        }

                        Provider.instance(ctx).settings().disallowReceiveOfNearbyCanteensPushNotifications(ctx);
                    }
                };

                OnFailureListener onAddGeofencesFailureListener = new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        if(isViewAvailableToInteract) {

                            view.showUnregisteringNearbyCanteensPushNotificationsReceiveFailedToast();

                            view.activateNearbyCanteensPushNotificationsSwitch();

                            view.enableNearbyCanteensPushNotificationsSwitchInteraction();

                        }

                    }
                };

                geofencingClient.removeGeofences(geofencingPendingIntent)
                        .addOnSuccessListener(onAddGeofencesSuccessListener)
                        .addOnFailureListener(onAddGeofencesFailureListener);

            }catch (RequestException requestException){

                requestException.printStackTrace();

                result.requestException = requestException;

            }

            return result;

        }

        @Override
        protected void onPostExecute(BackgroundResult result) {

            super.onPostExecute(result);

            SettingsPresenter presenter = SettingsPresenter.this;

            if(presenter.isViewAvailableToInteract){

                if (result.ioException != null) {

                    if (!CommunicationMediator.hasInternetConnection((SettingsActivity) presenter.view)) {

                        presenter.view.showNoInternetConnectionError();

                    } else {

                        presenter.view.showServerNotAvailableError();

                    }

                    presenter.view.activateNearbyCanteensPushNotificationsSwitch();

                    view.enableNearbyCanteensPushNotificationsSwitchInteraction();

                } else if (result.requestException != null) {

                    presenter.view.showUnexepectedServerFailureError();

                    presenter.view.activateNearbyCanteensPushNotificationsSwitch();

                    view.enableNearbyCanteensPushNotificationsSwitchInteraction();

                }

            }

        }

        private class BackgroundResult {

            public IOException ioException; // TODO: Not necessary anymore

            public RequestException requestException; // TODO: Not necessary anymore

        }
    }

    private class BackgroundResult {

        public IOException ioException;

        public RequestException requestException;

    }
}
