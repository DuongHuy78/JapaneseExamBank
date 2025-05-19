package Controller.Exam;

import Dao.ExamsDAO;
import Dao.Exam_QuestionsDAO;
import Dao.QuestionsDAO;
import Models.Exams;
import Models.Exam_Questions;
import Models.Questions;
import View.Exam.ShowExamUI;
import View.Exam.UpdateExamUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UpdateExamController implements ActionListener {

    private UpdateExamUI updateExamUI;
    private ExamsDAO examsDAO = new ExamsDAO();
    private QuestionsDAO questionsDAO = new QuestionsDAO();
    private Exam_QuestionsDAO examQuestionsDAO = new Exam_QuestionsDAO();
    private Exams currentExam;
    private ArrayList<Questions> availableQuestions;
    private ArrayList<Exam_Questions> examQuestions;

    public UpdateExamController(UpdateExamUI updateExamUI) {
        this.updateExamUI = updateExamUI;
        
        registerActionListeners();
        
        loadExamData();
        
        loadAvailableQuestions();
    }

    private void registerActionListeners() {
        updateExamUI.getBtnAddQuestion().addActionListener(this);
        updateExamUI.getBtnRemoveQuestion().addActionListener(this);
        updateExamUI.getBtnSave().addActionListener(this);
        updateExamUI.getBtnCancel().addActionListener(this);
        updateExamUI.getBtnSearch().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == updateExamUI.getBtnAddQuestion()) {
            addQuestion();
        } 
        else if (source == updateExamUI.getBtnRemoveQuestion()) {
            removeQuestion();
        } 
        else if (source == updateExamUI.getBtnSave()) {
            saveExam();
        }
        else if (source == updateExamUI.getBtnCancel()) {
            cancel();
        }
        else if (source == updateExamUI.getBtnSearch()) {
            searchQuestions();
        }
    }
    
    private void loadExamData() {
        int examId = updateExamUI.getExamId();
        currentExam = examsDAO.selectById(new Exams(examId, "", ""));
        
        if (currentExam != null) {
            updateExamUI.getTxtTitle().setText(currentExam.getTitle());
            
            String level = currentExam.getLevel();
            for (int i = 0; i < updateExamUI.getComboLevel().getItemCount(); i++) {
                if (updateExamUI.getComboLevel().getItemAt(i).equals(level)) {
                    updateExamUI.getComboLevel().setSelectedIndex(i);
                    break;
                }
            }
            
            loadExamQuestions();
        } else {
            JOptionPane.showMessageDialog(updateExamUI,
                "Không thể tải thông tin đề thi.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            cancel();
        }
    }
    
    private void loadExamQuestions() {
        examQuestions = examQuestionsDAO.selectByCondition("ExamID = " + currentExam.getId());
        
        if (examQuestions != null) {
            updateExamUI.getTableModel().setRowCount(0);
            
            Collections.sort(examQuestions, new Comparator<Exam_Questions>() {
                @Override
                public int compare(Exam_Questions eq1, Exam_Questions eq2) {
                    return Integer.compare(eq1.getQuestionOrder(), eq2.getQuestionOrder());
                }
            });
            
            for (Exam_Questions eq : examQuestions) {
                Questions question = questionsDAO.selectById(new Questions(eq.getQuestionId(), "", "", "", ""));
                
                if (question != null) {
                    Object[] rowData = {
                        question.getId(),
                        question.getContent().length() > 50 ? 
                            question.getContent().substring(0, 47) + "..." : 
                            question.getContent(),
                        question.getSection(),
                        eq.getQuestionOrder()
                    };
                    
                    updateExamUI.getTableModel().addRow(rowData);
                }
            }
        }
    }
    
    private void loadAvailableQuestions() {
        availableQuestions = questionsDAO.selectAll();
    }
    
    private void addQuestion() {
        JDialog dialog = new JDialog(updateExamUI, "Chọn câu hỏi để thêm", true);
        dialog.setSize(700, 500);
        dialog.setLocationRelativeTo(updateExamUI);
        dialog.setLayout(new BorderLayout(10, 10));
        
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Tìm kiếm");
        JComboBox<String> sectionCombo = new JComboBox<>(
            new String[]{"Tất cả phần", "Grammar", "Vocabulary", "Reading", "Listening"});
        
        JPanel searchOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchOptions.add(new JLabel("Phần:"));
        searchOptions.add(sectionCombo);
        searchOptions.add(searchButton);
        
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchOptions, BorderLayout.EAST);
        
        String[] columnNames = {"ID", "Nội dung", "Phần"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable questionTable = new JTable(tableModel);
        questionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        questionTable.getTableHeader().setReorderingAllowed(false);
        
        questionTable.getColumnModel().getColumn(0).setPreferredWidth(50);
        questionTable.getColumnModel().getColumn(1).setPreferredWidth(350);
        questionTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        
        JScrollPane scrollPane = new JScrollPane(questionTable);
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));      
        JButton addButton = new JButton("Thêm");
        JButton cancelButton = new JButton("Hủy");
        buttonsPanel.add(addButton);
        buttonsPanel.add(cancelButton);
        
        dialog.add(searchPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);
        
        for (Questions q : availableQuestions) {
            boolean alreadyInExam = false;
            for (Exam_Questions eq : examQuestions) {
                if (eq.getQuestionId() == q.getId()) {
                    alreadyInExam = true;
                    break;
                }
            }
            
            if (!alreadyInExam) {
                Object[] rowData = {
                    q.getId(),
                    q.getContent().length() > 50 ? 
                        q.getContent().substring(0, 47) + "..." : 
                        q.getContent(),
                    q.getSection(),
                };
                
                tableModel.addRow(rowData);
            }
        }
        
        addButton.addActionListener(e -> {
            int selectedRow = questionTable.getSelectedRow();
            if (selectedRow >= 0) {
                int questionId = (int) tableModel.getValueAt(selectedRow, 0);
                String content = (String) tableModel.getValueAt(selectedRow, 1);
                String section = (String) tableModel.getValueAt(selectedRow, 2);
                
                int nextOrder = 1;
                if (!examQuestions.isEmpty()) {
                    for (Exam_Questions eq : examQuestions) {
                        if (eq.getQuestionOrder() >= nextOrder) {
                            nextOrder = eq.getQuestionOrder() + 1;
                        }
                    }
                }
                
                Exam_Questions newEq = new Exam_Questions(currentExam.getId(), questionId, nextOrder);
                examQuestions.add(newEq);
                
                Object[] rowData = {
                    questionId,
                    content,
                    section,
                    nextOrder
                };
                
                updateExamUI.getTableModel().addRow(rowData);
                
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Vui lòng chọn một câu hỏi để thêm.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        searchButton.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            String section = sectionCombo.getSelectedItem().toString();
            
   
            tableModel.setRowCount(0);
            
            for (Questions q : availableQuestions) {
                boolean alreadyInExam = false;
                for (Exam_Questions eq : examQuestions) {
                    if (eq.getQuestionId() == q.getId()) {
                        alreadyInExam = true;
                        break;
                    }
                }
                
                if (!alreadyInExam) {
                    boolean matchesKeyword = keyword.isEmpty() || 
                                            q.getContent().toLowerCase().contains(keyword.toLowerCase());
                    boolean matchesSection = section.equals("Tất cả phần") || 
                                            q.getSection().equals(section);
                    
                    if (matchesKeyword && matchesSection ) {
                        Object[] rowData = {
                            q.getId(),
                            q.getContent().length() > 50 ? 
                                q.getContent().substring(0, 47) + "..." : 
                                q.getContent(),
                            q.getSection()
                        };
                        
                        tableModel.addRow(rowData);
                    }
                }
            }
        });
        
        dialog.setVisible(true);
    }
    
    private void removeQuestion() {
        int selectedRow = updateExamUI.getTableQuestions().getSelectedRow();
        if (selectedRow >= 0) {
            int questionId = (int) updateExamUI.getTableModel().getValueAt(selectedRow, 0);
            
            for (int i = 0; i < examQuestions.size(); i++) {
                if (examQuestions.get(i).getQuestionId() == questionId) {
                    examQuestions.remove(i);
                    break;
                }
            }
            
            updateExamUI.getTableModel().removeRow(selectedRow);
            int newQuestionOrder = 1;
            for (int i = 0; i < updateExamUI.getTableModel().getRowCount(); i++) {
                int questionOrder = (int) updateExamUI.getTableModel().getValueAt(i, 3);
                if (questionOrder > newQuestionOrder) {
                    newQuestionOrder = questionOrder;
                }
            }
            
            updateQuestionOrders();
        } else {
            JOptionPane.showMessageDialog(updateExamUI,
                "Vui lòng chọn một câu hỏi để xóa.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateQuestionOrders() {
        DefaultTableModel model = updateExamUI.getTableModel();
        int rowCount = model.getRowCount();
        
        for (int i = 0; i < rowCount; i++) {
            int questionId = (int) model.getValueAt(i, 0);
            int newOrder = i + 1;
            
            model.setValueAt(newOrder, i, 3);
            
            for (Exam_Questions eq : examQuestions) {
                if (eq.getQuestionId() == questionId) {
                    eq.setQuestionOrder(newOrder);
                    break;
                }
            }
        }
    }
    
    private void saveExam() {
        String title = updateExamUI.getTxtTitle().getText().trim();
        String level = updateExamUI.getComboLevel().getSelectedItem().toString();
        
        // Validate input
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(updateExamUI,
                "Vui lòng nhập tiêu đề đề thi.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        currentExam.setTitle(title);
        currentExam.setLevel(level);
        
        int result = examsDAO.update(currentExam);
        System.out.println("Exam ID: " + currentExam.getId());
        if (result > 0) {
            ArrayList<Exam_Questions> oldExamQuestions = 
                examQuestionsDAO.selectByCondition("ExamID = " + currentExam.getId());
                
            if (oldExamQuestions != null) {
                for (Exam_Questions eq : oldExamQuestions) {
                    examQuestionsDAO.delete(eq);
                }
            }
            
            boolean success = true;
            for (Exam_Questions eq : examQuestions) {
                int insertResult = examQuestionsDAO.insert(eq);
                if (insertResult <= 0) {
                    success = false;
                }
            }
            
            if (success) {
                JOptionPane.showMessageDialog(updateExamUI,
                    "Đã cập nhật đề thi thành công.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                
                ShowExamUI showExamUI = new ShowExamUI();
                ShowExamController controller = new ShowExamController(showExamUI);
                showExamUI.setVisible(true);
                updateExamUI.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(updateExamUI,
                    "Đã cập nhật thông tin đề thi nhưng có lỗi khi cập nhật danh sách câu hỏi.",
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(updateExamUI,
                "Không thể cập nhật đề thi.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void cancel() {
        ShowExamUI showExamUI = new ShowExamUI();
        ShowExamController controller = new ShowExamController(showExamUI);
        showExamUI.setVisible(true);
        updateExamUI.setVisible(false);
    }
    
    private void searchQuestions() {
    	String keyword = updateExamUI.getTxtSearch().getText();
        if (keyword == null || keyword.trim().isEmpty()) {
            updateExamUI.getTableModel().setRowCount(0);
            this.loadExamQuestions();
            return;
        }
    	
        //lấy dữ liệu từ bảng câu hỏi
        ArrayList<String> tableData = new ArrayList<>();
        for(Exam_Questions eq: examQuestions) {
            Questions question = questionsDAO.selectById(new Questions(eq.getQuestionId(), "", "", "", ""));
            if (question != null) {
                String data = "";
                data += question.getId() + "\t";
                data += question.getContent() + "\t";
                data += question.getSection() + "\t";
                data += eq.getQuestionOrder() + "\t";
                tableData.add(data);
            }
        }

        updateExamUI.getTableModel().setRowCount(0);
        for (String data : tableData) {
            if(data.toLowerCase().contains(updateExamUI.getTxtSearch().getText().toLowerCase())) {
                int questionId = Integer.parseInt(data.split("\t")[0]);
                String content = data.split("\t")[1];
                String section = data.split("\t")[2];
                int questionOrder = Integer.parseInt(data.split("\t")[3]);

                Object[] rowData = {
                        questionId,
                        content.length() > 50 ? 
                            content.substring(0, 47) + "..." : 
                            content,
                        section,
                        questionOrder
                    };
                    
                    updateExamUI.getTableModel().addRow(rowData);
            }
        }
    }
}