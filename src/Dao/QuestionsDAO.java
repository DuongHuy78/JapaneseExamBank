package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Models.*;

public class QuestionsDAO implements DAOInterface<Questions>{
	
	
	@Override
	public int insert(Questions t) {
        System.out.println("Insert question: " + t.getContent());
        int generatedId = 0;
        try (Connection conn = SQLConnection.getConnection()) {
            String sql = "INSERT INTO Questions(Content, Section, AudioPath, ImagePath) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, t.getContent());
            ps.setString(2, t.getSection());
            ps.setString(3, t.getAudioPath());
            ps.setString(4, t.getImagePath());
            System.out.println("Executing: " + sql);
            
            ps.executeUpdate();
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
            
            if (t.getAnswers() != null && !t.getAnswers().isEmpty()) {
                AnswersDAO answersDAO = new AnswersDAO();
                for (Answers ans : t.getAnswers()) {
                    ans.setQuestionId(generatedId);
                    answersDAO.insert(ans);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return generatedId;
    }

	@Override
	public int update(Questions t) {
		try {
            Connection conn = SQLConnection.getConnection();
            
            // Update question details
            String sql = "UPDATE Questions SET Content = ?, Section = ?, AudioPath = ?, ImagePath = ? WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, t.getContent());
            ps.setString(2, t.getSection());
            ps.setString(3, t.getAudioPath());
            ps.setString(4, t.getImagePath());
            ps.setInt(5, t.getId());
            
            int result = ps.executeUpdate();
            
            if (t.getAnswers() != null && !t.getAnswers().isEmpty()) {
                AnswersDAO answersDAO = new AnswersDAO();
                for (Answers ans : t.getAnswers()) {
                    ans.setQuestionId(t.getId());
                    answersDAO.insert(ans);
                }
            }
            
            SQLConnection.closeConnection(conn);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
	}

	@Override
	public int delete(Questions t) {
		        try {
            Connection conn = SQLConnection.getConnection();
            
            // Delete question (related answers will be deleted by CASCADE constraint)
            String sql = "DELETE FROM Questions WHERE ID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, t.getId());
            
            int result = ps.executeUpdate();
            SQLConnection.closeConnection(conn);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
		return 0;
	}

	@Override
	public ArrayList<Questions> selectAll() {
		        ArrayList<Questions> questions = new ArrayList<>();
        
        try {
            Connection conn = SQLConnection.getConnection();
            String sql = "SELECT * FROM Questions";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("ID");
                String content = rs.getString("Content");
                String section = rs.getString("Section");
                String audioPath = rs.getString("AudioPath");
                String imagePath = rs.getString("ImagePath");
                
                AnswersDAO answersDAO = new AnswersDAO();
                
                String condition = "QuestionID = " + id;
                List<Answers> answers = (List<Answers>) answersDAO.selectByCondition(condition);
				Questions question = new Questions(id, content, section, audioPath, imagePath, answers);
                questions.add(question);
            }
            
            SQLConnection.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return questions;
	}

	@Override
	public Questions selectById(Questions t) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT * FROM Questions WHERE ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getId());
			ResultSet rs = ps.executeQuery();
            AnswersDAO tempDao = new AnswersDAO();
			if(rs.next()) {
				int id = rs.getInt("ID");
				String content = rs.getString("Content");
				String section = rs.getString("Section");
				String audioPath = rs.getString("AudioPath");
				String imagePath = rs.getString("ImagePath");
                List<Answers> answers = tempDao.selectByCondition("QuestionID = " + id);                
				Questions question = new Questions(id, content, section, audioPath, imagePath, answers);
				
				SQLConnection.closeConnection(conn);
				return question;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Questions> selectByCondition(String condition) {
		 ArrayList<Questions> questions = new ArrayList<>();
        
        try {
            Connection conn = SQLConnection.getConnection();
            String sql = "SELECT * FROM Questions WHERE " + condition;
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            AnswersDAO tempDao = new AnswersDAO();
            while (rs.next()) {
                int id = rs.getInt("ID");
                String content = rs.getString("Content");
                String section = rs.getString("Section");
                String audioPath = rs.getString("AudioPath");
                String imagePath = rs.getString("ImagePath");
                
                List<Answers> answers = tempDao.selectByCondition("QuestionID = " + id);  
				Questions question = new Questions(id, content, section, audioPath, imagePath, answers);
                questions.add(question);
            }
            
            SQLConnection.closeConnection(conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return questions;
	}
	
	public ArrayList<Questions> selectBySection(String section) {
        return selectByCondition("Section = '" + section + "'");
    }
}
