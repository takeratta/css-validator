// $Id$
// Author: Yves Lafon <ylafon@w3.org>
//
// (c) COPYRIGHT MIT, ERCIM and Keio University, 2012.
// Please first read the full copyright statement in file COPYRIGHT.html
package org.w3c.css.properties.css3;

import org.w3c.css.util.ApplContext;
import org.w3c.css.util.InvalidParamException;
import org.w3c.css.values.CssExpression;
import org.w3c.css.values.CssFunction;
import org.w3c.css.values.CssIdent;
import org.w3c.css.values.CssTypes;
import org.w3c.css.values.CssValue;
import org.w3c.css.values.CssValueList;

import java.util.ArrayList;

import static org.w3c.css.values.CssOperator.SPACE;

/**
 * @spec http://www.w3.org/TR/2011/WD-css3-lists-20110524/#list-style
 */
public class CssListStyle extends org.w3c.css.properties.css.CssListStyle {

	/**
	 * Create a new CssListStyle
	 */
	public CssListStyle() {
		value = initial;
		cssListStyleType = new CssListStyleType();
		cssListStylePosition = new CssListStylePosition();
		cssListStyleImage = new CssListStyleImage();
	}


	/**
	 * Set the value of the property<br/>
	 * Does not check the number of values
	 *
	 * @param expression The expression for this property
	 * @throws org.w3c.css.util.InvalidParamException
	 *          The expression is incorrect
	 */
	public CssListStyle(ApplContext ac, CssExpression expression)
			throws InvalidParamException {
		this(ac, expression, false);
	}

	/**
	 * Set the value of the property
	 *
	 * @param expression The expression for this property
	 * @param check      set it to true to check the number of values
	 * @throws org.w3c.css.util.InvalidParamException
	 *          The expression is incorrect
	 */
	public CssListStyle(ApplContext ac, CssExpression expression,
						boolean check) throws InvalidParamException {
		if (check && expression.getCount() > 3) {
			throw new InvalidParamException("unrecognize", ac);
		}
		setByUser();

		CssValue val;
		char op;
		CssValue imageVal = null;
		CssValue positionVal = null;
		CssValue typeVal = null;
		int nbnone = 0;

		while (!expression.end()) {
			val = expression.getValue();
			op = expression.getOperator();

			switch (val.getType()) {
				case CssTypes.CSS_IMAGE:
				case CssTypes.CSS_URL:
					if (imageVal != null) {
						throw new InvalidParamException("value", val,
								getPropertyName(), ac);
					}
					imageVal = val;
					break;
				case CssTypes.CSS_FUNCTION:
					if (typeVal != null) {
						// TODO duplicate value error
						throw new InvalidParamException("value", none,
								getPropertyName(), ac);
					}
					typeVal = CssListStyleType.parseSymbolsFunction(ac, (CssFunction) val, this);
					break;
				case CssTypes.CSS_STRING:
					if (typeVal != null) {
						// TODO duplicate value error
						throw new InvalidParamException("value", none,
								getPropertyName(), ac);
					}
					typeVal = val;
					break;
				case CssTypes.CSS_IDENT:
					if (inherit.equals(val)) {
						if (expression.getCount() > 1) {
							throw new InvalidParamException("unrecognize", ac);
						}
						value = inherit;
						imageVal = inherit;
						positionVal = inherit;
						typeVal = inherit;
						break;
					}
					if (none.equals(val)) {
						nbnone++;
						break;
					}
					// now we go to other values...
					CssIdent id = (CssIdent) val;
					if (positionVal == null) {
						positionVal = CssListStylePosition.getAllowedIdent(id);
						if (positionVal != null) {
							break;
						}
					}
					if (typeVal == null) {
						typeVal = CssListStyleType.getAllowedIdent(id);
						if (typeVal != null) {
							// TODO check the @counter-style
							typeVal = val;
							break;
						} else {
							// it's still acceptable
							// but the name should be listed in a
							// @counter-style rule
							typeVal = val;
							break;
						}
					}
					// unrecognized ident.. fail!
				default:
					throw new InvalidParamException("value", val,
							getPropertyName(), ac);
			}
			if (op != SPACE) {
				throw new InvalidParamException("operator",
						((new Character(op)).toString()), ac);
			}
			expression.next();
		}
		// some postprocessing...
		if (nbnone > 0) {
			switch (nbnone) {
				case 1:
					// we set the value ot the non-specified by the shorthand
					// values...
					if (imageVal != null && typeVal != null) {
						// TODO duplicate value error
						throw new InvalidParamException("value", none,
								getPropertyName(), ac);
					}
					if (typeVal == null) {
						typeVal = none;
					}
					if (imageVal == null) {
						imageVal = none;
					}
					break;
				case 2:
					if (imageVal != null || typeVal != null) {
						// TODO duplicate value error
						throw new InvalidParamException("value", none,
								getPropertyName(), ac);
					}
					typeVal = none;
					imageVal = none;
					break;
				default:
					throw new InvalidParamException("value", none,
							getPropertyName(), ac);
			}
		}

		// set the value
		if (value != inherit) {
			ArrayList<CssValue> v = new ArrayList<CssValue>();
			if (typeVal != null) {
				v.add(typeVal);
			}
			if (positionVal != null) {
				v.add(positionVal);
			}
			if (imageVal != null) {
				if (nbnone != 1 || typeVal != none || imageVal != none) {
					v.add(imageVal);
				}
			}
			value = (v.size() == 1) ? v.get(0) : new CssValueList(v);
		}
		// then the shorthand values
		cssListStyleType = new CssListStyleType();
		cssListStyleType.value = typeVal;
		cssListStylePosition = new CssListStylePosition();
		cssListStylePosition.value = positionVal;
		cssListStyleImage = new CssListStyleImage();
		cssListStyleImage.value = imageVal;
	}
}
