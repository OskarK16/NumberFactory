package io.github.NumberFactory.controller;

import io.github.NumberFactory.utils.Directions;

public interface ScreenToCellMapper {

    int[] toCell(int screenX, int screenY);

    Directions toPortDirection(int screenX, int screenY);
}
