package UI;

import model.Record;
import db.DbHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;


public class MainView extends JFrame {


    private static final class JGradientButton extends JButton{
        private JGradientButton(String text){
            super(text);
            setContentAreaFilled(false);
        }

        @Override
        protected void paintComponent(Graphics g){
            Graphics2D g2 = (Graphics2D)g.create();
            g2.setPaint(new GradientPaint(
                    new Point(0, 0),
                    getBackground(),
                    new Point(0, getHeight()/3),
                    Color.WHITE));
            g2.fillRect(0, 0, getWidth(), getHeight()/3);
            g2.setPaint(new GradientPaint(
                    new Point(0, getHeight()/3),
                    Color.WHITE,
                    new Point(0, getHeight()),
                    getBackground()));
            g2.fillRect(0, getHeight()/3, getWidth(), getHeight());
            g2.dispose();

            super.paintComponent(g);
        }
    }


    private static final Pattern DATE_PATTERN = Pattern.compile(
            "^((19|2[0-9])[0-9]{2})-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$");
    private static final Pattern MONTH_PATTERN = Pattern.compile("^(((19|2[0-9])[0-9]{2})-(0[1-9]|1[0-2]))|-$");
    private static final Pattern NUMBER_PATTERN = Pattern.compile("^([0-9]+)$");
    private static final String pass = "000";

    public DbHandler handler;
    private MainView mainView = this;

    private JTable table;

    private final String filePath = "Logs.txt";
    private Date changeDate = new Date();

    private JGradientButton addButton = new JGradientButton("Добавить запись");
    private JGradientButton editButton = new JGradientButton("Изменить запись");
    private JGradientButton removeButton = new JGradientButton("Удалить запись");

    private JGradientButton showPersonsListButton = new JGradientButton("Список клиентов");

    private JGradientButton paymentSumButton = new JGradientButton("Сумма выплат");
    private JGradientButton boughtButton = new JGradientButton("Список купивших клиентов");
    private JGradientButton notBoughtButton = new JGradientButton("Список не купивших клиентов");


    Object[] tableHeaders = {
        "ID", "ФИО", "Наименование ТСР", "Выплата", "Дата назначения",
        "Месяц выплаты", "Предельный срок", "Стоимость", "Организация"
    };

    private Action createRecord = new AbstractAction() {
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
                        "Дата назначения (год-месяц-день):",
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
                        "Месяц выплаты (год-месяц):",
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

            int id;
            var temp = handler.getRecordsList().size()-1;
            if (temp < 0)
                id = 0;
            else
                id = handler.getRecordsList().get(handler.getRecordsList().size()-1).getId()+1;


            var record = new Record(
                    id,
                    personObject,
                    name,
                    Integer.parseInt(payment),
                    date,
                    monthOfPayment,
                    Integer.parseInt(cost),
                    organization
            );

            handler.addRecord(record);
            showRecordsList();

            String lines = changeDate + " Добавлена запись: " + record + "\n";
            try {
                FileWriter writer = new FileWriter(filePath, true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);
                bufferWriter.write(lines);
                bufferWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    };

    private Action editRecord = new AbstractAction() {
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

            var persons = handler.getPersonsList();
            Object[] personsNames = new Object[persons.size()];
            for (var i = 0; i < persons.size(); i++) {
                var person = persons.get(i);
                personsNames[i] = String.format("%s - %s", person.getId(), person.getFullName());
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
                        "Дата назначения (год-месяц-день):",
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
                        "Месяц выплаты (год-месяц):",
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

            var id = (int) table.getModel().getValueAt(row, 0);

            var record = new Record(
                    id,
                    personObject,
                    name,
                    Integer.parseInt(payment),
                    date,
                    monthOfPayment,
                    Integer.parseInt(cost),
                    organization
            );

            handler.editRecord(record, id);
            showRecordsList();

            String lines = changeDate + " Изменена запись: " + record + "\n";
            try {
                FileWriter writer = new FileWriter(filePath, true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);
                bufferWriter.write(lines);
                bufferWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    };

    private Action removeRecord = new AbstractAction() {
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

            Record record = handler.getRecord(id);
            String lines = changeDate + " Удалена запись: " + record + "\n";
            try {
                FileWriter writer = new FileWriter(filePath, true);
                BufferedWriter bufferWriter = new BufferedWriter(writer);
                bufferWriter.write(lines);
                bufferWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            handler.deleteRecord(id);
            showRecordsList();
        }
    };

    private Action showPersonsList = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {

            JPasswordField pf = new JPasswordField();
            int okCxl = JOptionPane.showConfirmDialog(null, pf, "Введите пароль", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (okCxl == JOptionPane.OK_OPTION) {
                String password = new String(pf.getPassword());
                if (pass.equals(password))
                    new PersonsView(mainView);
            }

 //           new PersonsView(mainView);
        }

    };

    private Action paymentSum = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            var year = "";
            while (Objects.equals(year, "")) {
                year = JOptionPane.showInputDialog(
                        null,
                        "Год:",
                        "Сумма выплат",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (year == null) {
                    return;
                }
            }

            var quarter = "";
            while (Objects.equals(quarter, "")) {
                quarter = JOptionPane.showInputDialog(
                        null,
                        "Квартал:",
                        "Сумма выплат",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (quarter == null) {
                    return;
                }
            }

            JOptionPane.showMessageDialog(null, "Сумма выплат " + handler.getPaymentSum(quarter,year));

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
        table.setPreferredScrollableViewportSize(new Dimension(1300,500));
        addButton.addActionListener(createRecord);
        addButton.setBackground(new Color(111,195,237));
        editButton.addActionListener(editRecord);
        editButton.setBackground(new Color(111,195,237));
        removeButton.addActionListener(removeRecord);
        removeButton.setBackground(new Color(111,195,237));
        paymentSumButton.addActionListener(paymentSum);
        paymentSumButton.setBackground(new Color(111,237,187));
        showPersonsListButton.addActionListener(showPersonsList);
        showPersonsListButton.setBackground(new Color(143,111,237));
        boughtButton.addActionListener(e -> new ResultView(mainView, true));
        boughtButton.setBackground(new Color(111,237,187));
        notBoughtButton.addActionListener(e -> new ResultView(mainView, false));
        notBoughtButton.setBackground(new Color(111,237,187));

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
            .addComponent(scrollPane)
            .addGroup(layout.createParallelGroup(LEADING)
                    .addComponent(addButton)
                    .addComponent(editButton)
                    .addComponent(removeButton)
                    .addComponent(showPersonsListButton)
                    .addComponent(boughtButton)
                    .addComponent(notBoughtButton)
                    .addComponent(paymentSumButton)
            )
        );

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
                            .addComponent(paymentSumButton))
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(boughtButton))
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(notBoughtButton))
                    .addGroup(layout.createParallelGroup(BASELINE)
                            .addComponent(showPersonsListButton))
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
                    record.getId(),
                    record.getPerson().getFullName(),
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
