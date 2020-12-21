package ui;

import presenter.GoProRenamer;
import utils.TextUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class RenamePage extends JFrame {
    private JTextArea mLoggerView;
    private StringBuilder builder = new StringBuilder();

    public void showUI() {
        init();
        configUI();
    }

    private void init() {
        setLocation(200, 100);
        setSize(500, 300);
        setVisible(true);
        setTitle("GoPro重命名!");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });
    }

    private void configUI() {
        JTextField inputView = new JTextField();
        inputView.setBackground(Color.GREEN);
        add(BorderLayout.NORTH, inputView);
        inputView.addActionListener(actionEvent -> {
            String path = inputView.getText();
            rename(path);
        });
        mLoggerView = new JTextArea();
        mLoggerView.setText("大俊子在此!");
        new JScrollPane(mLoggerView);
        add(BorderLayout.CENTER, mLoggerView);
    }

    private void rename(String path) {
        if (TextUtil.isEmpty(path)) {
            log("文件夹为空");
        } else {
            new GoProRenamer(path, this::log);
        }
    }

    private void log(String content) {
        builder.append(content);
        builder.append("\n");
        mLoggerView.setText(builder.toString());
    }
}
