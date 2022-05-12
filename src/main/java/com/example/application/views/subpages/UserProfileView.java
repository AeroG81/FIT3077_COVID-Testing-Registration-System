package com.example.application.views.subpages;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "/userprofile")
@PageTitle("Profile")
/**
 * This is the page for User booking via System
 */
public class UserProfileView extends VerticalLayout {
    private final VerticalLayout mainLayout = new VerticalLayout();
    private final Tab tabUserProfile = new Tab("UserProfile");
    private final Tab tabActiveBookings = new Tab("Active Bookings");
    private final Tab tabHomeBooking = new Tab("Main Page");
    private final Tabs mainTabs = new Tabs(tabUserProfile, tabActiveBookings, tabHomeBooking);

    public UserProfileView(){
        // Change layout based on selected tab
        mainLayout.add(new UserProfileLayout());
        mainTabs.addSelectedChangeListener(event -> {
            mainLayout.removeAll();
            if (event.getSelectedTab().equals(tabUserProfile)) {
                mainLayout.add(new UserProfileLayout());
            }
            else if (event.getSelectedTab().equals(tabActiveBookings)) {
                mainLayout.add(new PinVerifyLayout());
            }
            else if (event.getSelectedTab().equals(tabHomeBooking)) {
                UI.getCurrent().navigate("systembooking");
            }
        });
        setMargin(false);
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(new ProfileLayout(),mainTabs,mainLayout);
    }
}
