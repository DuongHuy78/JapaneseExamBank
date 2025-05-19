package Models;

import java.util.List;

public class Questions {
    private int id;
    private String content;
    private String section;
    private String audioPath;
    private String imagePath;
    private List<Answers> answers;
    
    public Questions(int id, String content, String section, String audioPath, String imagePath, List<Answers> answers) {
        super();
        this.id = id;
        this.content = content;
        this.section = section;
        this.audioPath = audioPath;
        this.imagePath = imagePath;
        this.answers = answers;
    }
    
    // Constructor without answers for initial creation
    public Questions(int id, String content, String section, String audioPath, String imagePath) {
        this(id, content, section, audioPath, imagePath, null);
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSection() {
        return section;
    }
    
    public void setSection(String section) {
        this.section = section;
    }
    
    public String getAudioPath() {
        return audioPath;
    }
    
    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }
    
    public String getImagePath() {
        return imagePath;
    }
    
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public List<Answers> getAnswers() {
        return answers;
    }
    
    public void setAnswers(List<Answers> answers) {
        this.answers = answers;
    }
}