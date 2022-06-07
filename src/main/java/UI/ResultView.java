package UI;

import db.DbHandler;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import static javax.swing.GroupLayout.Alignment.BASELINE;

public class ResultView extends JDialog {
    private DbHandler handler;
    private JTable table;
    MainView mainView;
    Object[] tableHeaders = {
            "ID", "ФИО", "Наименование ТСР", "Выплата", "Дата назначения",
            "Месяц выплаты", "Предельный срок", "Стоимость", "Организация"
    };

    public ResultView(MainView mainView, boolean bought) {
        this.mainView = mainView;
        handler = mainView.handler;
        setTitle("Результат поиска");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        createView(getContentPane());
        setResizable(false);
        setLocationByPlatform(true);
        showResultList(bought);
        pack();
        setVisible(true);
    }

    private void createView(Container contentPane) {
        var layout = new GroupLayout(contentPane);
        contentPane.setLayout(layout);

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
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addComponent(scrollPane));

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(BASELINE)
                        .addComponent(scrollPane)));

    }

    void showResultList(boolean bought) {
        if (bought) {
            var recordsList = handler.getWhoBoughtList();
            var model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            for (var record : recordsList) {
                model.addRow(new Object[]{
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

        else {
            var recordsList = handler.getWhoNotBoughtList();
            var model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            for (var record : recordsList) {
                model.addRow(new Object[]{
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
}
