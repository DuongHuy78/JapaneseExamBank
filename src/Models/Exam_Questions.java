package Models;

public class Exam_Questions {
    private int examId;
    private int questionId;
    private int questionOrder;
    
    public Exam_Questions(int examId, int questionId, int questionOrder) {
        super();
        this.examId = examId;
        this.questionId = questionId;
        this.questionOrder = questionOrder;
    }
    
    public int getExamId() {
        return examId;
    }
    
    public void setExamId(int examId) {
        this.examId = examId;
    }
    
    public int getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    public int getQuestionOrder() {
        return questionOrder;
    }
    
    public void setQuestionOrder(int questionOrder) {
        this.questionOrder = questionOrder;
    }
}