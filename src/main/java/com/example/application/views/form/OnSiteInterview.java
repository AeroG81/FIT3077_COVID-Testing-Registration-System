package com.example.application.views.form;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.*;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "/interview")
@PageTitle("On-Site Interview  | Vaadin CRM")
public class OnSiteInterview extends VerticalLayout {
    RadioButtonGroup<String> firstQuestion = new RadioButtonGroup<>();

    RadioButtonGroup<String> secondQuestion = new RadioButtonGroup<>();

    RadioButtonGroup<String> thirdQuestion = new RadioButtonGroup<>();

    RadioButtonGroup<String> fourthQuestion = new RadioButtonGroup<>();

    RadioButtonGroup<String> fifthQuestion = new RadioButtonGroup<>();

    RadioButtonGroup<String> sixthQuestion = new RadioButtonGroup<>();

    public OnSiteInterview() {
        H1 title = new H1("Interview Questions for Preferred Testing Method");

        Hr hr1 = new Hr();
        Hr hr2 = new Hr();
        Hr hr3 = new Hr();
        Hr hr4 = new Hr();
        Hr hr5 = new Hr();
        Hr hr6 = new Hr();

        // First interview question
        Span label_first = new Span("1. Are you exhibiting 2 or more symptoms as listed below?");

        UnorderedList ul1 = new UnorderedList(
                new ListItem("Fever"),
                new ListItem("Chills"),
                new ListItem("Shivering"),
                new ListItem("Body Ache"),
                new ListItem("Headache"),
                new ListItem("Sore Throat"),
                new ListItem("Nausea or vomiting"),
                new ListItem("Diarrhea"),
                new ListItem("Fatigue"),
                new ListItem("Runny nose or nasal congestion")
        );
        firstQuestion.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        firstQuestion.setLabel("Choose one:");
        firstQuestion.setRequired(true);
        firstQuestion.setItems("Yes", "No");

        // Second interview question
        Span label_second = new Span("2. Besides the above, are you exhibiting any of the symptoms listed below?");
        UnorderedList ul2 = new UnorderedList(
                new ListItem("Cough"),
                new ListItem("Difficulty Breathing"),
                new ListItem("Loss of smell"),
                new ListItem("Loss of taste")
        );
        secondQuestion.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        secondQuestion.setLabel("Choose one:");
        secondQuestion.setRequired(true);
        secondQuestion.setItems("Yes", "No");

        // Third interview question
        Span label_third = new Span("3. Have you attended any event / areas associated with known COVID-19 cluster?");
        thirdQuestion.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        thirdQuestion.setLabel("Choose one:");
        thirdQuestion.setRequired(true);
        thirdQuestion.setItems("Yes", "No");

        // Fourth interview question
        Span label_fourth = new Span("4. Have you travelled abroad within the last 14 days?");
        fourthQuestion.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        fourthQuestion.setLabel("Choose one:");
        fourthQuestion.setRequired(true);
        fourthQuestion.setItems("Yes", "No");

        // Fifth interview question
        Span label_fifth = new Span("5. Have you had any close contact with any confirmed or suspected COVID-19 cases within the last 14 days?");
        fifthQuestion.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        fifthQuestion.setLabel("Choose one:");
        fifthQuestion.setRequired(true);
        fifthQuestion.setItems("Yes", "No");

        // Sixth interview question
        Span label_sixth = new Span("6. Are you a MOH COVID-19 volunteer in the last 14 days?");
        sixthQuestion.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        sixthQuestion.setLabel("Choose one:");
        sixthQuestion.setRequired(true);
        sixthQuestion.setItems("Yes", "No");

        // Extra response
        TextArea extra_question = new TextArea();
        extra_question.setWidthFull();
        extra_question.setLabel("Anything else you feel like we should know? Leave blank, if none.");

        add(    title,
                label_first, ul1, firstQuestion, hr1,
                label_second, ul2, secondQuestion, hr2,
                label_third, thirdQuestion, hr3,
                label_fourth, fourthQuestion, hr4,
                label_fifth, fifthQuestion, hr5,
                label_sixth, sixthQuestion, hr6,
                extra_question
        );
    }


}
