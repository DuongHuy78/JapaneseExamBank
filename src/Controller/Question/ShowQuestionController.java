package Controller.Question;

import Models.Questions;
import Models.Answers;
import Dao.QuestionsDAO;
import Dao.AnswersDAO;
import View.Question.ShowQuestionUI;
import View.Question.UpdateQuestionUI;
import View.UI.UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ShowQuestionController implements ActionListener {

    private ShowQuestionUI showQuestionUI;
    private QuestionsDAO questionsDAO;
    private AnswersDAO answersDAO;

    public ShowQuestionController(ShowQuestionUI showQuestionUI) {
        this.showQuestionUI = showQuestionUI;
        this.questionsDAO = new QuestionsDAO();
        this.answersDAO = new AnswersDAO();

        // Register action listeners
        registerActionListeners();

        // Load all questions initially
        loadAllQuestions();
    }

    private void registerActionListeners() {
        showQuestionUI.getBtnViewQuestion().addActionListener(this);
        showQuestionUI.getBtnEditQuestion().addActionListener(this);
        showQuestionUI.getBtnDeleteQuestion().addActionListener(this);
        showQuestionUI.getBtnBack().addActionListener(this);
        showQuestionUI.getBtnSearch().addActionListener(this);
        showQuestionUI.getComboSectionSearch().addActionListener(this); // Also search when section changes
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == showQuestionUI.getBtnViewQuestion()) {
            viewQuestionDetails();
        } else if (source == showQuestionUI.getBtnEditQuestion()) {
            editQuestion();
        } else if (source == showQuestionUI.getBtnDeleteQuestion()) {
            deleteQuestion();
        } else if (source == showQuestionUI.getBtnBack()) {
            goBack();
        } else if (source == showQuestionUI.getBtnSearch() || source == showQuestionUI.getComboSectionSearch()) {
            searchQuestions();
        }
    }

    private void loadAllQuestions() {
        ArrayList<Questions> questions = questionsDAO.selectAll();
        showQuestionUI.updateTable(questions);
    }

    private void viewQuestionDetails() {
        int questionId = showQuestionUI.getSelectedQuestionId();
        if (questionId < 0) {
            JOptionPane.showMessageDialog(showQuestionUI, "Vui lòng chọn một câu hỏi để xem chi tiết.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Questions question = questionsDAO.selectById(new Questions(questionId, null, null, null, null, null));
        if (question != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Chi tiết câu hỏi:\n\n");
            sb.append("ID: ").append(question.getId()).append("\n");
            sb.append("Nội dung: ").append(question.getContent()).append("\n");
            sb.append("Phần: ").append(question.getSection()).append("\n");
            if (question.getImagePath() != null && !question.getImagePath().isEmpty()) {
                sb.append("Đường dẫn ảnh: ").append(question.getImagePath()).append("\n");
            }
            if (question.getAudioPath() != null && !question.getAudioPath().isEmpty()) {
                sb.append("Đường dẫn audio: ").append(question.getAudioPath()).append("\n");
            }

            sb.append("\nCác đáp án:\n");
            List<Answers> answers = question.getAnswers(); // Assumes getAnswers() fetches them
            if (answers != null && !answers.isEmpty()) {
                char option = 'A';
                for (Answers ans : answers) {
                    sb.append(option++).append(". ").append(ans.getContent());
                    if (ans.isCorrect()) {
                        sb.append(" (Đáp án đúng)");
                    }
                    sb.append("\n");
                }
            } else {
                sb.append("Không có đáp án nào cho câu hỏi này.\n");
            }

            JTextArea textArea = new JTextArea(sb.toString());
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new java.awt.Dimension(500, 400));

            JOptionPane.showMessageDialog(showQuestionUI, scrollPane, "Chi tiết câu hỏi", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(showQuestionUI, "Không thể tải thông tin câu hỏi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editQuestion() {
        int questionId = showQuestionUI.getSelectedQuestionId();
        if (questionId < 0) {
            JOptionPane.showMessageDialog(showQuestionUI, "Vui lòng chọn một câu hỏi để chỉnh sửa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        UpdateQuestionUI updateQuestionUI = new UpdateQuestionUI(questionId);
        updateQuestionUI.setVisible(true);
        showQuestionUI.dispose();
    }

    private void deleteQuestion() {
        int questionId = showQuestionUI.getSelectedQuestionId();
        if (questionId < 0) {
            JOptionPane.showMessageDialog(showQuestionUI, "Vui lòng chọn một câu hỏi để xóa.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int option = JOptionPane.showConfirmDialog(showQuestionUI, "Bạn có chắc chắn muốn xóa câu hỏi này và các đáp án liên quan?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            ArrayList<Answers> answers = (ArrayList<Answers>)answersDAO.selectByCondition("QuestionId = " + questionId);
            for (Answers answer : answers) {
                answersDAO.delete(answer);
            }

            // Then, delete the question
            Questions questionToDelete = new Questions(questionId, null, null, null, null, null);
            int result = questionsDAO.delete(questionToDelete);

            if (result > 0) {
                JOptionPane.showMessageDialog(showQuestionUI, "Đã xóa câu hỏi thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                loadAllQuestions(); // Refresh the table
            } else {
                JOptionPane.showMessageDialog(showQuestionUI, "Không thể xóa câu hỏi. Có thể câu hỏi này đang được sử dụng trong một đề thi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void goBack() {
        // Assuming you have a main UI or Question Management UI to go back to
        // For example, going back to the main UI
        UI mainUI = new UI(); 
        mainUI.setVisible(true);
        showQuestionUI.dispose(); // Close current UI
    }

    private void searchQuestions() {
        String keyword = showQuestionUI.getTxtSearch().getText().trim().toLowerCase();
        String selectedSection = showQuestionUI.getComboSectionSearch().getSelectedItem().toString();

        StringBuilder condition = new StringBuilder();

        if (!keyword.isEmpty()) {
            // Escape dấu nháy đơn trong keyword nếu cần
            String safeKeyword = keyword.replace("'", "''");
            condition.append("LOWER(Content) LIKE '%").append(safeKeyword).append("%'");
        }

        if (!selectedSection.equals("Tất cả")) {
            if (condition.length() > 0) {
                condition.append(" AND ");
            }
            // Escape dấu nháy đơn trong section nếu cần
            String safeSection = selectedSection.replace("'", "''");
            condition.append("Section = '").append(safeSection).append("'");
        }

        ArrayList<Questions> filteredQuestions;
        if (condition.length() > 0) {
            filteredQuestions = questionsDAO.selectByCondition(condition.toString());
        } else {
            filteredQuestions = questionsDAO.selectAll();
        }
        showQuestionUI.updateTable(filteredQuestions);
    }
}