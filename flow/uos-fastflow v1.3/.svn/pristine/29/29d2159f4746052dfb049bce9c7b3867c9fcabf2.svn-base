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

import java.util.List;

/**
 * Interface which represents a condition.
 * 
 * @author Anthony Eden
 */

public interface Condition {

	/**
	 * Return the condition type.
	 * 
	 * @return The condition type
	 */

	public ConditionType getType();

	/**
	 * Set the condition type.
	 * 
	 * @param type
	 *            The new condition type
	 */

	public void setType(ConditionType type);

	/**
	 * Return the value of the condition. The value is the actual condition
	 * logic. The language for the condition logic is defined by the runtime
	 * engine.
	 * 
	 * @return The value
	 */

	public String getValue();

	/**
	 * Set the condition logic.
	 * 
	 * @param value
	 *            The condition logic
	 */

	public void setValue(String value);

	/**
	 * Get a list of all expressions.
	 * 
	 * @return The expressions
	 */

	public List<Xpression> getXpressions();

}
