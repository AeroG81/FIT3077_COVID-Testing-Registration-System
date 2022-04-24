package com.example.application.views.form;


import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;

@Route(value = "/onsitebooking")
@PageTitle("OnSiteBooking | Vaadin CRM")
public class OnSiteBooking extends HorizontalLayout {

    TextField filterText = new TextField();
    TestingSiteCollection collection = new TestingSiteCollection();
    private TextField name;
    private ComboBox<TestingSite> testingSite;
    private DateTimePicker startTime;
    private TextField notes;
    private Button submit;

    public OnSiteBooking(){
        FormLayout form = new FormLayout();
        name = new TextField("Your name");
        notes = new TextField("Notes");
        configureDateTimePicker();
        configureComboBox();
        populateComboBox();
        configureButton();
        setSizeFull();
        form.setColspan(name, 2);
        form.setColspan(notes, 2);
        form.setColspan(submit, 2);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0",1),
                new FormLayout.ResponsiveStep("45%",2)
        );
        form.add(
                name, testingSite,
                startTime, notes,
                submit
        );
        setMargin(false);
        setPadding(true);
        add(form);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private void configureButton(){
        submit = new Button("Submit");
        submit.addClickListener(e -> {
            if (testingSite.getValue() == null){
                Notification.show("Site must not be empty");
            }
            else {
                Notification.show("Site ID: " + testingSite.getValue().getId());
            }
        });
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
