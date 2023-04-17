package sc.player2023.logic;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sc.api.plugins.Coordinates;
import sc.api.plugins.IGameState;
import sc.player.IGameHandler;
import sc.player2023.Debug;
import sc.plugin2023.Board;
import sc.plugin2023.Field;
import sc.plugin2023.GameState;
import sc.plugin2023.Move;
import sc.shared.GameResult;

import java.util.List;

/**
 * Das Herz des Clients:
 * Eine sehr simple Logik, die ihre Zuege zufaellig waehlt,
 * aber gueltige Zuege macht.
 * <p>
 * Ausserdem werden zum Spielverlauf Konsolenausgaben gemacht.
 */
public class Logic implements IGameHandler {
  private static final Logger log = LoggerFactory.getLogger(Logic.class);
  private static final Debug debug = new Debug(Debug.ANZAHL_FELDER, Debug.ANZAHL_FISCHE, Debug.STOP_GRUND);

  /** Aktueller Spielstatus. */
  private GameState gameState;

  public void onGameOver(GameResult data) {
    log.info("Das Spiel ist beendet, Ergebnis: {}", data);
  }

  @NotNull
  @Override
  public Move calculateMove() {
    long startTime = System.currentTimeMillis();
    log.info("Es wurde ein Zug von {} angefordert.", gameState.getCurrentTeam());

    List<Move> possibleMoves = gameState.getPossibleMoves();
    Move move;
    if(gameState.getTurn() < 8) {
      move = placePinguin();
    } else {
      move = movePinguin();
    }

    log.info("Sende {} nach {}ms.", move, System.currentTimeMillis() - startTime);
    return move;
  }
  
  public Move placePinguin() {
    Board board = gameState.getBoard();
    Move doMove = new Move(new Coordinates(0, 0), new Coordinates(0, 0));
    int maxFische = 0;
    for (Move move: gameState.getPossibleMoves()) {
      Coordinates pos = move.getTo();
      int fische = 0;

      // Debug
      final Integer[] fischeProRichtung = new Integer[]{0, 0, 0, 0, 0, 0};
      final Integer[] felderProRichtung = new Integer[]{0, 0, 0, 0, 0, 0};
      final Character[] stopGrundProRichtung = new Character[]{'e', 'e', 'E', 'E', 'E', 'E'};

      // Rechts
      for (int x = pos.getX() + 2; x <= 14; x += 2) {
        Field field = board.get(x, pos.getY());
        if (field.isOccupied() || field.isEmpty()) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[0] = 'O');
          break;
        }
        fische += field.getFish();
        debug.action(Debug.ANZAHL_FISCHE, () -> fischeProRichtung[0] += field.getFish());
        debug.action(Debug.ANZAHL_FELDER, () -> felderProRichtung[0]++);
      }

      // Links
      for (int x = pos.getX() - 2; x >= 0; x -= 2) {
        Field field = board.get(x, pos.getY());
        if (field.isOccupied() || field.isEmpty()) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[1] = 'O');
          break;
        }
        fische += field.getFish();
        debug.action(Debug.ANZAHL_FISCHE, () -> fischeProRichtung[1] += field.getFish());
        debug.action(Debug.ANZAHL_FELDER, () -> felderProRichtung[1]++);
      }

      // Runter-R
      int x = pos.getX();
      for (int y = pos.getY() + 1; y <= 7; y++) {
        x++;
        if (x > 14) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[2] = 'e');
          break;
        }
        Field field = board.get(x, y);
        if (field.isOccupied() || field.isEmpty()) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[2] = 'O');
          break;
        }
        fische += field.getFish();
        debug.action(Debug.ANZAHL_FISCHE, () -> fischeProRichtung[2] += field.getFish());
        debug.action(Debug.ANZAHL_FELDER, () -> felderProRichtung[2]++);
      }

      // Runter-L
      x = pos.getX();
      for (int y = pos.getY() + 1; y <= 7; y++) {
        x--;
        if (x < 0) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[3] = 'e');
          break;
        }
        Field field = board.get(x, y);
        if (field.isOccupied() || field.isEmpty()) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[3] = 'O');
          break;
        }
        fische += field.getFish();
        debug.action(Debug.ANZAHL_FISCHE, () -> fischeProRichtung[3] += field.getFish());
        debug.action(Debug.ANZAHL_FELDER, () -> felderProRichtung[3]++);
      }

      // Hoch-R
      x = pos.getX();
      for (int y = pos.getY() - 1; y >= 0; y--) {
        x++;
        if (x > 14) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[4] = 'e');
          break;
        }
        Field field = board.get(x, y);
        if (field.isOccupied() || field.isEmpty()) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[4] = 'O');
          break;
        }
        fische += field.getFish();
        debug.action(Debug.ANZAHL_FISCHE, () -> fischeProRichtung[4] += field.getFish());
        debug.action(Debug.ANZAHL_FELDER, () -> felderProRichtung[4]++);
      }

      // Hoch-L
      x = pos.getX();
      for (int y = pos.getY() - 1; y >= 0; y--) {
        x--;
        if (x < 0) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[5] = 'e');
          break;
        }
        Field field = board.get(x, y);
        if (field.isOccupied() || field.isEmpty()) {
          debug.action(Debug.STOP_GRUND, () -> stopGrundProRichtung[5] = 'O');
          break;
        }
        fische += field.getFish();
        debug.action(Debug.ANZAHL_FISCHE, () -> fischeProRichtung[5] += field.getFish());
        debug.action(Debug.ANZAHL_FELDER, () -> felderProRichtung[5]++);
      }
      if (fische > maxFische) {
        maxFische = fische;
        doMove = move;
        debug.log(Debug.ANZAHL_FISCHE, pos, fische, fischeProRichtung);
        debug.log(Debug.ANZAHL_FELDER, (Object) felderProRichtung);
        debug.log(Debug.STOP_GRUND, (Object) stopGrundProRichtung);
      }
    }
    return doMove;
  }
  
  public Move movePinguin() {
    return null;
  }

  @Override
  public void onUpdate(IGameState gameState) {
    this.gameState = (GameState) gameState;
    log.info("Zug: {} Dran: {}", gameState.getTurn(), gameState.getCurrentTeam());
  }

  @Override
  public void onError(String error) {
    log.warn("Fehler: {}", error);
  }
}
