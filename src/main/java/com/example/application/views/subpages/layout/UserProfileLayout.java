package com.example.application.views.subpages.layout;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;

public class UserProfileLayout extends VerticalLayout {
    private final TextField userName = new TextField("Username");
    private final TextField userGivenName = new TextField("Given Name");
    private final TextField userFamilyName = new TextField("Family Name");
    private final IntegerField userPhoneNumber = new IntegerField("Phone Number");
    private Button updateButton;
    private final FormLayout userForm = new FormLayout();

    public UserProfileLayout(){
        this.configureRegistrationButton();
        this.configureFormLayout();
        this.populateFields();
        add(userForm);
    }

    public void populateFields(){
        userGivenName.setValue(UI.getCurrent().getSession().getAttribute("userGivenName").toString());
        userFamilyName.setValue(UI.getCurrent().getSession().getAttribute("userFamilyName").toString());
        userName.setValue(UI.getCurrent().getSession().getAttribute("userName").toString());
        userPhoneNumber.setValue(Integer.parseInt(UI.getCurrent().getSession().getAttribute("userPhoneNumber").toString()));
    }

    public void configureFormLayout(){
        userForm.setColspan(updateButton, 2);
        userForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("30%",2)
        );
        userForm.add(
                userGivenName, userFamilyName,
                userName, userPhoneNumber,
                updateButton
        );
    }

    /**
     * Configuring Registration Button
     */
    private void configureRegistrationButton(){
        updateButton = new Button("Update");
        updateButton.setEnabled(false);
    }
}
