package com.example.application.views.subpages.layout;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.Booking.HomeTestingBooking;
import com.example.application.data.entity.Booking.OnSiteTestingBooking;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.select.Select;

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
    private Select<String> historySelect = new Select<>();
    private ArrayList<String> currentBookingHistory = new ArrayList<>();
    private RadioButtonGroup<String> bookingOptions = new RadioButtonGroup<>();
    private List<Booking> gridBookingData = bookingCollection.getActiveAndCancelledBooking();

    public BookingManagementLayout() {
        this.configureRadioGroup();
        this.reloadForm();
        this.configureStartTime();
        this.populateTestingSiteComboBox();
        this.configureHistoryRevert();
        this.configureEditorForm();
        this.configureEditorDialog();
    }

    private void configureRadioGroup(){
        bookingOptions.setLabel("Show Booking");
        bookingOptions.setItems("Show All", "Show Active and Cancelled");
        bookingOptions.setValue("Show Active and Cancelled");
        bookingOptions.addValueChangeListener(e->{
            if (bookingOptions.getValue().equals("Show Active and Cancelled")){
                gridBookingData = bookingCollection.getActiveAndCancelledBooking();
            }
            else{
                gridBookingData = bookingCollection.getCollection();
            }
            this.reloadForm();
        });
    }

    private void configureStartTime(){
        startTime.setMin(LocalDateTime.now());
        startTime.setMax(LocalDateTime.now().plusDays(90));
    }

    private void configureHistoryRevert(){
        historySelect.setLabel("History");
    }

    private void updateRevertOptions() {
        historySelect.clear();
        currentBookingHistory.clear();
        if (selectedBooking.getHistory().get(0) != null && !selectedBooking.getHistory().get(0).equals("null"))
            currentBookingHistory.add(selectedBooking.getHistory().get(0));
        if (selectedBooking.getHistory().get(1) != null && !selectedBooking.getHistory().get(1).equals("null"))
            currentBookingHistory.add(selectedBooking.getHistory().get(1));
        if (selectedBooking.getHistory().get(2) != null && !selectedBooking.getHistory().get(2).equals("null"))
            currentBookingHistory.add(selectedBooking.getHistory().get(2));
        currentBookingHistory.add(0, "current");
        historySelect.setItems(currentBookingHistory);
        historySelect.setValue("current");
    }

    private void configureEditorDialog() {
        HorizontalLayout buttonLayout = configureButtonLayout();
        editorDialog.add(editorForm, buttonLayout);
        editorDialog.setWidth("1200px");
    }

    private HorizontalLayout configureButtonLayout() {

        Button closeButton = new Button("Close",e -> editorDialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

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
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

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
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Button historyRevertButton = new Button("Revert History",e -> {
            if (!historySelect.getValue().equals("current")) {
                List<String> additionalInfo = new ArrayList<>();
                additionalInfo.add(0, selectedBooking.getQrcode());
                if (selectedBooking.getClass().equals(HomeTestingBooking.class))
                    additionalInfo.add(1, ((HomeTestingBooking) selectedBooking).getUrl());
                int index = currentBookingHistory.indexOf(historySelect.getValue());
                ObjectNode jsonNode = null;
                try {
                    jsonNode = new ObjectMapper().readValue(historySelect.getValue(), ObjectNode.class);
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
                        HttpResponse<String> response = BookingCollection.revertBooking(selectedBooking.getBookingId(), additionalInfo, selectedBooking.getHistory(), historySelect.getValue(), index);
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
        historyRevertButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout buttonLayout = new HorizontalLayout(historyRevertButton,deleteButton, saveButton, closeButton);
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
        editorForm.setColspan(historySelect, 2);
        editorForm.add(pin, qr, url, testingSite, startTime, historySelect);
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

        gridBookingData.sort(new Comparator<Booking>() {
            @Override
            public int compare(Booking o1, Booking o2) {
                return ZonedDateTime.parse(o2.getLastUpdateTime()).toLocalDateTime().compareTo(ZonedDateTime.parse(o1.getLastUpdateTime()).toLocalDateTime());
            }
        });
        grid.setItems(gridBookingData);
        grid.addSelectionListener(selection -> {
            Optional<Booking> optionalPerson = selection.getFirstSelectedItem();
            optionalPerson.ifPresent(booking -> {
                selectedBooking = booking;
                pin.setText("PIN CODE: " + selectedBooking.getSmsPin());
                qr.setText("QR CODE: " + selectedBooking.getQrcode());
                startTime.setValue(ZonedDateTime.parse(selectedBooking.getStartTime()).toLocalDateTime());
                if (selectedBooking.getClass().equals(OnSiteTestingBooking.class)) {
                    testingSite.setValue(((OnSiteTestingBooking) selectedBooking).getTestingSite());
                    url.setText("URL: " + null);
                    testingSite.setEnabled(true);
                } else {
                    url.setText("URL: " + ((HomeTestingBooking) selectedBooking).getUrl());
                    testingSite.setValue(null);
                    testingSite.setEnabled(false);
                }
                this.updateRevertOptions();
                editorDialog.open();
            });
        });
        this.removeAll();
        grid.setHeight("900px");
        this.add(bookingOptions, grid);
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
