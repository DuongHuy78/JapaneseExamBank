package Controller.Exam;


import Dao.ExamsDAO;
import Dao.Exam_QuestionsDAO;
import Models.Exams;
import Models.Exam_Questions;
import Utils.Utils;
import View.Exam.DetailsExamUI;
import View.Exam.ExamUI;
import View.Exam.ShowExamUI;
import View.Exam.UpdateExamUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ShowExamController implements ActionListener {

    private ShowExamUI showExamUI;
    private ExamsDAO examsDAO = new ExamsDAO();
    private Exam_QuestionsDAO examQuestionsDAO = new Exam_QuestionsDAO();

    public ShowExamController(ShowExamUI showExamUI) {
        this.showExamUI = showExamUI;
        
        // Register action listeners
        registerActionListeners();
        
        // Load all exams initially
        loadAllExams();
    }

    private void registerActionListeners() {
        showExamUI.getBtnViewExam().addActionListener(this);
        showExamUI.getBtnEditExam().addActionListener(this);
        showExamUI.getBtnDeleteExam().addActionListener(this);
        showExamUI.getBtnExportPDF().addActionListener(this);
        showExamUI.getBtnBack().addActionListener(this);
        showExamUI.getBtnSearch().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == showExamUI.getBtnViewExam()) {
            viewExamDetails();
        } 
        else if (source == showExamUI.getBtnEditExam()) {
            editExam();
        } 
        else if (source == showExamUI.getBtnDeleteExam()) {
            deleteExam();
        }
        else if (source == showExamUI.getBtnExportPDF()) {
            exportToPDF();
        }
        else if (source == showExamUI.getBtnBack()) {
            goBack();
        }
        else if (source == showExamUI.getBtnSearch()) {
            searchExams();
        }
    }
    
    private void loadAllExams() {
        ArrayList<Exams> exams = examsDAO.selectAll();
        showExamUI.updateTable(exams);
    }
    
    private void viewExamDetails() {
        int examId = showExamUI.getSelectedExamId();
        
        if (examId < 0) {
            JOptionPane.showMessageDialog(showExamUI,
                "Vui lòng chọn một đề thi để xem chi tiết.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Exams exam = examsDAO.selectById(new Exams(examId, "", ""));
        
        if (exam != null) {
            new DetailsExamUI(examId).setVisible(true);
            showExamUI.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(showExamUI,
                "Không thể tải thông tin đề thi.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editExam() {
        int examId = showExamUI.getSelectedExamId();
        
        if (examId < 0) {
            JOptionPane.showMessageDialog(showExamUI,
                "Vui lòng chọn một đề thi để chỉnh sửa.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Open UpdateExamUI with selected exam ID
        UpdateExamUI updateExamUI = new UpdateExamUI(examId);
        updateExamUI.setVisible(true);
        showExamUI.setVisible(false);
    }
    
    private void deleteExam() {
        int examId = showExamUI.getSelectedExamId();
        
        if (examId < 0) {
            JOptionPane.showMessageDialog(showExamUI,
                "Vui lòng chọn một đề thi để xóa.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int option = JOptionPane.showConfirmDialog(showExamUI,
            "Bạn có chắc chắn muốn xóa đề thi này?",
            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            // Delete exam questions first
            ArrayList<Exam_Questions> examQuestions = examQuestionsDAO.selectByCondition("ExamID = " + examId);
            if (examQuestions != null) {
                for (Exam_Questions eq : examQuestions) {
                    examQuestionsDAO.delete(eq);
                }
            }
            
            // Then delete the exam
            Exams exam = new Exams(examId, "", "");
            int result = examsDAO.delete(exam);
            
            if (result > 0) {
                JOptionPane.showMessageDialog(showExamUI,
                    "Đã xóa đề thi thành công.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    
                // Refresh the table
                loadAllExams();
            } else {
                JOptionPane.showMessageDialog(showExamUI,
                    "Không thể xóa đề thi.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void exportToPDF() {
        int examId = showExamUI.getSelectedExamId();
        
        if (examId < 0) {
            JOptionPane.showMessageDialog(showExamUI,
                "Vui lòng chọn một đề thi để xuất PDF.",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        Exams exam = examsDAO.selectById(new Exams(examId, "", ""));
        
        if (exam != null) {
            // Load exam questions
            ArrayList<Exam_Questions> examQuestions = examQuestionsDAO.selectByCondition("ExamID = " + examId);
            exam.setExamQuestions(examQuestions);
            
            try {
                Utils.exportToPDF(exam);
                JOptionPane.showMessageDialog(showExamUI,
                    "Đã xuất đề thi ra file PDF thành công!",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(showExamUI,
                    "Lỗi khi xuất file PDF: " + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(showExamUI,
                "Không thể tải thông tin đề thi.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void goBack() {
        ExamUI examUI = new ExamUI();
        examUI.setVisible(true);
        showExamUI.setVisible(false);
    }
    
    private void searchExams() {
        String keyword = showExamUI.getTxtSearch().getText().trim();
        String level = showExamUI.getComboLevel().getSelectedItem().toString();
        
        ArrayList<Exams> filteredExams;
        
        // Build the condition for querying exams
        StringBuilder condition = new StringBuilder();
        boolean hasCondition = false;
        
        if (!keyword.isEmpty()) {
            condition.append("Title LIKE '%").append(keyword).append("%'");
            hasCondition = true;
        }
        
        if (!level.equals("Tất cả cấp độ")) {
            if (hasCondition) {
                condition.append(" AND ");
            }
            condition.append("Level = '").append(level).append("'");
            hasCondition = true;
        }
        
        if (hasCondition) {
            filteredExams = examsDAO.selectByCondition(condition.toString());
        } else {
            filteredExams = examsDAO.selectAll();
        }
        
        showExamUI.updateTable(filteredExams);
    }
}