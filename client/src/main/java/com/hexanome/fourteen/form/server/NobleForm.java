package com.hexanome.fourteen.form.server;

/**
 * Noble form.
 *
 * @param prestigePoints The amount of prestige points associated with the noble
 * @param cost           The amount of gem discounts needed to acquire the noble
 */
public record NobleForm(int prestigePoints, GemsForm cost) {
}
