package Controller.Question;

import Models.Answers;
import Models.Questions;
import Dao.QuestionsDAO;
import Dao.AnswersDAO;
import Utils.Utils;
import View.Question.UpdateQuestionUI;
import View.Question.ShowQuestionUI;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpdateQuestionController implements ActionListener {

    private UpdateQuestionUI updateQuestionUI;
    private QuestionsDAO questionsDAO;
    private AnswersDAO answersDAO;
    private int questionId;
    private Questions currentQuestion; // To store the original question data including answer IDs
    private List<Answers> currentAnswers; // To store original answers with their IDs

    private File selectedImageFile = null;
    private File selectedAudioFile = null;

    public UpdateQuestionController(UpdateQuestionUI updateQuestionUI, int questionId) {
        this.updateQuestionUI = updateQuestionUI;
        this.questionsDAO = new QuestionsDAO();
        this.answersDAO = new AnswersDAO();
        this.questionId = questionId;

        registerActionListeners();
        loadQuestionData();
    }

    private void registerActionListeners() {
        updateQuestionUI.getBtnSaveChanges().addActionListener(this);
        updateQuestionUI.getBtnCancel().addActionListener(this);
        updateQuestionUI.getBtnBrowseImage().addActionListener(this);
        updateQuestionUI.getBtnBrowseAudio().addActionListener(this);
    }

    private void loadQuestionData() {
        currentQuestion = questionsDAO.selectById(new Questions(questionId, null, null, null, null, null));
        if (currentQuestion != null) {
            currentAnswers = currentQuestion.getAnswers(); // Assumes selectById fetches answers
            updateQuestionUI.setQuestionData(currentQuestion, currentAnswers);
        } else {
            JOptionPane.showMessageDialog(updateQuestionUI, "Không thể tải dữ liệu câu hỏi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            updateQuestionUI.dispose();
            // Optionally, open ShowQuestionUI again
            ShowQuestionUI showUI = new ShowQuestionUI();
            showUI.setVisible(true);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == updateQuestionUI.getBtnSaveChanges()) {
            saveQuestionChanges();
        } else if (source == updateQuestionUI.getBtnCancel()) {
            cancelUpdate();
        } else if (source == updateQuestionUI.getBtnBrowseImage()) {
            handleImageUpload();
        } else if (source == updateQuestionUI.getBtnBrowseAudio()) {
            handleAudioUpload();
        }
    }

    private boolean validateInputs() {
        if (updateQuestionUI.getTxtAreaContent().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(updateQuestionUI, "Vui lòng nhập nội dung câu hỏi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (updateQuestionUI.getComboBoxSection().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(updateQuestionUI, "Vui lòng chọn phần cho câu hỏi!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (updateQuestionUI.getTxtAnswerA().getText().trim().isEmpty() ||
            updateQuestionUI.getTxtAnswerB().getText().trim().isEmpty() ||
            updateQuestionUI.getTxtAnswerC().getText().trim().isEmpty() ||
            updateQuestionUI.getTxtAnswerD().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(updateQuestionUI, "Vui lòng nhập đầy đủ 4 đáp án!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!updateQuestionUI.getRadioAnswerA().isSelected() &&
            !updateQuestionUI.getRadioAnswerB().isSelected() &&
            !updateQuestionUI.getRadioAnswerC().isSelected() &&
            !updateQuestionUI.getRadioAnswerD().isSelected()) {
            JOptionPane.showMessageDialog(updateQuestionUI, "Vui lòng chọn đáp án đúng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        // Additional validation for Listening section and audio file
        if (updateQuestionUI.getComboBoxSection().getSelectedItem().toString().equals("Listening") &&
            (updateQuestionUI.getTxtAudioPath().getText().trim().isEmpty() && selectedAudioFile == null)) {
             if (currentQuestion.getAudioPath() == null || currentQuestion.getAudioPath().trim().isEmpty()){
                JOptionPane.showMessageDialog(updateQuestionUI, "Vui lòng chọn file âm thanh cho câu hỏi Listening!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
             }
        }
        return true;
    }

    private void saveQuestionChanges() {
        if (!validateInputs()) {
            return;
        }

        Questions updatedQuestion = new Questions(
            questionId,
            updateQuestionUI.getTxtAreaContent().getText().trim(),
            updateQuestionUI.getComboBoxSection().getSelectedItem().toString(),
            selectedAudioFile != null ? selectedAudioFile.getAbsolutePath() : updateQuestionUI.getTxtAudioPath().getText(),
            selectedImageFile != null ? selectedImageFile.getAbsolutePath() : updateQuestionUI.getTxtImagePath().getText(),
            null // Answers will be handled separately
        );

        // If section is not Listening, clear audio path
        if (!updatedQuestion.getSection().equals("Listening")) {
            updatedQuestion.setAudioPath(null);
        }
        
        int questionResult = questionsDAO.update(updatedQuestion);

        if (questionResult > 0) {

            ArrayList<Answers> newAnswers = new ArrayList<>();
            newAnswers.add(new Answers(currentAnswers.get(0).getId(), questionId, updateQuestionUI.getTxtAnswerA().getText().trim(), updateQuestionUI.getRadioAnswerA().isSelected()));
            newAnswers.add(new Answers(currentAnswers.get(1).getId(), questionId, updateQuestionUI.getTxtAnswerB().getText().trim(), updateQuestionUI.getRadioAnswerB().isSelected()));
            newAnswers.add(new Answers(currentAnswers.get(2).getId(), questionId, updateQuestionUI.getTxtAnswerC().getText().trim(), updateQuestionUI.getRadioAnswerC().isSelected()));
            newAnswers.add(new Answers(currentAnswers.get(3).getId(), questionId, updateQuestionUI.getTxtAnswerD().getText().trim(), updateQuestionUI.getRadioAnswerD().isSelected()));

            boolean allAnswersUpdated = true;
            for (Answers ans : newAnswers) {
                if (answersDAO.update(ans) <= 0) {
                    allAnswersUpdated = false;

                }
            }

            if (allAnswersUpdated) {
                JOptionPane.showMessageDialog(updateQuestionUI, "Cập nhật câu hỏi thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                updateQuestionUI.dispose();
                ShowQuestionUI showUI = new ShowQuestionUI(); // Go back to the list
                showUI.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(updateQuestionUI, "Cập nhật thông tin câu hỏi thành công, nhưng có lỗi khi cập nhật một số đáp án.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(updateQuestionUI, "Không thể cập nhật câu hỏi.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelUpdate() {
        int response = JOptionPane.showConfirmDialog(updateQuestionUI,
            "Bạn có chắc muốn hủy bỏ các thay đổi?",
            "Xác nhận",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            updateQuestionUI.dispose();
            ShowQuestionUI showUI = new ShowQuestionUI(); // Go back to the list
            showUI.setVisible(true);
        }
    }

    private void handleImageUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file hình ảnh");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg", "gif"));
        int returnValue = fileChooser.showOpenDialog(updateQuestionUI);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            updateQuestionUI.getTxtImagePath().setText(selectedImageFile.getAbsolutePath());
        }
    }

    private void handleAudioUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file âm thanh");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Audio files", "mp3", "wav", "ogg"));
        int returnValue = fileChooser.showOpenDialog(updateQuestionUI);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedAudioFile = fileChooser.getSelectedFile();
            updateQuestionUI.getTxtAudioPath().setText(selectedAudioFile.getAbsolutePath());
        }
    }
}