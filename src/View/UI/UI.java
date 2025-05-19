package View.UI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Controller.UI.*;
import View.Exam.*;
import View.Question.*;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;

public class UI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblImagePreview;
    private JTextArea textAreaExtractedContent;
    private JTextField txtImagePath;
    private JButton btnUploadImage;
    private JButton btnExtractText;
    private JButton btnGoToQuestionManagement;
    
    public static void run() {
    	 try {
             UI frame = new UI();
             frame.setVisible(true);
         } catch (Exception e) {
             e.printStackTrace();
             //D:\Pictures\Screenshots\Screenshot 2025-05-15 214726.png
         }
    }

    public UI() {
        setTitle("Trình Trích Xuất Đề Thi AI");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(new BorderLayout(10, 10));
		setContentPane(contentPane);

		// --- Thêm MenuBar ---
		UI.createMenuBar(this);
		
		JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        
        JPanel fileSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtImagePath = new JTextField(30);
        txtImagePath.setEditable(false);
        txtImagePath.setFont(new Font("Arial", Font.PLAIN, 14));
        btnUploadImage = new JButton("Tải Ảnh Lên");
        btnUploadImage.setFont(new Font("Arial", Font.BOLD, 14));
        fileSelectionPanel.add(new JLabel("File ảnh:"));
        fileSelectionPanel.add(txtImagePath);
        fileSelectionPanel.add(btnUploadImage);
        
        topPanel.add(fileSelectionPanel, BorderLayout.NORTH);

        lblImagePreview = new JLabel("Chưa có ảnh nào được chọn", JLabel.CENTER);
        lblImagePreview.setPreferredSize(new Dimension(300, 200));
        lblImagePreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblImagePreview.setFont(new Font("Arial", Font.ITALIC, 14));
        JScrollPane imageScrollPane = new JScrollPane(lblImagePreview);
        topPanel.add(imageScrollPane, BorderLayout.CENTER);

        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 5));
        JLabel lblExtractedTextTitle = new JLabel("Nội dung đề bài trích xuất (có thể chỉnh sửa):");
        lblExtractedTextTitle.setFont(new Font("Arial", Font.BOLD, 16));
        centerPanel.add(lblExtractedTextTitle, BorderLayout.NORTH);

        textAreaExtractedContent = new JTextArea();
        textAreaExtractedContent.setFont(new Font("Arial", Font.PLAIN, 14));
        textAreaExtractedContent.setLineWrap(true);
        textAreaExtractedContent.setWrapStyleWord(true);
        JScrollPane scrollPaneText = new JScrollPane(textAreaExtractedContent);
        centerPanel.add(scrollPaneText, BorderLayout.CENTER);

        contentPane.add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnExtractText = new JButton("Trích Xuất Đề Bài (AI)");
        btnExtractText.setFont(new Font("Arial", Font.BOLD, 14));
        btnExtractText.setEnabled(false);

        btnGoToQuestionManagement = new JButton("Tạo Câu Hỏi");
        btnGoToQuestionManagement.setFont(new Font("Arial", Font.BOLD, 14));

        bottomPanel.add(btnExtractText);
        bottomPanel.add(btnGoToQuestionManagement);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        new UIController(this);
    }
    
    public static void createMenuBar(JFrame current) {
    	JMenuBar menuBar = new JMenuBar();

		JMenu ScanImageMenu = new JMenu("ScanImageMenu");
		JMenu fileMenu = new JMenu("Question");
		JMenu examMenu = new JMenu("Exam");
		JMenu helpMenu = new JMenu("Help");

		//ScanImageMenu
		JMenuItem scanImageMenuItem = new JMenuItem("Scan Image");
		scanImageMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UI tempUI = new UI();
				tempUI.setVisible(true);
				current.setEnabled(false);
				current.setVisible(false);
			}
		});
		ScanImageMenu.add(scanImageMenuItem);
		
		//questionMenu
		JMenuItem exitItem = new JMenuItem("Create question");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				QuestionUI questionEditor = new QuestionUI();
                questionEditor.setVisible(true);
                current.setEnabled(false);
                current.setVisible(false);
			}
		});
		fileMenu.add(exitItem);

        JMenuItem showQuestion = new JMenuItem("Show Question");
        showQuestion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ShowQuestionUI showQuestion = new ShowQuestionUI();
                showQuestion.setVisible(true);
                current.setEnabled(false);
                current.setVisible(false);
            }
        });
        fileMenu.add(showQuestion);
		
		//examMenu
		JMenuItem createExam = new JMenuItem("Create Exam");
		createExam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ExamUI EUI = new ExamUI();
				EUI.setVisible(true);
				current.setEnabled(false);
				current.setVisible(false);
			}
		});
		examMenu.add(createExam);

        JMenuItem showExam = new JMenuItem("Show Exam");
        showExam.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Show the exam UI
                ShowExamUI showExam = new ShowExamUI();
                showExam.setVisible(true);
                current.setEnabled(false);
                current.setVisible(false);
            }
        });
        examMenu.add(showExam);
		
		//helpMenu
		JMenuItem aboutItem = new JMenuItem("About");
		helpMenu.add(aboutItem);

		menuBar.add(ScanImageMenu);
		menuBar.add(fileMenu);
		menuBar.add(examMenu);
		menuBar.add(helpMenu);
		current.setJMenuBar(menuBar);
    }

    public JButton getBtnUploadImage() {
        return btnUploadImage;
    }
    
    public JButton getBtnExtractText() {
        return btnExtractText;
    }
    
    public JButton getBtnGoToQuestionManagement() {
        return btnGoToQuestionManagement;
    }
    
    public JTextField getTxtImagePath() {
        return txtImagePath;
    }
    
    public JLabel getLblImagePreview() {
        return lblImagePreview;
    }
    
    public JTextArea getTextAreaExtractedContent() {
        return textAreaExtractedContent;
    }

    public JButton lblExtractedTextTitle() {
        return btnExtractText;
    }
}