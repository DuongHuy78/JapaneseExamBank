package View.Exam;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controller.Exam.ExamController;
import View.UI.UI;

import java.awt.*;

public class ExamUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea examTextArea;
    private JButton btnRandomExam;
    private JButton btnSaveExam;
    private JButton btnExportPDF;
    private JTextField audioSearchField;
    private JButton btnBrowseAudio;
    private JButton btnPlayAudio;
    private JComboBox<String> comboBoxLevel;
    private JSpinner spinnerGrammar;
    private JSpinner spinnerVocab;
    private JSpinner spinnerReading;
    private JSpinner spinnerListening;
    private JTextField txtExamTitle;

    public ExamUI() {
        setTitle("Chức năng Đề Thi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        UI.createMenuBar(this);

        // Exam configuration panel
        JPanel configPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        configPanel.setBorder(BorderFactory.createTitledBorder("Cấu hình đề thi"));
        
        configPanel.add(new JLabel("Tiêu đề đề thi:"));
        txtExamTitle = new JTextField("Đề thi JLPT");
        configPanel.add(txtExamTitle);
        
        configPanel.add(new JLabel("Cấp độ:"));
        comboBoxLevel = new JComboBox<>(new String[]{"Chọn cấp độ", "N1", "N2", "N3", "N4", "N5"});
        configPanel.add(comboBoxLevel);
        
        configPanel.add(new JLabel("Số câu ngữ pháp:"));
        spinnerGrammar = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        configPanel.add(spinnerGrammar);
        
        configPanel.add(new JLabel("Số câu từ vựng:"));
        spinnerVocab = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        configPanel.add(spinnerVocab);
        
        configPanel.add(new JLabel("Số câu đọc hiểu:"));
        spinnerReading = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        configPanel.add(spinnerReading);
        
        configPanel.add(new JLabel("Số câu nghe hiểu:"));
        spinnerListening = new JSpinner(new SpinnerNumberModel(5, 0, 100, 1));
        configPanel.add(spinnerListening);

        // Tạo panel chức năng (hàng ngang chứa 3 nút)
        JPanel functionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnRandomExam = new JButton("Random đề thi");
        btnSaveExam = new JButton("Lưu đề thi");
        btnExportPDF = new JButton("Xuất ra file PDF");
        functionPanel.add(btnRandomExam);
        functionPanel.add(btnSaveExam);
        functionPanel.add(btnExportPDF);

        // Tạo panel cho tìm kiếm file audio
        JPanel audioPanel = new JPanel(new BorderLayout(5, 0));
        audioPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm file audio"));
        
        JPanel audioControlPanel = new JPanel(new BorderLayout(5, 0));
        audioSearchField = new JTextField();
        audioSearchField.setEditable(false);
        audioSearchField.setToolTipText("Đường dẫn file audio");
        
        JPanel audioButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnBrowseAudio = new JButton("Duyệt...");
        btnPlayAudio = new JButton("Phát");
        btnPlayAudio.setEnabled(false);
        
        audioButtonPanel.add(btnBrowseAudio);
        audioButtonPanel.add(btnPlayAudio);
        
        audioControlPanel.add(audioSearchField, BorderLayout.CENTER);
        audioControlPanel.add(audioButtonPanel, BorderLayout.EAST);
        audioPanel.add(audioControlPanel, BorderLayout.CENTER);

        examTextArea = new JTextArea();
        examTextArea.setFont(new Font("Arial", Font.PLAIN, 14));
        examTextArea.setLineWrap(true);
        examTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(examTextArea);

        JPanel controlPanel = new JPanel(new BorderLayout(5, 10));
        controlPanel.add(configPanel, BorderLayout.NORTH);
        controlPanel.add(functionPanel, BorderLayout.CENTER);
        controlPanel.add(audioPanel, BorderLayout.SOUTH);

        JPanel examPanel = new JPanel(new BorderLayout(5, 5));
        examPanel.add(controlPanel, BorderLayout.NORTH);
        examPanel.add(scrollPane, BorderLayout.CENTER);

        contentPane.add(examPanel, BorderLayout.CENTER);

        new ExamController(this);
    }
    
    // Getter methods for all components
    public JTextArea getExamTextArea() {
        return examTextArea;
    }
    
    public JButton getBtnRandomExam() {
        return btnRandomExam;
    }
    
    public JButton getBtnSaveExam() {
        return btnSaveExam;
    }
    
    public JButton getBtnExportPDF() {
        return btnExportPDF;
    }
    
    public JTextField getAudioSearchField() {
        return audioSearchField;
    }
    
    public JButton getBtnBrowseAudio() {
        return btnBrowseAudio;
    }
    
    public JButton getBtnPlayAudio() {
        return btnPlayAudio;
    }
    
    public JComboBox<String> getComboBoxLevel() {
        return comboBoxLevel;
    }
    
    public JSpinner getSpinnerGrammar() {
        return spinnerGrammar;
    }
    
    public JSpinner getSpinnerVocab() {
        return spinnerVocab;
    }
    
    public JSpinner getSpinnerReading() {
        return spinnerReading;
    }
    
    public JSpinner getSpinnerListening() {
        return spinnerListening;
    }
    
    public JTextField getTxtExamTitle() {
        return txtExamTitle;
    }
}