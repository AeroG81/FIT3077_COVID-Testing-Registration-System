package com.example.application.views.Subpages;

import com.example.application.data.entity.BookingMethod.SystemBookingMethod;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
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
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "/systembooking")
@PageTitle("System")
public class HomeBookingView extends VerticalLayout {

    private DateTimePicker startTime;
    private final TextArea notes = new TextArea("Notes");
    private Button submitRegistration;
    private final RadioButtonGroup<String> needTestKit = new RadioButtonGroup<>();
    private final VerticalLayout mainLayout = new VerticalLayout();
    private final Tab tabSiteBooking = new Tab("Book for Site Testing");
    private final Tab tabHomeBooking = new Tab("Book for Home Testing");
    private final Tab tabVerifyPin = new Tab("Verify PIN");
    private final Tab tabTestingSites = new Tab("Testing Sites");
    private final Tabs mainTabs = new Tabs(tabHomeBooking, tabSiteBooking, tabVerifyPin, tabTestingSites);

    private final ConfirmDialog dialog = new ConfirmDialog();

    public HomeBookingView(){
        tabSiteBooking.setEnabled(false);

        this.configureDateTimePicker();
        this.configureRegistrationButton();
        this.configureRadioGroup();
        mainLayout.add(startTime,notes,needTestKit,submitRegistration);
        mainTabs.addSelectedChangeListener(event -> {
            this.clearFields();
            mainLayout.removeAll();
            if (event.getSelectedTab().equals(tabHomeBooking)) {
                mainLayout.add(startTime,notes,needTestKit,submitRegistration);
            }
            else if (event.getSelectedTab().equals(tabVerifyPin)) {
                mainLayout.add(new PinVerifyLayout());
            }
            else if (event.getSelectedTab().equals(tabTestingSites)) {
                UI.getCurrent().navigate("testingsite");
            }
        });
        setMargin(false);
        setPadding(true);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(mainTabs,mainLayout);
    }

    private void configureRadioGroup(){
        needTestKit.setLabel("Test Kit Needed");
        needTestKit.setItems("Yes", "No");
        needTestKit.setValue("No");
    }

    private void configureRegistrationButton(){
        submitRegistration = new Button("Submit");
        submitRegistration.addClickListener(e -> {
            if (startTime.getValue().toLocalTime().getHour() < 8  || startTime.getValue().toLocalTime().getHour() >= 21){
                Notification.show("Online testing only available during 0800 - 2100");
            }
            else {
                dialog.setHeader("Confirm Appointment");
                dialog.setText("Do you want to an online testing appointment at "+startTime.getValue().toLocalDate()+ " " +startTime.getValue().toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME));
                dialog.setRejectable(true);
                dialog.setRejectText("Discard");
                dialog.setConfirmText("Save");
                dialog.addConfirmListener(event -> {
                    // send POST to make booking
                    User user = new Resident(); // retrieve from website storage
                    ObjectNode mappedResponse = null;
                    try {
                        HttpResponse<String> response = new SystemBookingMethod().addBooking(startTime.getValue().toString(),user,notes.getValue());
                        mappedResponse = new ObjectMapper().readValue(response.body(), ObjectNode.class);
                        TextArea label = new TextArea();
                        Notification noti = Notification.show("Application submitted");
                        noti.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                        Dialog responseDialog = new Dialog();
                        label.setWidth("500px");
                        label.clear();
                        // need QR code display to user
                        if (needTestKit.getValue().equals("Yes"))
                            label.setValue(mappedResponse.toPrettyString());
                        // QR code not needed for user
                        else
                            label.setValue(mappedResponse.toPrettyString().replace("\"qrcode\" : \"" + mappedResponse.get("additionalInfo").get("qrcode").asText()+"\",",""));
                        Button closeButton = new Button(new Icon("lumo", "cross"), (ev) -> responseDialog.close());
                        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
                        responseDialog.add(closeButton,label);
                        responseDialog.open();
                    }
                    catch (Exception exception) {
                        Notification noti = Notification.show("Appointment failed");
                        noti.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        System.out.println(exception);
                    }
                });
                dialog.open();
            }
        });
    }

    private void clearFields(){
        notes.clear();
    }

    private void configureDateTimePicker() {
        startTime = new DateTimePicker();
        startTime.setLabel("Appointment Date and Time");
        startTime.setAutoOpen(true);
        startTime.setMin(LocalDateTime.now().plusDays(1));
        startTime.setValue(LocalDateTime.now().plusDays(1));
        startTime.setMax(LocalDateTime.now().plusDays(90));
    }


}