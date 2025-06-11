// package BouncingBalls.src;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class BouncingBalls extends Application {
    private final ArrayList<Ball> balls = new ArrayList<>();
    private final Random rand = new Random();

    @Override
    public void start(Stage primaryStage) {
        Pane pane = new Pane();
        pane.setPrefSize(600, 400);

        
        pane.setOnMouseClicked(e -> addBall(e, pane));

        
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(20), e -> moveBalls(pane)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Scene scene = new Scene(pane);
        primaryStage.setTitle("Bouncing Balls");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void addBall(MouseEvent event, Pane pane) {
        double x = event.getX();
        double y = event.getY();
        double dx = rand.nextDouble() * 4 - 2; 
        double dy = rand.nextDouble() * 4 - 2;

        Color color = Color.color(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());

        Ball ball = new Ball(x, y, dx, dy, 10, color);
        balls.add(ball);
        pane.getChildren().add(ball.circle);
    }

    private void moveBalls(Pane pane) {
        for (Ball ball : balls) {
            ball.move(pane.getWidth(), pane.getHeight());
        }
    }

    private static class Ball {
        Circle circle;
        double dx, dy;

        Ball(double x, double y, double dx, double dy, double radius, Color color) {
            this.circle = new Circle(x, y, radius, color);
            this.dx = dx;
            this.dy = dy;
        }

        void move(double width, double height) {
            double x = circle.getCenterX();
            double y = circle.getCenterY();

          
            if (x + dx < 0 || x + dx > width) {
                dx *= -1;
            }
            if (y + dy < 0 || y + dy > height) {
                dy *= -1;
            }

            circle.setCenterX(x + dx);
            circle.setCenterY(y + dy);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
