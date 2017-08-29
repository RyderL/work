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
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base implementation of the Condition interface.
 * 
 * @author Anthony Eden
 */

public abstract class AbstractCondition implements Condition, Serializable {
	static final long serialVersionUID = -6563693584809960943L;

	private String value = "";
	private List<Xpression> xpressions;
	private ConditionType type = ConditionType.CONDITION;

	/** Construct a new AbstractCondition. */

	public AbstractCondition() {
		xpressions = new ArrayList<Xpression>();
	}

	/**
	 * Get the condition type.
	 * 
	 * @return The condition type
	 */

	public ConditionType getType() {
		return type;
	}

	/**
	 * Set the condition type.
	 * 
	 * @param type
	 *            The condition type
	 */

	public void setType(ConditionType type) {
		this.type = type;
	}

	/**
	 * Get the condition value. This value represents a conditional expression.
	 * 
	 * @return The value
	 */

	public String getValue() {
		return value;
	}

	/**
	 * Set the condition value.
	 * 
	 * @param value
	 *            The value
	 */

	public void setValue(String value) {
		this.value = value.trim();
	}

	/**
	 * Get a list of Xpressions.
	 * 
	 * @return A List of Xpressions
	 */

	public List<Xpression> getXpressions() {
		return xpressions;
	}

}
