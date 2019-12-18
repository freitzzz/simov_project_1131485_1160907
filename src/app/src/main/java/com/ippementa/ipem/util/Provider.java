package com.ippementa.ipem.util;

import com.ippementa.ipem.model.RepositoryFactory;

/**
 * An utility class that has the responsibility of providing a set of features
 */
public class Provider {

    private static Provider _instance;

    private Provider(){}

    public static Provider instance(){

        if(_instance == null){

            _instance = new Provider();

        }

        return _instance;

    }

    public RepositoryFactory repositoryFactory(){

        return new RepositoryFactory.IPEDRepositoryFactoryImpl();

    }

}
