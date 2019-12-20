package com.ippementa.ipem.view.settings;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import com.ippementa.ipem.model.canteen.Canteen;
import com.ippementa.ipem.model.canteen.RoomCanteensRepositoryImpl;
import com.ippementa.ipem.model.dish.Dish;
import com.ippementa.ipem.model.dish.RoomDishRepositoryImpl;
import com.ippementa.ipem.model.menu.Menu;
import com.ippementa.ipem.model.menu.RoomMenusRepositoryImpl;
import com.ippementa.ipem.model.school.RoomSchoolsRepositoryImpl;
import com.ippementa.ipem.model.school.School;
import com.ippementa.ipem.util.Provider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreferenceCompat;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

        Context context = getPreferenceManager().getContext();

        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);

        SwitchPreferenceCompat notificationPreference = new SwitchPreferenceCompat(context);

        notificationPreference.setKey("offline-mode");
        notificationPreference.setTitle("Offline Mode");
        notificationPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                boolean checked = (boolean)newValue;

                if(checked){

                    Toast.makeText
                            (SettingsFragment.this.getContext(),
                                    "Please wait while data is being downloaded...",
                                    Toast.LENGTH_LONG
                            ).show();



                    new DownloadOfflineDataAsyncTask().execute();
                }

                return true;
            }
        });

        screen.addPreference(notificationPreference);

        setPreferenceScreen(screen);
    }

    private class DownloadOfflineDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {

                Context ctx = SettingsFragment.this.getContext();

                List<School> schools = Provider.instance(ctx).repositoryFactory(ctx).createSchoolsRepository().availableSchools();

                List<Canteen> canteens = new ArrayList<>();

                List<Menu> menus = new ArrayList<>();

                List<Dish> dishes = new ArrayList<>();

                for(School school : schools){
                    try {
                        List<Canteen> _canteens = Provider.instance(ctx).repositoryFactory(ctx).createCanteensRepository().canteens(school.id);

                        canteens.addAll(_canteens);

                    }catch (Exception e){

                        e.printStackTrace();

                    }

                }

                for(Canteen canteen : canteens){

                    System.out.println(canteen.schoolId);
                    try {
                        List<Menu> _menus = Provider.instance(ctx).repositoryFactory(ctx).createMenusRepository().menus(canteen.schoolId, canteen.id);
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
                        List<Dish> _dishes = Provider.instance(ctx).repositoryFactory(ctx).createDishRepository().dishes(schoolId, menu.canteenId, menu.id);
                        dishes.addAll(_dishes);
                    }catch (Exception e){

                        e.printStackTrace();

                    }


                }

                Provider.instance(ctx).settings().activateOfflineMode(ctx);

                ((RoomSchoolsRepositoryImpl)Provider.instance(ctx).repositoryFactory(ctx).createSchoolsRepository()).insertAll(schools.toArray(new School[]{}));

                ((RoomCanteensRepositoryImpl)Provider.instance(ctx).repositoryFactory(ctx).createCanteensRepository()).insertAll(canteens.toArray(new Canteen[]{}));

                ((RoomMenusRepositoryImpl)Provider.instance(ctx).repositoryFactory(ctx).createMenusRepository()).insertAll(menus.toArray(new Menu[]{}));

                ((RoomDishRepositoryImpl)Provider.instance(ctx).repositoryFactory(ctx).createDishRepository()).insertAll(dishes.toArray(new Dish[]{}));

            }catch(IOException e){
                System.out.println("----------");
                e.printStackTrace();
                System.out.println("----------");
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if(result){

                Toast.makeText
                        (SettingsFragment.this.getContext(),
                                "Offline Data downloaded with success",
                                Toast.LENGTH_LONG
                        ).show();

            }else{

                Toast.makeText
                        (SettingsFragment.this.getContext(),
                                "Something went wrong downloading offline data",
                                Toast.LENGTH_LONG
                        ).show();

            }

        }
    }
}
