package com.example.application.views.subpages;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.Booking.OnSiteTesting;
import com.example.application.data.entity.Booking.OnlineTesting;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserBookingsLayout extends VerticalLayout {
    Accordion accordion = new Accordion();

    public UserBookingsLayout(){
        this.addAccordion();
        add(accordion);
    }

    private void addAccordion(){
        List<Booking> userBookings = new BookingCollection().getBookingsById(UI.getCurrent().getSession().getAttribute("userId").toString());

        for (Booking b : userBookings){
            AccordionPanel panel = accordion.add(b.getStartTime()+" - "+b.getStatus(),this.configureForm(b));
            panel.addOpenedChangeListener(e -> {
                if (e.isOpened())
                    panel.setSummaryText(b.getStartTime()+" - "+b.getStatus());
            });
        };
    }



    private FormLayout configureForm(Booking b){
        FormLayout form = new FormLayout();
        DateTimePicker startTime = new DateTimePicker();
        TextArea notes = new TextArea("Notes");
        Button submitUpdate = new Button("Update");;

        startTime.setLabel("Appointment Date and Time");
        startTime.setAutoOpen(true);
        startTime.setMin(LocalDateTime.now());
        startTime.setValue( ZonedDateTime.parse(b.getStartTime()).toLocalDateTime() );
        startTime.setMax(LocalDateTime.now().plusDays(90));

        form.setColspan(notes, 2);
        form.setColspan(submitUpdate, 2);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0",2)
        );
        if (b.getNotes()==null)
            notes.setValue("");
        else
            notes.setValue(b.getNotes());

        if (b.getClass().equals(OnlineTesting.class)){
            if (!b.getStatus().equals("INITIATED")) {
                submitUpdate.setEnabled(false);
                startTime.setEnabled(false);
                notes.setEnabled(false);
            }
            form.setColspan(startTime, 2);
            form.add(
                    startTime,
                    notes,
                    submitUpdate
            );
        }
        else if (b.getClass().equals(OnSiteTesting.class)){
            ComboBox<TestingSite> testingSite = new ComboBox<>("TestingSite");
            if (!b.getStatus().equals("INITIATED")){
                submitUpdate.setEnabled(false);
                startTime.setEnabled(false);
                notes.setEnabled(false);
                testingSite.setEnabled(false);
                testingSite.setItems(((OnSiteTesting) b).getTestingSite());
            }
            else {
                testingSite.setRequired(true);
                TestingSiteCollection collection = new TestingSiteCollection();
                testingSite.setItems(collection.getCollection());
            }
            testingSite.setItemLabelGenerator(TestingSite::getName);
            testingSite.setValue(((OnSiteTesting) b).getTestingSite());
            form.add(
                    testingSite, startTime,
                    notes,
                    submitUpdate
            );
        }
        return form;
    }
}
