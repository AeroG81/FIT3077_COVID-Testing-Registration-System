package com.example.application.views.form;

import com.example.application.data.entity.BookingMethod.FacilityBookingMethod;
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
@PageTitle("OnSite")
public class OnSiteBookingView extends VerticalLayout {

    private final TestingSiteCollection collection = new TestingSiteCollection();

    private ComboBox<TestingSite> testingSite;
    private DateTimePicker startTime;
    private final TextArea notes = new TextArea("Notes");
    private final TextField userName = new TextField("Username");
    private final PasswordField userPassword = new PasswordField("Password");;
    private final TextField userGivenName = new TextField("Given Name");
    private final TextField userFamilyName = new TextField("Family Name");
    private final IntegerField userPhoneNumber = new IntegerField("Phone Number");
    private Button submitRegistration;


    private final FormLayout registrationCommonForm = new FormLayout();
    private final Dialog dialog = new Dialog();
    private final TextArea label = new TextArea();
    private final VerticalLayout content = new VerticalLayout();
    private final Tab tabExist = new Tab("Existing User");
    private final Tab tabNew = new Tab("New User");
    private final Tabs registrationSubTabs = new Tabs(tabExist, tabNew);

    private final Tab tabRegistration = new Tab("Registration");
    private final Tab tabVerifyPin = new Tab("Verify PIN") ;
    private final Tab tabScanQr = new Tab("RAT-kit verify QR");
    private final Tabs mainTabs = new Tabs(tabRegistration,tabVerifyPin,tabScanQr);
    private final VerticalLayout mainLayout = new VerticalLayout();

    public OnSiteBookingView(){
        this.configureRegistrationNoti();
        this.configureRegistrationTabs();
        this.configureDateTimePicker();
        this.configureComboBox();
        this.populateComboBox();
        this.configureRegistrationButton();
        this.configureRegistrationForm();

        mainLayout.add(registrationSubTabs,content,registrationCommonForm);
        mainTabs.addSelectedChangeListener(event -> {
            this.clearFields();
            mainLayout.removeAll();
            if (event.getSelectedTab().equals(tabRegistration)) {
                mainLayout.add(registrationSubTabs,content,registrationCommonForm);
            }
            else if (event.getSelectedTab().equals(tabVerifyPin)) {
                mainLayout.add(new PinVerifyLayout());
            }
            else if (event.getSelectedTab().equals(tabScanQr)) {
                mainLayout.add(new QrVerifyLayout());
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
                Notification.show("Site must not be empty or Booking time is not within operation hour");
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
                    response = new FacilityBookingMethod().addBooking(testingSite.getValue(),startTime.getValue().format(DateTimeFormatter.ISO_DATE_TIME), user,notes.getValue());
                    mappedResponse = new ObjectMapper().readValue(response.body(),ObjectNode.class);
                    testingSite.getValue().setWaitingTime((Integer.parseInt(testingSite.getValue().getWaitingTime().substring(0,testingSite.getValue().getWaitingTime().length()-3))+10)+"min");
                } catch (Exception exception){
                    System.out.println("Error creating: " + exception);
                }
                if (response!=null){
                    Notification noti = Notification.show("Application submitted");
                    noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    label.setWidth("500px");
                    label.setEnabled(false);
                    label.clear();
                    label.setValue("PIN: "+ mappedResponse.get("smsPin").asText());
                    dialog.open();
                }
            }
        });
    }

    private boolean validateFields(){
        boolean validation = true;
        if(registrationSubTabs.getSelectedTab().equals(tabExist)){
            if(userName.isEmpty() || userPassword.isEmpty()){
                validation = false;
            }
        }
        else if (registrationSubTabs.getSelectedTab().equals(tabNew)){
            if(userName.isEmpty() || userPassword.isEmpty() || userGivenName.isEmpty() || userFamilyName.isEmpty() || userPhoneNumber.isEmpty()){
                validation = false;
            }
        }
        if (startTime.getValue().toLocalTime().getHour() < Integer.parseInt(testingSite.getValue().getOperationTime().substring(0,2)) || startTime.getValue().toLocalTime().getHour() >= Integer.parseInt(testingSite.getValue().getOperationTime().substring(7,9)))
            validation = false;
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
        startTime.setMin(LocalDateTime.now().plusDays(1));
        startTime.setValue(LocalDateTime.now().plusDays(1));
        startTime.setMax(LocalDateTime.now().plusDays(90));
    }

    private void populateComboBox(){
        testingSite.setItems(collection.getCollection());
        testingSite.setItemLabelGenerator(TestingSite::getName);
    }
}
