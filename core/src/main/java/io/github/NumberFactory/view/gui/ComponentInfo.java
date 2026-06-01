package io.github.NumberFactory.view.gui;

import io.github.NumberFactory.model.components.Component;
import io.github.NumberFactory.model.components.arithmetic.AddComponent;
import io.github.NumberFactory.model.components.arithmetic.DivideComponent;
import io.github.NumberFactory.model.components.arithmetic.ModuloComponent;
import io.github.NumberFactory.model.components.arithmetic.MultiplyComponent;
import io.github.NumberFactory.model.components.arithmetic.SubtractComponent;
import io.github.NumberFactory.model.components.logic.EqualsComponent;
import io.github.NumberFactory.model.components.logic.GreaterOrEqualComponent;
import io.github.NumberFactory.model.components.logic.GreaterThanComponent;
import io.github.NumberFactory.model.components.logic.LessOrEqualComponent;
import io.github.NumberFactory.model.components.logic.LessThanComponent;
import io.github.NumberFactory.model.components.logic.NotEqualsComponent;
import io.github.NumberFactory.model.components.utility.CopyComponent;
import io.github.NumberFactory.model.components.utility.DestroyerComponent;
import io.github.NumberFactory.model.components.utility.GeneratorComponent;
import io.github.NumberFactory.model.components.utility.NeutralComponent;
import io.github.NumberFactory.model.components.utility.OutputComponent;
import io.github.NumberFactory.model.components.utility.TransportComponent;

import java.util.List;


public final class ComponentInfo {

    public record Entry(Class<? extends Component> type, String name, String description) {}

    public record Category(String name, String description, List<Entry> entries) {}

    private ComponentInfo() {}

    public static List<Category> categories() {
        return List.of(
            new Category("Utility",
                "General-purpose components that move and manage numbers without doing any math. "
                    + "Generators introduce numbers into the machine, transport and copy components route and "
                    + "duplicate them, and the output delivers finished values to the goal. Destroyers throw "
                    + "values away and neutral walls simply block space so you can shape the layout.",
                utility()),
            new Category("Arithmetic",
                "Two-input components that combine numbers with a single math operation. Each one waits until "
                    + "both of its inputs hold a value, computes the result (addition, subtraction, multiplication, "
                    + "integer division or modulo) and sends it to a single output. They are the core of turning "
                    + "your source numbers into the values the goal asks for.",
                arithmetic()),
            new Category("Logic",
                "Two-input comparison components used to sort and branch number streams. Each compares its inputs "
                    + "a and b with a relation such as =, <, or >=, then routes the value depending on the result: "
                    + "if the comparison holds, value a goes to one output, otherwise it goes to the other. "
                    + "Use them to filter values or send them down different paths.",
                logic())
        );
    }

    public static List<Entry> utility() {
        return List.of(
            new Entry(GeneratorComponent.class, "Generator",
                "Emits its constant value once per run, then stops. Acts as the source of numbers for your machine."),
            new Entry(TransportComponent.class, "Transport",
                "Carries items from its inputs to its outputs without changing them. Can route up to two independent streams."),
            new Entry(DestroyerComponent.class, "Destroyer",
                "Consumes any value it receives and discards it. Has an input but no output."),
            new Entry(OutputComponent.class, "Output",
                "Collects every value that reaches it and delivers it to the level goal."),
            new Entry(CopyComponent.class, "Copy",
                "Duplicates each received value and sends an identical copy to both of its outputs."),
            new Entry(NeutralComponent.class, "Neutral (Wall)",
                "A passive obstacle with no ports. It never accepts or emits items - use it to block paths and shape the board.")
        );
    }

    public static List<Entry> arithmetic() {
        return List.of(
            new Entry(AddComponent.class, "Add", "Waits until both inputs a and b are filled, then outputs a + b."),
            new Entry(SubtractComponent.class, "Subtract", "Outputs a - b once both inputs are present."),
            new Entry(MultiplyComponent.class, "Multiply", "Outputs a * b once both inputs are present."),
            new Entry(DivideComponent.class, "Divide", "Outputs the integer division a / b once both inputs are present."),
            new Entry(ModuloComponent.class, "Modulo", "Outputs the remainder of a divided by b once both inputs are present.")
        );
    }

    public static List<Entry> logic() {
        return List.of(
            new Entry(EqualsComponent.class, "Equals (=)", routing("a = b")),
            new Entry(NotEqualsComponent.class, "Not equal (!=)", routing("a != b")),
            new Entry(LessThanComponent.class, "Less than (<)", routing("a < b")),
            new Entry(LessOrEqualComponent.class, "Less or equal (<=)", routing("a <= b")),
            new Entry(GreaterThanComponent.class, "Greater than (>)", routing("a > b")),
            new Entry(GreaterOrEqualComponent.class, "Greater or equal (>=)", routing("a >= b"))
        );
    }

    public static String portsSummary() {
        return "How the edges of every component connect to its neighbours, and why the blue ports take "
            + "priority over the red ones. Open for details.";
    }

    public static String portsDetails() {
        return String.join("\n\n",
            "Every component has four edges - north, east, south and west - and each edge carries one port. "
                + "While editing a component you cycle a port through: Closed -> Input A -> Input B -> "
                + "Output A -> Output B -> Closed.",
            "Ports come in two channels, shown by colour: channel A is BLUE and channel B is RED. So Input A "
                + "and Output A are blue, while Input B and Output B are red. Inputs receive values into the "
                + "component, outputs send values out of it.",
            "Two components only exchange a value across a shared edge when one side is an output and the "
                + "other side is a matching input. A closed edge passes nothing, and an output pointing at a "
                + "wall or empty space passes nothing either.",
            "Priority: the blue (A) ports are served before the red (B) ports. When a component has to choose "
                + "- for example a transport pushing two streams through a single output - the value on the "
                + "blue side moves first. The colour is your hint: blue = priority, red = secondary.");
    }

    private static String routing(String cond) {
        return "Takes two inputs a and b. If " + cond + " is true it routes value a to output A; "
            + "otherwise it routes a to output B.";
    }
}
