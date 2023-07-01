package ui;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import service.ServiceRegistry;
import ui.login.LogoutCallback;

import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class GUI extends JFrame {
    private LogoutCallback logoutCallback;

    public GUI(LogoutCallback logoutCallback) {
        this.logoutCallback = logoutCallback;
        setLocationRelativeTo(null);

        addMenuBar();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ServiceRegistry.getInstance().saveData();
                dispose();
            }
        });
    }

    protected void logout() {
        logoutCallback.onLogout();
        dispose();
    }

    private void addMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu logoutMenu = new JMenu("Logout");
        menuBar.add(logoutMenu);

        logoutMenu.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {}

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                logout();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {}

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {}

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {}
        });

    }
}
