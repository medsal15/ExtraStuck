package com.medsal15.items.shields;

//todo? use a component
public class ThornShield extends ESShield {
    // Damage dealt when attacked
    public float damage;

    public ThornShield(Properties properties, float damage) {
        super(properties);
        this.damage = damage;
    }
}
