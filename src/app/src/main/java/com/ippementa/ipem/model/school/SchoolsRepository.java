package com.ippementa.ipem.model.school;

import java.io.IOException;

public interface SchoolsRepository {

    AvailableSchoolsResponsePayload availableSchools() throws IOException;

}
