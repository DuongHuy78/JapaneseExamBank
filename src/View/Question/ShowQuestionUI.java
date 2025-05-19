package View.Question;

import Models.Questions;
import View.UI.UI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controller.Question.ShowQuestionController;

import java.awt.*;
import java.util.ArrayList;

public class ShowQuestionUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tableQuestions;
    private DefaultTableModel tableModel;
    private JButton btnViewQuestion;
    private JButton btnEditQuestion;
    private JButton btnDeleteQuestion;
    private JButton btnBack;
    private JTextField txtSearch;
    private JButton btnSearch;
    private JComboBox<String> comboSectionSearch;


    public ShowQuestionUI() {
        setTitle("Danh sách Câu hỏi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 600);

        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        UI.createMenuBar(this);

        // Create search panel
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Tìm kiếm câu hỏi"));

        JPanel searchControlPanel = new JPanel(new BorderLayout(5, 0));
        txtSearch = new JTextField();
        txtSearch.setToolTipText("Nhập từ khóa tìm kiếm (nội dung câu hỏi)");
        searchControlPanel.add(txtSearch, BorderLayout.CENTER);

        btnSearch = new JButton("Tìm kiếm");
        searchControlPanel.add(btnSearch, BorderLayout.EAST);
        
        JPanel searchOptionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        searchOptionsPanel.add(new JLabel("Phần:"));
        comboSectionSearch = new JComboBox<>(new String[]{"Tất cả", "Grammar", "Vocabulary", "Reading", "Listening"});
        searchOptionsPanel.add(comboSectionSearch);

        searchPanel.add(searchControlPanel, BorderLayout.CENTER);
        searchPanel.add(searchOptionsPanel, BorderLayout.SOUTH);


        // Create table
        String[] columnNames = {"ID", "Nội dung", "Phần"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        tableQuestions = new JTable(tableModel);
        tableQuestions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableQuestions.getTableHeader().setReorderingAllowed(false);
        tableQuestions.setRowHeight(25);

        // Set column widths
        tableQuestions.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tableQuestions.getColumnModel().getColumn(1).setPreferredWidth(550); // Content
        tableQuestions.getColumnModel().getColumn(2).setPreferredWidth(100); // Section

        JScrollPane scrollPane = new JScrollPane(tableQuestions);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Danh sách câu hỏi"));

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnViewQuestion = new JButton("Xem chi tiết");
        btnEditQuestion = new JButton("Chỉnh sửa");
        btnDeleteQuestion = new JButton("Xóa");
        btnBack = new JButton("Quay lại");

        buttonsPanel.add(btnViewQuestion);
        buttonsPanel.add(btnEditQuestion);
        buttonsPanel.add(btnDeleteQuestion);
        buttonsPanel.add(btnBack);

        // Add components to content pane
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.add(searchPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        contentPane.add(mainPanel, BorderLayout.CENTER);

        new ShowQuestionController(this);
    }

    // Update table with question data
    public void updateTable(ArrayList<Questions> questions) {
        // Clear existing data
        tableModel.setRowCount(0);

        // Add questions to table
        if (questions != null) {
            for (Questions question : questions) {
                Object[] rowData = {
                    question.getId(),
                    question.getContent().length() > 100 ? question.getContent().substring(0, 97) + "..." : question.getContent(), // Preview content
                    question.getSection()
                };
                tableModel.addRow(rowData);
            }
        }
    }

    // Getter methods for components
    public JTable getTableQuestions() {
        return tableQuestions;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getBtnViewQuestion() {
        return btnViewQuestion;
    }

    public JButton getBtnEditQuestion() {
        return btnEditQuestion;
    }

    public JButton getBtnDeleteQuestion() {
        return btnDeleteQuestion;
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
    
    public JComboBox<String> getComboSectionSearch() {
        return comboSectionSearch;
    }

    // Get selected question ID from table
    public int getSelectedQuestionId() {
        int selectedRow = tableQuestions.getSelectedRow();
        if (selectedRow >= 0) {
            return (int) tableModel.getValueAt(selectedRow, 0);
        }
        return -1; // Return -1 if no row is selected
    }
}