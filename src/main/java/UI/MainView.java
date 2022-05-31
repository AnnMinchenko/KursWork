package UI;

import model.Record;
import db.DbHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Pattern;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

public class MainView extends JFrame {
    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
    private static final Pattern MONTH_PATTERN = Pattern.compile("^([1-9]|[0-1][0-2])$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^[0-9]+$");

    private DbHandler handler;
    private MainView mainView = this;

    private JTable table;

    private JButton addButton = new JButton("Добавить запись");
    private JButton editButton = new JButton("Изменить запись");
    private JButton removeButton = new JButton("Удалить запись");
    private JButton addPersonButton = new JButton("Добавить клиента");

    private JButton showPersonsListButton = new JButton("Список клиентов");

    Object[] tableHeaders = {
        "ID", "ФИО", "Наименование ТСР", "Выплата", "Дата",
        "Месяц выплаты", "Предельный срок приобретения", "Стоимость", "Организация"
    };

    Action createRecord = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            var persons = handler.getPersonsList();
            Object[] personsNames = new Object[persons.size()];
            for (var i = 0; i < persons.size(); i++) {
                var person = persons.get(i);
                personsNames[i] = String.format("%s - %s", person.getId(), person.getFullName());
            }

            if (persons.size() == 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Список клиентов пуст",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            //Клиент
            var person = (String) JOptionPane.showInputDialog(
                    null,
                    "ФИО клиента:",
                    "Новая запись",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    personsNames,
                    personsNames[0]
            );
            if (person == null) {
                return;
            }

            //Название ТСР
            var name = "";
            while (Objects.equals(name, "")) {
                name = JOptionPane.showInputDialog(
                        null,
                        "Название ТСР:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (name == null) {
                    return;
                }
            }

            //Сумма выплаты
            var payment = "";
            while (Objects.equals(payment, "") || !NUMBER_PATTERN.matcher(payment).matches()) {
                payment = JOptionPane.showInputDialog(
                        null,
                        "Сумма выплаты:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (payment == null) {
                    return;
                }
            }

            //Дата
            var date = "";
            while (Objects.equals(date, "") || !DATE_PATTERN.matcher(date).matches()) {
                date = JOptionPane.showInputDialog(
                        null,
                        "Дата выплаты (год-месяц-день):",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (date == null) {
                    return;
                }
            }

            //Месяц выплаты
            var monthOfPayment = "";
            while (Objects.equals(monthOfPayment, "") || !MONTH_PATTERN.matcher(monthOfPayment).matches()) {
                monthOfPayment = JOptionPane.showInputDialog(
                        null,
                        "Месяц выплаты:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (monthOfPayment == null) {
                    return;
                }
            }

            //Цена приобретения
            var cost = "";
            while (Objects.equals(cost, "") || !NUMBER_PATTERN.matcher(cost).matches()) {
                cost = JOptionPane.showInputDialog(
                        null,
                        "Цена приобретения:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (cost == null) {
                    return;
                }
            }

            //Организация
            var organization = "";
            while (Objects.equals(organization, "")) {
                organization = JOptionPane.showInputDialog(
                        null,
                        "Организация:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (organization == null) {
                    return;
                }
            }

            var personObject = handler.getPerson(Integer.parseInt(person.split(" ")[0]));

            var record = new Record(
                    handler.getRecordsList().size(),
                    personObject,
                    name,
                    Integer.parseInt(payment),
                    date,
                    Integer.parseInt(monthOfPayment),
                    Integer.parseInt(cost),
                    organization
            );

            handler.addRecord(record);
            showRecordsList();
        }
    };

    Action addPerson = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            var persons = handler.getPersonsList();
            Object[] personsNames = new Object[persons.size()];
            for (var i = 0; i < persons.size(); i++) {
                var person = persons.get(i);
                personsNames[i] = String.format("%s - %s", person.getId(), person.getFullName());
            }

            if (persons.size() == 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Список клиентов пуст",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            //Клиент
            var person = (String) JOptionPane.showInputDialog(
                    null,
                    "ФИО клиента:",
                    "Новая запись",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    personsNames,
                    personsNames[0]
            );
            if (person == null) {
                return;
            }
////////////////////////////////
            //ФИО
            var fullName = "";
            while (Objects.equals(fullName, "")) {
                fullName = JOptionPane.showInputDialog(
                        null,
                        "ФИО:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (fullName == null) {
                    return;
                }
            }

            //Сумма выплаты
            var payment = "";
            while (Objects.equals(payment, "") || !NUMBER_PATTERN.matcher(payment).matches()) {
                payment = JOptionPane.showInputDialog(
                        null,
                        "Сумма выплаты:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (payment == null) {
                    return;
                }
            }

            //Дата
            var date = "";
            while (Objects.equals(date, "") || !DATE_PATTERN.matcher(date).matches()) {
                date = JOptionPane.showInputDialog(
                        null,
                        "Дата выплаты (год-месяц-день):",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (date == null) {
                    return;
                }
            }

            //Месяц выплаты
            var monthOfPayment = "";
            while (Objects.equals(monthOfPayment, "") || !MONTH_PATTERN.matcher(monthOfPayment).matches()) {
                monthOfPayment = JOptionPane.showInputDialog(
                        null,
                        "Месяц выплаты:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (monthOfPayment == null) {
                    return;
                }
            }

            //Цена приобретения
            var cost = "";
            while (Objects.equals(cost, "") || !NUMBER_PATTERN.matcher(cost).matches()) {
                cost = JOptionPane.showInputDialog(
                        null,
                        "Цена приобретения:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (cost == null) {
                    return;
                }
            }

            //Организация
            var organization = "";
            while (Objects.equals(organization, "")) {
                organization = JOptionPane.showInputDialog(
                        null,
                        "Организация:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (organization == null) {
                    return;
                }
            }

            var personObject = handler.getPerson(Integer.parseInt(person.split(" ")[0]));

            var record = new Record(
                    handler.getRecordsList().size(),
                    personObject,
                    fullName,
                    Integer.parseInt(payment),
                    date,
                    Integer.parseInt(monthOfPayment),
                    Integer.parseInt(cost),
                    organization
            );

            handler.addRecord(record);
        }
    };

    Action editRecord = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    };

    Action removeRecord = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(
                        null,
                        "Запись не выбрана",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }
            int choose = JOptionPane.showConfirmDialog(
                    null,
                    "Вы действительно хотите удалить запись?",
                    "Подтверждение",
                    JOptionPane.YES_NO_OPTION);
            if (choose == JOptionPane.NO_OPTION) {
                return;
            }
            var id = (int) table.getModel().getValueAt(row, 0);

            handler.deletePerson(id);
            showRecordsList();
        }
    };

    Action showPersonsList = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            new PersonsView(mainView);
        }
    };

    public MainView() {
        try {
             handler = new DbHandler();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
        setTitle("Учёт приобретения ТСР");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createView(getContentPane());
        setResizable(false);
        setLocationByPlatform(true);
        showRecordsList();
        pack();
        setVisible(true);
    }

    private void createView(Container container) {
        var layout = new GroupLayout(container);
        container.setLayout(layout);

        DefaultTableModel model = new DefaultTableModel(new Object[0][9], tableHeaders){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumnModel().getColumn(0).setMinWidth(30);
        table.getColumnModel().getColumn(1).setMinWidth(300);
        table.getColumnModel().getColumn(2).setMinWidth(200);
        table.getColumnModel().getColumn(3).setMinWidth(150);
        table.getColumnModel().getColumn(4).setMinWidth(100);
        table.getColumnModel().getColumn(5).setMinWidth(100);
        table.getColumnModel().getColumn(6).setMinWidth(100);
        table.getColumnModel().getColumn(7).setMinWidth(150);
        table.getColumnModel().getColumn(8).setMinWidth(200);
        table.setPreferredScrollableViewportSize(new Dimension(1390,500));
        addButton.addActionListener(createRecord);
        editButton.addActionListener(editRecord);
        removeButton.addActionListener(removeRecord);
        showPersonsListButton.addActionListener(showPersonsList);
        addPersonButton.addActionListener(createRecord);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addComponent(scrollPane)
            .addGroup(layout.createParallelGroup(LEADING)
                    .addComponent(addButton)
                    .addComponent(editButton)
                    .addComponent(removeButton)
                    .addComponent(showPersonsListButton)
                    .addComponent(addPersonButton)
            )
        );
        layout.linkSize(SwingConstants.HORIZONTAL, addButton, showPersonsListButton);

        layout.setVerticalGroup(layout.createSequentialGroup()
            .addGroup(layout.createParallelGroup(BASELINE)
                .addComponent(scrollPane)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(addButton))
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(editButton))
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(removeButton))
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(showPersonsListButton))
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(addPersonButton))
                )
            )
        );
    }

    private void showRecordsList() {
        var recordsList = handler.getRecordsList();
        var model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (var record : recordsList) {
            model.addRow (new Object[] {
                    record.getPerson().getFullName(),
                    record.getId(),
                    record.getName(),
                    record.getPayment(),
                    record.getDate(),
                    record.getMonthOfPayment(),
                    record.getPurchaseDeadline(),
                    record.getCost(),
                    record.getOrganization()
            });
        }
    }
}