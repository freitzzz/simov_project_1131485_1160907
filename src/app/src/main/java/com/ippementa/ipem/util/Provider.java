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

        if(this.settings.isInOfflineMode()){

            this.roomRepositoryFactory
                    = Room.databaseBuilder(ctx, RepositoryFactory.RoomRepositoryFactoryImpl.class, "ipp-ementa-sqlite")
                    .build();
        }

    }

    public static Provider instance(Context ctx){

        if(_instance == null){

            _instance = new Provider(ctx);

        }

        return _instance;

    }

    public RepositoryFactory repositoryFactory(Context ctx){

        if(this.settings.isInOfflineMode()){

            if(roomRepositoryFactory == null){
                this.roomRepositoryFactory
                        = Room.databaseBuilder(ctx, RepositoryFactory.RoomRepositoryFactoryImpl.class, "ipp-ementa-sqlite")
                        .build();
            }

            return roomRepositoryFactory;

        }else{

            return new RepositoryFactory.IPEDRepositoryFactoryImpl();

        }

    }

}
