package ru.vsu.cs.yesikov.seabattle;

import java.util.Scanner;

public class Game {
    private final Field player1Field;
    private final Field player2Field;
    private int currentPlayer;

    public Game(int countMines, int countSub, int countMinTar) {
        player1Field = new Field(countMines, countSub, countMinTar);
        player2Field = new Field(countMines, countSub, countMinTar);
        currentPlayer = 1;
    }

    public int countOfMines(int player) {
        if (player == 1) {
            return player1Field.getCountMines();
        }
        return player2Field.getCountMines();
    }

    public int countOfShipAll(int player) {
        if (player == 1) {
            return player1Field.getCountShip();
        }
        return player2Field.getCountShip();
    }

    public int countOfShip1(int player) {
        if (player == 1) {
            return player1Field.getCountShip1();
        }
        return player2Field.getCountShip1();
    }

    public int countOfShip2(int player) {
        if (player == 1) {
            return player1Field.getCountShip2();
        }
        return player2Field.getCountShip2();
    }

    public int countOfShip3(int player) {
        if (player == 1) {
            return player1Field.getCountShip3();
        }
        return player2Field.getCountShip3();
    }

    public int countOfShip4(int player) {
        if (player == 1) {
            return player1Field.getCountShip4();
        }
        return player2Field.getCountShip4();
    }

    public int countOfMineTar(int player) {
        if (player == 1) {
            return player1Field.getCountMinTar();
        }
        return player2Field.getCountMinTar();
    }

    public int countOfSub(int player) {
        if (player == 1) {
            return player1Field.getCountSub();
        }
        return player2Field.getCountSub();
    }

    public boolean placeShip(int size, int x, int y, boolean horizontal) {
        Field currentField = (currentPlayer == 1) ? player1Field : player2Field;
        if (horizontal) {
            if (x < 0 || y < 0 || x > 9 || y > 10 - size) {
                return false;
            }
        } else {
            if (x < 0 || y < 0 || x > 10 - size || y > 9) {
                return false;
            }
        }
        return currentField.placeShip(size, x, y, horizontal);
    }

    public boolean placeMine(int x, int y) {
        Field currentField = (currentPlayer == 1) ? player1Field : player2Field;
        if (x < 0 || y < 0 || x > 9 || y > 9) {
            return false;
        }
        return currentField.placeMine(x, y);
    }

    public int attack(int x, int y) {
        Field opponentField = (currentPlayer == 1) ? player2Field : player1Field;
        if (x < 0 || y < 0 || x > 9 || y > 9) {
            return -1;
        }
        int res = opponentField.attack(x, y);
        if (res > 1) {
            switchPlayer();
            return 1;
        }
        if (res == 1) {
            switchPlayer();
            return 2;
        }
        return 0;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }

    public boolean checkWin() {
        return player1Field.allShipsSunk() || player2Field.allShipsSunk();
    }

    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        while (!checkWin()) {
            System.out.println("Current Player: " + currentPlayer);
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            attack(x, y);
            switchPlayer();
        }
        System.out.println("Player " + currentPlayer + " wins!");
        scanner.close();
    }

    public static void main(String[] args) {
        Game game = new Game(3, 3, 3);
        game.startGame();
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

}