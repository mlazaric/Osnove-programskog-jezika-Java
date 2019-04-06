package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.java.custom.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.math.Vector2D;

public class LSystemBuilderImpl implements LSystemBuilder {

	private final Dictionary<Character, String> productions;
	private final Dictionary<Character, Command> commands;

	private double unitLength = 0.1;
	private double unitLengthDegreeScaler = 1;
	private Vector2D origin = new Vector2D(0, 0);
	private double angle = 0;
	private String axiom = "";

	public LSystemBuilderImpl() {
		productions = new Dictionary<>();
		commands = new Dictionary<>();
	}

	@Override
	public LSystem build() {
		return new LSystemImpl();
	}

	@Override
	public LSystemBuilder configureFromText(String[] args) {
		for (String line : args) {
			ConfigurationParser.parse(line, this);
		}

		return this;
	}

	@Override
	public LSystemBuilder registerCommand(char character, String command) {
		commands.put(character, CommandParser.parse(command));

		return this;
	}

	@Override
	public LSystemBuilder registerProduction(char character, String replacement) {
		productions.put(character, replacement);

		return this;
	}

	@Override
	public LSystemBuilder setAngle(double angle) {
		this.angle = angle;

		return this;
	}

	@Override
	public LSystemBuilder setAxiom(String axiom) {
		this.axiom = axiom;

		return this;
	}

	@Override
	public LSystemBuilder setOrigin(double x, double y) {
		origin = new Vector2D(x, y);

		return this;
	}

	@Override
	public LSystemBuilder setUnitLength(double unitLength) {
		this.unitLength = unitLength;

		return this;
	}

	@Override
	public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
		this.unitLengthDegreeScaler = unitLengthDegreeScaler;

		return this;
	}

	private class LSystemImpl implements LSystem {

		@Override
		public void draw(int level, Painter painter) {
			// TODO
		}

		@Override
		public String generate(int level) {
			String current = axiom;

			for (int iteration = 0; iteration < level; iteration++) {
				current = iterateOnce(current);
			}

			return current;
		}

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
