package com.example.application.views.form;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.BookingMethod.BookingMethod;
import com.example.application.data.entity.Booking.BookingCollection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

public class PinVerifyLayout extends VerticalLayout {
    private Button submitVerification;
    private final IntegerField verifyPin = new IntegerField("PIN");
    private final Dialog dialog = new Dialog();
    private final Label label = new Label();

    public PinVerifyLayout(){
        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.add(closeButton,label);
        this.configureVerifyButton();
        add(verifyPin,submitVerification,dialog);
    }

    private void configureVerifyButton(){
        verifyPin.addValueChangeListener(e -> {
            if (!verifyPin.isEmpty() && !verifyPin.isInvalid())
                submitVerification.setEnabled(true);
            else
                submitVerification.setEnabled(false);
        });
        submitVerification = new Button("Verify");
        submitVerification.setEnabled(false);
        submitVerification.addClickListener(e -> {
            if (!verifyPin.isEmpty() && !verifyPin.isInvalid()){
                BookingCollection collection = new BookingCollection();
                Booking userBookingMethod = collection.verifyPin(verifyPin.getValue().toString());
                label.removeAll();
                if (userBookingMethod !=null){
                    label.add(userBookingMethod.toString());
                    dialog.open();
                }
                else {
                    Notification noti = Notification.show("Error! Invalid PIN");
                    noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
            else {
                Notification noti = Notification.show("Error! Invalid PIN");
                noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }

}
