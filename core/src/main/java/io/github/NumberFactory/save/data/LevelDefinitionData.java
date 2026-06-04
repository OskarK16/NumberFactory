package io.github.NumberFactory.save.data;

import java.util.List;

public class LevelDefinitionData {
    public MetadataLevelData metadata;
    public List<CellTypeSaveData> cellTypeDefinitions;
    public List<CellDefinitionData> boardLayout;
    public List<InventoryEntryData> inventory;
    public List<Integer> goal;
}
