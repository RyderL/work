/*--
 Copyright (C) 2002-2003 Anthony Eden.
 All rights reserved.
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows
    these conditions in the documentation and/or other materials
    provided with the distribution.
 3. The names "OBE" and "Open Business Engine" must not be used to
    endorse or promote products derived from this software without prior
    written permission.  For written permission, please contact
    me@anthonyeden.com.
 4. Products derived from this software may not be called "OBE" or
    "Open Business Engine", nor may "OBE" or "Open Business Engine"
    appear in their name, without prior written permission from
    Anthony Eden (me@anthonyeden.com).
 In addition, I request (but do not require) that you include in the
 end-user documentation provided with the redistribution and/or in the
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by
      Anthony Eden (http://www.anthonyeden.com/)."
 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR(S) BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
 IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 For more information on OBE, please see <http://www.openbusinessengine.org/>.
 */

package com.zterc.uos.fastflow.model.condition;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Anthony Eden
 * @author Adrian Price
 */
public final class ConditionType implements Serializable {
	static final long serialVersionUID = 8019004705357307670L;

	public static final int CONDITION_INT = 0;
	public static final int OTHERWISE_INT = 1;
	public static final int EXCEPTION_INT = 2;
	public static final int DEFAULTEXCEPTION_INT = 3;
	public static final ConditionType CONDITION = new ConditionType(CONDITION_INT);
	public static final ConditionType OTHERWISE = new ConditionType(OTHERWISE_INT);
	public static final ConditionType EXCEPTION = new ConditionType(EXCEPTION_INT);
	public static final ConditionType DEFAULTEXCEPTION = new ConditionType(DEFAULTEXCEPTION_INT);

	public static final String[] TAGS = { "CONDITION", "OTHERWISE", "EXCEPTION", "DEFAULTEXCEPTION" };
	public static final ConditionType[] VALUES = { CONDITION, OTHERWISE, EXCEPTION, DEFAULTEXCEPTION };
	private static final HashMap<String, ConditionType> tagMap = new HashMap<String, ConditionType>();

	private final int _value;

	static {
		for (int i = 0; i < TAGS.length; i++) {
			tagMap.put(TAGS[i], VALUES[i]);
		}
	}

	/**
	 * Convert the specified String to an ConditionType object. If there no
	 * matching ConditionType for the given String then this method returns
	 * null.
	 * 
	 * @param tag
	 *            The String
	 * @return The ConditionType object
	 */
	public static ConditionType fromString(String tag) {
		return (ConditionType) tagMap.get(tag);
	}

	/**
	 * Construct a new ConditionType instance.
	 * 
	 * @param value
	 *            The value
	 */
	private ConditionType(int value) {
		_value = value;
	}

	/**
	 * Get the value. This value can be used in switch statements in conjunction
	 * with the xxx_VALUE contants defined in this class.
	 * 
	 * @return An int value
	 */
	public int getValue() {
		return _value;
	}

	/**
	 * Return a string representation of the object.
	 * 
	 * @return The String representation
	 */
	public String toString() {
		return TAGS[_value];
	}

	public Object readResolve() {
		return VALUES[_value];
	}
}