package Controller.Exam;

import Dao.ExamsDAO;
import Dao.QuestionsDAO;
import Dao.Exam_QuestionsDAO;
import Models.Exams;
import Models.Questions;
import Models.Exam_Questions;
import Models.Answers;
import View.Exam.DetailsExamUI;
import View.Exam.ShowExamUI;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class DetailExamController implements ActionListener {

    private DetailsExamUI detailsExamUI;
    private ExamsDAO examsDAO = new ExamsDAO();
    private QuestionsDAO questionsDAO = new QuestionsDAO();
    private Exam_QuestionsDAO examQuestionsDAO = new Exam_QuestionsDAO();
    private File selectedAudioFile = null;
    private Exams currentExam = null;
    private ArrayList<Exam_Questions> examQuestions = null;
    private MediaPlayer mediaPlayer;

    public DetailExamController(DetailsExamUI detailsExamUI) {
        this.detailsExamUI = detailsExamUI;
        registerActionListeners();
        loadExamDetails();
    }

    private void registerActionListeners() {
        detailsExamUI.getBtnSearch().addActionListener(this);
        detailsExamUI.getBtnBack().addActionListener(this);
        detailsExamUI.getBtnBrowseAudio().addActionListener(this);
        detailsExamUI.getBtnPlayAudio().addActionListener(this);
        detailsExamUI.getBtnStopAudio().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == detailsExamUI.getBtnSearch()) {
            searchQuestions();
        } 
        else if (source == detailsExamUI.getBtnBack()) {
            goBack();
        }
        else if (source == detailsExamUI.getBtnBrowseAudio()) {
            browseAudioFile();
        }
        else if (source == detailsExamUI.getBtnPlayAudio()) {
            playAudioFile();
        }
        else if (source == detailsExamUI.getBtnStopAudio()) {
        	stopAudioFile();
        }
    }
    
    private void loadExamDetails() {
        int examId = detailsExamUI.getExamId();
        currentExam = examsDAO.selectById(new Exams(examId, "", ""));
        
        if (currentExam != null) {
            // Load exam questions
            examQuestions = examQuestionsDAO.selectByCondition("ExamID = " + examId);
            currentExam.setExamQuestions(examQuestions);
            
            // Display exam details
            StringBuilder sb = new StringBuilder();
            sb.append("ĐỀ THI: ").append(currentExam.getTitle()).append("\n");
            sb.append("Cấp độ: ").append(currentExam.getLevel()).append("\n");
            sb.append("Tổng số câu hỏi: ").append(examQuestions != null ? examQuestions.size() : 0).append("\n\n");
            
            // Count different question types
            int grammarCount = 0, vocabCount = 0, readingCount = 0, listeningCount = 0;
            
            if (examQuestions != null) {
                for (Exam_Questions eq : examQuestions) {
                    Questions q = questionsDAO.selectById(new Questions(eq.getQuestionId(), "", "", "", ""));
                    if (q != null) {
                        String section = q.getSection();
                        if ("Grammar".equals(section)) grammarCount++;
                        else if ("Vocabulary".equals(section)) vocabCount++;
                        else if ("Reading".equals(section)) readingCount++;
                        else if ("Listening".equals(section)) listeningCount++;
                    }
                }
            }
            
            sb.append("Ngữ pháp: ").append(grammarCount).append(" câu\n");
            sb.append("Từ vựng: ").append(vocabCount).append(" câu\n");
            sb.append("Đọc hiểu: ").append(readingCount).append(" câu\n");
            sb.append("Nghe hiểu: ").append(listeningCount).append(" câu\n");
            
            detailsExamUI.getExamContentArea().setText(sb.toString());
            
            // Display all questions initially
            displayAllQuestions();
        } else {
            JOptionPane.showMessageDialog(detailsExamUI,
                "Không thể tải thông tin đề thi.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            goBack();
        }
    }
    
    private void displayAllQuestions() {
        if (examQuestions == null || examQuestions.isEmpty()) {
            detailsExamUI.getQuestionDetailsArea().setText("Đề thi này không có câu hỏi nào.");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        
        for (Exam_Questions eq : examQuestions) {
            Questions question = questionsDAO.selectById(new Questions(eq.getQuestionId(), "", "", "", ""));
            if (question != null) {
                displayQuestionDetails(question, eq.getQuestionOrder(), sb);
                sb.append("\n----------------------------------------------------\n\n");
            }
        }
        
        detailsExamUI.getQuestionDetailsArea().setText(sb.toString());
        detailsExamUI.getQuestionDetailsArea().setCaretPosition(0); // Scroll to top
    }
    
    private void displayQuestionDetails(Questions question, int order, StringBuilder sb) {
        sb.append("Câu ").append(order).append(": ");
        sb.append(question.getContent()).append("\n");
        sb.append("Phần: ").append(question.getSection()).append("\n");
        
        // Display audio path for listening questions
        if ("Listening".equals(question.getSection()) && question.getAudioPath() != null && !question.getAudioPath().isEmpty()) {
            sb.append("File audio: ").append(question.getAudioPath()).append("\n");
        }
        
        // Display image path if available
        if (question.getImagePath() != null && !question.getImagePath().isEmpty()) {
            sb.append("File hình ảnh: ").append(question.getImagePath()).append("\n");
        }
        
        // Display answers
        sb.append("\nCác đáp án:\n");
        List<Answers> answers = question.getAnswers();
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
    }
    
    private void searchQuestions() {
        String keyword = detailsExamUI.getSearchField().getText().trim();
        String section = detailsExamUI.getComboBoxSection().getSelectedItem().toString();
        
        if (keyword.isEmpty() && "Tất cả".equals(section)) {
            displayAllQuestions();
            return;
        }
        
        if (examQuestions == null || examQuestions.isEmpty()) {
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        
        for (Exam_Questions eq : examQuestions) {
            Questions question = questionsDAO.selectById(new Questions(eq.getQuestionId(), "", "", "", ""));
            if (question != null) {
                boolean matchesKeyword = keyword.isEmpty() || 
                                        question.getContent().toLowerCase().contains(keyword.toLowerCase());
                boolean matchesSection = "Tất cả".equals(section) || 
                                        question.getSection().equals(section);
                
                if (matchesKeyword && matchesSection) {
                    displayQuestionDetails(question, eq.getQuestionOrder(), sb);
                    sb.append("\n----------------------------------------------------\n\n");
                    found = true;
                }
            }
        }
        
        if (found) {
            detailsExamUI.getQuestionDetailsArea().setText(sb.toString());
            detailsExamUI.getQuestionDetailsArea().setCaretPosition(0); // Scroll to top
        } else {
            detailsExamUI.getQuestionDetailsArea().setText("Không tìm thấy câu hỏi nào phù hợp với tiêu chí tìm kiếm.");
        }
    }
    
    private void browseAudioFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file audio");
        
        // Add common audio file filter
        FileNameExtensionFilter audioFilter = new FileNameExtensionFilter(
            "File audio (*.mp3, *.wav, *.ogg)", "mp3", "wav", "ogg");
        fileChooser.addChoosableFileFilter(audioFilter);
        fileChooser.setFileFilter(audioFilter);
        
        int result = fileChooser.showOpenDialog(detailsExamUI);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedAudioFile = fileChooser.getSelectedFile();
            detailsExamUI.getAudioSearchField().setText(selectedAudioFile.getAbsolutePath());
            detailsExamUI.getBtnPlayAudio().setEnabled(true);
        }
    }
    
    private void playAudioFile() {
        if (selectedAudioFile == null || !selectedAudioFile.exists()) {
            JOptionPane.showMessageDialog(detailsExamUI,
                "Không tìm thấy file audio đã chọn.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        final String uriString = selectedAudioFile.toURI().toString();
        try {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            
            Platform.startup(() -> {
                Media media = new Media(uriString);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            });
            detailsExamUI.getBtnStopAudio().setEnabled(true);
        } catch (IllegalStateException ise) {
            // Nếu đã khởi tạo rồi thì chạy luôn
            Platform.runLater(() -> {
                Media media = new Media(uriString);
                mediaPlayer = new MediaPlayer(media);
                mediaPlayer.play();
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(detailsExamUI,
                "Có lỗi khi phát file audio: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void stopAudioFile() {
        if (mediaPlayer == null) {
            return;
        }
        
        try {
            Platform.runLater(() -> {
                mediaPlayer.stop();
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(detailsExamUI,
                "Có lỗi khi dừng file audio: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    
    private void goBack() {
        ShowExamUI showExamUI = new ShowExamUI();
        showExamUI.setVisible(true);
        detailsExamUI.dispose();
    }
}