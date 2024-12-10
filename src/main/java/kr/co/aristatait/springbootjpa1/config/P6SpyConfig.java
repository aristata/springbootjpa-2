package kr.co.aristatait.springbootjpa1.config;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Configuration
public class P6SpyConfig implements MessageFormattingStrategy {

    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions
                .getActiveInstance()
                .setLogMessageFormat(this.getClass()
                                         .getName());
    }

    private static final Set<String> SQL_KEYWORDS;

    static {
        // 주요 SQL 키워드 (대문자로 지정)
        SQL_KEYWORDS = new HashSet<>();
        SQL_KEYWORDS.add("SELECT");
        SQL_KEYWORDS.add("FROM");
        SQL_KEYWORDS.add("WHERE");
        SQL_KEYWORDS.add("INSERT");
        SQL_KEYWORDS.add("UPDATE");
        SQL_KEYWORDS.add("DELETE");
        SQL_KEYWORDS.add("JOIN");
        SQL_KEYWORDS.add("LEFT");
        SQL_KEYWORDS.add("RIGHT");
        SQL_KEYWORDS.add("INNER");
        SQL_KEYWORDS.add("OUTER");
        SQL_KEYWORDS.add("GROUP");
        SQL_KEYWORDS.add("ORDER");
        SQL_KEYWORDS.add("HAVING");
        SQL_KEYWORDS.add("LIMIT");
    }

    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        return String.format(
                             "[Category: %s] | Execution Time: %d ms | Connection ID: %d | SQL:%s\n",
                category, elapsed, connectionId, formatSql(category, sql)
                     )
                     .trim() + "\n";
    }

    private String formatSql(String category, String sql) {
        if (sql != null && !sql.trim()
                               .isEmpty() && Category.STATEMENT.getName()
                                                               .equals(category)) {
            String trimmedSQL = sql.trim()
                                   .toLowerCase(Locale.ROOT);
            if (trimmedSQL.startsWith("create") || trimmedSQL.startsWith("alter") || trimmedSQL.startsWith("comment")) {
                sql = FormatStyle.DDL.getFormatter()
                                     .format(sql);
            } else {
                sql = FormatStyle.BASIC.getFormatter()
                                       .format(sql);
            }
            // 포맷팅 후 ANSI 색상 적용
            return applyAnsiColors(sql);
        }
        return sql == null ? "" : sql.trim();
    }

    private String applyAnsiColors(String sql) {
        for (String keyword : SQL_KEYWORDS) {
            sql = sql.replaceAll("(?i)\\b" + keyword + "\\b", "\u001B[34m" + keyword + "\u001B[0m");
        }
        return sql;
    }

}
