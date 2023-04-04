package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.ThreadLocalRandom;

class cluster{
    int id;
    Vector <Integer> v = new Vector<>();
}
class carry{
    int index;
    int value;
    int id;
}


public class Main {

    static int getMin(Vector<Integer> v) {
        int min = v.get(0);
        int ind = 0;
        for (int i = 0; i < v.size(); i++) {
            if (v.get(i) <= min) {
                min = v.get(i);
                ind = i;
            }
        }
        return ind;
    }

    public static void main(String[] args) throws IOException {

        //User Variables
        Scanner in = new Scanner(System.in);
        int numOfKeys =0, numofRowp =0,numOfRows =0 , numofiteration =0;

        System.out.println("Enter number of rows :");
        numofRowp = in.nextInt();
        numOfRows=(numofRowp*700)/100;

        System.out.println("Enter number of Keys :");
        numOfKeys = in.nextInt();

        //Main Variables
        Vector<Integer> keys = new Vector<>();
        Vector<Integer> mins = new Vector<>();
        Vector<Integer> newv = new Vector<>();

        ArrayList<cluster> All_clstrs = new ArrayList<>();
        ArrayList<cluster> Rdm_clstrs = new ArrayList<>();
        ArrayList<carry> calculations = new ArrayList<>();
        ArrayList<carry> calculations2 = new ArrayList<>();
        ArrayList<carry> calculations3 = new ArrayList<>();
        ArrayList <Vector<Integer>> Vec = new ArrayList<>();

        cluster c;


        // Fill the Clusters...
        File file = new File("E:\\DM_Ass2\\src\\test.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        for (int i = 0; i < numOfRows; i++) {
            String x = br.readLine();
            String[] xx = x.split("\\s+");
            c = new cluster();
            c.id = i + 1;
            for (int j = 0; j < xx.length; j++) {
                c.v.add(Integer.parseInt(xx[j]));
            }
            All_clstrs.add(c);
        }

        for (int i = 0; i < numOfKeys; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(10, numOfRows);
            keys.add(randomNum);
        }

        //Copy the main clusters
        for (int i = 0; i < numOfKeys; i++) {
            cluster cc = new cluster();
            cc.id = keys.get(i);
            for (int j = 0; j < All_clstrs.get(i).v.size(); j++)
                cc.v.add(All_clstrs.get(keys.get(i)).v.get(j));
            Rdm_clstrs.add(cc);
        }

        //Remove main clusters From all Clusters
        for (int i = 0; i < Rdm_clstrs.size(); i++) {
            All_clstrs.remove(Rdm_clstrs.get(i).id - 1);
        }

        for (int i = 0 ; i<All_clstrs.size() ; i++)
            All_clstrs.get(i).id = i;

        //Confirm the modulation
        for (int j = 0; j < Rdm_clstrs.size(); j++) {
            for (int h = 0; h < All_clstrs.size(); h++) {
                int sum = 0;
                carry ca = new carry();
                for (int x = 0; x < All_clstrs.get(h).v.size(); x++) {
                    sum += (int) Math.sqrt(Math.pow(All_clstrs.get(h).v.get(x) - Rdm_clstrs.get(j).v.get(x), 2));
                }
                ca.index = j;
                ca.value = sum;
                ca.id = All_clstrs.get(h).id;
                calculations.add(ca);
            }
        }

        int randomNum = ThreadLocalRandom.current().nextInt(5, 18);
        numofiteration=randomNum;

        for (int i = 0; i < All_clstrs.size(); i++) {
            int sum = 0;
            for (int j = 0; j < numOfKeys; j++) {
                mins.add(calculations.get(i + sum).value);
                sum += All_clstrs.size();
            }
            carry ca = new carry();
            ca.index = getMin(mins);
            ca.value = mins.get(getMin(mins));
            ca.id = calculations.get(i).id;
            calculations2.add(ca);
            while (!mins.isEmpty()) {
                mins.remove(0);
            }
        }


        //repeat from here
        int test=0;
        while (test ==0){

            for (int h=0 ; h<numOfKeys ; h++) {
                for (int i = 0; i < All_clstrs.get(0).v.size(); i++) {
                    int sum = 0 , count =0;
                    for (int j = 0; j < All_clstrs.size(); j++) {
                        if(calculations2.get(j).index == h){
                            sum += All_clstrs.get(calculations2.get(j).id).v.get(i);
                            count++ ;
                        }
                    }
                    newv.add(sum/count);
                }
                Vec.add(newv);
                newv = new Vector<>();
            }

            while (!Rdm_clstrs.isEmpty())
                Rdm_clstrs.remove(0);

            for (int i =0 ; i<numOfKeys ; i++){
                c = new cluster();
                Rdm_clstrs.add(c);
            }

            for (int j = 0; j < numOfKeys; j++){
                Rdm_clstrs.get(j).id=j;
                for (int i = 0; i < Vec.get(j).size(); i++){
                    Rdm_clstrs.get(j).v.add(Vec.get(j).get(i));
                }
            }

            calculations = new ArrayList<>();

            //calc after get avg
            for (int j = 0; j < Rdm_clstrs.size(); j++) {
                for (int h = 0; h < All_clstrs.size(); h++) {
                    int sum = 0;
                    carry ca = new carry();
                    for (int x = 0; x < All_clstrs.get(h).v.size(); x++) {
                        sum += (int) Math.sqrt(Math.pow(All_clstrs.get(h).v.get(x) - Rdm_clstrs.get(j).v.get(x), 2));
                    }
                    ca.index = j;
                    ca.value = sum;
                    ca.id = All_clstrs.get(h).id;
                    calculations.add(ca);
                }
            }


            for (int i = 0; i < All_clstrs.size(); i++) {
                int sum = 0;
                for (int j = 0; j < numOfKeys; j++) {
                    mins.add(calculations.get(i + sum).value);
                    sum += All_clstrs.size();
                }
                carry ca = new carry();
                ca.index = getMin(mins);
                ca.value = mins.get(getMin(mins));
                ca.id = calculations.get(i).id;
                calculations3.add(ca);
                while (!mins.isEmpty()) {
                    mins.remove(0);
                }
            }

            for (int i=0 ; i<calculations2.size() ; i++){
                if(calculations2.get(i).id == calculations3.get(i).id){
                    if(calculations2.get(i).index == calculations3.get(i).index)
                        test++;
                }
                else{
                    calculations2 = calculations3;
                    calculations3 = new ArrayList<>();
                }
            }
            numofiteration++;
        }


        //Last Statics
        newv = new Vector<>();
        Vec = new ArrayList<>();

        for (int i=0 ; i<numOfKeys ; i++){
            for (int j = 0; j < calculations3.size(); j++) {
                if(calculations3.get(j).index == i){
                    newv.add(calculations3.get(j).id);
                }
            }
            Vec.add(newv);
            newv = new Vector<>();
        }
        for (int i=0 ; i<Vec.size() ; i++){
            System.out.println("Cluster number "+(i+1)+" have : " + Vec.get(i));
        }
        System.out.println("---------------------------------------------");
        System.out.println("Number of iterations is : " + numofiteration);

    }
}