package sc.player2023;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.Logic;

import java.util.Arrays;
import java.util.List;
public class Debug {
    public interface DebugAction {
        void perform();
    }
    private static final Logger log = LoggerFactory.getLogger(Logic.class);
    /**
     * Anzahl der Fische welche im bereich des neu platzierten Pinguins sind.
     */
    public static final String ANZAHL_FISCHE = "anzahl-fische";

    /**
     * Anzahl der Felder welche im bereich des neu platzierten Pinguins sind.
     */
    public static final String ANZAHL_FELDER = "anzahl-felder";

    /**
     * Grund warum das Felderfortschreiten gestoppt wurde.
     */
    public static final String STOP_GRUND = "stop-grund";

    private final List<String> debug;
    public Debug(String... debug) {
        this.debug = Arrays.asList(debug);
    }

    public void log(String id, Object... args) {
        if (!debug.contains(id)) {
            return;
        }
        log.info("DEBUG {}:", id);
        switch (id) {
            case ANZAHL_FISCHE:
                anzahlFische(args);
                break;
            case ANZAHL_FELDER:
                anzahlFelder(args);
                break;
            case STOP_GRUND:
                stopGrund(args);
                break;
        }
        log.info("");
    }

    public void action(String id, DebugAction action) {
        if (debug.contains(id)) {
            action.perform();
        }
    }

    private final int[] fixOrderAnzahl = new int[]{5, 4, 1, 0, 3, 2};
    private void anzahlFische(Object[] args) {
        log.info("  {} {} Fische!", args[0], args[1]);
        displayHex((Object[]) args[2], fixOrderAnzahl);
    }

    private void anzahlFelder(Object[] args) {
        displayHex((Object[]) args[0], fixOrderAnzahl);
    }

    private void stopGrund(Object[] args) {
        log.info("  E: Ende des Boards vertikal erreicht.");
        log.info("  e: Ende des Boards horizontal erreicht.");
        log.info("  O: Besetztes oder leeres Feld erreicht.");
        displayHex((Object[]) args[0], fixOrderAnzahl);
    }

    /**
     * @param sixUnordered Muss genau sechs element beinhalen.
     * @param order Muss genau sech elemente beinhalten.
     */
    private void displayHex(Object[] sixUnordered, int... order) {
        displayHex(sixUnordered[order[0]], sixUnordered[order[1]], sixUnordered[order[2]], sixUnordered[order[3]], sixUnordered[order[4]], sixUnordered[order[5]]);
    }

    /**
     * @param six Muss genau sechs element beinhalen.
     */
    private void displayHex(Object[] six) {
        displayHex(six[0], six[1], six[2], six[3], six[4], six[5]);
    }

    private void displayHex(Object tl, Object tr, Object l, Object r, Object bl, Object br) {
        displayHex(tl.toString(), tr.toString(), l.toString(), r.toString(), bl.toString(), br.toString());
    }

    private void displayHex(String tl, String tr, String l, String r, String bl, String br) {
        if (tl.length() == 0) {
            log.info("         {}", tr);
        } else if (tl.length() == 1) {
            log.info("      {}  {}", tl, tr);
        } else if (tl.length() == 2){
            log.info("     {}  {}", tl, tr);
        } else {
            log.info("    {}  {}", tl, tr);
        }
        if (l.length() == 0) {
            log.info("       PP  {}", r);
        } else if (l.length() == 1) {
            log.info("    {}  PP  {}", l, r);
        } else if (l.length() == 2){
            log.info("   {}  PP  {}", l, r);
        } else {
            log.info("  {}  PP  {}", l, r);
        }
        if (bl.length() == 0) {
            log.info("         {}", br);
        } else if (bl.length() == 1) {
            log.info("      {}  {}", bl, br);
        } else if (bl.length() == 2){
            log.info("     {}  {}", bl, br);
        } else {
            log.info("    {}  {}", bl, br);
        }
    }
}
