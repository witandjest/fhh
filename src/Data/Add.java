/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Data;

import Parse.Trie;
import static Utils.StringCmds.round;
import static Utils.StringCmds.timeToFloat;
import java.util.List;
import java.util.Map;

/**s
 *
 * @author trevor.witjes
 */
public class Add { 
    private String timestamp;
    private GsonData [] skaterData;
    
    public void Gson(Map <String, Player> players, Trie lookup, String team){
        String [] stats;
        for (int i=0; i<skaterData.length; i++){
            // Create new player and populate stat values
            Player p = new Player();
            stats = skaterData[i].toString().split("\\,");
            for (int j=0; j<stats.length; j++){
                if (j!=2) stats[j] = stats[j].replaceAll("\\s", "");
            }
            
            if (stats[0].equals("null")) {
                p.jersey = 0;
            } else {
                p.jersey = Integer.parseInt(stats[0]);
            }
            p.team = team;
            p.position = stats[1];
            p.name = stats[2].substring(1);
//            p.gp = Integer.parseInt(stats[3]);
            p.goals = Integer.parseInt(stats[4]);
//            p.assists = Integer.parseInt(stats[5]);
//            p.points = Integer.parseInt(stats[6]);
//            p.plusminus = Integer.parseInt(stats[7]);
//            p.pim = Integer.parseInt(stats[8]);
            p.sog = Integer.parseInt(stats[9]);  
            p.toi = timeToFloat(stats[10]);
            
            
            List <String> temp = lookup.getWords(p.name.substring(3).toLowerCase());
                    
            for (int j=0; j<temp.size(); j++){
                if (!players.get(temp.get(j)).splits.isEmpty()) {  
                    String test = temp.get(j);
                    System.out.println(test);
                    for (int k=0; k<players.get(test).splits.size(); k++){
                        if (players.get(test).splits.get(k).matches(p)){
                            players.get(test).jersey = p.jersey;
                            players.get(test).position = p.position;
                            players.get(test).splits.get(k).toi = p.toi;
                        }
                    }
                }
            }
            
        }
    }
    
    public void Yahoo(String input, Map <String, Player> players, Trie lookup, String team){
        String [] data;
        String [] stats;
        data = input.split(";\">");
        
        for (int i=1; i<data.length; i++){
            Player p = new Player();
            p.team = team;
            p.name = data[i].substring(0, data[i].indexOf("<"));
            stats = data[i].split("title=\"");

            p.gp = Integer.parseInt(stats[1].substring((stats[1].indexOf(">")+1), stats[1].indexOf("<")));
            p.goals = Integer.parseInt(stats[2].substring((stats[2].indexOf(">")+1), stats[2].indexOf("<")));
            p.assists = Integer.parseInt(stats[3].substring((stats[3].indexOf(">")+1), stats[3].indexOf("<")));
            p.points = Integer.parseInt(stats[4].substring((stats[4].indexOf(">")+1), stats[4].indexOf("<")));
            p.plusminus = Integer.parseInt(stats[5].substring((stats[5].indexOf(">")+1), stats[5].indexOf("<")));
            p.pim = Integer.parseInt(stats[6].substring((stats[6].indexOf(">")+1), stats[6].indexOf("<")));
            p.hits = Integer.parseInt(stats[7].substring((stats[7].indexOf(">")+1), stats[7].indexOf("<")));
            p.blocks = Integer.parseInt(stats[8].substring((stats[8].indexOf(">")+1), stats[8].indexOf("<")));
            p.fow = Integer.parseInt(stats[9].substring((stats[9].indexOf(">")+1), stats[9].indexOf("<")));
            p.fol = Integer.parseInt(stats[10].substring((stats[10].indexOf(">")+1), stats[10].indexOf("<")));
            p.ppg = Integer.parseInt(stats[11].substring((stats[11].indexOf(">")+1), stats[11].indexOf("<")));
            p.ppa = Integer.parseInt(stats[12].substring((stats[12].indexOf(">")+1), stats[12].indexOf("<")));
            p.shg = Integer.parseInt(stats[13].substring((stats[13].indexOf(">")+1), stats[13].indexOf("<")));
            p.sha = Integer.parseInt(stats[14].substring((stats[14].indexOf(">")+1), stats[14].indexOf("<")));
            p.stp = p.ppg + p.ppa + p.shg + p.sha;
            p.gwg = Integer.parseInt(stats[15].substring((stats[15].indexOf(">")+1), stats[15].indexOf("<")));
            p.sog = Integer.parseInt(stats[16].substring((stats[16].indexOf(">")+1), stats[16].indexOf("<")));
            p.sht_pcnt = round(Float.parseFloat(stats[17].substring((stats[17].indexOf(">")+1), stats[17].indexOf("<")))*100, 2);
             
            // Add player to lookup
            if (lookup.getWords(p.name.toLowerCase()).isEmpty()){
                lookup.addWord(p.name.toLowerCase());
            } 
            
            // Add player to hashmap
            String key = p.name.toLowerCase(); //.replace(" ", "")
            if (players.get(key) != null){ // if player name exists, add to list
                Split s = new Split(team, p);
                players.get(key).addSplit(s);
            } else {
                players.put(key, p);
            }     
        }
    }
    
    @Override
    public String toString() {
        return timestamp + " (" + skaterData[0] + ")";
    }
}
