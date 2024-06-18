	package org.example.wizone.SVGLoader.svg;

    import org.example.wizone.SVGLoader.svg.element.BaseElement;
    import org.example.wizone.SVGLoader.svg.element.Element;

    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;
    import java.util.Collections;
    import java.util.Comparator;

	//-----------------------------------------------------------------------------

	/**
	 * Class for parsing SVG files.
	 * @author cambolbro
	 */
	public class SVGParser
	{
		private String fileName = "";

		private SVG svg = new SVG();

		//-------------------------------------------------------------------------

		public String fileName()
		{
			return fileName;
		}

		public SVG svg()
		{
			return svg;
		}

		//-------------------------------------------------------------------------

		/**
		 * Load and parse SVG content from the specified file.
		 * @param name
		 * @throws IOException
		 */
		@SuppressWarnings("resource")
		public void loadAndParse(final String name) throws Exception
		{
			fileName = new String(name);

			// Load the file
			BufferedReader reader;
			reader = new BufferedReader(new FileReader(fileName));

			// Read its content into a string
			StringBuilder content = new StringBuilder();
			String line = reader.readLine();
			while (line != null)
			{
				content.append(line);
				line = reader.readLine();
			}
			reader.close();

			//System.out.println();

			// Parse the content
			parse(content.toString());
		}

		//-------------------------------------------------------------------------

		/**
		 * Load the specified SVG content from the file with the given name.
		 * @param content
		 */
		public void parse(final String content) throws Exception {
			svg.clear();

			// Load SVG elements
			for (Element prototype : BaseElement.elementFactory().prototypes())
			{
				// Load all occurrences of this prototype
				final String label = prototype.label();

				int pos = 0;
				while (pos < content.length())
				{
					pos = content.indexOf("<"+label, pos);
					if (pos == -1)
						break;

					final int to = content.indexOf(">", pos);

					if (to == -1)
					{
						System.out.println("* Failed to close expression: " + content.substring(pos));
						break;
					}

					final String expr = content.substring(pos, to+1);
					final Element element = BaseElement.elementFactory().makeElement(label);
					element.load(expr);
					((BaseElement)element).setFilePos(pos);
					svg.elements().add(element);

					pos = to;
				}
			}

			sortElements();
		}

		//-------------------------------------------------------------------------

		/**
		 * Sort elements in order of occurrence.
		 */
		void sortElements()
		{
			Collections.sort
			(
				svg.elements(),
				new Comparator<Element>()
				{
					@Override
					public int compare(final Element a, final Element b)
					{
						final int filePosA = ((BaseElement)a).filePos();
						final int filePosB = ((BaseElement)b).filePos();

						if (filePosA < filePosB)
							return -1;
						if (filePosA > filePosB)
							return 1;
						return 0;
					}
				}
			);
		}

		//-------------------------------------------------------------------------

		/**
		 * @param ch
		 * @return Whether ch is possibly part of a numeric string.
		 */
		public static boolean isNumeric(final char ch)
		{
			return ch >= '0' && ch <= '9' || ch == '-' || ch == '.';
		}

		//-------------------------------------------------------------------------

		/**
		 * @param expr
		 * @param from
		 * @return Extract double from expression, else return null.
		 */
		public static Double extractDoubleAt(final String expr, final int from)
		{
			final StringBuilder sb = new StringBuilder();

			int c = from;
			char ch;
			while (c < expr.length())
			{
				ch = expr.charAt(c);
				if (!isNumeric(ch))
					break;
				sb.append(ch);
				c++;
			}

			Double result = null;
			try
			{
				result = Double.parseDouble(sb.toString());
			}
			catch (Exception e)
			{
				 // Do nothing...
			}
			return result;
		}

		//-------------------------------------------------------------------------

		/**
		 * @param expr
		 * @param heading
		 * @return Extract double from expression, else return null.
		 */
		public static Double[] extractDouble(final String expr, final String heading)
		{
			// Extract the substring enclosed y quotation marks
			final int pos    = expr.indexOf(heading);
			final int from   = expr.indexOf("\"", pos);     // opening quote
			final int to     = expr.indexOf("\"", from+1);  // closing quote
			final String sub = expr.substring(from+1, to);
			String[] subs = sub.split(" ");
			Double[] result = new Double[subs.length];
			try
			{
				for (int j = 0; j < subs.length; j++) {
					result[j] = Double.parseDouble(subs[j]);
				}
			}
			catch (Exception e)
			{
				 // Do nothing...
			}
			return result;
		}

		//-------------------------------------------------------------------------

		/**
		 * @param str
		 * @param pos
		 * @return String at the specified position, else null if none.
		 */
		public static String extractStringAt(final String str, final int pos)
		{
			if (str.charAt(pos) != '"')
				return null;  // not a string

			final StringBuilder sb = new StringBuilder();

			for (int c = pos+1; c < str.length() && str.charAt(c) != '"'; c++)
				sb.append(str.charAt(c));

			return sb.toString();
		}

		//-------------------------------------------------------------------------

		@Override
		public String toString()
		{

			return fileName + " has " +
					svg;
		}

		//-------------------------------------------------------------------------

	}
