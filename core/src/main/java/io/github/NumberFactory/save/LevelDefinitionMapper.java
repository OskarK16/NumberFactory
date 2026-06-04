package io.github.NumberFactory.save;

import io.github.NumberFactory.model.*;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.board.Cell;
import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.utility.GeneratorComponent;
import io.github.NumberFactory.save.data.*;
import io.github.NumberFactory.utils.ComponentType;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

import java.util.*;

public class LevelDefinitionMapper {

    public LevelDefinitionData toDefinition(Level level, String name) {
        Board board = level.getMachine().getBoard();

        MetadataLevelData meta = new MetadataLevelData();
        meta.name = name;
        meta.mode = level.getMode().name();
        meta.boardWidth = board.width;
        meta.boardHeight = board.height;

        List<CellDefinitionData> boardLayout = new ArrayList<>();
        for (int x = 0; x < board.width; x++) {
            for (int y = 0; y < board.height; y++) {
                Cell cell = board.getCell(x, y);
                if (cell.isEmpty() && !cell.isReadOnly()) continue;
                CellDefinitionData cellData = new CellDefinitionData();
                cellData.x = x;
                cellData.y = y;
                cellData.readOnly = cell.isReadOnly();
                cellData.component = cell.isEmpty() ? null : toComponentData(cell.getComponent());
                boardLayout.add(cellData);
            }
        }

        List<InventoryEntryData> inventoryData = null;
        Inventory inv = level.getInventory();
        if (!inv.isUnlimited()) {
            inventoryData = new ArrayList<>();
            for (ComponentType type : ComponentType.values()) {
                int limit = inv.limitOf(type.componentClass());
                if (limit > 0) {
                    InventoryEntryData entry = new InventoryEntryData();
                    entry.type  = type.name();
                    entry.limit = limit;
                    inventoryData.add(entry);
                }
            }
        }

        LevelDefinitionData data = new LevelDefinitionData();
        data.metadata = meta;
        data.cellTypeDefinitions = null; // TODO after we implement level editor
        data.boardLayout = boardLayout;
        data.inventory = inventoryData;
        data.goal = level.getGoal() != null ? new ArrayList<>(level.getGoal().expected()) : null;
        return data;
    }

    public Level fromDefinition(LevelDefinitionData data) {
        MetadataLevelData meta = data.metadata;
        Board board = new Board(meta.boardWidth, meta.boardHeight);

        Map<String, CellTypeSaveData> cellTypeMap = buildCellTypeMap(data.cellTypeDefinitions);

        if (data.boardLayout != null) {
            for (CellDefinitionData cellData : data.boardLayout) {
                Cell cell = board.getCell(cellData.x, cellData.y);
                if (cell == null) continue;

                if (cellData.cellTypeId != null) {
                    CellTypeSaveData ct = cellTypeMap.get(cellData.cellTypeId);
                    if (ct != null) applyFilter(cell, ct);
                }

                if (cellData.component != null) {
                    cell.setComponent(fromComponentData(cellData.component));
                }

                cell.setReadOnly(cellData.readOnly);
            }
        }

        board.rewireAll();
        Machine machine = new Machine(board);
        GameMode mode = GameMode.valueOf(meta.mode);

        if (mode == GameMode.SANDBOX) return Level.sandbox(machine);
        return Level.campaign(machine, buildInventory(data.inventory), new Goal(data.goal));
    }

    Component fromComponentData(ComponentSaveData data) {
        ComponentType type = ComponentType.valueOf(data.type);
        Component c = type.create(data.value != null ? data.value : 0);
        for (Directions dir : Directions.values()) {
            int i = dir.ordinal();
            if (data.ports != null && data.ports[i] != null) c.setPort(dir, data.ports[i]);
            if (data.portReadOnly != null && data.portReadOnly[i]) c.setPortReadOnly(dir, true);
        }
        return c;
    }

    ComponentSaveData toComponentData(Component c) {
        PortType[] ports    = new PortType[4];
        boolean[] readOnly  = new boolean[4];
        for (Directions dir : Directions.values()) {
            ports[dir.ordinal()]   = c.getPort(dir);
            readOnly[dir.ordinal()] = c.isPortReadOnly(dir);
        }
        ComponentType type = ComponentType.of(c);

        ComponentSaveData data = new ComponentSaveData();
        data.type = type.name();
        data.ports = ports;
        data.portReadOnly = readOnly;
        data.value = type == ComponentType.GENERATOR
            ? ((GeneratorComponent) c).getSeedValue()
            : null;
        return data;
    }

    private void applyFilter(Cell cell, CellTypeSaveData ct) {
        Set<ComponentType> filter = EnumSet.noneOf(ComponentType.class);
        if (ct.componentTypes != null) {
            for (String name : ct.componentTypes) filter.add(ComponentType.valueOf(name));
        }
        cell.setFilter(filter, ct.blacklist);
    }

    private Map<String, CellTypeSaveData> buildCellTypeMap(List<CellTypeSaveData> defs) {
        Map<String, CellTypeSaveData> map = new HashMap<>();
        if (defs != null) {
            for (CellTypeSaveData ct : defs) map.put(ct.id, ct);
        }
        return map;
    }

    private Inventory buildInventory(List<InventoryEntryData> entries) {
        if (entries == null) return Inventory.unlimited();
        Map<Class<? extends Component>, Integer> limits = new HashMap<>();
        for (InventoryEntryData e : entries) {
            if (e.limit != null) {
                limits.put(ComponentType.valueOf(e.type).componentClass(), e.limit);
            }
        }
        return Inventory.of(limits);
    }
}
