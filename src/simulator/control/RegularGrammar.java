package simulator.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RegularGrammar {
    private final List<String> terminals;
    private Map<String, List<String>> p;
    
    private final String[] alphabet = 
            "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
    private List<String> combinations; /* S, A, B, ... Z, AA, AB, ... ZZ*/
    
    public RegularGrammar(Automaton a) {
        this.makeCombinations();
        
        this.terminals = a.getAlphabet();
        this.makeRG(a);
    }
    
    private void makeCombinations() {
        this.combinations = new ArrayList<>();
        this.combinations.add("S");
        this.combinations.addAll(Arrays.asList(this.alphabet));
        for(String a : alphabet) {
            for(String b : alphabet) {
                this.combinations.add(a+b);
            }
        }
    }
    
    private void makeRG(Automaton a) {
        this.p = new LinkedHashMap<>();
        
        for(int i = 0; i < a.getStatesCount(); i++) {
            this.p.put(this.combinations.get(i), new ArrayList<>());
        }
        
        for(Transition t : a.getTransitions()) {
            int from = Integer.parseInt(t.getFrom());
            int to = Integer.parseInt(t.getTo());
            String symbol = t.getSymbol();
            
            List<String> l = this.p.get(combinations.get(from));
            l.add(symbol+combinations.get(to));
        }
        
        for(String e : a.getEndings()) {
            int ending = Integer.parseInt(e);
            List<String> l = this.p.get(combinations.get(ending));
            l.add("\u03B5"); //epsilon
        }
    }

    public String getGrammar() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<strong>G = (V<sub>T</sub>, V<sub>N</sub>,"
                + " S, P)</strong>");
        
        sb.append("<br><br>");
        sb.append("<strong>V<sub>T</sub></strong> = {");
        for(String a : terminals) sb.append(a).append(", ");
        sb.delete(sb.length()-2, sb.length()+1);
        sb.append("}");
        
        sb.append("<br>");
        sb.append("<strong>V<sub>N</sub></strong> = {");
        for(String a : p.keySet()) sb.append(a).append(", ");
        sb.delete(sb.length()-2, sb.length()+1);
        sb.append("}");
        
        sb.append("<br>");
        sb.append("<strong>S</strong> = {");
        for(String a : p.get("S")) sb.append(a).append(", ");
        sb.delete(sb.length()-2, sb.length()+1);
        sb.append("}");
        
        sb.append("<br>");
        sb.append("<strong>P</strong> = {");
        Set<String> set = p.keySet();
        for(String s : set) {
            sb.append("<br>").append("&emsp;&emsp;").append(s).append(" -> ");
            for(String a : p.get(s)) sb.append(a).append(" |");
            sb.delete(sb.length()-2, sb.length()+1);
        }
        sb.append("<br>}");
        
        return sb.toString();
    }
}
