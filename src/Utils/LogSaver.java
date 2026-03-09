package Utils;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogSaver {

    public static void saveLogToFile(String content, Component parentComponent) {
        String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss").format(new Date());
        String fileName = "log_" + timestamp + ".txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(content);
            JOptionPane.showMessageDialog(parentComponent, "Log saved successfully to:\n" + fileName, "Save Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parentComponent, "Error saving log:\n" + e.getMessage(), "Save Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}
