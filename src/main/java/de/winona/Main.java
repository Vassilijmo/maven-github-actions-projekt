// src/main/java/de/winona/Main.java
package de.winona;

public class Main {
    public static void main(String[] args) {
        String iban = args.length > 0 ? args[0] : "DE89370400440532013000";
        boolean ok = IBANChecker.validate(iban);  // <-- HIER: validate statt isValid
        System.out.println("IBAN " + iban + " -> " + (ok ? "gültig" : "ungültig"));
    }
}
