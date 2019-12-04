package be.kdg.chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatFrame extends JFrame implements TextReceiver {
    private JLabel nameLabel;
    private JTextArea history;
    private JTextField messageField;
    private JButton sendButton;
    private JButton exitButton;
    private IChatClient chatClient;

    public ChatFrame(IChatClient chatClient) {
        this.chatClient = chatClient;
        chatClient.setTextReceiver(this);
        String name = chatClient.getName();
        setTitle("Chat: " + name);
        createComponents(name);
        layoutComponents();
        addListeners();
        setSize(500, 300);
        setVisible(true);
    }

    private void createComponents(String name) {
        this.nameLabel = new JLabel(name);
        this.history = new JTextArea();
        this.history.setEditable(false);
        this.messageField = new JTextField();
        this.sendButton = new JButton("send");
        this.exitButton = new JButton("exit");
    }

    private void layoutComponents() {
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        namePanel.add(nameLabel);
        JScrollPane historyPane = new JScrollPane(history);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(messageField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);
        bottomPanel.add(inputPanel, BorderLayout.NORTH);
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitPanel.add(exitButton);
        bottomPanel.add(exitPanel, BorderLayout.SOUTH);
        Container contentPane = getContentPane();
        contentPane.add(namePanel, BorderLayout.NORTH);
        contentPane.add(historyPane, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void addListeners() {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                stop();
            }
        });
        sendButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                send();
            }
        });
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                exit();
            }
        });
        messageField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                send();
            }
        });
    }

    private void exit() {
        stop();
        setVisible(false);
    }

    private void send() {
        String message = messageField.getText();
        messageField.setText("");
        chatClient.send(message);
    }

    private void stop() {
        chatClient.unregister();
    }

    public void receive(String text) {
        String historyText = history.getText();
        history.setText(historyText + '\n' + text);
    }
}
