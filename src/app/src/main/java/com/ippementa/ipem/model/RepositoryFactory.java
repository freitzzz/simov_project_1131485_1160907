package com.ippementa.ipem.model;

import com.ippementa.ipem.model.canteen.Canteen;
import com.ippementa.ipem.model.canteen.CanteensRepository;
import com.ippementa.ipem.model.canteen.IPEDCanteensRepositoryImpl;
import com.ippementa.ipem.model.canteen.RoomCanteensRepositoryImpl;
import com.ippementa.ipem.model.dish.Dish;
import com.ippementa.ipem.model.dish.DishRepository;
import com.ippementa.ipem.model.dish.IPEDDishRepositoryImpl;
import com.ippementa.ipem.model.dish.RoomDishRepositoryImpl;
import com.ippementa.ipem.model.menu.IPEDMenusRepositoryImpl;
import com.ippementa.ipem.model.menu.Menu;
import com.ippementa.ipem.model.menu.MenusRepository;
import com.ippementa.ipem.model.menu.RoomMenusRepositoryImpl;
import com.ippementa.ipem.model.pushnotifications.IPEPNPushNotificationsRepositoryImpl;
import com.ippementa.ipem.model.pushnotifications.PushNotificationsRepository;
import com.ippementa.ipem.model.school.IPEDSchoolsRepositoryImpl;
import com.ippementa.ipem.model.school.RoomSchoolsRepositoryImpl;
import com.ippementa.ipem.model.school.School;
import com.ippementa.ipem.model.school.SchoolsRepository;

import androidx.room.Database;
import androidx.room.RoomDatabase;

public interface RepositoryFactory {

    SchoolsRepository createSchoolsRepository();

    CanteensRepository createCanteensRepository();

    MenusRepository createMenusRepository();

    DishRepository createDishRepository();

    PushNotificationsRepository createPushNotificationsRepository();


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

        @Override
        public PushNotificationsRepository createPushNotificationsRepository() {

            throw new IllegalStateException("IPED DOES NOT IMPLEMEMENT PUSH NOTIFICATIONS REPOSITORY");

        }
    }

    class IPEPNRepositoryFactoryImpl implements RepositoryFactory{


        @Override
        public SchoolsRepository createSchoolsRepository() {

            throw new IllegalStateException("IPEPN DOES NOT IMPLEMEMENT SCHOOLS REPOSITORY");

        }

        @Override
        public CanteensRepository createCanteensRepository() {

            throw new IllegalStateException("IPEPN DOES NOT IMPLEMEMENT CANTEENS REPOSITORY");

        }

        @Override
        public MenusRepository createMenusRepository() {

            throw new IllegalStateException("IPEPN DOES NOT IMPLEMEMENT MENUS REPOSITORY");
        }

        @Override
        public DishRepository createDishRepository() {

            throw new IllegalStateException("IPEPN DOES NOT IMPLEMEMENT DISH REPOSITORY");
        }

        @Override
        public PushNotificationsRepository createPushNotificationsRepository() {

            return new IPEPNPushNotificationsRepositoryImpl();

        }
    }

    @Database(entities = {School.class, Canteen.class, Menu.class, Dish.class},version = 1,exportSchema = false)
    public abstract class RoomRepositoryFactoryImpl extends RoomDatabase implements RepositoryFactory {

        public abstract RoomSchoolsRepositoryImpl createSchoolsRepository();

        public abstract RoomCanteensRepositoryImpl createCanteensRepository();

        public abstract RoomMenusRepositoryImpl createMenusRepository();

        public abstract RoomDishRepositoryImpl createDishRepository();

    }

}
