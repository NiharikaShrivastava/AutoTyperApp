import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoTyperApp extends Application {

    private volatile boolean isTyping = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Enter text to type:");
        TextArea textArea = new TextArea();

        Label speedLabel = new Label("Typing Delay (ms):");
        TextField delayField = new TextField("100");

        Button startButton = new Button("Start Typing");
        Button stopButton = new Button("Stop");

        startButton.setOnAction(e -> {
            if (!isTyping) {
                isTyping = true;
                String text = textArea.getText();
                int delay = Integer.parseInt(delayField.getText());
                startTyping(text, delay);
            }
        });

        stopButton.setOnAction(e -> isTyping = false);

        VBox root = new VBox(10, label, textArea, speedLabel, delayField, startButton, stopButton);
        root.setPadding(new Insets(15));
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Auto Typer");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void startTyping(String text, int delay) {
        executor.submit(() -> {
            try {
                Thread.sleep(3000); // Time to switch to another window
                Robot robot = new Robot();
                for (char ch : text.toCharArray()) {
                    if (!isTyping) break;

                    typeCharacter(robot, ch);
                    Thread.sleep(delay);
                }
            } catch (AWTException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    private void typeCharacter(Robot robot, char ch) {
        try {
            boolean upperCase = Character.isUpperCase(ch);
            int keyCode = KeyEvent.getExtendedKeyCodeForChar(ch);

            if (keyCode == KeyEvent.VK_UNDEFINED) return;

            if (upperCase) robot.keyPress(KeyEvent.VK_SHIFT);

            robot.keyPress(keyCode);
            robot.keyRelease(keyCode);

            if (upperCase) robot.keyRelease(KeyEvent.VK_SHIFT);
        } catch (IllegalArgumentException ignored) {
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}







