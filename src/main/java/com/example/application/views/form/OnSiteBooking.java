package com.example.application.views.form;


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
    private Button submit;

    private final Dialog dialog;
    private final Label label;
    private final VerticalLayout content;
    private final Tab tabExist;
    private final Tab tabNew;
    private final Tabs tabs;

    public OnSiteBooking(){
        tabExist = new Tab("Existing User");
        tabNew = new Tab("New User");
        tabs = new Tabs(tabExist, tabNew);
        content = new VerticalLayout();
        dialog = new Dialog();
        label = new Label();
        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.add(label,closeButton);

        this.configureTabs();
        this.configureDateTimePicker();
        this.configureComboBox();
        this.populateComboBox();
        this.configureButton();

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

    private void configureTabs(){
        // First form
        FormLayout existingUserLayout = new FormLayout();
        existingUserLayout.add(
                userName, userPassword
        );

        // Second form
        FormLayout newUserLayout = new FormLayout();

        content.setSpacing(false);
        content.add(existingUserLayout);
        tabs.addSelectedChangeListener(event -> {
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

    private void configureButton(){
        submit = new Button("Submit");
        submit.setEnabled(false);
        submit.addClickListener(e -> {
            if (!validateFields()){
                Notification.show("Site must not be empty");
            }
            else {
                // Get User ID via http request
                // if statement here to check the tab, post to verify or post to create
                UserCollection collection = new UserCollection();
                User user = null;
                try {
                    if (tabs.getSelectedTab().equals(tabExist))
                        user = collection.searchUserId(userName.getValue(), userPassword.getValue());
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
                    label.add("PIN: "+ mappedResponse.get("smsPin").asText());
                    dialog.open();
                }
            }
        });
    }

    private boolean validateFields(){
        boolean validation = false;
        if(tabs.getSelectedTab().equals(tabExist)){
            if(!userName.isEmpty() && !userPassword.isEmpty()){
                validation = true;
            }
        }
        else if (tabs.getSelectedTab().equals(tabNew)){
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
                submit.setEnabled(true);
            }
            else {
                submit.setEnabled(false);
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
