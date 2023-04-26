import java.sql.SQLOutput;

public class Lab00 {
    private float score = 42;
    private String name = "Dr.C";
    public static void main(String[] args) {
        System.out.println("Hello There!");
        Lab00 lab00 = new Lab00();
        System.out.println(lab00);

    }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Lab00{" +
                "score=" + score +
                ", name='" + name + '\'' +
                '}';
    }

}
