package com.pslib.jtool.db.sqlformatter.languages;

import com.pslib.jtool.db.sqlformatter.core.DialectConfig;
import com.pslib.jtool.db.sqlformatter.core.FormatConfig;
import com.pslib.jtool.db.sqlformatter.core.Formatter;
import com.pslib.jtool.db.sqlformatter.core.Params;
import com.pslib.jtool.db.sqlformatter.core.Tokenizer;

import java.util.List;
import java.util.Map;

import static com.pslib.jtool.db.sqlformatter.core.FormatConfig.DEFAULT_INDENT;

public abstract class AbstractFormatter {

	abstract DialectConfig dialectConfig();

	/**
	 * Formats DB2 query to make it easier to read
	 *
	 * @param query query string
	 * @param cfg FormatConfig
	 * @return formatted string
	 */
	public String format(String query, FormatConfig cfg) {
		Tokenizer tokenizer = new Tokenizer(this.dialectConfig());
		return new Formatter(cfg, tokenizer).format(query);
	}

	public String format(String query, String indent, List<?> params) {
		return format(
						query,
						FormatConfig.builder()
										.indent(indent)
										.params(Params.Holder.of(params))
										.build());
	}
	public String format(String query, List<?> params) {
		return format(query, DEFAULT_INDENT, params);
	}

	public String format(String query, String indent, Map<String, ?> params) {
		return format(
						query,
						FormatConfig.builder()
										.indent(indent)
										.params(Params.Holder.of(params))
										.build());
	}
	public String format(String query, Map<String, ?> params) {
		return format(query, DEFAULT_INDENT, params);
	}

	public String format(String query, String indent) {
		return format(
						query,
						FormatConfig.builder()
										.indent(indent)
										.build());
	}
	public String format(String query) {
		return format(query, DEFAULT_INDENT);
	}


}
