package de.winona;

import java.util.*;

/**
 * Prüft IBANs gemäß ISO 13616 (Modulo-97).
 *
 * Kleines Demo-Entry: prüft eine Beispiel-IBAN.
 */
public class IBANChecker {

    /** Erwartete IBAN-Längen pro Länderpräfix. */
    private static final Map<String, Integer> LENGTHS = new HashMap<>();
    static {
        LENGTHS.put("AT", 20);
        LENGTHS.put("BE", 16);
        LENGTHS.put("CZ", 24);
        LENGTHS.put("DE", 22);
        LENGTHS.put("DK", 18);
        LENGTHS.put("FR", 27);
    }

    /**
     * Kleines Demo-Entry.
     * @param args keine Nutzung
     */
    public static void main(String[] args) {
        String iban = "DE22790200760027913168"; // gültige Beispiel-IBAN
        System.out.println("Welcome to the IBAN Checker!");
        System.out.println("IBAN " + iban + " is " + validate(iban));
    }

    /**
     * Validiert eine IBAN.
     * Ablauf: Längencheck → Verschieben → A..Z→10..35 → Modulo-97.
     * @param iban IBAN (Leerzeichen erlaubt, Groß/Klein egal)
     * @return true, wenn gültig
     */
    public static boolean validate(String iban) {
        if (iban == null) return false;
        iban = iban.replace(" ", "");
        if (!checkLength(iban)) return false;

        String rearranged = rearrangeIban(iban);
        String converted  = convertToInteger(rearranged);
        List<String> segs = createSegments(converted);
        return calculate(segs) == 1;
    }

    /** Prüft, ob die Länge zum Länderpräfix passt. */
    private static boolean checkLength(String iban) {
        if (iban.length() < 4) return false;
        String countryCode = iban.substring(0, 2).toUpperCase(Locale.ROOT);
        Integer expected = LENGTHS.get(countryCode);
        return expected != null && expected == iban.length();
    }

    /** Verschiebt die ersten 4 Zeichen ans Ende. */
    private static String rearrangeIban(String iban) {
        return iban.substring(4) + iban.substring(0, 4);
    }

    /** Wandelt Buchstaben A..Z zu 10..35, Ziffern bleiben. */
    private static String convertToInteger(String s) {
        StringBuilder out = new StringBuilder();
        for (char c : s.toUpperCase(Locale.ROOT).toCharArray()) {
            if (Character.isDigit(c)) {
                out.append(c);
            } else if (Character.isLetter(c)) {
                out.append(c - 55); // 'A'(65) -> 10
            }
        }
        return out.toString();
    }

    /** Erstes Segment 9-stellig, danach 7er-Blöcke; Rest anhängen. */
    private static List<String> createSegments(String digits) {
        List<String> segs = new ArrayList<>();
        if (digits.isEmpty()) return segs;
        int first = Math.min(9, digits.length());
        segs.add(digits.substring(0, first));
        int i = first;
        while (i + 7 <= digits.length()) {
            segs.add(digits.substring(i, i + 7));
            i += 7;
        }
        if (i < digits.length()) segs.add(digits.substring(i));
        return segs;
    }

    /** Modulo-97 schrittweise über die Segmente. */
    private static int calculate(List<String> segments) {
        long n = 0;
        for (String seg : segments) {
            String part = (seg.length() == 9) ? seg : (n + seg);
            n = Long.parseLong(part) % 97;
        }
        return (int) n;
    }
}
