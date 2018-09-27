package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Automaton {
    
    private final List<Transition> transitions;
    private final List<String> endings;
    private final String begin;
    
    private String reading;
    
    private final List<String> alphabet;
    private final List<String> states;

    public Automaton(List<String> list) {
        this.transitions = this.fillTransitions(list);
        this.begin = this.getBegin(list);
        this.endings = this.fillEndings(list);
        this.alphabet = this.fillAlphabet(this.transitions);
        this.states = this.fillStates(this.transitions);
        this.reading = new String();
    }
    
    public boolean verifySentence(String s){
        
        String currentState = this.begin;
        String currentSymbol;
        
        this.reading = new String();
        this.reading += "<html>";
        this.reading += s + "<br>";
        
        for (int i = 0; i < s.length(); i++) {
            currentSymbol = s.charAt(i) + "";

            for (Transition transition : this.transitions) {
                        
                if (currentSymbol.equals(transition.getSymbol())
                        && currentState.equals(transition.getFrom())) {
                    
                    this.reading += this.formatReading(transition, s, i);
                    
                    currentState = transition.getTo();
                    break;
                }
            }
        }
        
        this.reading += "</html>";
        return this.endings.contains(currentState);
    }
    
    private String formatReading(Transition transition, String s, int i) {
        return String.format("%s<font color='blue'>q%s</font><font color='red'>%s</font>%s<br>",
                s.substring(0,i), transition.getFrom(), transition.getSymbol(), s.substring(i+1, s.length()));
    }
    
    public boolean validateDFA() {
        int n = this.states.size();
        int m = this.alphabet.size();
        int mxValidation[][] = new int[n][m];
        
        for(Transition t : this.transitions) {
            int idxSymbol = this.alphabet.indexOf(t.getSymbol());
            int idxState = this.states.indexOf(t.getFrom());
            
            if(mxValidation[idxState][idxSymbol] == 0)
                mxValidation[idxState][idxSymbol] = 1;
            else
                return false;
        }
        
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                if(mxValidation[i][j] == 0)
                    return false;
            }
        }
        
        return true;
    }
    
    private List<String> fillAlphabet(List<Transition> transitions)  {
        Set<String> s = new HashSet<>();
        
        transitions.forEach((t) -> {
            s.add(t.getSymbol());
        });
        
        return new ArrayList<>(s);
    }
    
    private List<String> fillStates(List<Transition> transitions) {
        Set<String> s = new HashSet<>();
        
        transitions.forEach((t) -> {
            s.add(t.getFrom());
        });
        
        return new ArrayList<>(s);
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

    public String getReading() {
        return reading;
    }
}
