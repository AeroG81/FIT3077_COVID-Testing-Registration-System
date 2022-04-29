package com.example.application.views.main;

import com.example.application.data.entity.User.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Login | Vaadin CRM")
@Route(value = "")
public class LoginView extends HorizontalLayout {

    private VerticalLayout loginLayout = new VerticalLayout();

    private VerticalLayout dialogLayout = new VerticalLayout();

    private Dialog redirectOptions = new Dialog();

    H1 title = new H1();

    TextField username = new TextField();

    PasswordField password = new PasswordField();

    UserCollection uc = new UserCollection();

    Button loginButton = new Button("Log In");

    Button redirectToTestingSite = new Button("Browse Testing Sites");

    Button redirectToOnsiteInterview = new Button("Test Recommendation");

    Button redirectToOnsiteBooking = new Button("Make a Booking (On-site only)");

    public LoginView() {
        this.loginUser();

        title.setText("COVID Testing Made Easy");

        username.setPrefixComponent(VaadinIcon.USER.create());
        username.setLabel("Username");
        username.setRequired(true);

        password.setRevealButtonVisible(false);
        password.setLabel("Password");
        password.setRevealButtonVisible(true);
        password.setRequired(true);

        loginLayout.setSizeFull();
        loginLayout.setAlignItems(Alignment.CENTER);
        loginLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        loginLayout.add(title, username, password, loginButton);

        add(loginLayout);

    }

    public void loginUser() {
        redirectOptions = new Dialog();

        dialogLayout.add(new H1("Choose where you want to go: "));
        dialogLayout.add(new Hr());


        redirectToTestingSite = new Button("Browse Testing Sites");
        redirectToTestingSite.addClickListener(f -> {
                redirectToTestingSite.getUI().ifPresent(ui -> ui.navigate("testingsite"));
                redirectOptions.close();
            }
        );

        redirectToOnsiteInterview = new Button("Test Recommendation");
        redirectToOnsiteInterview.addClickListener(g -> {
                    redirectToOnsiteInterview.getUI().ifPresent(ui -> ui.navigate("onsiteinterview"));
                    redirectOptions.close();
            }
        );

        redirectToOnsiteBooking = new Button("Make a Booking (On-site only)");
        redirectToOnsiteBooking.addClickListener(h -> {
                redirectToOnsiteBooking.getUI().ifPresent(ui -> ui.navigate("onsitebooking"));
                redirectOptions.close();
            }
        );

        loginButton.addClickListener(e -> {
            if (username.getValue().equals("") && password.getValue().equals("")) {
                Notification.show("Please fill in all the fields");
            }
            else {
                User user = uc.verifyUserId(username.getValue(), password.getValue());
                if (user != null) {
                    try {
                        if (uc.checkIsCustomer(user.getUserName())){
                            // add testing site route
                            dialogLayout.add(redirectToTestingSite);
                        }
                        if (uc.checkIsReceptionist(user.getUserName())){
                            // add onsite interview route
                            dialogLayout.add(redirectToOnsiteBooking);
                        }
                        if (uc.checkIsHealthcareWorker(user.getUserName())){
                            // add onsite booking route
                            dialogLayout.add(redirectToOnsiteInterview);
                        }

                        redirectOptions.add(dialogLayout);
                        redirectOptions.open();

                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                }
                else {
                    Notification.show("Incorrect username and/or password");
                }
            }
        });
    }
}
