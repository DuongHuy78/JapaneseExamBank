package Controller.Question;

import View.Question.QuestionUI;
import View.UI.UI;
import Models.Answers;
import Models.Questions;
import Utils.Utils;
import Dao.AnswersDAO;
import Dao.QuestionsDAO;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class QuestionController implements ActionListener {

    private QuestionUI questionUI;
    private AnswersDAO aDao = new AnswersDAO();
    private QuestionsDAO qDao = new QuestionsDAO();
    
    private File selectedImageFile;
    private File selectedAudioFile;

    public QuestionController(QuestionUI qUi) {
        this.questionUI = qUi;
        
        // Register all action listeners
        registerActionListeners();
    }

    private void registerActionListeners() {
        questionUI.getBtnBrowseImage().addActionListener(this);
        questionUI.getBtnBrowseAudio().addActionListener(this);
        questionUI.getBtnSave().addActionListener(this);
        questionUI.getBtnCancel().addActionListener(this);
        questionUI.getBtnClear().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == questionUI.getBtnBrowseImage()) {
            handleImageUpload();
        } 
        else if (source == questionUI.getBtnBrowseAudio()) {
            handleAudioUpload();
        } 
        else if (source == questionUI.getBtnSave()) {
            saveQuestion();
        } 
        else if (source == questionUI.getBtnCancel()) {
            cancelQuestionCreation();
        }
        else if (source == questionUI.getBtnClear()) {
            clearForm();
        }
    }

    private void handleImageUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file hình ảnh");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg", "gif"));
        int returnValue = fileChooser.showOpenDialog(questionUI);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            questionUI.getTxtImagePath().setText(selectedImageFile.getAbsolutePath());
        }
    }
    
    private void handleAudioUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file âm thanh");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Audio files", "mp3", "wav", "ogg"));
        int returnValue = fileChooser.showOpenDialog(questionUI);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedAudioFile = fileChooser.getSelectedFile();
            questionUI.getTxtAudioPath().setText(selectedAudioFile.getAbsolutePath());
        }
    }
    
    private void saveQuestion() {
        // Validate inputs
        if (!validateInputs()) {
            return;
        }
        
        try {
            int success = 0;
            Questions question = new Questions(0, "", "", null, null, null);
            
            question.setContent(questionUI.getTxtAreaContent().getText().trim());
            String section = questionUI.getComboBoxSection().getSelectedItem().toString();
            question.setSection(section);
            if (selectedImageFile != null) {
                question.setImagePath(selectedImageFile.getAbsolutePath());
            }
            
            if (selectedAudioFile != null) {
                question.setAudioPath(selectedAudioFile.getAbsolutePath());
            }
            success += qDao.insert(question);
            
            int questionId = Utils.getNextQuestionId() - 1;
            ArrayList<Answers> answers = new ArrayList<>();
            answers.add(new Answers(0, questionId, questionUI.getTxtAnswerA().getText(), questionUI.getRadioAnswerA().isSelected()));
            answers.add(new Answers(0, questionId, questionUI.getTxtAnswerB().getText(), questionUI.getRadioAnswerB().isSelected()));
            answers.add(new Answers(0, questionId, questionUI.getTxtAnswerC().getText(), questionUI.getRadioAnswerC().isSelected()));
            answers.add(new Answers(0, questionId, questionUI.getTxtAnswerD().getText(), questionUI.getRadioAnswerD().isSelected()));
            question.setAnswers(answers);

            for(Answers answer : answers) {
                success += aDao.insert(answer);
            }


            if(success >= 2) {
                JOptionPane.showMessageDialog(questionUI, 
                    "Thêm câu hỏi thành công!", 
                    "Thông báo", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Clear form
                clearForm();
            } else {
                JOptionPane.showMessageDialog(questionUI, 
                    "Thêm câu hỏi thất bại!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
                    
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(questionUI, 
                "Lỗi: " + ex.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private boolean validateInputs() {
        // Check empty
        if (questionUI.getTxtAreaContent().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(questionUI, 
                "Vui lòng nhập nội dung câu hỏi!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check selected
        if (questionUI.getComboBoxSection().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(questionUI, 
                "Vui lòng chọn phần (section) cho câu hỏi!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (questionUI.getTxtAnswerA().getText().trim().isEmpty() ||
            questionUI.getTxtAnswerB().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(questionUI, 
                "Vui lòng nhập ít nhất hai đáp án (A và B)!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        // Check selected
        if (!questionUI.getRadioAnswerA().isSelected() && 
            !questionUI.getRadioAnswerB().isSelected() &&
            !questionUI.getRadioAnswerC().isSelected() && 
            !questionUI.getRadioAnswerD().isSelected()) {
            JOptionPane.showMessageDialog(questionUI, 
                "Vui lòng chọn đáp án đúng!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void clearForm() {
        questionUI.getTxtAreaContent().setText("");
        questionUI.getComboBoxSection().setSelectedIndex(0);
        questionUI.getTxtImagePath().setText("");
        questionUI.getTxtAudioPath().setText("");
        questionUI.getTxtAnswerA().setText("");
        questionUI.getTxtAnswerB().setText("");
        questionUI.getTxtAnswerC().setText("");
        questionUI.getTxtAnswerD().setText("");
        
        questionUI.getRadioAnswerA().setSelected(false);
        questionUI.getRadioAnswerB().setSelected(false);
        questionUI.getRadioAnswerC().setSelected(false);
        questionUI.getRadioAnswerD().setSelected(false);
        
        selectedImageFile = null;
        selectedAudioFile = null;
    }
    
    private void cancelQuestionCreation() {
        int response = JOptionPane.showConfirmDialog(questionUI, 
            "Bạn có chắc muốn hủy? Mọi thay đổi sẽ không được lưu.", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            questionUI.setVisible(false);
            UI mainUI = new UI();
            mainUI.setVisible(true);
        }
    }
}