package com.ippementa.ipem.model;

import com.ippementa.ipem.model.canteen.CanteensRepository;
import com.ippementa.ipem.model.dish.DishRepository;
import com.ippementa.ipem.model.menu.MenusRepository;
import com.ippementa.ipem.model.school.SchoolsRepository;

public interface RepositoryFactory {

    SchoolsRepository createSchoolsRepository();

    CanteensRepository createCanteensRepository();

    MenusRepository createMenusRepository();

    DishRepository createDishRepository();

}
