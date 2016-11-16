//
// Author: Yves Lafon <ylafon@w3.org>
//
// (c) COPYRIGHT MIT, ERCIM, Keio, Beihang, 2016.
// Please first read the full copyright statement in file COPYRIGHT.html
package org.w3c.css.properties.css;

import org.w3c.css.parser.CssStyle;
import org.w3c.css.properties.svg.SVGBasicStyle;
import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssExpression;
import org.w3c.css.values.CssValue;

/**
 * @since SVG
 */
public class CssGlyphOrientationHorizontal extends CssProperty {

	public CssValue value;

	/**
	 * Create a new CssGlyphOrientationHorizontal
	 */
	public CssGlyphOrientationHorizontal() {
	}

	/**
	 * Creates a new CssGlyphOrientationHorizontal
	 *
	 * @param expression The expression for this property
	 * @throws org.w3c.css.util.InvalidParamException
	 *          Expressions are incorrect
	 */
	public CssGlyphOrientationHorizontal(ApplContext ac, CssExpression expression, boolean check)
			throws InvalidParamException {
		throw new InvalidParamException("value",
				expression.getValue().toString(),
				getPropertyName(), ac);
	}

	public CssGlyphOrientationHorizontal(ApplContext ac, CssExpression expression)
			throws InvalidParamException {
		this(ac, expression, false);
	}

	/**
	 * Returns the value of this property
	 */
	public Object get() {
		return value;
	}


	/**
	 * Returns the name of this property
	 */
	public final String getPropertyName() {
		return "glyph-orientation-horizontal";
	}

	/**
	 * Returns true if this property is "softly" inherited
	 * e.g. his value is equals to inherit
	 */
	public boolean isSoftlyInherited() {
		return value.equals(inherit);
	}

	/**
	 * Returns a string representation of the object.
	 */
	public String toString() {
		return value.toString();
	}

	/**
	 * Add this property to the CssStyle.
	 *
	 * @param style The CssStyle
	 */
	public void addToStyle(ApplContext ac, CssStyle style) {
		SVGBasicStyle s = (SVGBasicStyle) style;
		if (s.cssGlyphOrientationHorizontal != null) {
			style.addRedefinitionWarning(ac, this);
		}
		s.cssGlyphOrientationHorizontal = this;
	}


	/**
	 * Compares two properties for equality.
	 *
	 * @param property The other property.
	 */
	public boolean equals(CssProperty property) {
		return (property instanceof CssGlyphOrientationHorizontal &&
				value.equals(((CssGlyphOrientationHorizontal) property).value));
	}


	/**
	 * Get this property in the style.
	 *
	 * @param style   The style where the property is
	 * @param resolve if true, resolve the style to find this property
	 */
	public CssProperty getPropertyInStyle(CssStyle style, boolean resolve) {
		if (resolve) {
			return ((SVGBasicStyle) style).getGlyphOrientationHorizontal();
		} else {
			return ((SVGBasicStyle) style).cssGlyphOrientationHorizontal;
		}
	}
}
