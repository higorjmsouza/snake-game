package higor.jacinto.snake;

import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class SnakeGame extends Application {

    private static final AudioClip bgSound = new AudioClip(Objects.requireNonNull(SnakeGame.class.getResource("/sounds/background.wav")).toExternalForm());

    @Override
    public void start(final Stage stage) {
        stage.setTitle("Snake Game com IA");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/snake-game.png"))));
        final var root = new StackPane();

        final var jogarLabel = getAnimatedLabel("▶ Jogar", () -> PlayerController.run(stage));
        final var iaLabel = getAnimatedLabel("🤖 IA Jogar", () -> AIController.run(stage));
        final var title = getTitle();

        final var vbox = new VBox(20, title, jogarLabel, iaLabel);
        vbox.setAlignment(Pos.CENTER);
        VBox.setMargin(title, new Insets(0, 0, 20, 0));

        final var minLabelWidth = 200D;
        jogarLabel.setMinWidth(minLabelWidth);
        jogarLabel.setAlignment(Pos.CENTER);

        iaLabel.setMinWidth(minLabelWidth);
        iaLabel.setAlignment(Pos.CENTER);

        root.getChildren().addAll(getBackground(), vbox);

        final var scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.show();

        if (bgSound.isPlaying()) {
            bgSound.stop();
        }

        bgSound.setCycleCount(AudioClip.INDEFINITE);
        bgSound.setVolume(0.1D);
        bgSound.play();
    }

    private ImageView getBackground() {
        final var background = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/background.jpg"))));
        background.setFitWidth(600);
        background.setFitHeight(400);
        background.setPreserveRatio(false);
        return background;
    }

    private Label getTitle() {
        final var title = new Label("🐍 Snake Game");
        title.setStyle("-fx-font-size: 32px; -fx-text-fill: white; -fx-font-weight: bold;");
        title.setEffect(new DropShadow(4, Color.BLACK));
        return title;
    }

    private Label getAnimatedLabel(final String text, final Runnable onClick) {
        final var label = new Label(text);
        label.setStyle("-fx-font-size: 22px; -fx-text-fill: white; -fx-background-radius: 8px; -fx-padding: 10px;");
        label.setEffect(new DropShadow(3, Color.BLACK));

        label.setOnMouseEntered(e -> {
            label.setStyle("-fx-font-size: 22px; -fx-text-fill: #FFD700; -fx-background-color: rgba(255,255,255,0.1); -fx-background-radius: 8px; -fx-padding: 10px;");
            label.setCursor(Cursor.HAND);
            animateScale(label, 1.1);
        });

        label.setOnMouseExited(e -> {
            label.setStyle("-fx-font-size: 22px; -fx-text-fill: white; -fx-background-radius: 8px; -fx-padding: 10px;");
            animateScale(label, 1.0);
        });

        label.setOnMouseClicked(e -> {
            final var clickSound = new AudioClip(Objects.requireNonNull(SnakeGame.class.getResource("/sounds/click.wav")).toExternalForm());
            clickSound.setVolume(0.1D);
            clickSound.play();
            onClick.run();
        });

        return label;
    }

    private void animateScale(final Label label, final Double scale) {
        final var st = new ScaleTransition(Duration.millis(200), label);
        st.setToX(scale);
        st.setToY(scale);
        st.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
