package com.ippementa.ipem.model.canteen;

import java.io.IOException;

public interface CanteensRepository {

    AvailableCanteensResponsePayload availableCanteens(long schoolId) throws IOException;

}
