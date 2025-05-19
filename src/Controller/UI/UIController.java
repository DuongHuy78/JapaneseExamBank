package Controller.UI;

import View.*;
import View.UI.UI;
import Models.*;
import Dao.*;
import Utils.Utils;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class UIController implements ActionListener {
    // View references
    private UI ui;

    private QuestionsDAO qDao = new QuestionsDAO();
    
    private File selectedImageFile;
    
    public UIController(UI ui) {
        this.ui = ui;
        
        // Register all action listeners
        registerActionListeners();
    }
    
    private void registerActionListeners() {
        // Register this controller as the action listener for all UI components
        ui.getBtnUploadImage().addActionListener(this);
        ui.getBtnExtractText().addActionListener(this);
        ui.getBtnGoToQuestionManagement().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        switch(command) {
            case "Tải Ảnh Lên":
                handleImageUpload();
                break;
                
            case "Trích Xuất Đề Bài (AI)":
                handleImageExtraction();
                break;
                
            case "Tạo Câu Hỏi":
            	createQuestion();
                break;
        }
    }
    
    private void handleImageUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn file ảnh đề thi");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "png", "jpeg", "gif"));
        int returnValue = fileChooser.showOpenDialog(ui);
        
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedImageFile = fileChooser.getSelectedFile();
            ui.getTxtImagePath().setText(selectedImageFile.getAbsolutePath());
            
            try {
                ImageIcon originalIcon = new ImageIcon(selectedImageFile.getAbsolutePath());
                int originalWidth = originalIcon.getIconWidth();
                int originalHeight = originalIcon.getIconHeight();
                
                // Lấy kích thước của label
                int labelWidth = ui.getLblImagePreview().getWidth() > 0 ? ui.getLblImagePreview().getWidth() - 10 : 280;
                int labelHeight = ui.getLblImagePreview().getHeight() > 0 ? ui.getLblImagePreview().getHeight() - 10 : 180;
                
                // Tính toán tỷ lệ khung hình để giữ nguyên tỷ lệ
                double widthRatio = (double) labelWidth / originalWidth;
                double heightRatio = (double) labelHeight / originalHeight;
                double ratio = Math.min(widthRatio, heightRatio);
                
                // Tính toán kích thước mới giữ nguyên tỷ lệ
                int newWidth = (int) (originalWidth * ratio);
                int newHeight = (int) (originalHeight * ratio);
                
                // Thay đổi kích thước ảnh
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                    newWidth, newHeight, Image.SCALE_SMOOTH);
                
                ui.getLblImagePreview().setIcon(new ImageIcon(scaledImage));
                ui.getLblImagePreview().setText(null);
                ui.getBtnExtractText().setEnabled(true);
            } catch (Exception ex) {
                ui.getLblImagePreview().setIcon(null);
                ui.getLblImagePreview().setText("Lỗi hiển thị ảnh");
                JOptionPane.showMessageDialog(ui, "Không thể hiển thị ảnh: " + ex.getMessage(), 
                    "Lỗi Ảnh", JOptionPane.ERROR_MESSAGE);
                ui.getBtnExtractText().setEnabled(false);
            }
        }
    }
    
    private void handleImageExtraction() {
        if (selectedImageFile != null) {
            String imagePath = selectedImageFile.getAbsolutePath();
            String extractedText = Utils.scanImage(imagePath);
            String text = Utils.extractTextFromResponse(extractedText);
            
            ui.getTextAreaExtractedContent().setFont(new Font("MS Gothic", Font.PLAIN, 14));
            ui.getTextAreaExtractedContent().setText(text);
            JOptionPane.showMessageDialog(ui, "Đã trích xuất nội dung. Vui lòng kiểm tra và chỉnh sửa.", 
                "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(ui, "Vui lòng chọn một file ảnh trước.", 
                "Chưa Chọn Ảnh", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void createQuestion() {
        System.out.println("Creating question...");
        ArrayList<Questions> questions;
    	String text = ui.getTextAreaExtractedContent().getText();
        System.out.println(text);
        questions = Utils.getQuestionsFromJson(text);
        if (questions != null && !questions.isEmpty()) {
            for (Questions question : questions) {
            	System.out.println(question.getContent());
                qDao.insert(question);
            }
            JOptionPane.showMessageDialog(ui, "Câu hỏi đã được tạo thành công!", 
                "Thông Báo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(ui, "Không thể tạo câu hỏi từ nội dung đã trích xuất.", 
                "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}