package com.example.application.views.subpages.layout;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.Booking.OnSiteTestingBooking;
import com.example.application.data.entity.Booking.HomeTestingBooking;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.accordion.AccordionPanel;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class UserBookingsLayout extends VerticalLayout {
    Accordion accordion = new Accordion();

    public UserBookingsLayout(){
        this.addAccordion();
        add(accordion);
        getStyle().set("font-family","Roboto Mono");
    }

    private void addAccordion() {
        List<Booking> userBookings = new BookingCollection().getBookingsById(UI.getCurrent().getSession().getAttribute("userId").toString());
        userBookings.sort(new Comparator<Booking>() {
            @Override
            public int compare(Booking o1, Booking o2) {
                return ZonedDateTime.parse(o2.getStartTime()).toLocalDateTime().compareTo(ZonedDateTime.parse(o1.getStartTime()).toLocalDateTime());
            }
        });
        for (Booking b : userBookings) {
            if (LocalDateTime.now().compareTo(ZonedDateTime.parse(b.getStartTime()).toLocalDateTime()) > 0 && b.getStatus().equals("INITIATED")) {
                AccordionPanel panel = accordion.add(ZonedDateTime.parse(b.getStartTime()).format(DateTimeFormatter.ofPattern("E dd/MM/yyyy - HH:mm z")) + " - " + "EXPIRED", this.configureForm(b));
                panel.getSummary().getElement().getStyle().set("font-family","Roboto Mono");
            } else {
                AccordionPanel panel = accordion.add(ZonedDateTime.parse(b.getStartTime()).format(DateTimeFormatter.ofPattern("E dd/MM/yyyy - HH:mm z")) + " - " + b.getStatus(), this.configureForm(b));
                panel.getSummary().getElement().getStyle().set("font-family","Roboto Mono");
            }
        }
    }

    private FormLayout configureForm(Booking b){
        FormLayout form = new FormLayout();
        DateTimePicker startTime = new DateTimePicker();
        Button submitUpdate = new Button("Update");

        startTime.setLabel("Appointment Date and Time");
        startTime.setValue(ZonedDateTime.parse(b.getStartTime()).toLocalDateTime());

        form.setColspan(submitUpdate, 2);
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 2)
        );

        Label pin = new Label("PIN CODE: " + b.getSmsPin());
        Label qr = new Label("QR CODE: " + b.getQrcode());
        form.setColspan(pin, 2);
        form.setColspan(qr, 2);

        if (b.getClass().equals(HomeTestingBooking.class)) {
            this.configureFormForOnlineTesting(b, startTime, submitUpdate);
            form.setColspan(startTime, 2);
            Label link = new Label("MEETING LINK: " + ((HomeTestingBooking) b).getUrl());
            form.setColspan(link, 2);
            if (isInvalidStatus(b)){
                form.add(
                        pin,
                        qr,
                        link,
                        startTime
                );
            }
            else {
                startTime.setAutoOpen(true);
                startTime.setMin(LocalDateTime.now());
                startTime.setValue(ZonedDateTime.parse(b.getStartTime()).toLocalDateTime());
                startTime.setMax(LocalDateTime.now().plusDays(90));
                form.add(
                        pin,
                        qr,
                        link,
                        startTime,
                        submitUpdate
                );
            }

        } else if (b.getClass().equals(OnSiteTestingBooking.class)) {
            ComboBox<TestingSite> testingSite = new ComboBox<>("TestingSite");
            this.configureFormForOnSiteTesting(b, startTime, submitUpdate, testingSite);
            form.setColspan(startTime, 1);
            if (isInvalidStatus(b)){
                form.add(
                        pin,
                        qr,
                        testingSite, startTime
                );
            }
            else {
                startTime.setAutoOpen(true);
                startTime.setMin(LocalDateTime.now());

                startTime.setMax(LocalDateTime.now().plusDays(90));
                form.add(
                        pin,
                        qr,
                        testingSite, startTime,
                        submitUpdate
                );
            }
        }

        if (!this.isInvalidStatus(b)){
            this.addCancelButton(form, b.getBookingId());
            this.addRevertOptions(form, b);
        }

        return form;
    }

    private void addRevertOptions(FormLayout form, Booking b) {

        Select<String> select = new Select<>();
        select.setLabel("History");
        ArrayList<String> history = new ArrayList<>();
        if (b.getHistory().get(0) != null && !b.getHistory().get(0).equals("null"))
            history.add(b.getHistory().get(0));
        if (b.getHistory().get(1) != null && !b.getHistory().get(1).equals("null"))
            history.add(b.getHistory().get(1));
        if (b.getHistory().get(2) != null && !b.getHistory().get(2).equals("null"))
            history.add(b.getHistory().get(2));
        history.add(0, "current");
        select.setItems(history);
        select.setValue("current");

        Button revert = new Button("Revert previous version");
        revert.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);
        revert.addClickListener(e -> {
            if (!select.getValue().equals("current")) {
                List<String> additionalInfo = new ArrayList<>();
                additionalInfo.add(0, b.getQrcode());
                if (b.getClass().equals(HomeTestingBooking.class))
                    additionalInfo.add(1, ((HomeTestingBooking) b).getUrl());
                int index = history.indexOf(select.getValue());
                ObjectNode jsonNode = null;
                try {
                    jsonNode = new ObjectMapper().readValue(select.getValue(), ObjectNode.class);
                }
                catch (Exception exception) {
                    System.out.println("Unable to map select");
                }
                if (ZonedDateTime.parse(jsonNode.get("starttime").asText()).toLocalDateTime().compareTo(LocalDateTime.now())<0){
                    Notification noti = Notification.show("History date is not a future date");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
                else {
                    try {
                        HttpResponse<String> response = BookingCollection.revertBooking(b.getBookingId(), additionalInfo, b.getHistory(), select.getValue(), index);
                        if (response.statusCode()==200){
                            Notification noti = Notification.show("Revert Success");
                            noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        }
                        this.reloadForm();
                    } catch (Exception exception) {
                        Notification noti = Notification.show("Revert Failed");
                        noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        System.out.println("Error Reverting History |" + exception);
                    }
                }
            }
            else {
                Notification noti = Notification.show("Please select a history version");
                noti.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            }
        });
        form.add(select, revert);
    }

    private void configureFormForOnlineTesting(Booking b, DateTimePicker startTime, Button submitUpdate) {
        if (isInvalidStatus(b)) {
            startTime.setEnabled(false);
            submitUpdate.setEnabled(false);
        }
        else {
            submitUpdate.addClickListener(e -> {
                if (startTime.getValue().toLocalTime().getHour() < 8  || startTime.getValue().toLocalTime().getHour() >= 21){
                    Notification noti = Notification.show("Online testing only available during 0800 - 2100");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
                else if (startTime.getValue().equals(ZonedDateTime.parse(b.getStartTime()).toLocalDateTime())){
                    Notification noti = Notification.show("No changes in value, Unable to update");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
                else {
                    List<String> additionalInfo = new ArrayList<>();
                    additionalInfo.add(0, b.getQrcode());
                    String content = "";
                    String newSiteId = null;

                    additionalInfo.add(1, ((HomeTestingBooking) b).getUrl());
                    content = "{"  + "\"testingsitename\":" + null  + ", \"testingsiteid\":" + null + ", \"starttime\": \"" + b.getStartTime() + "\" } ";
                    List<String> history = b.getHistory();
                    try {
                        HttpResponse<String> response = BookingCollection.updateBooking(b.getBookingId(), additionalInfo, history, content, startTime.getValue().format(DateTimeFormatter.ISO_DATE_TIME), newSiteId);
                        if (response.statusCode() == 200){
                            Notification noti = Notification.show("Update Success");
                            noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        }
                        else
                            throw new Exception(response.body());
                        this.reloadForm();
                    } catch (Exception exception) {
                        System.out.println(exception);
                        Notification noti = Notification.show("Update Failed");
                        noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }
            });
        }
    }

    private void configureFormForOnSiteTesting(Booking b, DateTimePicker startTime, Button submitUpdate, ComboBox<TestingSite> testingSite) {
        if (isInvalidStatus(b)) {
            startTime.setEnabled(false);
            testingSite.setEnabled(false);
            testingSite.setItems(((OnSiteTestingBooking) b).getTestingSite());
        } else {
            testingSite.setRequired(true);
            TestingSiteCollection collection = new TestingSiteCollection();
            testingSite.setItems(collection.getCollection());
            submitUpdate.addClickListener(e -> {
                if (testingSite.getValue()!=null)
                    if (startTime.getValue().toLocalTime().getHour() < Integer.parseInt(testingSite.getValue().getOperationTime().substring(0,2)) || startTime.getValue().toLocalTime().getHour() >= Integer.parseInt(testingSite.getValue().getOperationTime().substring(7,9))){
                        Notification noti = Notification.show("Booking time is not within operation hour");
                        noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                    else if (startTime.getValue().equals(ZonedDateTime.parse(b.getStartTime()).toLocalDateTime()) && testingSite.getValue().getId().equals(((OnSiteTestingBooking) b).getTestingSite().getId())){
                        Notification noti = Notification.show("No changes in value, Unable to update");
                        noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                    else {
                        List<String> additionalInfo = new ArrayList<>();
                        additionalInfo.add(0, b.getQrcode());
                        String content = "";
                        String newSiteId = "";
                        content = "{" + "\"testingsitename\":\"" + ((OnSiteTestingBooking) b).getTestingSite().getName()  + "\", \"testingsiteid\":\"" + ((OnSiteTestingBooking) b).getTestingSite().getId() + "\", \"starttime\": \"" + b.getStartTime() + "\" } ";
                        newSiteId = testingSite.getValue().getId();
                        List<String> history = b.getHistory();

                        try {
                            HttpResponse<String> response = BookingCollection.updateBooking(b.getBookingId(), additionalInfo, history, content, startTime.getValue().format(DateTimeFormatter.ISO_DATE_TIME), newSiteId);
                            if (response.statusCode() == 200){
                                Notification noti = Notification.show("Update Success");
                                noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                            }
                            else
                                throw new Exception(response.body());
                            this.reloadForm();
                        } catch (Exception exception) {
                            System.out.println(exception);
                            Notification noti = Notification.show("Update Failed");
                            noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }
                    }
            });
        }
        testingSite.setItemLabelGenerator(TestingSite::getName);
        testingSite.setValue(((OnSiteTestingBooking) b).getTestingSite());
    }

    private void reloadForm() {
        this.removeAll();
        accordion = new Accordion();
        this.addAccordion();
        this.add(accordion);
    }

    /**
     * Helper function to check if booking is COMPLETED, CANCELLED or EXPIRED
     * @param b Booking
     * @return true if booking is not INITIATED or booking has EXPIRED, false if booking is valid
     */
    private boolean isInvalidStatus(Booking b){
        boolean valid = false;
        if (!b.getStatus().equals("INITIATED") || LocalDateTime.now().compareTo(ZonedDateTime.parse(b.getStartTime()).toLocalDateTime()) > 0){
            valid = true;
        }
        return valid;
    }

    private void addCancelButton(FormLayout form, String bookingId){
        Button cancelBooking = new Button("Cancel");
        cancelBooking.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        cancelBooking.addClickListener(e -> {
            try {
                HttpResponse<String> response = BookingCollection.cancelBooking(bookingId);
                if (response.statusCode() == 200){
                    Notification noti = Notification.show("Cancellation Success");
                    noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
                else
                    throw new Exception();
                this.reloadForm();
            }
            catch (Exception exception) {
                System.out.println("Cancellation failed " + exception);
            }
        });
        form.add(cancelBooking);
        form.setColspan(cancelBooking,2);
    }
}
