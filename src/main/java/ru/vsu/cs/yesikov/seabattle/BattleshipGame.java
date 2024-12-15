package ru.vsu.cs.yesikov.seabattle;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;

public class BattleshipGame extends Application {
    private Game game;
    private GridPane player1Grid;
    private GridPane player2Grid;
    private Label messageLabel2;
    private Label messageLabel1;
    private final int size = 10;
    private int currentShipLength = 4;
    private boolean start = false;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private boolean horizontal = true;
    private int currentGun = 1;
    private VBox buttonsVBox;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        game = new Game(3, 3, 3);
        primaryStage.setTitle("Battleship Game");
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        player1Grid = createGridPane(1);
        player2Grid = createGridPane(2);

        messageLabel1 = new Label("""
                Корабли выставляются вниз и вправо
                на выбранную вами длину в зависимости от
                выбранного вами направления.""");
        messageLabel2 = new Label("Игрок 1 расставляет корабли! Изначальная длина 4!");
        messageLabel2.setPrefSize(300, 40);
        Label lab = new Label("<-------------------------------------------------------------->");
        button5 = new Button("Разместить горизонтально");
        button5.setPrefSize(200, 25);
        button5.setOnAction(e -> changeHor());

        button6 = new Button("Мин осталось " + (3 - game.countOfMines(1)));
        button6.setPrefSize(200, 25);
        button6.setOnAction(e -> setCurrentShipLength(0));

        button4 = new Button("Кораблей длины 4 осталось " + (1 - game.countOfShip4(1)));
        button4.setPrefSize(200, 25);
        button4.setOnAction(e -> setCurrentShipLength(4));

        button3 = new Button("Кораблей длины 3 осталось " + (2 - game.countOfShip3(1)));
        button3.setPrefSize(200, 25);
        button3.setOnAction(e -> setCurrentShipLength(3));

        button2 = new Button("Кораблей длины 2 осталось " + (3 - game.countOfShip2(1)));
        button2.setPrefSize(200, 25);
        button2.setOnAction(e -> setCurrentShipLength(2));

        button1 = new Button("Кораблей длины 1 осталось " + (4 - game.countOfShip1(1)));
        button1.setPrefSize(200, 25);
        button1.setOnAction(e -> setCurrentShipLength(1));

        HBox hbox = new HBox(20);
        messageLabel1.setAlignment(Pos.CENTER);
        messageLabel2.setAlignment(Pos.CENTER);
        VBox player1VBox = new VBox(new Label("Ваше поле"), player1Grid);
        VBox player2VBox = new VBox(new Label("Поле противника"), player2Grid);
        buttonsVBox = new VBox(button5, button6, button4, button3, button2, button1, messageLabel2, lab, messageLabel1);

        buttonsVBox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(player1VBox, buttonsVBox, player2VBox);
        hbox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(hbox);

        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private GridPane createGridPane(int player) {
        GridPane grid = new GridPane();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button cellButton = new Button();
                cellButton.setMinSize(30, 30);
                final int x = i;
                final int y = j;
                cellButton.setOnAction(e -> click(x, y, player));
                grid.add(cellButton, j, i);
            }
        }
        return grid;
    }

    private void startChange() {
        messageLabel1.setText("Выберите необходимое вам оружие\nи нажмите на поле противника");
        messageLabel2.setText("Вы используете снаряд!");

        buttonsVBox.getChildren().remove(button4);
        buttonsVBox.getChildren().remove(button5);
        buttonsVBox.getChildren().remove(button6);

        button1.setText("Снаряды");
        button1.setOnAction(e -> setCurrentGun(1));

        button2.setText("Подводные лодки");
        button2.setOnAction(e -> setCurrentGun(2));

        button3.setText("Минные тральщики");
        button3.setOnAction(e -> setCurrentGun(3));
    }

    private void setCurrentGun(int gun) {
        switch (gun) {
            case 1:
                currentGun = 1;
                messageLabel2.setText("Вы используете снаряд!");
                break;
            case 2:
                if (game.countOfSub(game.getCurrentPlayer()) < 3) {
                    currentGun = 2;
                    messageLabel2.setText("Вы используете подводную лодку!");
                } else {
                    messageLabel2.setText("Подводные лодки закончились!");
                }
                break;
            case 3:
                if (game.countOfMineTar(game.getCurrentPlayer()) < 3) {
                    currentGun = 3;
                    messageLabel2.setText("Вы используете минный тральщик!");
                } else {
                    messageLabel2.setText("Минные тральщики закончились!");
                }
                break;
        }
    }

    private void click(int x, int y, int player) {
        if (!start) {
            switch (currentShipLength) {
                case 0:
                    if (game.countOfMines(game.getCurrentPlayer()) < 3) {
                        if (game.placeMine(x, y)) {
                            Button button = (Button) player1Grid.getChildren().get(x * size + y);
                            button.setStyle("-fx-background-color: orange;");
                            messageLabel2.setText("Мина успешно размещена! ");
                            button6.setText("Мин осталось: " + (3 - game.countOfMines(1)));
                            if (game.countOfShipAll(game.getCurrentPlayer()) +
                                    game.countOfMines(game.getCurrentPlayer()) == 13) {
                                startChange();
                                start = true;
                            }
                        } else {
                            messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
                        }
                    } else {
                        messageLabel2.setText("Мин нет!");
                    }
                    break;
                case 1:
                    if (game.countOfShip1(game.getCurrentPlayer()) < 4) {
                        if (game.placeShip(currentShipLength, x, y, horizontal)) {
                            Button button = (Button) player1Grid.getChildren().get(x * size + y);
                            button.setStyle("-fx-background-color: blue;");
                            messageLabel2.setText("Корабль успешно размещен! ");
                            button1.setText("Кораблей длины 1 осталось " + (4 - game.countOfShip1(1)));
                            if (game.countOfShipAll(game.getCurrentPlayer()) +
                                    game.countOfMines(game.getCurrentPlayer()) == 13) {
                                startChange();
                                start = true;
                            }
                        } else {
                            messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
                        }
                    } else {
                        messageLabel2.setText("Корабли длинны 1 закончились!");
                    }
                    break;
                case 2:
                    if (game.countOfShip2(game.getCurrentPlayer()) < 3) {
                        if (game.placeShip(currentShipLength, x, y, horizontal)) {
                            Button button1 = (Button) player1Grid.getChildren().get(x * size + y);
                            button1.setStyle("-fx-background-color: blue;");
                            if (horizontal) {
                                Button button2 = (Button) player1Grid.getChildren().get(x * size + y + 1);
                                button2.setStyle("-fx-background-color: blue;");
                            } else {
                                Button button2 = (Button) player1Grid.getChildren().get(x * size + y + 10);
                                button2.setStyle("-fx-background-color: blue;");
                            }
                            messageLabel2.setText("Корабль успешно размещен! ");
                            button2.setText("Кораблей длины 2 осталось " + (3 - game.countOfShip2(1)));
                            if (game.countOfShipAll(game.getCurrentPlayer()) +
                                    game.countOfMines(game.getCurrentPlayer()) == 13) {
                                startChange();
                                start = true;
                            }
                        } else {
                            messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
                        }
                    } else {
                        messageLabel2.setText("Корабли длинны 2 закончились!");
                    }
                    break;
                case 3:
                    if (game.countOfShip3(game.getCurrentPlayer()) < 2) {
                        if (game.placeShip(currentShipLength, x, y, horizontal)) {
                            Button button1 = (Button) player1Grid.getChildren().get(x * size + y);
                            button1.setStyle("-fx-background-color: blue;");
                            if (horizontal) {
                                Button button2 = (Button) player1Grid.getChildren().get(x * size + y + 1);
                                button2.setStyle("-fx-background-color: blue;");
                                Button button3 = (Button) player1Grid.getChildren().get(x * size + y + 2);
                                button3.setStyle("-fx-background-color: blue;");
                            } else {
                                Button button2 = (Button) player1Grid.getChildren().get(x * size + y + 10);
                                button2.setStyle("-fx-background-color: blue;");
                                Button button3 = (Button) player1Grid.getChildren().get(x * size + y + 20);
                                button3.setStyle("-fx-background-color: blue;");
                            }
                            messageLabel2.setText("Корабль успешно размещен! ");
                            button3.setText("Кораблей длины 3 осталось " + (2 - game.countOfShip3(1)));
                            if (game.countOfShipAll(game.getCurrentPlayer()) +
                                    game.countOfMines(game.getCurrentPlayer()) == 13) {
                                startChange();
                                start = true;
                            }
                        } else {
                            messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
                        }
                    } else {
                        messageLabel2.setText("Корабли длинны 3 закончились!");
                    }
                    break;
                case 4:
                    if (game.countOfShip4(game.getCurrentPlayer()) < 1) {
                        if (game.placeShip(currentShipLength, x, y, horizontal)) {
                            Button button1 = (Button) player1Grid.getChildren().get(x * size + y);
                            button1.setStyle("-fx-background-color: blue;");
                            if (horizontal) {
                                Button button2 = (Button) player1Grid.getChildren().get(x * size + y + 1);
                                button2.setStyle("-fx-background-color: blue;");
                                Button button3 = (Button) player1Grid.getChildren().get(x * size + y + 2);
                                button3.setStyle("-fx-background-color: blue;");
                                Button button4 = (Button) player1Grid.getChildren().get(x * size + y + 3);
                                button4.setStyle("-fx-background-color: blue;");
                            } else {
                                Button button2 = (Button) player1Grid.getChildren().get(x * size + y + 10);
                                button2.setStyle("-fx-background-color: blue;");
                                Button button3 = (Button) player1Grid.getChildren().get(x * size + y + 20);
                                button3.setStyle("-fx-background-color: blue;");
                                Button button4 = (Button) player1Grid.getChildren().get(x * size + y + 30);
                                button4.setStyle("-fx-background-color: blue;");
                            }
                            messageLabel2.setText("Корабль успешно размещен! ");
                            button4.setText("Кораблей длины 4 осталось " + (1 - game.countOfShip4(1)));
                            if (game.countOfShipAll(game.getCurrentPlayer()) +
                                    game.countOfMines(game.getCurrentPlayer()) == 13) {
                                startChange();
                                start = true;
                            }
                        } else {
                            messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
                        }
                    } else {
                        messageLabel2.setText("Корабли длинны 4 закончились!");
                    }
                    break;
            }
        } else {
            switch (currentGun) {
                case 1:
                case 2:
                case 3:
            }
        }
    }

    private void setCurrentShipLength(int length) {
        currentShipLength = length;
        switch (length) {
            case 0:
                if (game.countOfMines(game.getCurrentPlayer()) < 3) {
                    messageLabel2.setText("Выбрана мина");
                    messageLabel1.setText("Мины можно ставить близко к кораблю.");
                } else {
                    messageLabel2.setText("Мин нет!");
                }
                break;
            case 1:
                if (game.countOfShip1(game.getCurrentPlayer()) < 4) {
                    messageLabel2.setText("Выбран корабль длинной " + currentShipLength);
                    messageLabel1.setText("""
                            Корабли выставляются вниз и вправо
                            на выбранную вами длину в зависимости от
                            выбранного вами направления.""");
                } else {
                    messageLabel2.setText("Корабли длинны 1 закончились!");
                }
                break;
            case 2:
                if (game.countOfShip2(game.getCurrentPlayer()) < 3) {
                    messageLabel2.setText("Выбран корабль длинной " + currentShipLength);
                    messageLabel1.setText("""
                            Корабли выставляются вниз и вправо
                            на выбранную вами длину в зависимости от
                            выбранного вами направления.""");
                } else {
                    messageLabel2.setText("Корабли длинны 2 закончились!");
                }
                break;
            case 3:
                if (game.countOfShip3(game.getCurrentPlayer()) < 2) {
                    messageLabel2.setText("Выбран корабль длинной " + currentShipLength);
                    messageLabel1.setText("""
                            Корабли выставляются вниз и вправо
                            на выбранную вами длину в зависимости от
                            выбранного вами направления.""");
                } else {
                    messageLabel2.setText("Корабли длинны 3 закончились!");
                }
            case 4:
                if (game.countOfShip4(game.getCurrentPlayer()) < 1) {
                    messageLabel2.setText("Выбран корабль длинной " + currentShipLength);
                    messageLabel1.setText("""
                            Корабли выставляются вниз и вправо
                            на выбранную вами длину в зависимости от
                            выбранного вами направления.""");
                } else {
                    messageLabel2.setText("Корабли длинны 4 закончились!");
                }
        }
    }

    public void changeHor() {
        if (horizontal) {
            button5.setText("Разместить вертикально");
            horizontal = false;
        } else {
            button5.setText("Разместить горизонтально");
            horizontal = true;
        }
    }


    /*private void handleCellClick(int x, int y, int player) {
        if (game.checkWin()) {
            messageLabel.setText("Game over! Player " + game.getCurrentPlayer() + " wins!");
            return;
        }

        if (game.getCurrentPlayer() == player) {
            if (player == 1) {
                if (game.placeShip(currentShipLength, x, y, true)) {
                    Button button = (Button) player1Grid.getChildren().get(x * size + y);
                    button.setStyle("-fx-background-color: blue;");
                    messageLabel.setText("Player 1 placed a ship of length " + currentShipLength + "! Switch to Player 2.");
                } else {
                    messageLabel.setText("Invalid placement! Try again.");
                }
            } else {
                int attackResult = game.attack(x, y);
                Button button = (Button) player1Grid.getChildren().get(x * size + y);
                switch (attackResult) {
                    case -1:
                        messageLabel.setText("Invalid attack! Try again.");
                        break;
                    case 0:
                        button.setStyle("-fx-background-color: lightgray;");
                        messageLabel.setText("Player 2 missed! Switch to Player 1.");
                        break;
                    case 1:
                        button.setStyle("-fx-background-color: red;");
                        messageLabel.setText("Player 2 hit a ship! Switch to Player 1.");
                        break;
                    case 2:
                        button.setStyle("-fx-background-color: darkred;");
                        messageLabel.setText("Player 2 sunk a ship! Switch to Player 1.");
                        break;
                }
            }
            game.switchPlayer();
        } else {
            messageLabel.setText("It's not your turn!");
        }*/
}