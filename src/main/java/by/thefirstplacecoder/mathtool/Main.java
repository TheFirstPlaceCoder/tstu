package by.thefirstplacecoder.mathtool;

public class Main {

	public static void main(String[] args) {
		int exitCode = new MathToolApplication().run(args);

		if (exitCode != 0) {
			System.exit(exitCode);
		}
	}
}
