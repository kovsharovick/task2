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
    private Ships currentShip = SHIP4;
    private boolean start = false;
    private boolean attack = false;
    private boolean horizontal = true;
    private final int countOfMine = 2;
    private final int countOfMineSweeper = 1;
    private final int countOfShip1 = 4;
    private final int countOfShip2 = 3;
    private final int countOfShip3 = 2;
    private final int countOfShip4 = 1;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        game = new Game(size);
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

        button4 = new Button("Кораблей длины 4 осталось " + (countOfShip4 - game.countOfShip4(1)));
        button4.setPrefSize(300, 25);
        button4.setOnAction(e -> setCurrentShip(SHIP4));

        button3 = new Button("Кораблей длины 3 осталось " + (countOfShip3 - game.countOfShip3(1)));
        button3.setPrefSize(300, 25);
        button3.setOnAction(e -> setCurrentShip(SHIP3));

        button2 = new Button("Кораблей длины 2 осталось " + (countOfShip2 - game.countOfShip2(1)));
        button2.setPrefSize(300, 25);
        button2.setOnAction(e -> setCurrentShip(SHIP2));

        button1 = new Button("Кораблей длины 1 осталось " + (countOfShip1 - game.countOfShip1(1)));
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
        if (attack) {
            switch (game.getCurrentPlayer()) {
                case 1 -> attack(player1GridForAttack, player2Grid, x, y);
                case 2 -> attack(player2GridForAttack, player1Grid, x, y);
            }
        } else {
            messageLabel2.setText("Вы не можете так походить!");
        }
    }

    private void attack(GridPane gridForAttack, GridPane gridNextPlayer, int x, int y) {
        Button button;
        int[] redArea;
        int maxX, maxY, minX, minY;
        switch (game.attack(x, y)) {
            case UNREAL:
                messageLabel2.setText("Вы не можете так походить!");
                break;
            case NOTHING:
                button = (Button) gridForAttack.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: red;");
                button = (Button) gridNextPlayer.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: red;");
                messageLabel2.setText("Промах! ");
                attack = false;
                game.switchPlayer();
                buttonChangePlayer.setText("Завершить ход. Игрок 1.");
                buttonsVBox.getChildren().add(buttonChangePlayer);
                break;
            case KILL:
                redArea = game.getKillArea(game.getCurrentPlayer());
                int[] killArea = game.getKillShip();
                minX = Math.min(redArea[0], redArea[1]);
                maxX = Math.max(redArea[0], redArea[1]);
                minY = Math.min(redArea[2], redArea[3]);
                maxY = Math.max(redArea[2], redArea[3]);
                for (int xi = minX; xi <= maxX; xi++) {
                    for (int yi = minY; yi <= maxY; yi++) {
                        button = (Button) gridForAttack.getChildren().get((xi) * size + yi);
                        button.setStyle("-fx-background-color: red;");
                        button = (Button) gridNextPlayer.getChildren().get((xi) * size + yi);
                        button.setStyle("-fx-background-color: red;");
                    }
                }
                for (int j : killArea) {
                    button = (Button) gridForAttack.getChildren().get(j);
                    button.setStyle("-fx-background-color: black;");
                    button = (Button) gridNextPlayer.getChildren().get(j);
                    button.setStyle("-fx-background-color: black;");
                }
                if (game.checkWin()) {
                    playerVBox.getChildren().clear();
                    playerVBoxForAttack.getChildren().clear();
                    buttonsVBox.getChildren().clear();
                    messageLabel2.setText("Игрок " + game.getCurrentPlayer() + " победил!!!");
                    buttonsVBox.getChildren().add(messageLabel2);
                    playerVBox.getChildren().addAll(new Label("Поле игрока 1"), player1Grid);
                    playerVBoxForAttack.getChildren().addAll(new Label("Поле игрока 2"), player2Grid);
                    break;
                }
                messageLabel2.setText("Убил! Делайте ход снова!");
                break;
            case HIT:
                button = (Button) gridForAttack.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: black;");
                button = (Button) gridNextPlayer.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: black;");
                messageLabel2.setText("Ранил! Делайте ход снова!");
                break;
            case MINE:
                redArea = game.getKillArea(game.getCurrentPlayer());
                minX = Math.min(redArea[0], redArea[1]);
                maxX = Math.max(redArea[0], redArea[1]);
                minY = Math.min(redArea[2], redArea[3]);
                maxY = Math.max(redArea[2], redArea[3]);
                for (int xi = minX; xi <= maxX; xi++) {
                    for (int yi = minY; yi <= maxY; yi++) {
                        button = (Button) gridForAttack.getChildren().get((xi) * size + yi);
                        button.setStyle("-fx-background-color: red;");
                        button = (Button) gridNextPlayer.getChildren().get((xi) * size + yi);
                        button.setStyle("-fx-background-color: red;");
                    }
                }
                button = (Button) gridForAttack.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: orange;");
                button = (Button) gridNextPlayer.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: brown;");
                messageLabel2.setText("Вы попали в мину!");
                messageLabel1.setText("""
                        Вы должны выбрать корабль, по которому
                        нанесется удар. Нажмите на него для этого!
                        """);
                currentShip = DELSHIP;
                attack = false;
                start = false;
                break;
            case SWEEPER:
                redArea = game.getKillArea(game.getCurrentPlayer());
                minX = Math.min(redArea[0], redArea[1]);
                maxX = Math.max(redArea[0], redArea[1]);
                minY = Math.min(redArea[2], redArea[3]);
                maxY = Math.max(redArea[2], redArea[3]);
                for (int xi = minX; xi <= maxX; xi++) {
                    for (int yi = minY; yi <= maxY; yi++) {
                        button = (Button) gridForAttack.getChildren().get((xi) * size + yi);
                        button.setStyle("-fx-background-color: red;");
                        button = (Button) gridNextPlayer.getChildren().get((xi) * size + yi);
                        button.setStyle("-fx-background-color: red;");
                    }
                }
                button = (Button) gridForAttack.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: green;");
                button = (Button) gridNextPlayer.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: brown;");
                messageLabel2.setText("Вы попали в минный тральщик!");
                messageLabel1.setText("""
                        Вы должны выбрать любую мину на своем
                        поле, чтобы разминировать её.
                        Нажмите на неё для этого!
                        """);
                currentShip = DELMINE;
                attack = false;
                start = false;
                break;
        }
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
            buttonChangePlayer.setText("Начать расстановку игрок 2.");
            start = false;
        } else if (game.getCurrentPlayer() == 2 && !start) {
            buttonsVBox.getChildren().clear();
            buttonChangePlayer.setText("Завершить расстановку игрок 2.");
            messageLabel1.setText("""
                    Корабли выставляются вниз и вправо
                    на выбранную вами длину в зависимости от
                    выбранного вами направления.""");
            messageLabel2.setText("Игрок 2 расставляет корабли! Изначальная длина 4!");
            Label lab = new Label("<----------------------------------------------------->");
            button5.setText("Разместить горизонтально");
            button6.setText("Мин осталось " + (countOfMine - game.countOfMines(2)));
            button7.setText("Минных тральщиков осталось  " + (countOfMineSweeper - game.countOfMineSweeper(2)));
            button4.setText("Кораблей длины 4 осталось " + (countOfShip4 - game.countOfShip4(2)));
            button3.setText("Кораблей длины 3 осталось " + (countOfShip3 - game.countOfShip3(2)));
            button2.setText("Кораблей длины 2 осталось " + (countOfShip2 - game.countOfShip2(2)));
            button1.setText("Кораблей длины 1 осталось " + (countOfShip1 - game.countOfShip1(2)));
            playerVBox.getChildren().addAll(new Label("Ваше поле"), player2Grid);
            playerVBoxForAttack.getChildren().addAll(new Label("Поле противника"), player2GridForAttack);
            buttonsVBox.getChildren().addAll(button5, button6, button7, button4, button3, button2, button1, messageLabel2, lab, messageLabel1);
        } else {
            buttonsVBox.getChildren().clear();
            buttonsVBox.getChildren().add(buttonChangePlayer);
            buttonChangePlayer.setText("Завершить расстановку игрок 2.");
            buttonChangePlayer.setOnAction(e -> changePlayerForAttack());
            start = true;
            game.switchPlayer();
        }
    }

    private void changePlayerForAttack() {
        if (!attack && game.getCurrentPlayer() == 1) {
            playerVBox.getChildren().clear();
            playerVBoxForAttack.getChildren().clear();
            buttonsVBox.getChildren().clear();
            buttonsVBox.getChildren().add(buttonChangePlayer);
            buttonChangePlayer.setText("Сделать ход. Игрок 1.");
            attack = true;
        } else if (game.getCurrentPlayer() == 1) {
            buttonsVBox.getChildren().clear();
            messageLabel1.setText("""
                    Чтобы нанести удар, нажмите на клеточку
                    на поле противника, где по вашему мнению
                    расположены его корабли.""");
            messageLabel2.setText("Игрок 1 ходит!");
            Label lab = new Label("<----------------------------------------------------->");
            playerVBox.getChildren().addAll(new Label("Ваше поле"), player1Grid);
            playerVBoxForAttack.getChildren().addAll(new Label("Поле противника"), player1GridForAttack);
            buttonsVBox.getChildren().addAll(messageLabel2, lab, messageLabel1);
        } else if (!attack && game.getCurrentPlayer() == 2) {
            playerVBox.getChildren().clear();
            playerVBoxForAttack.getChildren().clear();
            buttonsVBox.getChildren().clear();
            buttonsVBox.getChildren().add(buttonChangePlayer);
            buttonChangePlayer.setText("Сделать ход. Игрок 2.");
            attack = true;
        } else {
            buttonsVBox.getChildren().clear();
            messageLabel1.setText("""
                    Чтобы нанести удар, нажмите на клеточку
                    на поле противника, где по вашему мнению
                    расположены его корабли.""");
            messageLabel2.setText("Игрок 2 ходит!");
            Label lab = new Label("<----------------------------------------------------->");
            playerVBox.getChildren().addAll(new Label("Ваше поле"), player2Grid);
            playerVBoxForAttack.getChildren().addAll(new Label("Поле противника"), player2GridForAttack);
            buttonsVBox.getChildren().addAll(messageLabel2, lab, messageLabel1);
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
                        case DELMINE -> deleteMine(player1Grid, player2GridForAttack, x, y);
                        case DELSHIP -> deleteShip(player1Grid, player2GridForAttack, x, y);
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
                        case DELMINE -> deleteMine(player2Grid, player1GridForAttack, x, y);
                        case DELSHIP -> deleteShip(player2Grid, player1GridForAttack, x, y);
                    }
                    break;
            }
        } else {
            messageLabel2.setText("Вы не можете так походить!");
        }
    }

    private void deleteMine(GridPane playerGrid, GridPane playerGridForAttack, int x, int y) {
        if (game.countOfMines(game.getCurrentPlayer() == 1 ? 2 : 1) != 0 && game.deleteMine(x, y)) {
            int[] redArea = game.getKillArea(game.getCurrentPlayer() == 2 ? 1 : 2);
            int minX = Math.min(redArea[0], redArea[1]);
            int maxX = Math.max(redArea[0], redArea[1]);
            int minY = Math.min(redArea[2], redArea[3]);
            int maxY = Math.max(redArea[2], redArea[3]);
            for (int xi = minX; xi <= maxX; xi++) {
                for (int yi = minY; yi <= maxY; yi++) {
                    Button button = (Button) playerGrid.getChildren().get((xi) * size + yi);
                    button.setStyle("-fx-background-color: red;");
                    button = (Button) playerGridForAttack.getChildren().get((xi) * size + yi);
                    button.setStyle("-fx-background-color: red;");
                }
            }
            Button button = (Button) playerGrid.getChildren().get(x * size + y);
            button.setStyle("-fx-background-color: brown;");
            button = (Button) playerGridForAttack.getChildren().get(x * size + y);
            button.setStyle("-fx-background-color: orange;");
            messageLabel2.setText("Мина успешно разминирована! ");
            buttonsVBox.getChildren().clear();
            buttonChangePlayer.setText("Завершить ход. Игрок " + game.getCurrentPlayer() + ".");
            buttonsVBox.getChildren().add(buttonChangePlayer);
            game.switchPlayer();
            start = true;
            attack = false;
        } else if (game.countOfMines(game.getCurrentPlayer() == 1 ? 2 : 1) == 0) {
            messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
        } else {
            messageLabel2.setText("Разминировать больше нечего! ");
            buttonsVBox.getChildren().clear();
            buttonChangePlayer.setText("Завершить ход. Игрок " + game.getCurrentPlayer() + ".");
            buttonsVBox.getChildren().add(buttonChangePlayer);
            game.switchPlayer();
            start = true;
            attack = false;
        }
    }

    private void deleteShip(GridPane playerGrid, GridPane playerGridForAttack, int x, int y) {
        Button button;
        switch (game.deleteShip(x, y, game.getCurrentPlayer() == 1 ? 2 : 1)) {
            case HIT:
                button = (Button) playerGrid.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: black;");
                button = (Button) playerGridForAttack.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: black;");
                messageLabel2.setText("Урон успешно нанесен! ");
                buttonsVBox.getChildren().clear();
                buttonChangePlayer.setText("Завершить ход. Игрок " + game.getCurrentPlayer() + ".");
                buttonsVBox.getChildren().add(buttonChangePlayer);
                buttonsVBox.getChildren().remove(messageLabel1);
                game.switchPlayer();
                start = true;
                attack = false;
                break;
            case KILL:
                int[] redArea = game.getKillArea(game.getCurrentPlayer() == 2 ? 1 : 2);
                int minX = Math.min(redArea[0], redArea[1]);
                int maxX = Math.max(redArea[0], redArea[1]);
                int minY = Math.min(redArea[2], redArea[3]);
                int maxY = Math.max(redArea[2], redArea[3]);
                for (int xi = minX; xi <= maxX; xi++) {
                    for (int yi = minY; yi <= maxY; yi++) {
                        button = (Button) playerGrid.getChildren().get((xi) * size + yi);
                        button.setStyle("-fx-background-color: red;");
                        button = (Button) playerGridForAttack.getChildren().get((xi) * size + yi);
                        button.setStyle("-fx-background-color: red;");
                    }
                }
                button = (Button) playerGrid.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: black;");
                button = (Button) playerGridForAttack.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: black;");
                if (game.checkWin()) {
                    game.switchPlayer();
                    playerVBox.getChildren().clear();
                    playerVBoxForAttack.getChildren().clear();
                    buttonsVBox.getChildren().clear();
                    messageLabel2.setText("Игрок " + game.getCurrentPlayer() + " победил!!!");
                    buttonsVBox.getChildren().add(messageLabel2);
                    playerVBox.getChildren().addAll(new Label("Поле игрока 1"), player1Grid);
                    playerVBoxForAttack.getChildren().addAll(new Label("Поле игрока 2"), player2Grid);
                    break;
                }
                messageLabel2.setText("Урон успешно нанесен! ");
                buttonsVBox.getChildren().clear();
                buttonChangePlayer.setText("Завершить ход. Игрок " + game.getCurrentPlayer() + ".");
                buttonsVBox.getChildren().add(buttonChangePlayer);
                buttonsVBox.getChildren().remove(messageLabel1);
                game.switchPlayer();
                start = true;
                attack = false;
                break;
            default:
                messageLabel2.setText("Недоступные координаты попробуй ещё раз.");
                break;
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
                        game.countOfMineSweeper(currentPlayer) == countOfShip1 + countOfMineSweeper +
                        countOfMine + countOfShip2 + countOfShip3 + countOfShip4) {
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
                        game.countOfMineSweeper(currentPlayer) == countOfShip1 + countOfMineSweeper +
                        countOfMine + countOfShip2 + countOfShip3 + countOfShip4) {
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
        if (game.countOfShip1(currentPlayer) < countOfShip1) {
            if (game.placeShip(1, x, y, horizontal)) {
                Button button = (Button) playerGrid.getChildren().get(x * size + y);
                button.setStyle("-fx-background-color: blue;");
                messageLabel2.setText("Корабль успешно размещен! ");
                button1.setText("Кораблей длины 1 осталось " + (countOfShip1 - game.countOfShip1(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == countOfShip1 + countOfMineSweeper +
                        countOfMine + countOfShip2 + countOfShip3 + countOfShip4) {
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
        if (game.countOfShip2(currentPlayer) < countOfShip2) {
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
                button2.setText("Кораблей длины 2 осталось " + (countOfShip2 - game.countOfShip2(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == countOfShip1 + countOfMineSweeper +
                        countOfMine + countOfShip2 + countOfShip3 + countOfShip4) {
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
        if (game.countOfShip3(currentPlayer) < countOfShip3) {
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
                button3.setText("Кораблей длины 3 осталось " + (countOfShip3 - game.countOfShip3(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == countOfShip1 + countOfMineSweeper +
                        countOfMine + countOfShip2 + countOfShip3 + countOfShip4) {
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
        if (game.countOfShip4(currentPlayer) < countOfShip4) {
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
                button4.setText("Кораблей длины 4 осталось " + (countOfShip4 - game.countOfShip4(currentPlayer)));
                if (game.countOfShipAll(currentPlayer) + game.countOfMines(currentPlayer) +
                        game.countOfMineSweeper(currentPlayer) == countOfShip1 + countOfMineSweeper +
                        countOfMine + countOfShip2 + countOfShip3 + countOfShip4) {
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
                if (game.countOfShip1(game.getCurrentPlayer()) < countOfShip1) {
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
                if (game.countOfShip2(game.getCurrentPlayer()) < countOfShip2) {
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
                if (game.countOfShip3(game.getCurrentPlayer()) < countOfShip3) {
                    messageLabel2.setText("Выбран корабль длинной 3.");
                    messageLabel1.setText("""
                            Корабли выставляются вниз и вправо
                            на выбранную вами длину в зависимости от
                            выбранного вами направления.""");
                } else {
                    messageLabel2.setText("Корабли длинны 3 закончились!");
                }
            case SHIP4:
                if (game.countOfShip4(game.getCurrentPlayer()) < countOfShip4) {
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

}