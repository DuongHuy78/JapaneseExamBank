package View.Exam;

import Models.Exams;
import View.UI.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controller.Exam.ShowExamController;

import java.awt.*;
import java.util.ArrayList;

public class ShowExamUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableExams;
    private DefaultTableModel tableModel;
    private JButton btnViewExam;
    private JButton btnEditExam;
    private JButton btnDeleteExam;
    private JButton btnExportPDF;
    private JButton btnBack;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JComboBox<String> comboLevel;

    public ShowExamUI() {
        setTitle("Danh sách đề thi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        UI.createMenuBar(this);

        // Create search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm đề thi"));
        
        JPanel searchControlPanel = new JPanel(new BorderLayout(5, 0));
        
        txtSearch = new JTextField();
        txtSearch.setToolTipText("Nhập từ khóa tìm kiếm");
        
        JPanel searchOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        comboLevel = new JComboBox<>(new String[]{"Tất cả cấp độ", "N1", "N2", "N3", "N4", "N5"});
        btnSearch = new JButton("Tìm kiếm");
        
        searchOptionsPanel.add(new JLabel("Cấp độ:"));
        searchOptionsPanel.add(comboLevel);
        searchOptionsPanel.add(btnSearch);
        
        searchControlPanel.add(txtSearch, BorderLayout.CENTER);
        searchControlPanel.add(searchOptionsPanel, BorderLayout.EAST);
        searchPanel.add(searchControlPanel, BorderLayout.CENTER);

        // Create table
        String[] columnNames = {"ID", "Tiêu đề", "Cấp độ", "Số câu hỏi"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        
        tableExams = new JTable(tableModel);
        tableExams.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableExams.getTableHeader().setReorderingAllowed(false);
        tableExams.setRowHeight(25);
        
        // Set column widths
        tableExams.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tableExams.getColumnModel().getColumn(1).setPreferredWidth(350); // Title
        tableExams.getColumnModel().getColumn(2).setPreferredWidth(80); // Level
        tableExams.getColumnModel().getColumn(3).setPreferredWidth(100); // Question count
        
        JScrollPane scrollPane = new JScrollPane(tableExams);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách đề thi"));

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnViewExam = new JButton("Xem chi tiết");
        btnEditExam = new JButton("Chỉnh sửa");
        btnDeleteExam = new JButton("Xóa");
        btnExportPDF = new JButton("Xuất PDF");
        btnBack = new JButton("Quay lại");
        
        buttonsPanel.add(btnViewExam);
        buttonsPanel.add(btnEditExam);
        buttonsPanel.add(btnDeleteExam);
        buttonsPanel.add(btnExportPDF);
        buttonsPanel.add(btnBack);

        // Add components to content pane
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        contentPane.add(mainPanel, BorderLayout.CENTER);

        new ShowExamController(this);
    }
    
    // Update table with exam data
    public void updateTable(ArrayList<Exams> exams) {
        // Clear existing data
        tableModel.setRowCount(0);
        
        // Add exams to table
        if (exams != null) {
            for (Exams exam : exams) {
                int questionCount = 0;
                if (exam.getExamQuestions() != null) {
                    questionCount = exam.getExamQuestions().size();
                }
                
                Object[] rowData = {
                    exam.getId(),
                    exam.getTitle(),
                    exam.getLevel(),
                    questionCount,
                    "N/A" // Date would be added if available in the Exams model
                };
                
                tableModel.addRow(rowData);
            }
        }
    }
    
    // Getter methods for components
    public JTable getTableExams() {
        return tableExams;
    }
    
    public JButton getBtnViewExam() {
        return btnViewExam;
    }
    
    public JButton getBtnEditExam() {
        return btnEditExam;
    }
    
    public JButton getBtnDeleteExam() {
        return btnDeleteExam;
    }
    
    public JButton getBtnExportPDF() {
        return btnExportPDF;
    }
    
    public JButton getBtnBack() {
        return btnBack;
    }
    
    public JTextField getTxtSearch() {
        return txtSearch;
    }
    
    public JButton getBtnSearch() {
        return btnSearch;
    }
    
    public JComboBox<String> getComboLevel() {
        return comboLevel;
    }
    
    public DefaultTableModel getTableModel() {
        return tableModel;
    }
    
    // Get selected exam ID from table
    public int getSelectedExamId() {
        int selectedRow = tableExams.getSelectedRow();
        if (selectedRow >= 0) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1;
    }
}