package Monsters;

import Abilities.Attack;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public abstract class  Monster {
    private Integer maxHP;
    private Integer hp;
    private Integer xp = 10;
    private HashMap<String, Integer> items;
    Integer agility = 10;
    Integer defense = 10;
    Integer strength = 10;
    Attack attack;

    public Monster(Integer maxHP, Integer xp, HashMap<String, Integer> items) {
        this.maxHP = maxHP;
        hp = this.maxHP;
        this.xp = xp;
        this.items = items;
    }

    public Integer attackTarget(Monster target) {
        Integer strike = attack.attack(target);
         target.takeDamage(strike);
        return strike;
    }

    boolean takeDamage(Integer damage) {
        if(damage > 0) {
            hp -= damage;
            System.out.println("The creature was hit for " + damage + " damage.");
            if(hp <= 0) {
                System.out.println("Oh no! The creature has perished");
                System.out.println(this.toString());
                return false;
            } else {
                System.out.println(this.toString());
                return true;
            }
        }
        System.out.println(this.toString());
        return false;
    }
    Integer getAttribute(Integer min, Integer max) {
        Random rand = new Random();
        if(min > max) {
            Integer temp = min;
            min = max;
            max = temp;
        }
        // returns a random number between min and max inclusive
        return rand.nextInt(max-min) + min;
    }

    public Integer getAgility() {
        return agility;
    }

    public Integer getDefense() {
        return defense;
    }

    public Integer getStrength() {
        return strength;
    }

    public Integer getMaxHP() {
        return maxHP;
    }

    public Integer getHp() {
        return hp;
    }

    public void setHp(Integer hp) {
        this.hp = hp;
    }

    public Integer getXp() {
        return xp;
    }

    public HashMap<String, Integer> getItems() {
        return items;
    }

    public void setItems(HashMap<String, Integer> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Monster monster = (Monster) o;

        if (getMaxHP() != null ? !getMaxHP().equals(monster.getMaxHP()) : monster.getMaxHP() != null) return false;
        if (getHp() != null ? !getHp().equals(monster.getHp()) : monster.getHp() != null) return false;
        if (getXp() != null ? !getXp().equals(monster.getXp()) : monster.getXp() != null) return false;
        if (getItems() != null ? !getItems().equals(monster.getItems()) : monster.getItems() != null) return false;
        if (getAgility() != null ? !getAgility().equals(monster.getAgility()) : monster.getAgility() != null)
            return false;
        if (getDefense() != null ? !getDefense().equals(monster.getDefense()) : monster.getDefense() != null)
            return false;
        if (getStrength() != null ? !getStrength().equals(monster.getStrength()) : monster.getStrength() != null)
            return false;
        return Objects.equals(attack, monster.attack);
    }

    @Override
    public int hashCode() {
        int result = getMaxHP() != null ? getMaxHP().hashCode() : 0;
        result = 31 * result + (getHp() != null ? getHp().hashCode() : 0);
        result = 31 * result + (getXp() != null ? getXp().hashCode() : 0);
        result = 31 * result + (getItems() != null ? getItems().hashCode() : 0);
        result = 31 * result + (getAgility() != null ? getAgility().hashCode() : 0);
        result = 31 * result + (getDefense() != null ? getDefense().hashCode() : 0);
        result = 31 * result + (getStrength() != null ? getStrength().hashCode() : 0);
        result = 31 * result + (attack != null ? attack.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return " hp=" + hp + "/" + maxHP;
    }
}
