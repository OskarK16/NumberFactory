package io.github.NumberFactory;

import io.github.NumberFactory.model.Machine;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.components.utility.GeneratorComponent;
import io.github.NumberFactory.model.components.utility.OutputComponent;
import io.github.NumberFactory.model.components.utility.TransportComponent;
import io.github.NumberFactory.model.components.arithmetic.AddComponent;
import io.github.NumberFactory.model.components.arithmetic.DivideComponent;
import io.github.NumberFactory.model.components.arithmetic.MultiplyComponent;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;

public class MachineTest {

    public static void main(String[] args) {
        testAddition();
        testChainedOperations();
        testTransport();
        testDivisionByZero();
        System.out.println("All tests passed.");
    }

    // (0,0)gen5 -> (1,0)t1 -> (2,0)adder -> (3,0)t3 -> (4,0)out
    //                           ^
    //                         (2,1)t2
    //                           ^
    //                         (2,2)gen7
    private static void testAddition() {
        GeneratorComponent gen5  = new GeneratorComponent(5);
        TransportComponent t1    = new TransportComponent();
        GeneratorComponent gen7  = new GeneratorComponent(7);
        TransportComponent t2    = new TransportComponent();
        AddComponent       adder = new AddComponent();
        TransportComponent t3    = new TransportComponent();
        OutputComponent    out   = new OutputComponent();

        gen5.setPort(Directions.EAST, PortType.OUTPUT_A);
        gen5.connect(Directions.EAST, t1);

        t1.setPort(Directions.WEST,  PortType.INPUT_A);
        t1.setPort(Directions.EAST,  PortType.OUTPUT_A);
        t1.connect(Directions.EAST, adder);

        gen7.setPort(Directions.NORTH, PortType.OUTPUT_A);
        gen7.connect(Directions.NORTH, t2);

        t2.setPort(Directions.SOUTH, PortType.INPUT_A);
        t2.setPort(Directions.NORTH, PortType.OUTPUT_A);
        t2.connect(Directions.NORTH, adder);

        adder.setPort(Directions.WEST,  PortType.INPUT_A);
        adder.setPort(Directions.SOUTH, PortType.INPUT_B);
        adder.setPort(Directions.EAST,  PortType.OUTPUT_A);
        adder.connect(Directions.EAST, t3);

        t3.setPort(Directions.WEST, PortType.INPUT_A);
        t3.setPort(Directions.EAST, PortType.OUTPUT_A);
        t3.connect(Directions.EAST, out);

        out.setPort(Directions.WEST, PortType.INPUT_A);

        Board board = new Board(5, 3);
        board.getCell(0, 0).setComponent(gen5);
        board.getCell(1, 0).setComponent(t1);
        board.getCell(2, 0).setComponent(adder);
        board.getCell(2, 1).setComponent(t2);
        board.getCell(2, 2).setComponent(gen7);
        board.getCell(3, 0).setComponent(t3);
        board.getCell(4, 0).setComponent(out);

        Machine machine = new Machine(board);
        runUntil(machine, out, 10);

        check("testAddition", 12, out.getCollected().get(0).getValue());
    }

    // gen3 -> t_a -> adder -> t_sum -> multiplier -> t_out -> out
    //                 ^                    ^
    //               t_b                  t_c
    //                ^                    ^
    //               gen4                gen2
    private static void testChainedOperations() {
        GeneratorComponent gen3  = new GeneratorComponent(3);
        GeneratorComponent gen4  = new GeneratorComponent(4);
        GeneratorComponent gen2  = new GeneratorComponent(2);
        TransportComponent ta    = new TransportComponent();
        TransportComponent tb    = new TransportComponent();
        TransportComponent tc    = new TransportComponent();
        TransportComponent tSum  = new TransportComponent();
        TransportComponent tOut  = new TransportComponent();
        AddComponent       adder = new AddComponent();
        MultiplyComponent  mult  = new MultiplyComponent();
        OutputComponent    out   = new OutputComponent();

        gen3.setPort(Directions.EAST, PortType.OUTPUT_A);
        gen3.connect(Directions.EAST, ta);

        ta.setPort(Directions.WEST, PortType.INPUT_A);
        ta.setPort(Directions.EAST, PortType.OUTPUT_A);
        ta.connect(Directions.EAST, adder);

        gen4.setPort(Directions.NORTH, PortType.OUTPUT_A);
        gen4.connect(Directions.NORTH, tb);

        tb.setPort(Directions.SOUTH, PortType.INPUT_A);
        tb.setPort(Directions.NORTH, PortType.OUTPUT_A);
        tb.connect(Directions.NORTH, adder);

        adder.setPort(Directions.WEST,  PortType.INPUT_A);
        adder.setPort(Directions.SOUTH, PortType.INPUT_B);
        adder.setPort(Directions.EAST,  PortType.OUTPUT_A);
        adder.connect(Directions.EAST, tSum);

        tSum.setPort(Directions.WEST, PortType.INPUT_A);
        tSum.setPort(Directions.EAST, PortType.OUTPUT_A);
        tSum.connect(Directions.EAST, mult);

        gen2.setPort(Directions.NORTH, PortType.OUTPUT_A);
        gen2.connect(Directions.NORTH, tc);

        tc.setPort(Directions.SOUTH, PortType.INPUT_A);
        tc.setPort(Directions.NORTH, PortType.OUTPUT_A);
        tc.connect(Directions.NORTH, mult);

        mult.setPort(Directions.WEST,  PortType.INPUT_A);
        mult.setPort(Directions.SOUTH, PortType.INPUT_B);
        mult.setPort(Directions.EAST,  PortType.OUTPUT_A);
        mult.connect(Directions.EAST, tOut);

        tOut.setPort(Directions.WEST, PortType.INPUT_A);
        tOut.setPort(Directions.EAST, PortType.OUTPUT_A);
        tOut.connect(Directions.EAST, out);

        out.setPort(Directions.WEST, PortType.INPUT_A);

        Board board = new Board(7, 3);
        board.getCell(0, 0).setComponent(gen3);
        board.getCell(1, 0).setComponent(ta);
        board.getCell(2, 0).setComponent(adder);
        board.getCell(2, 1).setComponent(tb);
        board.getCell(2, 2).setComponent(gen4);
        board.getCell(3, 0).setComponent(tSum);
        board.getCell(4, 0).setComponent(mult);
        board.getCell(4, 1).setComponent(tc);
        board.getCell(4, 2).setComponent(gen2);
        board.getCell(5, 0).setComponent(tOut);
        board.getCell(6, 0).setComponent(out);

        Machine machine = new Machine(board);
        runUntil(machine, out, 15);

        check("testChainedOperations", 14, out.getCollected().get(0).getValue());
    }

    // (0,0)gen -> (1,0)t1 -> (2,0)t2 -> (3,0)out
    private static void testTransport() {
        GeneratorComponent gen = new GeneratorComponent(9);
        TransportComponent t1  = new TransportComponent();
        TransportComponent t2  = new TransportComponent();
        OutputComponent    out = new OutputComponent();

        gen.setPort(Directions.EAST, PortType.OUTPUT_A);
        gen.connect(Directions.EAST, t1);

        t1.setPort(Directions.WEST, PortType.INPUT_A);
        t1.setPort(Directions.EAST, PortType.OUTPUT_A);
        t1.connect(Directions.EAST, t2);

        t2.setPort(Directions.WEST, PortType.INPUT_A);
        t2.setPort(Directions.EAST, PortType.OUTPUT_A);
        t2.connect(Directions.EAST, out);

        out.setPort(Directions.WEST, PortType.INPUT_A);

        Board board = new Board(4, 1);
        board.getCell(0, 0).setComponent(gen);
        board.getCell(1, 0).setComponent(t1);
        board.getCell(2, 0).setComponent(t2);
        board.getCell(3, 0).setComponent(out);

        Machine machine = new Machine(board);
        runUntil(machine, out, 10);

        check("testTransport", 9, out.getCollected().get(0).getValue());
    }

    // 5 / 0 — output should stay empty, no crash
    private static void testDivisionByZero() {
        GeneratorComponent gen5    = new GeneratorComponent(5);
        GeneratorComponent gen0    = new GeneratorComponent(0);
        TransportComponent ta      = new TransportComponent();
        TransportComponent tb      = new TransportComponent();
        TransportComponent tOut    = new TransportComponent();
        DivideComponent    divider = new DivideComponent();
        OutputComponent    out     = new OutputComponent();

        gen5.setPort(Directions.EAST, PortType.OUTPUT_A);
        gen5.connect(Directions.EAST, ta);

        ta.setPort(Directions.WEST, PortType.INPUT_A);
        ta.setPort(Directions.EAST, PortType.OUTPUT_A);
        ta.connect(Directions.EAST, divider);

        gen0.setPort(Directions.NORTH, PortType.OUTPUT_A);
        gen0.connect(Directions.NORTH, tb);

        tb.setPort(Directions.SOUTH, PortType.INPUT_A);
        tb.setPort(Directions.NORTH, PortType.OUTPUT_A);
        tb.connect(Directions.NORTH, divider);

        divider.setPort(Directions.WEST,  PortType.INPUT_A);
        divider.setPort(Directions.SOUTH, PortType.INPUT_B);
        divider.setPort(Directions.EAST,  PortType.OUTPUT_A);
        divider.connect(Directions.EAST, tOut);

        tOut.setPort(Directions.WEST, PortType.INPUT_A);
        tOut.setPort(Directions.EAST, PortType.OUTPUT_A);
        tOut.connect(Directions.EAST, out);

        out.setPort(Directions.WEST, PortType.INPUT_A);

        Board board = new Board(5, 3);
        board.getCell(0, 0).setComponent(gen5);
        board.getCell(1, 0).setComponent(ta);
        board.getCell(2, 0).setComponent(divider);
        board.getCell(2, 1).setComponent(tb);
        board.getCell(2, 2).setComponent(gen0);
        board.getCell(3, 0).setComponent(tOut);
        board.getCell(4, 0).setComponent(out);

        Machine machine = new Machine(board);
        for (int i = 0; i < 10; i++) machine.tick();

        if (!out.getCollected().isEmpty())
            throw new AssertionError("testDivisionByZero FAILED: expected no output, got "
                + out.getCollected().get(0).getValue());
        System.out.println("testDivisionByZero: OK");
    }



    private static void runUntil(Machine machine, OutputComponent out, int maxTicks) {
        for (int i = 0; i < maxTicks && out.getCollected().isEmpty(); i++) {
            machine.tick();
        }
    }

    private static void check(String name, int expected, int actual) {
        if (expected != actual)
            throw new AssertionError(name + " FAILED: expected " + expected + ", got " + actual);
        System.out.println(name + ": OK (" + actual + ")");
    }
}
