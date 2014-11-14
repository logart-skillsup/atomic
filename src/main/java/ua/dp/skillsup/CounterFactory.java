package ua.dp.skillsup;

/**
 * @author Andrey Lomakin <a href="mailto:lomakin.andrey@gmail.com">Andrey Lomakin</a>
 * @since 03/11/14
 */
public class CounterFactory {
	public enum CounterType {
		ATOMIC,  MAGIC_3, MAGIC_7, CURSED_13, DC
	}

	public static Counter build(CounterType type) {
		switch (type) {
			case ATOMIC:
				return new AtomicCounter();
			case MAGIC_3:
				return new MyCounter(3);
			case MAGIC_7:
				return new MyCounter(7);
            case CURSED_13:
                return new MyCounter(13);
            case DC:
                return new MyCounterDynamic();
		}

		throw new IllegalArgumentException();
	}
}