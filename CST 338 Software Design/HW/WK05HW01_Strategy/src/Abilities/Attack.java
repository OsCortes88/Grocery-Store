package Abilities;

import Abilities.Ability;
import Monsters.Monster;

public interface Attack extends Ability {
    Integer attack(Monster monster);
}
