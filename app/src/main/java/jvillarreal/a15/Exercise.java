package jvillarreal.a15;

import java.io.Serializable;

/**
 * Class that represents a single exercise.
 */
public class Exercise implements Serializable {
    private int totalSets = 1;
    private String exerciseName = "Empty";
    private int restSeconds = 1;
    private int currentSet = 1;
    private float weight = 0;

    /**
     * Default constructor
     */
    Exercise(){
        totalSets = 1;
        exerciseName = "Empty";
        restSeconds = 60;
    }

    /**
     * Create an exercise with defined parameters
     * @param totalSets How many sets the exercise has
     * @param exerciseName The name of the exercise
     * @param restSeconds How long the rest interval is for the exercise
     */
    Exercise(int totalSets, String exerciseName, int restSeconds){
        this.totalSets = totalSets;
        this.exerciseName = exerciseName;
        this.restSeconds = restSeconds;
    }

    /**
     * Increments the set if it is not greater than the total sets
     */
    public void incrementCurrentSet(){
        if (currentSet < totalSets ){
            currentSet++;
        }
    }

    /**
     * Gets the current set that you are on in the exercise
     * @return the current set
     */
    public int getCurrentSet(){
        return currentSet;
    }

    /**
     * Reset the sets back to 1
     */
    public void resetCurrentSets(){
        this.currentSet = 1;
    }

    /**
     * The weight you define for the exercise
     * @param weight float that represents weight of an exercise
     */
    public void setWeight(float weight){
        if(weight<0){
            this.weight = 0;
        }
        this.weight = weight;
    }

    /**
     * Get the weight for the exercise
     * @return float representing the weight
     */
    public float getWeight(){
        return weight;
    }

    /**
     * Get the total sets of this exercise
     * @return integer representing the total sets
     */
    public int getTotalSets() {
        return totalSets;
    }

    /**
     * Set the total sets for the workout
     * @param totalSets integer to define the total sets in this workout
     */
    public void setTotalSets(int totalSets) {
        this.totalSets = totalSets;
    }

    /**
     * Get the exercise current name
     * @return
     */
    public String getExerciseName() {
        return exerciseName;
    }

    /**
     * Set the exercise name
     * @param exerciseName String representing the exercise name
     */
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    /**
     * Get the time of the rest interval
     * @return integer representing seconds of rest
     */
    public int getRestSeconds() {
        return restSeconds;
    }

    /**
     * Set the rest interval for this exercise
     * @param restSeconds integer that represents rest interval in seconds
     */
    public void setRestSeconds(int restSeconds) {
        this.restSeconds = restSeconds;
    }

    /**
     * To string to display in list view and for debugging
     * @return A string that contains the attributes of the current exercise
     */
    @Override
    public String toString(){
        return String.format("%s,  Sets:  %d", this.exerciseName, this.totalSets);
    }


}
