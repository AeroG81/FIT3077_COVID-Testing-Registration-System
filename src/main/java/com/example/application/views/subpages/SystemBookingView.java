package com.example.application.views.subpages;

import com.example.application.views.subpages.layout.*;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

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
    private final Tab tabVerifyBookingId = new Tab("Verify Booking ID");
    private final Tab tabTestingSites = new Tab("| Testing Sites |");
    private final Tabs mainTabs = new Tabs(tabHomeBooking, tabSiteBooking, tabVerifyPin, tabVerifyBookingId, tabTestingSites);

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
            else if (event.getSelectedTab().equals(tabVerifyBookingId)) {
                mainLayout.add(new BookingIdVerifyLayout());
            }
            else if (event.getSelectedTab().equals(tabSiteBooking)) {
                mainLayout.add(new OnSiteBookingLayout());
            }
            else if (event.getSelectedTab().equals(tabTestingSites)) {
                UI.getCurrent().navigate("testingsite");
            }
        });
        setMargin(false);
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(new ProfileAvatarLayout(),mainTabs,mainLayout);
    }
}
