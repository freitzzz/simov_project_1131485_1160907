package com.ippementa.ipem.model.canteen;

import java.io.IOException;
import java.util.List;

public interface CanteensRepository {

    List<Canteen> canteens(long schoolId) throws IOException;

    Canteen canteen(long schoolId, long canteenId) throws IOException;

}
