package com.example.application.views.form;


import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.Booking.FacilityBooking;
import com.example.application.data.entity.Registration.OnSiteTesting;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.example.application.data.entity.User.User;
import com.example.application.data.entity.User.UserCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "/onsitebooking")
@PageTitle("OnSiteBooking | Vaadin CRM")
public class OnSiteBooking extends VerticalLayout {

    private final TestingSiteCollection collection = new TestingSiteCollection();

    private ComboBox<TestingSite> testingSite;
    private DateTimePicker startTime;
    private final TextArea notes = new TextArea("Notes");
    private final TextField userName = new TextField("Username");
    private final PasswordField userPassword = new PasswordField("Password");;
    private final TextField userGivenName = new TextField("Given Name");
    private final TextField userFamilyName = new TextField("Family Name");
    private final IntegerField userPhoneNumber = new IntegerField("Phone Number");
    private final IntegerField verifyPin = new IntegerField("PIN");
    private Button submitRegistration;
    private Button submitVerification;

    private final FormLayout registrationCommonForm = new FormLayout();
    private final Dialog dialog = new Dialog();
    private final Label label = new Label();
    private final VerticalLayout content = new VerticalLayout();
    private final Tab tabExist = new Tab("Existing User");
    private final Tab tabNew = new Tab("New User");
    private final Tabs registrationSubTabs = new Tabs(tabExist, tabNew);

    private final Tab tabRegistration = new Tab("Registration");
    private final Tab tabVerifyPin = new Tab("Verification") ;
    private final Tabs mainTabs = new Tabs(tabRegistration,tabVerifyPin);
    private final VerticalLayout mainLayout = new VerticalLayout();

    public OnSiteBooking(){
        this.configureRegistrationNoti();
        this.configureRegistrationTabs();
        this.configureDateTimePicker();
        this.configureComboBox();
        this.populateComboBox();
        this.configureRegistrationButton();
        this.configureRegistrationForm();
        this.configureVerifyButton();

        mainLayout.add(registrationSubTabs,content,registrationCommonForm);
        mainTabs.addSelectedChangeListener(event -> {
            this.clearFields();
            mainLayout.removeAll();
            if (event.getSelectedTab().equals(tabRegistration)) {
                mainLayout.add(registrationSubTabs,content,registrationCommonForm);
            }
            else if (event.getSelectedTab().equals(tabVerifyPin)) {
                mainLayout.add(verifyPin, submitVerification);
            }
        });

        setMargin(false);
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(mainTabs,mainLayout);
    }

    private void configureRegistrationForm(){
        registrationCommonForm.setColspan(notes, 2);
        registrationCommonForm.setColspan(submitRegistration, 2);
        registrationCommonForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0",1),
                new FormLayout.ResponsiveStep("30%",2)
        );
        registrationCommonForm.add(
                testingSite, startTime,
                notes,
                submitRegistration
        );
    }

    private void configureRegistrationTabs(){
        // First form
        FormLayout existingUserLayout = new FormLayout();
        existingUserLayout.add(
                userName, userPassword
        );

        // Second form
        FormLayout newUserLayout = new FormLayout();

        content.setSpacing(false);
        content.add(existingUserLayout);
        registrationSubTabs.addSelectedChangeListener(event -> {
                    this.clearFields();
                    content.removeAll();
                    newUserLayout.removeAll();
                    existingUserLayout.removeAll();
                    if (event.getSelectedTab().equals(tabNew)){
                        newUserLayout.add(
                                userGivenName, userFamilyName,
                                userName, userPhoneNumber,
                                userPassword
                        );
                        newUserLayout.setColspan(userPassword, 2);
                        content.add(newUserLayout);
                    }
                    else if (event.getSelectedTab().equals(tabExist)){
                        existingUserLayout.add(
                                userName, userPassword
                        );
                        existingUserLayout.setColspan(userPassword, 1);
                        content.add(existingUserLayout);
                    }
                }
        );
    }

    private void configureRegistrationNoti(){
        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.add(closeButton,label);
    }

    private void configureRegistrationButton(){
        submitRegistration = new Button("Submit");
        submitRegistration.setEnabled(false);
        submitRegistration.addClickListener(e -> {
            if (!validateFields()){
                Notification.show("Site must not be empty");
            }
            else {
                // Get User ID via http request
                // if statement here to check the tab, post to verify or post to create
                UserCollection collection = new UserCollection();
                User user = null;
                try {
                    if (registrationSubTabs.getSelectedTab().equals(tabExist))
                        user = collection.verifyUserId(userName.getValue(), userPassword.getValue());
                    else
                        user = collection.addUserService(userGivenName.getValue(), userFamilyName.getValue(), userName.getValue(), userPassword.getValue(), userPhoneNumber.getValue().toString(), true, false,false);
                }
                catch (Exception exception){
                    Notification.show(exception.toString());
                }
                HttpResponse<String> response = null;
                ObjectNode mappedResponse = null;
                try {
                    OnSiteTesting testingMethod = new OnSiteTesting(testingSite.getValue());
                    response = new FacilityBooking(testingMethod,startTime.getValue().format(DateTimeFormatter.ISO_DATE_TIME), user,notes.getValue()).addBooking();
                    mappedResponse = new ObjectMapper().readValue(response.body(),ObjectNode.class);
                } catch (Exception exception){
                    System.out.println("Error creating: " + exception.toString());
                }
                if (response!=null){
                    Notification noti = Notification.show("Application submitted");
                    noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    label.removeAll();
                    label.add("PIN: "+ mappedResponse.get("smsPin").asText());
                    dialog.open();
                }
            }
        });
    }

    private void configureVerifyButton(){
        verifyPin.addValueChangeListener(e -> {
            if (!verifyPin.isEmpty() && !verifyPin.isInvalid())
                submitVerification.setEnabled(true);
            else
                submitVerification.setEnabled(false);
        });
        submitVerification = new Button("Verify");
        submitVerification.setEnabled(false);
        submitVerification.addClickListener(e -> {
            if (!verifyPin.isEmpty() && !verifyPin.isInvalid()){
                BookingCollection collection = new BookingCollection();
                Booking userBooking = collection.verifyPin(verifyPin.getValue().toString());
                label.removeAll();
                if (userBooking!=null){
                    label.add(userBooking.toString());
                    dialog.open();
                }
                else {
                    Notification noti = Notification.show("Error! Invalid PIN");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
            else {
                Notification noti = Notification.show("Error! Invalid PIN");
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }

    private boolean validateFields(){
        boolean validation = false;
        if(registrationSubTabs.getSelectedTab().equals(tabExist)){
            if(!userName.isEmpty() && !userPassword.isEmpty()){
                validation = true;
            }
        }
        else if (registrationSubTabs.getSelectedTab().equals(tabNew)){
            if(!userName.isEmpty() && !userPassword.isEmpty() && !userGivenName.isEmpty() && !userFamilyName.isEmpty() && !userPhoneNumber.isEmpty()){
                validation = true;
            }
        }
        return validation;
    }

    private void clearFields(){
        userName.clear();
        userPassword.clear();
        userFamilyName.clear();
        userGivenName.clear();
        userPhoneNumber.clear();
    }

    private void configureComboBox(){
        testingSite = new ComboBox<>("TestingSite");
        testingSite.setRequired(true);
        testingSite.addValueChangeListener(event -> {
            if (testingSite.getValue() != null){
                submitRegistration.setEnabled(true);
            }
            else {
                submitRegistration.setEnabled(false);
            }
        });
    }

    private void configureDateTimePicker() {
        startTime = new DateTimePicker();
        startTime.setLabel("Appointment Date and Time");
        startTime.setAutoOpen(true);
        startTime.setMin(LocalDateTime.now());
        startTime.setValue(LocalDateTime.now());
        startTime.setMax(LocalDateTime.now().plusDays(90));
    }

    private void populateComboBox(){
        testingSite.setItems(collection.getCollection());
        testingSite.setItemLabelGenerator(TestingSite::getName);
    }
}
