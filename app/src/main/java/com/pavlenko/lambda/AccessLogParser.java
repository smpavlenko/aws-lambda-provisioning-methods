package com.pavlenko.lambda;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AccessLogParser {
    private static final Pattern PATTERN = Pattern.compile(
        "http[s]?://[^#&]*.site[^#&]*.net:[\\d]{2,3}/([^#&]+)[?&]sessionId=([^#&]+).*[?&]adSourceId=([^#&]+).*[?&]originalRequestTime=([\\d]+)(?:&errorCode=(\\d*))?");

    public Set<String> parse(final String accessLogFileText) {
        if ((accessLogFileText == null) || accessLogFileText.isEmpty()) {
            return Collections.emptySet();
        }

        final Set<String> result = new HashSet<>();
        final Matcher matcher = PATTERN.matcher(accessLogFileText);
        while (matcher.find()) {
            result.add(matcher.group(2));
        }
        return result;
    }
}
