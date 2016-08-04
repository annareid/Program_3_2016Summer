//Modified by Anna Koldunova (Reid) on 08/03/2016

package edu.uwb.css;

import java.util.concurrent.locks.Condition;

public class DiningPhilosophers{
    public int nPhil;
    public DiningState[] state;
    public Object[] monitors;

    // This will let all the tests run (and fail)
    // You'll want to remove it once you actually create an array :)
    //int nPhil;

    public DiningPhilosophers(int nPhilosophers) {
        this.nPhil =  nPhilosophers;
        this.state = new DiningState[nPhil];
        this.monitors = new Object[nPhil];

        for (int i = 0; i < nPhil; i++) {
            this.monitors[i] = new Object();
            this.state[i] = DiningState.THINKING;

        }
    }

    //this philosopher tries to take forks
    public void takeForks(int i) {
        Main.TPrint( "TakeForks:   i=" + i);
        this.state[i] = DiningState.HUNGRY;     //change state to Hungry
        Main.TPrint( "Philosopher:   i=" + i +  " is hungry.");

        test(i);                                //check if this philosopher can change his state to Eating

        if(this.state[i] != DiningState.EATING){    //if this philosoper is not eating, it has to wait
            try{
                Main.TPrint( "Philosopher:   i=" + i + " is waiting");
                synchronized(this.monitors[i]) {
                    this.monitors[i].wait();
                }
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    //this philosopher returns forks
    public void returnForks(int i) {
        Main.TPrint( "returnForks:   i=" + i );
        this.state[i] = DiningState.THINKING;
        Main.TPrint( "Philosopher is thinking:   i=" + i);

        //check if left or right neighbours can eat now
        test((i + nPhil - 1) % nPhil);
        test((i + 1) % nPhil);
    }

    private void test(int i){
       if(
               this.state[(i + nPhil - 1) % nPhil] != DiningState.EATING &&     //right philosopher is not eating
               this.state[i] == DiningState.HUNGRY &&
               this.state[(i +1) % nPhil] != DiningState.EATING                 //left philosopher is not eating
         ){
           this.state[i] = DiningState.EATING;                                  //change this philosopher's state to Eating
           Main.TPrint( "Philosopher:   i=" + i +  " is eating.");

           synchronized (this.monitors[i]){
               this.monitors[i].notify();                                        //wake up all other threads
           }
       }
    }

    public int numPhilosophers() {
        return nPhil;
    }

    public DiningState getDiningState(int i) {
        return state[i];
    }
}
