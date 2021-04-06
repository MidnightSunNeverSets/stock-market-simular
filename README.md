# Stock Market Simulator

## Project Proposal

***Investing is only fun when you YOLO it! :D***

The purpose of this application is to simulate some of the trading conditions one might encounter in the actual 
stock market. Through a virtual market environment, all with its own fake companies and equities, players
interested in learning more about the market, or just for their own entertainment, will be able to trade stocks
and have fun with their trading methods without actually running financial risk. 

As someone with very little prior interest in investing, I view this project as a way for me learn more about the stock 
market. When it comes to money, my knowledge of finances, and really anything involving the economy, is quite lacking. 
Therefore, by basing the project around the stock exchange, I will be forced to put in the necessary research to 
understand its terminology and mechanics. Moreover, in light of the recent GME trading mania, I've also realized that 
I've always wanted to experience the thrill of betting one's life savings on high-risk trades and losing everything, 
just without the parts involving actual financial ruin.   


## User Stories
- As a user, I want to be able to see the catalogue of stocks to purchase from
- As a user, I want to be able to purchase shares of a company
- As a user, I want to be able to check the current stock value
- As a user, I want to be able to check the current ask price of the stock
- As a user, I want to be able to check the current bid price of the stock 
- As a user, I want to be able to sell shares
- As a user, I want to be able to check the daily percentage change of the stock
- As a user, I want to be able to forward the stock market to the next day
- As a user, I want to be able to save my portfolio when I exit
- As a user, I want to be able to choose between reloading my portfolio or starting a new one

## Phase 4: Task 2
- Made use of the Map interface in the StockMarketGUI class. 

## Phase 4: Task 3
Given more time, one of the things I would like to change is the ArrayList fields in the Market class that store the
company names and available stocks. As each name corresponds to a single stock, it would be more efficient to use a 
single Map implementation instead, where the company name is the key and the corresponding stock is the value. 

I would also like to refactor the StockMarketGUI class. The class is involved with creating both the GUI components and
implementing the functionality of the simulator. Therefore, to increase cohesion, I could extract all the methods
and fields dealing with the simulator to a separate class, using an association to the new class to implement the 
simulator functionality in the GUI. 




