package Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import Models.*;

public class ExamsDAO implements DAOInterface<Exams>{

	@Override
	public int insert(Exams t) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "INSERT INTO Exams(Title, Level) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, t.getTitle());
			ps.setString(2, t.getLevel());
			ps.executeUpdate();
			SQLConnection.closeConnection(conn);
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	public int update(Exams t) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "UPDATE Exams SET Title = ?, Level = ? WHERE ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, t.getTitle());
			ps.setString(2, t.getLevel());
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
	public int delete(Exams t) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "DELETE FROM Exams WHERE ID = ?";
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
	public ArrayList<Exams> selectAll() {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT * FROM Exams";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList<Exams> examsList = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String title = rs.getString("Title");
				String level = rs.getString("Level");
				Exams exam = new Exams(id, title, level);
				examsList.add(exam);
			}
			SQLConnection.closeConnection(conn);
			return examsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Exams selectById(Exams t) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT * FROM Exams WHERE ID = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, t.getId());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				int id = rs.getInt("ID");
				String title = rs.getString("Title");
				String level = rs.getString("Level");
				Exams exam = new Exams(id, title, level);
				SQLConnection.closeConnection(conn);
				return exam;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public ArrayList<Exams> selectByCondition(String condition) {
		try {
			Connection conn = SQLConnection.getConnection();
			String sql = "SELECT * FROM Exams WHERE " + condition;
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList<Exams> examsList = new ArrayList<>();
			while (rs.next()) {
				int id = rs.getInt("ID");
				String title = rs.getString("Title");
				String level = rs.getString("Level");
				Exams exam = new Exams(id, title, level);
				examsList.add(exam);
			}
			SQLConnection.closeConnection(conn);
			return examsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
