package Controller.Exam;

import Models.Exams;
import Models.Questions;
import Models.Exam_Questions;
import Utils.Utils;
import View.Exam.ExamUI;
import Dao.*;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class ExamController extends Application implements ActionListener {

    private ExamUI examUI;
    private ExamsDAO examsDAO = new ExamsDAO();
    private QuestionsDAO questionsDAO = new QuestionsDAO();
    private Exam_QuestionsDAO examQuestionsDAO = new Exam_QuestionsDAO();
    private File selectedAudioFile = null;
    private Exams currentExam = null;

    public ExamController(ExamUI examUi) {
        this.examUI = examUi;
        registerActionListeners();
    }

    private void registerActionListeners() {
        examUI.getBtnRandomExam().addActionListener(this);
        examUI.getBtnSaveExam().addActionListener(this);
        examUI.getBtnExportPDF().addActionListener(this);
        examUI.getBtnBrowseAudio().addActionListener(this);
        examUI.getBtnPlayAudio().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == examUI.getBtnRandomExam()) {
            randomizeExam();
        } 
        else if (source == examUI.getBtnSaveExam()) {
            saveExam();
        } 
        else if (source == examUI.getBtnExportPDF()) {
            exportToPDF();
        }
        else if (source == examUI.getBtnBrowseAudio()) {
            browseAudioFile();
        }
        else if (source == examUI.getBtnPlayAudio()) {
            playAudioFile();
        }
    }
    
    private void randomizeExam() {
        if (!validateExamInputs()) {
            return;
        }
        
        String title = examUI.getTxtExamTitle().getText().trim();
        String level = examUI.getComboBoxLevel().getSelectedItem().toString();
        int numGrammar = (Integer) examUI.getSpinnerGrammar().getValue();
        int numVocab = (Integer) examUI.getSpinnerVocab().getValue();
        int numReading = (Integer) examUI.getSpinnerReading().getValue();
        int numListening = (Integer) examUI.getSpinnerListening().getValue();
        
        currentExam = Utils.getRandomExams(title, level, numGrammar, numVocab, numReading, numListening);
        
        if (currentExam != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Đề thi: ").append(currentExam.getTitle()).append("\n");
            sb.append("Cấp độ: ").append(currentExam.getLevel()).append("\n\n");
            
            // Display question list
            sb.append("Danh sách câu hỏi trong đề thi:\n");
            if (currentExam.getExamQuestions() != null) {
                for (Exam_Questions eq : currentExam.getExamQuestions()) {
                    sb.append("Câu ").append(eq.getQuestionOrder())
                      .append(": ID câu hỏi: ").append(eq.getQuestionId()).append("\n");
                    Questions currQuestions;
                    currQuestions = questionsDAO.selectById(new Questions(eq.getQuestionId(), "", "", "", ""));
                    if(currQuestions.getSection() == "Listening") {
                        sb.append("File audio path: ").append(currQuestions.getAudioPath()).append("\n");
                    }
                }
            }
            
            examUI.getExamTextArea().setText(sb.toString());
            
            JOptionPane.showMessageDialog(examUI,
                "Đã tạo đề thi thành công!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(examUI,
                "Không thể tạo đề thi. Vui lòng kiểm tra lại thông tin.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private boolean validateExamInputs() {
        if (examUI.getTxtExamTitle().getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(examUI,
                "Vui lòng nhập tiêu đề đề thi!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if (examUI.getComboBoxLevel().getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(examUI,
                "Vui lòng chọn cấp độ cho đề thi!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        int total = (Integer)examUI.getSpinnerGrammar().getValue() +
                    (Integer)examUI.getSpinnerVocab().getValue() +
                    (Integer)examUI.getSpinnerReading().getValue() +
                    (Integer)examUI.getSpinnerListening().getValue();
                    
        if (total <= 0) {
            JOptionPane.showMessageDialog(examUI,
                "Đề thi phải có ít nhất một câu hỏi!",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void saveExam() {
        if (currentExam == null) {
            JOptionPane.showMessageDialog(examUI,
                "Vui lòng tạo hoặc chọn một đề thi trước khi lưu!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        examsDAO.insert(currentExam);
        int examId = Utils.getNextExamId() - 1;
        ArrayList<Exam_Questions> examQuestions = (ArrayList<Exam_Questions>) currentExam.getExamQuestions();
        for (Exam_Questions eq : examQuestions) {
            eq.setExamId(examId);
            examQuestionsDAO.insert(eq);
        }
        JOptionPane.showMessageDialog(examUI, 
            "Đề thi đã được lưu thành công!", 
            "Chỉnh sửa", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void exportToPDF() {
        if (currentExam == null) {
            JOptionPane.showMessageDialog(examUI,
                "Vui lòng tạo hoặc chọn một đề thi trước khi xuất PDF!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        try {
            Utils.exportToPDF(currentExam);
            JOptionPane.showMessageDialog(examUI,
                "Đã xuất đề thi ra file PDF thành công!",
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(examUI,
                "Lỗi khi xuất file PDF: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
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
        
        int result = fileChooser.showOpenDialog(examUI);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedAudioFile = fileChooser.getSelectedFile();
            examUI.getAudioSearchField().setText(selectedAudioFile.getAbsolutePath());
            examUI.getBtnPlayAudio().setEnabled(true);
        }
    }
    
    private void playAudioFile() {
        if (selectedAudioFile == null || !selectedAudioFile.exists()) {
            JOptionPane.showMessageDialog(examUI,
                "Không tìm thấy file audio đã chọn.",
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
        // Khởi tạo JavaFX toolkit nếu chưa có
        Platform.startup(() -> {
            String uriString = selectedAudioFile.toURI().toString();
            Media media = new Media(uriString);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        });
    } catch (IllegalStateException ise) {
        // Nếu đã khởi tạo rồi thì chạy luôn
        Platform.runLater(() -> {
            String uriString = selectedAudioFile.toURI().toString();
            Media media = new Media(uriString);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(examUI,
                "Có lỗi khi phát file audio: " + ex.getMessage(),
                "Lỗi", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) {
        
        String uriString = selectedAudioFile.toURI().toString();
        Media media = new Media(uriString);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
    }
}