* launch.json 跟 setting.json 記得要改
* Java 跟 JavaFX 的版本要對應  
  openjdk 17.0.14 2025-01-21  
  javafx-sdk-17.0.15
* 在終端用指令編譯執行
  javac --module-path "C:/Program Files/JavaFX/javafx-sdk-17.0.15/lib" --add-modules javafx.controls,javafx.fxml -d bin src/BouncingBalls.java
  java --module-path "C:/Program Files/JavaFX/javafx-sdk-17.0.15/lib" --add-modules javafx.controls,javafx.fxml -cp bin BouncingBalls
