package dev.alchemyredirected.customEffects;

public record CustomEffect<T extends EffectType>(T effect,int amplifier,int duration)
{

}
