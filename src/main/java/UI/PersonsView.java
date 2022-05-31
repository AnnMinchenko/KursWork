package UI;

import javax.swing.*;
import java.awt.*;

public class PersonsView extends JDialog {
    MainView mainView;

    public PersonsView(MainView mainView) {
        this.mainView = mainView;
        setTitle("Список клиентов");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        createView(getContentPane());
        setResizable(false);
        setLocationByPlatform(true);
        pack();
        setVisible(true);
    }

    private void createView(Container contentPane) {

    }
}
