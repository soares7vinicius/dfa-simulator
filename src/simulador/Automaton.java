/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author vinicius
 */
public class Automaton {
    
    private final List<Transition> transitions;
    private final List<String> endings;
    private final String begin;

    public Automaton(List<String> list) {
        this.transitions = this.fillTransitions(list);
        this.begin = this.getBegin(list);
        this.endings = this.fillEndings(list);
    }
    
    public boolean validateDFA() {
        for (int i = 0; i < transitions.size() - 1; i++) {
            Transition a = transitions.get(i);
            for (int j = i + 1; j < transitions.size(); j++) {
                Transition b = transitions.get(j);
                if ((a.getFrom().equals(b.getFrom())) && 
                        (a.getSymbol().equals(b.getSymbol()))) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private List<Transition> fillTransitions(List<String> list) {
        List<Transition> l = new ArrayList<>();
        for(int i = 0; i < list.size() - 2; i++) {
            String[] values = list.get(i).split(",");
            l.add(new Transition(values[0], values[1], values[2]));
        }
        return l;
    }
    
    private String getBegin(List<String> list) {
        String[] b = list.get(list.size() - 2).split("=");
        return b[1];
    }
    
    private List<String> fillEndings(List<String> list) {
        List<String> l = new ArrayList<>();
        String[] e = list.get(list.size() - 1).split("=")[1].split(",");
        l.addAll(Arrays.asList(e));
        
        return l;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public List<String> getEndings() {
        return endings;
    }

    public String getBegin() {
        return begin;
    }

    
}
