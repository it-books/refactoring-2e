package org.woookk.refactoring.chapter01;

import org.woookk.refactoring.chapter01.dto.Invoice;

import java.util.Map;

public class Statement {
    public static String statement(Map plays, Invoice invoice) {
        double totalAmount = 0;
        int volumeCredits = 0;
        String result = String.format("청구내역(고객명: %s)\n", invoice.getCustomer());

        for (Invoice.Performance performance : invoice.getPerformances()) {
            Map<String, String> play = (Map<String, String>) plays.get(performance.getPlayId());
            double thisAmount = 0;
            switch (play.get("type")) {
                case "tragedy": // 비극
                    thisAmount = 40000;
                    if (performance.getAudience() > 30) {
                        thisAmount += 1000 * (performance.getAudience() - 30);
                    }
                    break;
                case "comedy": // 희극
                    thisAmount = 30000;
                    if (performance.getAudience() > 20) {
                        thisAmount += 10000 + 500 * (performance.getAudience() - 20);
                    }
                    thisAmount += 300 * performance.getAudience();
                    break;
                default:
                    throw new RuntimeException("Unknown type: " + play.get("type"));
            }
            // 포인트를 적립한다.
            volumeCredits += Math.max(performance.getAudience() - 30, 0);
            // add extra credit for every ten comedy attendees
            if ("comedy".equals(play.get("type"))) {
                volumeCredits += Math.floor(performance.getAudience() / 5);
            }
            // 청구 내역을 출력한다.
            result += String.format("  %s: $%.2f (%d석)\n", play.get("name"), thisAmount / 100, performance.getAudience());
            totalAmount += thisAmount;
        }
        result += String.format("총액: $%.2f\n", totalAmount / 100);
        result += String.format("적립 포인트: %d점\n", volumeCredits);
        return result;
    }
}