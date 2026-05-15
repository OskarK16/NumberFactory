// ===============================================
// -----------------------------------------------
// ============= GEMINI-GENERATED ================
// -----------------------------------------------
// ===============================================

package io.github.NumberFactory.model.components.logic;

import io.github.NumberFactory.model.Machine;
import io.github.NumberFactory.model.board.Board;
import io.github.NumberFactory.model.components.GeneratorComponent;
import io.github.NumberFactory.model.components.OutputComponent;
import io.github.NumberFactory.model.components.logic.*;
import io.github.NumberFactory.utils.Directions;
import io.github.NumberFactory.utils.PortType;
import io.github.NumberFactory.model.components.TransportComponent;

public class LogicComponentsTest {

    public static void main(String[] args) {
        try {
            System.out.println("--- START ---");

            testEquals();
            testNotEquals();
            testGreaterThan();
            testGreaterOrEqual();
            testLessThan();
            testLessOrEqual();
            testDesynchronizedInputs();
            testCascadingLogic();

            System.out.println("--- SUCCESS ---");
        } catch (Throwable e) {
            System.err.println("\n=== ERROR ===");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void testEquals() {
        testLogicGate("Equals (True)", new EqualsComponent(), 5, 5, true);
        testLogicGate("Equals (False)", new EqualsComponent(), 5, 4, false);
    }

    private static void testNotEquals() {
        testLogicGate("NotEquals (True)", new NotEqualsComponent(), 8, 3, true);
        testLogicGate("NotEquals (False)", new NotEqualsComponent(), 7, 7, false);
    }

    private static void testGreaterThan() {
        // a > b
        testLogicGate("GreaterThan (True: 10 > 5)", new GreaterThanComponent(), 10, 5, true);
        testLogicGate("GreaterThan (False: 3 > 8)", new GreaterThanComponent(), 3, 8, false);
        testLogicGate("GreaterThan (False: 5 > 5)", new GreaterThanComponent(), 5, 5, false); // Równe nie są większe
    }

    private static void testGreaterOrEqual() {
        // a >= b
        testLogicGate("GreaterOrEqual (True: 10 >= 5)", new GreaterOrEqualComponent(), 10, 5, true);
        testLogicGate("GreaterOrEqual (True: 5 >= 5)", new GreaterOrEqualComponent(), 5, 5, true);
        testLogicGate("GreaterOrEqual (False: 3 >= 8)", new GreaterOrEqualComponent(), 3, 8, false);
    }

    private static void testLessThan() {
        // a < b
        testLogicGate("LessThan (True: 3 < 8)", new LessThanComponent(), 3, 8, true);
        testLogicGate("LessThan (False: 10 < 5)", new LessThanComponent(), 10, 5, false);
        testLogicGate("LessThan (False: 5 < 5)", new LessThanComponent(), 5, 5, false);
    }

    private static void testLessOrEqual() {
        // a <= b
        testLogicGate("LessOrEqual (True: 3 <= 8)", new LessOrEqualComponent(), 3, 8, true);
        testLogicGate("LessOrEqual (True: 5 <= 5)", new LessOrEqualComponent(), 5, 5, true);
        testLogicGate("LessOrEqual (False: 10 <= 5)", new LessOrEqualComponent(), 10, 5, false);
    }



    // (0,0)GenA(7) -> (1,0)T1 -> (2,0)T2 -> (3,0)T3 -> [SOUTH]
    //                                                     v
    // (4,1)OutTrue <- [EAST(True)] <- (3,1)EqualsLogic <-
    //                                                     ^
    //                                                  [NORTH]
    //                                                  (3,2)GenB(7)
    private static void testDesynchronizedInputs() {
        GeneratorComponent genA = new GeneratorComponent(7);
        TransportComponent t1 = new TransportComponent();
        TransportComponent t2 = new TransportComponent();
        TransportComponent t3 = new TransportComponent();
        GeneratorComponent genB = new GeneratorComponent(7);
        EqualsComponent logic = new EqualsComponent();
        OutputComponent outTrue = new OutputComponent();

        // Long pipeline for A
        genA.setPort(Directions.EAST, PortType.OUTPUT_A);
        genA.connect(Directions.EAST, t1);
        t1.setPort(Directions.WEST, PortType.INPUT_A);
        t1.setPort(Directions.EAST, PortType.OUTPUT_A);
        t1.connect(Directions.EAST, t2);
        t2.setPort(Directions.WEST, PortType.INPUT_A);
        t2.setPort(Directions.EAST, PortType.OUTPUT_A);
        t2.connect(Directions.EAST, t3);
        t3.setPort(Directions.WEST, PortType.INPUT_A);
        t3.setPort(Directions.SOUTH, PortType.OUTPUT_A); // Turns south into logic
        t3.connect(Directions.SOUTH, logic);

        // Short pipeline for B
        genB.setPort(Directions.NORTH, PortType.OUTPUT_A);
        genB.connect(Directions.NORTH, logic);

        // Logic Gate Configuration
        logic.setPort(Directions.NORTH, PortType.INPUT_A); // A comes from above (T3)
        logic.setPort(Directions.SOUTH, PortType.INPUT_B); // B comes from below (GenB)
        logic.setPort(Directions.EAST, PortType.OUTPUT_A);
        logic.connect(Directions.EAST, outTrue);

        outTrue.setPort(Directions.WEST, PortType.INPUT_A);

        Board board = new Board(5, 3);
        board.getCell(0, 0).setComponent(genA);
        board.getCell(1, 0).setComponent(t1);
        board.getCell(2, 0).setComponent(t2);
        board.getCell(3, 0).setComponent(t3);
        board.getCell(3, 2).setComponent(genB);
        board.getCell(3, 1).setComponent(logic);
        board.getCell(4, 1).setComponent(outTrue);

        Machine machine = new Machine(board);
        for (int i = 0; i < 10; i++) machine.tick();

        if (outTrue.getCollected().isEmpty()) {
            throw new AssertionError("testDesynchronizedInputs FAILED: Logic gate dropped the data due to desync!");
        }
        if (outTrue.getCollected().get(0).getValue() != 7) {
            throw new AssertionError("testDesynchronizedInputs FAILED: Incorrect output value.");
        }
        System.out.println("testDesynchronizedInputs: OK (Handled asynchronous arrival)");
    }


    // (0,1)GenA(5) -> [EAST] -> (1,1)Logic1( > ) -> [EAST(True)] -> (2,1)Logic2( > ) -> [EAST(True)]   -> (3,1)OutTrue
    //                              ^                                   ^             -> [SOUTH(False)] -> (2,2)OutFalse
    //                           [NORTH]                             [NORTH]
    //                         (1,0)GenB(3)                        (2,0)GenC(10)
    private static void testCascadingLogic() {
        GeneratorComponent genA = new GeneratorComponent(5);  // a = 5 (Ładunek)
        GeneratorComponent genB = new GeneratorComponent(3);  // b = 3 (Warunek 1)
        GeneratorComponent genC = new GeneratorComponent(10); // b = 10 (Warunek 2)

        GreaterThanComponent logic1 = new GreaterThanComponent();
        GreaterThanComponent logic2 = new GreaterThanComponent();

        OutputComponent outTrue = new OutputComponent();
        OutputComponent outFalse = new OutputComponent();

        // Podłączenie generatorów
        genA.setPort(Directions.EAST, PortType.OUTPUT_A);
        genA.connect(Directions.EAST, logic1);

        genB.setPort(Directions.SOUTH, PortType.OUTPUT_A);
        genB.connect(Directions.SOUTH, logic1);

        genC.setPort(Directions.SOUTH, PortType.OUTPUT_A);
        genC.connect(Directions.SOUTH, logic2);

        // Konfiguracja Logic 1 (5 > 3 == TRUE, przepuszcza 5 na Wschód)
        logic1.setPort(Directions.WEST, PortType.INPUT_A);
        logic1.setPort(Directions.NORTH, PortType.INPUT_B);
        logic1.setPort(Directions.EAST, PortType.OUTPUT_A);
        logic1.setPort(Directions.SOUTH, PortType.OUTPUT_B); // Zabezpieczenie portu
        logic1.connect(Directions.EAST, logic2);

        // Konfiguracja Logic 2 (5 > 10 == FALSE, zrzuca 5 na Południe)
        logic2.setPort(Directions.WEST, PortType.INPUT_A);
        logic2.setPort(Directions.NORTH, PortType.INPUT_B);
        logic2.setPort(Directions.EAST, PortType.OUTPUT_A);
        logic2.setPort(Directions.SOUTH, PortType.OUTPUT_B);
        logic2.connect(Directions.EAST, outTrue);
        logic2.connect(Directions.SOUTH, outFalse);

        outTrue.setPort(Directions.WEST, PortType.INPUT_A);
        outFalse.setPort(Directions.NORTH, PortType.INPUT_A);

        Board board = new Board(4, 3);
        board.getCell(0, 1).setComponent(genA);
        board.getCell(1, 0).setComponent(genB);
        board.getCell(2, 0).setComponent(genC);
        board.getCell(1, 1).setComponent(logic1);
        board.getCell(2, 1).setComponent(logic2);
        board.getCell(3, 1).setComponent(outTrue);
        board.getCell(2, 2).setComponent(outFalse);

        Machine machine = new Machine(board);
        for (int i = 0; i < 10; i++) machine.tick();

        if (outFalse.getCollected().isEmpty()) {
            throw new AssertionError("testCascadingLogic FAILED: Value did not reach the final False output.");
        }
        if (!outTrue.getCollected().isEmpty()) {
            throw new AssertionError("testCascadingLogic FAILED: Value incorrectly routed to the final True output.");
        }
        if (outFalse.getCollected().get(0).getValue() != 5) {
            throw new AssertionError("testCascadingLogic FAILED: Payload was modified during transit.");
        }
        System.out.println("testCascadingLogic: OK (Multi-stage routing successful)");
    }


    /**
     * Universal test method for logic gates.
     * Constructs a 3x3 intersection and routes data through it.
     * * @param testName Name of the test for logging purposes.
     * @param logic Instance of the logic component to test.
     * @param valA The payload value entering from the left (Input A).
     * @param valB The condition value entering from the top (Input B).
     * @param expectsTrue Whether the expected routing path is OUTPUT_A (True).
     */
    private static void testLogicGate(String testName, LogicComponent logic, int valA, int valB, boolean expectsTrue) {
        GeneratorComponent genA = new GeneratorComponent(valA);
        GeneratorComponent genB = new GeneratorComponent(valB);
        OutputComponent outTrue = new OutputComponent();
        OutputComponent outFalse = new OutputComponent();

        genA.setPort(Directions.EAST, PortType.OUTPUT_A);
        genA.connect(Directions.EAST, logic);

        genB.setPort(Directions.SOUTH, PortType.OUTPUT_A);
        genB.connect(Directions.SOUTH, logic);

        // Configure logic component ports
        logic.setPort(Directions.WEST, PortType.INPUT_A);
        logic.setPort(Directions.NORTH, PortType.INPUT_B);
        logic.setPort(Directions.EAST, PortType.OUTPUT_A);
        logic.setPort(Directions.SOUTH, PortType.OUTPUT_B);

        logic.connect(Directions.EAST, outTrue);
        logic.connect(Directions.SOUTH, outFalse);

        outTrue.setPort(Directions.WEST, PortType.INPUT_A);
        outFalse.setPort(Directions.NORTH, PortType.INPUT_A);

        Board board = new Board(3, 3);
        board.getCell(0, 1).setComponent(genA);
        board.getCell(1, 0).setComponent(genB);
        board.getCell(1, 1).setComponent(logic);
        board.getCell(2, 1).setComponent(outTrue);
        board.getCell(1, 2).setComponent(outFalse);

        Machine machine = new Machine(board);
        for (int i = 0; i < 10; i++) machine.tick();

        // Verification via assertions
        if (expectsTrue) {
            if (outTrue.getCollected().isEmpty()) {
                throw new AssertionError(testName + " FAILED: True output is empty. Expected: " + valA);
            }
            if (!outFalse.getCollected().isEmpty()) {
                throw new AssertionError(testName + " FAILED: Value was incorrectly routed to the False output.");
            }
            if (outTrue.getCollected().get(0).getValue() != valA) {
                throw new AssertionError(testName + " FAILED: Incorrect value in the True output.");
            }
            System.out.println(testName + ": OK (True Path)");
        } else {
            if (outFalse.getCollected().isEmpty()) {
                throw new AssertionError(testName + " FAILED: False output is empty. Expected: " + valA);
            }
            if (!outTrue.getCollected().isEmpty()) {
                throw new AssertionError(testName + " FAILED: Value was incorrectly routed to the True output.");
            }
            if (outFalse.getCollected().get(0).getValue() != valA) {
                throw new AssertionError(testName + " FAILED: Incorrect value in the False output.");
            }
            System.out.println(testName + ": OK (False Path)");
        }
    }
}
