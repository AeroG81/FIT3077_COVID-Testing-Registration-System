package com.example.application.views.subpages.layout;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.Booking.HomeTestingBooking;
import com.example.application.data.entity.Booking.OnSiteTestingBooking;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class BookingManagementLayout extends VerticalLayout {

    private TestingSiteCollection collection = new TestingSiteCollection();
    private ComboBox<TestingSite> testingSite = new ComboBox<>("TestingSite");
    private DateTimePicker startTime = new DateTimePicker();
    private FormLayout editorForm = new FormLayout();
    private Dialog editorDialog = new com.vaadin.flow.component.dialog.Dialog();
    private Label pin = new Label();
    private Label qr = new Label();
    private Label url = new Label();
    private Booking selectedBooking = null;
    private BookingCollection bookingCollection = new BookingCollection();
    private Grid<Booking> grid = null;

    public BookingManagementLayout() {
        this.reloadForm();
        this.populateTestingSiteComboBox();
        this.configureEditorForm();
        startTime.setMin(LocalDateTime.now());
        startTime.setMax(LocalDateTime.now().plusDays(90));
        this.configureEditorDialog();
    }

    private void configureEditorDialog() {
        HorizontalLayout buttonLayout = configureButtonLayout();
        editorDialog.add(editorForm, buttonLayout);
        editorDialog.setWidth("1000px");
    }

    private HorizontalLayout configureButtonLayout() {

        Button closeButton = new Button("Close");
        closeButton.addClickListener(e -> editorDialog.close());

        Button saveButton = new Button("Save", e -> {
            if (isInvalidStatus(selectedBooking)) {
                Notification noti = Notification.show("Booking are CANCELLED, COMPLETED or EXPIRED, unable to update");
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else {

                if (selectedBooking.getClass().equals(HomeTestingBooking.class) && (startTime.getValue().toLocalTime().getHour() < 8 || startTime.getValue().toLocalTime().getHour() >= 21)) {
                    Notification noti = Notification.show("Online testing only available during 0800 - 2100");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else if (selectedBooking.getClass().equals(OnSiteTestingBooking.class) && (startTime.getValue().toLocalTime().getHour() < Integer.parseInt(testingSite.getValue().getOperationTime().substring(0, 2)) || startTime.getValue().toLocalTime().getHour() >= Integer.parseInt(testingSite.getValue().getOperationTime().substring(7, 9)))) {
                    Notification noti = Notification.show("Booking time is not within operation hour");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else if (selectedBooking.getClass().equals(HomeTestingBooking.class) && (testingSite.getValue()!=null)){
                    Notification noti = Notification.show("This is an online testing, testing site should be empty");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else if (selectedBooking.getClass().equals(OnSiteTestingBooking.class) && (testingSite.getValue()==null)){
                    Notification noti = Notification.show("This is an onsite testing, testing site should not be empty");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else if (selectedBooking.getClass().equals(HomeTestingBooking.class) && (startTime.getValue().equals(ZonedDateTime.parse(selectedBooking.getStartTime()).toLocalDateTime()))) {
                    Notification noti = Notification.show("No changes in value, Unable to update");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else if (selectedBooking.getClass().equals(OnSiteTestingBooking.class) && (startTime.getValue().equals(ZonedDateTime.parse(selectedBooking.getStartTime()).toLocalDateTime()) && testingSite.getValue().getId().equals(((OnSiteTestingBooking) selectedBooking).getTestingSite().getId()))) {
                    Notification noti = Notification.show("No changes in value, Unable to update");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    List<String> additionalInfo = new ArrayList<>();
                    additionalInfo.add(0, selectedBooking.getQrcode());
                    if (selectedBooking.getClass().equals(HomeTestingBooking.class))
                        additionalInfo.add(1, ((HomeTestingBooking) selectedBooking).getUrl());
                    String content;
                    String newSiteId;
                    if (this.selectedBooking.getClass().equals(OnSiteTestingBooking.class)){
                        content = "{" + "\"testingsitename\":\"" + ((OnSiteTestingBooking) selectedBooking).getTestingSite().getName()  + "\", \"testingsiteid\":\"" + ((OnSiteTestingBooking) selectedBooking).getTestingSite().getId() + "\", \"starttime\": \"" + selectedBooking.getStartTime() + "\" } ";
                        newSiteId = testingSite.getValue().getId();
                    } else {
                        content = "{"  + "\"testingsitename\":" + null  + ", \"testingsiteid\":" + null + ", \"starttime\": \"" + selectedBooking.getStartTime() + "\" } ";
                        newSiteId = null;
                    }

                    List<String> history = selectedBooking.getHistory();

                    try {
                        HttpResponse<String> response = BookingCollection.updateBooking(selectedBooking.getBookingId(), additionalInfo, history, content, startTime.getValue().format(DateTimeFormatter.ISO_DATE_TIME), newSiteId);
                        if (response.statusCode() == 200){
                            Notification noti = Notification.show("Update Success");
                            noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        }
                        else
                            throw new Exception(response.body());
                        editorDialog.close();
                        this.reloadForm();
                    } catch (Exception exception) {
                        System.out.println(exception);
                        Notification noti = Notification.show("Update Failed");
                        noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }
            }
            editorDialog.close();
        });

        Button deleteButton = new Button("Delete", e -> {
            try {
                HttpResponse<String> response = BookingCollection.deleteBooking(selectedBooking.getBookingId());
                if (response.statusCode() == 204) {
                    Notification noti = Notification.show("Delete Success");
                    noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } else
                    throw new Exception(response.body());
                editorDialog.close();
                this.reloadForm();
            } catch (Exception exception) {
                Notification noti = Notification.show("Delete Failed");
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                System.out.println("Receptionist Delete Failed " + exception);
            }
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(deleteButton, saveButton, closeButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        return buttonLayout;
    }

    private void configureEditorForm() {
        editorForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 2)
        );
        editorForm.setColspan(pin, 2);
        editorForm.setColspan(qr, 2);
        editorForm.setColspan(url, 2);
        editorForm.add(pin, qr, url, testingSite, startTime);
    }

    private void populateTestingSiteComboBox() {
        testingSite.setItems(collection.getCollection());
        testingSite.setItemLabelGenerator(TestingSite::getName);
    }

    private void reloadForm() {
        bookingCollection.refreshCollection();
        grid = new Grid<>(Booking.class, false);
        grid.addColumn(b -> b.getCustomer().getGivenName()).setHeader("Given name").setTextAlign(ColumnTextAlign.START);
        grid.addColumn(b -> b.getCustomer().getFamilyName()).setHeader("Family name").setTextAlign(ColumnTextAlign.START);
        grid.addColumn(b -> b.getCustomer().getUserName()).setHeader("Username").setTextAlign(ColumnTextAlign.START);
        grid.addColumn(b -> ZonedDateTime.parse(b.getStartTime()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))).setHeader("Booking DateTime").setAutoWidth(true).setTextAlign(ColumnTextAlign.END).setSortable(true).setComparator(new Comparator<Booking>() {
            @Override
            public int compare(Booking o1, Booking o2) {
                return ZonedDateTime.parse(o2.getStartTime()).toLocalDateTime().compareTo(ZonedDateTime.parse(o1.getStartTime()).toLocalDateTime());
            }
        });
        grid.addColumn(b -> { if (b.getClass().equals(OnSiteTestingBooking.class)) { return ((OnSiteTestingBooking) b).getTestingSite().getName(); } else { return "-"; }}).setHeader("Testing site").setAutoWidth(true).setTextAlign(ColumnTextAlign.START);
        grid.addColumn(b -> b.getSmsPin()).setHeader("PIN");
        grid.addColumn(b -> b.getQrcode()).setHeader("QR").setAutoWidth(true);
        grid.addColumn(b -> {
            if (LocalDateTime.now().compareTo(ZonedDateTime.parse(b.getStartTime()).toLocalDateTime()) > 0 && b.getStatus().equals("INITIATED"))
                return "EXPIRED";
            else
                return b.getStatus();
        }).setHeader("Status").setAutoWidth(true).setTextAlign(ColumnTextAlign.END).setSortable(true);
        grid.addColumn(b -> ZonedDateTime.parse(b.getLastUpdateTime()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm"))).setHeader("Last Modified").setAutoWidth(true).setTextAlign(ColumnTextAlign.START).setSortable(true).setComparator(new Comparator<Booking>() {
            @Override
            public int compare(Booking o1, Booking o2) {
                return ZonedDateTime.parse(o2.getLastUpdateTime()).toLocalDateTime().compareTo(ZonedDateTime.parse(o1.getLastUpdateTime()).toLocalDateTime());
            }
        });
        List<Booking> bookings = bookingCollection.getCollection();
        bookings.sort(new Comparator<Booking>() {
            @Override
            public int compare(Booking o1, Booking o2) {
                return ZonedDateTime.parse(o2.getLastUpdateTime()).toLocalDateTime().compareTo(ZonedDateTime.parse(o1.getLastUpdateTime()).toLocalDateTime());
            }
        });
        grid.setItems(bookings);
        grid.addSelectionListener(selection -> {
            Optional<Booking> optionalPerson = selection.getFirstSelectedItem();
            optionalPerson.ifPresent(booking -> {
                pin.setText("PIN CODE: " + booking.getSmsPin());
                qr.setText("QR CODE: " + booking.getQrcode());
                startTime.setValue(ZonedDateTime.parse(booking.getStartTime()).toLocalDateTime());
                if (booking.getClass().equals(OnSiteTestingBooking.class)) {
                    testingSite.setValue(((OnSiteTestingBooking) booking).getTestingSite());
                    url.setText("URL: " + null);
                    testingSite.setEnabled(true);
                } else {
                    url.setText("URL: " + ((HomeTestingBooking) booking).getUrl());
                    testingSite.setValue(null);
                    testingSite.setEnabled(false);
                }
                selectedBooking = booking;
                editorDialog.open();
            });
        });
        this.removeAll();
        grid.setHeight("900px");
        this.add(grid);
    }

    /**
     * Helper function to check if booking is COMPLETED, CANCELLED or EXPIRED
     *
     * @param b Booking
     * @return true if booking is not INITIATED or booking has EXPIRED, false if booking is valid
     */
    private boolean isInvalidStatus(Booking b) {
        boolean valid = false;
        if (!b.getStatus().equals("INITIATED") || LocalDateTime.now().compareTo(ZonedDateTime.parse(b.getStartTime()).toLocalDateTime()) > 0) {
            valid = true;
        }
        return valid;
    }

}
