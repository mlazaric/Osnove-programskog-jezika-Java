package hr.fer.zemris.lsystems.impl;

import java.awt.Color;
import java.util.Objects;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.math.Vector2D;

/**
 * Factory object for a Lindermayer system implementation.
 *
 * @author Marko Lazarić
 *
 */
public class LSystemBuilderImpl implements LSystemBuilder {

	/**
	 * The productions to apply to {@link #axiom}.
	 */
	private final Dictionary<Character, String> productions;

	/**
	 * The character to command mapping.
	 */
	private final Dictionary<Character, Command> commands;

	/**
	 * The unit length.
	 */
	private double unitLength = 0.1;

	/**
	 * The unit length degree scaler.
	 */
	private double unitLengthDegreeScaler = 1;

	/**
	 * The origin of the system.
	 */
	private Vector2D origin = new Vector2D(0, 0);

	/**
	 * The starting angle of the system.
	 */
	private double angle = 0;

	/**
	 * The axiom of the system.
	 */
	private String axiom = "";

	/**
	 * Creates a new {@link LSystemBuilderImpl} with the default values.
	 */
	public LSystemBuilderImpl() {
		productions = new Dictionary<>();
		commands = new Dictionary<>();
	}

	/**
	 * Creates a new {@link LSystemImpl} object with the stored values.
	 *
	 * @return the newly created {@link LSystemImpl}
	 */
	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	/**
	 * Configures the parameters of the {@link LSystemBuilderImpl} from input text.
	 *
	 * @param args the configuration for the {@link LSystemBuilderImpl}
	 * @return {@code this}
	 *
	 * @throws NullPointerException if {@code args} is {@code null}
	 */
	@Override
	public LSystemBuilder configureFromText(String[] args) {
		Objects.requireNonNull(args, "Cannot configure LSystemBuilderImpl from null.");

		for (String line : args) {
			ConfigurationParser.parse(line, this);
		}

		return this;
	}

	/**
	 * Registers a character to command mapping.
	 *
	 * @param character the character that represents the command
	 * @param command the command it represents
	 * @return {@code this}
	 *
	 * @throws NullPointerException if {@code command} is {@code null}
	 *
	 * @see CommandParser#parse(String)
	 */
	@Override
	public LSystemBuilder registerCommand(char character, String command) {
		Objects.requireNonNull(command, "Cannot register null command.");

		commands.put(character, CommandParser.parse(command));

		return this;
	}

	/**
	 * Registers a production.
	 *
	 * @param character the character to be replaced
	 * @param replacement the replacement for the character
	 * @return {@code this}
	 *
	 * @throws NullPointerException if {@code replacement} is {@code null}
	 */
	@Override
	public LSystemBuilder registerProduction(char character, String replacement) {
		Objects.requireNonNull(replacement, "Cannot replace character with null.");

		productions.put(character, replacement);

		return this;
	}

	/**
	 * Sets the {@link #angle} to the given argument.
	 *
	 * @param angle the new angle
	 * @return {@code this}
	 */
	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;

		return this;
	}

	/**
	 * Sets the {@link #axiom} to the given argument.
	 *
	 * @param axiom the new axiom
	 * @return {@code this}
	 *
	 * @throws NullPointerException if {@code axiom} is {@code null}
	 */
	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = Objects.requireNonNull(axiom, "Axiom cannot be null.");

		return this;
	}

	/**
	 * Sets the {@link #origin} to the given arguments.
	 *
	 * @param x the x coordinate of the new origin
	 * @param y the y coordinate of the new origin
	 * @return {@code this}
	 */
	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		origin = new Vector2D(x, y);

		return this;
	}

	/**
	 * Sets the {@link #unitLength} to the given argument.
	 *
	 * @param unitLength the new unit length
	 * @return {@code this}
	 */
	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;

		return this;
	}

	/**
	 * Sets the {@link #unitLengthDegreeScaler} to the given argument.
	 *
	 * @param unitLengthDegreeScaler the new unit length degree scaler
	 * @return {@code this}
	 */
	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;

		return this;
	}

	/**
	 * Models a simple Lindermayer system.
	 *
	 * @author Marko Lazarić
	 *
	 */
	private class LSystemImpl implements LSystem {

		/**
		 * Generates and draws a Lindermayer system of level {@code level}.
		 *
		 * @param level the number of times to apply the productions to the axiom
		 * @param painter the painter used for drawing the generated system
		 *
		 * @throws NullPointerException if {@code painter} is {@code null}
		 * @throws IllegalArgumentException if {@code level} is negative
		 */
		@Override
		public void draw(int level, Painter painter) {
			Objects.requireNonNull(painter, "Cannot paint using null painter");

			Context context = new Context();
			context.pushState(new TurtleState(origin.copy(), new Vector2D(1, 0).rotated(angle), Color.BLACK, unitLength * Math.pow(unitLengthDegreeScaler, level)));

			String generated = generate(level);
			char[] commandCharacters = generated.toCharArray();

			for (char command : commandCharacters) {
				Command com = commands.get(command);

				if (com != null) {
					com.execute(context, painter);
				}
			}
		}

		/**
		 * Applies the productions {@code level - 1} number of times and returns the result.
		 * For {@code level = 0}, it returns {@link LSystemBuilderImpl#axiom}.
		 * For {@code level = 1}, it returns the result of applying the {@link LSystemBuilderImpl#productions} on the {@link LSystemBuilderImpl#axiom} once.
		 *
		 * @param level the number of times to apply the productions to {@link LSystemBuilderImpl#axiom}
		 * @return the result of applying the productions {@code level - 1} times to the {@link LSystemBuilderImpl#axiom}
		 *
		 * @throws IllegalArgumentException if {@code level} is negative
		 */
		@Override
		public String generate(int level) {
			if (level < 0) {
				throw new IllegalArgumentException("Level cannot be negative.");
			}

			String current = axiom;

			for (int iteration = 0; iteration < level; iteration++) {
				current = iterateOnce(current);
			}

			return current;
		}

		/**
		 * Applies the productions to the string <b>once</b> and returns the result.
		 *
		 * @param lSystem the Lindermayer system to iterate
		 * @return the result of applying the productions to the argument
		 */
		private String iterateOnce(String lSystem) {
			char[] array = lSystem.toCharArray();
			StringBuilder sb = new StringBuilder();

			for (char character : array) {
				String production = productions.get(character);

				if (production == null) {
					sb.append(character);
				}
				else {
					sb.append(production);
				}
			}

			return sb.toString();
		}

	}

}
