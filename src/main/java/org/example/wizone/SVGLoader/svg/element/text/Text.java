package org.example.wizone.SVGLoader.svg.element.text;

import org.example.wizone.SVGLoader.svg.element.BaseElement;
import org.example.wizone.SVGLoader.svg.element.Element;
//-----------------------------------------------------------------------------

/**
 * SVG text elements. How handled yet -- added for completeness.
 * @author cambolbro
 */
public class Text extends BaseElement
{

	//-------------------------------------------------------------------------

	public Text()
	{
		super("Text");
	}

	//-------------------------------------------------------------------------

	@Override
	public Element newInstance()
	{
		return new Text();
	}

	//-------------------------------------------------------------------------

	@Override
	public boolean load(final String expr)
	{
		try
		{
			throw new Exception("SVG text loading not implemented yet.");
		} 
		catch (Exception e)
		{
			// ...
		}
		return false;
	}

	@Override
	public String getLabel() {
		return "";
	}

	//-------------------------------------------------------------------------

}
