package com.ippementa.ipem.model;

import com.ippementa.ipem.model.canteen.CanteensRepository;
import com.ippementa.ipem.model.canteen.IPEDCanteensRepositoryImpl;
import com.ippementa.ipem.model.dish.DishRepository;
import com.ippementa.ipem.model.dish.IPEDDishRepositoryImpl;
import com.ippementa.ipem.model.menu.IPEDMenusRepositoryImpl;
import com.ippementa.ipem.model.menu.MenusRepository;
import com.ippementa.ipem.model.school.IPEDSchoolsRepositoryImpl;
import com.ippementa.ipem.model.school.SchoolsRepository;

import androidx.room.RoomDatabase;

public interface RepositoryFactory {

    SchoolsRepository createSchoolsRepository();

    CanteensRepository createCanteensRepository();

    MenusRepository createMenusRepository();

    DishRepository createDishRepository();


    class IPEDRepositoryFactoryImpl implements RepositoryFactory{


        @Override
        public SchoolsRepository createSchoolsRepository() {
            return new IPEDSchoolsRepositoryImpl();
        }

        @Override
        public CanteensRepository createCanteensRepository() {
            return new IPEDCanteensRepositoryImpl();
        }

        @Override
        public MenusRepository createMenusRepository() {

            return new IPEDMenusRepositoryImpl();
        }

        @Override
        public DishRepository createDishRepository() {

            return new IPEDDishRepositoryImpl();
        }
    }

    abstract class RoomRepositoryFactoryImpl extends RoomDatabase implements RepositoryFactory {

    }

}
