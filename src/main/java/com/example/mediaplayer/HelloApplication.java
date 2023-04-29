package com.example.mediaplayer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

//import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class HelloApplication extends Application {
    private MediaPlayer mediaPlayer;
    @Override
    public void start(Stage stage) throws IOException {
        SplitPane splitPane = new SplitPane();
        BorderPane pane = new BorderPane();
        Button sport = new Button("\uD83D\uDCC1 Explorer");
        sport.setId("files");
        Button playPuase = new Button("▶");
        playPuase.setId("playpuase");
        Button forward = new Button("⏭");
        forward.setId("playpuase");
        Button backward = new Button("⏪");
        Button stop = new Button("■");
        stop.setId("playpuase");
        Button reapet = new Button("\uD83D\uDD01");
        reapet.setId("playpuase");
        backward.setId("playpuase");
        Slider volume = new Slider();
        Slider slTime = new Slider();
        Label lbTime = new Label();
        Label label = new Label();
        Label medianame = new Label("Unkwon");
        ImageView imageView = new ImageView();


        VBox mediadetails = new VBox();
        mediadetails.setId("mediadetails");
        HBox hBox1 = new HBox(30);
        HBox vumes = new HBox();
        vumes.setId("vumes");
        HBox times = new HBox();
        times.setId("times");
        MediaView mediaView = new MediaView();
        sport.setOnAction(actionEvent -> {
            FileChooser mmino = new FileChooser();
            mmino.setTitle("Open Resource File");
            mmino.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac","*.mp4", "*.avi","*.3gp","*.mkv"));
            File choosenfile = mmino.showOpenDialog(stage);
            if(mediaPlayer != null){
                mediaPlayer.stop();
                mediaPlayer = null;
            }
            if (choosenfile != null){
                Media media = new Media(choosenfile.toURI().toString());
                mediaPlayer = new  MediaPlayer(media);
                mediaPlayer.setAutoPlay(true);
                playPuase.setText("⏸");
                String fileName = choosenfile.getName();
                medianame.setText(fileName);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaView.setFitHeight(500);
                mediaView.setFitWidth(600);
                playPuase.setOnAction(e -> {
                    if (playPuase.getText().equals("▶")) {
                        mediaPlayer.play();
                        playPuase.setText("⏸");
                    } else {
                        mediaPlayer.pause();
                        playPuase.setText("▶");
                    }
                });
                ////////volume slider
                volume.setPrefWidth(150);
                volume.setMaxWidth(Region.USE_PREF_SIZE);
                volume.setMinWidth(30);
                volume.setValue(10);
                volume.setOrientation(Orientation.HORIZONTAL);
                volume.setLayoutX(50);
                volume.setLayoutY(50);
                volume.setPrefWidth(100);
                mediaPlayer.volumeProperty().bind(volume.valueProperty().divide(100));
                label.setText(""+volume.getValue());
                volume.valueProperty().addListener((observable, oldValue, newValue) -> {
                    label.setText(""+newValue.intValue());
                });
                ///////////////duration
                slTime.setPrefWidth(150);
                slTime.setMaxWidth(Region.USE_PREF_SIZE);
                slTime.setMinWidth(30);
                slTime.setMin(0);
                mediaPlayer.currentTimeProperty().addListener(ov -> {
                    if (!slTime.isValueChanging()) {
                        double total = mediaPlayer.getTotalDuration().toMillis();
                        double current = mediaPlayer.getCurrentTime().toMillis();
                        slTime.setMax(total);
                        slTime.setValue(current);
                        lbTime.setText(getTimeString(current) + "/" + getTimeString(total));
                    }
                });
                slTime.valueProperty().addListener(ov -> {
                    if (slTime.isValueChanging()) {
                        mediaPlayer.seek(new Duration(slTime.getValue()));
                    }
                });
                // Set the onEndOfMedia event handler
                mediaPlayer.setOnEndOfMedia(() -> {
                    // Set the MediaPlayer to the beginning and pause it
                    mediaPlayer.seek(Duration.ZERO);
                    mediaPlayer.pause();

                    // Change the text of the play button to "Play"
                    playPuase.setText("▶");
                });
                forward.setOnAction(event -> {
                    mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
                });
                backward.setOnAction(event -> {
                    mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.seconds(10)));
                });
                stop.setOnAction(event -> {
                    if(playPuase.getText().equals("⏸")){
                        mediaPlayer.stop();
                        playPuase.setText("▶");
                    }
                    else {
                        mediaPlayer.stop();
                    }
                });
                reapet.setOnAction(event -> {
                    mediaPlayer.setCycleCount(2); // sets the media to play only once
                    //mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // sets the media to repeat indefinitely
                });
                // If the selected file is an mp3, set the background of the root pane to an image
                if (choosenfile.getName().endsWith(".mp3")) {
                    Image backgroundImage = new Image(getClass().getResourceAsStream("/mp3background.jpeg"));
                    BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
                    pane.setBackground(new Background(background));
                }
                else {
                    BackgroundFill backgroundFill =
                            new BackgroundFill(
                                    Color.valueOf("#000000"),
                                    new CornerRadii(10),
                                    new Insets(10)
                            );
                    Background background = new Background(backgroundFill);
                    pane.setBackground(background);
                    mediaView.setFitHeight(1000);
                    mediaView.setFitWidth(700);
                }
            }
            mediadetails.getChildren().addAll(medianame);
            vumes.getChildren().addAll (new Label("Vol"),volume,label);
            times.getChildren().addAll(new Label("Time"), slTime, lbTime);
            hBox1.getChildren().addAll(backward,playPuase,stop,forward,reapet,new Label("      "),times,vumes);
        });


        //BorderPane pane = new BorderPane();
        pane.setId("pane");
        //pane.setMaxWidth(800);
        pane.setCenter(mediaView);
        pane.setTop(mediadetails);
        pane.setBottom(hBox1);
       // pane.setLeft(vumes);
        pane.setRight(imageView);
       // HBox hBox =new HBox(vBox,mediaView);

        VBox main = new VBox(20,sport,pane);
        main.setId("main");
        splitPane.getItems().addAll(main);
        Scene scene = new Scene(splitPane, 700, 600);
        stage.setTitle("Media Player");
        String style = getClass().getResource("/styling.css").toExternalForm();
        scene.getStylesheets().add(style);
        stage.setScene(scene);
        stage.show();
    }
    public static String getTimeString(double millis) {
        millis /= 1000;
        String s = formatTime(millis % 60);
        millis /= 60;
        String m = formatTime(millis % 60);
        millis /= 60;
        String h = formatTime(millis % 24);
        return h + ":" + m + ":" + s;
    }
    public static String formatTime(double time) {
        int t = (int)time;
        if (t > 9) { return String.valueOf(t); }
        return "0" + t;
    }
}