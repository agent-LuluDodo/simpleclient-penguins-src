package sc.player2023;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.player2023.logic.Logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

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

    /**
     * Felder welche gescanned wurde.
     */
    public static final String SCANNED_FIELD = "scanned-field";
    private final HashMap<String, ArrayList<Object>> debug;
    public Debug(String... debug) {
        this.debug = new HashMap<>();
        for (String debugOption:debug) {
            this.debug.put(debugOption, new ArrayList<>());
        }
    }

    public void log(String id) {
        if (!debug.containsKey(id)) {
            return;
        }
        log.info("DEBUG {}:", id);
        switch (id) {
            case ANZAHL_FISCHE -> anzahlFische(getArgs(id));
            case ANZAHL_FELDER -> anzahlFelder(getArgs(id));
            case STOP_GRUND -> stopGrund(getArgs(id));
            case SCANNED_FIELD -> scannedField(getArgs(id));
        }
        log.info("");
    }

    private final int[] fixOrderAnzahl = new int[]{5, 4, 1, 0, 3, 2};
    private void anzahlFische(Object[] args) {
        log.info("  {} {} Fische!", args[6], args[7]);
        displayHex(args, fixOrderAnzahl);
    }

    private void anzahlFelder(Object[] args) {
        displayHex(args, fixOrderAnzahl);
    }

    private void stopGrund(Object[] args) {
        log.info("  E: Ende des Boards vertikal erreicht.");
        log.info("  e: Ende des Boards horizontal erreicht.");
        log.info("  O: Besetztes oder leeres Feld erreicht.");
        displayHex(args, fixOrderAnzahl);
    }

    private void scannedField(Object[] args) {
        log.info("Scanned {}!", args[0]);
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

    private Object[] getArgs(String id) {
        return debug.get(id).toArray();
    }

    public void setArg(String id, int index, Object arg) {
        if (!debug.containsKey(id)) {
            return;
        }
        List<Object> args = debug.get(id);
        if (args.size() == index) {
            args.add(arg);
        } else {
            args.set(index, arg);
        }
    }

    public void setArg(String id, int index, Consumer<Object> action) {
        if (!debug.containsKey(id)) {
            return;
        }
        action.accept(debug.get(id).get(index));
    }

    public void setArg(String id, Object... args) {
        if (!debug.containsKey(id)) {
            return;
        }
        debug.put(id, new ArrayList<>(List.of(args)));
    }

    public void countArg(String id, int index, int amount) {
        if (!debug.containsKey(id)) {
            return;
        }
        Integer count;
        try {
            count = (Integer) debug.get(id).get(index);
        } catch (IndexOutOfBoundsException e) {
            count = 0;
        }
        List<Object> args = debug.get(id);
        if (args.size() == index) {
            args.add(count + amount);
        } else {
            args.set(index, count + amount);
        }
    }
}
