package org.jxapi.generator.html;

import java.util.List;

import org.springframework.util.CollectionUtils;

import org.jxapi.util.CollectionUtil;

/**
 * Helper methods around HTML generation
 */
public class HtmlGenerationUtil {
	
	/**
	 * Indentation string used in HTML generation
	 */
	public static final String INDENTATION = "  ";

	private HtmlGenerationUtil() {}
	
	/**
	 * Generates a HTML table with given caption, header and rows
	 * @param caption HTML table <code>&lt;caption&gt;</code> content
	 * @param columns Column names nested in <code>&lt;th&gt;</code>
	 * @param cells List of rows containing values for cells
	 * @return HTML <code>&lt;table&gt;</code> code fragment.
	 */
	public static String generateTable(String caption, List<String> columns, List<List<String>> cells) {
		StringBuilder s = new StringBuilder();
		s.append("<table>\n");
		if (caption != null) {
		 s.append(INDENTATION)
		  .append("<caption>")
		  .append(caption)
		  .append("</caption>\n");
		}
		if (!CollectionUtil.isEmpty(columns)) {
			s.append(INDENTATION)
			 .append("<tr>\n");
			columns.forEach(c -> s.append(INDENTATION)
	   			    			  .append(INDENTATION)
	   			    			  .append("<th>")
								  .append(c)
								  .append("</th>\n"));
			s.append(INDENTATION)
			 .append("</tr>\n");
		}
		if (!CollectionUtils.isEmpty(cells)) {
			cells.forEach(row -> {
				s.append(INDENTATION)
				 .append("<tr>\n");
				if (!CollectionUtil.isEmpty(row)) {
					row.forEach(c -> s.append(INDENTATION)
			   			    .append(INDENTATION)
							.append("<td>")
							.append(c)
							.append("</td>\n"));
				}
				s.append(INDENTATION)
				 .append("</tr>\n");
			});
		}
		s.append("</table>\n");
		return s.toString();
	}

}
