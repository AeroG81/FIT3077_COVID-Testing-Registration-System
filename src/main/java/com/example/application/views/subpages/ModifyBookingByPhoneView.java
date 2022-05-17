package com.example.application.views.subpages;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.Booking.OnSiteTesting;
import com.example.application.data.entity.Booking.OnlineTesting;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@PageTitle("Phone Call Booking Modification | Vaadin CRM")
@Route(value = "/phonecall")
public class ModifyBookingByPhoneView extends VerticalLayout {
    private VerticalLayout bookingIDandPINDialogLayout = new VerticalLayout();

    private VerticalLayout mainLayoutPhoneCallLayout = new VerticalLayout();

    private HorizontalLayout verifyUserLayout = new HorizontalLayout();

    private HorizontalLayout pickVenueLayout = new HorizontalLayout();

    private HorizontalLayout pickDatetimeLayout = new HorizontalLayout();

    private Dialog bookingIDandPINDialog = new Dialog();

    private ComboBox<TestingSite> newBookingVenue = new ComboBox<>("Testing Sites");

    private DateTimePicker newBookingTime = new DateTimePicker();

    private TextField bookingIDTextField = new TextField();

    private TextField smsPinTextField = new TextField();

    private TextField customerUserIdTextField = new TextField();

    private H1 dialogHeader = new H1("Enter Customer's Booking ID and PIN: ");

    private Button verifyBookingIDandPINButton = new Button("Verify");

    private Button changeBookingButton = new Button("Modify Different Booking");

    private Button modifyBookingButton = new Button("Modify Booking");

    private Button revertBookingButton = new Button("Revert to Last Change");

    private Button verifyUserButton = new Button("Verify User");
    private MultiSelectListBox<String> bookingContent;
    private final H2 title = new H2("Modify Bookings Through Phone Calls");

    private final H3 bookingDetailsHeader = new H3("Booking Details");

    private final Hr hr1 = new Hr();

    private String bookingIDList = "Booking ID: N/A";
    private String customerIDList ="Customer ID: N/A";
    private String customerFullNameList = "Customer Full Name: N/A";
    private String bookingTestingSiteList = "Booking Testing Site: N/A";
    private String bookingStartTimeList = "Booking Start Time: N/A";

    LocalDateTime nowDateTime = LocalDateTime.now();

    BookingCollection bc = new BookingCollection();

    TestingSiteCollection tc = new TestingSiteCollection();

    Booking bookingToModify = null;

    public ModifyBookingByPhoneView(){
        this.changeBookingToModify();
        this.checkBookingIDandPIN();
        this.verifyUser();
        this.configureTimeAndVenuePicker();
        this.populateVenuePicker();
        this.modifyBooking();

        // Verifying Booking dialog
        bookingIDandPINDialog = new Dialog();

        bookingIDTextField.setLabel("Booking ID");
        bookingIDTextField.setRequired(true);
        bookingIDTextField.setClearButtonVisible(true);

        smsPinTextField.setLabel("Booking PIN");
        smsPinTextField.setRequired(true);
        smsPinTextField.setClearButtonVisible(true);

        verifyBookingIDandPINButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        bookingIDandPINDialogLayout.add(dialogHeader, bookingIDTextField, smsPinTextField, verifyBookingIDandPINButton);

        bookingIDandPINDialog.add(bookingIDandPINDialogLayout);
        bookingIDandPINDialog.open();

        changeBookingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        // Verify customer's identity
        customerUserIdTextField.setLabel("1. Enter Customer's ID");
        customerUserIdTextField.setRequired(true);
        customerUserIdTextField.setClearButtonVisible(true);

        verifyUserButton.setEnabled(false);

        verifyUserLayout.add(customerUserIdTextField, verifyUserButton);
        verifyUserLayout.setAlignItems(Alignment.END);

        modifyBookingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        modifyBookingButton.setEnabled(false);

        pickVenueLayout.add(newBookingVenue);

        pickDatetimeLayout.add(newBookingTime);
        pickDatetimeLayout.setAlignItems(Alignment.END);

        bookingContent = new MultiSelectListBox<>();
        bookingContent.setItems(bookingIDList, customerIDList, customerFullNameList, bookingTestingSiteList, bookingStartTimeList);

        mainLayoutPhoneCallLayout.add(
                title,
                hr1,
                bookingDetailsHeader,
                bookingContent,
                changeBookingButton,
                verifyUserLayout,
                pickVenueLayout,
                pickDatetimeLayout,
                modifyBookingButton);

        mainLayoutPhoneCallLayout.setSizeFull();
        mainLayoutPhoneCallLayout.setHorizontalComponentAlignment(Alignment.CENTER, title, bookingDetailsHeader, bookingContent, changeBookingButton, verifyUserLayout, pickVenueLayout, pickDatetimeLayout, modifyBookingButton);

        add(bookingIDandPINDialog, mainLayoutPhoneCallLayout);
    }

    private void changeBookingToModify(){
        changeBookingButton.addClickListener(e-> {
            bookingIDandPINDialog.open();
            customerUserIdTextField.setEnabled(false);
            verifyUserButton.setEnabled(false);
            newBookingTime.setEnabled(false);
            newBookingVenue.setEnabled(false);
            modifyBookingButton.setEnabled(false);

            bookingIDList = "Booking ID: N/A";
            customerIDList ="Customer ID: N/A";
            customerFullNameList = "Customer Full Name: N/A";
            bookingTestingSiteList = "Booking Testing Site: N/A";
            bookingStartTimeList = "Booking Start Time: N/A";

            bookingContent.clear();
            bookingContent.setItems(bookingIDList, customerIDList, customerFullNameList, bookingTestingSiteList, bookingStartTimeList);

        });
    }

    private void checkBookingIDandPIN(){
        verifyBookingIDandPINButton.addClickListener(e -> {
            bookingToModify = bc.verifyBookingIdandPin(bookingIDTextField.getValue(), smsPinTextField.getValue());

            if (bookingToModify == null){
                Notification noti = Notification.show("Invalid Booking ID and/or PIN");
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else{
                if (bookingToModify instanceof OnSiteTesting){
                    bookingTestingSiteList = "Booking Testing Site: " + ((OnSiteTesting) bookingToModify).getTestingSite().getName();
                }
                else {
                    bookingTestingSiteList = "Booking Testing Site: N/A";
                }

                bookingIDandPINDialog.close();

                bookingIDList = "Booking ID: " + bookingToModify.getBookingId();
                customerIDList = "Customer ID: " + bookingToModify.getCustomer().getId();
                customerFullNameList = "Customer Full Name: " + bookingToModify.getCustomer().getGivenName() + " " + bookingToModify.getCustomer().getFamilyName();
                bookingStartTimeList = "Booking Start Time: " + bookingToModify.getStartTime();

                bookingContent.clear();

                LocalDateTime bookingStartDateTime = ZonedDateTime.parse(bookingToModify.getStartTime()).toLocalDateTime();
                bookingStartTimeList = "Booking Date Time: " + bookingStartDateTime.getDayOfMonth() + "-" + bookingStartDateTime.getMonthValue() + "-" +  bookingStartDateTime.getYear() + " " +
                        bookingStartDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                bookingContent.setItems(bookingIDList, customerIDList, customerFullNameList, bookingTestingSiteList, bookingStartTimeList);

                if (!isInvalidStatus(bookingToModify)){
                    customerUserIdTextField.setEnabled(true);
                    verifyUserButton.setEnabled(true);
                    Notification.show("Booking can be modified");
                    bookingIDandPINDialog.close();
                }
                else {
                    customerUserIdTextField.setEnabled(false);
                    verifyUserButton.setEnabled(false);

                    Notification noti = Notification.show("Booking cannot be modified.");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });
    }

    private void verifyUser() {
        verifyUserButton.addClickListener(e -> {
            if (Objects.equals(bookingToModify.getCustomer().getId(), customerUserIdTextField.getValue())) {
                Notification.show("Valid Customer");
                if (bookingToModify instanceof OnSiteTesting){
                    newBookingVenue.setEnabled(true);
                }
                newBookingTime.setEnabled(true);
                modifyBookingButton.setEnabled(true);
            } else {
                newBookingVenue.setEnabled(false);
                newBookingTime.setEnabled(false);
                modifyBookingButton.setEnabled(false);
                Notification noti = Notification.show("Invalid Customer");
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }

    /**
     * Configuring Combo Box of Testing Site
     */
    private void configureTimeAndVenuePicker(){
        newBookingTime = new DateTimePicker();
        newBookingTime.setLabel("3. Choose new date & time: ");
        newBookingTime.setEnabled(false);
        newBookingTime.setStep(Duration.ofMinutes(20));

        newBookingTime.setValue(LocalDateTime.of(nowDateTime.toLocalDate().getYear(), nowDateTime.toLocalDate().getMonthValue(), nowDateTime.toLocalDate().getDayOfMonth(), nowDateTime.toLocalTime().getHour(),0, 0).plusHours(1));

        newBookingTime.addValueChangeListener(e -> {
            if (newBookingTime.getValue().compareTo(nowDateTime) < 0){
                Notification noti = Notification.show("Time chosen is not valid. Choose another time.");
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        SimpleDateFormat twentyFourDateFormat = new SimpleDateFormat("HHmm");

        newBookingVenue = new ComboBox<>("2. Choose new venue: ");
        newBookingVenue.setEnabled(false);
        newBookingVenue.setRequired(true);
        newBookingVenue.setPlaceholder("Select venue");
        newBookingVenue.addValueChangeListener(f -> {
            try {
                newBookingTime.setMin(LocalDateTime.of(nowDateTime.getYear(), nowDateTime.getMonthValue(), nowDateTime.getDayOfMonth(), twentyFourDateFormat.parse(newBookingVenue.getValue().getOpenTime()).getHours(), twentyFourDateFormat.parse(newBookingVenue.getValue().getOpenTime()).getMinutes()));
                newBookingTime.setMax(LocalDateTime.of(nowDateTime.getYear(), nowDateTime.getMonthValue(), nowDateTime.getDayOfMonth(), twentyFourDateFormat.parse(newBookingVenue.getValue().getCloseTime()).getHours(), twentyFourDateFormat.parse(newBookingVenue.getValue().getCloseTime()).getMinutes()));
                newBookingTime.setValue(LocalDateTime.of(nowDateTime.getYear(), nowDateTime.getMonthValue(), nowDateTime.getDayOfMonth(), nowDateTime.getHour() ,0).plusHours(1));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * Populate the combo box of testing site
     */
    private void populateVenuePicker(){
        newBookingVenue.setItems(tc.getCollection());
        newBookingVenue.setItemLabelGenerator(TestingSite::getName);
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

    private void revertBooking(){

    }

    private void modifyBooking(){
        modifyBookingButton.addClickListener(e -> {
            if (bookingToModify instanceof OnSiteTesting){
                bc = new BookingCollection();
                bookingToModify = bc.getBookingsByBookingId(bookingToModify.getBookingId());

                this.modifyOnSiteBooking();

                bc = new BookingCollection();
                bookingToModify = bc.getBookingsByBookingId(bookingToModify.getBookingId());

                bookingIDList = "Booking ID: " + bookingToModify.getBookingId();
                customerIDList = "Customer ID: " + bookingToModify.getCustomer().getId();
                customerFullNameList = "Customer Full Name: " + bookingToModify.getCustomer().getGivenName() + " " + bookingToModify.getCustomer().getFamilyName();
                bookingTestingSiteList = "Booking Testing Site: " + ((OnSiteTesting) bookingToModify).getTestingSite().getName();
                LocalDateTime bookingStartDateTime = ZonedDateTime.parse(bookingToModify.getStartTime()).toLocalDateTime();
                bookingStartTimeList = "Booking Date Time: " + bookingStartDateTime.getDayOfMonth() + "-" + bookingStartDateTime.getMonthValue() + "-" +  bookingStartDateTime.getYear() + " " +
                        bookingStartDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                bookingContent.clear();
                bookingContent.setItems(bookingIDList, customerIDList, customerFullNameList, bookingTestingSiteList, bookingStartTimeList);
           }
           else if (bookingToModify instanceof OnlineTesting){
                bc = new BookingCollection();
                bookingToModify = bc.getBookingsByBookingId(bookingToModify.getBookingId());

                this.modifyOnlineBooking();

                bc = new BookingCollection();
                bookingToModify = bc.getBookingsByBookingId(bookingToModify.getBookingId());
                bookingIDList = "Booking ID: " + bookingToModify.getBookingId();
                customerIDList = "Customer ID: " + bookingToModify.getCustomer().getId();
                customerFullNameList = "Customer Full Name: " + bookingToModify.getCustomer().getGivenName() + " " + bookingToModify.getCustomer().getFamilyName();
                bookingTestingSiteList = "Booking Testing Site: " + "N/A";
                LocalDateTime bookingStartDateTime = ZonedDateTime.parse(bookingToModify.getStartTime()).toLocalDateTime();
                bookingStartTimeList = "Booking Date Time: " + bookingStartDateTime.getDayOfMonth() + "-" + bookingStartDateTime.getMonthValue() + "-" +  bookingStartDateTime.getYear() + " " +
                        bookingStartDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

                bookingContent.clear();
                bookingContent.setItems(bookingIDList, customerIDList, customerFullNameList, bookingTestingSiteList, bookingStartTimeList);
           }
        });
    }

    private void modifyOnlineBooking() {
        if (isInvalidStatus(bookingToModify)) {
            Notification noti = Notification.show("Booking cannot be modified");
            noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        else {
            if (newBookingTime.getValue().toLocalTime().getHour() < 8  || newBookingTime.getValue().toLocalTime().getHour() >= 21){
                Notification noti = Notification.show("Online testing only available during 0800 - 2100");
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            else if (newBookingTime.getValue().equals(ZonedDateTime.parse(bookingToModify.getStartTime()).toLocalDateTime())){
                Notification noti = Notification.show("No changes in value, Unable to update");
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
            else {
                List<String> additionalInfo = new ArrayList<>();
                additionalInfo.add(0, bookingToModify.getQrcode());
                String content = "";
                String newSiteId = null;

                additionalInfo.add(1, ((OnlineTesting) bookingToModify).getUrl());
                content = "{"  + "\"testingsitename\":" + null  + ", \"testingsiteid\":" + null + ", \"starttime\": \"" + bookingToModify.getStartTime() + "\" } ";
                List<String> history = bookingToModify.getHistory();
                try {
                    HttpResponse<String> response = BookingCollection.updateBooking(bookingToModify.getBookingId(), additionalInfo, history, content, newBookingTime.getValue().format(DateTimeFormatter.ISO_DATE_TIME), newSiteId);
                    if (response.statusCode() == 200){
                        Notification noti = Notification.show("Update Success");
                        noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    }
                    else
                        throw new Exception(response.body());
                } catch (Exception exception) {
                    System.out.println(exception);
                    Notification noti = Notification.show("Update Failed");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        }
    }

    private void modifyOnSiteBooking() {
        if (isInvalidStatus(bookingToModify)) {
            Notification noti = Notification.show("Booking cannot be modified");
            noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            if (newBookingVenue.getValue()!=null) {
                if (newBookingTime.getValue().toLocalTime().getHour() < Integer.parseInt(newBookingVenue.getValue().getOperationTime().substring(0, 2)) || newBookingTime.getValue().toLocalTime().getHour() >= Integer.parseInt(newBookingVenue.getValue().getOperationTime().substring(7, 9))) {
                    Notification noti = Notification.show("Booking time is not within operation hour");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else if (newBookingTime.getValue().equals(ZonedDateTime.parse(bookingToModify.getStartTime()).toLocalDateTime()) && newBookingVenue.getValue().getId().equals(((OnSiteTesting) bookingToModify).getTestingSite().getId())) {
                    Notification noti = Notification.show("No changes in value, Unable to update");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                } else {
                    List<String> additionalInfo = new ArrayList<>();
                    additionalInfo.add(0, bookingToModify.getQrcode());
                    String content = "";
                    String newSiteId = "";
                    content = "{" + "\"testingsitename\":\"" + ((OnSiteTesting) bookingToModify).getTestingSite().getName() + "\", \"testingsiteid\":\"" + ((OnSiteTesting) bookingToModify).getTestingSite().getId() + "\", \"starttime\": \"" + bookingToModify.getStartTime() + "\" } ";
                    newSiteId = newBookingVenue.getValue().getId();
                    List<String> history = bookingToModify.getHistory();

                    try {
                        HttpResponse<String> response = BookingCollection.updateBooking(bookingToModify.getBookingId(), additionalInfo, history, content, newBookingTime.getValue().format(DateTimeFormatter.ISO_DATE_TIME), newSiteId);
                        if (response.statusCode() == 200) {
                            Notification noti = Notification.show("Update Success");
                            noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        } else
                            throw new Exception(response.body());
                    } catch (Exception exception) {
                        System.out.println(exception);
                        Notification noti = Notification.show("Update Failed");
                        noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }
                }
            }
        }
    }

    
}
