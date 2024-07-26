package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Bug;
import model.Developer;
import utility.DBConnection;

public class BugDAO {

	//Add Bug
	public int addBug(Bug bug) throws SQLException {
		System.out.println("Add Bug!");
        String query = "INSERT INTO bugs (description, category_id, assigned_to, status, created_date) VALUES (?, ?, ?, ?, ?)";
        int result;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, bug.getDescription());
            statement.setInt(2, bug.getCategoryId());
            statement.setInt(3, bug.getAssignedTo());
            statement.setString(4, bug.getStatus());
            statement.setTimestamp(5, new Timestamp(bug.getCreatedDate().getTime()));
            result = statement.executeUpdate();
        }
        return result;
    }
	
	//Get All Bugs
	public ObservableList<Bug> getAllBugs() throws SQLException {
		
        ObservableList<Bug> bugs = FXCollections.observableArrayList();
        String query = "SELECT * FROM bugs";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Bug bug = new Bug();
                bug.setBugId(resultSet.getInt("bug_id"));
                bug.setDescription(resultSet.getString("description"));
                bug.setCategoryId(resultSet.getInt("category_id"));
                bug.setAssignedTo(resultSet.getInt("assigned_to"));
                bug.setStatus(resultSet.getString("status"));
                bug.setCreatedDate(resultSet.getTimestamp("created_date"));
                bug.setResolvedDate(resultSet.getTimestamp("resolved_date"));
                bugs.add(bug);
            }
        }
    	System.out.println(bugs);
        return bugs;
    }
	
	//Update Bug
	public int updateBug(int bugId, String description,int categoryId, int developerId, String status) throws SQLException {
        String query = "UPDATE bugs SET description = ?, category_id = ?, assigned_to = ?, status = ? WHERE bug_id = ?";
        int result;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, description);
            statement.setInt(2, categoryId);
            statement.setInt(3, developerId);
            statement.setString(4, status);
            statement.setInt(5, bugId);
            result = statement.executeUpdate();
        }
        System.out.println(result);
        return result;
    }
	
	//Delete Bug
	public int deleteBug(int bugId) throws SQLException {
		int result;
        String query = "DELETE FROM bugs WHERE bug_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bugId);
            result = statement.executeUpdate();
        }
        return result;
    }
	
	//Get all bugs assigned to a developer with a specific name
    public List<Bug> getBugsAssignedToDeveloper(String developerName) throws SQLException {
        List<Bug> bugs = new ArrayList<>();
        String query = "SELECT b.* FROM bugs b " +
                       "JOIN developers d ON b.assigned_to = d.developer_id " +
                       "WHERE d.name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, developerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Bug bug = new Bug();
                    bug.setBugId(resultSet.getInt("bug_id"));
                    bug.setDescription(resultSet.getString("description"));
                    bug.setCategoryId(resultSet.getInt("category_id"));
                    bug.setAssignedTo(resultSet.getInt("assigned_to"));
                    bug.setStatus(resultSet.getString("status"));
                    bug.setCreatedDate(resultSet.getTimestamp("created_date"));
                    bug.setResolvedDate(resultSet.getTimestamp("resolved_date"));
                    bugs.add(bug);
                }
            }
        }
        return bugs;
    }
    
    //Get all bugs along with their category names
    public List<Bug> getAllBugsWithCategoryNames() throws SQLException {
        List<Bug> bugs = new ArrayList<>();
        String query = "SELECT b.*, c.category_name FROM bugs b " +
                       "JOIN categories c ON b.category_id = c.category_id";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Bug bug = new Bug();
                bug.setBugId(resultSet.getInt("bug_id"));
                bug.setDescription(resultSet.getString("description"));
                bug.setCategoryId(resultSet.getInt("category_id"));
                bug.setAssignedTo(resultSet.getInt("assigned_to"));
                bug.setStatus(resultSet.getString("status"));
                bug.setCreatedDate(resultSet.getTimestamp("created_date"));
                bug.setResolvedDate(resultSet.getTimestamp("resolved_date"));
                // Adding category name to description for demonstration purposes
                bug.setDescription(bug.getDescription() + " (Category: " + resultSet.getString("category_name") + ")");
                bugs.add(bug);
            }
        }
        return bugs;
    }
    
    //Count the number of bugs assigned to each developer
    public List<String> getBugCountsByDeveloper() throws SQLException {
        List<String> result = new ArrayList<>();
        String query = "SELECT d.name, COUNT(b.bug_id) AS bug_count FROM developers d " +
                       "LEFT JOIN bugs b ON d.developer_id = b.assigned_to " +
                       "GROUP BY d.name";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String entry = resultSet.getString("name") + " has " + resultSet.getInt("bug_count") + " bugs";
                result.add(entry);
            }
        }
        return result;
    }
    
    //Get bugs with their assigned developer's details
    public List<String> getBugsWithDeveloperDetails() throws SQLException {
        List<String> result = new ArrayList<>();
        String query = "SELECT b.bug_id, b.description, d.name, d.email FROM bugs b " +
                       "JOIN developers d ON b.assigned_to = d.developer_id";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                String entry = "Bug ID: " + resultSet.getInt("bug_id") +
                               ", Description: " + resultSet.getString("description") +
                               ", Assigned to: " + resultSet.getString("name") +
                               " (" + resultSet.getString("email") + ")";
                result.add(entry);
            }
        }
        return result;
    }
    
    //Get bugs resolved within a specific time frame
    public List<Bug> getBugsResolvedInTimeFrame(Timestamp startTime, Timestamp endTime) throws SQLException {
        List<Bug> bugs = new ArrayList<>();
        String query = "SELECT * FROM bugs WHERE resolved_date BETWEEN ? AND ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, startTime);
            statement.setTimestamp(2, endTime);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Bug bug = new Bug();
                    bug.setBugId(resultSet.getInt("bug_id"));
                    bug.setDescription(resultSet.getString("description"));
                    bug.setCategoryId(resultSet.getInt("category_id"));
                    bug.setAssignedTo(resultSet.getInt("assigned_to"));
                    bug.setStatus(resultSet.getString("status"));
                    bug.setCreatedDate(resultSet.getTimestamp("created_date"));
                    bug.setResolvedDate(resultSet.getTimestamp("resolved_date"));
                    bugs.add(bug);
                }
            }
        }
        return bugs;
    }

    
    //Get developers with the highest number of bugs assigned
    public List<Developer> getDevelopersWithMostBugsAssigned() throws SQLException {
        List<Developer> developers = new ArrayList<>();
        String query = "SELECT d.* FROM developers d " +
                       "JOIN (SELECT assigned_to, COUNT(*) AS bug_count FROM bugs GROUP BY assigned_to) AS bug_counts " +
                       "ON d.developer_id = bug_counts.assigned_to " +
                       "ORDER BY bug_counts.bug_count DESC LIMIT 1";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Developer developer = new Developer();
                developer.setDeveloperId(resultSet.getInt("developer_id"));
                developer.setName(resultSet.getString("name"));
                developer.setEmail(resultSet.getString("email"));
                developers.add(developer);
            }
        }
        return developers;
    }
    
    //Get all bugs within a specific category
    public List<Bug> getBugsByCategoryName(String categoryName) throws SQLException {
        List<Bug> bugs = new ArrayList<>();
        String query = "SELECT b.* FROM bugs b " +
                       "JOIN categories c ON b.category_id = c.category_id " +
                       "WHERE c.category_name = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, categoryName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Bug bug = new Bug();
                    bug.setBugId(resultSet.getInt("bug_id"));
                    bug.setDescription(resultSet.getString("description"));
                    bug.setCategoryId(resultSet.getInt("category_id"));
                    bug.setAssignedTo(resultSet.getInt("assigned_to"));
                    bug.setStatus(resultSet.getString("status"));
                    bug.setCreatedDate(resultSet.getTimestamp("created_date"));
                    bug.setResolvedDate(resultSet.getTimestamp("resolved_date"));
                    bugs.add(bug);
                }
            }
        }
        return bugs;
    }
    
    //Get the maximum number of bugs assigned to a single developer
    public int getMaxBugsAssignedToDeveloper() throws SQLException {
        String query = "SELECT MAX(bug_count) AS max_bugs FROM (SELECT COUNT(*) AS bug_count FROM bugs GROUP BY assigned_to) AS bug_counts";
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                return resultSet.getInt("max_bugs");
            }
        }
        return 0;
    }
    
    //Get developers who have resolved more than a certain number of bugs
    public List<Developer> getDevelopersWithResolvedBugsAboveThreshold(int threshold) throws SQLException {
        List<Developer> developers = new ArrayList<>();
        String query = "SELECT d.* FROM developers d " +
                       "JOIN (SELECT assigned_to, COUNT(*) AS resolved_count FROM bugs WHERE status = 'Resolved' GROUP BY assigned_to HAVING resolved_count > ?) AS resolved_bugs " +
                       "ON d.developer_id = resolved_bugs.assigned_to";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, threshold);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Developer developer = new Developer();
                    developer.setDeveloperId(resultSet.getInt("developer_id"));
                    developer.setName(resultSet.getString("name"));
                    developer.setEmail(resultSet.getString("email"));
                    developers.add(developer);
                }
            }
        }
        return developers;
    }

	public ObservableList<Bug> getAllBugsById() throws SQLException {
		 ObservableList<Bug> bugsid = FXCollections.observableArrayList();
	        String query = "SELECT * FROM bugs";
	        try (Connection connection = DBConnection.getConnection();
	             Statement statement = connection.createStatement();
	             ResultSet resultSet = statement.executeQuery(query)) {
	            while (resultSet.next()) {
	                Bug bug = new Bug();
	                bug.setBugId(resultSet.getInt("bug_id"));
	                bugsid.add(bug);
	            }
	        }
//	    	System.out.println(bugs);
	        return bugsid;
	}
}


