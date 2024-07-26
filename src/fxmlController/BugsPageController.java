package fxmlController;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import controller.BugTrackingSystemController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.Bug;
import model.User;
import utility.DBConnection;

public class BugsPageController implements Initializable {

    @FXML
    private Button Category;

    @FXML
    private TableView<Bug> availablebugs_tableview;

    @FXML
    private AnchorPane bug_Form;

    @FXML
    private Button check_btn;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private TextField open_bugs;

    @FXML
    private TextField resolved_bugs;

    @FXML
    private TextField total_no_of_bugs;
    
    @FXML
    private TableColumn<Bug, Integer> available_assigned;

    @FXML
    private TableColumn<Bug, Integer> available_bugid;

    @FXML
    private TableColumn<Bug, Integer> available_category;

    @FXML
    private TableColumn<Bug, String> available_description;

    @FXML
    private TableColumn<Bug, String> available_status;
    
    @FXML
    private TextField bug_id;

    @FXML
    private TextField category_id;
    
    @FXML
    private TextField developer_id;
    
    @FXML
    private TextArea bug_description;
    
    @FXML
    private TextField status;
    
    
    
    ObservableList<Bug> bugList;
   

    BugTrackingSystemController con = new BugTrackingSystemController();
    @FXML
    void CategoryForm(ActionEvent event) {

    }

    @FXML
    void addbugs(ActionEvent event) {
    	System.out.println("Add Bug!");
    	try {
    		Alert alert;
    		if(bug_id.getText().isEmpty()
    				||category_id.getText().isEmpty()
    				|| developer_id.getText().isEmpty()
    				|| bug_description.getText().isEmpty()
    				) {
    			alert = new Alert(AlertType.ERROR);
    			alert.setHeaderText(null);
        		alert.setContentText("Enter all values!");
        		alert.showAndWait();
    		}
    		else
    		{
    			int result = con.addBugs(new Bug(0,bug_description.getText(),Integer.parseInt(category_id.getText()),Integer.parseInt(developer_id.getText()),"open", new Date(), null));
        		if(result>0)
        		{
        			showAvailableBugList();
        			alert = new Alert(AlertType.INFORMATION);
        			alert.setHeaderText(null);
            		alert.setContentText("Bug Added!");
            		alert.showAndWait();
        		}
        		else
        		{
        			alert = new Alert(AlertType.ERROR);
        			alert.setHeaderText(null);
            		alert.setContentText("Error! Couldn't add Bug!");
            		alert.showAndWait();
        		}
    		}
    		
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void deletebugs(ActionEvent event) {
    	try
    	{
    		Alert alert;
    		if(bug_id.getText().isEmpty()) {
    			alert = new Alert(AlertType.ERROR);
    			alert.setHeaderText(null);
        		alert.setContentText("Enter all values!");
        		alert.showAndWait();
    		}
    		else
    		{
    			int result = con.deleteBugs(Integer.parseInt(bug_id.getText()));
        		if(result>0)
        		{
        			showAvailableBugList();
        			alert = new Alert(AlertType.INFORMATION);
        			alert.setHeaderText(null);
            		alert.setContentText("Bug Deleted!");
            		alert.showAndWait();
        		}
        		else
        		{
        			alert = new Alert(AlertType.ERROR);
        			alert.setHeaderText(null);
            		alert.setContentText("Error! Couldn't delete Bug!");
            		alert.showAndWait();
        		}
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @FXML
    void updatebugs(ActionEvent event) {
    	try
    	{
    		Alert alert;
    		if(bug_id.getText().isEmpty()
    				||category_id.getText().isEmpty()
    				|| developer_id.getText().isEmpty()
    				|| bug_description.getText().isEmpty()
    				) {
    			alert = new Alert(AlertType.ERROR);
    			alert.setHeaderText(null);
        		alert.setContentText("Enter all values!");
        		alert.showAndWait();
    		}
    		else
    		{
    			
    			int result = con.updateBugs(Integer.parseInt(bug_id.getText()),bug_description.getText(),Integer.parseInt(category_id.getText()),Integer.parseInt(developer_id.getText()),status.getText());
        		if(result>0)
        		{
        			showAvailableBugList();
        			alert = new Alert(AlertType.INFORMATION);
        			alert.setHeaderText(null);
            		alert.setContentText("Bug Updated!");
            		alert.showAndWait();
        		}
        		else
        		{
        			alert = new Alert(AlertType.ERROR);
        			alert.setHeaderText(null);
            		alert.setContentText("Error! Couldn't update Bug!");
            		alert.showAndWait();
        		}
    		}
    	}
    	catch(Exception e) {
    		e.printStackTrace();
    	}

    }


    @FXML
    void switchForm(ActionEvent event) {

    }
    
    private void showAvailableBugList() throws SQLException {
    	bugList =  con.getAllBugs();
    	for(Bug b:bugList)
    	System.out.println(b.getBugId());
    	
    	available_bugid.setCellValueFactory(new PropertyValueFactory<Bug,Integer>("bugId"));
    	available_description.setCellValueFactory(new PropertyValueFactory<Bug,String>("description"));
    	available_assigned.setCellValueFactory(new PropertyValueFactory<Bug,Integer>("assignedTo"));
    	available_category.setCellValueFactory(new PropertyValueFactory<Bug,Integer>("categoryId"));
    	available_status.setCellValueFactory(new PropertyValueFactory<Bug,String>("status"));
    	availablebugs_tableview.setItems(bugList);
    	
    	
    }
    public void initialize(URL location,ResourceBundle resources) {
	try {
		showAvailableBugList();
	//	getBugIds();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}


}
