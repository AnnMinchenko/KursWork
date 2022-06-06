package UI;

import db.DbHandler;
import model.Person;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.regex.Pattern;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

public class PersonsView extends JDialog {

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


    private static final Pattern TELEPHONE_PATTERN = Pattern.compile("^89([1-9]{9})$");
    private DbHandler handler;
    private JTable table;
    private JGradientButton addPersonButton = new JGradientButton("Добавить клиента");
    private JGradientButton removePersonButton = new JGradientButton("Удалить клиента");
    private JGradientButton editPersonButton = new JGradientButton("Изменить данные клиента");
    MainView mainView;

    Object[] tableHeaders = {
            "ID", "ФИО", "Номер телефона", "Адрес"
    };

    public PersonsView(MainView mainView) {
        this.mainView = mainView;
        handler = mainView.handler;
        setTitle("Список клиентов");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        createView(getContentPane());
        setResizable(false);
        setLocationByPlatform(true);
        showPersonList();
        pack();
        setVisible(true);
    }

    Action editPerson = new AbstractAction() {
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

            //ФИО+
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

            //Номер телефона+
            var phoneNumber = "";
            while (Objects.equals(phoneNumber, "") || !TELEPHONE_PATTERN.matcher(phoneNumber).matches()) {
                phoneNumber = JOptionPane.showInputDialog(
                        null,
                        "Номер телефона:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (phoneNumber == null) {
                    return;
                }
            }


            //Адрес+
            var address = "";
            while (Objects.equals(address, "")) {
                address = JOptionPane.showInputDialog(
                        null,
                        "Адрес:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (address == null) {
                    return;
                }
            }

            var id = (int) table.getModel().getValueAt(row, 0);

            var person = new Person(
                    id,
                    fullName,
                    phoneNumber,
                    address
            );

            handler.editPerson(person, id);
            showPersonList();
        }
    };

    Action removePerson = new AbstractAction() {
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
            showPersonList();
        }
    };

    Action addPerson = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {

            //ФИО+
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

            //Номер телефона+
            var phoneNumber = "";
            while (Objects.equals(phoneNumber, "") || !TELEPHONE_PATTERN.matcher(phoneNumber).matches()) {
                phoneNumber = JOptionPane.showInputDialog(
                        null,
                        "Номер телефона:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (phoneNumber == null) {
                    return;
                }
            }


            //Адрес+
            var address = "";
            while (Objects.equals(address, "")) {
                address = JOptionPane.showInputDialog(
                        null,
                        "Адрес:",
                        "Новая запись",
                        JOptionPane.QUESTION_MESSAGE
                );
                if (address == null) {
                    return;
                }
            }

            var person = new Person(
                    handler.getRecordsList().size(),
                    fullName,
                    phoneNumber,
                    address
            );

            handler.addPerson(person);
            showPersonList();
        }
    };

    private void createView(Container contentPane) {
        var layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);

        DefaultTableModel model = new DefaultTableModel(new Object[0][4], tableHeaders){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        table.getColumnModel().getColumn(0).setMinWidth(30);
        table.getColumnModel().getColumn(1).setMinWidth(200);
        table.getColumnModel().getColumn(2).setMinWidth(200);
        table.getColumnModel().getColumn(3).setMinWidth(150);
        table.setPreferredScrollableViewportSize(new Dimension(600,500));
        addPersonButton.addActionListener(addPerson);
        addPersonButton.setBackground(new Color(111,195,237));
        removePersonButton.addActionListener(removePerson);
        removePersonButton.setBackground(new Color(111,195,237));
        editPersonButton.addActionListener(editPerson);
        editPersonButton.setBackground(new Color(111,195,237));

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(scrollPane)
                .addGroup(layout.createParallelGroup(LEADING)
                        .addComponent(addPersonButton)
                        .addComponent(removePersonButton)
                        .addComponent(editPersonButton)
                )
        );
        //layout.linkSize(SwingConstants.HORIZONTAL, addButton, showPersonsListButton);

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(scrollPane)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(addPersonButton))
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(removePersonButton))
                                .addGroup(layout.createParallelGroup(BASELINE)
                                        .addComponent(editPersonButton))
                        )
                )
        );
    }

    private void showPersonList() {
        var personList = handler.getPersonsList();
        var model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (var person : personList) {
            model.addRow (new Object[] {
                    person.getId(),
                    person.getFullName(),
                    person.getPhoneNumber(),
                    person.getAddress()
            });
        }
    }
}
