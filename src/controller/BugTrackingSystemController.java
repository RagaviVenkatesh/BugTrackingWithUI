package controller;

import java.sql.SQLException;


import dao.BugDAO;
import dao.UserDAO;
import javafx.collections.ObservableList;
import model.Bug;
import model.User;

public class BugTrackingSystemController {
	UserDAO userDAO= new UserDAO();
	BugDAO bugDAO = new BugDAO();
	
	public boolean userValidate(User user) throws SQLException {
         boolean result = userDAO.userValidate(user);
         return result;
    }
	  public ObservableList<Bug> getAllBugs() throws SQLException {
			
		  return bugDAO.getAllBugs();
	    }
	  public ObservableList<Bug> getAllBugsById() throws SQLException {
			
		  return bugDAO.getAllBugsById();
	    }
	public int addBugs(Bug bug) throws SQLException {
		System.out.println("Add Bug!");
		int result = bugDAO.addBug(bug);
		return result;
	}
	
	public int updateBugs(int bugId, String description,int categoryId, int developerId, String Status) throws SQLException {
		int result = bugDAO.updateBug(bugId,description,categoryId,developerId,Status);
		return result;
	}
	
	public int deleteBugs(int bug) throws SQLException {
		int result = bugDAO.deleteBug(bug);
		return result;
	}
}
