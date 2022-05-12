package com.example.application.views.subpages;

import com.example.application.data.entity.BookingMethod.SystemBookingMethod;
import com.example.application.data.entity.User.Resident;
import com.example.application.data.entity.User.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.awt.*;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "/systembooking")
@PageTitle("System")
/**
 * This is the page for User booking via System
 */
public class SystemBookingView extends VerticalLayout {
    private final VerticalLayout mainLayout = new VerticalLayout();
    private final Tab tabSiteBooking = new Tab("Book for Site Testing");
    private final Tab tabHomeBooking = new Tab("Book for Home Testing");
    private final Tab tabVerifyPin = new Tab("Verify PIN");
    private final Tab tabTestingSites = new Tab("| Testing Sites |");
    private final Tabs mainTabs = new Tabs(tabHomeBooking, tabSiteBooking, tabVerifyPin, tabTestingSites);

    /**
     * Populating page with components based on tab selected
     */
    public SystemBookingView(){

        // Change layout based on selected tab
        mainLayout.add(new HomeBookingLayout());
        mainTabs.addSelectedChangeListener(event -> {

            mainLayout.removeAll();
            if (event.getSelectedTab().equals(tabHomeBooking)) {
                mainLayout.add(new HomeBookingLayout());
            }
            else if (event.getSelectedTab().equals(tabVerifyPin)) {
                mainLayout.add(new PinVerifyLayout());
            }
            else if (event.getSelectedTab().equals(tabSiteBooking)) {
                mainLayout.add(new SiteBookingLayout());
            }
            else if (event.getSelectedTab().equals(tabTestingSites)) {
                UI.getCurrent().navigate("testingsite");
            }
        });
        setMargin(false);
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(new ProfileLayout(),mainTabs,mainLayout);
    }
}
