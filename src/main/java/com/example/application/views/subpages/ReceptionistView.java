package com.example.application.views.subpages;

import com.example.application.data.entity.BookingMethod.FacilityBookingMethod;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.example.application.data.entity.User.User;
import com.example.application.data.entity.User.UserCollection;
import com.example.application.views.subpages.layout.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "/receptionist")
@PageTitle("OnSite")
/**
 * This is the page for Receptionist to book user at site
 */
public class ReceptionistView extends VerticalLayout {

    private final Tab tabRegistration = new Tab("Registration");
    private final Tab tabVerifyPin = new Tab("Verify PIN") ;
    private final Tab tabScanQr = new Tab("RAT-kit verify QR");
    private final Tab tabBookings = new Tab("Booking Management");
    private final Tab tabModifyByPhone = new Tab("Modify Booking By Phone");
    private final Tabs mainTabs = new Tabs(tabRegistration,tabVerifyPin,tabScanQr,tabBookings,tabModifyByPhone);
    private final VerticalLayout mainLayout = new VerticalLayout();

    /**
     * Populating page with components based on tab selected
     */
    public ReceptionistView(){
        mainLayout.add(new ReceptionistBookingLayout());
        mainTabs.addSelectedChangeListener(event -> {
            mainLayout.removeAll();
            if (event.getSelectedTab().equals(tabRegistration)) {
                mainLayout.add(new ReceptionistBookingLayout());
            }
            else if (event.getSelectedTab().equals(tabVerifyPin)) {
                mainLayout.add(new PinVerifyLayout());
            }
            else if (event.getSelectedTab().equals(tabScanQr)) {
                mainLayout.add(new QrVerifyLayout());
            }
            else if (event.getSelectedTab().equals(tabBookings)) {
                mainLayout.add(new BookingManagementLayout());
            }
            else if (event.getSelectedTab().equals(tabModifyByPhone)) {
                mainLayout.add(new ModifyBookingByPhoneLayout());
            }
        });
        NotificationDialog notification = new NotificationDialog();
        if (notification.newNotificationExist())
            notification.open();
        setMargin(false);
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(new ProfileAvatarLayout(),mainTabs,mainLayout);
    }

}
