import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.beans.value.ChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MediaTest extends Application {

    @Override
    public void start(Stage stage) throws FileNotFoundException {
        VBox root = new VBox();
        ContextMenu cm = new ContextMenu();
        ContextMenu vm = new ContextMenu();
        MenuItem mv1 = new MenuItem("Głośnik");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("MediaTest");
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Audio","*.mp3"),
                new FileChooser.ExtensionFilter("Video","*mp4")
        );
        String source = fc.showOpenDialog(stage).toURI().toString();

        stage.show();
        Media media = new Media(source);
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(2147362154);
        Slider sl = new Slider(0.25,4.0,1.0);
        Slider v = new Slider(0.0,1.0,0.5);
        Slider tm = new Slider();
        sl.setShowTickMarks(true);
        sl.setMajorTickUnit(0.25);
        sl.setShowTickLabels(true);
        sl.setSnapToTicks(true);
        sl.setMaxSize(200,40);
        MenuItem mv2 = new MenuItem("0.5");

        v.valueProperty().addListener((o,ol,ne)->{mediaPlayer.setVolume((Double)ne);mv2.setText(String.valueOf(ne));});
        sl.valueProperty().addListener((observable,old,New)-> mediaPlayer.setRate((Double) New));
        mediaPlayer.cycleDurationProperty().addListener((observable,old,nw) -> tm.setMax(mediaPlayer.getCycleDuration().toSeconds()));
        ChangeListener<Duration> list = new ChangeListener<Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {

                    tm.setValue(newValue.toSeconds());

            }
        };
        mediaPlayer.currentTimeProperty().addListener(list);
        mediaPlayer.setOnRepeat(()-> tm.setValue(0));
        tm.valueProperty().addListener((ob,o,n)-> {
                    tm.setValue((Double) n);
                    if(((Double)o*1.02<(Double)n||(Double)o>(Double)n*1.02)&&(Double)n>=3)
                    mediaPlayer.seek(Duration.seconds((Double) n));
                }
            );
        MediaView mediaView = new MediaView(mediaPlayer);
        MenuItem mi = new MenuItem("Prędkość");
        MenuItem m2 = new MenuItem("1.0");
        sl.valueProperty().addListener((event,o,n) -> m2.setText(String.valueOf(n)));
        v.setShowTickLabels(true);
        v.setMajorTickUnit(0.1);
        v.setShowTickMarks(true);
        v.setContextMenu(vm);
        vm.getItems().addAll(mv2,mv1);
        cm.getItems().add(mi);
        cm.getItems().add(m2);
        sl.setContextMenu(cm);
        Button start = new Button(">");
        Button stop = new Button("||");
        Button pr = new Button("<<");
        Button ne = new Button(">>");
        start.setDisable(true);
        pr.setOnAction(e->{
           Duration d = mediaPlayer.getCurrentTime();
           Duration sub = new Duration(-10_000);
           Duration duration = d.add(sub);
           mediaPlayer.seek(duration);
        });
        mediaPlayer.seek(Duration.seconds(12));
        mediaPlayer.seek(Duration.seconds(13));
        ne.setOnAction(e->{Duration d = mediaPlayer.getCurrentTime();
            Duration sub = new Duration(10_000);
            Duration duration = d.add(sub);
            mediaPlayer.seek(duration);
        });
        start.setOnAction(e ->{mediaPlayer.play();start.setDisable(true);stop.setDisable(false);});
        stop.setOnAction(e -> {mediaPlayer.pause();start.setDisable(false);stop.setDisable(true);});
        stage.setWidth(640);
        stage.setHeight(425);
        mediaView.setFitWidth(640);
        mediaView.setFitHeight(350);
        ((VBox) scene.getRoot()).getChildren().add(mediaView);
        stage.setIconified(true);
        stage.getIcons().add(new Image(new FileInputStream(new File("C:\\Users\\RP\\IdeaProjects\\mediaTest\\.idea\\Icon.png"))));
        HBox h =  new HBox();
        h.getChildren().addAll(pr,start,stop,ne);
        HBox x = new HBox();
        x.getChildren().addAll(sl,v);
        ((VBox) scene.getRoot()).getChildren().addAll(tm,x,h);
    }
    public static void main(String[] args) {

        launch(args);
    }
}
