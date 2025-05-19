package View.Exam;

import View.UI.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controller.Exam.UpdateExamController;

import java.awt.*;

public class UpdateExamUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtTitle;
    private JComboBox<String> comboLevel;
    private JTable tableQuestions;
    private DefaultTableModel tableModel;
    private JButton btnAddQuestion;
    private JButton btnRemoveQuestion;
    private JButton btnSave;
    private JButton btnCancel;
    private JButton btnSearch;
    private JTextField txtSearch;
    private JComboBox<String> comboSection;
    private int examId;

    public UpdateExamUI(int examId) {
        this.examId = examId;
        setTitle("Cập nhật đề thi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        UI.createMenuBar(this);

        JPanel examInfoPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        examInfoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin đề thi"));
        
        examInfoPanel.add(new JLabel("Tiêu đề:"));
        txtTitle = new JTextField();
        examInfoPanel.add(txtTitle);
        
        examInfoPanel.add(new JLabel("Cấp độ:"));
        comboLevel = new JComboBox<>(new String[]{"N1", "N2", "N3", "N4", "N5"});
        examInfoPanel.add(comboLevel);

        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm câu hỏi"));
        
        JPanel searchControlPanel = new JPanel(new BorderLayout(5, 0));
        
        txtSearch = new JTextField();
        txtSearch.setToolTipText("Nhập từ khóa tìm kiếm");
        
        JPanel searchOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        comboSection = new JComboBox<>(new String[]{"Tất cả phần", "Grammar", "Vocabulary", "Reading", "Listening"});
        btnSearch = new JButton("Tìm kiếm");
        
        searchOptionsPanel.add(new JLabel("Phần:"));
        searchOptionsPanel.add(comboSection);
        searchOptionsPanel.add(btnSearch);
        
        searchControlPanel.add(txtSearch, BorderLayout.CENTER);
        searchControlPanel.add(searchOptionsPanel, BorderLayout.EAST);
        searchPanel.add(searchControlPanel, BorderLayout.CENTER);

        String[] columnNames = {"ID", "Nội dung", "Phần", "Thứ tự"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only allow editing the order column
            }
        };
        
        tableQuestions = new JTable(tableModel);
        tableQuestions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableQuestions.getTableHeader().setReorderingAllowed(false);
        tableQuestions.setRowHeight(25);
        
        tableQuestions.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tableQuestions.getColumnModel().getColumn(1).setPreferredWidth(350); // Content
        tableQuestions.getColumnModel().getColumn(2).setPreferredWidth(80); // Section
        tableQuestions.getColumnModel().getColumn(3).setPreferredWidth(80); // Level
        
        JScrollPane scrollPane = new JScrollPane(tableQuestions);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách câu hỏi trong đề thi"));

        JPanel questionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        btnAddQuestion = new JButton("Thêm câu hỏi");
        btnRemoveQuestion = new JButton("Xóa câu hỏi");
        
        questionButtonsPanel.add(btnAddQuestion);
        questionButtonsPanel.add(btnRemoveQuestion);

        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnSave = new JButton("Lưu thay đổi");
        btnCancel = new JButton("Hủy bỏ");
        
        actionButtonsPanel.add(btnSave);
        actionButtonsPanel.add(btnCancel);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(examInfoPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        
        JPanel questionsPanel = new JPanel(new BorderLayout(10, 5));
        questionsPanel.add(scrollPane, BorderLayout.CENTER);
        questionsPanel.add(questionButtonsPanel, BorderLayout.SOUTH);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(questionsPanel, BorderLayout.CENTER);
        mainPanel.add(actionButtonsPanel, BorderLayout.SOUTH);
        
        contentPane.add(mainPanel, BorderLayout.CENTER);
        
        new UpdateExamController(this);
    }
    
    public int getExamId() {
        return examId;
    }
    
    public JTextField getTxtTitle() {
        return txtTitle;
    }
    
    public JComboBox<String> getComboLevel() {
        return comboLevel;
    }
    
    public JTable getTableQuestions() {
        return tableQuestions;
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    public JButton getBtnAddQuestion() {
        return btnAddQuestion;
    }
    
    public JButton getBtnRemoveQuestion() {
        return btnRemoveQuestion;
    }

    public JButton getBtnSave() {
        return btnSave;
    }
    
    public JButton getBtnCancel() {
        return btnCancel;
    }
    
    public JButton getBtnSearch() {
        return btnSearch;
    }
    
    public JTextField getTxtSearch() {
        return txtSearch;
    }
    
    public JComboBox<String> getComboSection() {
        return comboSection;
    }
    
    public int getSelectedQuestionId() {
        int selectedRow = tableQuestions.getSelectedRow();
        if (selectedRow >= 0) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }
    
    public int getSelectedQuestionOrder() {
        int selectedRow = tableQuestions.getSelectedRow();
        if (selectedRow >= 0) {
            return (int) tableModel.getValueAt(selectedRow, 4);
        }
        return -1;
    }
}