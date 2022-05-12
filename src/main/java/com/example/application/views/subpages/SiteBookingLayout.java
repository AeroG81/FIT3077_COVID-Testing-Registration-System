package com.example.application.views.subpages;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.BookingMethod.FacilityBookingMethod;
import com.example.application.data.entity.BookingMethod.SystemBookingMethod;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.example.application.data.entity.User.Resident;
import com.example.application.data.entity.User.User;
import com.example.application.data.entity.User.UserCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.server.VaadinSession;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SiteBookingLayout extends VerticalLayout {
    private Button submitVerification;
    private final IntegerField verifyPin = new IntegerField("PIN");
    private final Dialog dialog = new Dialog();
    private final TextArea label = new TextArea();
    private final FormLayout registrationCommonForm = new FormLayout();
    private final TextArea notes = new TextArea("Notes");
    private final TestingSiteCollection collection = new TestingSiteCollection();
    private ComboBox<TestingSite> testingSite;
    private DateTimePicker startTime;
    private Button submitRegistration;

    /**
     * populate the layout with components
     */
    public SiteBookingLayout(){
        this.configureRegistrationNotification();
        this.configureDateTimePicker();
        this.configureComboBox();
        this.populateComboBox();
        this.configureRegistrationButton();
        this.configureRegistrationForm();

        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.add(closeButton,label);

        add(registrationCommonForm);
    }

    /**
     * Configuring Registration Notification
     */
    private void configureRegistrationNotification(){
        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.add(closeButton,label);
    }

    /**
     * Configuring Registration Form
     */
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

    /**
     * Configuring Combo Box of Testing Site
     */
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

    /**
     * Configuring Data Time Picker
     */
    private void configureDateTimePicker() {
        startTime = new DateTimePicker();
        startTime.setLabel("Appointment Date and Time");
        startTime.setAutoOpen(true);
        startTime.setMin(LocalDateTime.now().plusDays(1));
        startTime.setValue(LocalDateTime.now().plusDays(1));
        startTime.setMax(LocalDateTime.now().plusDays(90));
    }

    /**
     * Populate the combo box of testing site
     */
    private void populateComboBox(){
        testingSite.setItems(collection.getCollection());
        testingSite.setItemLabelGenerator(TestingSite::getName);
    }

    /**
     * Validate the fields in Registration tab layout
     */
    private boolean validateFields(){
        boolean validation = true;
        if (startTime.getValue().toLocalTime().getHour() < Integer.parseInt(testingSite.getValue().getOperationTime().substring(0,2)) || startTime.getValue().toLocalTime().getHour() >= Integer.parseInt(testingSite.getValue().getOperationTime().substring(7,9)))
            validation = false;
        return validation;
    }

    /**
     * Configuring Registration Button
     */
    private void configureRegistrationButton(){
        submitRegistration = new Button("Submit");
        submitRegistration.setEnabled(false);
        submitRegistration.addClickListener(e -> {
            if (!validateFields()){
                Notification.show("Booking time is not within operation hour");
            }
            else {
                // Get User ID via http request
                // if statement here to check the tab, post to verify or post to create
                VaadinSession ui = UI.getCurrent().getSession();
                User user = new Resident(ui.getAttribute("userId").toString(), ui.getAttribute("userGivenName").toString(), ui.getAttribute("userFamilyName").toString(), ui.getAttribute("userName").toString(), ui.getAttribute("userPhoneNumber").toString());
                HttpResponse<String> response = null;
                ObjectNode mappedResponse = null;
                try {
                    response = new SystemBookingMethod().addBooking(testingSite.getValue(),startTime.getValue().format(DateTimeFormatter.ISO_DATE_TIME), user, notes.getValue());
                    mappedResponse = new ObjectMapper().readValue(response.body(),ObjectNode.class);
                    testingSite.getValue().setWaitingTime((Integer.parseInt(testingSite.getValue().getWaitingTime().substring(0,testingSite.getValue().getWaitingTime().length()-3))+10)+"min");
                } catch (Exception exception){
                    System.out.println("Error creating: " + exception);
                }
                if (response!=null){
                    label.setWidth("500px");
                    label.setEnabled(false);
                    label.clear();
                    label.setValue("PIN: "+ mappedResponse.get("smsPin").asText());
                    dialog.open();
                    Notification noti = Notification.show("Application submitted");
                    noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            }
        });
    }

}
