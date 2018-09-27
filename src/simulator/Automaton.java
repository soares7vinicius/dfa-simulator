package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Automaton {
    
    private final List<Transition> transitions;
    private final List<String> endings;
    private final String begin;
    
    private String reading;

    public Automaton(List<String> list) {
        this.transitions = this.fillTransitions(list);
        this.begin = this.getBegin(list);
        this.endings = this.fillEndings(list);
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

    public String getReading() {
        return reading;
    }
}
