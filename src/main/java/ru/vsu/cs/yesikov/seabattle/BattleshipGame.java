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

import static ru.vsu.cs.yesikov.seabattle.Ships.*;

public class BattleshipGame extends Application {

    private Game game;
    private GridPane player1Grid;
    private GridPane player1GridForAttack;
    private GridPane player2Grid;
    private GridPane player2GridForAttack;
    private Label messageLabel2;
    private Label messageLabel1;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button buttonChangePlayer;
    private VBox buttonsVBox;
    private VBox playerVBox;
    private VBox playerVBoxForAttack;
    private final int size = 10;
    private int currentGun = 1;
    private Ships currentShip = SHIP4;
    private boolean start = false;
    private boolean horizontal = true;
    private final int countOfMine = 3;
    private final int countOfMineSweeper = 1;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        game = new Game(countOfMine, countOfMineSweeper);
        primaryStage.setTitle("Battleship Game");
        VBox root = new VBox(10);
        root.setPrefSize(500, 500);
        root.setAlignment(Pos.CENTER);

        player1Grid = createGridPane();
        player1GridForAttack = createGridPaneForAttack();
        player2Grid = createGridPane();
        player2GridForAttack = createGridPaneForAttack();

        messageLabel1 = new Label("""
                Корабли выставляются вниз и вправо
                на выбранную вами длину в зависимости от
                выбранного вами направления.""");
        messageLabel2 = new Label("Игрок 1 расставляет корабли! Изначальная длина 4!");

        buttonChangePlayer = new Button("Завершить расстановку. Игрок 1.");
        buttonChangePlayer.setPrefSize(300, 25);
        buttonChangePlayer.setOnAction(e -> startChange());
        messageLabel1.setPrefSize(300, 60);
        messageLabel2.setPrefSize(300, 30);
        Label lab = new Label("<----------------------------------------------------->");
        button5 = new Button("Разместить горизонтально");
        button5.setPrefSize(300, 25);
        button5.setOnAction(e -> changeHor());

        button6 = new Button("Мин осталось " + (countOfMine - game.countOfMines(1)));
        button6.setPrefSize(300, 25);
        button6.setOnAction(e -> setCurrentShip(MINE));

        button7 = new Button("Минных тральщиков осталось  " + (countOfMineSweeper - game.countOfMineSweeper(1)));
        button7.setPrefSize(300, 25);
        button7.setOnAction(e -> setCurrentShip(SWEEPER));

        button4 = new Button("Кораблей длины 4 осталось " + (1 - game.countOfShip4(1)));
        button4.setPrefSize(300, 25);
        button4.setOnAction(e -> setCurrentShip(SHIP4));

        button3 = new Button("Кораблей длины 3 осталось " + (2 - game.countOfShip3(1)));
        button3.setPrefSize(300, 25);
        button3.setOnAction(e -> setCurrentShip(SHIP3));

        button2 = new Button("Кораблей длины 2 осталось " + (3 - game.countOfShip2(1)));
        button2.setPrefSize(300, 25);
        button2.setOnAction(e -> setCurrentShip(SHIP2));

        button1 = new Button("Кораблей длины 1 осталось " + (4 - game.countOfShip1(1)));
        button1.setPrefSize(300, 25);
        button1.setOnAction(e -> setCurrentShip(SHIP1));

        HBox hbox = new HBox(20);
        messageLabel1.setAlignment(Pos.CENTER);
        messageLabel2.setAlignment(Pos.CENTER);
        playerVBox = new VBox(new Label("Ваше поле"), player1Grid);
        playerVBoxForAttack = new VBox(new Label("Поле противника"), player1GridForAttack);
        buttonsVBox = new VBox(button5, button6, button7, button4, button3, button2, button1, messageLabel2, lab, messageLabel1);

        buttonsVBox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(playerVBox, buttonsVBox, playerVBoxForAttack);
        hbox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(hbox);

        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void changePlayer() {
        switch (game.getCurrentPlayer()) {
            case 1:
                if (buttonChangePlayer.getText().equals("Завершить расстановку. Игрок 1.")) {
                    playerVBox.getChildren().clear();
                    playerVBoxForAttack.getChildren().clear();
                    buttonChangePlayer.setText("Начать расстановку. Игрок 2.");
                    break;
                }
                game.switchPlayer();
                playerVBox.getChildren().addAll(new Label("Ваше поле"), player2Grid);
                playerVBoxForAttack.getChildren().addAll(new Label("Поле противника"), player2GridForAttack);
                buttonChangePlayer.setText("Завершить расстановку. Игрок 2.");
                break;
            case 2:
                if (buttonChangePlayer.getText().equals("Завершить расстановку. Игрок 2.")) {
                    playerVBox.getChildren().clear();
                    playerVBoxForAttack.getChildren().clear();
                    buttonChangePlayer.setText("Сделать ход. Игрок 1.");
                    break;
                }
                game.switchPlayer();
                playerVBox.getChildren().addAll(new Label("Ваше поле"), player1Grid);
                playerVBoxForAttack.getChildren().addAll(new Label("Поле противника"), player1GridForAttack);
                buttonChangePlayer.setText("Завершить ход. Игрок 1.");
                break;
        }
    }

    private GridPane createGridPaneForAttack() {
        GridPane grid = new GridPane();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button cellButton = new Button();
                cellButton.setMinSize(30, 30);
                final int x = i;
                final int y = j;
                cellButton.setOnAction(e -> clickAttack(x, y));
                grid.add(cellButton, j, i);
            }
        }
        return grid;
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Button cellButton = new Button();
                cellButton.setMinSize(30, 30);
                final int x = i;
                final int y = j;
                cellButton.setOnAction(e -> clickForStart(x, y));
                grid.add(cellButton, j, i);
            }
        }
        return grid;
    }

    private void clickAttack(int x, int y) {
        return;
    }

    private void startChange() {
        if (game.getCurrentPlayer() == 1 && !start) {
            buttonsVBox.getChildren().clear();
            buttonsVBox.getChildren().add(buttonChangePlayer);
            buttonChangePlayer.setText("Завершить расстановку игрок 1.");
            start = true;
        } else if (game.getCurrentPlayer() == 1) {
            game.switchPlayer();
            playerVBox.getChildren().clear();
            playerVBoxForAttack.getChildren().clear();
            buttonChangePlayer.setText("Начать расстановку игрок 1.");
            start = false;
        } else if (game.getCurrentPlayer() == 2 && !start) {
            buttonsVBox.getChildren().clear();
            start = false;
            buttonChangePlayer.setText("Завершить расстановку игрок 2.");
            messageLabel1.setText("""
                    Корабли выставляются вниз и вправо
                    на выбранную вами длину в зависимости от
                    выбранного вами направления.""");
            messageLabel2.setText("Игрок 2 расставляет корабли! Изначальная длина 4!");
            buttonChangePlayer.setText("Завершить расстановку. Игрок 2.");
            Label lab = new Label("<----------------------------------------------------->");
            button5.setText("Разместить горизонтально");
            button6.setText("Мин осталось " + (countOfMine - game.countOfMines(2)));
            button7.setText("Минных тральщиков осталось  " + (countOfMineSweeper - game.countOfMineSweeper(2)));
            button4.setText("Кораблей длины 4 осталось " + (1 - game.countOfShip4(2)));
            button3.setText("Кораблей длины 3 осталось " + (2 - game.countOfShip3(2)));
            button2.setText("Кораблей длины 2 осталось " + (3 - game.countOfShip2(2)));
            button1.setText("Кораблей длины 1 осталось " + (4 - game.countOfShip1(2)));
            playerVBox.getChildren().addAll(new Label("Ваше поле"), player2Grid);
            playerVBoxForAttack.getChildren().addAll(new Label("Поле противника"), player2GridForAttack);
            buttonsVBox.getChildren().addAll(button5, button6, button7, button4, button3, button2, button1, messageLabel2, lab, messageLabel1);
        } else {
            buttonsVBox.getChildren().clear();
            buttonsVBox.getChildren().add(buttonChangePlayer);
            buttonChangePlayer.setText("Завершить расстановку игрок 2.");
            button1.setOnAction(e -> changePlayerForAttack());
            start = true;
        }
    }

    private void changePlayerForAttack() {
    }

    private void setCurrentGun(int gun) {
        switch (gun) {
            case 1:
                currentGun = 1;
                messageLabel2.setText("Вы используете снаряд!");
                break;
            case 2:
                if (game.countOfMineSweeper(game.getCurrentPlayer()) < 3) {
                    currentGun = 3;
                    messageLabel2.setText("Вы используете минный тральщик!");
                } else {
                    messageLabel2.setText("Минные тральщики закончились!");
                }
                break;
        }
    }

    private void clickForStart(int x, int y) {
        if (!start) {
            switch (game.getCurrentPlayer()) {
                case 1:
                    switch (currentShip) {
                        case MINE -> placeMine(player1Grid, 1, x, y);
                        case SHIP1 -> placeShip1(player1Grid, 1, x, y);
                        case SHIP2 -> placeShip2(player1Grid, 1, x, y);
                        case SHIP3 -> placeShip3(player1Grid, 1, x, y);
                        case SHIP4 -> placeShip4(player1Grid, 1, x, y);
                        case SWEEPER -> placeMineSweeper(player1Grid, 1, x, y);
                    }
                    break;
                case 2:
                    switch (currentShip) {
                        case MINE -> placeMine(player2Grid, 2, x, y);
                        case SHIP1 -> placeShip1(player2Grid, 2, x, y);
                        case SHIP2 -> placeShip2(player2Grid, 2, x, y);
                        case SHIP3 -> placeShip3(player2Grid, 2, x, y);
                        case SHIP4 -> placeShip4(player2Grid, 2, x, y);
                        case SWEEPER -> placeMineSweeper(player2Grid, 2, x, y);
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

    private void placeMineSweeper(GridPane playerGrid, int currentPlayer, int x, int y) {
        if (game.countOfMineSweeper(currentPlayer) < countOfMineSweeper) {
            if (game.placeMineSweeper(x, y)) {
                Button button = (Button) playerGrid.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: green;");
                messageLabel2.setText("Минный тральщик успешно размещен! ");
                button7.setText("Минных тральщиков осталось: " + (countOfMineSweeper - game.countOfMineSweeper(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == 10 + countOfMineSweeper +
                        countOfMine) {
                    if (currentPlayer == 2) {
                        start = true;
                    }
                    startChange();
                }
            } else {
                messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
            }
        } else {
            messageLabel2.setText("Минных тральщиков нет!");
        }
    }

    private void placeMine(GridPane playerGrid, int currentPlayer, int x, int y) {
        if (game.countOfMines(currentPlayer) < countOfMine) {
            if (game.placeMine(x, y)) {
                Button button = (Button) playerGrid.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: orange;");
                messageLabel2.setText("Мина успешно размещена! ");
                button6.setText("Мин осталось: " + (countOfMine - game.countOfMines(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == 10 + countOfMineSweeper +
                        countOfMine) {
                    if (currentPlayer == 2) {
                        start = true;
                    }
                    startChange();
                }
            } else {
                messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
            }
        } else {
            messageLabel2.setText("Мин нет!");
        }
    }

    private void placeShip1(GridPane playerGrid, int currentPlayer, int x, int y) {
        if (game.countOfShip1(currentPlayer) < 4) {
            if (game.placeShip(1, x, y, horizontal)) {
                Button button = (Button) playerGrid.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: blue;");
                messageLabel2.setText("Корабль успешно размещен! ");
                button1.setText("Кораблей длины 1 осталось " + (4 - game.countOfShip1(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == 10 + countOfMineSweeper +
                        countOfMine) {
                    if (currentPlayer == 2) {
                        start = true;
                    }
                    startChange();
                }
            } else {
                messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
            }
        } else {
            messageLabel2.setText("Корабли длинны 1 закончились!");
        }
    }

    private void placeShip2(GridPane playerGrid, int currentPlayer, int x, int y) {
        if (game.countOfShip2(currentPlayer) < 3) {
            if (game.placeShip(2, x, y, horizontal)) {
                Button button1 = (Button) playerGrid.getChildren().get(x * size + y);
                button1.setStyle("-fx-background-color: blue;");
                if (horizontal) {
                    Button button2 = (Button) playerGrid.getChildren().get(x * size + y + 1);
                    button2.setStyle("-fx-background-color: blue;");
                } else {
                    Button button2 = (Button) playerGrid.getChildren().get(x * size + y + 10);
                    button2.setStyle("-fx-background-color: blue;");
                }
                messageLabel2.setText("Корабль успешно размещен! ");
                button2.setText("Кораблей длины 2 осталось " + (3 - game.countOfShip2(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == 10 + countOfMineSweeper +
                        countOfMine) {
                    if (currentPlayer == 2) {
                        start = true;
                    }
                    startChange();
                }
            } else {
                messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
            }
        } else {
            messageLabel2.setText("Корабли длинны 2 закончились!");
        }
    }

    private void placeShip3(GridPane playerGrid, int currentPlayer, int x, int y) {
        if (game.countOfShip3(currentPlayer) < 2) {
            if (game.placeShip(3, x, y, horizontal)) {
                Button button1 = (Button) playerGrid.getChildren().get(x * size + y);
                button1.setStyle("-fx-background-color: blue;");
                if (horizontal) {
                    Button button2 = (Button) playerGrid.getChildren().get(x * size + y + 1);
                    button2.setStyle("-fx-background-color: blue;");
                    Button button3 = (Button) playerGrid.getChildren().get(x * size + y + 2);
                    button3.setStyle("-fx-background-color: blue;");
                } else {
                    Button button2 = (Button) playerGrid.getChildren().get(x * size + y + 10);
                    button2.setStyle("-fx-background-color: blue;");
                    Button button3 = (Button) playerGrid.getChildren().get(x * size + y + 20);
                    button3.setStyle("-fx-background-color: blue;");
                }
                messageLabel2.setText("Корабль успешно размещен! ");
                button3.setText("Кораблей длины 3 осталось " + (2 - game.countOfShip3(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == 10 + countOfMineSweeper +
                        countOfMine) {
                    if (currentPlayer == 2) {
                        start = true;
                    }
                    startChange();
                }
            } else {
                messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
            }
        } else {
            messageLabel2.setText("Корабли длинны 3 закончились!");
        }
    }

    private void placeShip4(GridPane playerGrid, int currentPlayer, int x, int y) {
        if (game.countOfShip4(currentPlayer) < 1) {
            if (game.placeShip(4, x, y, horizontal)) {
                Button button1 = (Button) playerGrid.getChildren().get(x * size + y);
                button1.setStyle("-fx-background-color: blue;");
                if (horizontal) {
                    Button button2 = (Button) playerGrid.getChildren().get(x * size + y + 1);
                    button2.setStyle("-fx-background-color: blue;");
                    Button button3 = (Button) playerGrid.getChildren().get(x * size + y + 2);
                    button3.setStyle("-fx-background-color: blue;");
                    Button button4 = (Button) playerGrid.getChildren().get(x * size + y + 3);
                    button4.setStyle("-fx-background-color: blue;");
                } else {
                    Button button2 = (Button) playerGrid.getChildren().get(x * size + y + 10);
                    button2.setStyle("-fx-background-color: blue;");
                    Button button3 = (Button) playerGrid.getChildren().get(x * size + y + 20);
                    button3.setStyle("-fx-background-color: blue;");
                    Button button4 = (Button) playerGrid.getChildren().get(x * size + y + 30);
                    button4.setStyle("-fx-background-color: blue;");
                }
                messageLabel2.setText("Корабль успешно размещен! ");
                button4.setText("Кораблей длины 4 осталось " + (1 - game.countOfShip4(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == 10 + countOfMineSweeper +
                        countOfMine) {
                    if (currentPlayer == 2) {
                        start = true;
                    }
                    startChange();
                }
            } else {
                messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
            }
        } else {
            messageLabel2.setText("Корабли длинны 4 закончились!");
        }
    }

    private void setCurrentShip(Ships ship) {
        currentShip = ship;
        switch (ship) {
            case SWEEPER:
                if (game.countOfMineSweeper(game.getCurrentPlayer()) < countOfMineSweeper) {
                    messageLabel2.setText("Выбран минный тральщик.");
                    messageLabel1.setText("""
                            Ставятся на таком же расстоянии как и корабли.
                            При попадании в минный тральщик у противника
                            разминнируется одна из мин.
                            """);
                } else {
                    messageLabel2.setText("Минных тральщиков нет!");
                }
                break;
            case MINE:
                if (game.countOfMines(game.getCurrentPlayer()) < countOfMine) {
                    messageLabel2.setText("Выбрана мина.");
                    messageLabel1.setText("""
                            Ставятся на таком же расстоянии как и корабли.
                            При попадании в мину у противнику наносится
                            урон по одному из кораблей.
                            """);
                } else {
                    messageLabel2.setText("Мин нет!");
                }
                break;
            case SHIP1:
                if (game.countOfShip1(game.getCurrentPlayer()) < 4) {
                    messageLabel2.setText("Выбран корабль длинной 1.");
                    messageLabel1.setText("""
                            Корабли выставляются вниз и вправо
                            на выбранную вами длину в зависимости от
                            выбранного вами направления.""");
                } else {
                    messageLabel2.setText("Корабли длинны 1 закончились!");
                }
                break;
            case SHIP2:
                if (game.countOfShip2(game.getCurrentPlayer()) < 3) {
                    messageLabel2.setText("Выбран корабль длинной 2.");
                    messageLabel1.setText("""
                            Корабли выставляются вниз и вправо
                            на выбранную вами длину в зависимости от
                            выбранного вами направления.""");
                } else {
                    messageLabel2.setText("Корабли длинны 2 закончились!");
                }
                break;
            case SHIP3:
                if (game.countOfShip3(game.getCurrentPlayer()) < 2) {
                    messageLabel2.setText("Выбран корабль длинной 3.");
                    messageLabel1.setText("""
                            Корабли выставляются вниз и вправо
                            на выбранную вами длину в зависимости от
                            выбранного вами направления.""");
                } else {
                    messageLabel2.setText("Корабли длинны 3 закончились!");
                }
            case SHIP4:
                if (game.countOfShip4(game.getCurrentPlayer()) < 1) {
                    messageLabel2.setText("Выбран корабль длинной 4.");
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