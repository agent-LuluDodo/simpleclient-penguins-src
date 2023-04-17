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

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;

/**
 * Das Herz des Clients:
 * Eine sehr simple Logik, die ihre Zuege zufaellig waehlt,
 * aber gueltige Zuege macht.
 * <p>
 * Ausserdem werden zum Spielverlauf Konsolenausgaben gemacht.
 */
public class Logic implements IGameHandler {
  private static final Logger log = LoggerFactory.getLogger(Logic.class);
  private static final Debug debug = new Debug(Debug.ANZAHL_FELDER, Debug.ANZAHL_FISCHE, Debug.STOP_GRUND, Debug.SCANNED_FIELD);

  /** Aktueller Spielstatus. */
  private GameState gameState;

  public void onGameOver(@NotNull GameResult data) {
    log.info("Das Spiel ist beendet, Ergebnis: {}", data);
  }

  @NotNull
  @Override
  public Move calculateMove() {
    long startTime = System.currentTimeMillis();
    log.info("Es wurde ein Zug von {} angefordert.", gameState.getCurrentTeam());

    Move move;
    if(gameState.getTurn() < 8) {
      move = placePinguin();
    } else {
      move = movePinguin();
    }

    log.info("Sende {} nach {}ms.", move, System.currentTimeMillis() - startTime);
    return move;
  }

  // Todo: berechne abschneiden und sicherheit mit ein
  public Move placePinguin() {
    Move doMove = new Move(new Coordinates(0, 0), new Coordinates(0, 0));
    int maxFische = 0;
    for (Move move: gameState.getPossibleMoves()) {
      AtomicInteger fische = new AtomicInteger(0);

      debug.setArg(Debug.ANZAHL_FISCHE, 0, 0, 0, 0, 0, 0);
      foreachReachableFields(move.getTo(), (field, direction) -> {
        fische.addAndGet(field.getFish());
        debug.countArg(Debug.ANZAHL_FISCHE, direction.ordinal(), field.getFish());
      });

      debug.setArg(Debug.SCANNED_FIELD, 0, move.getTo());
      debug.log(Debug.SCANNED_FIELD);

      if (fische.get() > maxFische) {
        maxFische = fische.get();
        doMove = move;
        debug.setArg(Debug.ANZAHL_FISCHE, 6, move.getTo());
        debug.setArg(Debug.ANZAHL_FISCHE, 7, fische.get());
        debug.log(Debug.ANZAHL_FISCHE);
        debug.log(Debug.ANZAHL_FELDER);
        debug.log(Debug.STOP_GRUND);
      }
    }
    return doMove;
  }

  private enum Direction {
    RIGHT,
    LEFT,
    DOWN_RIGHT,
    DOWN_LEFT,
    UP_RIGHT,
    UP_LEFT
  }

  public void foreachReachableFields(Coordinates pos, BiConsumer<Field, Direction> action) {
    debug.setArg(Debug.STOP_GRUND, 'e', 'e', 'E', 'E', 'E', 'E');
    debug.setArg(Debug.ANZAHL_FELDER, 0, 0, 0, 0, 0, 0);
    Board board = gameState.getBoard();
    // Rechts
    for (int x = pos.getX() + 2; x <= 14; x += 2) {
      Field field = board.get(x, pos.getY());
      if (field.isOccupied() || field.isEmpty()) {
        debug.setArg(Debug.STOP_GRUND, 0, 'O');
        break;
      }
      debug.countArg(Debug.ANZAHL_FELDER, 0, 1);
      action.accept(field, Direction.RIGHT);
    }

    // Links
    for (int x = pos.getX() - 2; x >= 0; x -= 2) {
      Field field = board.get(x, pos.getY());
      if (field.isOccupied() || field.isEmpty()) {
        debug.setArg(Debug.STOP_GRUND, 1, 'O');
        break;
      }
      debug.countArg(Debug.ANZAHL_FELDER, 1, 1);
      action.accept(field, Direction.LEFT);
    }

    // Runter-R
    int x = pos.getX();
    for (int y = pos.getY() + 1; y <= 7; y++) {
      x++;
      if (x > 14) {
        debug.setArg(Debug.STOP_GRUND, 2, 'e');
        break;
      }
      Field field = board.get(x, y);
      if (field.isOccupied() || field.isEmpty()) {
        debug.setArg(Debug.STOP_GRUND, 2, 'O');
        break;
      }
      debug.countArg(Debug.ANZAHL_FELDER, 2, 1);
      action.accept(field, Direction.DOWN_RIGHT);
    }

    // Runter-L
    x = pos.getX();
    for (int y = pos.getY() + 1; y <= 7; y++) {
      x--;
      if (x < 0) {
        debug.setArg(Debug.STOP_GRUND, 3, 'e');
        break;
      }
      Field field = board.get(x, y);
      if (field.isOccupied() || field.isEmpty()) {
        debug.setArg(Debug.STOP_GRUND, 3, 'O');
        break;
      }
      debug.countArg(Debug.ANZAHL_FELDER, 3, 1);
      action.accept(field, Direction.DOWN_LEFT);
    }

    // Hoch-R
    x = pos.getX();
    for (int y = pos.getY() - 1; y >= 0; y--) {
      x++;
      if (x > 14) {
        debug.setArg(Debug.STOP_GRUND, 4, 'e');
        break;
      }
      Field field = board.get(x, y);
      if (field.isOccupied() || field.isEmpty()) {
        debug.setArg(Debug.STOP_GRUND, 4, 'O');
        break;
      }
      debug.countArg(Debug.ANZAHL_FELDER, 4, 1);
      action.accept(field, Direction.UP_RIGHT);
    }

    // Hoch-L
    x = pos.getX();
    for (int y = pos.getY() - 1; y >= 0; y--) {
      x--;
      if (x < 0) {
        debug.setArg(Debug.STOP_GRUND, 5, 'e');
        break;
      }
      Field field = board.get(x, y);
      if (field.isOccupied() || field.isEmpty()) {
        debug.setArg(Debug.STOP_GRUND, 5, 'O');
        break;
      }
      debug.countArg(Debug.ANZAHL_FELDER, 5, 1);
      action.accept(field, Direction.UP_LEFT);
    }
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
  public void onError(@NotNull String error) {
    log.warn("Fehler: {}", error);
  }
}
