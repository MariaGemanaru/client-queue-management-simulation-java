package GUI;

import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {
    private JTextField txtNoClients;
    private JTextField txtNoQueues;
    private JTextField txtSimulationTime;
    private JTextField txtMinArrival;
    private JTextField txtMaxArrival;
    private JTextField txtMinService;
    private JTextField txtMaxService;
    private JComboBox<String> comboStrategy;
    private JButton btnStart;
    private JButton btnSaveLog;
    private JTextArea txtLog;

    public SimulationFrame() {
        setTitle("Queue Simulation Manager");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(10, 2));

        panel.add(new JLabel("Number of Clients:"));
        txtNoClients = new JTextField();
        panel.add(txtNoClients);

        panel.add(new JLabel("Number of Queues:"));
        txtNoQueues = new JTextField();
        panel.add(txtNoQueues);

        panel.add(new JLabel("Simulation Time:"));
        txtSimulationTime = new JTextField();
        panel.add(txtSimulationTime);

        panel.add(new JLabel("Min Arrival Time:"));
        txtMinArrival = new JTextField();
        panel.add(txtMinArrival);

        panel.add(new JLabel("Max Arrival Time:"));
        txtMaxArrival = new JTextField();
        panel.add(txtMaxArrival);

        panel.add(new JLabel("Min Service Time:"));
        txtMinService = new JTextField();
        panel.add(txtMinService);

        panel.add(new JLabel("Max Service Time:"));
        txtMaxService = new JTextField();
        panel.add(txtMaxService);

        panel.add(new JLabel("Selection Policy:"));
        comboStrategy = new JComboBox<>(new String[]{"Shortest Time", "Shortest Queue"});
        panel.add(comboStrategy);

        btnStart = new JButton("Start Simulation");
        panel.add(btnStart);

        btnSaveLog = new JButton("Save Log to File");
        panel.add(btnSaveLog);

        getContentPane().add(panel, BorderLayout.NORTH);

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtLog);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public JButton getStartButton() {
        return btnStart;
    }

    public JButton getSaveLogButton() {
        return btnSaveLog;
    }

    public String getNoClients() {
        return txtNoClients.getText();
    }

    public String getNoQueues() {
        return txtNoQueues.getText();
    }

    public String getSimulationTime() {
        return txtSimulationTime.getText();
    }

    public String getMinArrival() {
        return txtMinArrival.getText();
    }

    public String getMaxArrival() {
        return txtMaxArrival.getText();
    }

    public String getMinService() {
        return txtMinService.getText();
    }

    public String getMaxService() {
        return txtMaxService.getText();
    }

    public String getSelectionPolicy() {
        return (String) comboStrategy.getSelectedItem();
    }

    public void appendLog(String message) {
        txtLog.append(message + "\n");
    }

    public String getLogContent() {
        return txtLog.getText();
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
