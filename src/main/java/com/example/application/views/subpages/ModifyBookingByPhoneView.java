package com.example.application.views.subpages;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.User.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Phone Call Booking Modification | Vaadin CRM")
@Route(value = "/phonecall")
public class ModifyBookingByPhoneView extends VerticalLayout {
    private VerticalLayout modDialogLayout = new VerticalLayout();
    private VerticalLayout bookingIDandPINDialogLayout = new VerticalLayout();
    private Dialog bookingIDandPINDialog = new Dialog();

    private TextField bookingID = new TextField();

    private TextField smsPin = new TextField();

    private H1 dialogHeader = new H1("Enter Customer's Booking ID and PIN: ");

    private Button verifyBookingIDandPINButton = new Button("Verify");
    BookingCollection bc = new BookingCollection();

    public ModifyBookingByPhoneView(){
        this.checkBookingIDandPIN();
        bookingIDandPINDialog = new Dialog();

        bookingID.setLabel("Booking ID");
        bookingID.setRequired(true);

        smsPin.setLabel("Booking PIN");
        smsPin.setRequired(true);

        bookingIDandPINDialogLayout.add(dialogHeader, bookingID, smsPin, verifyBookingIDandPINButton);

        bookingIDandPINDialog.add(bookingIDandPINDialogLayout);
        bookingIDandPINDialog.open();


        add(bookingIDandPINDialog);
    }

    public void checkBookingIDandPIN(){
        verifyBookingIDandPINButton.addClickListener(e -> {
            Booking verifyBookingIDandPIN = bc.verifyBookingIDandPin(bookingID.getValue(), smsPin.getValue());
            if (verifyBookingIDandPIN == null){
                Notification.show("Invalid Booking ID and/or PIN");
            }
            else{
                if (verifyBookingIDandPIN.getStatus() != "COMPLETED"){
                    
                }
            }
        });
    }


}
