package com.test.technique.ritmx.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ILogService {
    List<LogLine> readLog(String logPath);

    Map<String, Set<String>> listServersForEachClient(List<LogLine> lines);

    Map<String, Set<String>> listClientsForEachServer(List<LogLine> lines);

    String getServerWithMostConnections(List<LogLine> lines);
}
