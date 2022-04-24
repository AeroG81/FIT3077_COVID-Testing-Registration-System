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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
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
    private Button submit;
    private final TextField existingUserName = new TextField("Username");
    private final PasswordField existingUserPassword = new PasswordField("Password");;

    private final TextField newUserGivenName = new TextField("Given Name");
    private final TextField newUserFamilyName = new TextField("Family Name");
    private final TextField newUserPhoneNumber = new TextField("Phone Number");

    private final VerticalLayout content;
    private final Tab tabExist;
    private final Tab tabNew;
    private final Tabs tabs;

    public OnSiteBooking(){
        tabExist = new Tab("Existing User");
        tabNew = new Tab("New User");
        tabs = new Tabs(tabExist, tabNew);
        content = new VerticalLayout();

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
                existingUserName, existingUserPassword
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
                                newUserGivenName,newUserFamilyName,
                                existingUserName, newUserPhoneNumber,
                                existingUserPassword
                        );
                        newUserLayout.setColspan(existingUserPassword, 2);
                        content.add(newUserLayout);
                    }
                    else if (event.getSelectedTab().equals(tabExist)){
                        existingUserLayout.add(
                                existingUserName, existingUserPassword
                        );
                        existingUserLayout.setColspan(existingUserPassword, 1);
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
                try {
                    submit();
                }
                catch (Exception exception){
                    Notification.show(exception.toString());
                }
                // Booking booking = new FacilityBooking(new OnSiteTesting(testingSite.getValue()),startTime.getValue().toString(),existingUserName.getValue(),notes.getValue());
                Notification.show(existingUserName.getValue()+" "+existingUserPassword.getValue()+" "+newUserFamilyName.getValue()+" "+newUserGivenName.getValue()+" "+newUserPhoneNumber.getValue());
            }
        });
    }

    // Should be exist in UserCollection
    private void submit() throws Exception{
        String jsonString = "";
        String myApiKey = "7WwqfjwcprP7HPqLRmnmQ8QNzg9MWj";
        String url = "";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest
                .newBuilder(URI.create(url))
                .setHeader("Authorization", myApiKey)
                .header("Content-Type","application/json") // This header needs to be set when sending a JSON request body.
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(tabs.getSelectedTab().equals(tabExist)){
            url = "https://fit3077.com/api/v1/user/login?jwt=false";
        }
        else if (tabs.getSelectedTab().equals(tabNew)){
            url = "https://fit3077.com/api/v1/user";
        }
    }

    private boolean validateFields(){
        boolean validation = false;
        if(tabs.getSelectedTab().equals(tabExist)){
            if(!existingUserName.isEmpty() && !existingUserPassword.isEmpty()){
                validation = true;
            }
        }
        else if (tabs.getSelectedTab().equals(tabNew)){
            if(!existingUserName.isEmpty() && !existingUserPassword.isEmpty() && !newUserGivenName.isEmpty() && !newUserFamilyName.isEmpty() && !newUserPhoneNumber.isEmpty()){
                validation = true;
            }
        }
        return validation;
    }

    private void clearFields(){
        existingUserName.clear();
        existingUserPassword.clear();
        newUserFamilyName.clear();
        newUserGivenName.clear();
        newUserPhoneNumber.clear();
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
