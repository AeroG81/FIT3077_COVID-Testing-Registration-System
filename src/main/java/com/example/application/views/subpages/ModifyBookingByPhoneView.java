package com.example.application.views.subpages;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.example.application.data.entity.User.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Date;
import java.util.Objects;

@PageTitle("Phone Call Booking Modification | Vaadin CRM")
@Route(value = "/phonecall")
public class ModifyBookingByPhoneView extends VerticalLayout {
    private VerticalLayout bookingIDandPINDialogLayout = new VerticalLayout();

    private VerticalLayout mainLayoutPhoneCall = new VerticalLayout();

    private HorizontalLayout verifyUserLayout = new HorizontalLayout();

    private HorizontalLayout pickVenueAndTime = new HorizontalLayout();

    private Dialog bookingIDandPINDialog = new Dialog();

    ComboBox<TestingSite> newBookingVenue = new ComboBox<>("Testing Sites");

    private TimePicker newBookingTime = new TimePicker();

    private TextField bookingIDTextField = new TextField();

    private TextField smsPinTextField = new TextField();

    private TextField residentUserIdTextField = new TextField();

    private H1 dialogHeader = new H1("Enter Customer's Booking ID and PIN: ");

    private Button verifyBookingIDandPINButton = new Button("Verify");

    private Button changeBookingButton = new Button("Modify Different Booking");

    private Button verifyUserButton = new Button("Verify User");

    BookingCollection bc = new BookingCollection();

    TestingSiteCollection tc = new TestingSiteCollection();

    Booking bookingToModify = null;

    public ModifyBookingByPhoneView(){
        this.checkBookingIDandPIN();
        this.verifyUser();

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

        changeBookingButton.addClickListener(e-> {
            bookingIDandPINDialog.open();
            verifyUserButton.setEnabled(false);
        });
        changeBookingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        // Verify resident's identity
        residentUserIdTextField.setLabel("Enter Resident's ID");
        residentUserIdTextField.setRequired(true);
        residentUserIdTextField.setClearButtonVisible(true);

        verifyUserButton.setEnabled(false);

        verifyUserLayout.add(residentUserIdTextField, verifyUserButton);
        verifyUserLayout.setAlignItems(Alignment.END);

        // Venue and Time picker
//        newBookingVenue.setItems(tc.getCollection());
//        newBookingVenue.setItemLabelGenerator(Country::getName);
//        add(comboBox);

        newBookingTime.setLabel("Choose new booking time: ");
        newBookingTime.setStep(Duration.ofSeconds(1));
        newBookingTime.setValue(LocalTime.of(15, 45, 8));

        mainLayoutPhoneCall.add(changeBookingButton, verifyUserLayout);
        mainLayoutPhoneCall.setSizeFull();
        mainLayoutPhoneCall.setHorizontalComponentAlignment(Alignment.CENTER, changeBookingButton, verifyUserLayout);

        add(bookingIDandPINDialog, mainLayoutPhoneCall);
    }

    public void checkBookingIDandPIN(){
        verifyBookingIDandPINButton.addClickListener(e -> {
            bookingToModify = bc.verifyBookingIDandPin(bookingIDTextField.getValue(), smsPinTextField.getValue());
            if (bookingToModify == null){
                Notification.show("Invalid Booking ID and/or PIN");
            }
            else{
                if (bookingToModify.getStatus() != "COMPLETED" || bookingToModify.getStatus() != "CANCELLED" ){
                    String bookingStartTime = bookingToModify.getStartTime();
                    bookingStartTime = bookingStartTime.substring(0, bookingStartTime.length()-1);
                    String[] dateTimeSplit = bookingStartTime.split("T");
                    Date bookingStartDateTime= null;
                    try {
                        bookingStartDateTime = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS").parse(dateTimeSplit[0] + dateTimeSplit[1]);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    Date nowDateTime = new Date();
                    if (bookingStartDateTime.compareTo(nowDateTime) > 0){
                        verifyUserButton.setEnabled(true);
                        Notification.show("Booking can be modified");
                        bookingIDandPINDialog.close();
                    }
                    else {
                        Notification.show("Booking is lapsed and cannot be modified anymore.");
                    }
                }
                else {
                    Notification.show("Booking is already completed or cancelled.");
                }
            }
        });
    }

    public void verifyUser(){
        verifyUserButton.addClickListener(e->{
            if (Objects.equals(bookingToModify.getCustomer().getId(), residentUserIdTextField.getValue())){
                Notification.show("Valid Resident");
            }
            else {
                Notification.show("Invalid Resident");
                Notification.show(bookingToModify.getCustomer().getId());
                Notification.show(residentUserIdTextField.getValue());
            }
        });
    }

}
