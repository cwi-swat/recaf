package recaf.maybe;

public @interface AsOptional {

	@SuppressWarnings("rawtypes")
	public static final Class<MaybeExtension> builder = MaybeExtension.class;

	public Class<?> value();
}
