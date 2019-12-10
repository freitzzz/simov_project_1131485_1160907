package com.ippementa.ipem.view.school;

import com.ippementa.ipem.presenter.school.AvailableSchoolsModel;
import com.ippementa.ipem.view.error.ErrorView;

public interface AvailableSchoolsView extends ErrorView {

    void showSchools(AvailableSchoolsModel schools);

    void navigateToSchoolCanteensPage(AvailableSchoolsModel.Item school);

    void showUnavailableSchoolsError();

}
