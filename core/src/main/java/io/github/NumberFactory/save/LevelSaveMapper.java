package io.github.NumberFactory.save;

import io.github.NumberFactory.model.*;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.board.Cell;
import io.github.NumberFactory.save.data.*;

import java.util.ArrayList;
import java.util.List;

public class LevelSaveMapper {

    private final LevelDefinitionMapper componentMapper = new LevelDefinitionMapper();

    public LevelSaveData toSaveData(Level level, String levelId) {
        Board board = level.getMachine().getBoard();
        List<CellData> placements = new ArrayList<>();

        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Cell cell = board.getCell(x, y);
                if (cell.isReadOnly() || cell.isEmpty()) continue;
                CellData cellData = new CellData();
                cellData.x = x;
                cellData.y = y;
                cellData.component = componentMapper.toComponentData(cell.getComponent());
                placements.add(cellData);
            }
        }

        LevelSaveData save = new LevelSaveData();
        save.levelName = levelId;
        save.placements = placements;
        return save;
    }

    public void applyTo(Level level, LevelSaveData save) {
        if (save == null) return;
        Board board = level.getMachine().getBoard();

        if (save.placements != null) {
            for (CellData cellData : save.placements) {
                Cell cell = board.getCell(cellData.x, cellData.y);
                if (cell == null || cell.isReadOnly()) continue;
                cell.setComponent(componentMapper.fromComponentData(cellData.component));
            }
        }
        board.rewireAll();
        rebuildInventory(level);
    }

    private void rebuildInventory(Level level) {
        Board board = level.getMachine().getBoard();
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Cell cell = board.getCell(x, y);
                if (!cell.isEmpty() && !cell.isReadOnly()) {
                    level.getInventory().take(cell.getComponent().getClass());
                }
            }
        }
    }
}
