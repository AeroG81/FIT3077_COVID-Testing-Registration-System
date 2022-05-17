package com.example.application.views.subpages;

import com.example.application.data.entity.Booking.Booking;
import com.example.application.data.entity.Booking.BookingCollection;
import com.example.application.data.entity.Booking.OnSiteTesting;
import com.example.application.data.entity.Booking.OnlineTesting;
import com.example.application.data.entity.TestingSite.TestingSite;
import com.example.application.data.entity.TestingSite.TestingSiteCollection;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.function.SerializableBiConsumer;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ReceptionistBookingLayout extends VerticalLayout {

    private TestingSiteCollection collection = new TestingSiteCollection();
    private ComboBox<TestingSite> testingSite = new ComboBox<>("TestingSite");
    private DateTimePicker startTime = new DateTimePicker();
    private FormLayout editorForm = new FormLayout();
    private Dialog editorDialog = new com.vaadin.flow.component.dialog.Dialog();
    private Label pin = new Label();
    private Label qr = new Label();

    public ReceptionistBookingLayout(){
        Grid<Booking> grid = new Grid<>(Booking.class, false);
        grid.addColumn(b -> b.getCustomer().getGivenName()).setHeader("Given name").setAutoWidth(true);;
        grid.addColumn(b -> b.getCustomer().getFamilyName()).setHeader("Family name").setAutoWidth(true);;
        grid.addColumn(b -> b.getCustomer().getUserName()).setHeader("Username").setAutoWidth(true);;
        grid.addColumn(b -> ZonedDateTime.parse(b.getStartTime()).format(DateTimeFormatter.ofPattern("E dd/MM/yyyy - HH:mm z"))).setHeader("Booking DateTime");
        grid.addColumn(b -> {
            if (b.getClass().equals(OnSiteTesting.class)){
                return ((OnSiteTesting) b).getTestingSite().getName();
            }
            else {
                return "-";
            }
        }).setHeader("Testing site").setAutoWidth(true);;
        grid.addColumn(Booking::getStatus).setHeader("Status").setAutoWidth(true);
        List<Booking> bookings = new BookingCollection().getCollection();
        bookings.sort(new Comparator<Booking>() {
            @Override
            public int compare(Booking o1, Booking o2) {
                return ZonedDateTime.parse(o2.getStartTime()).toLocalDateTime().compareTo(ZonedDateTime.parse(o1.getStartTime()).toLocalDateTime());
            }
        });
        grid.setItems(bookings);

        grid.addSelectionListener(selection -> {
            Optional<Booking> optionalPerson = selection.getFirstSelectedItem();
            optionalPerson.ifPresent(booking -> {
                pin.setText("PIN CODE: " + booking.getSmsPin());
                qr.setText("QR CODE: " + booking.getQrcode());
                startTime.setValue(ZonedDateTime.parse(booking.getStartTime()).toLocalDateTime());
                System.out.printf("Selected person: %s%n", booking.getCustomer().getUserName());
                if (booking.getClass().equals(OnSiteTesting.class)){
                    testingSite.setValue(((OnSiteTesting) booking).getTestingSite());
                }
                else{
                    testingSite.setValue(null);
                }
                editorDialog.open();
            });
        });

        this.populateTestingSiteComboBox();

        editorForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0",2)
        );
        editorForm.setColspan(pin,2);
        editorForm.setColspan(qr,2);
        editorForm.add(pin,qr,testingSite,startTime);


        Button closeButton = new Button("Close");
        closeButton.addClickListener(e -> editorDialog.close());
        Button saveButton = new Button("Save", e -> editorDialog.close());
        saveButton.addClickListener(e -> editorDialog.close());
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton,closeButton);
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        editorDialog.add(editorForm,buttonLayout);

        add(grid);
    }

    private void populateTestingSiteComboBox(){
        testingSite.setItems(collection.getCollection());
        testingSite.setItemLabelGenerator(TestingSite::getName);
    }



}
