package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class AnswersDAO implements DAOInterface<Models.Answers>{

	@Override
	public int insert(Models.Answers t) {
		try {
			Connection conn = SQLConnection.getConnection();
	        String sql = "INSERT INTO Answers(QuestionID, Content, IsCorrect) VALUES (?, ?, ?)";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, t.getQuestionId());
	        ps.setString(2, t.getContent());
	        ps.setBoolean(3, t.isCorrect());
			ps.executeUpdate();
			SQLConnection.closeConnection(conn);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		return 0;
	}

	@Override
	public int update(Models.Answers t) {
		try {
			Connection conn = SQLConnection.getConnection();
	        String sql = "UPDATE Answers SET Content = ?, IsCorrect = ? WHERE ID = ?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, t.getContent());
	        ps.setBoolean(2, t.isCorrect());
	        ps.setInt(3, t.getId());

			ps.executeUpdate();
			SQLConnection.closeConnection(conn);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int delete(Models.Answers t) {
		try {
			Connection conn = SQLConnection.getConnection();
	        String sql = "DELETE FROM Answers WHERE ID = ?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, t.getId());
			ps.executeUpdate();
			SQLConnection.closeConnection(conn);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public ArrayList<Models.Answers> selectAll() {
		try {
			Connection conn = SQLConnection.getConnection();
	        String sql = "SELECT * FROM Answers";
	        PreparedStatement ps = conn.prepareStatement(sql);
			ArrayList<Models.Answers> list = new ArrayList<>();
			var rs = ps.executeQuery();
			while(rs.next()) {
				Models.Answers ans = new Models.Answers(rs.getInt("ID"), rs.getInt("QuestionID"), rs.getString("Content"), rs.getBoolean("IsCorrect"));
				list.add(ans);
			}
			SQLConnection.closeConnection(conn);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Models.Answers selectById(Models.Answers t) {
		try {
			Connection conn = SQLConnection.getConnection();
	        String sql = "SELECT * FROM Answers WHERE ID = ?";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1, t.getId());
			var rs = ps.executeQuery();
			if(rs.next()) {
				Models.Answers ans = new Models.Answers(rs.getInt("ID"), rs.getInt("QuestionID"), rs.getString("Content"), rs.getBoolean("IsCorrect"));
				SQLConnection.closeConnection(conn);
				return ans;
			}
			SQLConnection.closeConnection(conn);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Models.Answers> selectByCondition(String condition) {
		try {
			Connection conn = SQLConnection.getConnection();
	        String sql = "SELECT * FROM Answers WHERE " + condition;
	        PreparedStatement ps = conn.prepareStatement(sql);
			ArrayList<Models.Answers> list = new ArrayList<>();
			var rs = ps.executeQuery();
			while(rs.next()) {
				Models.Answers ans = new Models.Answers(rs.getInt("ID"), rs.getInt("QuestionID"), rs.getString("Content"), rs.getBoolean("IsCorrect"));
				list.add(ans);
			}
			SQLConnection.closeConnection(conn);
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
