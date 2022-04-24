package com.example.application.views.form;


import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.FacilityBooking;
import com.example.application.data.entity.Registration.OnSiteTesting;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.details.DetailsVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "/onsitebooking")
@PageTitle("OnSiteBooking | Vaadin CRM")
public class OnSiteBooking extends VerticalLayout {

    TextField filterText = new TextField();
    TestingSiteCollection collection = new TestingSiteCollection();

    private ComboBox<TestingSite> testingSite;
    private DateTimePicker startTime;
    private final TextField notes = new TextField("Notes");
    private Button submit;
    private final TextField existingUserName = new TextField("Username");
    private final PasswordField existingUserPassword = new PasswordField("Password");;
    private final TextField newUserName = new TextField("Username");
    private final TextField newUserGivenName = new TextField("Given Name");
    private final TextField newUserFamilyName = new TextField("Family Name");
    private final PasswordField newUserPassword = new PasswordField("Password");
    private final TextField newUserPhoneNumber = new TextField("Phone Number");
    private final VerticalLayout content;
    private final Tab tabExist;
    private final Tab tabNew;

    public OnSiteBooking(){
        // First form
        FormLayout existingUserLayout = new FormLayout();
        existingUserLayout.add(
                existingUserName,existingUserPassword
        );

        // Second form
        FormLayout newUserLayout = new FormLayout();
        newUserLayout.add(
                newUserGivenName,newUserFamilyName,
                newUserName, newUserPhoneNumber,
                newUserPassword
        );
        newUserLayout.setColspan(newUserPassword, 2);

        tabExist = new Tab("Existing User");
        tabNew = new Tab("New User");
        Tabs tabs = new Tabs(tabExist, tabNew);
        content = new VerticalLayout();
        content.setSpacing(false);
        content.add(existingUserLayout);
        tabs.addSelectedChangeListener(event -> {
                    clearFields();
                    content.removeAll();
                    if (event.getSelectedTab().equals(tabNew)){
                        content.add(newUserLayout);
                    }
                    else if (event.getSelectedTab().equals(tabExist)){
                        content.add(existingUserLayout);
                    }
                }
        );

        configureDateTimePicker();
        configureComboBox();
        populateComboBox();
        configureButton();

        FormLayout commonForm = new FormLayout();
        commonForm.setColspan(notes, 2);
        commonForm.setColspan(submit, 2);
        commonForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0",1),
                new FormLayout.ResponsiveStep("30%",2)
        );
        commonForm.add(
                testingSite, startTime,
                notes,
                submit
        );
        add(tabs,content,commonForm);
        setMargin(false);
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void configureButton(){
        submit = new Button("Submit");
        submit.addClickListener(e -> {
            if (testingSite.getValue() == null){
                Notification.show("Site must not be empty");
            }
            else {
                // Get User ID via http request
                // if statement here to check the tab, post to verify or post to create
                Booking booking = new FacilityBooking(new OnSiteTesting(testingSite.getValue()),startTime.getValue().toString(),existingUserName.getValue(),notes.getValue());
                Notification.show("Site ID: " + testingSite.getValue().getId()+"| "+startTime.getValue().toString()+"  |  "+startTime.getValue().format(DateTimeFormatter.ISO_DATE_TIME));
            }
        });
    }

    private void clearFields(){
        existingUserName.clear();
        existingUserPassword.clear();
        newUserFamilyName.clear();
        newUserGivenName.clear();
        newUserName.clear();
        newUserPassword.clear();
        newUserPhoneNumber.clear();
    }

    private void configureComboBox(){
        testingSite = new ComboBox<>("TestingSite");
    }

    private void configureDateTimePicker() {
        startTime = new DateTimePicker();
        startTime.setLabel("Appointment Date and Time");
        startTime.setAutoOpen(true);
        startTime.setMin(LocalDateTime.now());
        startTime.setValue(LocalDateTime.now().plusDays(7));
    }

    private void populateComboBox(){
        testingSite.setItems(collection.getCollection());
        testingSite.setItemLabelGenerator(TestingSite::getName);
    }
}
