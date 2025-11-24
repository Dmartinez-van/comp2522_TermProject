package ca.bcit.comp2522.mygame.generator.decorators;

public abstract class PointGeneratorDecorator implements PointGenerator
{
    protected final PointGenerator wrapped;

    protected PointGeneratorDecorator(PointGenerator wrapped)
    {
        this.wrapped = wrapped;
    }
}
