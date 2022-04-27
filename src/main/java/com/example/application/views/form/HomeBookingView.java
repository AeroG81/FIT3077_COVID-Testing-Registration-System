package com.example.application.views.form;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.OnlineTesting;
import com.example.application.data.entity.BookingMethod.BookingMethod;
import com.example.application.data.entity.BookingMethod.SystemBookingMethod;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.example.application.data.entity.User.User;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "/homebooking")
@PageTitle("SystemBookingMethod")
public class HomeBookingView extends VerticalLayout {

    private final TestingSiteCollection collection = new TestingSiteCollection();

    private DateTimePicker startTime;
    private final TextArea notes = new TextArea("Notes");
    private Button submitRegistration;

    private final VerticalLayout mainLayout = new VerticalLayout();
    private final Tab tabSiteBooking = new Tab("Book for Site Testing");
    private final Tab tabHomeBooking = new Tab("Book for Home Testing");
    private final Tab tabVerifyPin = new Tab("Verify PIN");
    private final Tabs mainTabs = new Tabs(tabHomeBooking, tabSiteBooking, tabVerifyPin);

    private final ConfirmDialog dialog = new ConfirmDialog();

    public HomeBookingView(){
        tabSiteBooking.setEnabled(false);

        this.configureDateTimePicker();
        this.configureRegistrationButton();

        mainLayout.add(startTime,notes,submitRegistration);
        mainTabs.addSelectedChangeListener(event -> {
            this.clearFields();
            mainLayout.removeAll();
            if (event.getSelectedTab().equals(tabHomeBooking)) {
                mainLayout.add(startTime,notes,submitRegistration);
            }
            else if (event.getSelectedTab().equals(tabVerifyPin)) {
                mainLayout.add(new PinVerifyLayout());
            }
        });
        setMargin(false);
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(mainTabs,mainLayout);
    }

    private void configureRegistrationButton(){
        submitRegistration = new Button("Submit");
        submitRegistration.addClickListener(e -> {
            dialog.setHeader("Confirm Appointment");
            dialog.setText("Do you want to an online testing appointment at "+startTime.getValue().toLocalDate()+ " " +startTime.getValue().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
            dialog.setRejectable(true);
            dialog.setRejectText("Discard");

            dialog.setConfirmText("Save");
            dialog.addConfirmListener(event -> {
                // send POST to make booking
                User user = null;
                try {
                    HttpResponse response = new SystemBookingMethod().addBooking(startTime.getValue().toString(),user,notes.getValue());
                    System.out.println(response.body());
                }
                catch (Exception exception) {
                    System.out.println(exception);
                }
            });
            dialog.open();
        });
    }

    private void clearFields(){
        notes.clear();
    }

    private void configureDateTimePicker() {
        startTime = new DateTimePicker();
        startTime.setLabel("Appointment Date and Time");
        startTime.setAutoOpen(true);
        startTime.setMin(LocalDateTime.now());
        startTime.setValue(LocalDateTime.now());
        startTime.setMax(LocalDateTime.now().plusDays(90));
    }


}
