package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import Models.*;

public class Exam_QuestionsDAO implements DAOInterface<Exam_Questions>{

	@Override
	public int insert(Exam_Questions t) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "INSERT INTO Exam_Questions(ExamID, QuestionID, questionOrder) VALUES (?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getExamId());
			ps.setInt(2, t.getQuestionId());
			ps.setInt(3, t.getQuestionOrder());
			ps.executeUpdate();
			SQLConnection.closeConnection(conn);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int update(Exam_Questions t) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "UPDATE Exam_Questions SET ExamID = ?, QuestionID = ?, questionOrder = ? WHERE ExamID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getExamId());
			ps.setInt(2, t.getQuestionId());
			ps.setInt(3, t.getQuestionOrder());
			ps.setInt(4, t.getExamId());
			ps.executeUpdate();
			SQLConnection.closeConnection(conn);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public int delete(Exam_Questions t) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "DELETE FROM Exam_Questions WHERE ExamID = ? AND QuestionID = ?";
			PreparedStatement ps = SQLConnection.getConnection().prepareStatement(sql);
			ps.setInt(1, t.getExamId());
			ps.setInt(2, t.getQuestionId());
			ps.executeUpdate();
			SQLConnection.closeConnection(conn);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public ArrayList<Exam_Questions> selectAll() {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT * FROM Exam_Questions";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList<Exam_Questions> list = new ArrayList<Exam_Questions>();
			while (rs.next()) {
				int examId = rs.getInt("ExamID");
				int questionId = rs.getInt("QuestionID");
				int questionOrder = rs.getInt("questionOrder");
				Exam_Questions examQuestion = new Exam_Questions(examId, questionId, questionOrder);
				list.add(examQuestion);
			}
			SQLConnection.closeConnection(conn);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Exam_Questions selectById(Exam_Questions t) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT * FROM Exam_Questions WHERE Exam = ? && QuestionID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getExamId());
			ps.setInt(2, t.getQuestionId());
			
			ps.executeUpdate();
			SQLConnection.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Exam_Questions> selectByCondition(String condition) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT * FROM Exam_Questions WHERE " + condition;
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList<Exam_Questions> list = new ArrayList<Exam_Questions>();
			while (rs.next()) {
				int examId = rs.getInt("ExamID");
				int questionId = rs.getInt("QuestionID");
				int questionOrder = rs.getInt("questionOrder");
				Exam_Questions examQuestion = new Exam_Questions(examId, questionId, questionOrder);
				list.add(examQuestion);
			}
			SQLConnection.closeConnection(conn);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
