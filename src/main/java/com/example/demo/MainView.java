package com.example.demo;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@Route("")
@SpringBootApplication
public class MainView extends VerticalLayout {
    private StudentRepository repository;
    private TextField firstName = new TextField("First Name");
    private TextField lastName = new TextField("Last Name");
    private TextField email = new TextField("Email");
    private TextField phone = new TextField("Phone");
    private Button save = new Button("Save");
    private Grid<Student> grid = new Grid<>(Student.class);
    private Binder<Student> binder = new Binder<>(Student.class);

    public MainView(StudentRepository repository) {
//        add(new H1("Forms"));
//        var button = new Button("Click Me");
//        var textField = new TextField();
//
////        add(textField, button);
//        add(new HorizontalLayout(textField, button));
//        button.addClickListener(event -> {
//            add(new Paragraph(textField.getValue()));
//            textField.clear();
//        });
        
        this.repository = repository;
        grid.setColumns("firstName", "lastName", "email", "phone");
        save.addClickShortcut(Key.ENTER);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        add(getForm(), grid);
        refreshGrid();
    }

    private Component getForm() {
        var layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.BASELINE);
        layout.add(firstName, lastName, email, phone, save);
        binder.bindInstanceFields(this);
        save.addClickListener(event -> {
            var student = new Student();
            try {
                binder.writeBean(student);
                repository.save(student);
                binder.readBean(new Student());
                refreshGrid();
            } catch (ValidationException e) {
                throw new RuntimeException(e);
            }
        });
        return layout;
    }

    private void refreshGrid() {
        grid.setItems(repository.findAll());
    }
}
