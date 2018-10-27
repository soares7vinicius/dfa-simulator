package simulator.control;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RegularGrammar {
    private final List<String> terminals;
    private Map<String, List<String>> p;
    
    private final String[] alphabet = 
            "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z".split(",");
    private List<String> combinations; /* S, A, B, ... Z, AA, AB, ... ZZ*/
    private final String epsilon = "\u03B5";
    
    public RegularGrammar(Automaton a) {
        this.makeCombinations();
        
        this.terminals = a.getAlphabet();
        this.makeRGFromAutomaton(a);
    }
    
    public RegularGrammar(List<String> l) {
        this.makeCombinations();
        
        this.terminals = this.getTerminals(l);
        this.makeRGFromFile(l);
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
    
    private void makeRGFromAutomaton(Automaton a) {
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
            l.add(this.epsilon);
        }
    }
    
    private void makeRGFromFile(List<String>l) {
        this.p = new LinkedHashMap<>();
        
        List<String> n = new ArrayList<>(Arrays.asList(l.get(1).substring(2).split(",")));
        for(String s : n) this.p.put(s, new ArrayList<>()); //adiciona as proposicoes no map
        
        for(int i = 2; i < l.size(); i++) { // para cada linha de cada proposicao
            List<String> line = new ArrayList<>(Arrays.asList(l.get(i).substring(2).split(",")));
            for(String j : line) {
                if(j.equals("null")) {
                    this.p.get(l.get(i).charAt(0)+"").add(this.epsilon);
                }else {
                    this.p.get(l.get(i).charAt(0)+"").add(j);
                }
            }
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
    
    private List<String> getTerminals(List<String> l) {
        return new ArrayList<>(Arrays.asList(l.get(0).substring(2).split(",")));
    }
    
    public String toAutomatonString() {
        StringBuilder sb = new StringBuilder();
        Set<String> finals = new LinkedHashSet<>();
        
        sb.append("<html>");
        for(String k : this.p.keySet()) {
            for(String s : this.p.get(k)) {
                if(s.length() == 1) finals.add(k); //epsilon ou terminais
                else sb.append(k).append(",").append(s.charAt(0)).append(",")
                        .append(s.charAt(1)).append("<br>");
            }   
        }
        sb.append("I=").append("S").append("<br>");
        sb.append("F=");
        for(String s : finals) {
            sb.append(s).append(",");
        }
        sb.delete(sb.length()-1, sb.length()+1);
        sb.append("<br>");
        
        return sb.toString();
    }
}
