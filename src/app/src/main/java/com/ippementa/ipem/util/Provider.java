package com.ippementa.ipem.util;

import android.content.Context;

import com.ippementa.ipem.model.RepositoryFactory;

import androidx.room.Room;

/**
 * An utility class that has the responsibility of providing a set of features
 */
public class Provider {

    private static Provider _instance;

    private Settings settings;

    private RepositoryFactory.RoomRepositoryFactoryImpl roomRepositoryFactory;

    private Provider(Context ctx){

        this.settings = new Settings(ctx);

        this.roomRepositoryFactory
                = Room
                .databaseBuilder(ctx, RepositoryFactory.RoomRepositoryFactoryImpl.class, "ipp-ementa-sqlite")
                .build();

    }

    public static Provider instance(Context ctx){

        if(_instance == null){

            _instance = new Provider(ctx);

        }

        return _instance;

    }

    /**
     * Provides access to a factory of repositories which type is defined in runtime
     * @param ctx context of the activity which repository factory is being called from
     * @return instance of RepositoryFactory which type is defined in runtime
     */
    public RepositoryFactory repositoryFactory(Context ctx){

        if(this.settings.isInOfflineMode()){

            return roomRepositoryFactory;

        }else{

            return new RepositoryFactory.IPEDRepositoryFactoryImpl();

        }

    }

    /**
     * Provides access to the factory of room repositories
     * @return implementation of RoomDatabase for ipp-ementa repositories
     */
    public RepositoryFactory.RoomRepositoryFactoryImpl roomRepositoryFactory(){

        return roomRepositoryFactory;

    }

    public Settings settings(){

        return this.settings;

    }

}
