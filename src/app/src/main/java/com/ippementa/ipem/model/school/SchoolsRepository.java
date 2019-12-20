package com.ippementa.ipem.model.school;

import java.io.IOException;
import java.util.List;

public interface SchoolsRepository {

    List<School> availableSchools() throws IOException;

}
