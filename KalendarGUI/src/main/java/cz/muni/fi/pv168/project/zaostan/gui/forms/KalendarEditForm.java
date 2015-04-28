package cz.muni.fi.pv168.project.zaostan.gui.forms;

import cz.muni.fi.pv168.project.zaostan.gui.forms.components.JXDateTimePicker;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by wermington on 4/27/15.
 */
public class KalendarEditForm {
    private JLabel labelName;
    private JLabel labelDateBegin;
    private JLabel labelDateEnd;
    private JLabel labelAddress;
    private JLabel labelDescription;
    private JTextField textName;
    private JTextArea textDescription;
    private JTextArea textAddress;
    private JButton btnSave;
    private JButton btnReset;
    private JButton btnCancel;
    private JXDateTimePicker inputDateEnd;
    private JXDateTimePicker inputDateBegin;
    private JPanel mainFramePanel;
    private JPanel mainFrameContextPanel;

    public void initComponents()
    {
        Date date = new Date();

        inputDateEnd.setFormats(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM));
        inputDateEnd.setTimeFormat(DateFormat.getTimeInstance(DateFormat.MEDIUM));
        inputDateEnd.setDate(date);

        inputDateBegin.setFormats(DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.MEDIUM));
        inputDateBegin.setTimeFormat(DateFormat.getTimeInstance(DateFormat.MEDIUM));
        inputDateBegin.setDate(date);


    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("KalendarEditForm");
        KalendarEditForm kalendarEditForm = new KalendarEditForm();
        kalendarEditForm.initComponents();
        frame.setContentPane(kalendarEditForm.mainFramePanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
