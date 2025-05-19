package View.Question;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import Controller.Question.UpdateQuestionController;
import Models.Answers;
import Models.Questions;
import View.UI.UI;

import java.awt.*;
import java.util.List;

public class UpdateQuestionUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea txtAreaContent;
    private JComboBox<String> comboBoxSection;
    private JTextField txtAnswerA, txtAnswerB, txtAnswerC, txtAnswerD;
    private JRadioButton radioAnswerA, radioAnswerB, radioAnswerC, radioAnswerD;
    private ButtonGroup answerGroup;
    private JTextField txtImagePath, txtAudioPath;
    private JButton btnBrowseImage, btnBrowseAudio;
    private JButton btnSaveChanges, btnCancel;
    private int questionId;

    public UpdateQuestionUI(int questionId) {
        this.questionId = questionId;
        initializeUI();
        new UpdateQuestionController(this, this.questionId); // Pass ID to controller
    }

    private void initializeUI() {
        setTitle("Cập nhật Câu hỏi - ID: " + questionId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Dispose on close to not exit app
        setBounds(100, 100, 750, 700);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        contentPane.setBackground(new Color(0xf9f9f9));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 20));

        UI.createMenuBar(this); // If you want the menu bar here too

        JLabel lblTitle = new JLabel("Cập nhật Câu hỏi");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        lblTitle.setForeground(new Color(0x2c3e50));
        contentPane.add(lblTitle, BorderLayout.NORTH);

        JPanel formPanel = createFormPanel();
        contentPane.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnSaveChanges = new JButton("Lưu thay đổi");
        btnSaveChanges.setFont(new Font("Arial", Font.BOLD, 14));
        btnSaveChanges.setBackground(new Color(0x3498db));
        btnSaveChanges.setForeground(Color.WHITE);

        btnCancel = new JButton("Hủy bỏ");
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));
        btnCancel.setBackground(new Color(0xe74c3c));
        btnCancel.setForeground(Color.WHITE);

        buttonPanel.add(btnSaveChanges);
        buttonPanel.add(btnCancel);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0xbdc3c7)),
                new EmptyBorder(15, 15, 15, 15)
        ));

        // Content
        formPanel.add(createLabel("Nội dung câu hỏi:"));
        txtAreaContent = new JTextArea(5, 20);
        txtAreaContent.setLineWrap(true);
        txtAreaContent.setWrapStyleWord(true);
        JScrollPane scrollContent = new JScrollPane(txtAreaContent);
        formPanel.add(scrollContent);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Section
        formPanel.add(createLabel("Phần (Section):"));
        comboBoxSection = new JComboBox<>(new String[]{"Chọn phần...", "Grammar", "Vocabulary", "Reading", "Listening"});
        formPanel.add(comboBoxSection);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Answers
        formPanel.add(createLabel("Các đáp án và chọn đáp án đúng:"));
        answerGroup = new ButtonGroup();
        txtAnswerA = new JTextField(30);
        radioAnswerA = new JRadioButton("A");
        formPanel.add(createAnswerPanel(radioAnswerA, txtAnswerA));
        txtAnswerB = new JTextField(30);
        radioAnswerB = new JRadioButton("B");
        formPanel.add(createAnswerPanel(radioAnswerB, txtAnswerB));
        txtAnswerC = new JTextField(30);
        radioAnswerC = new JRadioButton("C");
        formPanel.add(createAnswerPanel(radioAnswerC, txtAnswerC));
        txtAnswerD = new JTextField(30);
        radioAnswerD = new JRadioButton("D");
        formPanel.add(createAnswerPanel(radioAnswerD, txtAnswerD));
        answerGroup.add(radioAnswerA);
        answerGroup.add(radioAnswerB);
        answerGroup.add(radioAnswerC);
        answerGroup.add(radioAnswerD);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Image Path
        formPanel.add(createLabel("Đường dẫn hình ảnh (nếu có):"));
        JPanel imagePanel = new JPanel(new BorderLayout(5,0));
        txtImagePath = new JTextField();
        btnBrowseImage = new JButton("Chọn ảnh");
        imagePanel.add(txtImagePath, BorderLayout.CENTER);
        imagePanel.add(btnBrowseImage, BorderLayout.EAST);
        formPanel.add(imagePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Audio Path
        formPanel.add(createLabel("Đường dẫn âm thanh (cho Listening):"));
         JPanel audioPanel = new JPanel(new BorderLayout(5,0));
        txtAudioPath = new JTextField();
        btnBrowseAudio = new JButton("Chọn audio");
        audioPanel.add(txtAudioPath, BorderLayout.CENTER);
        audioPanel.add(btnBrowseAudio, BorderLayout.EAST);
        formPanel.add(audioPanel);

        return formPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createAnswerPanel(JRadioButton radio, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(radio, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    // --- Getters for Controller ---
    public int getQuestionId() { return questionId; }
    public JTextArea getTxtAreaContent() { return txtAreaContent; }
    public JComboBox<String> getComboBoxSection() { return comboBoxSection; }
    public JTextField getTxtAnswerA() { return txtAnswerA; }
    public JTextField getTxtAnswerB() { return txtAnswerB; }
    public JTextField getTxtAnswerC() { return txtAnswerC; }
    public JTextField getTxtAnswerD() { return txtAnswerD; }
    public JRadioButton getRadioAnswerA() { return radioAnswerA; }
    public JRadioButton getRadioAnswerB() { return radioAnswerB; }
    public JRadioButton getRadioAnswerC() { return radioAnswerC; }
    public JRadioButton getRadioAnswerD() { return radioAnswerD; }
    public JTextField getTxtImagePath() { return txtImagePath; }
    public JTextField getTxtAudioPath() { return txtAudioPath; }
    public JButton getBtnBrowseImage() { return btnBrowseImage; }
    public JButton getBtnBrowseAudio() { return btnBrowseAudio; }
    public JButton getBtnSaveChanges() { return btnSaveChanges; }
    public JButton getBtnCancel() { return btnCancel; }

    // --- Setters for Controller to populate data ---
    public void setQuestionData(Questions question, List<Answers> answers) {
        txtAreaContent.setText(question.getContent());
        comboBoxSection.setSelectedItem(question.getSection());
        txtImagePath.setText(question.getImagePath());
        txtAudioPath.setText(question.getAudioPath());

        if (answers != null && answers.size() >= 4) {
            txtAnswerA.setText(answers.get(0).getContent());
            radioAnswerA.setSelected(answers.get(0).isCorrect());
            txtAnswerB.setText(answers.get(1).getContent());
            radioAnswerB.setSelected(answers.get(1).isCorrect());
            txtAnswerC.setText(answers.get(2).getContent());
            radioAnswerC.setSelected(answers.get(2).isCorrect());
            txtAnswerD.setText(answers.get(3).getContent());
            radioAnswerD.setSelected(answers.get(3).isCorrect());
        }
    }
}