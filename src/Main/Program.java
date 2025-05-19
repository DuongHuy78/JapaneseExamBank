package Main;

import View.UI.UI;

public class Program {
	public static void main(String[] args) {
		UI ui = new UI();
		ui.run();
	}
}


//import javafx.application.Application;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.stage.Stage;
//import java.io.File;
//
//public class Program extends Application {
//    @Override
//    public void start(Stage primaryStage) {
//        String path =  "D:\\Downloads\\Doriru Doriru N3 Choukai Dokkai audio\\Doriru Doriru N3 Choukai Dokkai audio\\CD-B\\39 Track 39.mp3"; // Đường dẫn đến tệp MP3
//        Media media = new Media(new File(path).toURI().toString());
//        MediaPlayer mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

