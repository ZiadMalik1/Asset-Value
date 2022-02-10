/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.value;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import org.json.JSONObject;
import yahoofinance.YahooFinance;


/**
 *
 * @author ZiadN
 */
public class Main implements ActionListener{
    private Stocks assets = new Stocks();
    private static JLabel label;
    private static JLabel passLabel;
    private static JLabel success;
    private static JTextField creditCardDebt;
    private static JTextField bankBalance;
    private static JButton button;

    public static void main(String[] args) throws IOException, InterruptedException {

        JPanel panel = new JPanel();
        JFrame frame = new JFrame();
        frame.setSize(550,200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        
        panel.setLayout(null);
        
        label = new JLabel("Please Enter Credit Card Debt");
        label.setBounds(10,20,200,25);
        panel.add(label);
        
        creditCardDebt = new JTextField(20);
        creditCardDebt.setBounds(245,20,250,25);
        panel.add(creditCardDebt);

        passLabel = new JLabel("Please Enter Bank Account Balances");
        passLabel.setBounds(10,50,250,25);
        panel.add(passLabel);

        bankBalance = new JTextField(20);
        bankBalance.setBounds(245,50,250,25);
        panel.add(bankBalance);

        button = new JButton("Calculate");
        button.setBounds(187, 110, 150, 25);
        button.addActionListener(new Main());
        panel.add(button);

        success = new JLabel("");
        success.setBounds(10,110,300,25);
        panel.add(success);

        frame.setVisible(true);
        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        double debt = Double.valueOf(creditCardDebt.getText());
        double balance = Double.valueOf(bankBalance.getText());
        success.setText(String.valueOf("Total Assets: "
                + Math.round(assets.getTotalAssets(balance, debt) * 100.0) / 100.0));
    }
    
    public class Stocks{
    
           private static HttpURLConnection connection;
    public Stocks() {
    }

    public double getTotalAssets(double balance, double debt){
        double total = 0.0;
        try {
            total += (this.getStockAssets() + balance) - debt;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return total;
    }


    public float getCrypto(String cUrl){
        BufferedReader reader;
        String line;
        float price = 0.0F;
        StringBuilder responseContent = new StringBuilder();
        try {
            URL url = new URL(cUrl);
            connection = (HttpURLConnection) url.openConnection();

            //Request Setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            if(status > 299){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while((line = reader.readLine()) != null){
                    responseContent.append(line);
                }
                reader.close();
            }
            else{
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while((line = reader.readLine()) != null){
                    responseContent.append(line);
                }
                reader.close();
            }
            price = parse(responseContent.toString());
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        } finally {
            connection.disconnect();
        }
        return price;
    }

    private static float parse(String responseBody){
        JSONObject objects = new JSONObject(responseBody);
        return objects.getFloat("price");
    }

    public double getStock(String stockName) throws IOException{
        double price;
        BigDecimal bigPrice = YahooFinance.get(stockName).getQuote().getPrice();
        price = bigPrice.doubleValue();
        return price;
    }

    private double getStockAssets() throws IOException{
        double luvHoldings = 50.570824;
        double aaplHoldings = 25.646155;
        double disHoldings = 14.802847;
        double amznHoldings = 1;
        double cbayHoldings = 1;
        double krHoldings = 38.04049;
        double nvdaHoldings = 4;
        double vooHoldings = 3;
        double msftHoldings = 2.0037;
        double pyplHoldings = 3;
        double xswHoldings = 2;
        double btcHoldings = 0.02138327;
        double ethHoldings = 0.21395754;
        double fbHoldings = 3;
        double uHoldings = 4;
        double assets = (this.getStock("LUV") * luvHoldings)
                + (this.getStock("AAPL") * aaplHoldings)
                + (this.getStock("DIS")  * disHoldings)
                + (this.getStock("CBAY") * cbayHoldings)
                + (this.getStock("MSFT") * msftHoldings)
                + (this.getStock("PYPL") * pyplHoldings)
                + (this.getStock("XSW")  * xswHoldings)
                + (this.getStock("KR")   * krHoldings)
                + (this.getStock("AMZN") * amznHoldings)
                + (this.getStock("NVDA") * nvdaHoldings)
                + (this.getStock("VOO")  * vooHoldings)
                + (this.getStock("U") * uHoldings)
                + (this.getStock("FB") * fbHoldings)
                + (this.getCrypto("https://api.binance.com/api/v3/ticker/price?symbol=BTCUSDT") * btcHoldings)
                + (this.getCrypto("https://api.binance.com/api/v3/ticker/price?symbol=ETHUSDT") * ethHoldings);
        return assets;
    }
} 
}

