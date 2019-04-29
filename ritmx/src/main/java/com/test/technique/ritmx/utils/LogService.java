package com.test.technique.ritmx.utils;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LogService implements ILogService {

    /**
     * Parcourt la log dont les lignes suivent le format suivant : "timestamp unix" "hostname" "hostname"
     * @param logPath
     * @return
     */
    @Override
    public List<LogLine> readLog(String logPath) {
        List<LogLine> logLines = new ArrayList<LogLine>();
        try{
            File file = new File(logPath);
            InputStream is = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            logLines = br.lines().map(mapFromFileToLogLines).filter(line -> line != null).collect(Collectors.toList());
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return logLines ;
    }

    /**
     * Convertit les lignes de la log en objet
     */
    private Function<String, LogLine> mapFromFileToLogLines = (line) -> {
        String[] p = line.split(" ");
        LogLine item = new LogLine();
        if(p.length == 3) {
            item.setTimestamp(Integer.valueOf(p[0]));
            item.setClientHostname(p[1]);
            item.setServerHostname(p[2]);
            return item;
        }
        return null;
    };

    /**
     * Renvoie la liste des serveurs auxquels chaque client s'est connecté
     * @param lines
     * @return
     */
    @Override
    public Map<String, Set<String>> listServersForEachClient(List<LogLine> lines){
        Map<String, Set<String>> map = new HashMap<>();
        for(LogLine line : lines){
            if(!map.containsKey(line.getClientHostname())){
                map.put(line.getClientHostname(), new HashSet<>());
            }
            map.get(line.getClientHostname()).add(line.getServerHostname());
        }
        return map;
    }

    /**
     * Renvoie la liste des clients qui se sont connectés à chaque serveur
     * @param lines
     * @return
     */
    @Override
    public Map<String, Set<String>> listClientsForEachServer(List<LogLine> lines){
        Map<String, Set<String>> map = new HashMap<>();
        for(LogLine line : lines){
            if(!map.containsKey(line.getServerHostname())){
                map.put(line.getServerHostname(), new HashSet<>());
            }
            map.get(line.getServerHostname()).add(line.getClientHostname());
        }
        return map;
    }

    /**
     * Renvoie le nom du serveur auquel les clients se sont le plus connectés
     * @param lines
     * @return
     */
    @Override
    public String getServerWithMostConnections(List<LogLine> lines){
        String serverHostname = lines.stream()
                .filter(it -> Objects.nonNull(it.getServerHostname()))
                .collect(Collectors.groupingBy(LogLine::getServerHostname, Collectors.counting()))
                .entrySet().stream().max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey).orElse(null);
        return serverHostname;
    }



}
