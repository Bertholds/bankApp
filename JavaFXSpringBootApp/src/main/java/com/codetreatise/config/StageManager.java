package com.codetreatise.config;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.Objects;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.codetreatise.controller.AdherentsController;
import com.codetreatise.view.FxmlView;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * Manages switching Scenes on the Primary Stage
 */
public class StageManager {

    private static final Logger LOG = getLogger(StageManager.class);
    private Stage primaryStage;
    private Stage newPrimaryStage;
    private final SpringFXMLLoader springFXMLLoader;
    
    @Autowired
	private AdherentsController adherentsController;

    public StageManager(SpringFXMLLoader springFXMLLoader, Stage stage) {
        this.springFXMLLoader = springFXMLLoader;
        this.primaryStage = stage;
    }

    public void switchScene(final FxmlView view) {
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
        show(viewRootNodeHierarchy, view.getTitle());
    }
    
    public Stage switchSceneShowPreviousStage(final FxmlView view) {
		Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
		return showPreviousStage(viewRootNodeHierarchy, view.getTitle());
	}
    
    public void switchSceneShowPreviousStageInitOwner(final FxmlView view) {
		Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlFile());
		showPreviousStageInitOwner(viewRootNodeHierarchy, view.getTitle());
	}
    private void show(final Parent rootnode, String title) {
        Scene scene = prepareScene(rootnode);
        //scene.getStylesheets().add("/styles/Styles.css");
        
        //primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();
        
        try {
            primaryStage.show();
        } catch (Exception exception) {
            logAndExit ("Unable to show scene for title" + title,  exception);
        }
    }
    
    private Scene prepareScene(Parent rootnode){
        Scene scene = primaryStage.getScene();

        if (scene == null) {
            scene = new Scene(rootnode);
        }
        scene.setRoot(rootnode);
        return scene;
    }
private Scene prepareSceneShowPreviousScene(Parent rootnode) {
		
		Scene scene = new Scene(rootnode);
		System.out.println("numero 1");
		return scene;
	}

//this method create a new stage and display him
	private Stage showPreviousStage(final Parent rootnode, String title) {
		Scene scene = prepareSceneShowPreviousScene(rootnode);
		// scene.getStylesheets().add("/styles/Styles.css");

		newPrimaryStage = new Stage();
		newPrimaryStage.setTitle(title);
		newPrimaryStage.setScene(scene);
		newPrimaryStage.sizeToScene();
		newPrimaryStage.setX(primaryStage.getX() + 259); 
		newPrimaryStage.setY(primaryStage.getY() + 58);
		newPrimaryStage.initModality(Modality.WINDOW_MODAL);
		//newPrimaryStage.toFront();
		//primaryStage.initOwner(this.primaryStage);

		try {
			newPrimaryStage.initStyle(StageStyle.TRANSPARENT);
			newPrimaryStage.show();
		} catch (Exception exception) {
			logAndExit("Unable to show scene for title" + title, exception);
		}
		return newPrimaryStage;
	}

  //this method permit to define owner on stage dialog
  	private void showPreviousStageInitOwner(final Parent rootnode, String title) {
  		Scene scene = prepareSceneShowPreviousScene(rootnode);
  		// scene.getStylesheets().add("/styles/Styles.css");

  		// primaryStage.initStyle(StageStyle.TRANSPARENT);
  	    Stage  primaryStage = new Stage();
  	    primaryStage.setTitle(title);
  	    primaryStage.setScene(scene);
  	    primaryStage.sizeToScene();
  	    primaryStage.centerOnScreen();
  	    primaryStage.initModality(Modality.APPLICATION_MODAL);
  	    primaryStage.initOwner(this.primaryStage);
  	    primaryStage.setResizable(false);
  	    primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
			@Override
			public void handle(WindowEvent arg0) {
				adherentsController.setIsEditButtonClick(false);
			}
		});

  		try {
  			primaryStage.show();
  		} catch (Exception exception) {
  			logAndExit("Unable to show scene for title" + title, exception);
  		}
  	}
    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath) {
        Parent rootNode = null;
        try {
            rootNode = springFXMLLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception) {
            logAndExit("Unable to load FXML view" + fxmlFilePath, exception);
        }
        return rootNode;
    }
    
    
    private void logAndExit(String errorMsg, Exception exception) {
        LOG.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }

}
