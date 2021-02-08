package ui;

import model.Stock;

public class Main {
    public static void main(String[] args) {
        Stock stock = new Stock("A");
        System.out.println(stock.getBidPrice());


    }
}
